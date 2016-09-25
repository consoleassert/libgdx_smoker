package cns.ass.smoker.box2d;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ad on 24.09.2016.
 */
public class PhysicsHelper {
    private static PhysicsHelper ourInstance = new PhysicsHelper();

    public static PhysicsHelper getInstance() {
        return ourInstance;
    }

    private PhysicsHelper() {
    }

    public void setWorldWall(World world, Vector2 coord, float hx, float hv) {
        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        // Set its world position
        groundBodyDef.position.set(coord);
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);
        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(hx, hv);
        // Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
    }
}
