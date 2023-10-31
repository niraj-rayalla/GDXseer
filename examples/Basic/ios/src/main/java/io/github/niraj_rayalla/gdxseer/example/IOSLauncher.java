package io.github.niraj_rayalla.gdxseer.example;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.glkit.GLKViewDrawableDepthFormat;
import org.robovm.apple.uikit.UIApplication;

import io.github.niraj_rayalla.gdxseer.GDXseerGLManager;
import io.github.niraj_rayalla.gdxseer.effekseer_gl.OpenGLDeviceType;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        //noinspection deprecation
        config.useGL30 = true;
        config.depthFormat = GLKViewDrawableDepthFormat._24;

        // Create the adapter
        BasicExampleApplicationAdapter adapter = new BasicExampleApplicationAdapter(
                camera -> new GDXseerGLManager(1000, true, camera, null, OpenGLDeviceType.OpenGLES2)
        );

        return new IOSApplication(adapter, config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}
