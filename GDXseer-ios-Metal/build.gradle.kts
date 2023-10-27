plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":Metal"))
}

java {
    withJavadocJar()
    withSourcesJar()
}

/**
 * Make the jar a fat jar with just the Metal jar. Everything else is in the core GDXseer jar.
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
            it.name == "Metal.jar"
        }.map { zipTree(it) }
    })
}

/**
 * Build all the native libraries/frameworks before the jar is built and then include the built framework in the META-INF directory.
 */
tasks.withType<Jar> {
    dependsOn(":buildIOSMetalFrameworks")
    metaInf {
        from("./build/GDXseer.xcframework").into("robovm/ios/libs/GDXseer.xcframework")
    }
    metaInf {
        from("./robovm.xml").into("robovm/ios/")
    }
}