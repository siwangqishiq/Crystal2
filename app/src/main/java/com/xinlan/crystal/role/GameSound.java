package com.xinlan.crystal.role;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.xinlan.crystal.screen.GameScreen;

public class GameSound
{
    private Music bgMusic;

    private Sound generateSound;
    private Sound fireSound;
    private Sound killSound;
    private Sound bombSound;

    public static boolean canPlaySound = true;
    public static boolean canPlayMusic = true;

    public GameSound(GameScreen screen)
    {
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/main_bg.mp3"));
        bgMusic.setLooping(true);
        
        generateSound = Gdx.audio.newSound(Gdx.files
                .internal("sound/generate.mp3"));
        fireSound = Gdx.audio.newSound(Gdx.files.internal("sound/fire.ogg"));
        killSound = Gdx.audio.newSound(Gdx.files.internal("sound/kill.wav"));
        bombSound = Gdx.audio.newSound(Gdx.files.internal("sound/bomb.ogg"));
    }
    
    public void restartBackgroundMusic()
    {
        if (canPlayMusic)
        {
            playBackgroundMusic();
        }
    }
    
    public void stopMusic()
    {
        if(canPlayMusic && bgMusic.isPlaying())
        {
            bgMusic.stop();
        }
    }

    public void playBackgroundMusic()
    {
        playMusic(bgMusic);
    }

    public void playBombSound()
    {
        playSound(bombSound);
    }

    public void playKillSound()
    {
        playSound(killSound);
    }

    public void playFireSound()
    {
        playSound(fireSound);
    }

    public void playGenerateSound()
    {
        playSound(generateSound);
    }

    private void playMusic(Music music)
    {
        if (canPlayMusic)
        {
            music.play();
        }
    }

    private void playSound(Sound sound)
    {
        if (canPlaySound)
        {
            sound.play();
        }
    }

    public void dispose()
    {
        bgMusic.dispose();
        generateSound.dispose();
        fireSound.dispose();
        killSound.dispose();
        bombSound.dispose();
    }
}// end class
