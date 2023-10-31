package io.github.niraj_rayalla.gdxseer.example;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import io.github.niraj_rayalla.gdxseer.GDXseerGLManager;
import io.github.niraj_rayalla.gdxseer.effekseer_gl.OpenGLDeviceType;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the config object
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        // Create the adapter
        BasicExampleApplicationAdapter adapter = new BasicExampleApplicationAdapter(
                camera -> new GDXseerGLManager(1000, true, camera, null, OpenGLDeviceType.OpenGLES3)
        );

        // Initialize 3d app
        initialize(adapter, config);
    }
}
