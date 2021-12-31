package com.github.funczz.kotlin.result

/**
 * 入れ子になっている RoResult を一段階アンラップする
 * @return <code>RoResult<RoResult<T>></code> を アンラップした <code>RoResult<T></code>
 */
fun <T : Any> RoResult<RoResult<T>>.flatten(): RoResult<T> = when (this) {
    is Failure<RoResult<T>> -> andThen { it }
    is Success<RoResult<T>> -> value
}

/**
 * Failure なら関数 failure を適用し、
 * Success なら関数 success を適用する。
 * @param failure 引数として Throwable を持ち、戻り値のない関数
 * @param success 引数として型 T を持ち、戻り値のない関数
 */
inline fun <T : Any> RoResult<T>.match(failure: (Throwable) -> Unit = {}, success: (T) -> Unit) = when (this) {
    is Failure<T> -> failure(this.error)
    is Success<T> -> success(this.value)
}

/**
 * Failure なら関数 failure を適用し、
 * Success なら関数 success を適用する。
 * @param failure 引数として Throwable を持ち、型 R を返す関数
 * @param success 引数として型 T を持ち、型 R を返す関数
 * @return R
 */
inline fun <T : Any, R : Any> RoResult<T>.fold(failure: (Throwable) -> R, success: (T) -> R): R = when (this) {
    is Failure<T> -> failure(this.error)
    is Success<T> -> success(this.value)
}

/**
 * Result を RoResult に変換する。
 * @return RoResult
 */
fun <T : Any> Result<T>.toRoResult(): RoResult<T> = this.fold(
    onFailure = { RoResult.failure(it) },
    onSuccess = { RoResult.tee { it } }
)

/**
 * RoResult を Result に変換する。
 * @return Result
 */
fun <T : Any> RoResult<T>.toResult(): Result<T> = this.fold(
    failure = { Result.failure(it) },
    success = { Result.success(it) }
)