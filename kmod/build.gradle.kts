plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("maven-publish")
}

group = "de.flath.impulse.common.resources"

kotlin {

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "KModKit"
        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("io.github.microutils:kotlin-logging:3.0.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("com.fazecast:jSerialComm:2.10.4")
                implementation("org.slf4j:slf4j-api:2.0.9")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
                implementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
                implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
                implementation("org.apache.logging.log4j:log4j-api:2.20.0")
                implementation("org.apache.logging.log4j:log4j-core:2.20.0")
                implementation("org.apache.commons:commons-exec:1.3")
                implementation(libs.modbus.j2mod)
            }
        }
        val jsMain by getting {
            dependencies {
                // JS-specific dependencies if needed
            }
        }
        val jsTest by getting {
            dependencies {
                // JS-specific test dependencies if needed
            }
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                name.set("kmod")
                description.set("Kotlin Multiplatform implementation of the Modbus protocol")
                url.set("https://github.com/kmod/kmod")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("kmod")
                        name.set("kmod Team")
                        email.set("info@kmod.io")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/kmod/kmod.git")
                    developerConnection.set("scm:git:ssh://github.com/kmod/kmod.git")
                    url.set("https://github.com/kmod/kmod")
                }
            }
        }
    }
}
