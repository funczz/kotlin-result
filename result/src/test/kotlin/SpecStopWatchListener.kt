package io.kotlintest.provided

import io.kotlintest.Spec
import io.kotlintest.extensions.TestListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/*
 * プロジェクト用テストリスナー
 *
 * package io.kotlintest.provided
 * object ProjectConfig : AbstractProjectConfig() {
 *     override fun listeners(): List<TestListener> = listOf(SpecStopWatchListener)
 * }
 */
object SpecStopWatchListener : TestListener {
    private val logger: Logger by lazy { LoggerFactory.getLogger(this::class.java) }

    private var started = 0L

    override fun beforeSpec(spec: Spec) {
        started = System.currentTimeMillis()
    }

    override fun afterSpec(spec: Spec) {
        logger.info("Duration of `${spec.description().name}` = %dms".format(System.currentTimeMillis() - started))
    }
}