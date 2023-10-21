package io.github.niraj_rayalla.gdxseer.managedeffekseer

import com.badlogic.gdx.utils.Disposable

/**
 * TLDR:
 * Basically use the properties and methods in the implementations of this class, not [source], if it's available here, but not all functionality is wrapped here.
 * So also look at the methods in [source] for the full functionality, because only the methods in [Source] that create new objects on the JVM side will be implemented/managed here.
 *
 * Read further for more info:
 *
 * This class is used to manage an object of a generated Effekseer Java class (the ones in package [io.github.niraj_rayalla.gdxseer.effekseer]
 * and [io.github.niraj_rayalla.gdxseer.adapter_effekseer]), as specified by [Source], so that calling methods in those classes directly can be avoided if
 * that call would have created a new instance of some object.
 *
 * A class that extends this class can have the following that will help with the reduction of new object creation:
 * 1. [EffekseerManagedField]s that manage fields in [Source] so the value of the field will be cached in this class.
 * 2. Other properties or methods that will "extend" existing [Source] methods by copying what they do while allowing an existing object(s) to be passed in
 * that will get its data set to be the same as the object that would have been created.
 *
 * This also extends [Disposable] so that any extra native objects being cached can be freed when [dispose] is called. Call [dispose] when this object is no longer needed.
 */
abstract class EffekseerManagedObject<Source: Any>: Disposable {
    /**
     * The generated Effekseer Java class that this [EffekseerManagedObject] manages.
     */
    abstract val source: Source
}