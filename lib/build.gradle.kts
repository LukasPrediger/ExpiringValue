@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.jvm)

    `java-library`
    `jvm-test-suite`
    jacoco
}

dependencies {
    testImplementation(platform(libs.kotest.bom))
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.extensions)

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