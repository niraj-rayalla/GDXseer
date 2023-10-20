package io.github.niraj_rayalla.gdxseer.loader

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver

/**
 * An [AssetManager] that implements [DirectAssetAdder].
 */
class AssetManagerWithDirectAssetAdder: AssetManager, DirectAssetAdder {

    //region Constructors

    constructor()
    constructor(resolver: FileHandleResolver): super(resolver)
    constructor(resolver: FileHandleResolver, defaultLoaders: Boolean): super(resolver, defaultLoaders)

    //endregion

    //region Overrides

    override fun <T> addAssetDirectly(fileName: String, type: Class<T>, asset: T) {
        addAsset(fileName, type, asset)
    }

    //endregion
}