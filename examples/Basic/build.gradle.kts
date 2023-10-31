buildscript {
    val kotlinVersion = "1.9.0"
    val roboVMVersion = "2.3.20"

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        gradlePluginPortal()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("com.mobidevelop.robovm:robovm-gradle-plugin:$roboVMVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}