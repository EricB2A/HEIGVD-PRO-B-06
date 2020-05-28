package com.gdx.uch2.effects;

import com.gdx.uch2.entities.Player;

/**
 * Effet qui tue le joueur
 */
public class Kill implements Effect{
    @Override
    public void apply(Player player) {
        player.kill();
    }
}
