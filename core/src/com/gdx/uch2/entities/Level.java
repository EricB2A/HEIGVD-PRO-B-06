package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;

public class Level {

    private int width;
    private int height;
    private Block[][] blocks;
    private Vector2 spanPosition;
    private Vector2 finishPosition;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public Level() {  }

    public Block get(int x, int y) {
        return blocks[x][y];
    }

    public Vector2 getSpanPosition() {
        return spanPosition;
    }

    public void setSpanPosition(Vector2 spanPosition) {
        this.spanPosition = spanPosition;
    }

    public Vector2 getFinishPosition() {
        return finishPosition;
    }

    public Block getFinishBlocks() {
        return new Block(finishPosition);
    }

    public void setFinishPosition(Vector2 finishPosition) {
        this.finishPosition = finishPosition;
    }
}