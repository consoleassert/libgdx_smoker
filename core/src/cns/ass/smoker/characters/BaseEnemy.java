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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.sql.Statement;
import java.util.List;

/**
 * Created by Ad on 25.09.2016.
 */
public class BaseEnemy implements ApplicationListener {

    public final Body body;
    private Animation animation;
    private List<TextureRegion> textureRegionList;
    private SpriteBatch spriteBatch;
    private Stage stage;

    public BaseEnemy(World world, Vector2 position, List<TextureRegion> textureRegionList, SpriteBatch enemySpriteBatch, Stage stage) {
        this.textureRegionList = textureRegionList;
        this.spriteBatch = enemySpriteBatch;
        this.stage = stage;
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.x = position.x;
        bodyDef.position.y = position.y;

        // Create our body in the world using our body definition
        body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(1f);


        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
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
    }

    public void draw() {
        Sprite sprite = new Sprite(textureRegionList.get(0));
        stage.getBatch().begin();
        sprite.setScale(0.36f);
        sprite.setPosition(body.getPosition().x * 50 * 0.36f - (50 * 0.36f) - (sprite.getWidth() * 0.5f * 0.36f), (body.getPosition().y * 50 * 0.36f) - (50 * 0.36f) - (sprite.getHeight() * 0.5f * 0.36f));
        sprite.draw(stage.getBatch());
        stage.getBatch().end();
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
