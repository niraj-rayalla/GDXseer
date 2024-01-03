package io.github.niraj_rayalla.gdxseer.loader

import com.badlogic.gdx.Application
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.ObjectMap
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.EffekseerEffectAdapter
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.EffekseerManagerAdapter
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.EffekseerTextureType
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.MaterialRefWrapper
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.ModelRefWrapper
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.TextureRefWrapper
import io.github.niraj_rayalla.gdxseer.loader.EffekseerParticleAssetLoader.Companion.Result
import io.github.niraj_rayalla.gdxseer.utils.FilePath
import io.github.niraj_rayalla.gdxseer.utils.withNewPath
import java.io.File

/**
 * Loads the particle effect and its dependencies and produces a [Result] which can then load the assets into a given [EffekseerEffectAdapter].
 */
@Suppress("unused")
class EffekseerParticleAssetLoader: AsynchronousAssetLoader<Result, EffekseerParticleAssetLoader.Companion.Parameters> {

    companion object {

        /**
         * The string that is appended to the end of an efk file to be loaded by [EffekseerParticleAssetLoader].
         */
        private const val MAIN_EFK_FILE_POSTFIX = "_GDXseer"

        /**
         * All types of textures.
         */
        private var textureTypes: Array<EffekseerTextureType> = arrayOf(
            EffekseerTextureType.Color,
            EffekseerTextureType.Normal,
            EffekseerTextureType.Distortion
        )

        //region File Paths

        /**
         * @return [FileHandle] that represents the given file path and file type for loading an effect related file.
         */
        private fun getPathAsFileHandle(app: Application, files: Files, path: String, fileType: Files.FileType): FileHandle? {
            // Simplify/Normalize the given path so that "../" usages are removed.
            val normalizedPath: String = if (app.type == Application.ApplicationType.iOS) {
                FilePath.getNormalizedPath(path)
            }
            else {
                File(path).toPath().normalize().toString()
            }

            return when (fileType) {
                Files.FileType.Classpath -> files.classpath(normalizedPath)
                Files.FileType.Internal -> files.internal(normalizedPath)
                Files.FileType.External -> files.external(normalizedPath)
                Files.FileType.Absolute -> files.absolute(normalizedPath)
                Files.FileType.Local -> files.local(normalizedPath)
                else -> files.internal(normalizedPath)
            }
        }

        private fun getPathToSubAsset(effectFileHandle: FileHandle, subAssetPath: String): String {
            return FilePath.getParentPath(effectFileHandle.path()).let {
                if (it.isEmpty()) {
                    subAssetPath
                }
                else "$it${File.separator}$subAssetPath"
            }
        }

        private fun getTexturePath(effectFileHandle: FileHandle, textureIndex: Int, textureType: EffekseerTextureType, effekseerEffectAdapter: EffekseerEffectAdapter): String {
            return getPathToSubAsset(effectFileHandle, effekseerEffectAdapter.GetTexturePath(textureIndex, textureType))
        }

        private fun getModelPath(effectFileHandle: FileHandle, modelIndex: Int, effekseerEffectAdapter: EffekseerEffectAdapter): String {
            return getPathToSubAsset(effectFileHandle, effekseerEffectAdapter.GetModelPath(modelIndex))
        }

        private fun getMaterialPath(effectFileHandle: FileHandle, materialIndex: Int, effekseerEffectAdapter: EffekseerEffectAdapter): String {
            return getPathToSubAsset(effectFileHandle, effekseerEffectAdapter.GetMaterialPath(materialIndex))
        }

        //endregion

        //region Asset Descriptors

        /**
         * @return The [AssetDescriptor] to use for the main effect file.
         */
        @Suppress("MemberVisibilityCanBePrivate")
        fun getMainFileAssetDescriptor(
            effectFileHandle: FileHandle, effekseerManagerAdapter: EffekseerManagerAdapter?, effekseerEffectAdapter: EffekseerEffectAdapter?, magnification: Float
        ): AssetDescriptor<Result> {
            val parameters = Parameters()
            parameters.set(effectFileHandle, effekseerManagerAdapter, effekseerEffectAdapter, magnification)
            return AssetDescriptor(effectFileHandle.withNewPath("${effectFileHandle.path()}$MAIN_EFK_FILE_POSTFIX"), Result::class.java, parameters)
        }

        /**
         * @return A new [AssetDescriptor] instance for loading the given effect related file handle.
         */
        private fun getAssetDescriptorForSubAssetFileHandle(
            fileHandle: FileHandle,
            parameters: EffekseerParticleSubAssetLoader.Companion.Parameters?
        ): AssetDescriptor<EffekseerParticleSubAssetLoader.Companion.Result> {
            return AssetDescriptor(
                fileHandle,
                EffekseerParticleSubAssetLoader.Companion.Result::class.java, parameters
            )
        }

        //endregion

        //region Cache

        /**
         * Used to temporarily keep track of loaded the particle's sub-assets until it can be placed in the [AssetManager].
         */
        private val loaderCachedAssets: ObjectMap<String, EffekseerParticleSubAssetLoader.Companion.Result> = ObjectMap<String, EffekseerParticleSubAssetLoader.Companion.Result>(24)

        private fun cacheAssetInLoaderUntilInAssetManager(itemPath: String, asset: EffekseerParticleSubAssetLoader.Companion.Result) {
            synchronized(loaderCachedAssets) { loaderCachedAssets.put(itemPath, asset) }
        }

        private fun getCachedAssetInLoaderOrAssetManager(assetManager: AssetManager, itemPath: String): EffekseerParticleSubAssetLoader.Companion.Result? {
            synchronized(loaderCachedAssets) {
                return loaderCachedAssets.get(itemPath) ?: let {
                    // Get from asset manager
                    if (assetManager.contains(itemPath, EffekseerParticleSubAssetLoader.Companion.Result::class.java)) {
                        assetManager.get(itemPath, EffekseerParticleSubAssetLoader.Companion.Result::class.java)
                    }
                    else {
                        null
                    }
                }
            }
        }

        private fun removeCacheAssetInLoader(itemPath: String) {
            synchronized(loaderCachedAssets) { loaderCachedAssets.remove(itemPath) }
        }

        //endregion

        //region Classes

        /**
         * Parameters needed to load [EffekseerParticleAssetLoader].
         */
        class Parameters internal constructor() : AssetLoaderParameters<Result?>() {

            var effectFileHandle: FileHandle? = null
            var effekseerManagerAdapter: EffekseerManagerAdapter? = null
            var effekseerEffectAdapter: EffekseerEffectAdapter? = null
            var magnification = 0f
            internal var result: Result? = null

            fun set(effectFileHandle: FileHandle?, effekseerManagerAdapter: EffekseerManagerAdapter?, effekseerEffectAdapter: EffekseerEffectAdapter?, magnification: Float) {
                this.effectFileHandle = effectFileHandle
                this.effekseerEffectAdapter = effekseerEffectAdapter
                this.effekseerManagerAdapter = effekseerManagerAdapter
                this.magnification = magnification
                result = null
            }

            fun resetWithoutRecycling() {
                this.set(null, null, null, 0f)
                loadedCallback = null
            }
        }

        internal data class LoadedTextureResult(
            val textureType: EffekseerTextureType,
            val textureIndex: Int,
            val assetData: EffekseerParticleSubAssetLoader.Companion.Result
        )

        /**
         * Keeps track of the file data loaded for an effect.
         */
        class Result internal constructor(
            @Suppress("MemberVisibilityCanBePrivate")
            val loadedWithEffekseerEffectAdapter: EffekseerEffectAdapter
        ) {

            //region State

            internal lateinit var effectFileData: EffekseerParticleSubAssetLoader.Companion.Result
            internal lateinit var textures: com.badlogic.gdx.utils.Array<LoadedTextureResult>
            internal lateinit var models: com.badlogic.gdx.utils.Array<EffekseerParticleSubAssetLoader.Companion.Result>
            internal lateinit var materials: com.badlogic.gdx.utils.Array<EffekseerParticleSubAssetLoader.Companion.Result>

            //endregion

            //region Private Methods

            /**
             * Loads the GDX loaded sub assets of the particle into Effekseer. Should be called from the thread that Effekseer is running in (GL thread).
             */
            internal fun loadSubAssetsIntoEffekseer(effekseerEffectAdapter: EffekseerEffectAdapter, effekseerIsMipMapEnabledDecider: EffekseerIsMipMapEnabledDecider?) {
                // Load textures that haven't already been loaded into Effekseer
                for (textureAsset in this.textures) {
                    if (textureAsset.assetData.referenceWrapper == null) {
                        // First get if mipmaps should be used for the current texture asset
                        val shouldUseMipMaps = effekseerIsMipMapEnabledDecider?.isMipMapEnabledForTextureFile(textureAsset.assetData.fileHandle) ?: true

                        // Load into effekseer
                        textureAsset.assetData.referenceWrapper = effekseerEffectAdapter.LoadTexture(
                            textureAsset.assetData.data,
                            textureAsset.assetData.data.size,
                            textureAsset.textureIndex,
                            textureAsset.textureType,
                            shouldUseMipMaps
                        )
                        if (textureAsset.assetData.referenceWrapper == null || !(textureAsset.assetData.referenceWrapper as TextureRefWrapper).hasRef) {
                            System.out.printf("Failed to load Effekseer particle texture file %s.\n", textureAsset.assetData.fileHandle.toString())
                        }
                    }
                }

                // Load models that haven't already been loaded into Effekseer
                var currentIndex = 0
                for (modelAsset in this.models) {
                    if (modelAsset.referenceWrapper == null) {
                        modelAsset.referenceWrapper = effekseerEffectAdapter.LoadModel(modelAsset.data, modelAsset.data.size, currentIndex)
                        if (modelAsset.referenceWrapper == null || !(modelAsset.referenceWrapper as ModelRefWrapper).hasRef) {
                            System.out.printf("Failed to load Effekseer particle model file %s.\n", modelAsset.fileHandle.toString())
                        }
                    }
                    currentIndex += 1
                }

                // Load materials that haven't already been loaded into Effekseer
                currentIndex = 0
                for (materialAsset in this.materials) {
                    if (materialAsset.referenceWrapper == null) {
                        materialAsset.referenceWrapper = effekseerEffectAdapter.LoadMaterial(materialAsset.data, materialAsset.data.size, currentIndex)
                        if (materialAsset.referenceWrapper == null || !(materialAsset.referenceWrapper as MaterialRefWrapper).hasRef) {
                            System.out.printf("Failed to load Effekseer particle material file %s.\n", materialAsset.fileHandle.toString())
                        }
                    }
                    currentIndex += 1
                }
            }

            internal fun setSubAssetsInEffect(effekseerEffectAdapter: EffekseerEffectAdapter) {
                // Textures set
                for (textureAsset in this.textures) {
                    effekseerEffectAdapter.SetTexture(textureAsset.textureIndex, textureAsset.textureType, textureAsset.assetData.referenceWrapper as TextureRefWrapper)
                }

                // Models load
                var currentIndex = 0
                for (modelAsset in this.models) {
                    effekseerEffectAdapter.SetModel(currentIndex, modelAsset.referenceWrapper as ModelRefWrapper)
                    currentIndex += 1
                }

                // Materials load
                currentIndex = 0
                for (materialAsset in this.materials) {
                    effekseerEffectAdapter.SetMaterial(currentIndex, materialAsset.referenceWrapper as MaterialRefWrapper)
                    currentIndex += 1
                }

                // TODO sound
            }

            //endregion

            //region Public Methods

            /**
             * Loads the effect sub asset references in this result instance into the given [EffekseerManagerAdapter].
             */
            fun loadIntoEffect(effekseerManagerAdapter: EffekseerManagerAdapter, effekseerEffectAdapter: EffekseerEffectAdapter, magnification: Float) {
                try {
                    if (this.loadedWithEffekseerEffectAdapter !== effekseerEffectAdapter) {
                        // Main file load
                        if (!effekseerEffectAdapter.load(effekseerManagerAdapter, this.effectFileData.data, this.effectFileData.data.size, magnification)) {
                            throw Exception("Failed to load main Effekseer particle file " + this.effectFileData.fileHandle.toString())
                        }

                        // Sub assets load
                        setSubAssetsInEffect(effekseerEffectAdapter)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            /**
             * Adds the dependencies of this item to the given list.
             */
            fun getDependencies(outDependencies: com.badlogic.gdx.utils.Array<EffekseerParticleSubAssetLoader.Companion.Result>) {
                // Add textures
                for (texture in this.textures) {
                    outDependencies.add(texture.assetData)
                }

                // Add models
                for (model in this.models) {
                    outDependencies.add(model)
                }

                // Add materials
                for (material in this.materials) {
                    outDependencies.add(material)
                }
            }

            //endregion
        }

        /**
         * A [GdxRuntimeException] that gets thrown when an Effekseer effect at location [fileHandle] could not be loaded because [deletedEffekseerManagerAdapter] was deleted.
         * Check to see when [deletedEffekseerManagerAdapter] was deleted to fix the issue or catch this exception if it's recoverable.
         */
        @Suppress("MemberVisibilityCanBePrivate")
        class GDXseerDeletedEffekseerManagerAdapterException(val fileHandle: FileHandle?, val deletedEffekseerManagerAdapter: EffekseerManagerAdapter?)
            : GdxRuntimeException("Failed to load Effekseer particle effect of file: \"$fileHandle\" because the manager adapter was already deleted.")

        //endregion

        /**
         * Synchronously loads the given Effekseer effect.
         */
        @Throws(IllegalStateException::class)
        fun syncLoad(
            assetManager: AssetManager, effectFileHandle: FileHandle,
            effekseerManagerAdapter: EffekseerManagerAdapter, effekseerEffectAdapter: EffekseerEffectAdapter,
            magnification: Float
        ) {
            // Request load
            val mainAssetDescriptor: AssetDescriptor<Result> = getMainFileAssetDescriptor(effectFileHandle, effekseerManagerAdapter, effekseerEffectAdapter, magnification)
            assetManager.load(mainAssetDescriptor)

            // Wait for load to finish
            assetManager.finishLoading()

            // Now send the loaded file data to the effect if it wasn't already sent
            val loadedData: Result = assetManager.get(mainAssetDescriptor)
            loadedData.loadIntoEffect(effekseerManagerAdapter, effekseerEffectAdapter, magnification)
        }
    }

    //region State

    private val app: Application
    private val files: Files

    /**
     * Used for loading the sub assets of an effect without calling the [AssetManager]. This is needed because the sub assets
     * are not known until the main body data is loaded into the effect object, and thus we can't add the sub assets to
     * the dependencies list. When the main body data is loaded, all the sub assets are retrieved and loaded using this loader.
     */
    private val subAssetLoader = EffekseerParticleSubAssetLoader(null)

    /**
     * Use this to determine if a texture should use mipmaps.
     */
    private val effekseerIsMipMapEnabledDecider: EffekseerIsMipMapEnabledDecider?

    //endregion

    //region Constructors

    /**
     * @param effekseerIsMipMapEnabledDecider Pass in to override if a texture should use mipmaps. If this is null, then mipmaps will always be true.
     */
    constructor(
        fileHandleResolver: FileHandleResolver?, effekseerIsMipMapEnabledDecider: EffekseerIsMipMapEnabledDecider?,
        app: Application = Gdx.app, files: Files = Gdx.files
    ): super(fileHandleResolver) {
        this.effekseerIsMipMapEnabledDecider = effekseerIsMipMapEnabledDecider
        this.app = app
        this.files = files
    }

    constructor(fileHandleResolver: FileHandleResolver?, app: Application = Gdx.app, files: Files = Gdx.files): this(fileHandleResolver, null, app, files)

    //endregion

    //region Private Methods

    private fun cacheSubAssetInAssetManager(asset: EffekseerParticleSubAssetLoader.Companion.Result, manager: AssetManager) {
        if (manager is DirectAssetAdder) {
            manager.addAssetDirectly(asset.fileHandle.path(), EffekseerParticleSubAssetLoader.Companion.Result::class.java, asset)
        }
        else {
            // Cache the asset in memory until the AssetManager puts the asset in its state
            cacheAssetInLoaderUntilInAssetManager(asset.fileHandle.path(), asset)

            // Create the instances for loading
            val parameters = EffekseerParticleSubAssetLoader.Companion.Parameters()
            parameters.loadedResult = asset
            parameters.loadedCallback = LoadedCallback { _, fileName, _ -> removeCacheAssetInLoader(fileName) }

            // Start load
            manager.load(getAssetDescriptorForSubAssetFileHandle(asset.fileHandle, parameters))
        }
    }

    //endregion

    //region Overrides
    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: Parameters?): com.badlogic.gdx.utils.Array<AssetDescriptor<*>> {
        // Check that the manager and effect cores are available
        checkNotNull(parameter?.effekseerManagerAdapter) { "EffekseerManagerAdapter is needed to load Effekseer effects." }
        checkNotNull(parameter?.effekseerEffectAdapter) { "EffekseerEffectAdapter is needed to load Effekseer effects." }

        // Now create the result list with the found capacity
        val assets = com.badlogic.gdx.utils.Array<AssetDescriptor<*>>(false, 1)

        // Add the main file
        assets.add(getAssetDescriptorForSubAssetFileHandle(parameter!!.effectFileHandle!!, EffekseerParticleSubAssetLoader.Companion.Parameters()))

        return assets
    }

    override fun loadAsync(manager: AssetManager, fileName: String?, file: FileHandle?, parameter: Parameters) {
        val effectFileHandle = parameter.effectFileHandle!!
        val effekseerEffectAdapter = parameter.effekseerEffectAdapter!!

        // Create the result object that will be returned at the end
        val result = Result(effekseerEffectAdapter)
        parameter.result = result

        // Load main file data into the effect instance
        val mainFileAsset = manager[getAssetDescriptorForSubAssetFileHandle(effectFileHandle, null)]!!
        result.effectFileData = mainFileAsset

        // Don't continue if the manager adapter has already been deleted
        if (EffekseerManagerAdapter.getCPtr(parameter.effekseerManagerAdapter) != 0L) {
            effekseerEffectAdapter.load(parameter.effekseerManagerAdapter, mainFileAsset.data, mainFileAsset.data.size, parameter.magnification)

            // Load and add the textures
            var texturesCount = 0
            for (textureType in textureTypes) {
                texturesCount += effekseerEffectAdapter.GetTextureCount(textureType)
            }
            result.textures = com.badlogic.gdx.utils.Array<LoadedTextureResult>(false, texturesCount)
            for (textureType in textureTypes) {
                val currentTextureCount: Int = effekseerEffectAdapter.GetTextureCount(textureType)
                for (i in 0 until currentTextureCount) {
                    val path = getTexturePath(effectFileHandle, i, textureType, effekseerEffectAdapter)
                    val textureFileHandle = getPathAsFileHandle(this.app, this.files, path, effectFileHandle.type())
                    val cachedLoadedAsset: EffekseerParticleSubAssetLoader.Companion.Result? = getCachedAssetInLoaderOrAssetManager(manager, textureFileHandle!!.path())
                    if (cachedLoadedAsset != null) {
                        result.textures.add(LoadedTextureResult(textureType, i, cachedLoadedAsset))
                    } else {
                        // Load asset
                        val subAssetParam = EffekseerParticleSubAssetLoader.Companion.Parameters()
                        subAssetLoader.loadAsync(manager, "", textureFileHandle, subAssetParam)
                        val loadedAsset = subAssetLoader.loadSync(manager, "", textureFileHandle, subAssetParam)!!
                        // Cache asset
                        cacheSubAssetInAssetManager(loadedAsset, manager)
                        result.textures.add(LoadedTextureResult(textureType, i, loadedAsset))
                    }
                }
            }

            // Add the models
            val modelCount: Int = effekseerEffectAdapter.GetModelCount()
            result.models = com.badlogic.gdx.utils.Array<EffekseerParticleSubAssetLoader.Companion.Result>(false, modelCount)
            for (i in 0 until modelCount) {
                val path = getModelPath(effectFileHandle, i, effekseerEffectAdapter)
                val modelFileHandle = getPathAsFileHandle(this.app, this.files, path, effectFileHandle.type())
                val cachedLoadedAsset: EffekseerParticleSubAssetLoader.Companion.Result? = getCachedAssetInLoaderOrAssetManager(manager, modelFileHandle!!.path())
                if (cachedLoadedAsset != null) {
                    result.models.add(cachedLoadedAsset)
                } else {
                    // Load asset
                    val subAssetParam = EffekseerParticleSubAssetLoader.Companion.Parameters()
                    subAssetLoader.loadAsync(manager, "", modelFileHandle, subAssetParam)
                    val loadedAsset = subAssetLoader.loadSync(manager, "", modelFileHandle, subAssetParam)!!
                    // Cache asset
                    cacheSubAssetInAssetManager(loadedAsset, manager)
                    result.models.add(loadedAsset)
                }
            }

            // Add the materials
            val materialCount: Int = effekseerEffectAdapter.GetMaterialCount()
            result.materials = com.badlogic.gdx.utils.Array<EffekseerParticleSubAssetLoader.Companion.Result>(false, materialCount)
            for (i in 0 until materialCount) {
                val path = getMaterialPath(effectFileHandle, i, effekseerEffectAdapter)
                val materialFileHandle = getPathAsFileHandle(this.app, this.files, path, effectFileHandle.type())
                val cachedLoadedAsset: EffekseerParticleSubAssetLoader.Companion.Result? = getCachedAssetInLoaderOrAssetManager(manager, materialFileHandle!!.path())
                if (cachedLoadedAsset != null) {
                    result.materials.add(cachedLoadedAsset)
                } else {
                    // Load asset
                    val subAssetParam = EffekseerParticleSubAssetLoader.Companion.Parameters()
                    subAssetLoader.loadAsync(manager, "", materialFileHandle, subAssetParam)
                    val loadedAsset = subAssetLoader.loadSync(manager, "", materialFileHandle, subAssetParam)!!
                    // Cache asset
                    cacheSubAssetInAssetManager(loadedAsset, manager)
                    result.materials.add(loadedAsset)
                }
            }

            // TODO sound
        }
        else {
            // The manager adapter was deleted which means loading is impossible, so throw exception
            throw GDXseerDeletedEffekseerManagerAdapterException(file, parameter.effekseerManagerAdapter)
        }
    }

    override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: Parameters): Result {
        val result = parameter.result!!

        // Load the sub assets needed in this effect into Effekseer
        result.loadSubAssetsIntoEffekseer(parameter.effekseerEffectAdapter!!, this.effekseerIsMipMapEnabledDecider)
        // Set the sub asset references needed in this effect
        result.setSubAssetsInEffect(parameter.effekseerEffectAdapter!!)

        return parameter.result!!
    }

    //endregion
}