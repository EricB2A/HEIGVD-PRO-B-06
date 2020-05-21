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


    public OnlinePlayer(PlayerState initialState) {
        from  = new Vector2(initialState.getPosX(), initialState.getPosY());
        position = new Vector2(initialState.getPosX(), initialState.getPosY());
        to = new Vector2(initialState.getPosX(), initialState.getPosY());
        begin = initialState.getTime();
        end = begin;
        stateTime = 0;
        facingLeft = false;
    }

    public void addUpdate(PlayerState update) {
        from.x = position.x;
        from.y = position.y;
        to.x = update.getPosX();
        to.y = update.getPosY();

        begin = end;
        end = update.getTime();
        stateTime = 0;
    }

    public void update(float delta) {
        stateTime += delta;
        float diff = (end - begin) / 1e9f;

        if (stateTime >= diff) {
            position.x = to.x;
            position.y = to.y;
        } else {
            float scl = stateTime / diff;
            position.x = from.x + (to.x - from.x) * scl;
            position.y = from.y + (to.y - from.y) * scl;
        }

        System.out.println(from + "  " + position);

        if (position.x - from.x != 0)
            facingLeft = position.x - from.x < 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

}
