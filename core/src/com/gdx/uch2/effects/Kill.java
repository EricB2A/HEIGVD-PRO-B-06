package com.gdx.uch2.effects;

import com.gdx.uch2.entities.Player;

public class Kill implements Effect{
    boolean finished = false;

    @Override
    public void apply(Player player) {
        player.setState(Player.State.DEAD);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
