package com.gdx.uch2.effects;

import com.gdx.uch2.entities.Player;

public interface Effect {
    void apply(Player player);

    boolean isFinished();
}
