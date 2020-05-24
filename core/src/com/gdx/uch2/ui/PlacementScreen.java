package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.GamePhase;
import com.gdx.uch2.networking.client.ErrorHandler;
import com.gdx.uch2.networking.client.ClientPlayerStateTickManager;
import com.gdx.uch2.networking.client.GameClient;
import com.gdx.uch2.networking.client.GameClientHandler;
import com.gdx.uch2.view.WorldRenderer;

public class PlacementScreen extends ScreenAdapter implements InputProcessor {
    private World world;
    private WorldRenderer renderer;
    private String text;
    private Block.Type blockType;

    private int width, height;

    public PlacementScreen() {
        world = World.currentWorld;
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
        if (ErrorHandler.getInstance().isSet()) {
            ScreenManager.getInstance().showScreen(new ErrorScreen(ErrorHandler.getInstance().getError()));
            return;
        } else if (GameClientHandler.currentPhase == GamePhase.Moving) {
            ScreenManager.getInstance().showScreen(new GameScreen(world));
            return;
        }

        Gdx.gl.glClearColor(153f / 255, 187f / 255, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //TODO update le gamescreen ici si la phase de mouvement a commencé

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
        super.dispose();
    }

    // * InputProcessor methods ***************************//

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.NUM_1:
            case Input.Keys.NUMPAD_1: blockType = Block.Type.BOX; break;
            case Input.Keys.NUM_2:
            case Input.Keys.NUMPAD_2: blockType = Block.Type.BLOCK; break;
            case Input.Keys.NUM_3:
            case Input.Keys.NUMPAD_3: blockType = Block.Type.LETHAL; break;
            case Input.Keys.NUM_4:
            case Input.Keys.NUMPAD_4: blockType = Block.Type.G_DOWN; break;
            case Input.Keys.NUM_5:
            case Input.Keys.NUMPAD_5: blockType = Block.Type.G_UP; break;
            default: blockType = Block.Type.BOX; break;
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
        if(ClientPlayerStateTickManager.getInstance().getCanPlace()){
            if (button == Input.Buttons.LEFT) {
                Vector2 pos = new Vector2(x,height - y);
                renderer.scale(pos);
                x = (int) pos.x;
                y = (int) pos.y;
                Block[][] blocks = world.getLevel().getBlocks();
                if (blocks[x][y] == null) {
                    Block block;
                    if(blockType == null) blockType = Block.Type.BOX; // TODO GUILLAUME
                    switch (blockType){
                        case BOX:
                        case BLOCK: block = new Block(new Vector2(x, y), blockType); break;
                        case LETHAL:
                        case G_DOWN:
                        case G_UP: block = new Trap(new Vector2(x, y), blockType); break;
                        default: block = new Block(new Vector2(x, y), Block.Type.BOX); break;
                    }
                    ClientPlayerStateTickManager.getInstance().sendBlockPlacement(block);
                }
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
