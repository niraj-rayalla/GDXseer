val kotlinVersion: String by project
val gdxVersion: String by project
val libraryVersion: String by project
val roboVMVersion: String by project

plugins {
    kotlin("jvm")
    id("robovm")
}

sourceSets.main.get().java.srcDirs("/src/main/")
sourceSets.main.get().kotlin.srcDirs("/src/main/")
tasks.compileJava {
    options.encoding = "UTF-8"
}
tasks.compileTestJava {
    options.encoding = "UTF-8"
}

tasks.findByName("launchIPhoneSimulator")!!.dependsOn(tasks.build)
tasks.findByName("launchIPadSimulator")!!.dependsOn(tasks.build)
tasks.findByName("launchIOSDevice")!!.dependsOn(tasks.build)
tasks.findByName("createIPA")!!.dependsOn(tasks.build)

kotlin {
    jvmToolchain(17)
}

robovm {
    archs = "arm64"
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")

    api(project(":Example-Basic:core"))

    api("com.mobidevelop.robovm:robovm-rt:$roboVMVersion")
    api("com.mobidevelop.robovm:robovm-cocoatouch:$roboVMVersion")

    api("com.badlogicgames.gdx:gdx-backend-robovm:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-ios")

    api("io.github.niraj-rayalla:GDXseer-GL:$libraryVersion")
    api("io.github.niraj-rayalla:GDXseer-ios-GL:$libraryVersion")
}
