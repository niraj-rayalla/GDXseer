package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.ArrayGradientAlphas
import io.github.niraj_rayalla.gdxseer.effekseer.ArrayGradientColors
import io.github.niraj_rayalla.gdxseer.effekseer.Gradient
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [Gradient].
 */
@Suppress("unused")
class GDXseerGradient(override val source: Gradient): EffekseerManagedObject<Gradient>(2) {

    //region Managed Fields

    val colors: EffekseerManagedField<ArrayGradientColors> = EffekseerManagedField(
        { this.source.colors },
        { this.source.colors = it },
        this)

    val alpha: EffekseerManagedField<ArrayGradientAlphas> = EffekseerManagedField(
        { this.source.alphas },
        { this.source.alphas = it },
        this)

    //endregion
}