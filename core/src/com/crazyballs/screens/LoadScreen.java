package com.crazyballs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.crazyballs.Constants;
import com.crazyballs.MainGame;

/**
 * Created by Hector on 13/02/2016.
 */
public class LoadScreen extends com.crazyballs.screens.BaseScreen {

    /** Labels are also Actors. We will use Scene2D UI. */
    private Stage stage;

    /** This is the skin file (see GameOverScreen for more information on this). */
    BitmapFont font;

    /** This is the label that we use to display some text on the screen. */
    private Label loading;

    public LoadScreen(MainGame game) {
        super(game);

        // Set up the stage and the skin. See GameOverScreen for more comments on this.
        stage = new Stage(new FitViewport(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        font = new BitmapFont(true);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.WHITE);
        loading = new Label("Loading...", labelStyle);

        // Create some loading text using this skin file and position it on screen.

        loading.setPosition(320 - loading.getWidth() / 2, 180 - loading.getHeight() / 2);
        stage.addActor(loading);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // This is important. To load an asset from the asset manager you call update() method.
        // this method will return true if it has finished loading. Else it will return false.
        // You usually want to do the code that changes to the main menu screen if it has finished
        // loading, else you update the screen to not make the user angry and keep loading.
        if (game.getManager().update()) {
            // I'll notify the game that all the assets are loaded so that it can load the
            // remaining set of screens and enter the main menu. This avoids Exceptions because
            // screens cannot be loaded until all the assets are loaded.
            game.finishLoading();
        } else {
            // getProgress() returns the progress of the load in a range of [0,1]. We multiply
            // this progress per * 100 so that we can display it as a percentage.
            int progress = (int) (game.getManager().getProgress() * 100);
            loading.setText("Loading... " + progress + "%");
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();

    }

}
