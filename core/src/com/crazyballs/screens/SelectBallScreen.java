package com.crazyballs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.crazyballs.CharacterButton;
import com.crazyballs.Constants;
import com.crazyballs.MainGame;


/**
 * Created by Hector on 04/03/2016.
 */
public class SelectBallScreen extends BaseScreen implements Screen, InputProcessor{

    public OrthographicCamera camera;
    public SpriteBatch batch;
    BitmapFont font;
    String text;
    GlyphLayout lay;
    private Array<CharacterButton> characterButtons;

    public GameScreen gameScreen;

    String nameTexture;

    public SelectBallScreen(final MainGame game, AssetManager manager) {
        super(game);

        camera=new OrthographicCamera(Constants.SCREEN_WIDTH,Constants.SCREEN_HEIGHT);
        batch=new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        text = "Selecciona un jugador";
        lay = new GlyphLayout(font, text);
        characterButtons = new Array<CharacterButton>();
        CharacterButton smileCharacter = new CharacterButton(manager, 2*Constants.PIXELS_IN_METER, Constants.SCREEN_HEIGHT/2, "smile.png");
        CharacterButton bizcoCharacter = new CharacterButton(manager, 4*Constants.PIXELS_IN_METER, Constants.SCREEN_HEIGHT/2, "bizco.png");
        CharacterButton azulCharacter = new CharacterButton(manager, 6*Constants.PIXELS_IN_METER, Constants.SCREEN_HEIGHT/2, "azul.png");
        CharacterButton sorpresaCharacter = new CharacterButton(manager, 8*Constants.PIXELS_IN_METER, Constants.SCREEN_HEIGHT/2, "sorpresa.png");
        characterButtons.add(smileCharacter);
        characterButtons.add(bizcoCharacter);
        characterButtons.add(azulCharacter);
        characterButtons.add(sorpresaCharacter);
    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println(screenX + ", " + screenY);
        float pointerX = InputTransform.getCursorToModelX((int)Constants.SCREEN_WIDTH, screenX);
        float pointerY = InputTransform.getCursorToModelY((int)Constants.SCREEN_HEIGHT, screenY);
        for(int i = 0; i < characterButtons.size; i++)
        {
            if(characterButtons.get(i).checkIsClicked(pointerX, pointerY)){
                gameScreen = new GameScreen(game);
                nameTexture = characterButtons.get(i).getNameTexture();
                gameScreen.setNameTexture(nameTexture);
                game.setScreen(gameScreen);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        float pointerX = InputTransform.getCursorToModelX((int)Constants.SCREEN_WIDTH, screenX);
        float pointerY = InputTransform.getCursorToModelY((int)Constants.SCREEN_HEIGHT, screenY);
        for(int i = 0; i < characterButtons.size; i++)
        {
            characterButtons.get(i).checkIsClicked(pointerX, pointerY);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public static class InputTransform
    {

        public static float getCursorToModelX(int screenX, int cursorX)
        {
            return (((float)cursorX) * Constants.SCREEN_WIDTH) / ((float)screenX);
        }

        public static float getCursorToModelY(int screenY, int cursorY)
        {
            return ((float)(screenY - cursorY)) * Constants.SCREEN_HEIGHT / ((float)screenY) ;
        }
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.11f, 0.56f, 1.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, text, Constants.SCREEN_WIDTH/2 - (lay.width/2), Constants.SCREEN_HEIGHT*0.8f);
        for(int i = 0; i < characterButtons.size; i++)
        {
            characterButtons.get(i).update(batch);
        }
        batch.end();
    }

}
