package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    GameStart,
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
