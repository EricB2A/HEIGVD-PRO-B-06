package com.gdx.uch2.networking;

import java.util.ArrayList;
import java.util.List;

public class UserActionSequence {
    private List<UserAction> actions;
    private final int playerID;

    public UserActionSequence(int playerID){
        actions = new ArrayList<UserAction>();
        this.playerID = playerID;
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
}
