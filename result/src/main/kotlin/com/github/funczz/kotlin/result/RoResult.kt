package com.github.funczz.kotlin.result

import java.io.Serializable
import java.util.*

/**
 * RoResult シールドクラス:
 * 実装クラスに Failure と Success を持つ。
 */
sealed class RoResult<T : Any> : Serializable {

    /**
     * Failure かどうかを返す。
     * @return Failure なら true、
     *         Success なら false。
     */
    val isFailure: Boolean
        get() = this is Failure

    /**
     * Success かどうかを返す。
     * @return Success なら true、
     *         Failure なら false。
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Success の値を返す。
     * @param fn 引数に Throwable を持ち、型 T を返す関数
     * @return Success なら Success 値、
     *         Failure なら 関数 fn の戻り値。
     */
    abstract fun getOrElse(fn: (Throwable) -> T): T

    /**
     * Success の値を返す。
     * @return Success なら Success の 値、
     *         Failure なら null 。
     */
    abstract fun getOrNull(): T?

    /**
     * Success の値を返す。
     * @return Success なら Success の 値、
     *         Failure なら <code>RoResultException</code> をスロー。
     * @throws RoResultException
     */
    abstract fun getOrThrow(): T

    /**
     * Success かつ関数 fn の戻り値が true なら Success を返し、
     * それ以外は Failure を返す。
     * @param fn 引数に 値 T を持ち、型 Boolean を返す関数
     * @return RoResult
     */
    abstract fun filter(fn: (T) -> Boolean): RoResult<T>

    /**
     * Success なら関数 fn の戻り値を持つ Success を返す
     * @param fn 引数に 値 T を持ち、型 U を返す関数
     * @return RoResult
     */
    abstract fun <U : Any> map(fn: (T) -> U): RoResult<U>

    /**
     * Success なら関数 fn の戻り値を持つ Success を返し、
     * Failure なら関数 or の戻り値を持つ Success を返す。
     * @param fn 引数に 値 T を持ち、型 U を返す関数
     * @param or 引数に Throwable を持ち、型 U を返す関数
     * @return RoResult
     */
    abstract fun <U : Any> mapOrElse(fn: (T) -> U, or: (Throwable) -> U): RoResult<U>

    /**
     * Success なら関数 fn の戻り値を返す。
     * Success から Failure の変換が可能。
     * @param fn 引数に 値 T を持ち、RoResult を返す関数
     * @return RoResult
     */
    abstract fun <U : Any> andThen(fn: (T) -> RoResult<U>): RoResult<U>

    /**
     * Failure なら関数 fn の戻り値を返す。
     * Failure から Success の変換が可能。
     * @param fn 引数に Throwable を持ち、RoResult を返す関数
     * @return RoResult
     */
    abstract fun orElse(fn: (Throwable) -> RoResult<T>): RoResult<T>

    /**
     * Success なら関数 fn の戻り値を返し、
     * Failure なら関数 or の戻り値を返す。
     * Success から Failure と Failure から Success の変換が可能。
     * @param fn 引数に 値 T を持ち、RoResult を返す関数
     * @param or 引数に Throwable を持ち、RoResult を返す関数
     * @return RoResult
     */
    abstract fun <U : Any> andThenOrElse(fn: (T) -> RoResult<U>, or: (Throwable) -> RoResult<U>): RoResult<U>

    /**
     * 自身と関数 fn の戻り値が Success 同士なら、
     * それらの値を持つ Pair を値とする Success 返す。
     * @param fn 引数に 値 T を持ち、RoResult を返す関数
     * @return RoResult
     */
    abstract fun <U : Any> zip(fn: (T) -> RoResult<U>): RoResult<Pair<T, U>>

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    abstract override fun toString(): String

