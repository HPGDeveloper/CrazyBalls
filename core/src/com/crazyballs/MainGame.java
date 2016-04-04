package com.crazyballs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.crazyballs.screens.*;

public class MainGame extends Game {

	/** This is the asset manager we use to centralize the assets. */
	private AssetManager manager;

	/**
	 * These are the screens that we use in this game. I invite you to use a better system than
	 * just public variables. For instance, you could create an ArrayList or maybe use some
	 * structure such as a map where you can associate a number or a string to a screen.
	 */
	public BaseScreen selectBallScreen, loadScreen;




	@Override
	public void create() {
		// Initialize the asset manager. We add every aset to the manager so that it can be loaded
		// inside the LoadingScreen screen. Remember to put the name of the asset in the first
		// argument, then the type of the asset in the second argument.
		manager = new AssetManager();
		manager.load("wall.png", Texture.class);
		manager.load("ball.png", Texture.class);
		manager.load("smile.png", Texture.class);
		manager.load("azul.png", Texture.class);
		manager.load("sorpresa.png", Texture.class);
		manager.load("bizco.png", Texture.class);
		manager.load("resetableWall.png", Texture.class);
		manager.load("activatorWall.png", Texture.class);
		manager.load("finishWall.png", Texture.class);



		// Enter the loading screen to load the assets.
		loadScreen = new LoadScreen(this);
		setScreen(loadScreen);



	}

	/**
	 * This method is invoked by LoadingScreen when all the assets are loaded. Use this method
	 * as a second-step loader. You can load the rest of the screens here and jump to the main
	 * screen now that everything is loaded.
	 */
	public void finishLoading() {

		selectBallScreen = new SelectBallScreen(this, this.manager);
		setScreen(selectBallScreen);

	}







	public AssetManager getManager() {
		return manager;
	}

}
