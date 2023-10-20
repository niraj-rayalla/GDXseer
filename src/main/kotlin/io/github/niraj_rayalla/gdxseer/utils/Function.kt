package io.github.niraj_rayalla.gdxseer.utils

/**
 * Copied from the JDK.
 *
 * Represents a function that accepts one argument and produces a result.
 *
 *
 * This is a [functional interface](package-summary.html)
 * whose functional method is [.apply].
 *
 * @param T the type of the input to the function
 * @param R the type of the result of the function
 *
 * @since 1.8
 */
fun interface Function<T, R> {
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    fun apply(t: T): R

    /**
     * Returns a composed function that first applies the `before`
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param V the type of input to the `before` function, and to the
     * composed function
     * @param before the function to apply before this function is applied
     * @return a composed function that first applies the `before`
     * function and then applies this function
     * @throws NullPointerException if before is null
     *
     * @see [andThen]
     */
    fun <V> compose(before: Function<in V, out T>?): Function<V, R>? {
        if (before == null) throw NullPointerException()
        return Function { v: V -> apply(before.apply(v)) }
    }

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the `after` function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param V the type of output of the `after` function, and of the
     * composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the `after` function
     * @throws NullPointerException if after is null
     *
     * @see [compose]
     */
    fun <V> andThen(after: Function<in R, out V>?): Function<T, V>? {
        if (after == null) throw NullPointerException()
        return Function { t: T -> after.apply(apply(t)) }
    }

    companion object {
        /**
         * Returns a function that always returns its input argument.
         *
         * @param T the type of the input and output objects to the function
         * @return a function that always returns its input argument
         */
        fun <T> identity(): Function<T, T>? {
            return Function { t: T -> t }
        }
    }
}