package io.github.niraj_rayalla.gdxseer.loader

/**
 * Used to add a given asset directly to an [com.badlogic.gdx.assets.AssetManager]. Subclass [com.badlogic.gdx.assets.AssetManager]
 * and implement this by calling the [com.badlogic.gdx.assets.AssetManager.addAsset] to extend
 * its functionality for use in loading Effekseer effects.
 */
interface DirectAssetAdder {
    fun <T> addAssetDirectly(fileName: String, type: Class<T>, asset: T)
}