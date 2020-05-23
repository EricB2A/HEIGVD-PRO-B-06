package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;
import com.gdx.uch2.entities.Block;

public class ObjectPlacement {
    private int playerID;
    private Block block;

    static private Kryo kryo;

    public ObjectPlacement(int playerID, Block block){
        this.playerID = playerID;
        this.block = block;
    }

    /*
    Ne pas enlever
     */
    public ObjectPlacement(){

    }

    public static void setUpKryo() { //TODO Eric
        Kryo kryo = new Kryo();
        kryo.register(ObjectPlacement.class);
        kryo.register(Block.class);
        ObjectPlacement.kryo = kryo;
    }

    public static Kryo getKryo(){
        return ObjectPlacement.kryo;
    }

    public int getPlayerID(){
        return playerID;
    }

    public Block getBlock(){
        return block;
    }

    public float getHeight(){
        return block.getBounds().height;
    }

    public float getWidth(){
        return block.getBounds().width;
    }

    public float getX(){
        return block.getPosition().x;
    }

    public float getY(){
        return block.getPosition().y;
    }


    public String toString(){
        return "ID : " + playerID + " - " + (block == null ? "Block null" : (block.isLethal()? "Trap : " : "Block : ") +  "(" + getX() + ", " + getY() + ")");
    }

}
