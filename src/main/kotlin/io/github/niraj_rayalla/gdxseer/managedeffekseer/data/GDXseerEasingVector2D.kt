package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructEasingVector2D
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructEasingVector2D].
 */
@Suppress("unused")
class GDXseerEasingVector2D(override val source: InternalStructEasingVector2D): EffekseerManagedObject<InternalStructEasingVector2D>(2) {

    //region Managed Fields

    val start: EffekseerManagedField<GDXseerRandomVector2D> = EffekseerManagedField(
        { GDXseerRandomVector2D(this.source.start) },
        { this.source.start = it.source },
        this)

    val end: EffekseerManagedField<GDXseerRandomVector2D> = EffekseerManagedField(
        { GDXseerRandomVector2D(this.source.end) },
        { this.source.end = it.source },
        this)

    //endregion
}