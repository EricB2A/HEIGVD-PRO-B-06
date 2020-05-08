package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.effects.Effect;

import java.util.ArrayList;

public class Player {
    public enum State {
        IDLE, WALKING, JUMPING, SLIDING
    }

    public static final float SPEED = 6f;	// unit per second
    public static final float JUMP_VELOCITY = 4f;
    public static final float SIZE = 2f;
    public static final float HITBOX_WIDTH = 0.7f;
    public static final float HITBOX_HEIGHT = 1.4f;
    public static final Vector2 OFFSET = new Vector2(0.65f, 0.25f);

    ArrayList<Effect> effects = new ArrayList<>();
    Vector2 	position = new Vector2(0f, 0f);
    Vector2 	acceleration = new Vector2();
    Vector2 	velocity = new Vector2();
    Rectangle 	bounds = new Rectangle();
    State		state = State.IDLE;
    boolean		facingLeft = false;
    float		stateTime = 0;
    boolean		longJump = false;

    public Player(Vector2 position) {
        this.bounds.height = HITBOX_HEIGHT;
        this.bounds.width = HITBOX_WIDTH;
        setPosition(position);
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public State getState() {
        return state;
    }

    public void setState(State newState) {
        this.state = newState;
    }

    public float getStateTime() {
        return stateTime;
    }

    public boolean isLongJump() {
        return longJump;
    }

    public void setLongJump(boolean longJump) {
        this.longJump = longJump;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.bounds.x = position.x + OFFSET.x;
        this.bounds.y = position.y + OFFSET.y;
    }

    public void translate(Vector2 v) {
        this.position.add(v);
        this.bounds.x += v.x;
        this.bounds.y += v.y;
    }

    public void addEffect(Effect effect){
        effects.add(effect);
    }

    public boolean hasEffect(Effect effect){
        for(Effect e : effects) {
            if(e.equals(effect)){
                return true;
            }
        }
        return false;
    }

    public void applyEffects(){
        for(Effect effect : effects) {
            if(effect.isFinished()){
                effects.remove(effect);
                continue;
            }
            effect.apply(this);
        }
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void update(float delta) {
        stateTime += delta;
        applyEffects();
    }
}
