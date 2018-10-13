package com.xinlan.crystal.role;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.xinlan.crystal.Resource;
import com.xinlan.crystal.screen.GameScreen;

public final class GameOver
{
    private GameScreen mContext;

    public static enum STATE
    {
        COME_IN, SHOW, LEAVE_OUT
    };

    public STATE cur_state;

    private Sprite gameover_sprite;
    private Vector2 pos = new Vector2();
    private int distionation_x, distionation_y;
    private final float dx = 15;// 速度

    public GameOver(GameScreen context)
    {
        this.mContext = context;
        gameover_sprite = Resource.getInstance().gameOverSprite;
        reInit(false);
    }

    public void draw(SpriteBatch batch, float delta)
    {
        switch (cur_state)
        {
            case COME_IN:// 移入
                pos.add(dx, 0);
                if (pos.x >= distionation_x)
                {
                    pos.set(distionation_x, distionation_y);
                    cur_state = STATE.SHOW;// 状态跳转
                }// end if
                gameover_sprite.setPosition(pos.x, pos.y);
                gameover_sprite.draw(batch);
                break;
            case SHOW:// 正常显示
                gameover_sprite.setPosition(pos.x, pos.y);
                gameover_sprite.draw(batch);
                break;
            case LEAVE_OUT:
                pos.add(dx,0);
                if(pos.x>=GameScreen.SC_WIDTH)
                {
                    //TODO 重新开始
                    mContext.restart();
                }
                gameover_sprite.setPosition(pos.x, pos.y);
                gameover_sprite.draw(batch);
                break;
            default:
                break;
        }// end switch
    }

    public void reInit(boolean isStopPlayMusic)
    {
        cur_state = STATE.COME_IN;
        distionation_x = (GameScreen.SC_WIDTH >> 1)
                - (int) (gameover_sprite.getWidth() / 2);
        distionation_y = GameScreen.SC_HEIGHT - 200
                - (int) gameover_sprite.getHeight();
        pos.x = -gameover_sprite.getWidth();
        pos.y = distionation_y;
        gameover_sprite.setPosition(pos.x, pos.y);
        if(isStopPlayMusic)
        {
            mContext.gameSound.stopMusic();
        }
    }

}// end class
