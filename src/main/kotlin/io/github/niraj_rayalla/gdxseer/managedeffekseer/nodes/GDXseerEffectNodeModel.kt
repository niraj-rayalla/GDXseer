package io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes

import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeModel
import io.github.niraj_rayalla.gdxseer.managedeffekseer.data.GDXseerAllTypeColorParameter

/**
 * A [GDXseerEffectNode] for [EffectNodeModel].
 */
@Suppress("unused")
class GDXseerEffectNodeModel(source: EffectNodeModel, particleEffect: GDXseerParticleEffect): GDXseerEffectNode<EffectNodeModel>(source, particleEffect) {

    //region Managed Fields

    val allColor: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.allColor)
    }

    //endregion

}