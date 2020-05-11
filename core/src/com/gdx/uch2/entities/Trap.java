package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.effects.*;

public class Trap extends Block {
    Effect effect;
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
        return true;
    }

    @Override
    public void action(Player player) {
        if(!player.hasEffect(effect))
            player.addEffect(effect);
    }
}
