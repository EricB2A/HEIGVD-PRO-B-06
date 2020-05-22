package com.gdx.uch2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.entities.World;

import java.util.ArrayList;

public class WaitingRoomMenu implements Screen {
    private Stage stage;
    static private ArrayList<String> playersName = new ArrayList<>();

    public WaitingRoomMenu(String nickname){
        // create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        playersName.add(nickname);
       // getPlayersList();
    }

    public void getPlayersList(){
        // Get the lis of players from the server
        playersName.add("MEME-LORD");
        playersName.add("MEME-LORD");
        playersName.add("MEME-LORD");
        playersName.add("MEME-LORD");
    }

    public boolean canStartGame(){
        if(playersName.size() <= 2){
            //return false;
        }
        // Ask the server if enough peoples are connected
        return true;
    }

    @Override
    public void show() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);

        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("neon/skin/neon-ui.json"));

        Label titleLabel = new Label("Waiting for players", skin);
        titleLabel.setFontScale(2);
        table.add(titleLabel).center();
        table.row();

        for (String s : playersName){
            TextButton playerName = new TextButton(s, skin);
            playerName.padTop(20);
            table.add(playerName).width(200);
            table.row();
        }

        if(canStartGame()){
            Screen s = new PlacementScreen();
            ScreenManager.getInstance().setPlacementScreen(s);
            ScreenManager.getInstance().showScreen(s);
        }

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
