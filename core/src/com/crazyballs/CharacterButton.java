package com.crazyballs;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Hector on 06/03/2016.
 */
public class CharacterButton {

    private Sprite sprite;
    private String nameTexture;
    private boolean isClicked;
    private static final float SIZE = 1f;

    public CharacterButton(AssetManager manager, float x, float y, String nameTexture) {
        sprite = new Sprite((Texture)manager.get(nameTexture));
        this.nameTexture = nameTexture;
        isClicked = false;
        sprite.setPosition(x - SIZE/2, y - SIZE/2);
        sprite.setSize(SIZE * Constants.PIXELS_IN_METER, SIZE * Constants.PIXELS_IN_METER);
    }

    public void update (SpriteBatch batch) {
        sprite.draw(batch); // draw the button
    }

    public boolean checkIsClicked (float ix, float iy) {
        if (ix > sprite.getX() && ix < sprite.getX() + sprite.getWidth()) {
            if (iy > sprite.getY() && iy < sprite.getY() + sprite.getHeight()) {
                // the button was clicked, perform an action
                isClicked = true;
                return isClicked;
            }else{
                isClicked = false;
                return false;
            }
        }else{
            isClicked = false;
            return false;
        }
    }

    public String getNameTexture() {
        return nameTexture;
    }

    public void setNameTexture(String nameTexture) {
        this.nameTexture = nameTexture;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
}
