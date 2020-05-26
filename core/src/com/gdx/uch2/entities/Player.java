package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.effects.Effect;

import java.util.ArrayList;

/**
 * Classe représentant un joueur local
 */
public class Player {

    /**
     * Différents états dans lequel le joueur peut se trouver
     */
    public enum State {
        IDLE, WALKING, JUMPING, SLIDING, DEAD
    }

    public static final float SPEED = 6f;	// unit per second
    public static final float JUMP_VELOCITY = 4f;
    public static final float SIZE = 2f;
    public static final float HITBOX_WIDTH = 0.7f;
    public static final float HITBOX_HEIGHT = 1.4f;
    public static final Vector2 OFFSET = new Vector2(0.65f, 0.25f);

    private ArrayList<Effect> effects = new ArrayList<>();
    private Vector2 	position = new Vector2(0f, 0f);
    private Vector2 	acceleration = new Vector2();
    private Vector2 	velocity = new Vector2();
    private Rectangle 	bounds = new Rectangle();
    private State		state = State.IDLE;
    private boolean		facingLeft = false;
    private float		stateTime = 0;
    private boolean     dead = false;

    /**
     * Constructeur
     * @param position position initiale du joueur
     */
    public Player(Vector2 position) {
        this.bounds.height = HITBOX_HEIGHT;
        this.bounds.width = HITBOX_WIDTH;
        setPosition(position);
    }

    /**
     *
     * @return true si le joueur fait face à la gauche
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     * Modifie la direction du joueur
     * @param facingLeft Fait regarder le joueur à gauche si True, sinon fait regarder le joueur à droite
     */
    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
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
     * @return l'accélération du joueur
     */
    public Vector2 getAcceleration() {
        return acceleration;
    }

    /**
     *
     * @return la vélocité du joueur
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     *
     * @return la hitbox du joueur
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     *
     * @return l'état actuel du joueur
     */
    public State getState() {
        return state;
    }

    /**
     * Change l'état du joueur
     * @param newState le nouvel état
     */
    public void setState(State newState) {
        this.state = newState;
    }

    public float getStateTime() {
        return stateTime;
    }

    /**
     * Entraîne la mort du joueur
     */
    public void kill() {
        dead = true;
    }

    /**
     *
     * @return True si le joueur est mort, false sinon
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Donne une nouvelle position au joueur
     * @param position la nouvelle position
     */
    public void setPosition(Vector2 position) {
        this.position = position;
        this.bounds.x = position.x + OFFSET.x;
        this.bounds.y = position.y + OFFSET.y;
    }

    /**
     * Applique une translation au joueur
     * @param v destination
     */
    public void translate(Vector2 v) {
        this.position.add(v);
        this.bounds.x += v.x;
        this.bounds.y += v.y;
    }

    /**
     * Ajoute un effet spécial au joueur
     * @param effect effet à ajouter
     */
    public void addEffect(Effect effect){
        effects.add(effect);
    }

    /**
     * Indique si le joueur est affecté par l'effet donné
     * @param effect l'effet à tester
     * @return True si le joueur est affecté par l'effet donnée, false sinon
     */
    public boolean hasEffect(Effect effect){
        for(Effect e : effects) {
            if(e.equals(effect)){
                return true;
            }
        }
        return false;
    }

    /**
     * Applique les effets actuels au joueur
     */
    public void applyEffects(){
        for(Effect effect : effects) {
            if(effect.isFinished()){
                effects.remove(effect);
                break;
            }
            effect.apply(this);
        }
    }

    /**
     * met à jour la position du joueur et applique les effets
     * @param delta temps depuis la dernière mise à jour
     */
    public void update(float delta) {
        stateTime += delta;
        applyEffects();
    }
}
