package com.gdx.uch2.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.gdx.uch2.util.Assets;
import com.gdx.uch2.util.Constants;

public class Player {
    public enum State {
        IDLE, WALKING, JUMPING, SLIDING
    }

    public static final float SPEED = 6f;	// unit per second
    public static final float JUMP_VELOCITY = 4f;
    public static final float SIZE = 2f;

    Vector2 	position = new Vector2();
    Vector2 	acceleration = new Vector2();
    Vector2 	velocity = new Vector2();
    Rectangle 	bounds = new Rectangle();
    State		state = State.IDLE;
    boolean		facingLeft = true;
    float		stateTime = 0;
    boolean		longJump = false;

    public Player(Vector2 position) {
        this.position = position;
        this.bounds.height = SIZE / 1.1f;
        this.bounds.width = SIZE / 2.3f;
    }


    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public float getStateTime() {
        return stateTime;
    }


    public boolean isLongJump() {
        return longJump;
    }


    public void setLongJump(boolean longJump) {
        this.longJump = longJump;
    }


    public void setPosition(Vector2 position) {
        this.position = position;
        this.bounds.setX(position.x);
        this.bounds.setY(position.y);
    }


    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }


    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }


    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }


    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }


    public void update(float delta) {
//		position.add(velocity.tmp().mul(delta));
//		bounds.x = position.x;
//		bounds.y = position.y;
        stateTime += delta;
    }


//    Vector2 position;
//    Vector2 lastFramePosition;
//    Vector2 velocity;
//
//    Facing facing;
//    JumpState jumpState;
//    WalkState walkState;
//
//
//    long jumpStartTime;
//
//    long walkStartTime;
//
//    public Player(Vector2 position) {
//        this.position = position;
//        lastFramePosition = new Vector2(position);
//        velocity = new Vector2();
//        jumpState = JumpState.FALLING;
//        facing = Facing.RIGHT;
//        walkState = WalkState.STANDING;
//    }
//
//    public void update(float delta) {
//        lastFramePosition.set(position);
//        velocity.y -= Constants.GRAVITY;
//        position.mulAdd(velocity, delta);
//
//        if (jumpState != JumpState.JUMPING) {
//            jumpState = JumpState.FALLING;
//
//            if (position.y - Constants.PLAYER_EYE_HEIGHT < 0) {
//                jumpState = JumpState.GROUNDED;
//                position.y = Constants.PLAYER_EYE_HEIGHT;
//                velocity.y = 0;
//            }
//
//        }
//
//        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
//            moveLeft(delta);
//        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
//            moveRight(delta);
//        } else {
//            walkState = WalkState.STANDING;
//        }
//
//        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
//            switch (jumpState) {
//                case GROUNDED:
//                    startJump();
//                    break;
//                case JUMPING:
//                    continueJump();
//            }
//        } else {
//            endJump();
//        }
//    }
//
//    private void moveLeft(float delta) {
//        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
//            walkStartTime = TimeUtils.nanoTime();
//        }
//        walkState = WalkState.WALKING;
//        facing = Facing.LEFT;
//        position.x -= delta * Constants.PLAYER_MOVE_SPEED;
//    }
//
//    private void moveRight(float delta) {
//        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
//            walkStartTime = TimeUtils.nanoTime();
//        }
//        walkState = WalkState.WALKING;
//        facing = Facing.RIGHT;
//        position.x += delta * Constants.PLAYER_MOVE_SPEED;
//    }
//
//    private void startJump() {
//        jumpState = JumpState.JUMPING;
//        jumpStartTime = TimeUtils.nanoTime();
//        continueJump();
//    }
//
//    private void continueJump() {
//        if (jumpState == JumpState.JUMPING) {
//            float jumpDuration = MathUtils.nanoToSec * (TimeUtils.nanoTime() - jumpStartTime);
//            if (jumpDuration < Constants.MAX_JUMP_DURATION) {
//                velocity.y = Constants.JUMP_SPEED;
//            } else {
//                endJump();
//            }
//        }
//    }
//
//    private void endJump() {
//        if (jumpState == JumpState.JUMPING) {
//            jumpState = JumpState.FALLING;
//        }
//    }
//
//    public void render(SpriteBatch batch) {
//        TextureRegion region = Assets.instance.player1.standingRight;
//
//        if (facing == Facing.RIGHT && jumpState != JumpState.GROUNDED) {
//            //region = Assets.instance.player1.jumpingRight;
//        } else if (facing == Facing.RIGHT && walkState == WalkState.STANDING) {
//            float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
//            region = (TextureRegion) Assets.instance.player1.standingRightAnimation.getKeyFrame(walkTimeSeconds);
//        } else if (facing == Facing.RIGHT && walkState == WalkState.WALKING) {
//            float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
//            region = (TextureRegion) Assets.instance.player1.walkingRightAnimation.getKeyFrame(walkTimeSeconds);
//        } else if (facing == Facing.LEFT && jumpState != JumpState.GROUNDED) {
//            //region = Assets.instance.player1.jumpingLeft;
//        } else if (facing == Facing.LEFT && walkState == WalkState.STANDING) {
//            //region = Assets.instance.player1.standingLeft;
//        } else if (facing == Facing.LEFT && walkState == WalkState.WALKING) {
//            /*float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
//            region = Assets.instance.player1.walkingLeftAnimation.getKeyFrame(walkTimeSeconds);*/
//        }
//
//        batch.draw(
//                region.getTexture(),
//                position.x - Constants.PLAYER_EYE_POSITION.x,
//                position.y - Constants.PLAYER_EYE_POSITION.y,
//                0,
//                0,
//                region.getRegionWidth(),
//                region.getRegionHeight(),
//                1,
//                1,
//                0,
//                region.getRegionX(),
//                region.getRegionY(),
//                region.getRegionWidth(),
//                region.getRegionHeight(),
//                false,
//                false);
//    }
//
//    enum JumpState {
//        JUMPING,
//        FALLING,
//        GROUNDED
//    }
//
//    enum Facing {
//        LEFT,
//        RIGHT
//    }
//
//    enum WalkState {
//        STANDING,
//        WALKING
//    }
}
