package com.gdx.uch2.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;

public class MainMenu implements Screen {
    private Stage stage;
    private TextButton join;

    public MainMenu(){
        /// create stage and set it as input processor

    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);

        //table.setDebug(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("neon/skin/neon-ui.json"));

        // Create Image
        Image chicken = new Image(new Texture(Gdx.files.internal(("chicken.png"))));
        chicken.setWidth(129);
        chicken.setHeight(200);

        // Create Text
        Label title = new Label("Ultimate Chicken Horse 2", skin);
        title.setFontScale(2);
        Label nickname = new Label("Nickname", skin);
        Label joinRoom = new Label("Join Room", skin);
        Label createRoom = new Label("Create Room", skin);

        // Add to table
        table.add(title).expandX().right().padRight(10);
        table.add(chicken).expandX().left().padLeft(10);
        table.row();

        //create buttons
        join = new TextButton("Join", skin);
        TextButton create = new TextButton("Create", skin);
        TextButton quit = new TextButton("Quit", skin);

        //add buttons to table
        table.add(join).width(200).colspan(2);
        table.row();
        table.add(create).width(200).colspan(2);
        table.row();
        table.add(quit).width(200).colspan(2);

        // create button listeners
        quit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        join.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Screen s = new PlacementScreen();
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
