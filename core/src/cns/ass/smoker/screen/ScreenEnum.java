package cns.ass.smoker.screen;

import cns.ass.smoker.GameScreen;
import cns.ass.smoker.MainMenuScreen;

/**
 * Created by Ad on 25.09.2016.
 */
public enum ScreenEnum {
    MAIN_MENU {
        public AbstractScreen getScreen(Object... params) {
            return new MainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}
