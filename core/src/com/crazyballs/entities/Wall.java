package com.crazyballs.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.crazyballs.Constants;

/**
 * Created by Hector on 13/02/2016.
 */
public class Wall extends Actor {

    /** Textura de las paredes. */
    private Texture wallTexture;

    /** El mundo en el que se encuentra la pared. */
    private World world;

    /** El cuerpo de la pared. */
    private Body body;



    /** The fixtures assigned to both bodies. This gives bodies shape. */
    private Fixture fixture;

    private float width, height;


    private boolean isResetableWall;
    private boolean isActivator;
    private boolean isFinish;
    public String activatorUserData;
    public int wallToQuit;

    /**
     * Create a new floor. Le pasamos la posición inferior izquierda, el ancho y el alto
     */
    public Wall(World world, Texture wallTexture, Vector2 position, float width, float height, boolean isResetableWall, boolean isActivator, String activatorUserData, boolean isFinish, int wallToQuit) {
        this.world = world;

        this.wallTexture = wallTexture;
        this.isResetableWall = isResetableWall;
        this.isActivator = isActivator;
        this.activatorUserData = activatorUserData;
        this.isFinish = isFinish;
        this.width = width;
        this.height = height;

        // Creando el cuerpo de la pared.
        BodyDef def = new BodyDef();
        def.position.set(position.x + width / 2f, position.y + height / 2f);  // (2) Centrar la pared en las coordenadas
        body = world.createBody(def);               // (3) Create the floor. Easy.

        // Give it a box shape.
        PolygonShape box = new PolygonShape();      // (1) Create the polygon shape.
        box.setAsBox(width/2f, height/2f);              // (2) Give it some size.
        fixture = body.createFixture(box, 1f);       // (3) Create a fixture.

        if(isResetableWall()){
            fixture.setUserData("resetableWall");
        }else if(isActivator()) {
            fixture.setUserData(activatorUserData);
            fixture.setSensor(true);
            this.wallToQuit = wallToQuit;
        }else if(isFinish()) {
            fixture.setUserData("finishWall");
        }else
        {
            fixture.setUserData("wall");
        }


                        // (4) Set the user data for the fixture.
        box.dispose();                              // (5) Destroy the shape.


        // Now place the actor in the stage by converting the coordinates given in meters to px.
        setSize(width* Constants.PIXELS_IN_METER, height*Constants.PIXELS_IN_METER);
        this.setPosition((body.getPosition().x - width/2f)* Constants.PIXELS_IN_METER, (body.getPosition().y- height/2f)* Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Render both textures.
        batch.draw(wallTexture, getX(), getY(), getWidth(), getHeight());

    }


    public void detach() {
        body.destroyFixture(fixture);

        world.destroyBody(body);
    }

    public boolean isResetableWall() {
        return isResetableWall;
    }

    public void setIsResetableWall(boolean isResetableWall) {
        this.isResetableWall = isResetableWall;
    }

    public boolean isActivator() {
        return isActivator;
    }

    public void setIsActivator(boolean isActivator) {
        this.isActivator = isActivator;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }
}
