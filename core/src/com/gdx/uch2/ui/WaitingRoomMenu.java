package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.networking.GamePhase;
import com.gdx.uch2.networking.client.ErrorHandler;
import com.gdx.uch2.networking.client.GameClientHandler;
import com.gdx.uch2.ui.uiUtil.GameParameters;

import java.util.ArrayList;

public class WaitingRoomMenu implements Screen {
    private Stage stage;
    static private ArrayList<String> playersName = new ArrayList<>();
    private GameParameters params;

    public WaitingRoomMenu(GameParameters params){
        // create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        playersName.add(params.nickname);
        this.params = params;
    }

    @Override
    public void show() {


        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();


        stage.addActor(table);
        table.setFillParent(true);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("neon/skin/neon-ui.json"));

        Label hostnameLabel = new Label("hostname : " + params.hostname, skin);
        Label portLabel = new Label("port : " + params.port, skin);
        Label nicknamelabel = new Label("nickname : " + params.nickname, skin);

        VerticalGroup infosGroup = new VerticalGroup();
        infosGroup.columnAlign(Align.topLeft);
        infosGroup.space(10);


        infosGroup.addActor(hostnameLabel);
        infosGroup.addActor(portLabel);
        infosGroup.addActor(nicknamelabel);

        infosGroup.setBounds(50, stage.getHeight() - 70, 70, 50);
        stage.addActor(infosGroup);

        Label titleLabel = new Label("Waiting for players", skin);
        titleLabel.setFontScale(2);
        table.add(titleLabel).center();

        table.row();





    }

    @Override
    public void render(float delta) {
        if (ErrorHandler.getInstance().isSet()) {
            ScreenManager.getInstance().showScreen(new ErrorScreen(ErrorHandler.getInstance().getError()));
            return;
        }

        if(GameClientHandler.currentPhase == GamePhase.Editing){
            Screen s = new PlacementScreen();
            ScreenManager.getInstance().setPlacementScreen(s);
            ScreenManager.getInstance().showScreen(s);
            return;
        }

        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
