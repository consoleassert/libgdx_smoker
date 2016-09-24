package cns.ass.smoker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Ad on 08.09.2016.
 */
public class Assets {

    public static Texture playerHale;

    public static void loadTextures() {
        playerHale = loadTexture("sprites/Player_inhale_exhale.png");
    }

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static Sprite smokerStatic() {
        return new Sprite(loadTexture("sprites/Player_inhale_exhale.png"), 0, 0, 75, 75);
    }

    public static Texture getMenuBackground() {
        Texture background = loadTexture("sprites/main_menu_bg.png");
//        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        return background;
    }

    public static Texture getMenuButtonNormalTexture() {
        Texture texture = loadTexture("sprites/menu_button_bg.png");
        return texture;
    }

    public static Texture getMenuButtonSelectedTexture() {
        Texture texture = loadTexture("sprites/menu_button_pressed_bg.png");
        return texture;
    }

    public static TextureRegion getLogo() {
        return new TextureRegion(playerHale, 0, 0, 75, 75);
    }

    public static Texture getLogoTitle() {
        return loadTexture("sprites/logo_title.png");
    }


    public static void load() {
        loadTextures();
    }


    public static int getScreenCenterWidth() {
        return Gdx.graphics.getWidth() / 2;
    }

    public static int getScreenCenterHeight() {
        return Gdx.graphics.getHeight() / 2;
    }

}
