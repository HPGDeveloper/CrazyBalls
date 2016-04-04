package com.crazyballs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.crazyballs.Constants;
import com.crazyballs.MainGame;
import com.crazyballs.entities.Ball;
import com.crazyballs.entities.EntityFactory;
import com.crazyballs.entities.FriendlyBall;
import com.crazyballs.entities.Level;
import com.crazyballs.entities.Wall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Hector on 13/02/2016.
 */
public class GameScreen extends BaseScreen {

    public final int LEVEL_0 = 0;
    public final int LEVEL_1 = 1;
    public final int LEVEL_2 = 2;
    public final int LEVEL_3 = 3;
    public final int LEVEL_4 = 4;
    public final int LEVEL_5 = 5;
    public final int LEVEL_6 = 6;
    public final int LEVEL_7 = 7;
    public final int LEVEL_8 = 8;
    public final int FINAL_LEVEL = 9;


    public int currentLevel;

    private Array<Level> levels;

    /** Mundo de Box2D. */
    private World world;

    /** conexion socket **/
    private Socket socket;

    private SpriteBatch batch;

    private EntityFactory factory;

    /* id de la pelota*/
    String id;

    /* id de la pelota a borrar si se desconecta */
    private String ballToDetach;

    private boolean isNextLevel = false;

    /*Si estamos conectados al nivel*/
    private boolean isConnected;

    /* Si se ha desconectado algún amigo*/
    private boolean isFriendlyBallDisconnect;

    /* Frecuencia de envio de mensajes*/
    private final float UPDATE_TIME = 45/60f;
    float timer;

    private boolean isfinalLevel;
    Ball player;

    private String nameTexture;

    private int playersOnline = 0;

    BitmapFont font, font_final_level;
    String text, final_level_text;
    GlyphLayout lay, lay_final_level;

