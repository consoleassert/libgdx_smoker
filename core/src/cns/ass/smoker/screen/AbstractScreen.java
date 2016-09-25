package cns.ass.smoker.screen;

import cns.ass.smoker.PhysicsHelper;
import cns.ass.smoker.SmokerGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Ad on 25.09.2016.
 */
public abstract class AbstractScreen extends ScreenAdapter implements InputProcessor {
    protected FreeTypeFontGenerator generator;
    protected OrthographicCamera camera;
    protected Stage stage;
    protected SmokerGame smokerGame;
    protected HashMap<String, TextureRegion> textures = new HashMap<String, TextureRegion>();
    protected TextureAtlas textureAtlas;
    protected FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    protected World world;
    protected Box2DDebugRenderer debugRenderer;

    protected void init(SmokerGame smokerGame, String atlasFile) {
        this.smokerGame = smokerGame;
        initStage();
        initCamera();
        initFonts();
        initWorld();
        initTextures(atlasFile);
        initUI();
        Gdx.input.setInputProcessor(this);
    }

    protected void initStage() {
        stage = new Stage();
    }

    protected void initCamera() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
    }

    protected void initFonts() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("BalooBhai-Regular.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
    }

    protected void initWorld() {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        initWorldBounds();
    }

    protected void initTextures(String atlasFile) {
        textureAtlas = new TextureAtlas(atlasFile);
        TextureRegion textureRegion;
        Iterator<TextureAtlas.AtlasRegion> iterator = textureAtlas.getRegions().iterator();
        while(iterator.hasNext()) {
            TextureAtlas.AtlasRegion next = iterator.next();
            textureRegion = new TextureRegion(next.getTexture(), next.getRegionX(), next.getRegionY(), next.getRegionWidth(), next.getRegionHeight());
            textures.put(next.name, textureRegion);
        }
    }

    protected void initUI() {
        throw new RuntimeException("Not implemented");
    }

    protected void initWorldBounds() {
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(0, (float) (-Gdx.graphics.getHeight() * 0.5) - 10), camera.viewportWidth, 10.0f);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(0, (float) (Gdx.graphics.getHeight() * 0.5) + 10), camera.viewportWidth, 10.0f);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2((float) ((Gdx.graphics.getWidth() * 0.5) + 10), 0), 10.0f, camera.viewportHeight);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(-(float) ((Gdx.graphics.getWidth() * 0.5) + 10), 0), 10.0f, camera.viewportHeight);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update();
        draw();
        Gdx.graphics.setTitle(String.format("fps: %d", Gdx.graphics.getFramesPerSecond()));
        world.step(1 / 60f, 6, 2);
    }

    protected abstract void draw();

    protected abstract void update();

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

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        textureAtlas.dispose();
        textures.clear();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
