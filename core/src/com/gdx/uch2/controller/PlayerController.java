package com.gdx.uch2.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.Player.State;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.PlayerState;
import com.gdx.uch2.networking.client.ClientPlayerStateTickManager;

import com.gdx.uch2.effects.Effect;

public class PlayerController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE;
    }

    private static final long LONG_JUMP_PRESS 	= 200;
    private static final float ACCELERATION 	= 20;
    public  static final float GRAVITY 			= -36f;
    private static final float MAX_JUMP_SPEED	= 10f;
    private static final float DAMP 			= 0.8f;
    private static final float MAX_VEL 			= 5.6f;
    private static final float MAX_FALL_VEL 	= -30;
    private static final float SLIDING_JUMP_RECOIL = 2f;
    private static final float SLIDING_FRICTION =  12f;
    private static final long SLIDING_JUMP_RECOIL_TIME = 0;

    private World 	world;
    private Player 	player;
    private long	jumpPressedTime;
    private boolean jumpingActive;
    private boolean jumpingPressed;
    private boolean grounded = false;
    private long recoilBeginTime;
    private boolean finished = false;

    // This is the rectangle pool used in collision detection
    // Good to avoid instantiation each frame
    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    static Map<Keys, Boolean> keys = new HashMap<PlayerController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.JUMP, false);
        keys.put(Keys.FIRE, false);
    };

    // Blocks that Player can collide with any given frame
    private Array<Block> collidable = new Array<Block>();

    public PlayerController(World world) {
        this.world = world;
        this.player = world.getPlayer();
    }

    // ** Key presses and touches **************** //

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void jumpPressed() {
        jumpingPressed = true;
        keys.get(keys.put(Keys.JUMP, true));
    }

    public void firePressed() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    public void leftReleased() {
        keys.get(keys.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        keys.get(keys.put(Keys.RIGHT, false));
    }

    public void jumpReleased() {
        keys.get(keys.put(Keys.JUMP, false));
        jumpingActive = false;
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    /** The main update method **/
    public void update(float delta) {
        if (!finished || player.getState() != State.DEAD) {
            // Processing the input - setting the states of Player
            processInput();
        }

        // Fix resume mess after long inactivity
        delta = Math.min(delta, 0.025f);

        // Setting initial vertical acceleration
        player.getAcceleration().y = GRAVITY;

        // Need to update player here to apply effects before other operations
        player.update(delta);

        if (player.getState() == State.SLIDING && player.getVelocity().y < 0) {
            player.getAcceleration().y += SLIDING_FRICTION;
        }

        // Convert acceleration to frame time
        player.getAcceleration().scl(delta);

        // apply acceleration to change velocity
        player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);

        // checking collisions with the surrounding blocks depending on Player's velocity
        checkCollisionWithBlocks(delta);

        // If Player is grounded then reset the state to IDLE
        if (grounded && (player.getState().equals(State.JUMPING) || player.getState() == State.SLIDING)) {
            player.setState(State.IDLE);
        }

        // apply damping to halt Player nicely
        if (player.getState() == State.IDLE) {
            player.getVelocity().x *= DAMP;
        }

        // ensure terminal velocity is not exceeded
        if (player.getVelocity().x > MAX_VEL) {
            player.getVelocity().x = MAX_VEL;
        }
        if (player.getVelocity().x < -MAX_VEL) {
            player.getVelocity().x = -MAX_VEL;
        }

        if (player.getVelocity().y < MAX_FALL_VEL) {
            player.getVelocity().y = MAX_FALL_VEL;
        }

        ClientPlayerStateTickManager.getInstance().setCurrentState(new PlayerState(1,
                player.getPosition().x, player.getPosition().y, System.nanoTime()));
        // simply updates the state time
        player.update(delta);
    }

    private void finish() {
        finished = true;
        player.setState(State.IDLE);
    }

    /** Collision checking **/
    private void checkCollisionWithBlocks(float delta) {
        // scale velocity to frame units
        player.getVelocity().scl(delta);

        // Obtain the rectangle from the pool instead of instantiating it
        Rectangle playerRect = rectPool.obtain();
        // set the rectangle to player's bounding box
        playerRect.set(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

        // Check victory
        if (playerRect.overlaps(world.getLevel().getFinishBlocks().getBounds())) {
            finish();
        }

        // we first check the movement on the horizontal X axis
        int startX, endX;
        int startY = (int) player.getBounds().y;
        int endY = (int) (player.getBounds().y + player.getBounds().height);
        // if Player is heading left then we check if he collides with the block on his left
        // we check the block on his right otherwise
        if (player.getVelocity().x < 0) {
            startX = endX = (int) Math.floor(player.getBounds().x + player.getVelocity().x);
        } else {
            startX = endX = (int) Math.floor(player.getBounds().x + player.getBounds().width + player.getVelocity().x);
        }

        // get the block(s) player can collide with
        populateCollidableBlocks(startX, startY, endX, endY);

        // simulate player's movement on the X
        playerRect.x += player.getVelocity().x;

        // clear collision boxes in world
        world.getCollisionRects().clear();


        // if player collides, make his horizontal velocity 0
        for (Block block : collidable) {
            if (block == null) continue;
            if (playerRect.overlaps(block.getBounds())) {
                jumpingActive = false;
                if(!grounded) {
                    player.setState(State.SLIDING);
                }

                // Apply block action if any
                block.action(player);

                world.getCollisionRects().add(block.getBounds());

                // Fix oscillating state at colliding
                if (player.getVelocity().x < 0) {
                    player.translate(new Vector2(block.getBounds().x + block.getBounds().width - player.getBounds().x, 0));
                } else if (player.getVelocity().x > 0){
                    player.translate(new Vector2(block.getBounds().x - player.getBounds().x - player.getBounds().width, 0));
                }
            }
        }

        // reset the x position of the collision box
        playerRect.x = player.getBounds().x;

        // the same thing but on the vertical Y axis
        startX = (int) player.getBounds().x;
        endX = (int) (player.getBounds().x + player.getBounds().width);
        if (player.getVelocity().y <= 0) {
            startY = endY = (int) Math.floor(player.getBounds().y + player.getVelocity().y);
        } else {
            startY = endY = (int) Math.floor(player.getBounds().y + player.getBounds().height + player.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        playerRect.y += player.getVelocity().y;

        State backup = player.getState();
        if (player.getState() != State.SLIDING)
            player.setState(State.JUMPING);
        grounded = false;
        for (Block block : collidable) {
            if (block == null) continue;
            if (playerRect.overlaps(block.getBounds())) {
                jumpingActive = false;
                if (player.getVelocity().y < 0) {
                    // Fix oscillating state at landing
                    player.translate(new Vector2(0, block.getBounds().y + block.getBounds().height - player.getBounds().y));

                    player.setState(backup);
                    grounded = true;
                }

                player.getVelocity().y = 0;

                // Apply block action if any
                block.action(player);

                world.getCollisionRects().add(block.getBounds());
            }
        }
        // reset the collision box's position on Y
        playerRect.y = player.getBounds().y;

        // update Player's position

        // player.getPosition().add(player.getVelocity());
        // player.getBounds().x = player.getPosition().x;
        // player.getBounds().y = player.getPosition().y;

        player.translate(player.getVelocity());

        // un-scale velocity (not in frame time)
        player.getVelocity().scl(1 / delta);

    }

    /** populate the collidable array with the blocks found in the enclosing coordinates **/
    private void populateCollidableBlocks(int startX, int startY, int endX, int endY) {
        collidable.clear();
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (x >= 0 && x < world.getLevel().getWidth() && y >=0 && y < world.getLevel().getHeight()) {
                    collidable.add(world.getLevel().get(x, y));
                }
            }
        }
    }

    /** Change Player's state and parameters based on input controls **/
    private boolean processInput() {
        if (keys.get(Keys.JUMP)) {
            if (jumpingPressed && (!player.getState().equals(State.JUMPING))) {
                jumpingActive = true;
                jumpingPressed = false;
                jumpPressedTime = System.currentTimeMillis();

                player.getVelocity().y = MAX_JUMP_SPEED;
                grounded = false;

                if (player.getState() == State.SLIDING) {
                    recoilBeginTime = System.currentTimeMillis();
                    player.getAcceleration().x = 0;

                    if (player.isFacingLeft()) {
                        if (!keys.get(Keys.LEFT)) {
                            player.setFacingLeft(false);
                        }
                        player.getVelocity().x = MAX_JUMP_SPEED;
                    } else {
                        if (!keys.get(Keys.RIGHT)) {
                            player.setFacingLeft(true);
                        }
                        player.getVelocity().x = -MAX_JUMP_SPEED;
                    }
                }

                player.setState(State.JUMPING);
            } else if (jumpingActive){
                if ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS) {
                    jumpingActive = false;
                } else {
                    player.getVelocity().y = MAX_JUMP_SPEED;
                }
            }
        }

        float tmp;
        boolean flag = player.isFacingLeft();
        if (keys.get(Keys.LEFT)) {
            // left is pressed
            player.setFacingLeft(true);
            if (!player.getState().equals(State.JUMPING)) {
                player.setState(State.WALKING);
            }
            tmp = -ACCELERATION;
        } else if (keys.get(Keys.RIGHT)) {
            // left is pressed
            player.setFacingLeft(false);
            if (!player.getState().equals(State.JUMPING)) {
                player.setState(State.WALKING);

            }
            tmp = ACCELERATION;
        } else {
            if (!player.getState().equals(State.JUMPING) && player.getState() != State.SLIDING) {
                player.setState(State.IDLE);
            }
            tmp = 0;

        }

        if (System.currentTimeMillis() - recoilBeginTime >= SLIDING_JUMP_RECOIL_TIME
         || flag != player.isFacingLeft()) {
            player.getAcceleration().x = tmp;
        }

        return false;
    }

    public boolean isDone() {
        return finished;
    }

}
