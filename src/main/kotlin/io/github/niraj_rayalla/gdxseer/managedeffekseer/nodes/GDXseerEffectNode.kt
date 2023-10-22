package io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes

import com.badlogic.gdx.utils.Array
import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.DynamicFactorParameter
import io.github.niraj_rayalla.gdxseer.effekseer.EffectBasicRenderParameter
import io.github.niraj_rayalla.gdxseer.effekseer.EffectModelParameter
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeImplemented
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeType
import io.github.niraj_rayalla.gdxseer.effekseer.FalloffParameter
import io.github.niraj_rayalla.gdxseer.effekseer.KillRulesParameter
import io.github.niraj_rayalla.gdxseer.effekseer.LocalForceFieldParameter
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterAlphaCutoff
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterCommonValues
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterDepthValues
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterGenerationLocation
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterLODs
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterRendererCommon
import io.github.niraj_rayalla.gdxseer.effekseer.ParameterSound
import io.github.niraj_rayalla.gdxseer.effekseer.RotationParameter
import io.github.niraj_rayalla.gdxseer.effekseer.ScalingParameter
import io.github.niraj_rayalla.gdxseer.effekseer.SteeringBehaviorParameter
import io.github.niraj_rayalla.gdxseer.effekseer.TranslationParameter
import io.github.niraj_rayalla.gdxseer.effekseer.TriggerParameter
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing a sub-class of [EffectNodeImplemented].
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
open class GDXseerEffectNode<N: EffectNodeImplemented>(
    override val source: N,
    /**
     * The [GDXseerParticleEffect] this effect node belongs to.
     */
    protected val particleEffect: GDXseerParticleEffect
): EffekseerManagedObject<N>(11) {

    //region State

    /**
     * All the children of this [GDXseerEffectNode] as a list of [GDXseerEffectNode]s.
     */
    private val children: Array<GDXseerEffectNode<*>> = Array(0)

    //endregion

    //region Managed Fields

    val basicRenderParameter: EffekseerManagedField<EffectBasicRenderParameter> by lazy {
        EffekseerManagedField(
            { this.source.basicRenderParameter },
            { this.source.basicRenderParameter = it },
            this)
    }

    val commonValues: EffekseerManagedField<ParameterCommonValues> by lazy {
        EffekseerManagedField(
            { this.source.commonValues },
            { this.source.commonValues = it },
            this)
    }

    val steeringBehavior: EffekseerManagedField<SteeringBehaviorParameter> by lazy {
        EffekseerManagedField(
            { this.source.steeringBehaviorParam },
            { this.source.steeringBehaviorParam = it },
            this)
    }

    val triggerParam: EffekseerManagedField<TriggerParameter> by lazy {
        EffekseerManagedField(
            { this.source.triggerParam },
            { this.source.triggerParam = it },
            this)
    }

    val lodsParam: EffekseerManagedField<ParameterLODs> by lazy {
        EffekseerManagedField(
            { this.source.loDsParam },
            { this.source.loDsParam = it },
            this)
    }

    val killParam: EffekseerManagedField<KillRulesParameter> by lazy {
        EffekseerManagedField(
            { this.source.killParam },
            { this.source.killParam = it },
            this)
    }

    val generationLocation: EffekseerManagedField<ParameterGenerationLocation> by lazy {
        EffekseerManagedField(
            { this.source.generationLocation },
            { this.source.generationLocation = it },
            this)
    }

    val depthValues: EffekseerManagedField<ParameterDepthValues> by lazy {
        EffekseerManagedField(
            { this.source.depthValues },
            { this.source.depthValues = it },
            this)
    }

    val rendererCommon: EffekseerManagedField<ParameterRendererCommon> by lazy {
        EffekseerManagedField(
            { this.source.rendererCommon },
            { this.source.rendererCommon = it },
            this)
    }

    val falloffParam: EffekseerManagedField<FalloffParameter> by lazy {
        EffekseerManagedField(
            { this.source.falloffParam },
            { this.source.falloffParam = it },
            this)
    }

    val sound: EffekseerManagedField<ParameterSound> by lazy {
        EffekseerManagedField(
            { this.source.sound },
            { this.source.sound = it },
            this)
    }

    val dynamicFactor: EffekseerManagedField<DynamicFactorParameter> by lazy {
        EffekseerManagedField(
            { this.source.dynamicFactor },
            { this.source.dynamicFactor = it },
            this)
    }

    val modelParameter: EffectModelParameter by lazy {
        this.source.effectModelParameter
    }

    val translationParam: TranslationParameter by lazy {
        this.source.translationParam
    }

    val localForceField: LocalForceFieldParameter by lazy {
        this.source.localForceField
    }

    val rotationParam: RotationParameter by lazy {
        this.source.rotationParam
    }

    val scalingParam: ScalingParameter by lazy {
        this.source.scalingParam
    }

    val alphaCutoff: ParameterAlphaCutoff by lazy {
        this.source.alphaCutoff
    }

    //endregion

    //region Private Methods

    private fun ensureChildrenArraySize() {
        val childCount: Int = this.source.childrenCount

        // Make sure the children array is sized to fit all children
        if (childCount > 0 && this.children.size < childCount) {
            this.children.ensureCapacity(childCount)

            // Fill all empty slots with nulls
            var currentIndexToFill = this.children.size
            while (currentIndexToFill < childCount) {
                this.children.add(null)
                currentIndexToFill += 1
            }
        }
    }

    //endregion
    
    //region Public Methods

    /**
     * @return The [GDXseerEffectNode] at the given index.
     */
    fun getChild(index: Int): GDXseerEffectNode<*> {
        // First make sure the cached children array is sized properly (should only be needed the first time this gets called
        // after this node is loaded from an effect).
        ensureChildrenArraySize()

        // Get if already cached.
        var cachedNode: GDXseerEffectNode<*>? = this.children[index]

        // If it wasn't cached get the node from Effekseer and cache it
        if (cachedNode == null) {
            val coreChildNode: EffectNodeImplemented = this.source.getChild(index)

            // Check for specific node type
            cachedNode = when (coreChildNode.type) {
                EffectNodeType.Sprite -> GDXseerEffectNodeSprite(this.source.getChildAsSprite(index), this.particleEffect)
                EffectNodeType.Ribbon -> GDXseerEffectNodeRibbon(this.source.getChildAsRibbon(index), this.particleEffect)
                EffectNodeType.Track -> GDXseerEffectNodeTrack(this.source.getChildAsTrack(index), this.particleEffect)
                EffectNodeType.Ring -> GDXseerEffectNodeRing(this.source.getChildAsRing(index), this.particleEffect)
                EffectNodeType.Model -> GDXseerEffectNodeModel(this.source.getChildAsModel(index), this.particleEffect)
                else -> GDXseerEffectNode<EffectNodeImplemented>(this.source.getChild(index), this.particleEffect)
            }
            children[index] = cachedNode
        }
        return cachedNode
    }
    
    //endregion
}