package io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes

import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeTrack
import io.github.niraj_rayalla.gdxseer.managedeffekseer.data.GDXseerAllTypeColorParameter

/**
 * A [GDXseerEffectNode] for [EffectNodeTrack].
 */
@Suppress("unused")
class GDXseerEffectNodeTrack(source: EffectNodeTrack, particleEffect: GDXseerParticleEffect): GDXseerEffectNode<EffectNodeTrack>(source, particleEffect) {

    //region Managed Fields

    val trackColorLeft: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.trackColorLeft)
    }

    val trackColorCenter: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.trackColorCenter)
    }

    val trackColorRight: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.trackColorRight)
    }

    val trackColorLeftMiddle: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.trackColorLeftMiddle)
    }

    val trackColorCenterMiddle: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.trackColorCenterMiddle)
    }

    val trackColorRightMiddle: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.trackColorRightMiddle)
    }

    //endregion

}