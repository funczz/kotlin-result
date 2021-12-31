package io.kotlintest.provided

import io.kotlintest.AbstractProjectConfig
import io.kotlintest.extensions.TestListener
import java.io.File

/*
 * ProjectConfig : プロジェクト定義
 *
 * パッケージ: io.kotlintest.provided 固定
 */
object ProjectConfig : AbstractProjectConfig() {
    //テストリスナーをプロジェクトに登録する。
    override fun listeners(): List<TestListener> = listOf(SpecStopWatchListener)

    //テストファイルを並行に実行する数
    override fun parallelism(): Int = 1

    //プロジェクト名
    val projectName: String = File(".").canonicalFile.name
}