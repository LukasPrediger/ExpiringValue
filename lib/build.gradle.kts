@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.jvm)

    `java-library`
    `jvm-test-suite`

    alias(libs.plugins.kotest)
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            dependencies {
                implementation(platform(libs.kotest.bom))
                implementation(libs.kotest.engine)
                implementation(libs.kotest.assertions)
                implementation(libs.kotest.extensions)
            }
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
