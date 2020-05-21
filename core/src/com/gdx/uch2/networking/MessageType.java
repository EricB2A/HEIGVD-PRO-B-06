package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    GameStart,
    AckGameStart,
    BlockPlaced,
    PlayerStateUpdate,
    ReachedEnd,
    CanPlace,
    StartMovementPhase,
    StartEditingPhase
    ;

    public char getChar(){
        return (char)this.ordinal();
    }
}
