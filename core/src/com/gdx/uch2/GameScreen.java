package com.gdx.uch2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.controller.PlayerController;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.view.WorldRenderer;
import com.badlogic.gdx.Input.Keys;

public class GameScreen extends ScreenAdapter implements InputProcessor {
    private World world;
    private WorldRenderer renderer;
    private PlayerController controller;

    private int width, height;

    public GameScreen(World world) {
        this.world = world;
        renderer = new WorldRenderer(world, false);
        controller = new PlayerController(world);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(153f / 255, 187f / 255, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        renderer.render();
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
        if (keycode == Keys.A)
            controller.leftPressed();
        if (keycode == Keys.D)
            controller.rightPressed();
        if (keycode == Keys.W)
            controller.jumpPressed();
        if (keycode == Keys.X)
            controller.firePressed();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.A)
            controller.leftReleased();
        if (keycode == Keys.D)
            controller.rightReleased();
        if (keycode == Keys.W)
            controller.jumpReleased();
        if (keycode == Keys.X)
            controller.fireReleased();
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
        if (button == Input.Buttons.LEFT && controller.isDone()) {
            ScreenManager.getInstance().showScreen(ScreenManager.getInstance().getPlacementScreen());
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

//    Level level;
//    SpriteBatch batch;
//    ExtendViewport viewport;
//    OrthogonalTiledMapRenderer mapRenderer;
//    OrthographicCamera gameCamera;
//
//    @Override
//    public void show() {
//        AssetManager am = new AssetManager();
//        Assets.instance.init(am);
//        level = new Level();
//        batch = new SpriteBatch();
//        gameCamera = new OrthographicCamera();
//        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
//
//        TiledMap map = new TmxMapLoader().load("level/level.tmx");
//        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 70f, batch);
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height, true);
//    }
//
//    @Override
//    public void dispose() {
//        Assets.instance.dispose();
//    }
//
//    @Override
//    public void render(float delta) {
//        level.update(delta);
//        viewport.apply();
//        Gdx.gl.glClearColor(
//                Constants.BACKGROUND_COLOR.r,
//                Constants.BACKGROUND_COLOR.g,
//                Constants.BACKGROUND_COLOR.b,
//                Constants.BACKGROUND_COLOR.a);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
////        batch.setProjectionMatrix(viewport.getCamera().combined);
//        mapRenderer.render();
//        level.render(batch);
//    }
}
