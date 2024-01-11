package io.github.niraj_rayalla.gdxseer

import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import io.github.niraj_rayalla.gdxseer.effekseer_metal.EffekseerMetalManagerAdapter

/**
 * [GDXseerManager] with Metal backend renderer.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class GDXseerMetalManager(
    maxSpriteCount: Int,
    autoFlip: Boolean = true,
    /**
     * An optional [RenderContext] used to reduce graphics calls.
     */
    renderContext: RenderContext?
): GDXseerManager<EffekseerMetalManagerAdapter>(maxSpriteCount, autoFlip, renderContext) {

    //region Properties

    private val effekseerManagerAdapterNameInstance: String by lazy {
        GDXseerMetalManager::class.simpleName!!
    }

    //endregion

    //region Overrides

    override fun getEffekseerManagerAdapterName(): String {
        return this.effekseerManagerAdapterNameInstance
    }

    override fun createEffekseerManagerAdapter(): EffekseerMetalManagerAdapter {
        return EffekseerMetalManagerAdapter(this.maxSpriteCount, this.autoFlip)
    }

    //endregion
}