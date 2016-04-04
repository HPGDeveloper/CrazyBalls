package com.crazyballs.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Hector on 13/02/2016.
 */
public class EntityFactory {

    private AssetManager manager;

    /**
     * Crea una nueva entidad usando el asset manager.
     * @param manager   El assetManager utilizado para generar cosas.
     */
    public EntityFactory(AssetManager manager) {
        this.manager = manager;
    }

    /**
     * Crea un jugador usando una textura.
     * @param world     mundo en el que el jugador se encuentra.
     * @param position  posicion inicial del jugador en el mundo (meters,meters).
     * @param nameTexture nombre del archivo .png de la textura del jugador
     * @return          el jugador.
     */
    public Ball createPlayer(World world, Vector2 position, String nameTexture) {
        Texture playerTexture = manager.get(nameTexture);
        return new Ball(world, playerTexture, position);
    }

    /**
     * Crea un jugador usando una textura.
     * @return          el jugador.
     */
    public FriendlyBall createFriendlyPlayer(World world, Vector2 startPosition, String nameTexture) {
        Texture playerTexture = manager.get(nameTexture);
        return new FriendlyBall(world, playerTexture, startPosition);
    }

    /**
     * Crea un muro utilizando una textura.
     * @param world     mundo en el que el muro se encuentra.
     * @param position  esquina inferior izquierda del muro en el mundo (meters,meters).
     * @param width     ancho del muro. (meters).
     * @param height    alto del muro. (meters).
     * @return          un muro.
     */
    public Wall createWall(World world, Vector2 position, float width, float height) {
        Texture wallTexture = manager.get("wall.png");
        return new Wall(world, wallTexture, position, width, height, false, false, null, false, 0);
    }


    /**
     * Crea un muro que se encarga de resetear al jugador utilizando una textura.
     * @param world     mundo en el que el muro se encuentra.
     * @param position  esquina inferior izquierda del muro en el mundo (meters,meters).
     * @param width     ancho del muro. (meters).
     * @param height    alto del muro. (meters).
     * @return          un muro.
     */
    public Wall createResetableWall(World world, Vector2 position, float width, float height) {
        Texture wallTexture = manager.get("resetableWall.png");
        return new Wall(world, wallTexture, position, width, height, true, false, null, false, 0);
    }

    /**
     * Crea un muro que se encarga de resetear al jugador utilizando una textura.
     * @param world     mundo en el que el muro se encuentra.
     * @param position  esquina inferior izquierda del muro en el mundo (meters,meters).
     * @param width     ancho del muro. (meters).
     * @param height    alto del muro. (meters).
     * @return          un muro.
     */
    public Wall createFalseFinishWall(World world, Vector2 position, float width, float height) {
        Texture wallTexture = manager.get("finishWall.png");
        return new Wall(world, wallTexture, position, width, height, true, false, null, false, 0);
    }

    /**
     * Crea un muro que se encarga de ocultar otros muros utilizando una textura.
     * @param world     mundo en el que el muro se encuentra.
     * @param position  esquina inferior izquierda del muro en el mundo (meters,meters).
     * @param width     ancho del muro. (meters).
     * @param height    alto del muro. (meters).
     * @param activatorUserData    userData del muro para las colisiones.
     * @return          un muro.
     */
    public Wall createActivatorWall(World world, Vector2 position, float width, float height, String activatorUserData, int wallToQuit) {
        Texture wallTexture = manager.get("activatorWall.png");
        return new Wall(world, wallTexture, position, width, height, false, true, activatorUserData, false, wallToQuit);
    }

    /**
     * Crea un muro que se encarga de indicar que se ha pasado el nivel.
     * @param world     mundo en el que el muro se encuentra.
     * @param position  esquina inferior izquierda del muro en el mundo (meters,meters).
     * @param width     ancho del muro. (meters).
     * @param height    alto del muro. (meters).
     * @return          un muro.
     */
    public Wall createFinishWall(World world, Vector2 position, float width, float height) {
        Texture wallTexture = manager.get("finishWall.png");
        return new Wall(world, wallTexture, position, width, height, false, false, null, true, 0);
    }

}
