package com.xinlan.crystal.role.particle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.xinlan.crystal.role.CoreData;
import com.xinlan.crystal.screen.GameScreen;

/**
 * 方块消失 粒子效果
 * 
 * @author panyi
 * 
 */
public class BombParticle {
	private GameScreen context;

	private ParticleEffect blueParticle;// 蓝粒子
	private ParticleEffect pinkParticle;
	private ParticleEffect redParticle;
	private ParticleEffect yellowParticle;
	private ParticleEffectPool bluePool;// 蓝色粒子池 产生蓝色粒子
	private ParticleEffectPool pinkPool;//粉色池
	private ParticleEffectPool redPool;//红池
	private ParticleEffectPool yellowPool;

	private Array<ParticleEffect> particleList = new Array<ParticleEffect>();// 存贮所有粒子的容器

	public BombParticle(GameScreen context) {
		this.context = context;
		load();
	}

	/**
	 * 载入资源
	 */
	private void load() {
		blueParticle = new ParticleEffect();
		blueParticle.load(Gdx.files.internal("particle/blue"),
				Gdx.files.internal("particle"));
		bluePool = new ParticleEffectPool(blueParticle, 10, 15);
		
		pinkParticle = new ParticleEffect();
		pinkParticle.load(Gdx.files.internal("particle/pink"),
				Gdx.files.internal("particle"));
		pinkPool = new ParticleEffectPool(pinkParticle, 10, 15);
		
		redParticle = new ParticleEffect();
		redParticle.load(Gdx.files.internal("particle/red"),
				Gdx.files.internal("particle"));
		redPool = new ParticleEffectPool(redParticle, 10, 15);
		
		yellowParticle = new ParticleEffect();
		yellowParticle.load(Gdx.files.internal("particle/orange"),
				Gdx.files.internal("particle"));
		yellowPool = new ParticleEffectPool(yellowParticle, 10, 15);
	}

	/**
	 * 绘制 更新逻辑
	 * 
	 * @param batch
	 * @param delta
	 */
	public void draw(SpriteBatch batch, float delta) {
		/**
		 * 绘制粒子
		 */
		for (int i = 0; i < particleList.size; i++) {
			particleList.get(i).draw(batch, delta);
		}
		/**
		 * 将死亡的粒子移除
		 */
		ParticleEffect temparticle;
		for (int i = 0; i < particleList.size; i++) {
			temparticle = particleList.get(i);
			if (temparticle.isComplete()) {
				particleList.removeIndex(i);
			}
		} // end for i
	}

	/**
	 * 加入待显示的粒子
	 * 
	 * @param type
	 * @param posX
	 * @param posY
	 */
	public void addParticle(int type, int row, int col) {
		float posX = CoreData.PAD + CoreData.CUBE_WIDTH * col
				+ (CoreData.CUBE_WIDTH >> 1);
		float posY = GameScreen.SC_HEIGHT
				- (CoreData.CUBE_BORN_Y + CoreData.CUBE_HEIGHT * row - (CoreData.CUBE_HEIGHT >> 1));
		ParticleEffect addParticle = selectParticleFromType(type);
		addParticle.setPosition(posX, posY);
		particleList.add(addParticle);
	}
	
	/**
	 * 根据类型 返回相应颜色的粒子效果
	 * @param type
	 * @return
	 */
	private ParticleEffect selectParticleFromType(int type)
	{
		ParticleEffect retParticle = null;
		switch(type)
		{
		case CoreData.BLUE:
			retParticle = bluePool.obtain();
			break;
		case CoreData.PINK:
			retParticle = pinkPool.obtain();
			break;
		case CoreData.RED:
			retParticle = redPool.obtain();
			break;
		case CoreData.YELLOW:
			retParticle = yellowPool.obtain();
			break;
		default:
			retParticle = bluePool.obtain();
		}
		return retParticle;
	}

	public void dispose() {
		bluePool.clear();
		pinkPool.clear();
		redPool.clear();
		yellowPool.clear();
		particleList.clear();
	}
}// end class
