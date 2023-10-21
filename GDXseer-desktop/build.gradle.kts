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

    into("$rootDir/gdx-effekseer-desktop/src/main/resources")
}

/**
 * Run the jar task after [copyNativeLibs].
 */
tasks.jar.get().mustRunAfter(copyNativeLibs)