plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.gradleVersionsPlugin) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    Impulsee core requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}



group = "com.ghgande"
version = "3.0.0-SNAPSHOT"

