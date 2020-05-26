package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.effects.*;

/**
 * Classe représentant un block de type piège
 */
public class Trap extends Block {

    /**
     * Effet appliqué au joueur s'il touche le block
     */
    Effect effect;

    /**
     * Constructeur
     * @param pos position du block
     * @param type type de block
     */
    public Trap(Vector2 pos, Block.Type type) {
        super(pos, type);
        switch (type){
            case LETHAL: effect = new Kill(); break;
            case G_DOWN: effect = new LowGravity(); break;
            case G_UP: effect = new StrongGravity(); break;
            default: effect = new Kill(); break;
        }
    }

    @Override
    public boolean isLethal() {
        return type == Type.LETHAL;
    }

    @Override
    public void action(Player player) {
        if(!player.hasEffect(effect))
            player.addEffect(effect);
    }
}
