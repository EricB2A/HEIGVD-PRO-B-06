package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    UserAction,
    GameStart;

    public char getChar(){
        return (char)this.ordinal();
    }
}
