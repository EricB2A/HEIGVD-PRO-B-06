package com.gdx.uch2.networking.messages;

import com.gdx.uch2.networking.serialization.DecoderStream;
import com.gdx.uch2.networking.serialization.EncoderStream;

import java.io.IOException;
import java.net.Socket;

/**
 * Classe encapsulant un socket client
 */
public class PlayerContext {
    private int id;
    private Socket socket;
    public DecoderStream in = null;
    public EncoderStream out = null;

    /**
     * Constructeur
     * @param id l'ID du joueur
     * @param socket le Socket à encapsuler
     */
    public PlayerContext(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
        try {
            this.in = new DecoderStream(socket.getInputStream());
            this.out = new EncoderStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Constructeur
     * @param socket le socket à encapsuler
     */
    public PlayerContext(Socket socket) {
        this(-1, socket);
    }

    /**
     * Modifie la valeur de l'id du joueur
     * @param id la nouvelle valeur de l'id du joueur
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return l'id du joueur
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return le socket encapsulé
     */
    public Socket getSocket() {
        return socket;
    }
}
