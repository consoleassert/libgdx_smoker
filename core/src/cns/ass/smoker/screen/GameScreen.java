package cns.ass.smoker.screen;

import cns.ass.smoker.box2d.MapBodyBuilder;
import cns.ass.smoker.SmokerGame;
import cns.ass.smoker.box2d.PhysicsHelper;
import cns.ass.smoker.characters.BaseEnemy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ad on 10.09.2016.
 */
public class GameScreen extends AbstractScreen implements InputProcessor {
    private static final String TAG = GameScreen.class.getName();
    private OrthoCachedTiledMapRenderer renderer;
    private Vector3 cameraPostion;
    private TiledMap map;
    private int mapWidth;
    private int mapHeight;
    private int mapWidthIntTiles;
    private int mapHeightInTiles;
    private List<BaseEnemy> enemies = new ArrayList<BaseEnemy>();

    public GameScreen() {
        super();
        map = new TmxMapLoader().load("maps/level_1/Map 01.tmx");
        mapWidth = (int) ((map.getProperties().get("width", Integer.class) * (map.getProperties().get("tilewidth", Integer.class))) * 0.36);
        mapHeight = (int) (map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) * 0.36);
        mapWidthIntTiles = map.getProperties().get("width", Integer.class);
        mapHeightInTiles = map.getProperties().get("height", Integer.class);
    }

    @Override
    protected void init(SmokerGame smokerGame, String atlasFile) {
        super.init(smokerGame, atlasFile);
        renderer = new OrthoCachedTiledMapRenderer(map, 0.36f, 3000);
        MapBodyBuilder.buildShapes(map, 50, world);
        MapObjects spawn = map.getLayers().get("spawn").getObjects();
        Vector2 position = new Vector2(0, 0);
        for(MapObject mapObject : spawn) {
            position.x = mapObject.getProperties().get("x", Float.class);
            position.y  = mapObject.getProperties().get("y", Float.class);
            position.x /= 50;
            position.y /= 50;
            BaseEnemy enemy = new BaseEnemy(world, position);
            enemies.add(enemy);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    public void update() {
        camera.position.x = cameraPostion.x;
        camera.update();
        renderer.setView(camera);
        camera.combined.scl(50 * 0.36f);
        updateInput();
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        debugRenderer.render(world, camera.combined);
        stage.getBatch().begin();
        TextureRegion textureRegion = textures.get("Enemy_1_atlas");
        Vector3 vector3 = new Vector3(enemies.get(0).body.getPosition(), 0f);
        stage.getBatch().draw(textureRegion, vector3.x, vector3.y);
        stage.getBatch().end();
    }

    @Override
    protected void initTextures(String atlasFile) {
        super.initTextures(atlasFile);
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
        world = new World(new Vector2(0, -19.9f), true);
        debugRenderer = new Box2DDebugRenderer();
        initWorldBounds();
    }

    @Override
    protected void initWorldBounds() {
        //TODO Why walls size must be 0.5 for 1 tile? But map width and height same size
        float wallWidth = 0.5f;
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(-wallWidth, 0), wallWidth, mapHeightInTiles);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(mapWidthIntTiles + wallWidth, 0), wallWidth, mapHeightInTiles);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(0, mapHeightInTiles + wallWidth), mapWidthIntTiles, wallWidth);
        PhysicsHelper.getInstance().setWorldWall(world, new Vector2(0, 0 - wallWidth), mapWidthIntTiles, wallWidth);
    }

    private void updateInput() {
        Vector3 position = cameraPostion;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
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
