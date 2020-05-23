package com.gdx.uch2.networking;

import com.gdx.uch2.networking2.serialization.MyInputStream;
import com.gdx.uch2.networking2.serialization.MyOuputStream;

import java.io.IOException;
import java.net.Socket;

public class PlayerContext {
    private int id;
    private Socket socket;
    public MyInputStream in = null;
    public MyOuputStream out = null;

    public PlayerContext(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
        try {
            this.in = new MyInputStream(socket.getInputStream());
            this.out = new MyOuputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }
}
