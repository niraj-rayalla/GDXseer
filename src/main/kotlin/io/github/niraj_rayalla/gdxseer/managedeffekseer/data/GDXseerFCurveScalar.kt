package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.FCurve
import io.github.niraj_rayalla.gdxseer.effekseer.FCurveScalar
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing a [FCurveScalar].
 */
class GDXseerFCurveScalar(override val source: FCurveScalar): EffekseerManagedObject<FCurveScalar>(2) {

    //region Managed Fields

    val s: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.s },
        { this.source.s = it },
        this)

    //endregion
}