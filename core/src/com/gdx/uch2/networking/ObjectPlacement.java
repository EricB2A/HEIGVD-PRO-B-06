package com.gdx.uch2.networking;

import com.esotericsoftware.kryo.Kryo;
import com.gdx.uch2.entities.Block;

public class ObjectPlacement {
    private int playerID;
    private Block block;
    private boolean isTrap;

    static private Kryo kryo;

    public ObjectPlacement(int playerID, Block block, boolean isTrap){
        this.playerID = playerID;
        this.block = block;
        this.isTrap = isTrap;
    }

    /*
    Ne pas enlever
     */
    public ObjectPlacement(){

    }

    public static void setUpKryo() {
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

    public boolean isTrap(){
        return isTrap;
    }

    public String toString(){
        return isTrap? "Trap : " : "Block : " + "(" + getX() + ", " + getY() + ")";
    }

}
