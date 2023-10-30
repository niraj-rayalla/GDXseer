plugins {
    kotlin("jvm")
    id("maven-publish")
}

val gdxVersion: String by project

dependencies {
    api(rootProject)
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
}

//region Maven Publishing

apply(from = "../gdxseer_publishing.build.gradle.kts")

publishing {
    publications {
        getByName(project.name, MavenPublication::class) {
            pom {
                description.set("The GDXseer library when using OpenGL as the renderer.")
            }
        }
    }
}

//endregion