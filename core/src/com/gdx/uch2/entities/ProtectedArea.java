package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;

public class ProtectedArea extends Block{
    public ProtectedArea(Vector2 position) {
        super(position, Type.PROTECTED_AREA);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
