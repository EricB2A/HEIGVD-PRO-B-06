package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.controller.PlayerController;
import com.gdx.uch2.entities.OnlinePlayer;
import com.gdx.uch2.entities.OnlinePlayerManager;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.networking.client.ErrorHandler;
import com.gdx.uch2.networking.client.GameClientHandler;
import com.gdx.uch2.networking.server.GameServer;
import com.gdx.uch2.view.WorldRenderer;

/**
 * Ecran de jeu de phase de mouvement
 */
public class GameScreen extends ScreenAdapter implements InputProcessor {
    private WorldRenderer renderer;
    private PlayerController controller;
    private Stage stage;
    private Label[] nicknamesLabel;
    private float ppuX, ppuY;

    /**
     * Constructuer
     * @param world World sur lequel est basé l'écran
     */
    public GameScreen(World world) {
        stage = new Stage(new ScreenViewport());
        renderer = new WorldRenderer(world, stage.getBatch(), false);
        controller = new PlayerController(world);
        nicknamesLabel = new Label[OnlinePlayerManager.getInstance().getNicknames().length];
        ppuX = stage.getWidth() / world.getLevel().getWidth();
        ppuY = stage.getHeight() / world.getLevel().getHeight();
    }

    @Override
    public void show() {
        for(int i = 0; i < nicknamesLabel.length; ++i) {
            if (i != OnlinePlayerManager.getInstance().getPlayerId()) {
                nicknamesLabel[i] = new Label(OnlinePlayerManager.getInstance().getNicknames()[i],
                        new Label.LabelStyle(new BitmapFont(), null));
                stage.addActor(nicknamesLabel[i]);
            }
        }
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        if (ErrorHandler.getInstance().isSet()) {
            ScreenManager.getInstance().showScreen(new ErrorScreen(ErrorHandler.getInstance().getError()));
            return;
        } else if (GameClientHandler.isOver()) {
            ScreenManager.getInstance().showScreen(new EndGameScreen());
            return;
        } else if (GameClientHandler.isRoundOver()) {
            ScreenManager.getInstance().showScreen(new ScoreScreen(GameClientHandler.getnRound()));
            return;
        }

        Gdx.gl.glClearColor(153f / 255, 187f / 255, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update(delta);
        OnlinePlayerManager.getInstance().updatePlayers(delta);
        renderer.renderBackground();

        for (int i = 0; i < nicknamesLabel.length; ++i) {
            if (i != OnlinePlayerManager.getInstance().getPlayerId()) {
                OnlinePlayer p = OnlinePlayerManager.getInstance().getPlayer(i);
                nicknamesLabel[i].setPosition((p.getPosition().x + Player.OFFSET.x + Player.HITBOX_WIDTH / 2f ) * ppuX - nicknamesLabel[i].getWidth() / 2,
                        (p.getPosition().y + Player.HITBOX_HEIGHT + Player.OFFSET.y + 0.2f) * ppuY);
                nicknamesLabel[i].setAlignment(Align.center);
            }
        }

        stage.draw();

        renderer.renderPlayers();


    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
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
        if (keycode == Keys.K)
            controller.giveUp();
        if(keycode == Keys.ESCAPE) {
            World.currentWorld.stopMusic();
            GameServer.closeConnection();
            ScreenManager.getInstance().showScreen(new MainMenu());
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
//        if (button == Input.Buttons.LEFT && controller.isDone()) {
//            ScreenManager.getInstance().showScreen(ScreenManager.getInstance().getPlacementScreen());
//        }
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
