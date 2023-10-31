val kotlinVersion: String by project
val gdxVersion: String by project
val libraryVersion: String by project

plugins {
    id("kotlin")
}

sourceSets.main.get().java.srcDirs("src/main/")
sourceSets.main.get().resources.srcDirs("../android/assets")

// Attributes
val isForRoboVMAttribute = Attribute.of("is_for_robovm", String::class.java)

dependencies {
    api(project(":Example-Basic:core"))

    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-lwjgl3-glfw-awt-macos:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    api("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    api("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
    api("com.badlogicgames.gdx:gdx-tools:$gdxVersion")

    api("io.github.niraj-rayalla:GDXseer-GL:$libraryVersion")
    api("io.github.niraj-rayalla:GDXseer-desktop:$libraryVersion")
}

tasks.test {
    useJUnitPlatform()
}

val mainClassName = "io.github.niraj_rayalla.gdxseer.example.DesktopLauncher"
val assetsDir = File("../android/assets")

tasks.create("run", JavaExec::class.java) {
    mainClass.value(mainClassName)
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
}

tasks.create("debug", JavaExec::class.java) {
    mainClass.value(mainClassName)
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
    debug = true
}

val dist by tasks.creating(Jar::class) {
    manifest {
        attributes(Pair("Main-Class", mainClassName))
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(configurations.runtimeClasspath)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

dist.dependsOn(tasks.classes)