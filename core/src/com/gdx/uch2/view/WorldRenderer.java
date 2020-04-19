package com.gdx.uch2.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.gdx.uch2.entities.Block;
import com.gdx.uch2.entities.Player;
import com.gdx.uch2.entities.Player.State;
import com.gdx.uch2.entities.World;
import com.gdx.uch2.util.Constants;

public class WorldRenderer {

    private static final float CAMERA_WIDTH = 30f;
    private static final float CAMERA_HEIGHT = 15f;
    private static final float RUNNING_FRAME_DURATION = 0.06f;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    /** Textures **/
    private TextureRegion blockTexture;
    private TextureRegion spawnTexture;
    private TextureRegion finishTexture;
    private TextureRegion playerFrame;
    private TextureRegion playerJumpLeft;
    private TextureRegion playerFallLeft;
    private TextureRegion playerJumpRight;
    private TextureRegion playerFallRight;

    /** Animations **/
    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;
    private Animation idleRightAnimation;
    private Animation idleLeftAnimation;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;
    private float ppuX;	// pixels per unit on the X axis
    private float ppuY;	// pixels per unit on the Y axis

    public void setSize (int w, int h) {
        this.width = w;
        this.height = h;
        float ppu = Math.min(width / CAMERA_WIDTH, height / CAMERA_HEIGHT);
        ppuX = ppu;
        ppuY = ppu;
    }
    public boolean isDebug() {
        return debug;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
//        this.cam.position.set(world.getPlayer().getPosition().x, world.getPlayer().getPosition().y, 0);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        TextureAtlas playerAtlas = new TextureAtlas(Gdx.files.internal(Constants.PLAYER_1_ATLAS));
        TextureAtlas blocksAtlas = new TextureAtlas(Gdx.files.internal(Constants.BLOCKS_ATLAS));
        blockTexture = blocksAtlas.findRegion("box");
        spawnTexture = blocksAtlas.findRegion("signRight");
        finishTexture = blocksAtlas.findRegion("signExit");

        Array<TextureAtlas.AtlasRegion> idleRightFrames = new Array<TextureAtlas.AtlasRegion>();
        Array<TextureAtlas.AtlasRegion> idleLeftFrames = new Array<TextureAtlas.AtlasRegion>();
        for(int i = 1; i <= 4; i++){
            idleRightFrames.add(playerAtlas.findRegion("idle" + i));
            TextureAtlas.AtlasRegion tmp = new TextureAtlas.AtlasRegion(idleRightFrames.get(i-1));
            tmp.flip(true, false);
            idleLeftFrames.add(tmp);
        }
        idleRightAnimation = new Animation(Constants.LOOP_SPEED, idleRightFrames, Animation.PlayMode.LOOP);
        idleLeftAnimation = new Animation(Constants.LOOP_SPEED, idleLeftFrames, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> walkingRightFrames = new Array<TextureAtlas.AtlasRegion>();
        Array<TextureAtlas.AtlasRegion> walkingLeftFrames = new Array<TextureAtlas.AtlasRegion>();
        for(int i = 1; i <= 7; i++){
            walkingRightFrames.add(playerAtlas.findRegion("walk-right" + i));
            TextureAtlas.AtlasRegion tmp = new TextureAtlas.AtlasRegion(walkingRightFrames.get(i-1));
            tmp.flip(true, false);
            walkingLeftFrames.add(tmp);
        }
        walkRightAnimation = new Animation(Constants.LOOP_SPEED, walkingRightFrames, Animation.PlayMode.LOOP);
        walkLeftAnimation = new Animation(Constants.LOOP_SPEED, walkingLeftFrames, Animation.PlayMode.LOOP);

        playerJumpRight = playerAtlas.findRegion("jump1");
        playerJumpLeft = new TextureRegion(playerJumpRight);
        playerJumpLeft.flip(true, false);
        playerFallRight = playerAtlas.findRegion("jump2");
        playerFallLeft = new TextureRegion(playerFallRight);
        playerFallLeft.flip(true, false);
    }


    public void render() {
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        drawBlocks();
        drawPlayer();
        spriteBatch.end();
        if (debug) {
            drawDebug();
            drawCollisionBlocks();
        }
    }


    private void drawBlocks() {
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
//            spriteBatch.draw(blockTexture, block.getPosition().x * ppuX, block.getPosition().y * ppuY, Block.SIZE * ppuX, Block.SIZE * ppuY);
            spriteBatch.draw(blockTexture, block.getPosition().x, block.getPosition().y, Block.SIZE, Block.SIZE);
        }

        spriteBatch.draw(spawnTexture, world.getLevel().getSpanPosition().x, world.getLevel().getSpanPosition().y, Block.SIZE, Block.SIZE);
        spriteBatch.draw(finishTexture, world.getLevel().getFinishPosition().x, world.getLevel().getFinishPosition().y, Block.SIZE, Block.SIZE);
    }

    private void drawPlayer() {
        Player player = world.getPlayer();
        playerFrame = (TextureRegion) (player.isFacingLeft() ? idleLeftAnimation.getKeyFrame(player.getStateTime(), true) : idleRightAnimation.getKeyFrame(player.getStateTime(), true));
        if(player.getState().equals(State.WALKING)) {
            playerFrame = (TextureRegion) (player.isFacingLeft() ? walkLeftAnimation.getKeyFrame(player.getStateTime(), true) : walkRightAnimation.getKeyFrame(player.getStateTime(), true));
        } else if (player.getState().equals(State.JUMPING)) {
            if (player.getVelocity().y > 0) {
                playerFrame = player.isFacingLeft() ? playerJumpLeft : playerJumpRight;
            } else {
                playerFrame = player.isFacingLeft() ? playerFallLeft : playerFallRight;
            }
        }
//        spriteBatch.draw(playerFrame, player.getPosition().x * ppuX, player.getPosition().y * ppuY, Player.SIZE * ppuX, Player.SIZE * ppuY);
        spriteBatch.draw(playerFrame, player.getPosition().x, player.getPosition().y, Player.SIZE, Player.SIZE);
    }

    private void drawDebug() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getDrawableBlocks((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT)) {
            Rectangle rect = block.getBounds();
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        // render Player
        Player player = world.getPlayer();
        Rectangle rect = player.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        }
        debugRenderer.end();

    }
}