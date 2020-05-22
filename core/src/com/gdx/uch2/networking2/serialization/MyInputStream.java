package com.gdx.uch2.networking2.serialization;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.ProtectedArea;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;

import java.io.*;
import java.util.concurrent.Semaphore;

public class MyInputStream  {

    private final DataInputStream in;

    public MyInputStream(InputStream stream) {
        this.in = new DataInputStream(new BufferedInputStream(stream));
    }

    public MessageType getType() {
        MessageType m = null;
        try {
            m = MessageType.values()[in.readInt()];
        } catch (IOException e) {
            e.printStackTrace();
        }

        return m;
    }

    public PlayerState readPlayerState() {
        int id;
        float x;
        float y;
        long time;
        try {
            id = in.readInt();
            x = in.readFloat();
            y = in.readFloat();
            time = in.readLong();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new PlayerState(id, x, y, time);
    }

    public GameState readGameState() {
        int size;
        try {
            size = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        PlayerState[] players = new PlayerState[size];

        for (int i = 0; i < size; ++i) {
            players[i] = readPlayerState();
        }

        return new GameState(players);
    }

    public ObjectPlacement readObjectPlacement() {
        int id;
        Block.Type type;
        float x;
        float y;

        try {
            id = in.readInt();
            type = Block.Type.values()[in.readInt()];
            x = in.readFloat();
            y = in.readFloat();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        switch (type) {
            case PROTECTED_AREA:
                return new ObjectPlacement(id, new ProtectedArea(new Vector2(x, y)));
            case LETHAL:
            case G_DOWN:
            case G_UP:
                return new ObjectPlacement(id, new Trap(new Vector2(x, y), type));
            default:
                return new ObjectPlacement(id, new Block(new Vector2(x, y), type));
        }
    }

    public int readInt() {
        int i;
        try {
            i = in.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
