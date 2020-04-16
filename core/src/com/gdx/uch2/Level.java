package com.gdx.uch2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.uch2.entities.Player;

public class Level {

    Player player;

    public Level() {
        player = new Player();
    }

    public void update(float delta) {
        player.update(delta);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        player.render(batch);
        batch.end();
    }

}
