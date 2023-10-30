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
project(":GL").name = "GDXseer-GL"
include(":GDXseer-desktop")
include(":GDXseer-android")
include(":GDXseer-ios-GL")
include(":Metal")
project(":Metal").name = "GDXseer-Metal"
include(":GDXseer-ios-Metal")
