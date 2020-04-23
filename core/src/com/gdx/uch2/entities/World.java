package com.gdx.uch2.entities;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.uch2.controller.LevelLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class World {

    /** The collision boxes **/
    Array<Rectangle> collisionRects = new Array<Rectangle>();
//    /** The blocks making up the world **/
//    Array<Block> blocks = new Array<Block>();
    /** Our player controlled hero **/
    Player player;

    Level level;

    public static World currentWorld;

    // Getters -----------
//    public Array<Block> getBlocks() {
//        return blocks;
//    }
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

    /** Return only the blocks that need to be drawn **/
    public List<Block> getDrawableBlocks(int width, int height) {
//        List<Block> blocks = new LinkedList<>();
//        for (int col = 0; col < level.getWidth(); ++col) {
//            for (int row = 0; row < level.getHeight(); ++row) {
//                Block block = level.getBlocks()[col][row];
//                if (block != null)
//                    blocks.add(block);
//            }
//        }
//
//        return blocks;
        int x = (int)player.getBounds().x - width;
        int y = (int)player.getBounds().y - height;
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

    // --------------------
    public World() {
        createWorld();
    }

    private void createWorld() {
        level = LevelLoader.loadLevel(1);
        resetPlayer();
    }

    public void resetPlayer() {
        player = new Player(new Vector2(level.getSpanPosition()));
    }



//    private void createDemoWorld() {
//        player = new Player(new Vector2(7, 2));
//
//        for (int i = 0; i < 30; i++) {
//            blocks.add(new Block(new Vector2(i, 0)));
//            blocks.add(new Block(new Vector2(i, 14)));
//            if (i < 15) {
//                blocks.add(new Block(new Vector2(0, i)));
//                blocks.add(new Block(new Vector2(29, i)));
//            }
//
//            if (i >= 3 && i <= 6) {
//                blocks.add(new Block(new Vector2(8, i)));
//            }
//
//            if (i >= 5 && i <= 8) {
//                blocks.add(new Block(new Vector2(i, 3)));
//            }
//
//            if (i >= 13 && i <= 18) {
//                blocks.add(new Block(new Vector2(i, 6)));
//            }
//
//            if (i >= 23 && i <= 27) {
//                blocks.add(new Block(new Vector2(i, 8)));
//            }
//        }
//    }
}