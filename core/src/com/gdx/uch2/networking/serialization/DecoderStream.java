package com.gdx.uch2.networking.serialization;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.ProtectedArea;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.networking.GameState;
import com.gdx.uch2.networking.MessageType;
import com.gdx.uch2.networking.ObjectPlacement;
import com.gdx.uch2.networking.PlayerState;

import java.io.*;

/**
 * InputStream décodant les bytes lus en fonction de la méthode utilisée
 */
public class DecoderStream extends FilterInputStream {

    private final DataInputStream stream;
    public IOException e = null;


    /**
     * Constructeur prenant un Inputstream en paramètre
     * @param stream Inputstream sur lequel ajouter le DecoderStream
     */
    public DecoderStream(InputStream stream) {
        super(new DataInputStream(new BufferedInputStream(stream)));
        this.stream = (DataInputStream) in;
    }

    /**
     * Lit un type de message. Ne lit pas la suite du stream.
     * @return le type de message lu
     */
    public MessageType getType() {
        MessageType m = null;
        try {
            m = MessageType.values()[stream.readInt()];
            this.e = null;
        } catch (IOException e) {
            this.e = e;
        }

        return m;
    }

    /**
     * Lit un PlayerState
     * @return le PlayerState lu
     */
    public PlayerState readPlayerState() {
        int id;
        Player.State state;
        float x;
        float y;
        long time;
        try {
            id = stream.readInt();
            state = Player.State.values()[stream.readInt()];
            x = stream.readFloat();
            y = stream.readFloat();
            time = stream.readLong();
            this.e = null;
        } catch (IOException e) {
            this.e = e;
            return null;
        }

        return new PlayerState(id, state, x, y, time);
    }

    /**
     * Lit un GameState
     * @return le GameState lu
     */
    public GameState readGameState() {
        int size;
        try {
            size = stream.readInt();
            this.e = null;
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

    /**
     * Lit un tableau de scores
     * @return le tableau de scores lu
     */
    public int[] readScore(){
        int size;
        try{
            size = stream.readInt();

            int[] scores = new int[size];
            for(int i = 0; i < size; ++i){
                scores[i] = stream.readInt();
            }

            this.e = null;
            return scores;
        }catch (IOException e){
            this.e = e;
            return null;
        }
    }

    /**
     * Lit un ObjectPlacement
     * @return l'ObjectPlacement lu
     */
    public ObjectPlacement readObjectPlacement() {
        int id;
        Block.Type type = null;
        float x = 0;
        float y = 0;

        try {
            id = stream.readInt();
            int typeIdx = stream.readInt();
            if (typeIdx >= 0) {
                type = Block.Type.values()[typeIdx];
                x = stream.readFloat();
                y = stream.readFloat();
            }
            this.e = null;
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

    /**
     * Lit un entier sur 32 bits
     * @return l'entier lu
     */
    public int readInt() {
        int i;
        try {
            i = stream.readInt();
            this.e = null;
        } catch (IOException e) {
            this.e = e;
            return -1;
        }

        return i;
    }

    /**
     * Lit une String
     * @return la String lue
     */
    public String readString() {
        String s = null;
        try {
            s = stream.readUTF();
            this.e = null;
        } catch (IOException e) {
            this.e = e;
        }

        return s;
    }

    /**
     * Ferme le stream
     */
    public void close() {
        try {
            stream.close();
        } catch (IOException e) {
        }
    }
}
