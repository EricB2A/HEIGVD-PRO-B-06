package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;
import com.gdx.uch2.entities.Block;

public class PlayerIDGiver {

    private int id;
    static private Kryo kryo;

    /* Ne pas enlever */
    public PlayerIDGiver(){

    }

    public PlayerIDGiver(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static void setUpKryo() {
        Kryo kryo = new Kryo();
        kryo.register(PlayerIDGiver.class);
        PlayerIDGiver.kryo = kryo;
    }

    public static Kryo getKryo(){
        return PlayerIDGiver.kryo;
    }


}
