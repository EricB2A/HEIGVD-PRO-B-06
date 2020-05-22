package networking2.serialization;

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
