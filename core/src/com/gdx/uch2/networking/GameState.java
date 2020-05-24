package com.gdx.uch2.networking;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.gdx.uch2.networking.server.ServerGameStateTickManager;

import java.util.Map;
import java.util.TreeMap;

public class GameState {
    private Map<Integer,PlayerState> playersStates;
    static private Kryo kryo;

    public GameState(){
    }

    public GameState(PlayerState[] playerStates){
        playersStates = new TreeMap<>();
        for(int i = 0; i < playerStates.length; ++i){
            this.playersStates.put(playerStates[i].getPlayerID(), playerStates[i]); //TODO clone playerStates[i]
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "playersStates=" + playersStates +
                '}';
    }

    public static void setUpKryo() {
        Kryo kryo = new Kryo();
        kryo.register(PlayerState.class);
        GameState.kryo = kryo;
    }

    public static Kryo getKryo(){
        return GameState.kryo;
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
