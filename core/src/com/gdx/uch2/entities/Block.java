package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un block sur le niveau
 */
public class Block {

    /**
     * Taille par rapport à la grille du niveau
     */
    public static final float SIZE = 1f;

    /**
     * Différents types de blocks
     */
    public enum Type {
        BLOCK, BOX, LETHAL, G_UP, G_DOWN, PROTECTED_AREA
    }

    private Vector2 	position;
    private Rectangle 	bounds = new Rectangle();
    protected Type      type = Type.BLOCK;
    private boolean     isSolid = true;


    /**
     * Constructeur prenant une position en paramètre
     * @param pos la position du block
     */
    public Block(Vector2 pos) {
        this.position = pos;
        this.bounds.setX(pos.x);
        this.bounds.setY(pos.y);
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }

    /**
     * Constructeur prenant une position et un type en paramètre
     * @param pos la position du block
     * @param type le type de block
     */
    public Block(Vector2 pos, Type type) {
        this.position = pos;
        this.bounds.setX(pos.x);
        this.bounds.setY(pos.y);
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
        this.type = type;
    }

    /**
     *
     * @return la position du block
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     *
     * @return la bordure du block
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     *
     * @return true si le block est un piège, false sinon
     */
    public boolean isLethal() {
        return false;
    }

    /**
     *
     * @return true si le block est solide, false sinon
     */
    public boolean isSolid() {
        return isSolid;
    }

    /**
     * Applique l'action d'un block sur le joueur qui le touche
     * @param player le joueur sur lequel appliquer l'action du block
     */
    public void action(Player player) { }

    /**
     *
     * @return le type du block
     */
    public Type getType() {
        return type;
    }
}
