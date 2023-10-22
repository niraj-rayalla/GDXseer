package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructRandomVector3D
import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructVector3D
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructRandomVector3D].
 */
@Suppress("unused")
class GDXseerRandomVector3D(override val source: InternalStructRandomVector3D = InternalStructRandomVector3D()): EffekseerManagedObject<InternalStructRandomVector3D>(2) {

    //region Managed Fields

    val min: EffekseerManagedField<InternalStructVector3D> = EffekseerManagedField(
        { this.source.min },
        { this.source.min = it },
        this)

    val max: EffekseerManagedField<InternalStructVector3D> = EffekseerManagedField(
        { this.source.max },
        { this.source.max = it },
        this)

    //endregion
}