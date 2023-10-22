package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.FCurve
import io.github.niraj_rayalla.gdxseer.effekseer.FCurveVector2D
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing a [FCurveVector2D].
 */
class GDXseerFCurveVector2D(override val source: FCurveVector2D): EffekseerManagedObject<FCurveVector2D>(2) {

    //region Managed Fields

    val x: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.x },
        { this.source.x = it },
        this)

    val y: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.y },
        { this.source.y = it },
        this)

    //endregion
}