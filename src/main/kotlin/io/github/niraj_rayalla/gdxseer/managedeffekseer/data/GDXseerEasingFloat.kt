package io.github.niraj_rayalla.gdxseer.managedeffekseer.data

import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructEasingFloat
import io.github.niraj_rayalla.gdxseer.effekseer.InternalStructRandomFloat
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedField
import io.github.niraj_rayalla.gdxseer.managedeffekseer.EffekseerManagedObject

/**
 * A [EffekseerManagedObject] for managing [InternalStructEasingFloat].
 */
@Suppress("unused")
class GDXseerEasingFloat(override val source: InternalStructEasingFloat): EffekseerManagedObject<InternalStructEasingFloat>(2) {

    //region Managed Fields

    val start: EffekseerManagedField<InternalStructRandomFloat> = EffekseerManagedField(
        { this.source.start },
        { this.source.start = it },
        this)

    val end: EffekseerManagedField<InternalStructRandomFloat> = EffekseerManagedField(
        { this.source.end },
        { this.source.end = it },
        this)

    //endregion
}