package io.github.niraj_rayalla.gdxseer.example

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.math.RandomXS128
import com.badlogic.gdx.math.Vector3
import io.github.niraj_rayalla.gdxseer.GDXseer
import io.github.niraj_rayalla.gdxseer.GDXseerManager
import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.AllTypeColorParameter
import io.github.niraj_rayalla.gdxseer.example.BasicExampleApplicationAdapter.Companion.COLOR_CHANGE_INTERVAL_SECONDS
import io.github.niraj_rayalla.gdxseer.loader.EffekseerIsMipMapEnabledDecider
import io.github.niraj_rayalla.gdxseer.loader.EffekseerParticleAssetLoader
import io.github.niraj_rayalla.gdxseer.loader.EffekseerParticleSubAssetLoader
import io.github.niraj_rayalla.gdxseer.managedeffekseer.data.GDXseerAllTypeColorParameter
import io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes.GDXseerEffectNodeRing
import io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes.GDXseerEffectNodeSprite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * The [ApplicationAdapter] used for the Basic example application.
 * Loads and shows the Suzuki01 magma effect and changes the colors of some of the nodes in the effect every [COLOR_CHANGE_INTERVAL_SECONDS] seconds.
 */
class BasicExampleApplicationAdapter(private val gdxseerManagerCreator: ()->GDXseerManager<*>): ApplicationAdapter() {

    companion object {
        private const val COLOR_CHANGE_INTERVAL_SECONDS = 2f
    }

    //region State

    private val random = RandomXS128()

    private lateinit var assetManger: ExampleAssetManager

    private lateinit var camera: PerspectiveCamera
    private lateinit var gdxseerManger: GDXseerManager<*>

    private lateinit var particleEffect: GDXseerParticleEffect
    private lateinit var particleEffectFlareMiniNode: GDXseerEffectNodeRing
    private lateinit var particleEffectFlareNode: GDXseerEffectNodeRing
    private lateinit var particleEffectNeedleNode: GDXseerEffectNodeSprite
    private lateinit var particleEffectNeedle2Node: GDXseerEffectNodeSprite

    private var colorChangeTimeLeft = COLOR_CHANGE_INTERVAL_SECONDS

    //endregion

    //region Overrides

    override fun create() {
        super.create()

        // Create the asset manager
        this.assetManger = ExampleAssetManager().apply {
            setLoader(EffekseerParticleSubAssetLoader.Companion.Result::class.java, null, EffekseerParticleSubAssetLoader(this.fileHandleResolver))
            setLoader(EffekseerParticleAssetLoader.Companion.Result::class.java, null, EffekseerParticleAssetLoader(this.fileHandleResolver, object: EffekseerIsMipMapEnabledDecider {
                override fun isMipMapEnabledForTextureFile(textureFileHandle: FileHandle): Boolean {
                    return true
                }
            }))
        }

        // Initialize GDXseer
        GDXseer.init()

        // Create the camera
        this.camera = PerspectiveCamera(67f, 0f, 0f).apply {
            position.set(2f, 2f, -2f)
            lookAt(Vector3.Zero)
        }

        // Create the GDXseer manager used to manage Effekseer effects
        val shouldInitializeGDXManagerInOneStep = false
        if (shouldInitializeGDXManagerInOneStep) {
            val time = System.nanoTime()
            this.gdxseerManger = this.gdxseerManagerCreator.invoke().apply { initializeAll() }
            println("GDXseer manger instance complete creation time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")
        }
        else {
            var time: Long

            // Make the GDXseer manger instance
            this.gdxseerManger = runBlocking {
                this.async(Dispatchers.Default) {
                    time = System.nanoTime()
                    val result = gdxseerManagerCreator.invoke()
                    println("GDXseer manger instance creation time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")
                    result
                }.await()
            }

            // Step1 initialization
            time = System.nanoTime()
            runBlocking {
                this.launch(Dispatchers.Default) {
                    time = System.nanoTime()
                    gdxseerManger.initializeStep1CreateManagerAdapter()
                    println("Step1 initialization time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")
                }.join()
            }

            // Step2 initialization
            time = System.nanoTime()
            this.gdxseerManger.initializeStep2CreateRenderer()
            println("Step2 initialization time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")

            // Step3 initialization
            time = System.nanoTime()
            this.gdxseerManger.initializeStep3CreateSubRenderers()
            println("Step3 initialization time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")

            // Step4 initialization
            time = System.nanoTime()
            runBlocking {
                this.launch(Dispatchers.Default) {
                    time = System.nanoTime()
                    gdxseerManger.initializeStep4CreateLoaders()
                    println("Step4 initialization time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")
                }.join()
            }

            // Step5 initialization
            time = System.nanoTime()
            runBlocking {
                this.launch(Dispatchers.Default) {
                    time = System.nanoTime()
                    gdxseerManger.initializeStep5Finish()
                    println("Step5 initialization time: ${(System.nanoTime() - time).toDouble() / 1_000_000.0} ms")
                }.join()
            }
        }

        // Load the example Effekseer particle effect immediately
        this.particleEffect = GDXseerParticleEffect(this.gdxseerManger)
        this.particleEffect.syncLoad(this.assetManger, Gdx.files.getFileHandle("magma_effect.efk", Files.FileType.Internal))
        this.assetManger.finishLoading()
        // Start playing the animation of the particle effect now
        this.particleEffect.play()

        // Get the particle effect nodes for dynamic editing
        this.particleEffectFlareMiniNode = this.particleEffect.rootNode.getChild(0) as GDXseerEffectNodeRing
        this.particleEffectFlareNode = this.particleEffect.rootNode.getChild(2) as GDXseerEffectNodeRing
        this.particleEffectNeedleNode = this.particleEffect.rootNode.getChild(6) as GDXseerEffectNodeSprite
        this.particleEffectNeedle2Node = this.particleEffect.rootNode.getChild(7) as GDXseerEffectNodeSprite
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        // Update the camera to match the viewport size
        this.camera.apply {
            viewportWidth = width.toFloat()
            viewportHeight = height.toFloat()
            update()
        }
    }

