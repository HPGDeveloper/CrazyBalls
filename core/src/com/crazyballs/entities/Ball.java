package com.crazyballs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.crazyballs.Constants;

/**
 * Created by Hector on 13/02/2016.
 */
public class Ball extends Actor {

    /** The player texture. */
    private Texture texture;

    /** The world instance this player is in. */
    private World world;

    /** The body for this player. */
    private Body body;

    private Vector2 start_position;

    /** The fixture for this player. */
    private Fixture fixture;

    private boolean isAlive = true;

    private static final float SIZE = 0.9f;         //tamaño en metros

    public Ball(World world, Texture texture, Vector2 start_position) {
        this.world = world;
        this.texture = texture;
        this.start_position = start_position;

        // Create the player body.
        BodyDef def = new BodyDef();                // (1) Create the body definition.
        def.position.set(start_position);                 // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.
        body = world.createBody(def);               // (4) Now create the body.

        // Give it some shape.
        //PolygonShape box = new PolygonShape();      // (1) Create the shape.
       // box.setAsBox(0.5f, 0.5f);                   // (2) 1x1 meter box.
        CircleShape circle = new CircleShape();
        circle.setRadius(SIZE/2f);
        fixture = body.createFixture(circle, 1);       // (3) Create the fixture.
        fixture.setUserData("ball");              // (4) Set the user data.
        circle.dispose();                              // (5) Destroy the shape.

        // Pone el tamaño en los pixels correspondientes
        setSize(SIZE* Constants.PIXELS_IN_METER, SIZE*Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Always update the position of the actor when you are going to draw it, so that the
        // position of the actor on the screen is as accurate as possible to the current position
        // of the Box2D body.
        setPosition((body.getPosition().x - SIZE/2)*Constants.PIXELS_IN_METER, (body.getPosition().y - SIZE/2)*Constants.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        // Jump when you touch the screen.
        if (Gdx.input.justTouched()) {
            //Accion cuando tocamos la pantalla
        }


            // Only change X speed. Do not change Y speed because if the player is jumping,
            // this speed has to be managed by the forces applied to the player. If we modify
            // Y speed, jumps can get very very weir.d
         body.setLinearVelocity(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX());


    }



    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Vector2 getStart_position() {
        return start_position;
    }

    public void setStart_position(Vector2 start_position) {
        this.start_position = start_position;
    }


    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public static float getSize() {
        return SIZE;
    }

    public float getXPosition(){
        return ((body.getPosition().x - SIZE/2));
    }

    public float getYPosition(){
        return ((body.getPosition().y - SIZE/2));
    }

}
