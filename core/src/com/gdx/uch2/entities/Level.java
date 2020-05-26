package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe représentant un niveau
 */
public class Level {

    private int width;
    private int height;
    private Block[][] blocks;
    private Vector2 spawnPosition;
    private List<Vector2> finishPositions = new LinkedList<>();

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
     * @return Les positions des arrivées
     */
    public List<Vector2> getFinishPositions() {
        return finishPositions;
    }

    /**
     * @return Les blocks d'arrivée
     */
    public Block[] getFinishBlocks() {
        Block[] ret = new Block[finishPositions.size()];
        for(int i = 0; i < finishPositions.size(); ++i){
            ret[i] = new Block(finishPositions.get(i));
        }
        return ret;
    }

    /**
     * Ajoute une position d'arrivée
     * @param newFinishPosition la position du nouveau block d'arrivée
     */
    public void addFinishPosition(Vector2 newFinishPosition) {
        finishPositions.add(newFinishPosition);
    }
}