    companion object {

        /**
         * Failure を返す。
         * @return RoResult
         */
        @JvmStatic
        fun <T : Any> failure(error: Throwable): RoResult<T> = Failure(error = error)

        /**
         * 関数 fn の戻り値を値とする Success を返す。
         * @param fn 型 T を返す関数
         * @return RoResult
         */
        @JvmStatic
        fun <T : Any> success(fn: () -> T): RoResult<T> = Success(value = fn())

        /**
         * 関数 fn が Throwable をスローしたなら Failure を返し、
         * それ以外は戻り値を値とする Success を返す。
         * @param fn 型 T を返す関数
         * @return RoResult
         */
        @JvmStatic
        fun <T : Any> tee(fn: () -> T): RoResult<T> = try {
            success { fn() }
        } catch (e: Throwable) {
            failure(error = e)
        }

    }

}

/**
 * Failure クラス: エラー値を持つ RoResult クラス
 */
class Failure<T : Any>(

    /**
     * Failure クラス が保持するエラー値
     */
    val error: Throwable

) : RoResult<T>() {

    override fun getOrElse(fn: (Throwable) -> T): T = fn(error)

    override fun getOrNull(): T? = null

    override fun getOrThrow(): T = throw FailureRoResultException(message = "getOrThrow", cause = error)

    override fun filter(fn: (T) -> Boolean): RoResult<T> = this

    override fun <U : Any> map(fn: (T) -> U): RoResult<U> = failure(error = error)

    override fun <U : Any> mapOrElse(fn: (T) -> U, or: (Throwable) -> U): RoResult<U> = tee { or(error) }

    override fun <U : Any> andThen(fn: (T) -> RoResult<U>): RoResult<U> = failure(error = error)

    override fun orElse(fn: (Throwable) -> RoResult<T>): RoResult<T> = try {
        fn(error)
    } catch (e: Throwable) {
        failure(error = e)
    }

    override fun <U : Any> andThenOrElse(fn: (T) -> RoResult<U>, or: (Throwable) -> RoResult<U>): RoResult<U> = try {
        or(error)
    } catch (e: Throwable) {
        failure(error = e)
    }

    override fun <U : Any> zip(fn: (T) -> RoResult<U>): RoResult<Pair<T, U>> = failure(error = error)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Failure<*> -> true
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(javaClass)
    }

    override fun toString(): String {
        return String.format("Failure(%s)".format(error))
    }

    companion object {
        private const val SerialVersionUID: Long = 1L
    }

}

/**
 * Success クラス: 値を持つ RoResult クラス
 */
class Success<T : Any>(

    /**
     * Success クラス が保持する値
     */
    val value: T

) : RoResult<T>() {

    override fun getOrElse(fn: (Throwable) -> T): T = value

    override fun getOrNull(): T = value

    override fun getOrThrow(): T = value

    override fun filter(fn: (T) -> Boolean): RoResult<T> = when (fn(value)) {
        true -> this
        else -> failure(error = SuccessRoResultException("filter"))
    }

    override fun <U : Any> map(fn: (T) -> U): RoResult<U> = success { fn(value) }

    override fun <U : Any> mapOrElse(fn: (T) -> U, or: (Throwable) -> U): RoResult<U> = success { fn(value) }

    override fun <U : Any> andThen(fn: (T) -> RoResult<U>): RoResult<U> = try {
        fn(value)
    } catch (e: Throwable) {
        failure(error = e)
    }

    override fun orElse(fn: (Throwable) -> RoResult<T>): RoResult<T> = this

    override fun <U : Any> andThenOrElse(fn: (T) -> RoResult<U>, or: (Throwable) -> RoResult<U>): RoResult<U> = try {
        fn(value)
    } catch (e: Throwable) {
        failure(error = e)
    }

    override fun <U : Any> zip(fn: (T) -> RoResult<U>): RoResult<Pair<T, U>> = try {
        when (val result = fn(value)) {
            is Failure<U> -> failure(error = result.error)
            is Success<U> -> success { Pair(value, result.getOrThrow()) }
        }
    } catch (e: Throwable) {
        failure(error = e)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return when (other) {
            is Success<*> -> value == other.value
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(javaClass, value)
    }

    override fun toString(): String {
        return String.format("Success(%s)".format(value))
    }

    companion object {
        private const val SerialVersionUID: Long = 1L
    }
}
