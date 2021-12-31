package com.github.funczz.kotlin.result

import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class RoResultTest : StringSpec(), ISerializableUtil {

    init {

        "RoResult.failure" {
            val expected = true
            val actual = RoResult.failure<String>(Exception("test"))

            actual.isFailure shouldBe expected
        }

        "RoResult.success" {
            val expected = true
            val actual = RoResult.success { "hello world." }

            actual.isSuccess shouldBe expected
        }

        "RoResult.tee -> Failure" {
            val expected = true
            val actual = RoResult.tee<String> { throw Exception("test") }

            actual.isFailure shouldBe expected
        }

        "RoResult.tee -> Success" {
            val expected = true
            val actual = RoResult.tee { "hello world." }

            actual.isSuccess shouldBe expected
        }

        "Success(Success): flatten" {
            val expected = "hello world."
            val result = RoResult.success { RoResult.success { expected } }
            val actual = result.flatten()

            actual.getOrThrow() shouldBe expected
        }

        "Success(Failure): flatten" {
            val result: RoResult<RoResult<String>> =
                RoResult.success { RoResult.tee<String> { throw Exception("test") } }
            val actual: RoResult<String> = result.flatten()

            actual.isFailure shouldBe true
        }

        "Failure: flatten" {
            val result: RoResult<RoResult<String>> = RoResult.failure<RoResult<String>>(error = Exception("test"))
            val actual: RoResult<String> = result.flatten()

            actual.isFailure shouldBe true
        }

        "Failure: toResult" {
            val expected = "hello world."
            val result = RoResult.failure<String>(error = Exception(expected))
            val actual = result
                .toResult()
                .exceptionOrNull()!!
                .message

            actual shouldBe expected
        }

        "Failure: Result.toRoResult" {
            val expected = "hello world."
            val result = Result.failure<String>(Exception(expected))
            val actual = result
                .toRoResult()
                .fold(
                    failure = { it.message ?: "" },
                    success = { it }
                )

            actual shouldBe expected
        }

        "Success: toResult" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            val actual = result
                .toResult()
                .getOrThrow()

            actual shouldBe expected
        }

        "Success: Result.toRoResult" {
            val expected = "hello world."
            val result = Result.success(expected)
            val actual = result
                .toRoResult()
                .getOrThrow()

            actual shouldBe expected
        }
    }

}