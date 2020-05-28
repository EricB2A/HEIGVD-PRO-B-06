package com.gdx.uch2.effects;

import com.gdx.uch2.entities.Player;

/**
 * Effet augmentant la gravité pour le joueur sur lequel il est appliqué
 */
public class StrongGravity implements Effect {
    @Override
    public void apply(Player player) {
        player.getAcceleration().y = player.getAcceleration().y * 2;
    }
}
