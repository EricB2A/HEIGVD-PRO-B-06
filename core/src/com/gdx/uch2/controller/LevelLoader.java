package com.gdx.uch2.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Level;

import java.util.Arrays;

public class LevelLoader {

    private static final String LEVEL_PREFIX    = "levels/level-";

    private static final int    BOX             = 0x000000; // black
    private static final int    BLOCK           = 0x111111; // black
    private static final int    G_UP            = 0x222222; // black
    private static final int    G_DOWN          = 0x333333; // black
    private static final int    EMPTY           = 0xffffff; // white
    private static final int    START_POS       = 0x00ff00; // green
    private static final int    FINISH_POS      = 0xff0000; // red

    public static Level loadLevel(int number) {
        Level level = new Level();

        // Loading the png into a Pixmap
        Pixmap pixmap = new Pixmap(Gdx.files.internal(LEVEL_PREFIX + number + ".png"));

        // setting the size of the level based on the size of the pixmap
        level.setWidth(pixmap.getWidth());
        level.setHeight(pixmap.getHeight());

        // creating the backing blocks array
        Block[][] blocks = new Block[level.getWidth()][level.getHeight()];
        for (int col = 0; col < level.getWidth(); col++) {
            for (int row = 0; row < level.getHeight(); row++) {
                blocks[col][row] = null;
            }
        }

        for (int row = 0; row < level.getHeight(); row++) {
            for (int col = 0; col < level.getWidth(); col++) {
                int pixel = (pixmap.getPixel(col, row) >>> 8) & 0xffffff;
                int iRow = level.getHeight() - 1 - row;
                if (pixel == BOX) {
                    blocks[col][iRow] = new Block(new Vector2(col, iRow), Block.Type.BOX);
                } else if (pixel == BLOCK) {
                    blocks[col][iRow] = new Block(new Vector2(col, iRow), Block.Type.BLOCK);
                } else if (pixel == G_UP) {
                    blocks[col][iRow] = new Block(new Vector2(col, iRow), Block.Type.G_UP);
                } else if (pixel == G_DOWN) {
                    blocks[col][iRow] = new Block(new Vector2(col, iRow), Block.Type.G_DOWN);
                } else if (pixel == START_POS) {
                    level.setSpanPosition(new Vector2(col, iRow));
                } else if (pixel == FINISH_POS) {
                    level.setFinishPosition(new Vector2(col, iRow));
                }
            }
        }

        // setting the blocks
        level.setBlocks(blocks);
        return level;
    }

}