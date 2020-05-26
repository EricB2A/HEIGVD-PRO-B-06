package com.gdx.uch2.networking.serialization;

import com.gdx.uch2.entities.Block;
import com.gdx.uch2.networking.messages.GameState;
import com.gdx.uch2.networking.messages.MessageType;
import com.gdx.uch2.networking.messages.ObjectPlacement;
import com.gdx.uch2.networking.messages.PlayerState;

import java.io.*;
import java.util.concurrent.Semaphore;

/**
 * OutputStream encodant les bytes écrits en fonction de l'onjet passé en paramètre
 */
public class EncoderStream extends FilterOutputStream {
    private Semaphore mutex;
    private DataOutputStream stream;
    public IOException e = null;

    /**
     * Constructeur prenant un OutputStream en paramètre
     * @param stream Outputstream sur lequel ajouter l'EncoderStream
     */
    public EncoderStream(OutputStream stream) {
        super(new DataOutputStream( new BufferedOutputStream(stream)));
        this.mutex = new Semaphore(1);
        this.stream = (DataOutputStream) out;
    }

    /**
     * Ecrit un type de message
     * @param messageType le type de message à écrire
     */
    public void writeMessage(MessageType messageType) {
        writeMessage(messageType.ordinal());
    }

    /**
     * Ecrit un entier
     * @param i l'entier à écrire
     */
    public void writeMessage(int i) {
        try {
            mutex.acquire();
            try {
                stream.writeInt(i);
                stream.flush();
                this.e = null;
            } catch (IOException e) {
                this.e = e;
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecrit une String
     * @param s la String à écrire
     */
    public void writeMessage(String s) {
        try {
            mutex.acquire();
            try {
                stream.writeUTF(s);
                stream.flush();
                this.e = null;
            } catch (IOException e) {
                this.e = e;
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecrit un tableau de scores
     * @param scores le tableau de scores
     */
    public void writeMessage(int[] scores){
        try {
            mutex.acquire();
            stream.writeInt(MessageType.Score.ordinal());
            stream.writeInt(scores.length);
            for(int i = 0; i < scores.length; ++i){
                stream.writeInt(scores[i]);
            }
            stream.flush();
            this.e = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.e = e;

        }finally {
            mutex.release();
        }
    }

    /**
     * Ecrit un playerState
     * @param playerState le PlayerState à écrire
     */
    public void writeMessage(PlayerState playerState){
        try {
            mutex.acquire();
            writeMessage(playerState, true);
            stream.flush();
            this.e = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            this.e = e;
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
            stream.writeInt(playerState.getState().ordinal());
            stream.writeFloat(playerState.getPosX());
            stream.writeFloat(playerState.getPosY());
            stream.writeLong(playerState.getTime());
            this.e = null;
        } catch (IOException e) {
            this.e = e;
        }
    }

    /**
     * Ecrit un GameState
     * @param gameState le GameState à écrire
     */
    public void writeMessage(GameState gameState){
        try {
            mutex.acquire();
            try {
                // GameStateUpdate.
                stream.writeInt(MessageType.GameStateUpdate.ordinal());
                // Taille.
                stream.writeInt(gameState.getPlayerStates().size());
                for (PlayerState playerState : gameState.getPlayerStates().values()) {
                    writeMessage(playerState, false);
                }
                stream.flush();
                this.e = null;
            } catch (IOException e) {
                this.e = e;
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ecrit un ObjectPlacement
     * @param objectPlacement l'ObjectPlacement à écrire
     */
    public void writeMessage(ObjectPlacement objectPlacement){
        try {
            mutex.acquire();
            try {
                // BlockPlaced.
                stream.writeInt(MessageType.BlockPlaced.ordinal());
                stream.writeInt(objectPlacement.getPlayerID());
                // Bloc.
                Block b = objectPlacement.getBlock();
                if (b == null) {
                    stream.writeInt(-1);
                } else {
                    stream.writeInt(b.getType().ordinal());
                    stream.writeFloat(objectPlacement.getBlock().getPosition().x);
                    stream.writeFloat(objectPlacement.getBlock().getPosition().y);
                }
                stream.flush();
                this.e = null;
            } catch (IOException e) {
                this.e = e;
            } finally {
                mutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferme le stream
     */
    public void close(){
        try {
            stream.close();
        } catch (IOException e) {
        }
    }

    /**
     * Flush les données dans le stream
     */
    public void flush(){
        try {
            stream.flush();
        } catch (IOException e) {
        }
    }

}
