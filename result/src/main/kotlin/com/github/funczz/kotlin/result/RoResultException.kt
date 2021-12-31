package com.github.funczz.kotlin.result

open class RoResultException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)

class FailureRoResultException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
) : RoResultException(message = message, cause = cause)

class SuccessRoResultException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
) : RoResultException(message = message, cause = cause)
