package com.xinlan.crystal.role;

import android.preference.Preference;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xinlan.crystal.screen.GameScreen;

/**
 * 分数展示 逻辑 控件
 * 
 * @author panyi
 * 
 */
public final class Score {
	public static final String PREFERENCE_TAG = "crystal_preference";
	public static final String HISTORY_MAX_SOCRE = "history_max_score";

	public static final int LFET = 460;
	public static final int TOP = 760;
	public static final int SCORE_VALUE = 7;
	public static final int CHAR_WIDTH = 25;
	public static final int dScore = 1;// 分数增长量

	public int value = 0;// 分数
	private int cur_value;// 当前展示分数
	private GameScreen context;
	private BitmapFont bitmapFont;
	private Preferences pref;

	protected int maxHistoryScore;// 最高分

	public Score(GameScreen context) {
		this.context = context;

		bitmapFont = new BitmapFont(Gdx.files.internal("font/score2.fnt"),
				Gdx.files.internal("font/score2.png"), false);
		bitmapFont.setColor(0.5f, 0.5f, 0.5f, 1); // 设置颜色
		//bitmapFont.setScale(1.0f); // 设置字体比例大小

		reset();
		//System.out.println("maxScore---->"+maxHistoryScore);
	}
	
	public void reset()
	{
	    value = 0;// 分数
	    cur_value=0;// 当前展示分数
	    maxHistoryScore = getHistoryMaxScore();
	}

	/**
	 * 获取历史最高分
	 * 
	 * @return
	 */
	public int getHistoryMaxScore() {
		if (pref == null) {
			pref = Gdx.app.getPreferences(PREFERENCE_TAG);
		}
		return pref.getInteger(HISTORY_MAX_SOCRE, 0);
	}

	/**
	 * 设置最高分 存贮SP中
	 * 
	 * @param value
	 */
	public void setHistoryMaxScore(int maxValue) {
		if (pref == null) {
			pref = Gdx.app.getPreferences(PREFERENCE_TAG);
		}
		if (maxValue > 0) {
			pref.putInteger(HISTORY_MAX_SOCRE, maxValue);
			pref.flush();//将数据冲刷进去(重要)
		}
	}

	/**
	 * 消去的团子数量
	 * 
	 * @param addScore
	 */
	public void addScore(int dismissNum) {
		int addScore = dismissNum * SCORE_VALUE;
		value += addScore;
	}

	public void draw(SpriteBatch batch, float delta) {
		int weight = (cur_value + "").length();
		bitmapFont.draw(batch, cur_value + "", LFET - weight * CHAR_WIDTH, TOP);

		if (cur_value < value) {
			cur_value += dScore;
		}
	}

	public void dispose() {
	    //System.out.println("value > maxHistoryScore--->"+value+"   "+ maxHistoryScore);
		if (value > maxHistoryScore)// 存贮最高分
		{
			setHistoryMaxScore(value);
		}
		bitmapFont.dispose();
	}
}// end class
