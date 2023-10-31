val kotlinVersion: String by project
val gdxVersion: String by project
val libraryVersion: String by project

plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

sourceSets.main.get().java.srcDirs("src/main/")

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    api("io.github.hoc081098:FlowExt:0.7.1")

    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")

    api("io.github.niraj-rayalla:GDXseer:$libraryVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    testImplementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    testImplementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    testImplementation("io.github.niraj-rayalla:GDXseer:$libraryVersion")
}

tasks.test {
    useJUnitPlatform {
        workingDir(File("../android/assets"))
    }
}