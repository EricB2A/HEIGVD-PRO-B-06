package com.gdx.uch2.effects;

import com.gdx.uch2.entities.Player;

/**
 * Interface symbolisant un effet applicable au joueur
 */
public interface Effect {
    void apply(Player player);

    boolean isFinished();
}
