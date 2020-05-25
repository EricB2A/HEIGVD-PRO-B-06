package com.gdx.uch2.networking;

public enum MessageType {
    Ping,
    GameStateUpdate,
    GameStart,
    AckGameStart,
    BlockPlaced,
    PlayerStateUpdate,
    ReachedEnd,
    Death,
    CanPlace,
    StartMovementPhase,
    StartEditingPhase,
    EndGame,
    Score,
    CloseConnection
    ;

}
