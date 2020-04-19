package com.gdx.uch2.controller;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.Player.State;
import com.gdx.uch2.entities.World;

public class PlayerController {

    enum Keys {
        LEFT, RIGHT, JUMP, FIRE;
    }

    private static final long LONG_JUMP_PRESS 	= 200;
    private static final float ACCELERATION 	= 40;
    private static final float GRAVITY 			= -40f;
    private static final float MAX_JUMP_SPEED	= 10f;
    private static final float DAMP 			= 0.9f;
    private static final float MAX_VEL 			= 8f;
    private static final float SLIDING_JUMP_RECOIL = 8f;
    private static final long SLIDING_JUMP_RECOIL_TIME = 250;

    private World 	world;
    private Player 	player;
    private long	jumpPressedTime;
    private boolean jumpingPressed;
    private boolean grounded = false;
    private boolean sliding = false;
    private long recoilBeginTime;

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
        jumpingPressed = false;
    }

    public void fireReleased() {
        keys.get(keys.put(Keys.FIRE, false));
    }

    /** The main update method **/
    public void update(float delta) {
        // Processing the input - setting the states of Player
        processInput();

        // If Player is grounded then reset the state to IDLE
        if (grounded && player.getState().equals(State.JUMPING)) {
            player.setState(State.IDLE);
        }

        // Setting initial vertical acceleration
        player.getAcceleration().y = GRAVITY;

        // Convert acceleration to frame time
        player.getAcceleration().scl(delta);

        // apply acceleration to change velocity
        player.getVelocity().add(player.getAcceleration().x, player.getAcceleration().y);

        // checking collisions with the surrounding blocks depending on Player's velocity
        checkCollisionWithBlocks(delta);

        // apply damping to halt Player nicely
        player.getVelocity().x *= DAMP;

        // ensure terminal velocity is not exceeded
        if (player.getVelocity().x > MAX_VEL) {
            player.getVelocity().x = MAX_VEL;
        }
        if (player.getVelocity().x < -MAX_VEL) {
            player.getVelocity().x = -MAX_VEL;
        }

        // simply updates the state time
        player.update(delta);

    }

    /** Collision checking **/
    private void checkCollisionWithBlocks(float delta) {
        // scale velocity to frame units
        player.getVelocity().scl(delta);

        // Obtain the rectangle from the pool instead of instantiating it
        Rectangle playerRect = rectPool.obtain();
        // set the rectangle to player's bounding box
        playerRect.set(player.getBounds().x, player.getBounds().y, player.getBounds().width, player.getBounds().height);

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

        sliding = false;
        // if player collides, make his horizontal velocity 0
        for (Block block : collidable) {
            if (block == null) continue;
            if (playerRect.overlaps(block.getBounds())) {
                if(!grounded) {
                    sliding = true;
                }
                player.getVelocity().x = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }

        // reset the x position of the collision box
        playerRect.x = player.getPosition().x;

        // the same thing but on the vertical Y axis
        startX = (int) player.getBounds().x;
        endX = (int) (player.getBounds().x + player.getBounds().width);
        if (player.getVelocity().y < 0) {
            startY = endY = (int) Math.floor(player.getBounds().y + player.getVelocity().y);
        } else {
            startY = endY = (int) Math.floor(player.getBounds().y + player.getBounds().height + player.getVelocity().y);
        }

        populateCollidableBlocks(startX, startY, endX, endY);

        playerRect.y += player.getVelocity().y;

        for (Block block : collidable) {
            if (block == null) continue;
            if (playerRect.overlaps(block.getBounds())) {
                if (player.getVelocity().y < 0) {
                    grounded = true;
                    sliding = false;
                }
                player.getVelocity().y = 0;
                world.getCollisionRects().add(block.getBounds());
                break;
            }
        }
        // reset the collision box's position on Y
        playerRect.y = player.getPosition().y;

        // update Player's position
        player.getPosition().add(player.getVelocity());
        player.getBounds().x = player.getPosition().x;
        player.getBounds().y = player.getPosition().y;

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
            if (!player.getState().equals(State.JUMPING) || sliding) {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                player.setState(State.JUMPING);
                player.getVelocity().y = MAX_JUMP_SPEED;
                grounded = false;
                if (sliding) {
                    sliding = false;
                    recoilBeginTime = System.currentTimeMillis();
                    player.getAcceleration().x = 0;
                    if (player.isFacingLeft()) {
                        player.getVelocity().x = SLIDING_JUMP_RECOIL;
                    } else {
                        player.getVelocity().x = -SLIDING_JUMP_RECOIL;
                    }
                }
            } else {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS)) {
                    jumpingPressed = false;
                } else {
                    if (jumpingPressed) {
                        player.getVelocity().y = MAX_JUMP_SPEED;
                    }
                }
            }
        }

        float tmp;
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
            if (!player.getState().equals(State.JUMPING)) {
                player.setState(State.IDLE);
            }
            tmp = 0;

        }

        if (System.currentTimeMillis() - recoilBeginTime >= SLIDING_JUMP_RECOIL_TIME) {
            player.getAcceleration().x = tmp;
        }

        return false;
    }

}
