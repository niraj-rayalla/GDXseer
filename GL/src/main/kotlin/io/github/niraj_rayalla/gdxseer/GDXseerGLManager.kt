package io.github.niraj_rayalla.gdxseer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import io.github.niraj_rayalla.gdxseer.effekseer_gl.EffekseerGLManagerAdapter
import io.github.niraj_rayalla.gdxseer.effekseer_gl.OpenGLDeviceType

/**
 * [GDXseerManager] with GL backend renderer.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class GDXseerGLManager(
    maxSpriteCount: Int,
    autoFlip: Boolean = true,
    camera: Camera,
    /**
     * An optional [RenderContext] used to reduce graphics calls.
     */
    renderContext: RenderContext?,
    /**
     * The version of Open GL to use.
     */
    val openGLDeviceType: OpenGLDeviceType
): GDXseerManager<EffekseerGLManagerAdapter>(maxSpriteCount, autoFlip, camera, renderContext) {

    //region Properties

    private val effekseerManagerAdapterNameInstance: String by lazy {
        GDXseerGLManager::class.simpleName!!
    }

    //endregion

    //region Overrides

    override fun getEffekseerManagerAdapterName(): String {
        return this.effekseerManagerAdapterNameInstance
    }

    override fun createEffekseerManagerAdapter(): EffekseerGLManagerAdapter {
        return EffekseerGLManagerAdapter(this.maxSpriteCount, this.openGLDeviceType)
    }

    //endregion
}