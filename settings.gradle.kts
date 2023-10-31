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
        mavenLocal()
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

// Examples

// Basic
include(":Example-Basic")
project(":Example-Basic").projectDir = File(rootDir, "examples/Basic/")
include(":Example-Basic:core")
project(":Example-Basic:core").projectDir = File(rootDir, "examples/Basic/core/")
include(":Example-Basic:desktop")
project(":Example-Basic:desktop").projectDir = File(rootDir, "examples/Basic/desktop/")
include(":Example-Basic:android")
project(":Example-Basic:android").projectDir = File(rootDir, "examples/Basic/android/")
include(":Example-Basic:ios")
project(":Example-Basic:ios").projectDir = File(rootDir, "examples/Basic/ios/")