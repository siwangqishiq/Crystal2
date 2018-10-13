package com.xinlan.crystal.action;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.xinlan.crystal.role.AddDump;
import com.xinlan.crystal.role.CoreData;
import com.xinlan.crystal.role.GameOver;
import com.xinlan.crystal.screen.GameScreen;

/**
 * 
 * @author panyi
 * 
 */
public class TouchListener implements InputProcessor
{
    private GameScreen context;

    boolean isPressed = false;
    Vector3 touchPos = new Vector3();

    public TouchListener(GameScreen context)
    {
        this.context = context;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (context.addDump.status == AddDump.STATUS_WAITSHOOT)
        {
            this.isPressed = true;

            setTouchPoint(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        // System.out.println(screenX+","+screenY);
        if (context.addDump.status == AddDump.STATUS_WAITSHOOT && isPressed)
        {
            setTouchPoint(screenX, screenY);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if (context.game_state == GameScreen.STATE_OVER
                && context.mGameOver.cur_state == GameOver.STATE.SHOW)
        {
            context.mGameOver.cur_state = GameOver.STATE.LEAVE_OUT;
            return false;
        }

        if (context.addDump.status == AddDump.STATUS_WAITSHOOT && isPressed)
        {
            this.isPressed = false;
            touchPos.set(screenX, screenY, 0);
            context.cam.unproject(touchPos);
            Vector2 pos = context.addDump.pos;
            pos.x = touchPos.x;
            int addCol = -1;
            int LeftBound = CoreData.CUBE_WIDTH + CoreData.PAD;
            if (pos.x < LeftBound)
            {// 1
                pos.x = CoreData.PAD;
                addCol = 0;
            }
            else if (pos.x >= LeftBound
                    && pos.x < (LeftBound + CoreData.CUBE_WIDTH))
            {// 2
                pos.x = LeftBound;
                addCol = 1;
            }
            else if (pos.x >= LeftBound + CoreData.CUBE_WIDTH
                    && pos.x < LeftBound + (CoreData.CUBE_WIDTH << 1))
            {// 3
                pos.x = LeftBound + CoreData.CUBE_WIDTH;
                addCol = 2;
            }
            else if (pos.x >= LeftBound + (CoreData.CUBE_WIDTH << 1)
                    && pos.x < LeftBound + (CoreData.CUBE_WIDTH << 1)
                            + CoreData.CUBE_WIDTH)
            {// 4
                pos.x = LeftBound + (CoreData.CUBE_WIDTH << 1);
                addCol = 3;
            }
            else if (pos.x >= LeftBound + (CoreData.CUBE_WIDTH << 1)
                    + CoreData.CUBE_WIDTH
                    && pos.x < LeftBound + (CoreData.CUBE_WIDTH << 2))
            {// 5
                pos.x = LeftBound + (CoreData.CUBE_WIDTH << 1)
                        + CoreData.CUBE_WIDTH;
                addCol = 4;
            }
            else if (pos.x >= LeftBound + (CoreData.CUBE_WIDTH << 2))
            {// 6
                pos.x = LeftBound + (CoreData.CUBE_WIDTH << 2);
                addCol = 5;
            }

            context.addDump.curSprite.setPosition(pos.x, AddDump.ADD_POS_Y);
            context.addDump.curCol = addCol;

            context.addDump.status = AddDump.STATUS_SHOOTING;
            context.gameSound.playFireSound();
        }
        return false;
    }

    private void setTouchPoint(int screenX, int screenY)
    {
        touchPos.set(screenX, screenY, 0);
        context.cam.unproject(touchPos);

        Vector2 pos = context.addDump.pos;
        pos.x = touchPos.x - (CoreData.CUBE_WIDTH >> 1);

        if (pos.x < 0)
        {
            pos.x = 0;
        }
        else if (pos.x > GameScreen.SC_WIDTH - CoreData.CUBE_WIDTH)
        {
            pos.x = GameScreen.SC_WIDTH - CoreData.CUBE_WIDTH;
        }

        context.addDump.curSprite.setPosition(pos.x, AddDump.ADD_POS_Y);
    }

    @Override
    public boolean keyDown(int arg0)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char arg0)
    {
        return false;
    }

    @Override
    public boolean keyUp(int arg0)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int arg0, int arg1)
    {
        return false;
    }

    @Override
    public boolean scrolled(int arg0)
    {
        return false;
    }
}// end class
