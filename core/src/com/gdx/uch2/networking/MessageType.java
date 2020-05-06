package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    UserAction,
    GameStart,
    BlockPlaced,
    PlayerStateUpdate
    ;

    public char getChar(){
        return (char)this.ordinal();
    }
}
