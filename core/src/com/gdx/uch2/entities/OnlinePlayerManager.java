package com.gdx.uch2.entities;

import com.gdx.uch2.networking.messages.GameState;
import com.gdx.uch2.networking.messages.PlayerState;

import java.util.*;

public class OnlinePlayerManager {
    private static class Instance {
        private static final OnlinePlayerManager instance = new OnlinePlayerManager();
    }
    private Map<Integer, OnlinePlayer> players;
    private int playerId;
    private int[] scores;
    private String nickname;

    private OnlinePlayerManager() {
        players = new TreeMap<>();

    }

    public void setScores(int[] scores){ this.scores = scores;}

    public int[] getScores() { return scores; }

    public static OnlinePlayerManager getInstance() {
        return Instance.instance;
    }

    public void initPlayer(int id, String nickname) {
        System.out.println("CLI: Nouvel adversaire " + nickname + " #" + id);
        players.put(id, new OnlinePlayer(id, nickname));
    }

    public String[] getNicknames() {
        String[] nicknames = new String[players.size() + 1];
        nicknames[playerId] = nickname;
        for (int i = 0; i < players.size() + 1; ++i) {
            if (i != playerId) {
                nicknames[i] = players.get(i).getNickname();
            }
        }

        return nicknames;
    }

    public Collection<OnlinePlayer> getPlayers() {
        return players.values();
    }

    public void update(GameState state) {
        for (Map.Entry<Integer, PlayerState> entry : state.getPlayerStates().entrySet()) {
            if (entry.getKey() != playerId){
                players.get(entry.getKey()).addUpdate(entry.getValue());
            }
        }
    }

    public void updatePlayers(float delta) {
        for (OnlinePlayer p : players.values()) {
            p.update(delta);
        }
    }

    public void resetPlacementBlocks() {
        for (OnlinePlayer p : getPlayers()) {
            p.setPlacementBlock(null);
        }
    }

    public void setBlockPosition(int id, Block b) {
        players.get(id).setPlacementBlock(b);
    }

    public void init(int playerId, String nickname) {
        players.clear();
        this.playerId = playerId;
        this.nickname = nickname;
    }

    public OnlinePlayer getPlayer(int i) {
        return players.get(i);
    }

    public int getPlayerId() {
        return playerId;
    }
}
