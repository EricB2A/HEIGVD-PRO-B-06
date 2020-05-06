package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    UserAction,
    GameStart,
    BlockPlaced;

    public char getChar(){
        return (char)this.ordinal();
    }
}
