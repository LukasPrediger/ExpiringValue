plugins {
    `test-report-aggregation`
    `jacoco-report-aggregation`
    `java-base`
}

childProjects.onEach {
    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}