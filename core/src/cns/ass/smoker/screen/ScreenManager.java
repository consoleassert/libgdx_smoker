package cns.ass.smoker.screen;

import cns.ass.smoker.SmokerGame;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Created by Ad on 25.09.2016.
 */
public class ScreenManager {

    // Singleton: unique instance
    private static ScreenManager instance;

    // Reference to game
    private SmokerGame game;

    // Singleton: private constructor
    private ScreenManager() {
        super();
    }

    // Singleton: retrieve instance
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(SmokerGame game) {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(ScreenEnum screenEnum, String atlasName) {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        AbstractScreen newScreen = screenEnum.getScreen();
        newScreen.init(game, atlasName);
        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
