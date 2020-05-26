package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Trap;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.GamePhase;
import com.gdx.uch2.networking.client.ErrorHandler;
import com.gdx.uch2.networking.client.MessageSender;
import com.gdx.uch2.networking.client.GameClientHandler;
import com.gdx.uch2.networking.server.GameServer;
import com.gdx.uch2.view.WorldRenderer;

/**
 * Ecan de phase de placement
 */
public class PlacementScreen extends ScreenAdapter implements InputProcessor {
    private World world;
    private WorldRenderer renderer;
    private Stage stage;
    private Block.Type blockType;
    private Label message;
    private Label[] choicesLabel;
    private final Label.LabelStyle defaultStyle = new Label.LabelStyle(new BitmapFont(), null);
    private final Label.LabelStyle selectStyle = new Label.LabelStyle(new BitmapFont(), Color.CHARTREUSE);
    private Vector2 mousePosition;

    private int width, height;

    /**
     * Constructeur
     */
    public PlacementScreen() {
        world = World.currentWorld;
    }

    @Override
    public void show() {
        world.resetPlayer();
        stage = new Stage(new ScreenViewport());
        renderer = new WorldRenderer(world, stage.getBatch(), false);
        mousePosition = null;
        blockType = Block.Type.BOX;
        Gdx.input.setInputProcessor(this);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        message = new Label("...", new Label.LabelStyle(new BitmapFont(), null));
        choicesLabel = new Label[] {
            new Label("Key [1] : Pick a box", selectStyle),
            new Label("Key [2] : Pick a lethal trap", defaultStyle)
        };

        table.add(message).center().padBottom(40);
        table.row();
        table.add(new Label("TOOLBOX", new Label.LabelStyle(new BitmapFont(), null))).center().padBottom(20);
        table.row();

        for (Label l : choicesLabel) {
            table.add(l).colspan(2).left();
            table.row();
        }
    }

    @Override
    public void render(float delta) {
        if (ErrorHandler.getInstance().isSet()) {
            ScreenManager.getInstance().showScreen(new ErrorScreen(ErrorHandler.getInstance().getError()));
            return;
        } else if (GameClientHandler.currentPhase == GamePhase.Moving) {
            ScreenManager.getInstance().showScreen(new GameScreen(world));
            return;
        } else if (GameClientHandler.isOver()) {
            ScreenManager.getInstance().showScreen(new EndGameScreen());
            return;
        }

        Gdx.gl.glClearColor(153f / 255, 187f / 255, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.renderBackground();

        if(MessageSender.getInstance().getCanPlace()) {
            message.setText("Place an item on the map\n\n\n");
            if (mousePosition != null
                    && world.getLevel().getBlocks()[(int) mousePosition.x][(int) mousePosition.y] == null) {
                renderer.renderBlock(blockType, mousePosition);
            }
        } else {
            message.setText("Waiting for the other players placing their items\n\n\n");
        }

        stage.draw();
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
        int select = -1;
        switch(keycode){
            case Input.Keys.NUM_1:
            case Input.Keys.NUMPAD_1:
                blockType = Block.Type.BOX;
                select = 0;
                break;

            case Input.Keys.NUM_2:
            case Input.Keys.NUMPAD_2:
                blockType = Block.Type.LETHAL;
                select = 1;
                break;
            case Input.Keys.ESCAPE:
                World.currentWorld.stopMusic();
                GameServer.closeConnection();
                ScreenManager.getInstance().showScreen(new MainMenu());
                return true;
            default:
                break;
        }

        if (select != -1) {
            for (int i = 0; i < choicesLabel.length; ++i) {
                if (select == i) {
                    choicesLabel[i].setStyle(selectStyle);
                } else {
                    choicesLabel[i].setStyle(defaultStyle);
                }
            }
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
        if(MessageSender.getInstance().getCanPlace()){
            if (button == Input.Buttons.LEFT) {
                Vector2 pos = new Vector2(x,height - y);
                renderer.scale(pos);
                x = (int) pos.x;
                y = (int) pos.y;
                Block[][] blocks = world.getLevel().getBlocks();
                if (blocks[x][y] == null) {
                    Block block;
                    if(x >= 0 && y >= 0 && blockType == null) blockType = Block.Type.BOX; // TODO GUILLAUME
                    switch (blockType){
                        case BOX:
                        case BLOCK: block = new Block(new Vector2(x, y), blockType); break;
                        case LETHAL:
                        case G_DOWN:
                        case G_UP: block = new Trap(new Vector2(x, y), blockType); break;
                        default: block = new Block(new Vector2(x, y), Block.Type.BOX); break;
                    }
                    MessageSender.getInstance().sendBlockPlacement(block);
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
        Vector2 pos = new Vector2(screenX,height - screenY);
        renderer.scale(pos);
        pos.x = (float) Math.floor(pos.x);
        pos.y = (float) Math.floor(pos.y);

        if (pos.x >= 0 && pos.y >= 0 && pos.x < world.getLevel().getWidth() && pos.y < world.getLevel().getHeight())
            mousePosition = pos;
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
