@file:Suppress("UnstableApiUsage")

rootProject.name = "kformat"

pluginManagement {
    val kotlinVersion: String by settings
    val ktlintPluginVersion: String by settings

    val repos: (RepositoryHandler).() -> Unit = {
        jcenter()
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }
    repositories(repos)

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.jlleitschuh.gradle.ktlint") {
                useModule("org.jlleitschuh.gradle:ktlint-gradle:$ktlintPluginVersion")
            }
        }
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintPluginVersion
    }
}

gradle.projectsLoaded {
    rootProject.repositories.addAll(pluginManagement.repositories)
}
