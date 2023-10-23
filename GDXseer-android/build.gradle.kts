plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":GL"))
}

/**
 * Make the jar a fat jar.
 */
tasks.withType<Jar> {
    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.compileClasspath)
    from({
        configurations.compileClasspath.get().filter {
            // Only include the GDXseer GL jar since all other jars are already included in the core library
            it.name == "GL.jar"
        }.map { zipTree(it) }
    })
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