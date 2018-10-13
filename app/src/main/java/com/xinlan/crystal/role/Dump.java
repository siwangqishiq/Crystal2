package com.xinlan.crystal.role;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.xinlan.crystal.Resource;
import com.xinlan.crystal.screen.GameScreen;

public class Dump extends Sprite
{
    private GameScreen mContext;
    private Vector2 pos = new Vector2();
    public Dump(GameScreen context)
    {
        this.mContext = context;
        this.set(Resource.getInstance().dumpPink);
        pos.x = 100;
        pos.y = 400;
        this.setPosition(pos.x, pos.y);
    }
    
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        //this.setColor(1f, 1f, 1f, 1f);
        //this.setOrigin(getWidth()/2, getHeight()/2);
//        this.rotate(1);
//        this.scale(0.01f);
        //this.setSize(this.getHeight(), this.getWidth()+1);
    }
}//end class
