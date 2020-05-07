package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.GameScreen;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.controller.PlayerController;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.view.WorldRenderer;

public class PlacementScreen extends ScreenAdapter implements InputProcessor {
    private World world;
    private WorldRenderer renderer;
    private String text;
    private int blockType = 1;

    private int width, height;

    public PlacementScreen() {
        world = new World();
    }

    @Override
    public void show() {
        text = "Place a block";
        world.resetPlayer();
        renderer = new WorldRenderer(world, false);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(153f / 255, 187f / 255, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.renderBackground();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void dispose() {

    }

    // * InputProcessor methods ***************************//

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.NUM_1 || keycode == Input.Keys.NUMPAD_1) {
            blockType = 1;
        }
        if (keycode == Input.Keys.NUM_2 || keycode == Input.Keys.NUMPAD_2){
            blockType = 2;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Vector2 pos = new Vector2(x,height - y);
            renderer.scale(pos);
            x = (int) pos.x;
            y = (int) pos.y;
            Block[][] blocks = world.getLevel().getBlocks();
            if (blocks[x][y] == null) {
                if (blockType == 2) {
                    blocks[x][y] = new Trap(new Vector2(x, y));
                } else {
                    blocks[x][y] = new Block(new Vector2(x, y));
                }
                ScreenManager.getInstance().showScreen(new GameScreen(world));
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
