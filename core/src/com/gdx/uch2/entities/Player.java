package com.gdx.uch2.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.gdx.uch2.util.Assets;
import com.gdx.uch2.util.Constants;

public class Player {
    Vector2 position;
    Vector2 lastFramePosition;
    Vector2 velocity;

    Facing facing;
    JumpState jumpState;
    WalkState walkState;


    long jumpStartTime;

    long walkStartTime;

    public Player() {
        position = new Vector2(50, 50);
        lastFramePosition = new Vector2(position);
        velocity = new Vector2();
        jumpState = JumpState.FALLING;
        facing = Facing.RIGHT;
        walkState = WalkState.STANDING;
    }

    public void update(float delta) {
        lastFramePosition.set(position);
        velocity.y -= Constants.GRAVITY;
        position.mulAdd(velocity, delta);

        if (jumpState != JumpState.JUMPING) {
            jumpState = JumpState.FALLING;

            if (position.y - Constants.PLAYER_EYE_HEIGHT < 0) {
                jumpState = JumpState.GROUNDED;
                position.y = Constants.PLAYER_EYE_HEIGHT;
                velocity.y = 0;
            }

        }

        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            moveLeft(delta);
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            moveRight(delta);
        } else {
            walkState = WalkState.STANDING;
        }

        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
            switch (jumpState) {
                case GROUNDED:
                    startJump();
                    break;
                case JUMPING:
                    continueJump();
            }
        } else {
            endJump();
        }
    }

    private void moveLeft(float delta) {
        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }
        walkState = WalkState.WALKING;
        facing = Facing.LEFT;
        position.x -= delta * Constants.PLAYER_MOVE_SPEED;
    }

    private void moveRight(float delta) {
        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }
        walkState = WalkState.WALKING;
        facing = Facing.RIGHT;
        position.x += delta * Constants.PLAYER_MOVE_SPEED;
    }

    private void startJump() {
        jumpState = JumpState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump();
    }

    private void continueJump() {
        if (jumpState == JumpState.JUMPING) {
            float jumpDuration = MathUtils.nanoToSec * (TimeUtils.nanoTime() - jumpStartTime);
            if (jumpDuration < Constants.MAX_JUMP_DURATION) {
                velocity.y = Constants.JUMP_SPEED;
            } else {
                endJump();
            }
        }
    }

    private void endJump() {
        if (jumpState == JumpState.JUMPING) {
            jumpState = JumpState.FALLING;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion region = Assets.instance.player1.standingRight;

        if (facing == Facing.RIGHT && jumpState != JumpState.GROUNDED) {
            //region = Assets.instance.player1.jumpingRight;
        } else if (facing == Facing.RIGHT && walkState == WalkState.STANDING) {
            float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
            region = (TextureRegion) Assets.instance.player1.standingRightAnimation.getKeyFrame(walkTimeSeconds);
        } else if (facing == Facing.RIGHT && walkState == WalkState.WALKING) {
            float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
            region = (TextureRegion) Assets.instance.player1.walkingRightAnimation.getKeyFrame(walkTimeSeconds);
        } else if (facing == Facing.LEFT && jumpState != JumpState.GROUNDED) {
            //region = Assets.instance.player1.jumpingLeft;
        } else if (facing == Facing.LEFT && walkState == WalkState.STANDING) {
            //region = Assets.instance.player1.standingLeft;
        } else if (facing == Facing.LEFT && walkState == WalkState.WALKING) {
            /*float walkTimeSeconds = MathUtils.nanoToSec * (TimeUtils.nanoTime() - walkStartTime);
            region = Assets.instance.player1.walkingLeftAnimation.getKeyFrame(walkTimeSeconds);*/
        }

        batch.draw(
                region.getTexture(),
                position.x - Constants.PLAYER_EYE_POSITION.x,
                position.y - Constants.PLAYER_EYE_POSITION.y,
                0,
                0,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1,
                1,
                0,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    enum JumpState {
        JUMPING,
        FALLING,
        GROUNDED
    }

    enum Facing {
        LEFT,
        RIGHT
    }

    enum WalkState {
        STANDING,
        WALKING
    }
}
