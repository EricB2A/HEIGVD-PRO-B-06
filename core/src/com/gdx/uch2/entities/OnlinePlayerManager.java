package com.gdx.uch2.entities;

import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.PlayerState;

import java.util.*;

public class OnlinePlayerManager {
    private static class Instance {
        private static final OnlinePlayerManager instance = new OnlinePlayerManager();
    }
    private Map<Integer, OnlinePlayer> players;
    private int playerId;

    private OnlinePlayerManager() {
        players = new TreeMap<>();

    }

    public static OnlinePlayerManager getInstance() {
        return Instance.instance;
    }

    public void resetPlayers() {
        players.clear();
    }

    public Collection<OnlinePlayer> getPlayers() {
        return players.values();
    }

    public void update(GameState state) {
        for (Map.Entry<Integer, PlayerState> entry : state.getPlayerStates().entrySet()) {
            if (entry.getKey() != playerId){
                if (!players.containsKey(entry.getKey())) {
                    players.put(entry.getKey(), new OnlinePlayer(entry.getValue()));
                } else {
                    players.get(entry.getKey()).addUpdate(entry.getValue());
                }
            }
        }
    }

    public void updatePlayers(float delta) {
        for (OnlinePlayer p : players.values()) {
            p.update(delta);
        }
    }

    public void init(int playerId) {
        players.clear();
        this.playerId = playerId;
    }

}
