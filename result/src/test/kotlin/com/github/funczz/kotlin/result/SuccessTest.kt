package io.kotlintest.provided.com.github.funczz.kotlin.result

import com.github.funczz.kotlin.result.RoResult
import com.github.funczz.kotlin.result.Success
import com.github.funczz.kotlin.result.fold
import com.github.funczz.kotlin.result.match
import io.kotlintest.matchers.types.shouldNotBeSameInstanceAs
import io.kotlintest.provided.ISerializableUtil
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

internal class SuccessTest : StringSpec(), ISerializableUtil {

    init {

        "Success: Serialization" {
            val expected = "hello world."
            val origin = RoResult.success { expected }
            val actual = origin.dump().load() as Success<*>

            actual.javaClass shouldBe origin.javaClass
            actual shouldNotBeSameInstanceAs origin
            actual.isSuccess shouldBe true
            actual.getOrThrow() shouldBe expected
        }

        "Success: toString" {
            val expected = "Success(hello world.)"
            RoResult.success { "hello world." }.toString() shouldBe expected
        }

        "Success: hashCode" {
            RoResult.success { "hello world." }
                .hashCode() shouldBe RoResult.success { "hello world." }.hashCode()
            RoResult.success { "hello world." }
                .hashCode() shouldNotBe RoResult.success { "HELLO WORLD." }.hashCode()
            RoResult.success { "hello world." }
                .hashCode() shouldNotBe RoResult.failure<String>(error = Exception("test")).hashCode()
        }

        "Success: equals" {
            RoResult.success { "hello world." }
                .equals(RoResult.success { "hello world." }) shouldBe true
            RoResult.success { "hello world." }
                .equals(RoResult.success { "HELLO WORLD." }) shouldBe false
            RoResult.success { "hello world." }
                .equals(RoResult.failure<String>(error = Exception("test"))) shouldBe false
        }

        "Success: isFailure" {
            RoResult.success { "hello world." }.isFailure shouldBe false
        }

        "Success: isSuccess" {
            RoResult.success { "hello world." }.isSuccess shouldBe true
        }

        "Success: getOrElse" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            val actual = result.getOrElse { "" }

            actual shouldBe expected
        }

        "Success: getOrNull" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            val actual = result.getOrNull()

            actual shouldBe expected
        }

        "Success: getOrThrow" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            val actual = result.getOrThrow()

            actual shouldBe expected
        }

        "Success: fold" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            val actual = result.fold(
                failure = { it.message ?: "" },
                success = { it }
            )

            actual shouldBe expected
        }

        "Success: match" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            var actual = ""
            result.match { actual = expected }

            actual shouldBe expected
        }

        "Success -> Failure: filter" {
            val expected = "filter"
            val result = RoResult.success { "hello world." }
            var actual = ""
            result.filter {
                false
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Success -> Success: filter" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            var actual = ""
            result.filter {
                true
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: map" {
            val expected = "hello world."
            val result = RoResult.success { expected.uppercase() }
            var actual = ""
            result.map {
                it.lowercase()
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: mapOrElse" {
            val expected = "hello world."
            val result = RoResult.success { expected.uppercase() }
            var actual = ""
            result.mapOrElse(
                fn = { it.lowercase() },
                or = { expected.uppercase() }
            ).match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: andThen" {
            val expected = "hello world."
            val result = RoResult.success { expected.uppercase() }
            var actual = ""
            result.andThen {
                RoResult.tee { it.lowercase() }
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: orElse" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            var actual = ""
            result.orElse {
                RoResult.tee { expected.uppercase() }
            }.match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success: andThenOrElse" {
            val expected = "hello world."
            val result = RoResult.success { expected.uppercase() }
            var actual = ""
            result.andThenOrElse(
                fn = { RoResult.tee { it.lowercase() } },
                or = { RoResult.tee { expected.uppercase() } }
            ).match(
                failure = {},
                success = { actual = it }
            )

            actual shouldBe expected
        }

        "Success, Failure -> Failure: zip" {
            val expected = "hello world."
            val result = RoResult.success { expected }
            var actual = ""
            result.zip {
                RoResult.tee { throw Exception(it) }
            }.match(
                failure = { actual = it.message ?: "" },
                success = {}
            )

            actual shouldBe expected
        }

        "Success, Success -> Success: zip" {
            val v = "hello world."
            val expected = "(1:hello world., 2:hello world.)"
            val result = RoResult.success { "1:%s".format(v) }
            var actual = ""
            result.zip {
                RoResult.tee { "2:%s".format(v) }
            }.match(
                failure = {},
                success = { actual = it.toString() }
            )

            actual shouldBe expected
        }

    }

}


