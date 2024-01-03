package io.github.niraj_rayalla.gdxseer.example

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.utils.Logger
import io.github.niraj_rayalla.gdxseer.loader.DirectAssetAdder

/**
 * An [AssetManager] that implements [DirectAssetAdder] to avoid temporary caching of loaded GDXseer assets.
 */
class ExampleAssetManager: AssetManager, DirectAssetAdder {

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

    //region DirectAssetAdder

    override fun <T> addAssetDirectly(fileName: String, type: Class<T>, asset: T) {
        this.addAsset(fileName, type, asset)
    }

    //endregion
}