package com.gdx.uch2.entities;

import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.networking.PlayerState;

import java.util.LinkedList;
import java.util.Queue;

public class OnlinePlayer {
    private Vector2 position;
    private Vector2 to;
    private long begin;
    private long end;
    private float stateTime;
    private Queue<PlayerState> upcomingUpdates;

    public OnlinePlayer(PlayerState initialState) {
        position.x = initialState.getPosX();
        position.y = initialState.getPosY();
        begin = initialState.getTime();
        stateTime = 0;
        upcomingUpdates = new LinkedList<>();
    }

    public void addUpdate(PlayerState update) {
        if (to == null) {
            to = new Vector2(update.getPosX(), update.getPosY());
            end = update.getTime();
        } else {
            upcomingUpdates.add(update);
        }
    }

    public void update(float delta) {
        if (to == null) {
            return;
        }

        stateTime += delta;
        float diff = (end - begin) / 10e9f;
        while (stateTime > diff) {
            if (upcomingUpdates.isEmpty()) {
                break;
            }

            PlayerState s = upcomingUpdates.remove();
            begin = end;

            position.x = to.x;
            position.y = to.y;

            to.x = s.getPosX();
            to.y = s.getPosY();
            end = s.getTime();
            stateTime -= diff;
            diff = (end - begin) / 10e9f;
        }

        if (stateTime >= diff) {
            begin = end;

            position = to;
            to = null;
            stateTime = 0;
        } else {
            position = position.add(to.sub(position).scl(diff / stateTime));
        }
    }

    public Vector2 getPosition() {
        return position;
    }

}
