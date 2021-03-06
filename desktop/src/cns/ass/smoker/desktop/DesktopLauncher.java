package cns.ass.smoker.desktop;

import cns.ass.smoker.SmokerConfig;
import cns.ass.smoker.SmokerGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SmokerConfig.DESIRED_WIDTH;
		config.height = SmokerConfig.DESIRED_HEIGHT;
		config.title = "Smoker";
		new LwjglApplication(new SmokerGame(), config);
	}
}
