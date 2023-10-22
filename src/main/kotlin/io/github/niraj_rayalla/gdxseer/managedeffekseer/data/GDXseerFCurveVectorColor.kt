package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.FCurve
import io.github.niraj_rayalla.gdxseer.effekseer.FCurveVectorColor
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing a [FCurveVectorColor].
 */
class GDXseerFCurveVectorColor(override val source: FCurveVectorColor): EffekseerManagedObject<FCurveVectorColor>(2) {

    //region Managed Fields

    val r: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.r },
        { this.source.r = it },
        this)

    val g: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.g },
        { this.source.g = it },
        this)

    val b: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.b },
        { this.source.b = it },
        this)

    val a: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.a },
        { this.source.a = it },
        this)

    //endregion
}