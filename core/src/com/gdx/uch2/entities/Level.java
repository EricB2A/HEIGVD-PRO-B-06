package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un niveau
 */
public class Level {

    private int width;
    private int height;
    private Block[][] blocks;
    private Vector2 spawnPosition;
    private Vector2 finishPosition;

    /**
     * Constructeur sans arguments
     */
    public Level() {  }

    /**
     *
     * @return la largeur du niveau
     */
    public int getWidth() {
        return width;
    }

    /**
     * Donne une valeur à la largeur du niveau
     * @param width nouvelle largeur
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     *
     * @return la hauteur du niveau
     */
    public int getHeight() {
        return height;
    }

    /**
     * Donne une valeur à la hauteur du niveau
     * @param height nouvelle hauteur
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     *
     * @return les blocks du niveau
     */
    public Block[][] getBlocks() {
        return blocks;
    }

    /**
     * Donne un nouvel agencement de blocks
     * @param blocks le nouvel agencement de blocks
     */
    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    /**
     * Obtient le block à la position donnée en paramètre
     * @param x position X du block demandé
     * @param y position Y du block demandé
     * @return le block demandé
     */
    public Block get(int x, int y) {
        return blocks[x][y];
    }

    /**
     *
     * @return la position d'apparition des joueurs dans ce niveau
     */
    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

    /**
     * Donne une nouvelle valeur à la position d'apparition des joueurs
     */
    public void setSpawnPosition(Vector2 spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    /**
     *
     * @return La position de l'arrivée
     */
    public Vector2 getFinishPosition() {
        return finishPosition;
    }

    /**
     *
     * @return Le block d'arrivée
     */
    public Block getFinishBlock() {
        return new Block(finishPosition);
    }

    /**
     * Donne une nouvelle valeur à la position du block d'arrivée
     * @param finishPosition la nouvelle position du block d'arrivée
     */
    public void setFinishPosition(Vector2 finishPosition) {
        this.finishPosition = finishPosition;
    }
}