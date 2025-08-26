@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.jvm)

    `java-library`
    `jvm-test-suite`
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.extensions)

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}