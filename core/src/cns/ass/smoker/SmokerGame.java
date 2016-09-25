package cns.ass.smoker;

import cns.ass.smoker.screen.ScreenEnum;
import cns.ass.smoker.screen.ScreenManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SmokerGame extends Game {


	private ScreenManager instance;

	@Override
	public void create () {
		Assets.load();
		instance = ScreenManager.getInstance();
		instance.initialize(this);
		showScreen(ScreenEnum.MAIN_MENU, "sprites/menu_atlas.txt");
	}

	public void showScreen(ScreenEnum screen, String atlas) {
		instance.showScreen(screen, atlas);
	}

	@Override
	public void render() {
		super.render();
	}
}
