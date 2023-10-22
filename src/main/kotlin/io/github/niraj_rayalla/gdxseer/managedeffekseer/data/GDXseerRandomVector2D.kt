package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructRandomVector2D
import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructVector2D
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructRandomVector2D].
 */
@Suppress("unused")
class GDXseerRandomVector2D(override val source: InternalStructRandomVector2D = InternalStructRandomVector2D()): EffekseerManagedObject<InternalStructRandomVector2D>(2) {

    //region Field Wrappers

    val min: EffekseerManagedField<InternalStructVector2D> = EffekseerManagedField(
        { this.source.min },
        { this.source.min = it },
        this)

    val max: EffekseerManagedField<InternalStructVector2D> = EffekseerManagedField(
        { this.source.max },
        { this.source.max = it },
        this)

    //endregion
}