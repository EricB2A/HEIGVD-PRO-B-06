package com.gdx.uch2.networking.server;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Level;
import com.gdx.uch2.networking.*;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import static io.netty.buffer.Unpooled.buffer;

public class CentralGameManager {

    private List<PlayerContext> players;
    private boolean[] finished;
    private boolean[] recievedBlockPlacement;
    private boolean[] connected;
    private NettyKryoEncoder encoder = new NettyKryoEncoder();
    private NettyKryoDecoder decoder = new NettyKryoDecoder();
    private GamePhase currentPhase;
    private Level map;
    private int nbPlayersReady = 0;
    public static Semaphore BigMutex = new Semaphore(1);


    public CentralGameManager(final List<PlayerContext> players, Level map){
        this.players = players;
        this.map = map;
        finished = new boolean[players.size()];
        Arrays.fill(finished, false);
        recievedBlockPlacement = new boolean[players.size()];
        connected = new boolean[players.size()];
        //startEditingPhase();

    }


    public void readMessage(MessageType type, PlayerContext context) {
        if(type == MessageType.PlayerStateUpdate) {
            processPlayerState(context);
        }
        else if(type == MessageType.BlockPlaced){
            processObjectPlacement(context);
        }
        else if(type == MessageType.ReachedEnd){
            processPlayerReachedEnd(context);
        }
        else if(type == MessageType.AckGameStart){
            processAckGameStart(context);
        }
        else if(type == MessageType.AckBlockPlaced){
            processAckBlockPlaced(context);
        }
        else{
            System.out.println("SRV: Type de messages inconnu : " + type);
        }
    }

    private void processAckBlockPlaced(PlayerContext ctx) {
        recievedBlockPlacement[ctx.getId()] = true;
    }

    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
        Arrays.fill(finished, false);

        /*
        //Sends a message to all clients announcing the movement phase starts
        ByteBuf out = Unpooled.buffer(128);
        out.writeChar(MessageType.StartMovementPhase.getChar());
        broadcast(out);

         */
    }

    private void startEditingPhase(){
        final int STARTER_ID = 0;

        System.out.println("SRV: start editing phase");
        currentPhase = GamePhase.Editing;

        //Send an object with Block = null to inform players that the editing phase is starting
        ObjectPlacement op = new ObjectPlacement(STARTER_ID, null);
        System.out.println("SRV: envoie objet de placement avec block = null et ID = " + op.getPlayerID());
        sendBlockToAllPlayers(op);
    }

    private void computePoints(){
        //TODO
    }

    private void resetPlayersPositions(){
        GameState state = ServerGameStateTickManager.getInstance().getGameState();
        Vector2 spawn = map.getSpanPosition();
        for(int i = 0; i < players.size(); ++i){
            state.setPosX(spawn.x, i);
            state.setPosY(spawn.y, i);
        }
    }

    private void processPlayerReachedEnd(PlayerContext ctx){
        finished[ctx.getId()] = true;
        boolean allFinished = true;
        for (boolean b : finished) {
            if (!b) {
                allFinished = false;
                break;
            }
        }
        System.out.println("SRV: Le joueur #" + ctx.getId() + " est arrivé à la fin!");

        if(allFinished){
            computePoints();
            resetPlayersPositions();
            startEditingPhase();
        }
    }

    private void processAckGameStart(PlayerContext ctx){
        int playerId = ctx.getId();

        if (!connected[playerId]) {
            nbPlayersReady++;
            connected[playerId] = true;
            if (nbPlayersReady == players.size()) {
                startEditingPhase();
            }
        }
    }

    private void processPlayerState(PlayerContext ctx){
        ServerGameStateTickManager.getInstance().setPlayerState(ctx.in.readPlayerState());

    }

    private void processObjectPlacement(PlayerContext ctx){

        if(currentPhase == GamePhase.Editing) {
            //Reads the message
            ObjectPlacement op = ctx.in.readObjectPlacement();
            System.out.println("SRV: Placement de block reçu par le joueur #" + op.getPlayerID());

            int newID;
            if(op.getPlayerID() < players.size() - 1){
                newID = op.getPlayerID() + 1;
            }else{
                newID = -1;
                startMovementPhase();
            }

            sendBlockToAllPlayers(new ObjectPlacement(newID, op.getBlock()));
            System.out.println("SRV: broadcasted new block, next player to place is #" + newID);

        }

        ctx.out.writeMessage(MessageType.AckBlockPlaced);

//        ByteBuf out = buffer(128);
//        out.writeChar(MessageType.AckBlockPlaced.getChar());
//        ctx.writeAndFlush(out);

    }

    private void sendBlockToAllPlayers(final ObjectPlacement op){
        for (PlayerContext ctx : players) {
            ctx.out.writeMessage(op);
        }
    }
}
