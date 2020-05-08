package com.gdx.uch2.effects;

import com.gdx.uch2.entities.Player;

public class StrongGravity implements Effect {
    boolean finished = false;

    @Override
    public void apply(Player player) {
        player.getAcceleration().y = player.getAcceleration().y * 2;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
