package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;

public class Trap extends Block {
    public Trap(Vector2 pos) {
        super(pos);
    }

    @Override
    public boolean isLethal() {
        return true;
    }
}
