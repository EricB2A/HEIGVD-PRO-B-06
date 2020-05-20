package com.gdx.uch2.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {
    public static final Color BACKGROUND_COLOR = Color.SKY;

    public static final String PLAYER_1_ATLAS = "player.atlas";
    public static final String BLOCKS_ATLAS = "levels/blocks.atlas";
    public static final String OPPONENTS_ATLAS = "opponents.atlas";

    public static final float WORLD_SIZE = 128;

    public static final float LOOP_SPEED = .10f;

    public static final Vector2 PLAYER_EYE_POSITION = new Vector2(10, 10);
    public static final float PLAYER_EYE_HEIGHT = 16.0f;

    public static final float PLAYER_MOVE_SPEED = WORLD_SIZE / 2;

    public static final float JUMP_SPEED = 1.5f * WORLD_SIZE;
    public static final float MAX_JUMP_DURATION = .1f;

    public static final float GRAVITY = WORLD_SIZE / 10;
}
