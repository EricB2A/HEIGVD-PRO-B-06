package com.gdx.uch2.networking2.serialization;

import com.badlogic.gdx.Game;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class MyOuputStream {
    private Semaphore mutex;
    private DataOutputStream stream;

    public MyOuputStream(OutputStream stream) {
        this.mutex = new Semaphore(1);
        this.stream = new DataOutputStream(stream);
    }

    public void writeMessage(MessageType messageType) {
        try {
            mutex.acquire();
            try {
                stream.writeInt(MessageType.PlayerStateUpdate.ordinal());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(PlayerState playerState){
        try {
            mutex.acquire();
            writeMessage(playerState, true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            mutex.release();
        }

    }

    private void writeMessage(PlayerState playerState, boolean writeMessageType){
        try {
            // PlayerStateUpdate.
            if (writeMessageType) stream.writeInt(MessageType.PlayerStateUpdate.ordinal());
            stream.writeInt(playerState.getPlayerID());
            stream.writeFloat(playerState.getPosX());
            stream.writeFloat(playerState.getPosY());
            stream.writeLong(playerState.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(GameState gameState){
        try {
            mutex.acquire();
            try {
                // GameStateUpdate.
                stream.writeInt(MessageType.GameStateUpdate.ordinal());
                // Taille.
                stream.writeInt(gameState.getPlayerStates().size());
                for (PlayerState p : gameState.getPlayerStates().values()) {
                    writeMessage(p, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage(ObjectPlacement objectPlacement){
        try {
            mutex.acquire();
            try {
                // BlockPlaced.
                stream.writeInt(MessageType.BlockPlaced.ordinal());
                stream.writeInt(objectPlacement.getPlayerID());
                // Bloc.
                stream.writeInt(objectPlacement.getBlock().getType().ordinal());
                stream.writeFloat(objectPlacement.getBlock().getPosition().x);
                stream.writeFloat(objectPlacement.getBlock().getPosition().y);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush(){
        try {
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
