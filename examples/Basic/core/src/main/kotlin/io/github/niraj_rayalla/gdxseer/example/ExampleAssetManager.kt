package io.github.niraj_rayalla.gdxseer.example

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.utils.Logger

/**
 * An [AssetManager] that has a logger set.
 */
class ExampleAssetManager: AssetManager {

    //region Constructors

    constructor() : super()
    constructor(resolver: FileHandleResolver?) : super(resolver)
    constructor(resolver: FileHandleResolver?, defaultLoaders: Boolean) : super(resolver, defaultLoaders)

    //endregion

    //region Init

    init {
        this.logger = Logger(ExampleAssetManager::class.simpleName!!, Logger.DEBUG)
    }

    //endregion
}