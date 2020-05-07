package com.gdx.uch2.effects;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Player;

public class LowGravity implements Effect{
    boolean applyOnce = false;

    @Override
    public void apply(Player player) {
        if(!applyOnce) {
            player.translate(new Vector2(0, 5f));
            applyOnce = true;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
