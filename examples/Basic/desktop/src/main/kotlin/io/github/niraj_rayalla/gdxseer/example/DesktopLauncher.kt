package io.github.niraj_rayalla.gdxseer.example

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import io.github.niraj_rayalla.gdxseer.GDXseerGLManager
import io.github.niraj_rayalla.gdxseer.effekseer_gl.OpenGLDeviceType

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 2)
        config.setBackBufferConfig(8, 8, 8, 8, 24, 0, 2)
        config.setWindowedMode(1024, 1024)
        Lwjgl3Application(
            BasicExampleApplicationAdapter {
                GDXseerGLManager(1000, true, null, OpenGLDeviceType.OpenGL3)
            },
            config
        )
    }
}