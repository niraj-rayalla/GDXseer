package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructEasingVector3D
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructEasingVector3D].
 */
@Suppress("unused")
class GDXseerEasingVector3D(override val source: InternalStructEasingVector3D): EffekseerManagedObject<InternalStructEasingVector3D>(2) {

    //region Field Wrappers

    val start: EffekseerManagedField<GDXseerRandomVector3D> = EffekseerManagedField(
        { GDXseerRandomVector3D(this.source.start) },
        { this.source.start = it.source },
        this)

    val end: EffekseerManagedField<GDXseerRandomVector3D> = EffekseerManagedField(
        { GDXseerRandomVector3D(this.source.end) },
        { this.source.end = it.source },
        this)

    //endregion
}