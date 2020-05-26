package com.gdx.uch2.networking.server;

import com.gdx.uch2.entities.Level;
import com.gdx.uch2.networking.GamePhase;
import com.gdx.uch2.networking.messages.MessageType;
import com.gdx.uch2.networking.messages.ObjectPlacement;
import com.gdx.uch2.networking.messages.PlayerContext;

import java.util.Arrays;

import static io.netty.buffer.Unpooled.buffer;

/**
 * Classe métier gérant les
 */
public class CentralGameManager {

    private PlayerContext[] players;
    private int[] finished; // 0 = pas arrivé, 1 = arrivé, 2 = premier arrivé.
    private boolean[] dead;
    private boolean[] hasPlaced;
    private GamePhase currentPhase;
    private Level map;
    private int nbPlayersReady = 0;
    private int round;
    private final int nbRounds;
    private boolean isOver;
    private final int PTS_FIRST = 10, PTS_ARRIVED = 5;
    private int[] scoring;
    private boolean firstArrived;

    public CentralGameManager(Level map, int nbRounds){
        this.map = map;
        this.nbRounds = nbRounds;
        firstArrived = true;
        //startEditingPhase();

    }

    public void init(PlayerContext[] players) {
        this.players = players;
        finished = new int[players.length];
        Arrays.fill(finished, 0);
        dead = new boolean[players.length];
        scoring = new int[players.length];
        Arrays.fill(scoring, 0);
        hasPlaced = new boolean[players.length];
        firstArrived = true;

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
        else if (type == MessageType.Death) {
            processPlayerDeath(context);
        }
        else if(type == MessageType.AckGameStart){
            processAckGameStart(context);
        }
        else{
            System.out.println("SRV: Type de messages inconnu : " + type);
        }
    }

    public void disconnectedClient() {
        if (players != null) {
            endGame();
        }
    }

    public boolean isOver() {
        return isOver;
    }

    private void startMovementPhase(){
        currentPhase = GamePhase.Moving;
        Arrays.fill(finished, 0);
        Arrays.fill(dead, false);
        Arrays.fill(hasPlaced, false);
        firstArrived = true;
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
        for(int i = 0; i < players.length; ++i){
            if(finished[i] > 0){
                if(finished[i] == 2){ // Premier
                    scoring[i] += PTS_FIRST;
                }else if (finished[i] == 1){
                    scoring[i] += PTS_ARRIVED;
                }
            }
        }

        for(int i = 0; i < players.length; ++i){
            System.out.printf("SRV: joureur#%d possède [%d] pts\n", i, scoring[i]);
        }
        for (PlayerContext ctx : players) {
            if (!ctx.getSocket().isClosed()) {
                ctx.out.writeMessage(scoring);
            }
        }
    }

    private void resetPlayersPositions(){
        ServerGameStateTickManager.getInstance().getGameState().setPositions(map.getSpanPosition());
    }

    private void processPlayerDeath(PlayerContext ctx) {
        dead[ctx.getId()] = true;
        checkEndRound();
    }

    private void processPlayerReachedEnd(PlayerContext ctx){
        if(firstArrived){
            finished[ctx.getId()] = 2;
        }else{
            finished[ctx.getId()] = 1;
        }
        firstArrived = false;
        System.out.println("SRV: Le joueur #" + ctx.getId() + " est arrivé à la fin!");
        checkEndRound();
    }

    private void checkEndRound() {
        boolean allFinished = true;
        for (int i = 0; i < finished.length; ++i) {
            if (!players[i].getSocket().isClosed() && finished[i] == 0 && !dead[i]) {
                allFinished = false;
                break;
            }
        }


        if(allFinished){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            resetPlayersPositions();
            if (++round < nbRounds) {
                computePoints();
                startEditingPhase();
            } else {
                endGame();
            }

        }
    }

    private void endGame() {
        computePoints();
        for (PlayerContext p : players) {
            if (!p.getSocket().isClosed()) {
                p.out.writeMessage(MessageType.EndGame);
            }
        }

        isOver = true;
        GameServer.closeConnection();
    }

    private void processAckGameStart(PlayerContext ctx){
        nbPlayersReady++;
        if (nbPlayersReady == players.length) {
            startEditingPhase();
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

            hasPlaced[op.getPlayerID()] = true;

            boolean acc = true;
            for (int i = 0; i < hasPlaced.length; ++i) {
                acc = acc && hasPlaced[i];
            }

            if (acc) {
                startMovementPhase();
                sendBlockToAllPlayers(new ObjectPlacement(-1, op.getBlock()));
            } else {
                sendBlockToAllPlayers(new ObjectPlacement(0, op.getBlock()));
            }

            System.out.println("SRV: broadcasted new block, next player to place is #");

        }
    }

    private void sendBlockToAllPlayers(final ObjectPlacement op){
        for (PlayerContext ctx : players) {
            if (!ctx.getSocket().isClosed()) {
                ctx.out.writeMessage(op);
            }
        }
    }
}
