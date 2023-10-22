package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.AllTypeColorParameter
import io.github.niraj_rayalla.gdxseer.effekseer.Color
import io.github.niraj_rayalla.gdxseer.effekseer.FCurveVectorColor
import io.github.niraj_rayalla.gdxseer.effekseer.Gradient
import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructEasingColor
import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructRandomColor
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing a [AllTypeColorParameter].
 */
@Suppress("unused")
class GDXseerAllTypeColorParameter(override val source: AllTypeColorParameter): EffekseerManagedObject<AllTypeColorParameter>(2) {

    //region Managed Fields

    val fixed: EffekseerManagedField<Color> = EffekseerManagedField(
        { this.source.fixed },
        { this.source.fixed = it },
        this)

    val random: EffekseerManagedField<InternalStructRandomColor> = EffekseerManagedField(
        { this.source.random },
        { this.source.random = it },
        this)

    val easing: EffekseerManagedField<InternalStructEasingColor> = EffekseerManagedField(
        { this.source.easing },
        { this.source.easing = it },
        this)

    val fCurveRGBA: EffekseerManagedField<FCurveVectorColor> = EffekseerManagedField(
        { this.source.fCurveRGBA },
        { this.source.fCurveRGBA = it },
        this)

    val gradient: Gradient by lazy {
        this.source.gradient
    }

    //endregion
}