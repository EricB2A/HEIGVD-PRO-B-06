package com.gdx.uch2.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Constantes globales
 */
public class Constants {
    /**
     * Nom de fichier du sprite du joueur principal
     */
    public static final String PLAYER_1_ATLAS = "player.atlas";

    /**
     * Nom de fichier des sprites des divers blocks
     */
    public static final String BLOCKS_ATLAS = "levels/blocks.atlas";

    /**
     * Nom de fichier du sprite des adversaires
     */
    public static final String OPPONENTS_ATLAS = "opponents.atlas";

    /**
     * Durée en millisecondes séparant les envois de GameStates et des PlayerStates
     */
    public static final int TICK_DURATION = 50;

    /**
     * Vitesse de la boucle de jeu principale
     */
    public static final float LOOP_SPEED = .10f;
}
