package com.gdx.uch2.networking.messages;

import com.gdx.uch2.entities.Player;

/**
 * Classe représentant un snapshot d’un joueur à un moment donné
 */
public class PlayerState {

    private int playerID;
    private Player.State state;
    private float posX;
    private float posY;
    private long time;


    /**
     * Constructeur
     * @param playerID l'id du joueur
     * @param state l'état actuel du joueur
     * @param posX la position X du joueur
     * @param posY la position Y du joueur
     * @param time le timestamp indiquant le moment du snapshot
     */
    public PlayerState(int playerID, Player.State state,  float posX, float posY, long time) {
        this.playerID = playerID;
        this.state = state;
        this.posX = posX;
        this.posY = posY;
        this.time = time;
    }

    /**
     *
     * @return l'id du joueur
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     *
     * @return la position X du joueur
     */
    public float getPosX() {
        return posX;
    }

    /**
     * modifie la position X du joueur
     * @param posX la nouvelle position X
     */
    public void setPosX(float posX) {
        this.posX = posX;
    }

    /**
     *
     * @return la position Y du joueur
     */
    public float getPosY() {
        return posY;
    }

    /**
     * modifie la position Y du joueur
     * @param posY la nouvelle position Y
     */
    public void setPosY(float posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "Joueur#" + playerID + ", x=" + posX + ", y=" + posY + ", time=" + time;
    }

    /**
     *
     * @return le moment auquel le snapshot a été pris
     */
    public long getTime() {
        return time;
    }

    /**
     *
     * @return l'état du joueur
     */
    public Player.State getState() {
        return state;
    }
}
