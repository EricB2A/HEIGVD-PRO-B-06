package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.effects.LowGravity;
import com.gdx.uch2.effects.StrongGravity;

public class Trap extends Block {
    public Trap(Vector2 pos) {
        super(pos);
    }
    StrongGravity l = new StrongGravity();

    @Override
    public boolean isLethal() {
        return true;
    }

    @Override
    public void action(Player player) {
        if(!player.hasEffect(l))
            player.addEffect(l);
        //player.setState(Player.State.DEAD);
    }
}
