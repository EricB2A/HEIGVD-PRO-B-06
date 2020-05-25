package com.gdx.uch2.networking;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.gdx.uch2.networking.server.ServerGameStateTickManager;

import java.util.Map;
import java.util.TreeMap;

public class GameState {
    private Map<Integer,PlayerState> playersStates;


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

    public float getPosX(int playerID) {
        return playersStates.get(playerID).getPosX();
    }

    public void setPosX(float posX, int playerID) {
        playersStates.get(playerID).setPosX(posX);
    }

    public float getPosY(int playerID) {
        return playersStates.get(playerID).getPosY();
    }

    public void setPosY(float posY, int playerID) {
        playersStates.get(playerID).setPosY(posY);
    }

    public void setPlayerState(PlayerState p) {
        if (p != null)
            playersStates.put(p.getPlayerID(), p);
    }

    public void removePlayer(int id) {
        playersStates.remove(id);
    }

    public void setPositions(Vector2 pos) {
        for (PlayerState state : playersStates.values()) {
            state.setPosX(pos.x);
            state.setPosY(pos.y);
        }
    }

    public Map<Integer, PlayerState> getPlayerStates() {
        return playersStates;
    }
}
