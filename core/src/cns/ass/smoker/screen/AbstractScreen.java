package cns.ass.smoker.screen;

import cns.ass.smoker.box2d.PhysicsHelper;
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

import java.util.*;

/**
 * Created by Ad on 25.09.2016.
 */
public abstract class AbstractScreen extends ScreenAdapter implements InputProcessor {
    protected FreeTypeFontGenerator generator;
    protected OrthographicCamera camera;
    protected Stage stage;
    protected SmokerGame smokerGame;
    protected HashMap<String, List<TextureRegion>> textures = new HashMap<String, List<TextureRegion>>();
    protected TextureAtlas textureAtlas;
    protected FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    protected World world;
    protected Box2DDebugRenderer debugRenderer;
    protected ScreenOptions screenOptions;

    protected void init(SmokerGame smokerGame, ScreenOptions so) {
        this.screenOptions = so;
        this.smokerGame = smokerGame;
        initStage();
        initCamera();
        initFonts();
        initWorld();
        initTextures();
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

    protected void initTextures() {
        String atlasFile = screenOptions.getLevelAtlasName();
        textureAtlas = new TextureAtlas(atlasFile);
        TextureRegion textureRegion;
        Iterator<TextureAtlas.AtlasRegion> iterator = textureAtlas.getRegions().iterator();
        while(iterator.hasNext()) {
            TextureAtlas.AtlasRegion next = iterator.next();
            textureRegion = new TextureRegion(next.getTexture(), next.getRegionX(), next.getRegionY(), next.getRegionWidth(), next.getRegionHeight());
            if(textures.containsKey(next.name)) {
                textures.get(next.name).add(textureRegion);
            } else {
                List<TextureRegion> textureRegionList = new ArrayList<TextureRegion>();
                textureRegionList.add(textureRegion);
                textures.put(next.name, textureRegionList);
            }
        }
    }

    protected void initUI() {
        throw new RuntimeException("Not implemented");
    }

    protected abstract void initWorldBounds();

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

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
