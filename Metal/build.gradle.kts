plugins {
    kotlin("jvm")
}

val gdxVersion: String by project

dependencies {
    api(rootProject)
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
}

java {
    withJavadocJar()
    withSourcesJar()
}