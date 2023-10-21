plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":GL"))
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
            from("$rootDir/cmake-windows/cpp/GDXseer_Effekseer.dll")
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