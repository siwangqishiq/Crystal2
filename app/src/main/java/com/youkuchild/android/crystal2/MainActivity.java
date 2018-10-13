package com.youkuchild.android.crystal2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.xinlan.crystal.Crystal;

public class MainActivity extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useWakelock = true;
        config.useAccelerometer = false;
        config.useCompass = false;
        //config.useGL20 = false;
        initialize(new Crystal(), config);
    }
}
