package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.networking.PlayerState;

import java.util.LinkedList;
import java.util.Queue;

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
    float localTime = 0;


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

    public OnlinePlayer(int playerId, String nickname) {
        this(new PlayerState(playerId, Player.State.IDLE, 0, 0, 0));
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

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

    public Vector2 getPosition() {
        return position;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public boolean isFalling() {
        return falling;
    }

    public Player.State getState() {
        return state;
    }

    public boolean isDead() {
        return state == Player.State.DEAD;
    }

    public float getLocalTime() {
        return localTime;
    }
}
