package com.gdx.uch2.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.uch2.controller.LevelLoader;
import com.gdx.uch2.networking.client.MessageSender;
import com.gdx.uch2.networking.messages.PlayerState;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un monde/niveau dans lequel évoluent les personnages
 */
public class World {

    /**
     * Blocks du niveau
     **/
    Array<Rectangle> collisionRects = new Array<>();
//    /** The blocks making up the world **/

    /**
     * Personnage jouable
     **/
    Player player;

    /**
     * Musique d'ambiance
     */
    Sound sound;

    /**
     * Niveau utilisé pour créer le monde
     */
    Level level;

    /**
     * Monde unique actuellement utilisé
     */
    public static World currentWorld;

    /**
     * Constructeur à partir d'un numéro de niveau
     * @param noLevel numéro du niveau à utiliser pour ce monde
     */
    public World(int noLevel) {
        createWorld(noLevel);
        sound = Gdx.audio.newSound(Gdx.files.internal("sound/main_theme.mp3"));
        sound.loop(0.2f);
    }

    /**
     * Arrête la musique
     */
    public void stopMusic(){
        sound.stop();
    }

    // Getters -----------

    /**
     *
     * @return le personnage jouable
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return le niveau sur lequel est basé le monde
     */
    public Level getLevel() {
        return level;
    }

    /**
     *
     * @return les blocks du monde
     */
    public Array<Rectangle> getCollisionRects() {
        return collisionRects;
    }
    // --------------------

    /**
     * Retourne uniquement les blocks qui doivent être dessinés
     * @param width largeur
     * @param height hauteur
     * @return les blocks qui doivent être dessinés
     */
    public List<Block> getDrawableBlocks(int width, int height) {
        int x = (int) player.getBounds().x - width;
        int y = (int) player.getBounds().y - height;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int x2 = x + 2 * width;
        int y2 = y + 2 * height;
        if (x2 > level.getWidth()) {
            x2 = level.getWidth() - 1;
        }
        if (y2 > level.getHeight()) {
            y2 = level.getHeight() - 1;
        }

        List<Block> blocks = new ArrayList<>();
        Block block;
        for (int col = x; col <= x2; col++) {
            for (int row = y; row <= y2; row++) {
                block = level.getBlocks()[col][row];
                if (block != null) {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }


    /**
     * Place un block dans le monde
     * @param b le block à placer
     */
    public void placeBlock(Block b){
        level.getBlocks()[(int) b.getPosition().x][(int) b.getPosition().y] = b;
    }

    /**
     * Supprime un block du niveau aux coordonnées données
     * @param x coordonnée x
     * @param y coordonnée y
     */
    public void removeBlock(int x, int y){
        level.getBlocks()[x][y] = null;
        System.out.println("suppression du bloc");
    }

    private void createWorld(int noLevel) {
        level = LevelLoader.loadLevel(noLevel);
        resetPlayer();
    }

    /**
     * Recrée un nouveau personnage jouable au début du niveau
     */
    public void resetPlayer() {
        player = new Player(new Vector2(level.getSpawnPosition()));
        MessageSender.getInstance().setCurrentState(
                new PlayerState(OnlinePlayerManager.getInstance().getPlayerId(),
                        Player.State.IDLE, player.getPosition().x, player.getPosition().y,0));
    }
}