    override fun render() {
        super.render()

        val deltaTime = Gdx.graphics.deltaTime

        // Clear depth and color buffer for next frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        // Change color every interval
        this.colorChangeTimeLeft -= deltaTime
        if (this.colorChangeTimeLeft <= 0f) {
            this.colorChangeTimeLeft = COLOR_CHANGE_INTERVAL_SECONDS
            this.pickRandomColorForParticleEffect()
        }

        // Render the Effekseer effects
        this.gdxseerManger.draw(deltaTime, this.camera)
    }

    override fun dispose() {
        super.dispose()

        this.particleEffect.dispose()
        this.gdxseerManger.dispose()
    }

    //endregion

    //region Private Methods

    private fun GDXseerAllTypeColorParameter.setFixedColor(effekseerColor: io.github.niraj_rayalla.gdxseer.effekseer.Color) {
        source.type = AllTypeColorParameter.Fixed
        fixed.value = effekseerColor
    }

    /**
     * Pick a new color to use for some of the nodes in the particle effect.
     */
    private fun pickRandomColorForParticleEffect() {
        val r = (this.random.nextFloat() * 255f).toInt().toShort()
        val g = (this.random.nextFloat() * 255f).toInt().toShort()
        val b = (this.random.nextFloat() * 255f).toInt().toShort()

        val ringEffekseerOuterColor = io.github.niraj_rayalla.gdxseer.effekseer.Color(r, g, b, 0.toShort())
        val ringEffekseerCenterColor = io.github.niraj_rayalla.gdxseer.effekseer.Color(r, g, b, 100.toShort())
        this.particleEffectFlareMiniNode.apply {
            outerColor.setFixedColor(ringEffekseerOuterColor)
            centerColor.setFixedColor(ringEffekseerCenterColor)
        }
        this.particleEffectFlareNode.apply {
            outerColor.setFixedColor(ringEffekseerOuterColor)
            centerColor.setFixedColor(ringEffekseerCenterColor)
        }

        val spriteEffekseerColor = io.github.niraj_rayalla.gdxseer.effekseer.Color(r, g, b, 255.toShort())
        this.particleEffectNeedleNode.spriteAllColor.setFixedColor(spriteEffekseerColor)
        this.particleEffectNeedle2Node.spriteAllColor.setFixedColor(spriteEffekseerColor)
    }

    //endregion
}