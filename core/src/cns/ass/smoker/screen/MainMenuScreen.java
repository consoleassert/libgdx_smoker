package cns.ass.smoker.screen;

import cns.ass.smoker.Assets;
import cns.ass.smoker.SmokerGame;
import cns.ass.smoker.box2d.PhysicsHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Ad on 08.09.2016.
 */
public class MainMenuScreen extends AbstractScreen implements InputProcessor {
    private static final String TAG = MainMenuScreen.class.getName();
    public static final int PLAY = 0;
    public static final int EXIT = 1;
    Skin skin;
    int currentMenuState = PLAY;
    private TextButton playButton;
    private TextButton exitButton;
    private Body body;
    private Sprite smokerStatic;
    float angleSpeed = 1f;

    public void init(SmokerGame smokerGame, String atlasFile) {
        super.init(smokerGame, atlasFile);
        smokerStatic = new Sprite(textures.get("menu_player"), 0, 0, 75, 75);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(0, 15));
        body = world.createBody(bodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(smokerStatic.getWidth() * 0.5f, smokerStatic.getHeight() * 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        Fixture fixture = body.createFixture(fixtureDef);
        body.setUserData(smokerStatic);
        body.setFixedRotation(false);
        body.applyLinearImpulse(20 * 10, 20 * 10, 0, 0, true);

        body.applyLinearImpulse(100, 100, 0, 0, true);

    }

    @Override
    protected void initWorldBounds() {
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(0, (float) (-Gdx.graphics.getHeight() * 0.5) - 10), camera.viewportWidth, 10.0f);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(0, (float) (Gdx.graphics.getHeight() * 0.5) + 10), camera.viewportWidth, 10.0f);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2((float) ((Gdx.graphics.getWidth() * 0.5) + 10), 0), 10.0f, camera.viewportHeight);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(-(float) ((Gdx.graphics.getWidth() * 0.5) + 10), 0), 10.0f, camera.viewportHeight);
    }

    protected void initTextures(String atlasName) {
        super.initTextures(atlasName);
    }

    @Override
    public void initUI() {
        skin = new Skin();
        BitmapFont buttonFont = generator.generateFont(parameter);
        Pixmap pixmap = new Pixmap(100, 60, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("test", pixmap);

        skin.add("button_normal", textures.get("menu_button_bg"));
        skin.add("button_selected", textures.get("menu_button_pressed_bg"));
        skin.add("default", buttonFont);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("button_normal");
        textButtonStyle.down = skin.newDrawable("button_normal");
        textButtonStyle.checked = skin.newDrawable("button_selected");
        textButtonStyle.over = skin.newDrawable("button_normal");
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        Table table = new Table();
        table.setColor(Color.RED);
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(150);
        stage.addActor(table);

        playButton = new TextButton("Play", skin);
        exitButton = new TextButton("Exit", skin);
        table.add(playButton).size(100, 60);
        table.add(new Container<Actor>()).width(50);
        table.add(exitButton).size(100, 60);

//        stage.setDebugAll(true);
    }

    @Override
    public void update() {
        if (currentMenuState == PLAY) {
            exitButton.setChecked(false);
            playButton.setChecked(true);
        } else if (currentMenuState == EXIT) {
            playButton.setChecked(false);
            exitButton.setChecked(true);
        }

        body.setTransform(body.getPosition().x, body.getPosition().y, body.getAngle() + angleSpeed);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(textures.get("main_menu_bg"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextureRegion logoTitle = textures.get("logo_title");
        int logoTitleWidth = logoTitle.getRegionWidth() / 2;
        int logoTitleHeight = logoTitle.getRegionHeight() / 2;
        stage.getBatch().draw(new TextureRegion(logoTitle), Assets.getScreenCenterWidth() - (logoTitleWidth / 2), Assets.getScreenCenterHeight() + logoTitleHeight, 0, 0, logoTitleWidth, logoTitleHeight, 1f, 1f, 1f);
        Vector3 vector3 = new Vector3(body.getPosition(), 0f);
        Vector3 project = camera.project(vector3);
        smokerStatic.setCenter(project.x, project.y);
        smokerStatic.setRotation(body.getAngle());
        smokerStatic.draw(stage.getBatch());
        stage.getBatch().end();
        stage.draw();

//        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN || keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT) {
            currentMenuState = currentMenuState == PLAY ? EXIT : PLAY;
        } else if (keycode == Input.Keys.ENTER) {
            if (currentMenuState == PLAY) {
                smokerGame.showScreen(ScreenEnum.GAME_SCREEN, "sprites/level_1_atlas.txt");
            } else {
                Gdx.app.exit();
            }
        } else if (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
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

