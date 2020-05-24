package com.gdx.uch2.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gdx.uch2.ScreenManager;
import com.gdx.uch2.networking.client.GameClient;

public class JoinRoomMenu implements Screen {
    private Stage stage;
    private static String hostname = "localhost";

    public JoinRoomMenu(){
        // create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
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
        Label titleLabel = new Label("Ultimate Chicken Horse 2", skin);
        titleLabel.setFontScale(2);
        final Label nicknameLabel = new Label("Nickname:", skin);
        Label ipLabel = new Label("Hostname:", skin);
        Label portLabel = new Label("Port:", skin);
        final Label errorLabel = new Label("", skin);
        errorLabel.setWidth(100);

        // Create TextField
        TextField.TextFieldFilter.DigitsOnlyFilter digitsFilter = new TextField.TextFieldFilter.DigitsOnlyFilter();
        final TextField nicknameTF = new TextField("Player 1", skin);
        final TextField ipTF = new TextField(hostname, skin);
        final TextField portTF = new TextField("12345", skin);
        portTF.setTextFieldFilter(digitsFilter);
        nicknameTF.setMaxLength(20);
        ipTF.setMaxLength(30);
        portTF.setMaxLength(5);

        // Title
        HorizontalGroup titleGroup = new HorizontalGroup();
        titleGroup.space(10);
        titleGroup.addActor(titleLabel);
        titleGroup.addActor(chickenImg);
        table.add(titleGroup).colspan(2).center();
        table.row();

        // Nickname
        HorizontalGroup nicknameGroup = new HorizontalGroup();
        nicknameGroup.space(10);
        nicknameGroup.addActor(nicknameLabel);
        nicknameGroup.addActor(nicknameTF);
        table.add(nicknameGroup).colspan(2).center();
        table.row();

        // IP
        HorizontalGroup ipGroup = new HorizontalGroup();
        ipGroup.space(10);
        ipGroup.addActor(ipLabel);
        ipGroup.addActor(ipTF);
        table.add(ipGroup).colspan(2).center();
        table.row();

        // Port
        HorizontalGroup portGroup = new HorizontalGroup();
        portGroup.space(10);
        portGroup.addActor(portLabel);
        portGroup.addActor(portTF);
        table.add(portGroup).colspan(2).center();
        table.row();

        //create buttons
        TextButton joinButton = new TextButton("Join", skin);
        TextButton mainMenuButton = new TextButton("Go back", skin);

        //add buttons to table
        table.add(joinButton).width(200).colspan(2);
        table.row();
        table.add(mainMenuButton).width(200).colspan(2);
        table.row();

        // Error label
        table.add(errorLabel).colspan(2).center();

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
        joinButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                boolean somethingWentWrong = false;
                int port = 0;
                String nickname = null;
                try {
                    port = Integer.parseInt(String.valueOf(portTF.getText()));
                    nickname = String.valueOf(nicknameTF.getText());

                    if (port < 1025 || port > 65535 || nickname.length() == 0) {
                        somethingWentWrong = true;
                    }
                } catch (NumberFormatException e) {
                    somethingWentWrong = true;
                }

                if (somethingWentWrong) {
                    errorLabel.setText("One or more invalid arguments");
                    return;
                }

                hostname = ipTF.getText();
                new GameClient(ipTF.getText(), port, nickname);
                Screen s = new WaitingRoomMenu(nickname);
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

