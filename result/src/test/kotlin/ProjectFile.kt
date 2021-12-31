package io.kotlintest.provided

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/*
 * パッケージ: io.kotlintest.provided 固定
 */
object ProjectFile {

    val tmpdir = Paths.get(System.getProperty("java.io.tmpdir")).toFile().canonicalFile!!

    fun createTempDirectory(path: File = tmpdir, prefix: String = "${ProjectConfig.projectName}_"): File {
        return Files.createTempDirectory(path.toPath(), prefix).toFile()
    }

    fun createTempFile(path: File = tmpdir, prefix: String = "", suffix: String = ".temp"): File {
        return Files.createTempFile(path.toPath(), prefix, suffix).toFile()
    }
}

