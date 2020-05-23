package com.gdx.uch2.networking;

import com.gdx.uch2.networking.serialization.DecoderStream;
import com.gdx.uch2.networking.serialization.EncoderStream;

import java.io.IOException;
import java.net.Socket;

public class PlayerContext {
    private int id;
    private Socket socket;
    public DecoderStream in = null;
    public EncoderStream out = null;

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

    public PlayerContext(Socket socket) {
        this(-1, socket);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }
}
