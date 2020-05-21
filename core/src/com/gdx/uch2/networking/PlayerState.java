package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;

public class PlayerState {

    private int playerID;
    private float posX;
    private float posY;
    private long time;
    static private Kryo kryo;


    /**
     * Ne pas supprimmer.
     */
    public PlayerState(){
    }
    
    public PlayerState(int playerID, float posX, float posY, long time) {
        this.playerID = playerID;
        this.posX = posX;
        this.posY = posY;
        this.time = time;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "Joueur#" + playerID + ", x=" + posX + ", y=" + posY + ", time=" + time;
    }

    public long getTime() {
        return time;
    }
}
