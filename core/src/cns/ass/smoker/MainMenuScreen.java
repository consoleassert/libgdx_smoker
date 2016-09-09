package cns.ass.smoker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Ad on 08.09.2016.
 */
public class MainMenuScreen extends ScreenAdapter implements InputProcessor{
    public static final int PLAY = 0;
    public static final int EXIT = 1;
    SpriteBatch batcher = new SpriteBatch();
    Stage stage;
    SmokerGame smokerGame;
    OrthographicCamera guiCam;
    Skin skin;
    int currentMenuState = PLAY;
    private final TextButton playButton;
    private final TextButton exitButton;

    public MainMenuScreen (SmokerGame smokerGame) {
        this.smokerGame = smokerGame;

        guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiCam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);

        stage = new Stage();
        Gdx.input.setInputProcessor(this);

        skin = new Skin();


        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Store the default libgdx font under the name "default".
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setColor(Color.BLUE);
        stage.addActor(table);

//         Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        playButton = new TextButton("Play", skin);
        exitButton = new TextButton("Exit", skin);
        table.add(playButton).size(100, 60);
        table.row();
        table.row();
        table.add(exitButton).size(100, 60);
//        table.add(button);

        stage.setDebugAll(true);

        // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
//        table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

    }



    public void update () {
        if(currentMenuState == PLAY) {
            exitButton.setChecked(false);
            playButton.setChecked(true);
        } else if(currentMenuState == EXIT) {
            playButton.setChecked(false);
            exitButton.setChecked(true);
        }
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void draw () {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(Assets.getMenuBackground(), 0, 0, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextureRegion logo = Assets.getLogo();
        Texture logoTitle = Assets.getLogoTitle();
        int logoTitleWidth = logoTitle.getWidth() / 2;
        int logoTitleHeight = logoTitle.getHeight() / 2;
        stage.getBatch().draw(new TextureRegion(logoTitle), Assets.getScreenCenterWidth() - (logoTitleWidth /2), Assets.getScreenCenterHeight() + logoTitleHeight, 0 , 0, logoTitleWidth, logoTitleHeight, 1f, 1f, 1f);
        stage.getBatch().draw(logo, Assets.getScreenCenterWidth() + (logo.getRegionWidth() /2) + (logoTitleWidth /2), Assets.getScreenCenterHeight() + logo.getRegionHeight());
        stage.getBatch().end();

//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();

    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render (float delta) {
        update();
        draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
            currentMenuState = currentMenuState == PLAY ? EXIT : PLAY;
        } else if(keycode == Input.Keys.ENTER) {
            if(currentMenuState == PLAY) {
                smokerGame.setScreen(new GameScreen());
            } else {
                Gdx.app.exit();
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
}

