package com.xinlan.crystal.role;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.xinlan.crystal.Resource;
import com.xinlan.crystal.screen.GameScreen;

public class AddDump extends Sprite
{
    public static final int ADD_POS_Y = 45;

    public static final int STATUS_WAITSHOOT = 1;
    public static final int STATUS_SHOOTING = 2;

    public int status = STATUS_WAITSHOOT;

    private GameScreen context;
    public Vector2 pos = new Vector2();

    private Sprite spriteBlue;
    private Sprite spriteRed;
    private Sprite spriteYellow;
    private Sprite spritePink;
    private Sprite spriteBomb;// 炸弹

    public Sprite curSprite;
    public int curCol = -1;
    public int nextRowValue = -1;
    public int add_type = -1;// add类型

    private float dy = 20;

    public AddDump(GameScreen context)
    {
        this.context = context;
        spriteBlue = new Sprite(Resource.getInstance().dumpBlue);
        spriteBlue.setSize(CoreData.CUBE_WIDTH, CoreData.CUBE_HEIGHT);
        spriteBlue.setPosition(-100, -100);

        spriteRed = new Sprite(Resource.getInstance().dumpRed);
        spriteRed.setSize(CoreData.CUBE_WIDTH, CoreData.CUBE_HEIGHT);
        spriteRed.setPosition(-100, -100);

        spriteYellow = new Sprite(Resource.getInstance().dumpYellow);
        spriteYellow.setSize(CoreData.CUBE_WIDTH, CoreData.CUBE_HEIGHT);
        spriteYellow.setPosition(-100, -100);

        spritePink = new Sprite(Resource.getInstance().dumpPink);
        spritePink.setSize(CoreData.CUBE_WIDTH, CoreData.CUBE_HEIGHT);
        spritePink.setPosition(-100, -100);

        spriteBomb = new Sprite(Resource.getInstance().bombRegion);
        spriteBomb.setSize(CoreData.CUBE_WIDTH, CoreData.CUBE_HEIGHT);
        spriteBomb.setPosition(-100, -100);

        reSet();
        // reSetCurSprite(CoreData.BOMB);
    }
    
    public void reSet()
    {
        status = STATUS_WAITSHOOT;
        reSetCurSprite(MathUtils.random(1, CoreData.TYPE_NUM));
    }

    public void reSetCurSprite(int type)
    {
        this.add_type = type;
        switch (type)
        {
            case CoreData.RED:
                curSprite = spriteRed;
                break;
            case CoreData.PINK:
                curSprite = spritePink;
                break;
            case CoreData.YELLOW:
                curSprite = spriteYellow;
                break;
            case CoreData.BLUE:
                curSprite = spriteBlue;
                break;
            case CoreData.BOMB:
                curSprite = spriteBomb;
                break;
        }// end switch
    }

    public void draw(SpriteBatch batch, float delta)
    {
        if (curSprite == null || context.game_state == GameScreen.STATE_OVER)
            return;

        switch (status)
        {
            case STATUS_WAITSHOOT:
                curSprite.draw(batch);
                break;
            case STATUS_SHOOTING:
                curSprite.draw(batch);
                int canReachY = context.core.canStayPosYFromCol(this.curCol);
                if (curSprite.getY() >= canReachY)
                {
                    curSprite.setPosition(-100, -100);
                    // 修改核心矩阵
                    if (nextRowValue >= 0 && nextRowValue < CoreData.rowNum)// 判断数值合法性
                    {
                        context.core.data[nextRowValue][curCol] = add_type;// 增加的新点
                                                                           // 赋值
                        context.core.updateMatrix(nextRowValue, curCol);
                        status = STATUS_WAITSHOOT;
                        curCol = -1;
                    }
                    reSetCurSprite(randomNextDump());
                }
                else
                {
                    if (curSprite.getY() + dy > canReachY)
                    {// 越界判断 防止出现跳帧现象
                        curSprite.setY(canReachY);
                    }
                    else
                    {
                        curSprite.translateY(dy);
                    }
                }
                break;
        }// end switch
    }

    public int randomNextDump()
    {
        if (MathUtils.random() < 0.03f)
        {
            return CoreData.BOMB;
        }
        return MathUtils.random(1, CoreData.TYPE_NUM);
    }
}// end class
