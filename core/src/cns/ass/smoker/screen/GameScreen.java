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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
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
    public static final String TAG = GameScreen.class.getName();
    public static final float GAME_SCALE = 0.36f;
    public static final int TILE_MAP_CACHE_SIZE = 3000;
    public OrthoCachedTiledMapRenderer renderer;
    public Vector3 cameraPostion;
    public TiledMap map;
    public int mapWidth;
    public int mapHeight;
    public int mapWidthIntTiles;
    public int mapHeightInTiles;
    public List<BaseEnemy> enemies = new ArrayList<BaseEnemy>();
    public SpriteBatch spriteBatch;

    @Override
    protected void init(SmokerGame smokerGame, ScreenOptions so) {
        super.init(smokerGame, so);
        map = new TmxMapLoader().load(screenOptions.getLevelTilemapFile());
        mapWidth = (int) ((map.getProperties().get("width", Integer.class) * (map.getProperties().get("tilewidth", Integer.class))) * GAME_SCALE);
        mapHeight = (int) (map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) * GAME_SCALE);
        mapWidthIntTiles = map.getProperties().get("width", Integer.class);
        mapHeightInTiles = map.getProperties().get("height", Integer.class);
        renderer = new OrthoCachedTiledMapRenderer(map, GAME_SCALE, TILE_MAP_CACHE_SIZE);
        MapBodyBuilder.buildShapes(map, 50, world);
        MapObjects spawn = map.getLayers().get("spawn").getObjects();
        Vector2 position = new Vector2(0, 0);
        spriteBatch = new SpriteBatch();
        for(MapObject mapObject : spawn) {
            position.x = mapObject.getProperties().get("x", Float.class);
            position.y  = mapObject.getProperties().get("y", Float.class);
            position.x /= 50;
            position.y /= 50;
            BaseEnemy enemy = new BaseEnemy(world, position, textures.get("Enemy_1_atlas"), stage);
            enemies.add(enemy);
            break;
        }

        initContactListener();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    public void update() {
        camera.position.x = cameraPostion.x;
        camera.update();
        renderer.setView(camera);
        Iterator<BaseEnemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            BaseEnemy baseEnemy = enemyIterator.next();
            baseEnemy.update(camera);
        }
        updateInput();
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        Iterator<BaseEnemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            BaseEnemy baseEnemy = enemyIterator.next();
            baseEnemy.draw();
        }
        debugRenderer.render(world, camera.combined.scl(50 * GAME_SCALE));
    }

    @Override
    protected void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
        cameraPostion = camera.position;
    }

    @Override
    protected void initUI() {
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

    private void initContactListener() {
        world.setContactListener(new ContactListener() {
            private void checkEnemySensorForEndContact(Fixture fixture) {
                if(fixture.getUserData() instanceof String) {
                    String userData = (String) fixture.getUserData();
                    Body body = fixture.getBody();
                    BaseEnemy enemy = (BaseEnemy) body.getUserData();
                    if(userData.contains("ground")) {
                        enemy.grounded = false;
                    } else if(userData.contains("left_wall")) {
                    } else if(userData.contains("right_wall")) {
                    } else if(userData.contains("left_pit")) {
                        enemy.flip();
                    } else if(userData.contains("right_pit")) {
                        enemy.flip();
                    }
                }
            }

            private void checkEnemySensorForBeginContact(Fixture fixture) {
                if(fixture.getUserData() instanceof String) {
                    String userData = (String) fixture.getUserData();
                    Body body = fixture.getBody();
                    BaseEnemy enemy = (BaseEnemy) body.getUserData();
                    if(userData.contains("ground")) {
                        enemy.grounded = true;
                    } else if(userData.contains("left_wall")) {
                        enemy.flip();
                    } else if(userData.contains("right_wall")) {
                        enemy.flip();
                    } else if(userData.contains("left_pit")) {

                    } else if(userData.contains("right_pit")) {

                    }
                }
            }

            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getBody().getUserData() instanceof BaseEnemy) {
                    checkEnemySensorForBeginContact(contact.getFixtureA());
                } else if(contact.getFixtureB().getBody().getUserData() instanceof BaseEnemy) {
                    checkEnemySensorForBeginContact(contact.getFixtureB());
                }
            }

            @Override
            public void endContact(Contact contact) {
                if (contact.getFixtureA().getBody().getUserData() instanceof BaseEnemy) {
                    checkEnemySensorForEndContact(contact.getFixtureA());
                } else if(contact.getFixtureB().getBody().getUserData() instanceof BaseEnemy) {
                    checkEnemySensorForEndContact(contact.getFixtureB());
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
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
