package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.effects.LowGravity;

public class Trap extends Block {
    public Trap(Vector2 pos) {
        super(pos);
    }

    @Override
    public boolean isLethal() {
        return true;
    }

    @Override
    public void action(Player player) {
        player.addEffect(new LowGravity());
        //player.setState(Player.State.DEAD);
    }
}
