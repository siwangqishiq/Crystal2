package com.xinlan.crystal.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.xinlan.crystal.GameInstance;
import com.xinlan.crystal.Resource;
import com.xinlan.crystal.action.TouchListener;
import com.xinlan.crystal.role.AddDump;
import com.xinlan.crystal.role.Background;
import com.xinlan.crystal.role.CoreData;
import com.xinlan.crystal.role.Dump;
import com.xinlan.crystal.role.GameOver;
import com.xinlan.crystal.role.GameSound;
import com.xinlan.crystal.role.Score;

public final class GameScreen extends DefaultScreen
{
    public static final int SC_WIDTH = 480;
    public static final int SC_HEIGHT = 800;

    public static final int STATE_NORMAL = 13;
    public static final int STATE_OVER = STATE_NORMAL + 1;
    public int game_state = STATE_NORMAL;

    public OrthographicCamera cam;
    public SpriteCache cache = new SpriteCache();
    public SpriteBatch batch = new SpriteBatch();
    
    public Background mBackground;
    public Dump dump;
    public CoreData core;
    public AddDump addDump;
    public TouchListener touchListener;
    public GameSound gameSound;
    public Score score;// 分数
    public GameOver mGameOver;

    public GameScreen(Game game)
    {
        super(game);

        cache.getProjectionMatrix().setToOrtho2D(0, 0, SC_WIDTH, SC_HEIGHT);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, SC_WIDTH, SC_HEIGHT);

        cam = new OrthographicCamera(GameInstance.SCREEN_WIDTH,
                GameInstance.SCREEN_HEIGHT);
        cam.position.set(GameInstance.SCREEN_WIDTH / 2,
                GameInstance.SCREEN_HEIGHT / 2, 0);

    }

    @Override
    public void show()
    {
        Resource.getInstance().reInit();
        gameSound = new GameSound(this);
        mBackground = new Background(this);
        dump = new Dump(this);
        core = new CoreData(this);
        addDump = new AddDump(this);
        score = new Score(this);
        mGameOver = new GameOver(this);

        touchListener = new TouchListener(this);
        Gdx.input.setInputProcessor(touchListener);

        gameSound.canPlaySound =true;
        gameSound.canPlayMusic = true;
        
        gameSound.playBackgroundMusic();
    }

    @Override
    public void resize(int width, int height)
    {
        
    }

    @Override
    public void render(float delta)
    {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        cam.update();

        cache.setProjectionMatrix(cam.combined);
        cache.begin();
        mBackground.draw(cache);
        cache.end();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        // TODO
        System.out.println("gameState-->"+game_state+"    core--->"+core.status);
        switch (game_state)
        {
            case STATE_NORMAL://正常游戏状态
                core.draw(batch, delta);
                addDump.draw(batch, delta);
                score.draw(batch, delta);
                checkGameisOver();
                break;
            case STATE_OVER://游戏结束状态
                core.gameOverDraw(batch, delta);
                score.draw(batch, delta);
                mGameOver.draw(batch, delta);
                break;
        }// end switch
        batch.end();
    }
    
    /**
     * 重新开始游戏
     */
    public void restart()
    {
        game_state = STATE_NORMAL;
        core.restart();
        score.reset();
        addDump.reSet(); 
        gameSound.restartBackgroundMusic();
    }
    
    /**
     * 检查是否游戏结束
     */
    private void checkGameisOver()
    {
        if(core.isDead)
        {
            game_state = STATE_OVER;
        }
    }

    @Override
    public void dispose()
    {
        gameSound.dispose();
        core.dispose();
        score.dispose();
        batch.dispose();
        cache.dispose();
    }
}// end class
