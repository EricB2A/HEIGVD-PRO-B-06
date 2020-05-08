package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    GameStart,
    BlockPlaced,
    PlayerStateUpdate,
    ReachedEnd
    ;

    public char getChar(){
        return (char)this.ordinal();
    }
}
