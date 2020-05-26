package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.networking.messages.PlayerState;

/**
 * Classe représentant un adversaire distant
 */
public class OnlinePlayer {
    private Vector2 from;
    private Vector2 position;
    private Vector2 to;
    private long begin;
    private long end;
    private float stateTime;
    private boolean facingLeft;
    private boolean falling;
    private String nickname;
    private Player.State state;
    private float localTime = 0;

    /**
     * Constructeur à partir d'un playerState
     * @param initialState état initial du nouveau joueur
     */
    private OnlinePlayer(PlayerState initialState) {
        from  = new Vector2(initialState.getPosX(), initialState.getPosY());
        position = new Vector2(initialState.getPosX(), initialState.getPosY());
        to = new Vector2(initialState.getPosX(), initialState.getPosY());
        begin = initialState.getTime();
        end = begin;
        stateTime = 0;
        facingLeft = false;
        falling = false;
    }

    /**
     * Constructeur à partir d'un ID et d'un pseudonyme
     * @param playerId l'ID du joueur
     * @param nickname le pseudonyme du joueur
     */
    public OnlinePlayer(int playerId, String nickname) {
        this(new PlayerState(playerId, Player.State.IDLE, 0, 0, 0));
        this.nickname = nickname;
    }

    /**
     *
     * @return le pseudonyme du joueur
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Lit une mise à jour de l'état du joueur
     * @param update mise à jour à lire
     */
    public void addUpdate(PlayerState update) {
        from.x = position.x;
        from.y = position.y;
        to.x = update.getPosX();
        to.y = update.getPosY();

        begin = end;
        end = update.getTime();
        state = update.getState();
        stateTime = 0;
    }

    /**
     * Met à jour le playerState du joueur par rapport à la dernière mise à jour lue
     * @param delta différence de temps entre l'ancien playerState et le nouveau
     */
    public void update(float delta) {
        stateTime += delta;
        localTime += delta;
        float diff = (end - begin) / 1e9f;

        if (stateTime >= diff) {
            position.x = to.x;
            position.y = to.y;
        } else {
            float scl = stateTime / diff;
            position.x = from.x + (to.x - from.x) * scl;
            position.y = from.y + (to.y - from.y) * scl;
        }

        if (position.x - from.x != 0)
            facingLeft = position.x - from.x < 0;

        if (position.y - from.y != 0)
            falling = position.y - from.y < 0;
    }

    /**
     *
     * @return la position du joueur
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     *
     * @return True si le joueur est tourné vers la gauche, false sinon
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     *
     * @return True si le joueur est en train de tomber
     */
    public boolean isFalling() {
        return falling;
    }

    /**
     *
     * @return l'état actuel du joueur
     */
    public Player.State getState() {
        return state;
    }

    /**
     *
     * @return True si le joueur est mort, false sinon
     */
    public boolean isDead() {
        return state == Player.State.DEAD;
    }

    /**
     *
     * @return le timestamp de l'heure locale actuelle par rapport au début de la partie
     */
    public float getLocalTime() {
        return localTime;
    }
}
