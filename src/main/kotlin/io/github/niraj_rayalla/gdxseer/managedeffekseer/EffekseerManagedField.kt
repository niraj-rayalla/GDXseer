package io.github.niraj_rayalla.gdxseer.managedeffekseer

import io.github.niraj_rayalla.gdxseer.utils.Function

/**
 * Used to wrap a field in the generated Effekseer Java classes (the ones in package [io.github.niraj_rayalla.gdxseer.effekseer] and [io.github.niraj_rayalla.gdxseer.adapter_effekseer])
 * so that calling methods in those classes directly can be avoided if that call would have created a new instance of some object.
 * Instead, calling [value] will initially cache the value and any future sets can be done through this class instead, using [value], so that this object will
 * contain a reference to the actual data object at all times.
 * Requires a getter and setter that will get/set the value in Effekseer.
 */
class EffekseerManagedField<T: Any?>(
    private val getter: Function<Unit, T>,
    private val setter: Function<T, Unit>
) {

    //region State

    /**
     * @return True, if any source value from Effekseer has been set in this managed object, otherwise false.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isValueSet = false
        private set

    /**
     * The value of the field.
     */
    private var valueInstance: T? = null

    //endregion

    //region Properties

    /**
     * Gets/Sets the value for the wrapped field from/to Effekseer.
     */
    var value: T
        get() {
            if (!this.isValueSet) {
                this.valueInstance = this.getter.apply(Unit)
            }
            return this.valueInstance!!
        }
        set(value) {
            this.valueInstance = value
            this.isValueSet = true
            this.setter.apply(value)
        }

    //endregion

}