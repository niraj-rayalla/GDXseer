plugins {
    kotlin("jvm")
    id("maven-publish")
}

val libraryVersion: String by project

dependencies {
    api(project(":GDXseer-Metal"))
}

/**
 * Build all the native libraries/frameworks before the jar is built and then include the built framework in the META-INF directory.
 */
tasks.withType<Jar> {
    dependsOn(":buildIOSMetalFrameworks")
    metaInf {
        from("$rootDir/release_native_libs/ios/Metal/GDXseer.xcframework").into("robovm/ios/libs/GDXseer.xcframework")
    }
    metaInf {
        from("./robovm.xml").into("robovm/ios/")
    }
}
//region Maven Publishing

apply(from = "../gdxseer_publishing.build.gradle.kts")

publishing {
    publications {
        getByName(project.name, MavenPublication::class) {
            version = "$libraryVersion-WIP"
            pom {
                description.set("The specific GDXseer implementation used for iOS using the Metal renderer.")
            }
        }
    }
}

//endregion