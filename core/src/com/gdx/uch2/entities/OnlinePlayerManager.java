package com.gdx.uch2.entities;

import com.gdx.uch2.networking.GameState;

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

    public OnlinePlayerManager getInstance() {
        return Instance.instance;
    }

    public void reset() {
        players.clear();
    }

    public Collection<OnlinePlayer> getPlayers() {
        return players.values();
    }

    public void update(GameState state) {

    }

}
