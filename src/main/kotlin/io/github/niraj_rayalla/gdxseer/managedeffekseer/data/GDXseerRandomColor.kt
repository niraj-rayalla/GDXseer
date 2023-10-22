package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.Color
import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructRandomColor
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructRandomColor].
 */
@Suppress("unused")
class GDXseerRandomColor(override val source: InternalStructRandomColor = InternalStructRandomColor()): EffekseerManagedObject<InternalStructRandomColor>(2) {

    //region Managed Fields

    val min: EffekseerManagedField<Color> = EffekseerManagedField(
        { this.source.min },
        { this.source.min = it },
        this)

    val max: EffekseerManagedField<Color> = EffekseerManagedField(
        { this.source.max },
        { this.source.max = it },
        this)

    //endregion
}