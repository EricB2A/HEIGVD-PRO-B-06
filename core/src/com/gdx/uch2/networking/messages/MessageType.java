package com.gdx.uch2.networking.messages;

/**
 * Enum contenant tous les types de messages utilisés par le client et le serveur
 */
public enum MessageType {
    Ping,
    GameStateUpdate,
    GameStart,
    AckGameStart,
    BlockPlaced,
    PlayerStateUpdate,
    ReachedEnd,
    Death,
    EndGame,
    Score,
    CloseConnection,
    BlockPosition
}
