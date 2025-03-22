package com.task.podcast.core.domain

/**
 * A generic use case that either returns a result or throws an exception.
 * @param Params the type of input parameter (use [None] for no parameters)
 * @param Type the type of output data.
 */
abstract class UseCase<in Params, out Type> {
    abstract suspend operator fun invoke(params: Params): Type
}

/**
 * Object to use as a parameter when no parameters are needed.
 */
object None
