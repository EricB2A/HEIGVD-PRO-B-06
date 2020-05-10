package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {

    public static final float SIZE = 1f;

    public enum Type {
        BLOCK, BOX, LETHAL, G_UP, G_DOWN
    }

    Vector2 	position = new Vector2();
    Rectangle 	bounds = new Rectangle();
    Type        type = Type.BLOCK;
    private boolean solid;

    /*
    Ne pas enlever
     */
    public Block(){

    }

    public Block(Vector2 pos) {
        this.position = pos;
        this.bounds.setX(pos.x);
        this.bounds.setY(pos.y);
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }

    public Block(Vector2 pos, Type type) {
        this.position = pos;
        this.bounds.setX(pos.x);
        this.bounds.setY(pos.y);
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
        this.type = type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isLethal() {
        return false;
    }

    public void action(Player player) { }

    public Type getType() {
        return type;
    }
}
