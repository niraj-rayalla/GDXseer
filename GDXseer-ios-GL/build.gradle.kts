plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    api(project(":GDXseer-GL"))
}

/**
 * Build all the native libraries/frameworks before the jar is built and then include the built framework in the META-INF directory.
 */
tasks.withType<Jar> {
    dependsOn(":buildIOSGLFrameworks")
    metaInf {
        from("$rootDir/release_native_libs/ios/GL/GDXseer.xcframework").into("robovm/ios/libs/GDXseer.xcframework")
    }
    metaInf {
        from("./robovm.xml").into("robovm/ios/")
    }
}

/*
/**
 * Copies the native libraries needed into the jar.
 */
val copyNativeLibs: Task by tasks.creating {
    dependsOn(":buildAndSignIOSDeviceGLNativeLibrary")
    dependsOn(":buildIOSSimulatorGLNativeLibrary")

    val resourcesDir = File("$rootDir/GDXseer-ios-GL/src/main/resources")
    resourcesDir.mkdirs()

    val copyDeviceNativeLibs by tasks.creating(Copy::class.java) {
        from("$rootDir/cmake-ios-device-gl/cpp/libGDXseer_Effekseer.dylib")
        into(resourcesDir)
        rename { "libGDXseer_Effekseer_device.dylib" }
    }
    dependsOn(copyDeviceNativeLibs)
    copyDeviceNativeLibs.mustRunAfter(":buildAndSignIOSDeviceGLNativeLibrary")

    val copySimulatorNativeLibs by tasks.creating(Copy::class.java) {
        from("$rootDir/cmake-ios-simulator-gl/cpp/Release-iphonesimulator/libGDXseer_Effekseer.dylib")
        into(resourcesDir)
        rename { "libGDXseer_Effekseer_simulator.dylib" }
    }
    dependsOn(copySimulatorNativeLibs)
    copySimulatorNativeLibs.mustRunAfter(":buildIOSSimulatorGLNativeLibrary")
}

/**
 * Run the jar task after [copyNativeLibs].
 */
tasks.processResources.get().dependsOn(copyNativeLibs)
 */

//region Maven Publishing

apply(from = "../gdxseer_publishing.build.gradle.kts")

publishing {
    publications {
        getByName(project.name, MavenPublication::class) {
            pom {
                description.set("The specific GDXseer implementation used for iOS using the GLES renderer.")
            }
        }
    }
}

//endregion