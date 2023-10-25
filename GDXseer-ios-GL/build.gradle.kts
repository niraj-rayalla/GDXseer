plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":GL"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

/**
 * Make the jar a fat jar with just the GL jar. Everything else is in the core GDXseer jar.
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
 * Build all the native libraries/frameworks before the jar is built and then include the built framework in the META-INF directory.
 */
tasks.withType<Jar> {
    dependsOn(":buildIOSGLFrameworks")
    metaInf {
        from("./build/GDXseer.xcframework").into("robovm/ios/libs/GDXseer.xcframework")
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