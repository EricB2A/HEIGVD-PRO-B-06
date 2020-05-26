package com.gdx.uch2.networking.messages;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.TreeMap;

/**
 * Classe contenant un PlayerState pour chaque joueur de la partie
 */
public class GameState {
    private Map<Integer,PlayerState> playersStates;


    /**
     * Constructeur prenant un tableau de PlayerStates en paramètre
     * @param playerStates les PlayerStates à encapsuler dans le GameState
     */
    public GameState(PlayerState[] playerStates){
        playersStates = new TreeMap<>();
        for (PlayerState playerState : playerStates) {
            this.playersStates.put(playerState.getPlayerID(), playerState);
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "playersStates=" + playersStates +
                '}';
    }

    /**
     * Ajoute un PlayerState au GameState
     * @param p le PlayerState à ajouter
     */
    public void setPlayerState(PlayerState p) {
        if (p != null)
            playersStates.put(p.getPlayerID(), p);
    }

    /**
     * Modifie la position de tous les PlayerStates
     * @param pos la position à donner à tous les PlayerStates
     */
    public void setPositions(Vector2 pos) {
        for (PlayerState state : playersStates.values()) {
            state.setPosX(pos.x);
            state.setPosY(pos.y);
        }
    }

    /**
     * @return une Map contenant les PlayerStates mappés avec leurs IDs respectifs
     */
    public Map<Integer, PlayerState> getPlayerStates() {
        return playersStates;
    }
}
