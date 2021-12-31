import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*

object CommonDeps {

    /**
     * libs
     */
    object Jar {
        val libs = mapOf("dir" to "libs", "include" to listOf("*.jar"))
    }

    /**
     * java
     */
    object Java {
        val version = JavaVersion.VERSION_1_8
        const val encoding = "UTF-8"
    }

    /**
     * kotlin
     */
    object Kotlin {
        val jvmTarget = Java.version.name.replace("VERSION_", "").replace("_", ".")
        val freeCompilerArgs = listOf("-Xjsr305=strict")
        const val platform = "org.jetbrains.kotlin:kotlin-bom"
        const val stdlibJdk = "org.jetbrains.kotlin:kotlin-stdlib-jdk8" //javaVersion: 1.8
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect"
    }

    /**
     * kotlin-test
     */
    object Kotlintest {
        const val test = "org.jetbrains.kotlin:kotlin-test"
        const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit"
        const val runnerJunit5 = "io.kotlintest:kotlintest-runner-junit5:3.4.2"

        object Buildscript {
            object Dependencies {
                object Classpath {
                    //plugin: junit
                    const val junit = "org.junit.platform:junit-platform-gradle-plugin:1.0.2"
                }
            }
        }
    }

    /**
     * logger
     */
    object Logger {
        const val logbackClassic = "ch.qos.logback:logback-classic:1.1.7"
        const val jansi = "org.fusesource.jansi:jansi:1.18"
        const val slf4jApi = "org.slf4j:slf4j-api:1.7.21"
        const val slf4jJcl = "org.slf4j:jcl-over-slf4j:1.7.21"
    }

    /**
     * mockito-kotlin
     */
    object MockitoKotlin {
        const val core = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
    }

    /**
     * Toml
     */
    object Toml {
        const val core = "com.moandjiezana.toml:toml4j:0.7.2"
    }

    /**
     * JSON-B
     */
    object JsonB {
        const val jsonApi = "javax.json:javax.json-api:1.1"
        const val json = "org.glassfish:javax.json:1.1"
        const val jsonBind = "javax.json.bind:javax.json.bind-api:1.0"
        const val yasson = "org.eclipse::yasson:1.0.1"
    }

}

/**
 * Dependencies: kotlin
 */
fun Project.kotlinProjectDependencies() {
    dependencies {
        //libs
        "implementation"(fileTree(CommonDeps.Jar.libs))
        //kotlin
        //"implementation"(platform(CommonDeps.Kotlin.platform))
        "implementation"(CommonDeps.Kotlin.stdlibJdk)
        //kotlintest
        "testImplementation"(CommonDeps.Kotlintest.runnerJunit5)
    }
}

/**
 * Dependencies: JSON-B
 */
fun Project.JsonBProjectDependencies() {
    dependencies {
        "implementation"(CommonDeps.JsonB.jsonApi)
        "implementation"(CommonDeps.JsonB.json)
        "implementation"(CommonDeps.JsonB.jsonBind)
        "implementation"(CommonDeps.JsonB.yasson)
    }
}
