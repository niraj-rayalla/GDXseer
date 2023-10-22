package io.github.niraj_rayalla.gdxseer.managedeffekseer.nodes

import io.github.niraj_rayalla.gdxseer.GDXseerParticleEffect
import io.github.niraj_rayalla.gdxseer.effekseer.EffectNodeSprite
import io.github.niraj_rayalla.gdxseer.managedeffekseer.data.GDXseerAllTypeColorParameter

/**
 * A [GDXseerEffectNode] for [EffectNodeSprite].
 */
@Suppress("unused")
class GDXseerEffectNodeSprite(source: EffectNodeSprite, particleEffect: GDXseerParticleEffect): GDXseerEffectNode<EffectNodeSprite>(source, particleEffect) {

    //region Managed Fields

    val spriteAllColor: GDXseerAllTypeColorParameter by lazy {
        GDXseerAllTypeColorParameter(this.source.spriteAllColor)
    }

    //endregion

}