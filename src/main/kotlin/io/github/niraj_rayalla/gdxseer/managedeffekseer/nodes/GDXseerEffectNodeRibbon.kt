package io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes

import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeRibbon
import io.github.niraj_rayalla.gdxseer.managedeffekseer.data.GDXseerAllTypeColorParameter

/**
 * A [GDXseerEffectNode] for [EffectNodeRibbon].
 */
@Suppress("unused")
class GDXseerEffectNodeRibbon(source: EffectNodeRibbon, particleEffect: GDXseerParticleEffect): GDXseerEffectNode<EffectNodeRibbon>(source, particleEffect) {

    //region Managed Fields

    val ribbonAllColor: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.ribbonAllColor)
    }

    //endregion

}