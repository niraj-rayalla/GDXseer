plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    api(project(":GDXseer-GL"))
}

/**
 * Copies the native libraries needed into the jar.
 */
val copyNativeLibs by tasks.creating(Copy::class.java) {
    dependsOn(":buildAndroidNativeLibrary")

    val resourcesDir = File("$rootDir/GDXseer-android/src/main/resources")
    resourcesDir.mkdirs()

    from("$rootDir/android-build/libs")
    into(resourcesDir)
}

/**
 * Run the jar task after [copyNativeLibs].
 */
tasks.processResources.get().dependsOn(copyNativeLibs)

//region Maven Publishing

apply(from = "../gdxseer_publishing.build.gradle.kts")

publishing {
    publications {
        getByName(project.name, MavenPublication::class) {
            pom {
                description.set("The specific GDXseer implementation used for Android using the GLES renderer.")
            }
        }
    }
}

copyNativeLibs.mustRunAfter(tasks["sourcesJar"])

//endregion