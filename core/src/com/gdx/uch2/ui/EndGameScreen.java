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
import com.gdx.uch2.entities.OnlinePlayerManager;

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

    private int[] getScore(){
        return OnlinePlayerManager.getInstance().getScores();
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
        /*
        Image chickenImg = new Image(new Texture(Gdx.files.internal(("chicken.png"))));
        chickenImg.setWidth(129);
        chickenImg.setHeight(200);
         */

        int[] scores = getScore();
        String[] nicknames = OnlinePlayerManager.getInstance().getNicknames();

        //TODO Change text with nickname and points of players
        // Create TextField
        Label[] nickNamePlayers = new Label[scores.length];
        Label[] scorePlayers = new Label[scores.length];

        int winnerScore = 0;
        int winnerID = -1;
        for(int i = 0; i < scores.length; ++i){
            nickNamePlayers[i] = new Label(nicknames[i], skin);
            nickNamePlayers[i].setWidth(100);
            scorePlayers[i] = new Label(Integer.toString(scores[i]), skin);
            scorePlayers[i].setWidth(100);
            if(scores[i] > winnerScore){
                winnerScore = scores[i];
                winnerID = i;
            }
        }

        // Create Text
        Label titleLabel = new Label("End de partie ! ", skin);
        titleLabel.setFontScale(2);
       /* final Label nicknameLabel = new Label("Nickname", skin);
        nicknameLabel.setFontScale(1);*/
        Label gagnant = new Label("Le winner est : Player " + winnerID, skin);
        Label score = new Label("Score", skin);
        score.setFontScale(1);
        gagnant.setFontScale(2);

        // Title
        HorizontalGroup titleGroup = new HorizontalGroup();
        titleGroup.space(10);
        titleGroup.addActor(titleLabel);
        //titleGroup.addActor(chickenImg);
        table.add(titleGroup).colspan(2).center();
        table.row();


        //Infos
        HorizontalGroup infoGroup = new HorizontalGroup();
        infoGroup.space(30);
//        infoGroup.addActor(nicknameLabel);
        infoGroup.addActor(gagnant);
        table.add(infoGroup).colspan(2).uniform();
        infoGroup.space(30);
        table.row();
        table.add(score).colspan(2).center();
        table.row();


        //Players
        for(int i = 0; i < scores.length; ++i){
            HorizontalGroup playerGroup = new HorizontalGroup();
            playerGroup.space(30);
            playerGroup.addActor(nickNamePlayers[i]);
            playerGroup.addActor(scorePlayers[i]);
            table.add(playerGroup).colspan(2).uniform();
            table.row();
        }

        //create buttons
        TextButton mainMenuButton = new TextButton("Main menu", skin);

        //add buttons to table
        table.add(mainMenuButton).width(200).colspan(2);

        // create button listeners
        mainMenuButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Screen s = new MainMenu();
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