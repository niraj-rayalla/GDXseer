package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructEasingColor
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructEasingColor].
 */
@Suppress("unused")
class GDXseerEasingColor(override val source: InternalStructEasingColor): EffekseerManagedObject<InternalStructEasingColor>(2) {

    //region Managed Fields

    val start: EffekseerManagedField<GDXseerRandomColor> = EffekseerManagedField(
        { GDXseerRandomColor(this.source.start) },
        { this.source.start = it.source },
        this)

    val end: EffekseerManagedField<GDXseerRandomColor> = EffekseerManagedField(
        { GDXseerRandomColor(this.source.end) },
        { this.source.end = it.source },
        this)

    //endregion
}