    /**
     * Crea la pantalla.
     */
    public GameScreen(MainGame game) {
        super(game);

        //No estamos conectados al empezar
        isConnected = false;
        batch = new SpriteBatch();

        levels = new Array<Level>();

        currentLevel = 0;
        isfinalLevel = false;

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        font_final_level = new BitmapFont(Gdx.files.internal("font.fnt"));
        font_final_level.getData().setScale(0.5f);
        text = "Online: " + playersOnline;
        final_level_text = "Gracias por jugar. Crearemos nuevos niveles dentro de poco.";
        lay = new GlyphLayout(font, text);
        lay_final_level = new GlyphLayout(font_final_level, final_level_text);

        // Crea el mundo de Box2D
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new GameContactListener());
    }

    /**
     * Este método se ejecuta cuando se gva a renderizar la pantalla.
     * Se utiliza para construir el escenario.
     */
    @Override
    public void show() {

        factory = new EntityFactory(game.getManager());
        // Crea los nuveles
        Level level_0 = new Level(LEVEL_0);
        /*Posicion inicial en metros*/
        level_0.setStartPosition(new Vector2(1.5f, 5.5f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_0.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, 3.5f));
        level_0.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_0.addWall(factory.createWall(world, new Vector2(0f, 6.5f), 16f, 3.5f));

        level_0.addWall(factory.createFinishWall(world, new Vector2(14f, 4f), 2f, 2f));


        Level level_1 = new Level(LEVEL_1);
        /*Posicion inicial en metros*/
        level_1.setStartPosition(new Vector2(15f, 6f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_1.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_1.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_1.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_1.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        level_1.addWall(factory.createWall(world, new Vector2(15f, 6f), 1f, 3f));
        level_1.addWall(factory.createWall(world, new Vector2(13f, 6f), 3f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(13f, 4f), 1f, 3f));
        level_1.addWall(factory.createWall(world, new Vector2(8f, 4f), 5f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(7f, 5f), 2f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(6f, 6f), 3f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(3f, 7f), 4f, 1f));

        level_1.addWall(factory.createWall(world, new Vector2(1f, 3f), 12f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(1f, 1f), 5f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(0f, 5f), 4f, 1f));

        level_1.addWall(factory.createWall(world, new Vector2(1f, 7f), 1f, 2f));
        level_1.addWall(factory.createWall(world, new Vector2(10f, 6f), 1f, 3f));
        level_1.addWall(factory.createWall(world, new Vector2(5f, 1f), 1f, 4f));
        level_1.addWall(factory.createWall(world, new Vector2(8f, 1f), 1f, 2f));

        level_1.addWall(factory.createWall(world, new Vector2(15f, 3f), 1f, 2f));
        level_1.addWall(factory.createWall(world, new Vector2(10f, 1f), 4f, 1f));
        level_1.addWall(factory.createWall(world, new Vector2(10f, 0f), 1f, 1f));

        level_1.addWall(factory.createFinishWall(world, new Vector2(13f, 7f), 2f, 2f));

        //Para que no colisione con estas paredes
        level_1.setIgnoreWalls(true);

        // Crea los nuveles
        Level level_2 = new Level(LEVEL_2);
        /*Posicion inicial en metros*/
        level_2.setStartPosition(new Vector2(1f, 1f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_2.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_2.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_2.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_2.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        level_2.addWall(factory.createResetableWall(world, new Vector2(2f, 5f), 10.5f, 3f));
        level_2.addWall(factory.createResetableWall(world, new Vector2(2f, 0f), 1.8f, 5f));
        level_2.addWall(factory.createResetableWall(world, new Vector2(5f, 1f), 11f, 2f));
        level_2.addWall(factory.createResetableWall(world, new Vector2(14f, 3f), 2f, 8f));

        level_2.addWall(factory.createFinishWall(world, new Vector2(15f, 0f), 1f, 1f));

        //Para que no colisione con estas paredes
        level_2.setIgnoreWalls(true);

        // Crea los nuveles
        Level level_3 = new Level(LEVEL_3);
        /*Posicion inicial en metros*/
        level_3.setStartPosition(new Vector2(2.5f, 3.5f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_3.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_3.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_3.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_3.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        //Wall a esconder por el activator wall (4)
        level_3.addWall(factory.createWall(world, new Vector2(8f, 0f), 0.2f, 9f));
        level_3.addWall(factory.createActivatorWall(world, new Vector2(2f, 3f), 2f, 2f, "level3_wall4", 4));

        level_3.addWall(factory.createFinishWall(world, new Vector2(12f, 3f), 2f, 2f));

        //Para que no colisione con estas paredes
        level_3.setIgnoreWalls(true);


        // Crea los nuveles
        Level level_4 = new Level(LEVEL_4);
        /*Posicion inicial en metros*/
        level_4.setStartPosition(new Vector2(0f, 3.5f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_4.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_4.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_4.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_4.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(4f, 0f), 2f, 2f));
        level_4.addWall(factory.createFinishWall(world, new Vector2(7f, 0f), 2f, 2f));
        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(10f, 0f), 2f, 2f));
        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(13f, 0f), 2f, 2f));

        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(4f, 7f), 2f, 2f));
        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(7f, 7f), 2f, 2f));
        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(10f, 7f), 2f, 2f));
        level_4.addWall(factory.createFalseFinishWall(world, new Vector2(13f, 7f), 2f, 2f));

        level_4.addWall(factory.createWall(world, new Vector2(0f, 2f), 4f, 1f));
        level_4.addWall(factory.createWall(world, new Vector2(0f, 5f), 4f, 1f));
        level_4.addWall(factory.createWall(world, new Vector2(3f, 0f), 1f, 2f));

        level_4.addWall(factory.createWall(world, new Vector2(6f, 0f), 1f, 3f));
        level_4.addWall(factory.createWall(world, new Vector2(9f, 0f), 1f, 3f));
        level_4.addWall(factory.createWall(world, new Vector2(12f, 0f), 1f, 3f));
        level_4.addWall(factory.createWall(world, new Vector2(15f, 0f), 1f, 9f));

        level_4.addWall(factory.createWall(world, new Vector2(3f, 6f), 1f, 3f));
        level_4.addWall(factory.createWall(world, new Vector2(6f, 6f), 1f, 4f));
        level_4.addWall(factory.createWall(world, new Vector2(9f, 6f), 1f, 4f));
        level_4.addWall(factory.createWall(world, new Vector2(12f, 6f), 1f, 4f));
        //Para que no colisione con estas paredes
        level_4.setIgnoreWalls(true);

        // Crea los nuveles
        Level level_5 = new Level(LEVEL_5);
        /*Posicion inicial en metros*/
        level_5.setStartPosition(new Vector2(0f, 0f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_5.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_5.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_5.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_5.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        //Wall a esconder por el activator wall (4)
        level_5.addWall(factory.createWall(world, new Vector2(1f, 0f), 1f, 8f));
        level_5.addWall(factory.createWall(world, new Vector2(5f, 0f), 1f, 8f));
        level_5.addWall(factory.createWall(world, new Vector2(9f, 0f), 1f, 8f));
        level_5.addWall(factory.createWall(world, new Vector2(13f, 0f), 1f, 8f));
        level_5.addWall(factory.createWall(world, new Vector2(3f, 1f), 1f, 8f));
        level_5.addWall(factory.createWall(world, new Vector2(7f, 1f), 1f, 8f));
        level_5.addWall(factory.createWall(world, new Vector2(11f, 1f), 1f, 8f));

        level_5.addWall(factory.createFinishWall(world, new Vector2(14f, 0f), 2f, 2f));

        //Para que no colisione con estas paredes
        level_5.setIgnoreWalls(true);

        // Crea los nuveles
        Level level_6 = new Level(LEVEL_6);
        /*Posicion inicial en metros*/
        level_6.setStartPosition(new Vector2(0f, 4f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_6.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_6.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_6.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_6.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        //Wall a esconder por el activator wall (4)
        level_6.addWall(factory.createWall(world, new Vector2(0f, 3f), 2f, 1f));
        level_6.addWall(factory.createActivatorWall(world, new Vector2(7f, 2f), 2f, 1f, "level6_wall4", 4));

        level_6.addWall(factory.createWall(world, new Vector2(2f, 0f), 1f, 8f));
        level_6.addWall(factory.createWall(world, new Vector2(14f, 0f), 1f, 3f));
        level_6.addWall(factory.createWall(world, new Vector2(14f, 4f), 1f, 4f));

        level_6.addWall(factory.createWall(world, new Vector2(4f, 1f), 1f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(3f, 2f), 1f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(6f, 1f), 6f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(11f, 2f), 2f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(9f, 2f), 1f, 2f));
        level_6.addWall(factory.createWall(world, new Vector2(6f, 3f), 4f, 1f));

        level_6.addWall(factory.createWall(world, new Vector2(3f, 7f), 6f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(11f, 7f), 3f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(4f, 5f), 5f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(10f, 5f), 2f, 1f));
        level_6.addWall(factory.createWall(world, new Vector2(11f, 4f), 2f, 1f));

        level_6.addWall(factory.createWall(world, new Vector2(4f, 3f), 1f, 2f));
        level_6.addWall(factory.createWall(world, new Vector2(6f, 4f), 1f, 1f));

        level_6.addWall(factory.createFinishWall(world, new Vector2(0f, 0f), 2f, 2f));

        //Para que no colisione con estas paredes
        level_6.setIgnoreWalls(true);

        // Crea los nuveles
        Level level_7 = new Level(LEVEL_7);
        /*Posicion inicial en metros*/
        level_7.setStartPosition(new Vector2(0f, 8f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_7.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_7.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_7.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_7.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        //Wall a esconder por el activator wall (4)
        level_7.addWall(factory.createWall(world, new Vector2(6f, 7f), 1f, 1f));
        level_7.addWall(factory.createActivatorWall(world, new Vector2(3f, 4f), 8f, 1f, "level7_wall4", 4));

        level_7.addWall(factory.createWall(world, new Vector2(0f, 7f), 6f, 1f));
        level_7.addWall(factory.createWall(world, new Vector2(7f, 7f), 8f, 1f));
        level_7.addWall(factory.createWall(world, new Vector2(1f, 5f), 12f, 1f));
        level_7.addWall(factory.createWall(world, new Vector2(3f, 3f), 10f, 1f));
        level_7.addWall(factory.createWall(world, new Vector2(1f, 1f), 14f, 1f));

        level_7.addWall(factory.createWall(world, new Vector2(1f, 2f), 1f, 3f));
        level_7.addWall(factory.createWall(world, new Vector2(14f, 2f), 1f, 5f));
        level_7.addWall(factory.createWall(world, new Vector2(12f, 4f), 1f, 1f));

        level_7.addWall(factory.createFinishWall(world, new Vector2(11f, 4f), 1f, 1f));

        //Para que no colisione con estas paredes
        level_7.setIgnoreWalls(true);

        // Crea los nuveles
        Level level_8 = new Level(LEVEL_8);
        /*Posicion inicial en metros*/
        level_8.setStartPosition(new Vector2(7.5f, 6f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        level_8.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        level_8.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        level_8.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        level_8.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        //Wall a esconder por el activator wall (4)
        level_8.addWall(factory.createWall(world, new Vector2(7f, 5f), 2f, 1f));
        level_8.addWall(factory.createActivatorWall(world, new Vector2(4.8f, 0f), 1.2f, 1f, "level8_wall4", 4));
        level_8.addWall(factory.createWall(world, new Vector2(7f, 3f), 2f, 1f));
        level_8.addWall(factory.createActivatorWall(world, new Vector2(10f, 6f), 1f, 1f, "level8_wall6", 6));

        level_8.addWall(factory.createWall(world, new Vector2(6f, 0f), 1f, 8f));
        level_8.addWall(factory.createWall(world, new Vector2(9f, 0f), 1f, 8f));
        level_8.addWall(factory.createWall(world, new Vector2(3f, 7f), 3f, 1f));
        level_8.addWall(factory.createWall(world, new Vector2(10f, 7f), 4f, 1f));

        level_8.addWall(factory.createResetableWall(world, new Vector2(4f, 0f), 0.8f, 6f));
        level_8.addWall(factory.createResetableWall(world, new Vector2(13f, 0f), 0.8f, 6f));
        level_8.addWall(factory.createResetableWall(world, new Vector2(2f, 1f), 0.8f, 6f));
        level_8.addWall(factory.createResetableWall(world, new Vector2(11f, 1f), 0.8f, 6f));
        level_8.addWall(factory.createResetableWall(world, new Vector2(15f, 1f), 0.8f, 6f));

        level_8.addWall(factory.createFinishWall(world, new Vector2(7f, 0f), 2f, 2f));

        //Para que no colisione con estas paredes
        level_8.setIgnoreWalls(true);

        // Crea los nuveles
        Level final_level = new Level(FINAL_LEVEL);
        /*Posicion inicial en metros*/
        final_level.setStartPosition(new Vector2(2.5f, 3.5f));
        // Creamos las paredes de la pantalla con la posicion x, y de la esquina inferior izquierda y el tamaño en metros.
        final_level.addWall(factory.createWall(world, new Vector2(0f, 0f), 16f, -0.1f));
        final_level.addWall(factory.createWall(world, new Vector2(0f, 0f), -0.1f, 9f));
        final_level.addWall(factory.createWall(world, new Vector2(16f, 0f), -0.1f, 9f));
        final_level.addWall(factory.createWall(world, new Vector2(0f, 9f), 16f, -0.1f));

        //Para que no colisione con estas paredes
        final_level.setIgnoreWalls(true);


        levels.add(level_0);
        levels.add(level_1);
        levels.add(level_2);
        levels.add(level_3);
        levels.add(level_4);
        levels.add(level_5);
        levels.add(level_6);
        levels.add(level_7);
        levels.add(level_8);
        levels.add(final_level);


        //levels.get(0).setDebugAll(true);
        //Nos conectamos al socket
        connectSocket();
        configSocketEvents();
    }

    /**
     * Se ejecuta este método cuando la pantalla ya no es la activa
     * Se utiliza para destruir lo que se ha utilizado en este stage.
     */
    @Override
    public void hide() {
        // Quitamos todos los Actor del escenario.

        for(Level level : levels){
            level.clear();
            level.detachWalls();
        }

        if(player != null){
            player.detach();
        }

        /*levels.get(currentLevel).clear();
        levels.get(currentLevel).getPlayer().detach();
        levels.get(currentLevel).detachWalls();*/
    }

    /**
     * Enviamos mensajes a los otros jugadores
     */
    public void updateServer(float dt){
        timer += dt;
        if(timer >= UPDATE_TIME && player != null){
            JSONObject data = new JSONObject();
            try{
                data.put("x", player.getXPosition());
                data.put("y", player.getYPosition());
                data.put("room", currentLevel);
                socket.emit("playerMoved", data);
            }catch (JSONException e){
                Gdx.app.log("SOCKET.IO", "Error sending update data");
            }
        }
    }

    /**
     * Se llama cada vez que se renderiza la pantalla.
     */
    @Override
    public void render(float delta) {
        // Limpiamos la pantalla cada vez.
        Gdx.gl.glClearColor(0.11f, 0.56f, 1.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(isConnected){
            updateServer(Gdx.graphics.getDeltaTime());
            levels.get(currentLevel).act();
            //Dibujamos los otros jugadores del stage actual
            batch.begin();
            levels.get(currentLevel).drawFriendlyBalls(batch);
            batch.end();

            // Actualizamos el mundo (las fisicas y la posicion de las entidades).
            world.step(delta, 6, 2);

            //Eliminamos los desconectados de este stage o los que han pasado de nivel
            if(isFriendlyBallDisconnect){
                levels.get(currentLevel).friendlyBalls.get(ballToDetach).detach();
                levels.get(currentLevel).friendlyBalls.remove(ballToDetach);
                isFriendlyBallDisconnect = false;
                Gdx.app.log("SocketIO", "Number of other players: " + levels.get(currentLevel).friendlyBalls.size());
            }

            if (isNextLevel){
                levels.get(currentLevel).setIgnoreWalls(true);
                levels.get(currentLevel).clear();
                levels.get(currentLevel).detachWalls();
                levels.get(currentLevel).resetFriendlyBalls();
                JSONObject data = new JSONObject();
                try{
                    data.put("level", currentLevel);
                    socket.emit("finishLevel", data);
                }catch (JSONException e){
                    Gdx.app.log("SOCKET.IO", "Error sending update data");
                }
                currentLevel++;
                if(currentLevel == FINAL_LEVEL){
                    isfinalLevel = true;
                }
                levels.get(currentLevel).setIgnoreWalls(false);
                restartPlayer();
                isNextLevel = false;
            }

            if(player != null){
                //Restart jugador
                if(!player.isAlive()){
                    restartPlayer();
                }

            }
            levels.get(currentLevel).draw();
            batch.begin();
            font.draw(batch, text, Constants.SCREEN_WIDTH *0.8f - (lay.width / 2), Constants.SCREEN_HEIGHT * 0.95f);
            if(isfinalLevel){
                font_final_level.draw(batch, final_level_text, Constants.SCREEN_WIDTH /2 - (lay_final_level.width / 2), Constants.SCREEN_HEIGHT /2);
            }
            batch.end();
        }

    }

    /*Cuando el jugador muere o cambia de nivel*/
    public void restartPlayer(){
        player.detach();
        player = factory.createPlayer(world, new Vector2(levels.get(currentLevel).getStartPosition()), nameTexture);
        levels.get(currentLevel).addActor(player);
    }
    /**
     * Cuando la pantalla se deja de ver
     */
    @Override
    public void dispose() {

        for(Level level : levels){
            level.dispose();
        }
        //levels.get(currentLevel).dispose();
        world.dispose();
    }
    /**Devuelve la textura del jugador**/
    public String getNameTexture() {
        return nameTexture;
    }
    /**Crea la textura del jugador**/
    public void setNameTexture(String nameTexture) {
        this.nameTexture = nameTexture;
    }

    /**
     * El contact listener que comprueba las colisiones.
     */
    private class GameContactListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

            if (userDataA == null || userDataB == null) {
                return false;
            }

            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                    (userDataA.equals(userB) && userDataB.equals(userA));
        }

        /**
         * Método que se llama cuando empieza una colision: cuando dos fixtures colisionan.
         */
        @Override
        public void beginContact(Contact contact) {

            //Si el nivel actual tiene paredes activadoras
            Array<Wall> activatorWallList = levels.get(currentLevel).activatorWallList;
            if (activatorWallList != null && activatorWallList.size > 0) {
                for (Wall wall : activatorWallList) {
                    //Comprovamos con cual de todas a colisionado
                    if (areCollided(contact, "ball", wall.activatorUserData)) {
                        //Quitamos la pared que debe desaparecer
                        levels.get(currentLevel).wallList.get(wall.wallToQuit).getFixture().setSensor(true);
                        levels.get(currentLevel).wallList.get(wall.wallToQuit).setVisible(false);

                        //Enviamos mensaje de que esta colisionando con la pared activadora
                        JSONObject data = new JSONObject();
                        try {
                            data.put("activatorWallId", wall.wallToQuit);
                            data.put("level", currentLevel);
                            data.put("isColliding", true);
                            socket.emit("activatorWallCollision", data);
                        } catch (JSONException e) {
                            Gdx.app.log("SOCKET.IO", "Error sending update data");
                        }

                        //Si durante la colision pulsamos
                       /* if (Gdx.input.isTouched()) {
                        }*/
                    }
                }
            }

            // El jugador choca con la pared de reset.
            if (areCollided(contact, "ball", "resetableWall")) {
                player.setIsAlive(false);

            }

            // El jugador pasa el nivel
            if (areCollided(contact, "ball", "finishWall")) {
                isNextLevel = true;
            }
        }

        /**
         * Método se ejecuta cuando el contacto termina, dos fixtures dejan de colisionar.
         */
        @Override
        public void endContact(Contact contact) {

            //Si el nivel actual tiene paredes activadoras
            Array<Wall> activatorWallList = levels.get(currentLevel).activatorWallList;
            if (activatorWallList != null && activatorWallList.size > 0) {
                for (Wall wall : activatorWallList) {
                    //Comprobamos con cual de todas a colisionado
                    if (areCollided(contact, "ball", wall.activatorUserData)) {
                        // Se vuelve a mostrar el muro.
                        levels.get(currentLevel).wallList.get(wall.wallToQuit).setVisible(true);
                        levels.get(currentLevel).wallList.get(wall.wallToQuit).getFixture().setSensor(false);

                        //Se envia un mensaje de que no se esta colisionando
                        JSONObject data = new JSONObject();
                        try{
                            data.put("activatorWallId", wall.wallToQuit);
                            data.put("level", currentLevel);
                            data.put("isColliding", false);
                            socket.emit("activatorWallCollision", data);
                        }catch (JSONException e){
                            Gdx.app.log("SOCKET.IO", "Error sending update data");
                        }

                    }
                }
            }
        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }


    /**
    * Eventos de intercambio de mensajes
    **/
    public void configSocketEvents(){

        /**
         * Cuando nos conectamos creamos nuestro jugador en el nivel 0.
         * Con la textura que se haya seleccionado en el Screen anterior.
         * Añadimos el jugador al stage del nivel 0.
         * Se envia nuestra textura (y se queda almacenada en una variable en el index.js)
         **/
       socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                //Creamos jugador en el nivel 0, en posicion inicial y marcamos como conectado
                currentLevel = LEVEL_0;
                //Si ya se han creado los niveles
                if(levels.size != 0){
                    player = factory.createPlayer(world, new Vector2(levels.get(LEVEL_0).getStartPosition()), nameTexture);
                    levels.get(LEVEL_0).addActor(player);
                }
                isConnected = true;

                JSONObject data = new JSONObject();
                try{
                    data.put("texture", nameTexture);
                    socket.emit("playerTexture", data);
                }catch (JSONException e){
                    Gdx.app.log("SOCKET.IO", "Error sending update data");
                }
            }
           /**
            * Cuando recibimos el mensaje 'socketID' almacenamos el id de nuestro jugador
            **/
        }).on("socketID", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   id = data.getString("id");
                   Gdx.app.log("SocketIO", "My ID: " + id);
               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }
           /**
            * Cuando recibimos el mensaje 'newPlayer' es porque se ha unido un nuevo jugador a nuestro nivel.
            * Obtenemos su textura.
            * Añadimos ese jugador a la lista de jugadores de nuestro nivel.
            **/
       }).on("newPlayer", new Emitter.Listener() {
           @Override
           public void call(Object... args) {

               JSONObject data = (JSONObject) args[0];
               try {
                   //Se ha unido un jugador a nuestro nivel
                   String playerId = data.getString("id");
                   String playerTexture = data.getString("texture");
                   levels.get(currentLevel).friendlyBalls.put(playerId, factory.createFriendlyPlayer(world, levels.get(currentLevel).getStartPosition(), playerTexture));
                   Gdx.app.log("SocketIO", "New Player Connect With Id: " + playerId + "texture: " + playerTexture);
                   Gdx.app.log("SocketIO", "Number of other players: " + levels.get(currentLevel).friendlyBalls.size());

               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }

/**
 * Cuando recibimos el mensaje 'playerDisconnected' es porque se ha desconectado un jugador de nuestro nivel.
 * Preparamos el jugador para su borrado.
 **/
        }).on("playerDisconnected", new Emitter.Listener(){
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   //Se ha desconectado un jugador de nuestro nivel
                       String playerId = data.getString("id");
                       ballToDetach = playerId;
                       isFriendlyBallDisconnect = true;
                       Gdx.app.log("SocketIO", "Player Disconnect With Id: " + playerId);
               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }
           /**
            * Cuando recibimos el mensaje 'playerMoved' es porque un jugador de nuestro nivel se ha movido.
            * Actualizamos su posicion.
            **/
       }).on("playerMoved", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   //Un jugador se ha movido en nuestro nivel
                   String playerId = data.getString("id");
                   Double x = data.getDouble("x") * Constants.PIXELS_IN_METER;
                   Double y = data.getDouble("y") * Constants.PIXELS_IN_METER;
                   if (levels.get(currentLevel).friendlyBalls.get(playerId) != null) {
                       levels.get(currentLevel).friendlyBalls.get(playerId).setPosition(x.floatValue(), y.floatValue());
                   }
                   //Gdx.app.log("SocketIO", "Player Moved With Id: " + playerId);
               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }

           /**
            * Cuando recibimos el mensaje 'getPlayers' es porque nos hemos unido a un nuevo nivel y queremos
            * saber los jugadores que hay para dibujarlos.
            * Obtenemos la textura del jugador.
            * Obtenemos los jugadores del nivel y los añadimos a la lista (siempre y cuando no seamos nosotros).
            **/
       }).on("getPlayers", new Emitter.Listener(){
           @Override
           public void call(Object... args) {
               JSONArray objects = (JSONArray) args[0];
               try {
                   //Cuantos jugadores hay en este nivel
                   for(int i = 0; i < objects.length(); i++){
                       int friendLevel = objects.getJSONObject(i).getInt("level");
                       String friendId = objects.getJSONObject(i).getString("id");
                       if(friendLevel == currentLevel && !id.equals(friendId)){
                           Vector2 position = new Vector2();
                           position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                           position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                           String playerTexture = objects.getJSONObject(i).getString("texture");
                           FriendlyBall newBall = factory.createFriendlyPlayer(world, position, playerTexture);
                           levels.get(currentLevel).friendlyBalls.put(objects.getJSONObject(i).getString("id"), newBall);
                           Gdx.app.log("Getting Players", "Size: " + levels.get(currentLevel).friendlyBalls.size());
                       }

                    }
               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }

           /**
            * Cuando recibimos el mensaje 'finishLevel' es porque un amigo se ha pasado el nivel y se va.
            * Preparamos su borrado.
            **/
       }).on("finishLevel", new Emitter.Listener(){
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   //Se ha ido un jugador de nuestro nivel
                   String playerId = data.getString("id");
                   ballToDetach = playerId;
                   isFriendlyBallDisconnect = true;
                   Gdx.app.log("SocketIO", "Player " + playerId + " finish level " + currentLevel);
               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }

           /**
            * Cuando recibimos el mensaje 'activatorWallCollision' es porque un amigo se encuentra en un muro activador o sale
            * de un muero activador.
            * Habilitamos/Deshabilitamos el muro correspondiente.
            **/
       }).on("activatorWallCollision", new Emitter.Listener(){
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   //Otro jugador esta colisionando (o ha dejado) con una pared activadora de nuestro nivel
                   int activatorWallId = data.getInt("activatorWallId");
                   boolean isColliding = data.getBoolean("isColliding");
                   if(isColliding){
                       System.out.println("level: " + currentLevel + " wall: " + activatorWallId + " size: " + levels.get(currentLevel).wallList.size);
                       levels.get(currentLevel).wallList.get(activatorWallId).setVisible(false);
                       levels.get(currentLevel).wallList.get(activatorWallId).getFixture().setSensor(true);
                   }else{
                       levels.get(currentLevel).wallList.get(activatorWallId).setVisible(true);
                       levels.get(currentLevel).wallList.get(activatorWallId).getFixture().setSensor(false);
                   }
                   //Gdx.app.log("SocketIO", "Player Moved With Id: " + playerId);
               } catch (JSONException e) {
                   Gdx.app.log("SocketIO", "Error getting ID" + e);
               }
           }
       }).on("PlayersOnline", new Emitter.Listener() {
           @Override
           public void call(Object... args) {
               JSONObject data = (JSONObject) args[0];
               try {
                   playersOnline = data.getInt("online");
                   text = "Online: " + playersOnline;
                   Gdx.app.log("PlayersOnline", "Players online: " + playersOnline);
               } catch (JSONException e) {
                   Gdx.app.log("PlayersOnline", "Error getting players online" + e);
               }
           }
           /**
            * Cuando recibimos el mensaje 'playerMoved' es porque un jugador de nuestro nivel se ha movido.
            * Actualizamos su posicion.
            **/
       });

    }

    public void connectSocket(){
        try{
            socket = IO.socket("http://192.168.0.157:8080");
            socket.connect();
        }catch(Exception e){
            System.out.println(e);
        }
    }



}
