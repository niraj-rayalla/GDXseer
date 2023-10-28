plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":GL"))
}

java {
    withJavadocJar()
    withSourcesJar()
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
    dependsOn(":buildDesktopNativeLibrary")

    when {
        org.gradle.internal.os.OperatingSystem.current().isMacOsX -> {
            from("$rootDir/cmake-macos/cpp/libGDXseer_Effekseer.dylib")
        }
        org.gradle.internal.os.OperatingSystem.current().isLinux -> {
            from("$rootDir/cmake-linux/cpp/libGDXseer_Effekseer.so")
        }
        else -> {
            from("$rootDir/cmake-windows/cpp/Release/GDXseer_Effekseer.dll")
        }
    }


    val resourcesDir = File("$rootDir/GDXseer-desktop/src/main/resources")
    resourcesDir.mkdirs()
    into(resourcesDir)
}

/**
 * Run the jar task after [copyNativeLibs].
 */
tasks.processResources.get().dependsOn(copyNativeLibs)