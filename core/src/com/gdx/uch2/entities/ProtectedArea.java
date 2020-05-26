package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un bloc définissant une zone protégée dans laquelle is est impossible de placer des blocks
 */
public class ProtectedArea extends Block{

    /**
     * Constructeur
     * @param position position du block
     */
    public ProtectedArea(Vector2 position) {
        super(position, Type.PROTECTED_AREA);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
