package com.xinlan.crystal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.xinlan.crystal.screen.GameScreen;

public class Crystal extends Game {
	FPSLogger fps;
	public void create() {
		setScreen(new GameScreen(this));
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
		fps.log();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
	}
}//end class
