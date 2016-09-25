package cns.ass.smoker;

import cns.ass.smoker.screen.AbstractScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Ad on 10.09.2016.
 */
public class GameScreen extends AbstractScreen implements InputProcessor {
    private static final String TAG  = GameScreen.class.getName();
    private OrthoCachedTiledMapRenderer renderer;
    private Vector3 cameraPostion;
    private TiledMap map;
    private int mapWidth;
    private int mapHeight;

    @Override
    protected void init(SmokerGame smokerGame, String atlasFile) {
        super.init(smokerGame, atlasFile);
        map = new TmxMapLoader().load("maps/level_1/Map 01.tmx");
        renderer = new OrthoCachedTiledMapRenderer(map, 0.36f, 3000);
        mapWidth = (int) ((map.getProperties().get("width", Integer.class) * (map.getProperties().get("tilewidth", Integer.class))) * 0.36);
        mapHeight = (int) (map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) * 0.36);
        MapBodyBuilder.buildShapes(map, 50, world);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    public void update() {
        camera.position.x = cameraPostion.x;
        camera.update();
        renderer.setView(camera);
        updateInput();
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        Matrix4 scl = camera.combined.scl(50 * 0.36f);
        debugRenderer.render(world, camera.combined);
    }

    @Override
    protected void initTextures(String atlasFile) {
//        super.initTextures(atlasFile);
    }

    @Override
    protected void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
        cameraPostion = camera.position;
    }

    @Override
    protected void initFonts() {
        super.initFonts();
    }

    @Override
    protected void initStage() {
        super.initStage();
    }

    @Override
    protected void initUI() {
//        super.initUI();
    }

    @Override
    protected void initWorld() {
        super.initWorld();
    }

    @Override
    protected void initWorldBounds() {
//        super.initWorldBounds();
    }

    private void updateInput() {
        Vector3 position = cameraPostion;
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= 10;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += 10;
        }
        position.x = clamp(position.x, camera.viewportWidth * 0.5f, (float) (mapWidth - (camera.viewportWidth * 0.5)));
        cameraPostion = position;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
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

}
