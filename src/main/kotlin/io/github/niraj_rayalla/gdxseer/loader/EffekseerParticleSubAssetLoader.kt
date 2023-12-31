package io.github.niraj_rayalla.gdxseer.loader

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.MaterialRefWrapper
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.ModelRefWrapper
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.TextureRefWrapper

/**
 * Loads the assets needed for particle effects. Should be started by [EffekseerParticleAssetLoader].
 */
@Suppress("unused")
class EffekseerParticleSubAssetLoader(resolver: FileHandleResolver?): AsynchronousAssetLoader<EffekseerParticleSubAssetLoader.Companion.Result, EffekseerParticleSubAssetLoader.Companion.Parameters>(resolver) {

    companion object {

        //region Classes

        /**
         * Parameters needed to load [EffekseerParticleSubAssetLoader].
         */
        class Parameters: AssetLoaderParameters<Result?>()

        /**
         * Contains the data for any file loaded that is used in an Effekseer effect.
         */
        abstract class Result: Disposable {

            //region State

            abstract val fileHandle: FileHandle
            abstract val data: ByteArray

            /**
             * Contains the wrapped c++ reference pointer to the type of Effekseer asset loaded. For example if this result hold
             * texture data then this must be a [TextureRefWrapper].
             * Possible values:
             * [TextureRefWrapper]
             * [ModelRefWrapper]
             * [MaterialRefWrapper]
             */
            var referenceWrapper: Any? = null

            //endregion

            //region Disposable

            override fun dispose() {
                // Remove loaded reference wrappers
                when (val refWrapper = this.referenceWrapper) {
                    is TextureRefWrapper -> refWrapper.delete()
                    is ModelRefWrapper -> refWrapper.delete()
                    is MaterialRefWrapper -> refWrapper.delete()
                }
                this.referenceWrapper = null
            }

            //endregion
        }

        /**
         * An implementation of [Result] tha can only be made in [EffekseerParticleSubAssetLoader].
         */
        private class ResultImpl(override val fileHandle: FileHandle, override val data: ByteArray): Result()

        //endregion
    }

    //region State

    private var loadedResult: Result? = null

    //endregion

    //region Overrides

    override fun getDependencies(fileName: String, file: FileHandle, parameter: Parameters): Array<AssetDescriptor<*>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: Parameters) {
        this.loadedResult = ResultImpl(file, file.readBytes())
    }

    override fun unloadAsync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: Parameters?) {
        super.unloadAsync(manager, fileName, file, parameter)
        this.loadedResult = null
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: Parameters): Result? {
        return this.loadedResult
    }

    //endregion
}