package com.gdx.uch2.networking.server;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Level;
import com.gdx.uch2.networking.*;
import com.gdx.uch2.networking.kryo.NettyKryoDecoder;
import com.gdx.uch2.networking.kryo.NettyKryoEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.netty.buffer.Unpooled.buffer;

public class CentralGameManager {

    private List<ChannelHandlerContext> players;
    private boolean[] finished;
    private NettyKryoEncoder encoder = new NettyKryoEncoder();
    private NettyKryoDecoder decoder = new NettyKryoDecoder();
    private GamePhase currentPhase;
    private Level map;
    private int playerPlacing = -1;

    public CentralGameManager(List<ChannelHandlerContext> players, Level map){
        this.players = players;
        this.map = map;
        finished = new boolean[players.size()];
        Arrays.fill(finished, false);
        startEditingPhase();
    }


    public void readMessage(ChannelHandlerContext ctx, Object msg) throws Exception {

        try{
            ByteBuf m = (ByteBuf) msg;
            m.readChar();
            if(m.getChar(0) == MessageType.PlayerStateUpdate.getChar()){
                processPlayerState(m);
            }
            else if(m.getChar(0) == MessageType.BlockPlaced.getChar()){
                processObjectPlacement(m);
            }
            else if(m.getChar(0) == MessageType.ReachedEnd.getChar()){
                processPlayerReachedEnd(m);
            }
            else{
                while (m.isReadable()) {
                    System.out.print((char) m.readByte());
                    System.out.flush();
                }
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
        Arrays.fill(finished, false);
        //Sends a message to all clients announcing the movement phase starts
        ByteBuf out = buffer(128);
        out.writeChar(MessageType.StartMovementPhase.getChar());
        players.get(playerPlacing).writeAndFlush(out);
    }

    private void startEditingPhase(){
        System.out.println("SRV: start editing phase");
        currentPhase = GamePhase.Editing;
        playerPlacing = -1;
        nextPlayerCanPlace();
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

    private void processPlayerReachedEnd(ByteBuf m){
        int playerThatFinished = m.readInt();
        finished[playerThatFinished] = true;
        boolean allFinished = true;
        for (boolean b : finished) {
            if (!b) {
                allFinished = false;
                break;
            }
        }
        System.out.println("SRV: Le joueur #" + playerThatFinished + " est arrivé à la fin!");

        if(allFinished){
            computePoints();
            resetPlayersPositions();
            startEditingPhase();
        }
    }


    private void processPlayerState(ByteBuf m){

        List<Object> objects = new ArrayList<>();
        decoder.decode(m, objects);

        PlayerState state = (PlayerState) objects.get(0);
        System.out.println("SRV: playerState reçu du joueur #" + state.getPlayerID());
        ServerGameStateTickManager.getInstance().setPlayerState(state);
    }

    private void processObjectPlacement(ByteBuf m){
        if(currentPhase == GamePhase.Editing) {
            //Reads the message
            List<Object> objects = new ArrayList<>();
            decoder.decode(m, objects);
            ObjectPlacement op = (ObjectPlacement) objects.get(0);
            System.out.println("SRV: Placement de block reçu par le joueur #" + op.getPlayerID());
            //Broadcasts the message if it came from the right player
            if(op.getPlayerID() == playerPlacing){
                System.out.println("SRV: Placement de block DU BON JOUEUR reçu");
                sendBlockToAllPlayers((ObjectPlacement) (objects.get(0)));
                if(playerPlacing != players.size() - 1){
                    System.out.println("SRV: NEXT PLAYER CAN PLACE");
                    nextPlayerCanPlace();
                }else{
                    System.out.println("SRV: ALL PLAYERS HAVE PLACED");
                    startMovementPhase();
                }

            }
        }
    }

    private void sendBlockToAllPlayers(ObjectPlacement object){
        for(ChannelHandlerContext ctx : players){
            ByteBuf out = Unpooled.buffer(1024);
            encoder.encode(object, out, MessageType.BlockPlaced.getChar());
            ctx.channel().writeAndFlush(out);
        }
    }

    private void nextPlayerCanPlace(){ //TODO timeout si rien de reçu
        playerPlacing++;
        System.out.println("SRV: sending CAN PLACE to player #" + playerPlacing);
        ByteBuf out = buffer(128);
        out.writeChar(MessageType.CanPlace.getChar());
        players.get(playerPlacing).writeAndFlush(out);
    }

}
