package com.gdx.uch2.networking;

public enum MessageType {
    GameStateUpdate,
    GameStart,
    AckGameStart,
    BlockPlaced,
    AckBlockPlaced,
    PlayerStateUpdate,
    ReachedEnd,
    Death,
    CanPlace,
    StartMovementPhase,
    StartEditingPhase
    ;

    public char getChar(){
        return (char)this.ordinal();
    }
}
