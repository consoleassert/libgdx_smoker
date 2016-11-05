package cns.ass.smoker;

import cns.ass.smoker.screen.ScreenEnum;
import cns.ass.smoker.screen.ScreenManager;
import cns.ass.smoker.screen.ScreenOptions;
import com.badlogic.gdx.Game;

public class SmokerGame extends Game {


	private ScreenManager screenManager;

	@Override
	public void create () {
		Assets.load();
		screenManager = ScreenManager.getInstance();
		screenManager.initialize(this);
		ScreenOptions so = new ScreenOptions("sprites/menu_atlas.txt");
		showScreen(ScreenEnum.MAIN_MENU, so);
	}

	public void showScreen(ScreenEnum screen, ScreenOptions so) {
		screenManager.showScreen(screen, so);
	}

	@Override
	public void render() {
		super.render();
	}
}
