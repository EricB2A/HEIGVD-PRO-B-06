package com.gdx.uch2.entities;

import com.gdx.uch2.networking.messages.GameState;
import com.gdx.uch2.networking.messages.PlayerState;

import java.util.*;

/**
 * Singleton gèrant tous les joueurs distants
 */
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

    /**
     *
     * @return L'instance unique du singleton
     */
    public static OnlinePlayerManager getInstance() {
        return Instance.instance;
    }

    /**
     * modifie la valeur des scores
     * @param scores nouveaux scores
     */
    public void setScores(int[] scores){ this.scores = scores;}

    /**
     *
     * @return les scores actuels
     */
    public int[] getScores() { return scores; }


    /**
     * Initialise un nouveau joueur distant
     * @param id ID du joueur
     * @param nickname pseudonyme du joueur
     */
    public void initPlayer(int id, String nickname) {
        System.out.println("CLI: Nouvel adversaire " + nickname + " #" + id);
        players.put(id, new OnlinePlayer(id, nickname));
    }

    /**
     * Obtient tous les pseudonymes dans l'ordre des IDs
     * @return tous les pseudonymes dans l'ordre des IDs
     */
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

    /**
     *
     * @return La collection de joueurs distants
     */
    public Collection<OnlinePlayer> getPlayers() {
        return players.values();
    }

    /**
     * Traite un nouveau GameState en transmettant les mises à jour d'état aux OnlinePlayers concernés
     * @param state le nouveau GameState reçu
     */
    public void update(GameState state) {
        for (Map.Entry<Integer, PlayerState> entry : state.getPlayerStates().entrySet()) {
            if (entry.getKey() != playerId){
                players.get(entry.getKey()).addUpdate(entry.getValue());
            }
        }
    }

    /**
     * Demande à tous les OnlinePlayers de mettre à jour leur position
     * @param delta la différence de temps entre l'ancien état de jeu et le nouveau
     */
    public void updatePlayers(float delta) {
        for (OnlinePlayer p : players.values()) {
            p.update(delta);
        }
    }

    /**
     * Les blocs en cours de placement par les joueurs distants sont réinitialisés
     */
    public void resetPlacementBlocks() {
        for (OnlinePlayer p : getPlayers()) {
            p.setPlacementBlock(null);
        }
    }

    /**
     * Un joueur a effectué une modification sur un bloc en cours de placement est modifié
     * @param id Joueur modifiant le bloc
     * @param b Nouveau bloc
     */
    public void setBlockPosition(int id, Block b) {
        players.get(id).setPlacementBlock(b);
    }

    /**
     * Initialise / réinitialise l'objet
     * @param playerId ID du joueur local
     * @param nickname pseudonyme du joueur local
     */
    public void init(int playerId, String nickname) {
        players.clear();
        this.playerId = playerId;
        this.nickname = nickname;
    }

    /**
     * Obtient un joueur distant en fonction de son ID
     * @param i l'ID du joueur demandé
     * @return le joueur distant demandé
     */
    public OnlinePlayer getPlayer(int i) {
        return players.get(i);
    }

    /**
     *
     * @return l'ID du joueur local
     */
    public int getPlayerId() {
        return playerId;
    }
}
