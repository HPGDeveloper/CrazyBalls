package com.crazyballs;

import com.badlogic.gdx.Gdx;

/**
 * Created by Hector on 13/02/2016.
 */
public class Constants {


    /**
     * Ancho de la pantalla
     */
    public static final float SCREEN_WIDTH = Gdx.graphics.getWidth();

    /**
     * Alto de la pantalla
     */
    public static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();


    /**
     * Proporción 16:9
     */
    public static final float SCREEN_HEIGHT_PROPORCION = 9f;
    public static final float SCREEN_WIDTH_PROPORCION = 16f;

    /**
     * Pixels en un metro.
     */
    public static final float PIXELS_IN_METER = SCREEN_HEIGHT/SCREEN_HEIGHT_PROPORCION;

}
