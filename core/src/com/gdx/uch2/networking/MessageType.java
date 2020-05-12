package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    GameStart,
    BlockPlaced,
    PlayerStateUpdate,
    ReachedEnd,
    CanPlace,
    StartMovementPhase
    ;

    public char getChar(){
        return (char)this.ordinal();
    }
}
