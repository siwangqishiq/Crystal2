package com.xinlan.crystal.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class DefaultScreen implements Screen{
	public Game game;
	
	public DefaultScreen (Game game) {
		this.game = game;
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}//end class
