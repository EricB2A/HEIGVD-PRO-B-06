package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class EndGameScreen implements Screen {
    private Stage stage;
    List<Object> objects = new ArrayList<>();

    public EndGameScreen(List<Object> objects){
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.objects = objects;
    }

    @Override
    public void show() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);

        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("neon/skin/neon-ui.json"));

        // Create Image
        Image chickenImg = new Image(new Texture(Gdx.files.internal(("chicken.png"))));
        chickenImg.setWidth(129);
        chickenImg.setHeight(200);

        // Create Text
        Label titleLabel = new Label("Ultimate Chicken Horse 2 \n\n End Level ! Results", skin);
        titleLabel.setFontScale(2);
        final Label nicknameLabel = new Label("Nickname", skin);
        nicknameLabel.setFontScale(1);
        Label score = new Label("Score", skin);
        score.setFontScale(1);

        //TODO Change text with nickname and points of players
        // Create TextField
        final Label nicknamePlayer1 = new Label("Player 1", skin);
        final Label nicknamePlayer2 = new Label("Player 2", skin);

        final Label scorePlayer1 = new Label("100", skin);
        final Label scorePlayer2 = new Label("80", skin);
        nicknamePlayer1.setWidth(100);
        nicknamePlayer2.setWidth(100);
        scorePlayer1.setWidth(100);
        scorePlayer2.setWidth(100);

        // Title
        HorizontalGroup titleGroup = new HorizontalGroup();
        titleGroup.space(10);
        titleGroup.addActor(titleLabel);
        titleGroup.addActor(chickenImg);
        table.add(titleGroup).colspan(2).center();
        table.row();

        //Infos
        HorizontalGroup infoGroup = new HorizontalGroup();
        infoGroup.space(30);
        infoGroup.addActor(nicknameLabel);
        infoGroup.addActor(score);
        table.add(infoGroup).colspan(2).uniform();
        table.row();

        //Players
        HorizontalGroup player1Group = new HorizontalGroup();
        player1Group.space(30);
        player1Group.addActor(nicknamePlayer1);
        player1Group.addActor(scorePlayer1);
        table.add(player1Group).colspan(2).uniform();
        table.row();

        //Players
        HorizontalGroup player2Group = new HorizontalGroup();
        player2Group.space(30);
        player2Group.addActor(nicknamePlayer2);
        player2Group.addActor(scorePlayer2);
        table.add(player2Group).colspan(2).uniform();
        table.row();

        //create buttons
        TextButton createButton = new TextButton("Continue", skin);
        TextButton mainMenuButton = new TextButton("Main menu", skin);

        //add buttons to table
        table.add(createButton).width(200).colspan(2);
        table.row();
        table.add(mainMenuButton).width(200).colspan(2);

        // create button listeners
        createButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //TODO Add player nickname for waiting room
                Screen s = new WaitingRoomMenu("nickname");
                ScreenManager.getInstance().setPlacementScreen(s);
                ScreenManager.getInstance().showScreen(s);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        mainMenuButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Screen s = new MainMenu();
                ScreenManager.getInstance().setPlacementScreen(s);
                ScreenManager.getInstance().showScreen(s);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

    }

    @Override
    public void render(float delta) {
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