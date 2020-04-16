package com.gdx.uch2.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class PlayerAssets {
    //public final TextureAtlas.AtlasRegion standingLeft;
    public final TextureAtlas.AtlasRegion standingRight;
    //public final TextureAtlas.AtlasRegion walkingLeft;
    public final TextureAtlas.AtlasRegion walkingRight;
    /*public final TextureAtlas.AtlasRegion jumpingLeft;
    public final TextureAtlas.AtlasRegion jumpingRight;*/

    //public final Animation walkingLeftAnimation;
    public final Animation walkingRightAnimation;
    public final Animation standingRightAnimation;


    public PlayerAssets(TextureAtlas atlas) {
        standingRight = atlas.findRegion("idle1");
        walkingRight = atlas.findRegion("walk-right1");

        Array<TextureAtlas.AtlasRegion> walkingRightFrames = new Array<TextureAtlas.AtlasRegion>();
        for(int i = 1; i <= 7; i++){
            walkingRightFrames.add(atlas.findRegion("walk-right" + i));
        }
        walkingRightAnimation = new Animation(Constants.LOOP_SPEED, walkingRightFrames, Animation.PlayMode.LOOP);

        Array<TextureAtlas.AtlasRegion> standingRightFrames = new Array<TextureAtlas.AtlasRegion>();
        for(int i = 1; i <= 3; i++){
            standingRightFrames.add(atlas.findRegion("idle" + i));
        }
        standingRightAnimation = new Animation(Constants.LOOP_SPEED, standingRightFrames, Animation.PlayMode.LOOP);
    }
}
