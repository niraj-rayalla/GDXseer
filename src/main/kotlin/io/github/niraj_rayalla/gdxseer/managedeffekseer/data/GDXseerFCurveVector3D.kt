package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.FCurve
import io.github.niraj_rayalla.gdxseer.effekseer.FCurveVector3D
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing a [FCurveVector3D].
 */
class GDXseerFCurveVector3D(override val source: FCurveVector3D): EffekseerManagedObject<FCurveVector3D>(2) {

    //region Managed Fields

    val x: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.x },
        { this.source.x = it },
        this)

    val y: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.y },
        { this.source.y = it },
        this)

    val z: EffekseerManagedField<FCurve> = EffekseerManagedField(
        { this.source.z },
        { this.source.z = it },
        this)

    //endregion
}