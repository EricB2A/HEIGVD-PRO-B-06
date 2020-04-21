package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;

public class PlayerState {

    private int playedID;
    private int posX;
    private int posY;
    static private Kryo kryo;


    /**
     * Ne pas supprimmer.
     */
    public PlayerState(){
    }
    
    public PlayerState(int playedID, int posX, int posY) {
        this.playedID = playedID;
        this.posX = posX;
        this.posY = posY;
    }

    public int getPlayedID() {
        return playedID;
    }

    public void setPlayedID(int playedID) {
        this.playedID = playedID;
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
        return "Joueur#" + playedID + ", x=" + posX + ", y=" + posY;
    }

}
