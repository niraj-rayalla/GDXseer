pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion: String by settings
        id("org.jetbrains.kotlin.jvm") version "1.9.0"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GDXseer"
include(":GL")
include(":GDXseer-desktop")
include(":GDXseer-android")
include(":GDXseer-ios-GL")
include(":Metal")
