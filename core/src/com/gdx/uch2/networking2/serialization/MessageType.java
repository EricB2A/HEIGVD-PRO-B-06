package com.gdx.uch2.networking2.serialization;

public enum MessageType {
    GameStateUpdate,
    GameStart,
    AckGameStart,
    BlockPlaced,
    AckBlockPlaced,
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
