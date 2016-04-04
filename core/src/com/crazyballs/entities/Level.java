package com.crazyballs.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.crazyballs.Constants;

import java.util.HashMap;

/**
 * Created by Hector on 27/02/2016.
 */
public class Level extends Stage {

   public Array<Wall> wallList = new Array<Wall>();
    public Array<Wall> activatorWallList = new Array<Wall>();
    public final int id;
   public Vector2 startPosition;
    public HashMap<String, com.crazyballs.entities.FriendlyBall> friendlyBalls;

    public Level(final int id){
        super(new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        this.id = id;
        friendlyBalls = new HashMap<String, com.crazyballs.entities.FriendlyBall>();
    }

    public void addWall(Wall wall){
        wallList.add(wall);
        if(wall.isActivator()){
            activatorWallList.add(wall);
        }
        System.out.println("Adding wall to wallist: " + wallList.size);
        this.addActor(wall);
    }

    public int getId(){
        return this.id;
    }

    public void setStartPosition(Vector2 startPosition){
        this.startPosition = startPosition;
    }
    public Vector2 getStartPosition(){
        return this.startPosition;
    }

    public void detachWalls(){

        for (Wall walls : wallList){
            walls.detach();
        }

        wallList.clear();
        activatorWallList.clear();
    }

    public void setIgnoreWalls(boolean ignore){
        if(ignore){
            for (Wall walls : wallList){
                walls.getFixture().setUserData("Ignorar");
                walls.getFixture().setSensor(true);
            }
        }else{
            for (Wall walls : wallList) {
                if (walls.isResetableWall()) {
                    walls.getFixture().setUserData("resetableWall");
                    walls.getFixture().setSensor(false);
                } else if (walls.isActivator()) {
                    walls.getFixture().setUserData(walls.activatorUserData);
                    walls.getFixture().setSensor(true);
                } else if (walls.isFinish()) {
                    walls.getFixture().setUserData("finishWall");
                    walls.getFixture().setSensor(false);
                } else {
                    walls.getFixture().setUserData("wall");
                    walls.getFixture().setSensor(false);
                }
            }
        }


    }

    public void drawFriendlyBalls(SpriteBatch batch){
        for(HashMap.Entry<String, com.crazyballs.entities.FriendlyBall> entry : friendlyBalls.entrySet()){
            entry.getValue().draw(batch);
        }
    }

    public void resetFriendlyBalls(){
        friendlyBalls.clear();
        System.out.println("limpiando amigos: " + friendlyBalls.size());
    }

}
