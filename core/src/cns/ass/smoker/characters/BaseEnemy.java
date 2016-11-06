package cns.ass.smoker.characters;

import cns.ass.smoker.screen.GameScreen;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by Ad on 25.09.2016.
 */
public class BaseEnemy implements ApplicationListener {

    public final Body body;
    private Animation animation;
    private List<TextureRegion> textureRegionList;
    private Stage stage;
    private Vector2 bodySize = new Vector2();
    private float scaleFromBox2dToScreen = 50 * 0.36f;
    float elapsedTime;
    private int movementDirection = new Random().nextBoolean() ? 1 : -1;
    private Vector2 movementSpeed = new Vector2(2.5f, 0);
    public boolean spawned = false;
    public boolean grounded = false;

    public BaseEnemy(World world, Vector2 position, List<TextureRegion> textureRegionList, Stage stage) {
        this.textureRegionList = textureRegionList;
        this.stage = stage;
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.x = position.x;
        bodyDef.position.y = position.y;
        bodySize.set((textureRegionList.get(0).getRegionWidth() * 0.36f) / 50, (textureRegionList.get(0).getRegionHeight() * 0.36f) / 50);
        bodySize.mulAdd(bodySize, 2);

        // Create our body in the world using our body definition
        body = world.createBody(bodyDef);

        PolygonShape polygon = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(0, 0);
        vertices[1] = new Vector2(0, bodySize.y);
        vertices[2] = new Vector2(bodySize.x, bodySize.y);
        vertices[3] = new Vector2(bodySize.x, 0);
        polygon.set(vertices);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(0.01f, 0.01f, new Vector2(bodySize.x * 0.5f, -bodySize.y * 0.3f), body.getAngle());
        FixtureDef sensorFixture = new FixtureDef();
        sensorFixture.shape = sensorShape;
        Fixture groundFixture = body.createFixture(sensorFixture);
        groundFixture.setSensor(true);
        groundFixture.setUserData(new String("ground"));

        sensorShape.setAsBox(0.01f, 0.01f, new Vector2(bodySize.x * -0.5f, bodySize.y * 0.5f), body.getAngle());
        FixtureDef leftWallFixtureDef = new FixtureDef();
        leftWallFixtureDef.shape = sensorShape;
        Fixture leftWallFixture = body.createFixture(leftWallFixtureDef);
        leftWallFixture.setSensor(true);
        leftWallFixture.setUserData(new String("left_wall"));

        sensorShape.setAsBox(0.01f, 0.01f, new Vector2(bodySize.x * 1.5f, bodySize.y * 0.5f), body.getAngle());
        FixtureDef rightWallFixtureDef = new FixtureDef();
        rightWallFixtureDef.shape = sensorShape;
        Fixture rightWallFixture = body.createFixture(rightWallFixtureDef);
        rightWallFixture.setSensor(true);
        rightWallFixture.setUserData(new String("right_wall"));

        sensorShape.setAsBox(0.01f, 0.01f, new Vector2(bodySize.x * -0.5f,  - bodySize.y * 0.6f), body.getAngle());
        FixtureDef leftPitFixtureDef = new FixtureDef();
        leftPitFixtureDef.shape = sensorShape;
        Fixture leftPitFixture = body.createFixture(sensorFixture);
        leftPitFixture.setSensor(true);
        leftPitFixture.setUserData(new String("left_pit"));

        sensorShape.setAsBox(0.01f, 0.01f, new Vector2(bodySize.x * 1.5f,  - bodySize.y * 0.6f), body.getAngle());
        FixtureDef rightPitFixtureDef = new FixtureDef();
        rightPitFixtureDef.shape = sensorShape;
        Fixture rightPitFixture = body.createFixture(rightPitFixtureDef);
        rightPitFixture.setSensor(true);
        rightPitFixture.setUserData(new String("right_pit"));

        body.setUserData(BaseEnemy.this);

        ListIterator<TextureRegion> textureRegionListIterator = textureRegionList.listIterator();
        List<TextureRegion> movementTextureRegionList = new ArrayList<TextureRegion>();
        movementTextureRegionList.add(textureRegionList.get(textureRegionList.size() - 1));
        movementTextureRegionList.add(textureRegionList.get(textureRegionList.size() - 4));
        movementTextureRegionList.add(textureRegionList.get(textureRegionList.size() - 2));
        TextureRegion[] textureRegionsArray = new TextureRegion[movementTextureRegionList.size()];
        movementTextureRegionList.toArray(textureRegionsArray);
        animation = new Animation(1f/4f, textureRegionsArray);
    }

    public void flip() {
        movementDirection *= -1;
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
    }

    public void update(OrthographicCamera camera) {
        stage.getBatch().setProjectionMatrix(camera.combined);
        if(grounded && !body.isAwake()) {
            spawned = true;
        }
        if (spawned) {
            movementSpeed.x = Math.abs(movementSpeed.x) * movementDirection;
            body.setLinearVelocity(movementSpeed);
            Gdx.app.log("BaseEnemy", "ms: " + movementSpeed);
        }
//        Gdx.app.log("BaseEnemy", String.format("grounded: %b, awake: %b, spawned: %b", grounded, body.isAwake(), spawned));
    }

    public void draw() {
        elapsedTime +=Gdx.graphics.getDeltaTime();
        TextureRegion frame = animation.getKeyFrame(elapsedTime, true);
        Vector2 position = new Vector2();
        position.x = (((body.getPosition().x /*- (bodySize.x * 0.5f )*/) * scaleFromBox2dToScreen)/* - (frame.getRegionWidth() * 0.5f * 0.36f)*/);
        position.y = (((body.getPosition().y /*- (bodySize.y * 0.5f )*/) * scaleFromBox2dToScreen)/* - (frame.getRegionHeight() * 0.5f * 0.36f)*/);
        stage.getBatch().begin();
        if(movementDirection > 0 && !frame.isFlipX())
            frame.flip(true, false);
        else if(movementDirection < 0 && frame.isFlipX())
            frame.flip(false, false);

        stage.getBatch().draw(frame, position.x, position.y, frame.getRegionWidth() * 0.36f, frame.getRegionHeight() * 0.36f);
        stage.getBatch().end();

//        Sprite sprite = new Sprite(textureRegionList.get(0));
//        stage.getBatch().begin();
//        sprite.setScale(0.36f);
//        Vector2 position = new Vector2();
//        position.x = (((body.getPosition().x - (bodySize.x * 0.5f )) * scaleFromBox2dToScreen) - (sprite.getWidth() * 0.5f * 0.36f));
//        position.y = (((body.getPosition().y - (bodySize.y * 0.5f )) * scaleFromBox2dToScreen) - (sprite.getHeight() * 0.5f * 0.36f));
//        sprite.setPosition(position.x, position.y);
//
//        sprite.draw(stage.getBatch());
//        stage.getBatch().end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
