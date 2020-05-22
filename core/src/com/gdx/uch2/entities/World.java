package com.gdx.uch2.entities;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.controller.LevelLoader;
import com.gdx.uch2.ui.GameScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class World {

    /**
     * The collision boxes
     **/
    Array<Rectangle> collisionRects = new Array<Rectangle>();
//    /** The blocks making up the world **/
//    Array<Block> blocks = new Array<Block>();

    /**
     * Our player controlled hero
     **/
    Player player;

    Level level;

    public static World currentWorld = new World(1); //TODO 1 hardcodé

    // Getters -----------
    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
    }
    // --------------------

    /**
     * Return only the blocks that need to be drawn
     **/
    public List<Block> getDrawableBlocks(int width, int height) {
        int x = (int) player.getBounds().x - width;
        int y = (int) player.getBounds().y - height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int x2 = x + 2 * width;
        int y2 = y + 2 * height;
        if (x2 > level.getWidth()) {
            x2 = level.getWidth() - 1;
        }
        if (y2 > level.getHeight()) {
            y2 = level.getHeight() - 1;
        }

        List<Block> blocks = new ArrayList<Block>();
        Block block;
        for (int col = x; col <= x2; col++) {
            for (int row = y; row <= y2; row++) {
                block = level.getBlocks()[col][row];
                if (block != null) {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public World(int noLevel) {
        createWorld(noLevel);
    }

    public void placeBlock(Block b){
        level.getBlocks()[(int) b.getPosition().x][(int) b.getPosition().y] = b;
    }

    private void createWorld(int noLevel) {
        level = LevelLoader.loadLevel(noLevel);
        resetPlayer();
    }

    public void resetPlayer() {
        player = new Player(new Vector2(level.getSpanPosition()));
    }
}