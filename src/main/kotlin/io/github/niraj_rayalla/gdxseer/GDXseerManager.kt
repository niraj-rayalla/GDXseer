package io.github.niraj_rayalla.gdxseer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.EffekseerManagerAdapter
import io.github.niraj_rayalla.gdxseer.effekseer.EffekseerManagerParameters.DrawParameter

/**
 * The base abstract class used to manage an Effekseer manager instance (by wrapping an [EffekseerManagerAdapter]) and includes relevant GDX logic. This is the main entry class
 * for making use of Effekseer particle effects.
 *
 * MUST CALL [initializeAll] or all the initialize step methods, such as [initializeStep1CreateManagerAdapter], [initializeStep2CreateRenderer], etc.., before an instance of
 * this [GDXseerManager] is used to render particle effects.
 * The initialize step methods are there to split the entire initialization process of the [EffekseerManagerAdapter] used by a [GDXseerManager] so that GL thread doesn't
 * need to run the entire initialization process at once if not desired.
 * Each of the initialize step methods must be run sequentially according to their number, but not all of them are required to be run from the GL thread. Check each step for
 * determining if it should be called from the GL thread.
 *
 * All GDX relevant logic lives in this class, but not all properties and methods of the Effekseer manager object are available here. Use [effekseerManagerAdapter],
 * which is the [EffekseerManagerAdapter] being wrapped by this class, to access all of the JVM accessible Effekseer logic.
 *
 * This will be implemented by each renderer backend used (such as OpenGL or Metal).
 *
 * Override the following protected methods for customizing what happens during a draw call:
 *  [shouldCameraBeUpdatedInUpdate],
 *  [onPreDraw],
 *  [onPostDraw]
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class GDXseerManager<MA: EffekseerManagerAdapter>(
    protected val maxSpriteCount: Int,
    protected val autoFlip: Boolean = true,
    /**
     * An optional [RenderContext] used to reduce graphics calls.
     */
    protected val renderContext: RenderContext?
): Disposable {

    companion object {
        private const val SINGLE_FRAME_TIME_SECONDS = 1.0f / 60.0f
        private const val SINGLE_FRAME_TIME_SECONDS_INV = 1.0f / SINGLE_FRAME_TIME_SECONDS
        private const val MAX_BATCH_SIZE_PENDING_PARTICLE_TRANSFORM = 4
    }

    //region Abstract

    /**
     * @return The name of [MA] being used for logging. Can just be the class name.
     */
    abstract fun getEffekseerManagerAdapterName(): String

    /**
     * @return A new instance of [MA] to be used for this [GDXseerManager].
     */
    abstract fun createEffekseerManagerAdapter(): MA

    //endregion

    //region State

    /**
     * The [EffekseerManagerAdapter] instance that is being managed by this [GDXseerManager]. [EffekseerManagerAdapter] is the instance that actually
     * tells the Effekseer library what to do. All the remaining non-wrapped methods not in [GDXseerManager] will be in this [EffekseerManagerAdapter] instance.
     */
    lateinit var effekseerManagerAdapter: MA
        private set

    /**
     * Can set to a [Viewport] instance that will be used to determine the render size when the camera being used during rendering is an [OrthographicCamera]. Set to null
     * to not use a custom viewport and instead use the render size from the camera being used. Is null by default.
     */
    var viewport: Viewport? = null

    /**
     * Contains all the added Effekseer particle effects ([GDXseerParticleEffect]s).
     */
    private val addedParticleEffects = Array<GDXseerParticleEffect>(false, 16)

    /**
     * The [DrawParameter] instance to use for calling the [EffekseerManagerAdapter.Draw] method. Update the state of this instance for affecting how the particles
     * are drawn (culling and render sorting).
     */
    val drawParameter: DrawParameter = DrawParameter()

    /**
     * The time passed in seconds since the start.
     */
    private var timeInSeconds = 0f

    /**
     * Keep track of particle effects that have transform changes that need to be sent to Effekseer. Using this allows for batch updating
     * to reduce the number of JNI calls per frame.
     */
    private val pendingEffectsWithTransformChanges: Array<GDXseerParticleEffect> = Array<GDXseerParticleEffect>(false, 4)

    //endregion

    //region Init

    /**
     * Creates the Effekseer manager adapter instance and sets [effekseerManagerAdapter] to the created instance.
     * NOT required to be run on the GL thread.
     */
    fun initializeStep1CreateManagerAdapter() {
        this.effekseerManagerAdapter = this.createEffekseerManagerAdapter()
    }

    /**
     * Creates the Effekseer renderer needed to render all effects.
     * REQUIRED to be run on the GL thread.
     */
    fun initializeStep2CreateRenderer() {
        this.effekseerManagerAdapter.InitializeStep1_CreateRenderer()
    }

    /**
     * Creates the Effekseer "sub" renderers (sprite, model, track, etc... renderers) which will be used by the renderer created in [initializeStep2CreateRenderer].
     * REQUIRED to be run on the GL thread.
     */
    fun initializeStep3CreateSubRenderers() {
        this.effekseerManagerAdapter.InitializeStep2_CreateSubRenderers()
    }

    /**
     * Creates the asset loaders that Effekseer uses to load effect data.
     * NOT required to be run on the GL thread.
     */
    fun initializeStep4CreateLoaders() {
        this.effekseerManagerAdapter.InitializeStep3_CreateLoaders()
    }

    /**
     * Must be called after all the other steps to finish initialization.
     * NOT required to be run on the GL thread.
     */
    fun initializeStep5Finish() {
        this.effekseerManagerAdapter.InitializeStep4_Finish()

        // Check that the manager adapter has successfully initialized
        if (!this.effekseerManagerAdapter.GetHasSuccessfullyInitialized()) {
            throw RuntimeException("Failed to initialize the Effekseer manager instance of type ${this.getEffekseerManagerAdapterName()} and max sprite count of " + maxSpriteCount)
        }
    }

    /**
     * Calls all initialization steps in this one call, if the split of initialization steps is not needed for performance.
     */
    fun initializeAll() {
        this.effekseerManagerAdapter = this.createEffekseerManagerAdapter()
        this.effekseerManagerAdapter.InitializeAll()

        // Check that the manager adapter has successfully initialized
        if (!this.effekseerManagerAdapter.GetHasSuccessfullyInitialized()) {
            throw RuntimeException("Failed to initialize the Effekseer manager instance of type ${this.getEffekseerManagerAdapterName()} and max sprite count of " + maxSpriteCount)
        }
    }

    //endregion

    /**
     * Batches multiple pending effect transforms to a single JNI call.
     */
    private fun sendPendingEffectTransformsToEffekseer() {
        while (!this.pendingEffectsWithTransformChanges.isEmpty) {
            if (this.pendingEffectsWithTransformChanges.size >= MAX_BATCH_SIZE_PENDING_PARTICLE_TRANSFORM) {
                val particle1: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                val particle2: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                val particle3: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                val particle4: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                this.effekseerManagerAdapter.SetMatrixBatch4(
                    particle1.handle, particle1.getTransformValuesForEffekseer(),
                    particle2.handle, particle2.getTransformValuesForEffekseer(),
                    particle3.handle, particle3.getTransformValuesForEffekseer(),
                    particle4.handle, particle4.getTransformValuesForEffekseer()
                )
            } else if (this.pendingEffectsWithTransformChanges.size >= 2) {
                val particle1: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                val particle2: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                this.effekseerManagerAdapter.SetMatrixBatch2(
                    particle1.handle, particle1.getTransformValuesForEffekseer(),
                    particle2.handle, particle2.getTransformValuesForEffekseer()
                )
            } else {
                val particle: GDXseerParticleEffect = this.pendingEffectsWithTransformChanges.pop()
                this.effekseerManagerAdapter.SetMatrix(particle.handle, particle.getTransformValuesForEffekseer())
            }
        }
    }

    //endregion

    //region Protected Methods

    /**
     * @return True, if the given [Camera]'s [Camera.update] should be called during rendering, otherwise false. Default value is false.
     */
    protected open fun shouldCameraBeUpdatedInUpdate(): Boolean {
        return false
    }

    /**
     * Called at the start of the draw call.
     */
    protected open fun onPreDraw() {
        if (renderContext != null) {
            renderContext.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            renderContext.setDepthTest(GL20.GL_LESS, 0f, 1f)
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        }
    }

    /**
     * Called at the end of the draw call.
     */
    protected open fun onPostDraw() {
        // Do nothing
    }

    //endregion

    //region Internal Methods

    /**
     * Adds the given [GDXseerParticleEffect] to the list of added particle effects.
     */
    internal fun addParticleEffect(particleEffect: GDXseerParticleEffect) {
        this.addedParticleEffects.add(particleEffect)
    }

    /**
     * Removes the given [GDXseerParticleEffect] from the list of added particle effects.
     */
    internal fun removeParticleEffect(particleEffect: GDXseerParticleEffect) {
        this.addedParticleEffects.removeValue(particleEffect, true)
    }

    //endregion

    //region Public Methods

    /**
     * Updates all state needed for the current step in the simulation. Call this only once per frame.
     * If you only draw the simulation once per frame, you can call [draw] instead which calls this update method and then the Effekseer draw method.
     * @param delta The time in seconds since the last frame.
     */
    open fun update(delta: Float, camera: Camera) {
        this.timeInSeconds += delta

        // If the LibGDX camera state should be updated here
        if (shouldCameraBeUpdatedInUpdate()) {
            camera.update()
        }

        // Update and draw each particle effect
        for (particle in this.addedParticleEffects) {
            // Only update the particle effect if it is playing
            if (particle.isInPlayingState) {
                particle.update(delta)

                // Check if the current effect has just finished playing. If so, call its animation completed callback if available.
                if (!particle.isShown) {
                    particle.isInPlayingState = false
                    particle.onAnimationComplete?.onAnimationCompleted()
                }
                if (camera is PerspectiveCamera) {
                    if (particle.updateTransformMatrixIfQueued()) {
                        this.pendingEffectsWithTransformChanges.add(particle)

                        // Send if there are enough for batching
                        if (this.pendingEffectsWithTransformChanges.size >= MAX_BATCH_SIZE_PENDING_PARTICLE_TRANSFORM) {
                            sendPendingEffectTransformsToEffekseer()
                        }
                    }
                }
            }
        }
        if (!this.pendingEffectsWithTransformChanges.isEmpty) {
            sendPendingEffectTransformsToEffekseer()
        }

        // Get the camera width and height
        var cameraWidth = 0f
        var cameraHeight = 0f
        if (camera is OrthographicCamera) {
            val viewport = this.viewport
            if (viewport != null) {
                cameraWidth = viewport.worldWidth
                cameraHeight = viewport.worldHeight
                this.effekseerManagerAdapter.SetProjectionMatrix(camera.projection.`val`, camera.view.`val`, false, viewport.worldWidth, viewport.worldHeight)
            } else {
                cameraWidth = camera.viewportWidth
                cameraHeight = camera.viewportHeight
            }
        }

        // Update the manager core
        this.effekseerManagerAdapter.UpdateCombined(
            delta * SINGLE_FRAME_TIME_SECONDS_INV,
            this.timeInSeconds,
            camera.projection.`val`, camera.view.`val`, camera is PerspectiveCamera, cameraWidth, cameraHeight
        )
    }

    /**
     * This draws all added particle effects. This should only be called after a [.update] call and can be called
     * multiple times without affecting the update step.
     */
    open fun drawAfterUpdate() {
        onPreDraw()

        // Draw
        this.effekseerManagerAdapter.DrawCombined(this.drawParameter)
        onPostDraw()
    }

    /**
     * This calls the updates methods and then draws all added particle effects. Update [drawParameter] before this call to further affect how the particles are drawn.
     * If you want to draw multiple times do NOT use this method. Call [update] and then call [drawAfterUpdate] however many times draw calls are needed.
     * @param delta The time in seconds since the last frame.
     * @param camera The [Camera] instance used for rendering the particle effects.
     */
    open fun draw(delta: Float, camera: Camera) {
        // Update
        this.update(delta, camera)

        // Draw
        this.drawAfterUpdate()
    }

    //endregion

    //region Disposable

    override fun dispose() {
        // Delete all added effect instances. Use while loop to avoid nested iterator usages of LibGDX Array.
        while (!this.addedParticleEffects.isEmpty) {
            this.addedParticleEffects.first().dispose()
        }
        // Delete the manager core
        this.effekseerManagerAdapter.delete()
    }

    //endregion
}