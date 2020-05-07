package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {

    public static final float SIZE = 1f;

    Vector2 	position = new Vector2();
    Rectangle 	bounds = new Rectangle();
    private boolean solid;

    public Block(Vector2 pos) {
        this.position = pos;
        this.bounds.setX(pos.x);
        this.bounds.setY(pos.y);
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
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

    public void action(Player player) { player.getVelocity().x = 0; }
}
