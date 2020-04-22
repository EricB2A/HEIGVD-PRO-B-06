package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;

import java.util.ArrayList;
import java.util.List;

public class UserActionSequence {

    private List<UserAction> actions;
    private final int playerID;
    static private Kryo kryo;

    public UserActionSequence(){
        this.playerID = 99;
    }

    public UserActionSequence(int playerID){
        actions = new ArrayList<UserAction>();
        this.playerID = playerID;
    }

    public static void setUpKryo() {
        Kryo kryo = new Kryo();
        kryo.register(UserAction.class);
        kryo.register(UserActionSequence.class);
        UserActionSequence.kryo = kryo;
    }

    public static Kryo getKryo(){
        return UserActionSequence.kryo;
    }

    public void addAction(UserAction action){
        actions.add(action);
    }

    public List<UserAction> getActions(){
        return actions;
    }

    public int getPlayerID(){
        return this.playerID;
    }

    public void clear(){
        actions.clear();
    }
}
