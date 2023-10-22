package io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes

import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeRing
import io.github.niraj_rayalla.gdxseer.managedeffekseer.data.GDXseerAllTypeColorParameter

/**
 * A [GDXseerEffectNode] for [EffectNodeRing].
 */
@Suppress("unused")
class GDXseerEffectNodeRing(source: EffectNodeRing, particleEffect: GDXseerParticleEffect): GDXseerEffectNode<EffectNodeRing>(source, particleEffect) {

    //region Managed Fields

    val outerColor: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.outerColor)
    }

    val centerColor: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.centerColor)
    }

    val innerColor: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.innerColor)
    }

    //endregion

}