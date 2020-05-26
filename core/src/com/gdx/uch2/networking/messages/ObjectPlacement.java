package com.gdx.uch2.networking.messages;

import com.gdx.uch2.entities.Block;

/**
 * Classe échangée entre le client et le serveur pour gérer la phase de placement
 */
public class ObjectPlacement {
    private int playerID;
    private Block block;

    /**
     * Constructeur
     * @param playerID Si le message est envoyé d'un client vers le serveur, playerID signifie l'ID du joueur plaçant le block.
     *                 Si le message est envoyé du serveur vers un client, playerID vaut 0 en temps
     *                 normal et -1 si le message marque la fin de la phase de placement
     * @param block Si le message est envoyé d'un client vers le serveur, block signifie le block que le joueur souhaite placer.
     *              Si le message est envoyé du serveur vers un client, block signifie le block à placer sur les niveaux
     *              locaux des clients. Si block est null, cela signifie le début de la phase de placement.
     */
    public ObjectPlacement(int playerID, Block block){
        this.playerID = playerID;
        this.block = block;
    }

    /**
     * @return l'attribut playerID
     */
    public int getPlayerID(){
        return playerID;
    }

    /**
     *
     * @return l'attribut block
     */
    public Block getBlock(){
        return block;
    }

    /**
     *
     * @return la hauteur du block
     */
    public float getHeight(){
        return block.getBounds().height;
    }

    /**
     *
     * @return la largeur du block
     */
    public float getWidth(){
        return block.getBounds().width;
    }

    /**
     *
     * @return la position X du block
     */
    public float getX(){
        return block.getPosition().x;
    }

    /**
     *
     * @return la position Y du block
     */
    public float getY(){
        return block.getPosition().y;
    }


    /**
     * Rend l'objet affichage dans une console pour le déboguage
     * @return une String contenant certaines informations sur l'objet
     */
    public String toString(){
        return "ID : " + playerID + " - " + (block == null ? "Block null" : (block.isLethal()? "Trap : " : "Block : ") +  "(" + getX() + ", " + getY() + ")");
    }

}
