package com.crazyballs.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.crazyballs.Constants;

/**
 * Created by Hector on 20/02/2016.
 */

public class FriendlyBall  extends Sprite {
    private Vector2 position;

    /** The world instance this player is in. */
    private World world;
    /** The body for this player. */
    private Body body;

    /** The fixture for this player. */
    private Fixture fixture;

    private static final float SIZE = 0.9f;         //tamaño en metros

    public FriendlyBall(World world, Texture texture, Vector2 startPosition) {
        super(texture);
        this.world = world;
        BodyDef def = new BodyDef();                // (1) Create the body definition.
        def.position.set(startPosition);                 // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.
        body = world.createBody(def);               // (4) Now create the body.

        // Give it some shape.
        //PolygonShape box = new PolygonShape();      // (1) Create the shape.
        // box.setAsBox(0.5f, 0.5f);                   // (2) 1x1 meter box.
        CircleShape circle = new CircleShape();
        circle.setRadius(SIZE/2f);
        fixture = body.createFixture(circle, 1);       // (3) Create the fixture.
        fixture.setSensor(true);
        fixture.setUserData("friendlyBall");              // (4) Set the user data.
        circle.dispose();
        this.setAlpha(0.5f);
        this.setSize(SIZE * Constants.PIXELS_IN_METER, SIZE * Constants.PIXELS_IN_METER);
        this.setPosition((startPosition.x - SIZE/2)*Constants.PIXELS_IN_METER, (startPosition.y - SIZE/2)*Constants.PIXELS_IN_METER);
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }


}
