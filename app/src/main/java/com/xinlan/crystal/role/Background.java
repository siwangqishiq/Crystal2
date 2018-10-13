package com.xinlan.crystal.role;

import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.xinlan.crystal.GameInstance;
import com.xinlan.crystal.Resource;
import com.xinlan.crystal.screen.GameScreen;

public class Background {
	private GameScreen context;
	private int cacheId;

	public Background(GameScreen context) {
		this.context = context;

		SpriteCache cache = context.cache;
		TextureRegion texture = Resource.getInstance().gameBgTexture;
		cache.beginCache();
		cache.add(texture, 0, 0, GameInstance.SCREEN_WIDTH,
				GameInstance.SCREEN_HEIGHT);
		cacheId = cache.endCache();
	}

	public void draw(SpriteCache spriteCache) {
		spriteCache.draw(cacheId);
	}
}// end class
