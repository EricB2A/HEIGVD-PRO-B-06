package com.uch2.networking;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class GameState {
    private int playedID;
    private int posX;
    private int posY;
    static private Kryo kryo;
    public static void setUpKryo() {
        Kryo kryo = new Kryo();
        kryo.register(GameState.class);
        GameState.kryo = kryo;
    }

    public static Kryo getKryo(){
        return GameState.kryo;
    }

    //TODO constructeur vide 
    public GameState(){

    }
    
    public GameState(int playedID, int posX, int posY) {
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
