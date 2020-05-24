package com.gdx.uch2.networking.serialization;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.ProtectedArea;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;

import java.io.*;

public class DecoderStream {

    private final DataInputStream in;
    public IOException e = null;


    public DecoderStream(InputStream stream) {
        this.in = new DataInputStream(new BufferedInputStream(stream));
    }

    public MessageType getType() {

        MessageType m = null;
        try {
            m = MessageType.values()[in.readInt()];
        } catch (IOException e) {
            this.e = e;
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
            this.e = e;
            return null;
        }

        return new PlayerState(id, x, y, time);
    }

    public GameState readGameState() {
        int size;
        try {
            size = in.readInt();
        } catch (IOException e) {
            this.e = e;
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
        Block.Type type = null;
        float x = 0;
        float y = 0;

        try {
            id = in.readInt();
            int typeIdx = in.readInt();
            if (typeIdx >= 0) {
                type = Block.Type.values()[typeIdx];
                x = in.readFloat();
                y = in.readFloat();
            }
        } catch(IOException e) {
            this.e = e;
            return null;
        }

        if (type == null) return new ObjectPlacement(id, null);

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
            this.e = e;
            return -1;
        }

        return i;
    }

    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            this.e = e;
        }
    }
}