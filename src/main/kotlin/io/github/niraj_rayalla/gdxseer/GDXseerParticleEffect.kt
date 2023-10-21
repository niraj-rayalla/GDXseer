package io.github.niraj_rayalla.gdxseer

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters.LoadedCallback
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.EffekseerEffectAdapter
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.EffekseerManagerAdapter
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeRoot
import io.github.niraj_rayalla.gdxseer.effekseer.Vector3D
import io.github.niraj_rayalla.gdxseer.loader.EffekseerParticleAssetLoader
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField

/**
 * The class that manages an Effekseer particle effect object (by wrapping an [EffekseerEffectAdapter]) and includes relevant GDX logic. This is the class for representing
 * an Effekseer particle effect.
 * All GDX relevant logic lives in this class, but not all properties and methods of the Effekseer manager object are available here. Use [effekseerEffectAdapter],
 * which is the [EffekseerEffectAdapter] being wrapped by this class, to access all of the JVM accessible Effekseer logic.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class GDXseerParticleEffect(val manager: GDXseerManager<*>): Disposable {

    companion object {
        /**
         * Called when the particle effect is loaded.
         */
        interface LoadedListener {
            fun onEffectLoaded()
        }

        /**
         * Called when a playing particle effect has finished playing its animation.
         */
        interface OnAnimationComplete {
            fun onAnimationCompleted()
        }
    }

    //region State

    /**
     * The [EffekseerEffectAdapter] instance that is being managed by this [GDXseerParticleEffect]. [EffekseerEffectAdapter] is the instance that actually
     * tells the Effekseer library what to do for a single particle effect. All the remaining non-wrapped methods not in [GDXseerParticleEffect] will be in this
     * [EffekseerEffectAdapter] instance.
     */
    val effekseerEffectAdapter = EffekseerEffectAdapter()

    /**
     *
     * The [Matrix4] instance used for transforming this particle. Getting this value will also queue a transform update to Effekseer so that the transform
     * in this particle effect object is reflected in Effekseer.
     */
    val transform = Matrix4()
        get() {
            this.queueUpdateTransformMatrix()
            return field
        }
    private val matrixData = FloatArray(12)

    /**
     * Will be set to true, when this [GDXseerParticleEffect] is added to a [GDXseerManager], and will be reset when removed from it.
     */
    protected var isAddedToManager = false
        private set
    /**
     * The identifier to this particle effect when being played.
     */
    var handle: Int = 0
        private set
    /**
     * @return True, if the particle effect managed by this [GDXseerParticleEffect] is currently being played, otherwise false.
     * This is true, even if the animation is paused. This is basically just to track is play was called.
     */
    internal var isInPlayingState = false
    private var magnification = 1.0f

    /**
     * True, if the transform in this instance, what GDX can see, has been updated and thus needs to be sent to Effekseer to mirror the value, otherwise false.
     */
    private var isTransformMatrixUpdateQueued = false
    /**
     * True, if the transform in this instance should be updated to mirror the value in Effekseer for this particle effect, otherwise false.
     */
    private var isGetTransformMatrixFromEffekseerQueued = true

    /**
     * The [AssetManager] instance the assets needed for this particle effect are being loaded in, if any. Null, if not being loaded.
     */
    private var assetManagerBeingLoadedIn: AssetManager? = null

    /**
     * Set to listen to when this particle effect finishes playing its animation.
     */
    var onAnimationComplete: OnAnimationComplete? = null

    //endregion

    //region Managed Fields

    private val rootNodeField: EffekseerManagedField<EffectNodeRoot> = EffekseerManagedField(
        { effekseerEffectAdapter.GetRootNode() },
        {
            // Can't set root node
        }
    )

    //endregion

    //region Properties

    /**
     * @return The root node instance of this particle effect.
     */
    val rootNode: EffectNodeRoot
        get() {
            return this.rootNodeField.value
        }

    /**
     * Get/Set if this particle effect is visible.
     */
    var isShown: Boolean
        get() {
            return this.manager.effekseerManagerAdapter.GetShown(this.handle)
        }
        set(value) {
            this.manager.effekseerManagerAdapter.SetShown(this.handle, value)
        }

    /**
     * Get/Set if this particle effect's animation is currently paused.
     */
    var isPaused: Boolean
        get() {
            return this.manager.effekseerManagerAdapter.GetPaused(this.handle)
        }
        set(value) {
            this.manager.effekseerManagerAdapter.SetPaused(this.handle, value)
        }

    /**
     * Get/Set the layer of this particle effect.
     */
    var layer: Int
        get() {
            return this.manager.effekseerManagerAdapter.GetLayer(this.handle)
        }
        set(value) {
            this.manager.effekseerManagerAdapter.SetLayer(this.handle, value)
        }

    /**
     * Get/Set the group mask of this particle effect.
     */
    var groupMask: Long
        get() {
            return this.manager.effekseerManagerAdapter.GetGroupMask(this.handle)
        }
        set(value) {
            this.manager.effekseerManagerAdapter.SetGroupMask(this.handle, value)
        }

    /**
     * Get/Set the speed (time scale) of this particle effect.
     */
    var groupSpeed: Float
        get() {
            return this.manager.effekseerManagerAdapter.GetSpeed(this.handle)
        }
        set(value) {
            this.manager.effekseerManagerAdapter.SetSpeed(this.handle, value)
        }

    //endregion

    //region Transform Matrix Methods

    /**
     * @return True, if there were transform changes that were applied. Call [getTransformValuesForEffekseer] to get the transform floats to send to Effekseer.
     */
    internal fun updateTransformMatrixIfQueued(): Boolean {
        if (this.isTransformMatrixUpdateQueued) {
            this.transform.extract4x3Matrix(this.matrixData)
            this.isTransformMatrixUpdateQueued = false

            return true
        }

        return false
    }

    internal fun getTransformValuesForEffekseer(): FloatArray {
        return this.matrixData
    }

    /**
     * Gets the transform of this particle effect from Effekseer and puts it into [transform].
     */
    private fun getTransformFromEffekseer() {
        val dst = this.manager.effekseerManagerAdapter.GetMatrix(this.handle)

        this.transform.values[Matrix4.M00] = dst[0]
        this.transform.values[Matrix4.M10] = dst[1]
        this.transform.values[Matrix4.M20] = dst[2]
        this.transform.values[Matrix4.M01] = dst[3]
        this.transform.values[Matrix4.M11] = dst[4]
        this.transform.values[Matrix4.M21] = dst[5]
        this.transform.values[Matrix4.M02] = dst[6]
        this.transform.values[Matrix4.M12] = dst[7]
        this.transform.values[Matrix4.M22] = dst[8]
        this.transform.values[Matrix4.M03] = dst[9]
        this.transform.values[Matrix4.M13] = dst[10]
        this.transform.values[Matrix4.M23] = dst[11]
    }

    /**
     * Queues an update of the transform matrix, to be sent to Effekseer, for this effect the next time this effect is drawn.
     * [EffekseerManagerAdapter.SetMatrix] should be called to send the updated matrix to Effekseer.
     */
    private fun queueUpdateTransformMatrix() {
        this.isTransformMatrixUpdateQueued = true
    }

    /**
     * Helper function that sets the translation of this particle effect. Just sets the translation in [transform].
     */
    fun setTranslation(x: Float, y: Float, z: Float) {
        this.transform.setTranslation(x, y, z)
    }

    /**
     * Helper function translates of this particle effect. Just translates [transform].
     */
    fun translate(x: Float, y: Float, z: Float) {
        this.transform.translate(x, y, z)
    }

    /**
     * Helper function that sets the rotation of this particle effect. Just sets the rotation in [transform].
     */
    fun setRotation(axis: Vector3, angle: Float) {
        this.transform.setToRotation(axis, angle)
    }

    /**
     * Helper function that sets the rotation of this particle effect. Just sets the rotation in [transform].
     */
    fun setRotation(axisX: Float, axisY: Float, axisZ: Float, degrees: Float) {
        this.transform.setToRotation(axisX, axisY, axisZ, degrees)
    }

    /**
     * Helper function that rotates this particle effect. Just rotates [transform].
     */
    fun rotate(axis: Vector3, angle: Float) {
        this.transform.rotate(axis, angle)
    }

    /**
     * Helper function that rotates this particle effect. Just rotates [transform].
     */
    fun rotate(axisX: Float, axisY: Float, axisZ: Float, degrees: Float) {
        this.transform.rotate(axisX, axisY, axisZ, degrees)
    }

    /**
     * Helper function that sets the scale of this particle effect. Just sets the scale in [transform].
     */
    fun setScale(x: Float, y: Float, z: Float) {
        this.transform.setToScaling(x, y, z)
    }

    /**
     * Helper function that scales this particle effect. Just scales [transform].
     */
    fun scale(x: Float, y: Float, z: Float) {
        this.transform.scale(x, y, z)
    }

    //endregion

    //region Overrides

    override fun dispose() {
        this.stop()
        this.effekseerEffectAdapter.delete()
    }

    //endregion

    //region Private Methods

    /**
     * Adds this particle instance to the manager if it was not added to it.
     */
    private fun addToManager() {
        if (!this.isAddedToManager) {
            this.manager.addParticleEffect(this)
            this.isAddedToManager = true
        }
    }

    /**
     * Removes this particle instance from the manager if it was added to it.
     */
    private fun removeFromManager() {
        if (this.isAddedToManager) {
            this.manager.removeParticleEffect(this)
            this.isAddedToManager = false
        }
    }

    //endregion

    //region Internal Methods

    /**
     * Call each frame.
     */
    internal open fun update(delta: Float) {
        // Check for loading
        this.assetManagerBeingLoadedIn?.update()
    }

    //endregion

    //region Public Methods

    //region Loading

    /**
     * @return The [AssetDescriptor] used for loading the particle effect at the given [FileHandle] into this instance.
     * The [AssetDescriptor] returned will have its [com.badlogic.gdx.assets.AssetLoaderParameters.loadedCallback] set.
     */
    fun getAssetDescriptorWithoutLoading(effectFileHandle: FileHandle, loadedListener: LoadedListener?): AssetDescriptor<EffekseerParticleAssetLoader.Companion.Result> {
        // Create the asset descriptor for sending to the given AssetManager
        val assetDescriptor = EffekseerParticleAssetLoader.getMainFileAssetDescriptor(effectFileHandle, this.manager.effekseerManagerAdapter, this.effekseerEffectAdapter, this.magnification)

        // Listen for finished loading
        assetDescriptor.params.loadedCallback = LoadedCallback { assetManager, _, _ ->
            // Reset the tracked asset manager
            assetManagerBeingLoadedIn = null
            // Load the data into the effect
            val loadedData = assetManager.get(assetDescriptor)
            loadFromEffectAssetResult(loadedData)

            // Call listener
            loadedListener?.onEffectLoaded()
        }

        return assetDescriptor
    }

    /**
     * Loads the particle loaded effect data ([EffekseerParticleAssetLoader.Result]) into this instance.
     */
    fun loadFromEffectAssetResult(result: EffekseerParticleAssetLoader.Companion.Result) {
        result.loadIntoEffect(this.manager.effekseerManagerAdapter, this.effekseerEffectAdapter, this.magnification)
    }

    /**
     * Asynchronously loads the given effect file. An optional [LoadedListener] can be given for listening to when the effect has finished loading.
     */
    fun asyncLoad(assetManager: AssetManager, effectFileHandle: FileHandle, loadedListener: LoadedListener) {
        // Track the asset manager for updating load state
        this.assetManagerBeingLoadedIn = assetManager

        val assetDescriptor = this.getAssetDescriptorWithoutLoading(effectFileHandle, loadedListener)

        // Now start the load
        assetManager.load(assetDescriptor)
    }

    /**
     * Asynchronously loads the given effect file. An optional [LoadedListener] can be given for listening to when the effect has finished loading.
     */
    fun asyncLoad(assetManager: AssetManager, path: String, loadedListener: LoadedListener) {
        // Get the file handle
        val effectFileHandle = assetManager.fileHandleResolver.resolve(path)

        // Call load() with the generated file handle
        this.asyncLoad(assetManager, effectFileHandle, loadedListener)
    }

    /**
     * Synchronously loads the given effect file.
     */
    fun syncLoad(assetManager: AssetManager, effectFileHandle: FileHandle) {
        EffekseerParticleAssetLoader.syncLoad(assetManager, effectFileHandle, this.manager.effekseerManagerAdapter, this.effekseerEffectAdapter, this.magnification)
    }

    /**
     * Synchronously loads the given effect file.
     */
    fun syncLoad(assetManager: AssetManager, path: String) {
        // Get the file handle
        val effectFileHandle = assetManager.fileHandleResolver.resolve(path)

        // Call load() with the generated file handle
        this.syncLoad(assetManager, effectFileHandle)
    }

    //endregion

    //region Manager Wrapper Methods

    /**
     * Plays the animation of this particle effect.
     */
    fun play(): Int {
        // Add this effect to the manager class if not already added
        this.addToManager()
        this.isInPlayingState = true
        this.handle = this.manager.effekseerManagerAdapter.Play(this.effekseerEffectAdapter)

        if (this.isGetTransformMatrixFromEffekseerQueued) {
            // Get the initial transforms from Effekseer
            this.getTransformFromEffekseer()
            this.isGetTransformMatrixFromEffekseerQueued = false
        }
        this.queueUpdateTransformMatrix()

        return this.handle
    }

    /**
     * Set [isPaused] to true.
     */
    fun pause() {
        this.isPaused = true
    }

    /**
     * Set [isPaused] to false.
     */
    fun resume() {
        this.isPaused = false
    }

    /**
     * Stops animation of this particle effect.
     */
    fun stop() {
        if (this.isInPlayingState) {
            this.manager.effekseerManagerAdapter.StopEffect(this.handle)
            this.isInPlayingState = false
        }
        // Removes this effect from the manager class if added
        this.removeFromManager()
    }

    fun setTargetLocation(x: Float, y: Float, z: Float) {
        this.manager.effekseerManagerAdapter.SetTargetLocation(this.handle, x, y, z)
    }

    fun setTargetLocation(location: Vector3D) {
        this.manager.effekseerManagerAdapter.SetTargetLocation(this.handle, location)
    }

    fun getDynamicInput(index: Int): Float {
        return this.manager.effekseerManagerAdapter.GetDynamicInput(this.handle, index)
    }

    fun setDynamicInput(index: Int, value: Float) {
        this.manager.effekseerManagerAdapter.SetDynamicInput(this.handle, index, value)
    }

    fun setTimeScale(timeScale: Float) {
        this.manager.effekseerManagerAdapter.SetTimeScaleByHandle(this.handle, timeScale)
    }

    //endregion

    //endregion
}