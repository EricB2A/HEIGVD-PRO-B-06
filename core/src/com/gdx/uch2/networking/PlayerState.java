package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;

public class PlayerState {

    private int playerID;
    private int posX;
    private int posY;
    static private Kryo kryo;


    /**
     * Ne pas supprimmer.
     */
    public PlayerState(){
    }
    
    public PlayerState(int playerID, int posX, int posY) {
        this.playerID = playerID;
        this.posX = posX;
        this.posY = posY;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "Joueur#" + playerID + ", x=" + posX + ", y=" + posY;
    }

}
