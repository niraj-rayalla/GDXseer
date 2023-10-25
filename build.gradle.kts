@file:Suppress("LocalVariableName")

import java.io.FileInputStream
import java.util.Properties

plugins {
    kotlin("jvm")
}

val gdxVersion: String by project
val libraryVersion: String = "1.0.0"

dependencies {
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
}

java {
    withJavadocJar()
    withSourcesJar()
}

//region Java Wrapping of Effekseer C++ using Swig

/**
 * The directory all the Swig generated Java wrapper classes of Effekseer will be placed.
 */
val javaWrappedEffekseerDir = File("${projectDir}/src/main/java/io/github/niraj_rayalla/gdxseer/effekseer")
/**
 * Task used to create the directory pointed by [javaWrappedEffekseerDir].
 */
val createJavaWrappedEffekseerDir by tasks.creating {
    doLast {
        delete(javaWrappedEffekseerDir)
        javaWrappedEffekseerDir.mkdirs()
    }
}

/**
 * The directory all the Swig generated Java classes of the Effekseer adapter logic will be placed.
 */
val javaAdapterEffekseerDir = File("${projectDir}/src/main/java/io/github/niraj_rayalla/gdxseer/adapter_effekseer")
/**
 * Task used to create the directory pointed by [javaAdapterEffekseerDir].
 */
val createJavaAdapterEffekseerDir by tasks.creating {
    doLast {
        delete(javaAdapterEffekseerDir)
        javaAdapterEffekseerDir.mkdirs()
    }
}

/**
 * The directory all the Swig generated Java classes of the Effekseer GL logic will be placed.
 */
val javaEffekseerGLDir = File("${projectDir}/GL/src/main/java/io/github/niraj_rayalla/gdxseer/effekseer_gl")
/**
 * Task used to create the directory pointed by [javaEffekseerGLDir].
 */
val createJavaEffekseerGLDir by tasks.creating {
    doLast {
        delete(javaEffekseerGLDir)
        javaEffekseerGLDir.mkdirs()
    }
}

/**
 * The directory all the copied and transformed Effekseer source code is located. This is the actual files used by
 * Swig to generate the Java wrappers.
 */
val copiedAndTransformedEffekseerDir = File("${projectDir}/cpp/Effekseer/Dev/Cpp/")
/**
 * Task used to create the directory pointed by [copiedAndTransformedEffekseerDir].
 */
val createCopiedAndTransformedEffekseerDir by tasks.creating {
    doLast {
        copiedAndTransformedEffekseerDir.mkdirs()
    }
}
/**
 * Task used to copy the Effekseer source and do any transformations before getting Swigged.
 */
val copyAndTransformEffekseerSourceForSwig: Task by tasks.creating {
    val deleteTask by tasks.creating {
        // Delete existing copy
        delete(copiedAndTransformedEffekseerDir)
    }
    dependsOn(deleteTask)
    dependsOn(createCopiedAndTransformedEffekseerDir)
    createCopiedAndTransformedEffekseerDir.shouldRunAfter(deleteTask)

    val sourceCppDir = File("${projectDir}/Effekseer/Dev/Cpp/")

    val copyTask by tasks.creating(Copy::class.java) {
        // Get a new copy of Effekseer source
        copy {
            from(sourceCppDir)
                .into(copiedAndTransformedEffekseerDir)
                .exclude("3rdParty")
                .exclude("EditorCommon")
                .exclude("EffekseerMaterial*")
                .exclude("EffekseerRendererDX*")
                .exclude("TakeScreenshots*")
                .exclude("Test*")
                .exclude("Viewer")
        }
    }
    dependsOn(copyTask)
    copyTask.shouldRunAfter(createCopiedAndTransformedEffekseerDir)

    val transformTask by tasks.creating(Copy::class.java) {
        // Transform sources
        var wasTransformed = false
        val EffectNodeRelativeFilePath = "Effekseer/Effekseer/Effekseer.EffectNode.h"
        val transformSearch = "struct alignas(2) TriggerValues"
        val transformReplace = "struct TriggerValues"
        val copiedEffectNodeIncludePath = File(copiedAndTransformedEffekseerDir, EffectNodeRelativeFilePath)
        delete(copiedEffectNodeIncludePath)
        copy {
            from(File(sourceCppDir, EffectNodeRelativeFilePath))
                .into(copiedEffectNodeIncludePath.parentFile)
                .filter { line ->
                    if (line.contains(transformSearch)) {
                        wasTransformed = true
                        line.replace(transformSearch, transformReplace)
                    }
                    else line
                }
        }

        if (!wasTransformed) {
            throw GradleException("Failed to find \"$transformSearch\" in $copiedEffectNodeIncludePath so that it can be replaced with \"$transformReplace\" for SWIG generation. This is needed to avoid this error: \"Swig: Syntax error in input\".")
        }
    }
    dependsOn(transformTask)
    transformTask.shouldRunAfter(copyTask)
}

/**
 * Task used to generate the Java logic that wraps the C++ Effekseer logic using Swig.
 */
val generateWrapperEffekseer by tasks.creating {
    dependsOn(copyAndTransformEffekseerSourceForSwig)

    dependsOn(createJavaWrappedEffekseerDir)
    createJavaWrappedEffekseerDir.shouldRunAfter(copyAndTransformEffekseerSourceForSwig)

    // The effekseer logic
    val effekseerGenerateTask by tasks.creating(Exec::class.java) {
        commandLine(
            "swig", "-c++", "-java",
            "-package", "io.github.niraj_rayalla.gdxseer.effekseer",
            "-outdir", javaWrappedEffekseerDir.absolutePath,
            "-o", "${projectDir}/cpp/Effekseer_Swig.cpp",
            "${projectDir}/swig_interface/effekseer.i"
        )
    }
    dependsOn(effekseerGenerateTask)
    effekseerGenerateTask.shouldRunAfter(createJavaWrappedEffekseerDir)

    // The effekseer adapter logic
    val adapterEffekseerGenerateTask by tasks.creating(Exec::class.java) {
        dependsOn(createJavaAdapterEffekseerDir)
        commandLine(
            "swig", "-c++", "-java",
            "-package", "io.github.niraj_rayalla.gdxseer.adapter_effekseer",
            "-outdir", javaAdapterEffekseerDir.absolutePath,
            "-o", "${projectDir}/cpp/Adapter_Effekseer_Swig.cpp",
            "${projectDir}/swig_interface/adapter_effekseer.i"
        )
    }
    dependsOn(adapterEffekseerGenerateTask)
    adapterEffekseerGenerateTask.shouldRunAfter(effekseerGenerateTask)

    // The effekseer GL logic
    val effekseerGLGenerateTask by tasks.creating(Exec::class.java) {
        dependsOn(createJavaEffekseerGLDir)
        commandLine(
            "swig", "-c++", "-java",
            "-package", "io.github.niraj_rayalla.gdxseer.effekseer_gl",
            "-outdir", javaEffekseerGLDir.absolutePath,
            "-o", "${projectDir}/cpp/Effekseer_GL_Swig.cpp",
            "${projectDir}/swig_interface/effekseer_GL.i"
        )
    }
    dependsOn(effekseerGLGenerateTask)
    effekseerGLGenerateTask.shouldRunAfter(adapterEffekseerGenerateTask)
}

//endregion

//region CMAKE configuration

//region Desktop

/**
 * The directory to use for Cmake building on the current OS.
 */
val desktopCmakeBuildDir = when {
    org.gradle.internal.os.OperatingSystem.current().isMacOsX -> {
        File("${projectDir}/cmake-macos")
    }
    org.gradle.internal.os.OperatingSystem.current().isLinux -> {
        File("${projectDir}/cmake-linux")
    }
    else -> {
        File("${projectDir}/cmake-windows")
    }
}

/**
 * Used to create [desktopCmakeBuildDir]. This doesn't delete the directory if it already exists.
 */
val createDesktopCmakeBuildDir by tasks.creating {
    doLast {
        desktopCmakeBuildDir.mkdirs()
    }
}

/**
 * Calls cmake to configure for cmake building of the desktop library on the current OS.
 */
val configureDesktopBuilding by tasks.creating(Exec::class.java) {
    dependsOn(generateWrapperEffekseer)
    dependsOn(createDesktopCmakeBuildDir)
    createDesktopCmakeBuildDir.shouldRunAfter(generateWrapperEffekseer)

    val commandLineArgs = ArrayList<String>().apply {
        add("cmake")
        if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            add("-D")
            add("CMAKE_OSX_ARCHITECTURES=x86_64;arm64")
        }
        add("-D")
        add("GDXSEER_RENDERER=GL")
        add("-B")
        add(desktopCmakeBuildDir.absolutePath)
    }
    commandLine(commandLineArgs)
}

//endregion

//region iOS

/**
 * @return The directory the iOS library for the given properties should be built in.
 */
fun getIOSCmakeBuildDir(isForSimulator: Boolean, isUsingMetal: Boolean): File {
    val sim = if (isForSimulator) "simulator" else "device"
    val renderer = if (isUsingMetal) "metal" else "gl"
    return File("${projectDir}/cmake-ios-$sim-$renderer")
}

/**
 * Used to create the directory the iOS library for the given properties should be built in. This doesn't delete the directory if it already exists.
 */
fun createIOSCmakeBuildDir(isForSimulator: Boolean, isUsingMetal: Boolean): Task {
    val sim = if (isForSimulator) "simulator" else "device"
    val renderer = if (isUsingMetal) "metal" else "gl"
    return tasks.create("createIOS_${sim}_${renderer}") {
        doLast {
            getIOSCmakeBuildDir(isForSimulator, isUsingMetal).mkdirs()
        }
    }
}

/**
 * @return A task for configuring the Cmake for building the IOS library with option [isForSimulator].
 */
fun getConfigureIOSBuildTask(isForSimulator: Boolean, isUsingMetal: Boolean): Task {
    return tasks.create("configureIOS${if (isForSimulator) "Simulator" else "Device"}${if (isUsingMetal) "Metal" else "GL"}Building", Exec::class.java) {
        dependsOn(generateWrapperEffekseer)

        val createDirTask = createIOSCmakeBuildDir(isForSimulator, isUsingMetal)
        dependsOn(createDirTask)
        createDirTask.shouldRunAfter(generateWrapperEffekseer)

        fun getIosJavaHomeFromLocalProperties(): String? {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            return localProperties.getProperty("iOS_JAVA_HOME")
        }

        val commandLineArgs = ArrayList<String>().apply {
            add("cmake")
            if (isForSimulator) {
                add("-G")
                add("Xcode")
            }
            add("-D")
            add("iOS_JAVA_HOME=${getIosJavaHomeFromLocalProperties() ?: System.getenv("JAVA_HOME")}")
            add("-D")
            add("CMAKE_SYSTEM_NAME=iOS")
            add("-D")
            add("CMAKE_OSX_ARCHITECTURES=x86_64;arm64")
            add("-D")
            add("GDXSEER_RENDERER=${if (isUsingMetal) "Metal" else "GL"}")
            if (!isUsingMetal) {
                add("-D")
                add("USE_OPENGLES3=ON")
            }
            add("-B")
            add(getIOSCmakeBuildDir(isForSimulator, isUsingMetal).absolutePath)
        }
        commandLine(commandLineArgs)
    }
}

/**
 * Calls cmake to configure for cmake building of the iOS library on device using GLES.
 */
val configureIOSDeviceGLBuilding = getConfigureIOSBuildTask(isForSimulator = false, isUsingMetal = false)
/**
 * Calls cmake to configure for cmake building of the iOS library on simulator using GLES.
 */
val configureIOSSimulatorGLBuilding = getConfigureIOSBuildTask(isForSimulator = true, isUsingMetal = false)

/**
 * Calls cmake to configure for cmake building of the iOS library on device using Metal.
 */
val configureIOSDeviceMetalBuilding = getConfigureIOSBuildTask(isForSimulator = false, isUsingMetal = true)
/**
 * Calls cmake to configure for cmake building of the iOS library on simulator using Metal.
 */
val configureIOSSimulatorMetalBuilding = getConfigureIOSBuildTask(isForSimulator = true, isUsingMetal = true)

//endregion

//endregion

//region CMAKE Builds

/**
 * Builds the desktop GDXseer C++ library.
 */
val buildDesktopNativeLibrary by tasks.creating(Exec::class.java) {
    dependsOn(configureDesktopBuilding)

    // Build the core jar
    dependsOn(tasks.jar)
    tasks.jar.get().shouldRunAfter(configureDesktopBuilding)

    // Build the GL C++ library
    commandLine(
        "cmake",
        "--build", desktopCmakeBuildDir.absolutePath,
        "--config", "Release"
    )
}

/**
 * Builds the Android GDXseer C++ library.
 */
val buildAndroidNativeLibrary by tasks.creating {
    dependsOn(generateWrapperEffekseer)

    // Build the core jar
    dependsOn(tasks.jar)
    tasks.jar.get().shouldRunAfter(generateWrapperEffekseer)

    // Get the build project path
    val androidBuildProjectPath = projectDir.absolutePath
    val androidBuildScriptPath = File(projectDir, "cpp/Android_mk/Android.mk").absolutePath
    val androidNativeLibOutPath = File(projectDir, "android-build").absolutePath

    // Returns a new task for building the GDXseer Android native library for the given CPU architecture
    fun buildForArch(arch: String): Task {
        return tasks.create("buildAndroidNativeLibrary_$arch") {

            val clean = tasks.create("buildAndroidNativeLibrary_${arch}_clean", Exec::class.java) {
                // Clean
                commandLine(
                    "ndk-build", "clean",
                    "APP_BUILD_SCRIPT=$androidBuildScriptPath",
                    "NDK_PROJECT_PATH=$androidBuildProjectPath",
                    "NDK_APPLICATION_MK=cpp/Android_mk/${arch}.mk"
                )
            }

            dependsOn(clean)

            val build = tasks.create("buildAndroidNativeLibrary_${arch}_build", Exec::class.java) {
                // Build the GL C++ library
                commandLine(
                    "ndk-build", "-j4",
                    "APP_BUILD_SCRIPT=$androidBuildScriptPath",
                    "NDK_PROJECT_PATH=$androidBuildProjectPath",
                    "NDK_APPLICATION_MK=cpp/Android_mk/${arch}.mk",
                    "NDK_OUT=$androidNativeLibOutPath",
                    "NDK_LIBS_OUT=$androidNativeLibOutPath/libs/"
                )
            }

            dependsOn(build)
            build.shouldRunAfter(clean)
        }
    }

    val build_x86 = buildForArch("x86")
    dependsOn(build_x86)
    build_x86.shouldRunAfter(tasks.jar.get())

    val build_x86_64 = buildForArch("x86_64")
    dependsOn(build_x86_64)
    build_x86_64.shouldRunAfter(build_x86)

    val build_armeabi_v7a = buildForArch("armeabi-v7a")
    dependsOn(build_armeabi_v7a)
    build_armeabi_v7a.shouldRunAfter(build_x86_64)

    val build_arm64_v8a = buildForArch("arm64-v8a")
    dependsOn(build_arm64_v8a)
    build_arm64_v8a.shouldRunAfter(build_armeabi_v7a)
}

/**
 * @return A task for building the IOS library with options [isForSimulator] and [isUsingMetal].
 */
fun getBuildNativeIOSTask(isForSimulator: Boolean, isUsingMetal: Boolean): Task {
    val taskNamePostfix = "IOS${if (isForSimulator) "Simulator" else "Device"}${if (isUsingMetal) "Metal" else "GL"}NativeLibrary"
    return tasks.create("build$taskNamePostfix", Exec::class.java) {
        val configureIOSBuildingTask = if (isForSimulator) {
            if (isUsingMetal) configureIOSSimulatorMetalBuilding else configureIOSSimulatorGLBuilding
        }
        else {
            if (isUsingMetal) configureIOSDeviceMetalBuilding else configureIOSDeviceGLBuilding
        }
        dependsOn(configureIOSBuildingTask)

        // Build the core jar
        dependsOn(tasks.jar)
        tasks.jar.get().shouldRunAfter(configureIOSBuildingTask)

        // Build the C++ library
        val cmakeArgs = listOf(
            "cmake",
            "--build", getIOSCmakeBuildDir(isForSimulator, isUsingMetal).absolutePath,
            "--config", "Release"
        ) + (if (isForSimulator) listOf("--", "-sdk", "iphonesimulator") else emptyList())
        commandLine(cmakeArgs)
    }
}

/**
 * Builds the iOS GL GDXseer C++ library for use on a iOS device. Also signs the library.
 */
val buildIOSDeviceGLNativeLibrary = getBuildNativeIOSTask(isForSimulator = false, isUsingMetal = false)

/**
 * Builds the iOS GL GDXseer C++ library for use on a iOS simulator.
 */
val buildIOSSimulatorGLNativeLibrary = getBuildNativeIOSTask(isForSimulator = true, isUsingMetal = false)

/**
 * Builds the device and simulator GL library for iOS, and then their corresponding unsigned frameworks, and finally from that the unsigned xcframework.
 */
val buildIOSGLFrameworks by tasks.creating {
    dependsOn(buildIOSDeviceGLNativeLibrary)
    dependsOn(buildIOSSimulatorGLNativeLibrary)

    val cleanIOSGLFrameworks: Task by tasks.creating {
        doLast {
            delete("./GDXseer-ios-GL/build")
            delete("./GDXseer-ios-GL/build_device")
            delete("./GDXseer-ios-GL/build_simulator")
        }
    }
    dependsOn(cleanIOSGLFrameworks)
    cleanIOSGLFrameworks.mustRunAfter(buildIOSDeviceGLNativeLibrary).mustRunAfter(buildIOSSimulatorGLNativeLibrary)

    // Device framework
    val buildIOSGLDeviceFramework by tasks.creating(Exec::class.java) {
        commandLine(
            "xcodebuild",
            "-project", "./GDXseer-ios-GL/GDXseer_device.xcodeproj",
            "-scheme", "GDXseer",
            "-configuration", "Release",
            "-destination=\"generic/platform=iOS\"",
            "-derivedDataPath", "./GDXseer-ios-GL/build_device",
            "-archivePath", "./GDXseer-ios-GL/build_device",
            "ONLY_ACTIVE_ARCH=NO",
            "SKIP_INTSALL=NO",
            "BUILD_LIBRARY_FOR_DISTRBUTION=YES",
            "-sdk", "iphoneos"
        )
    }
    dependsOn(buildIOSGLDeviceFramework)
    buildIOSGLDeviceFramework.mustRunAfter(cleanIOSGLFrameworks)
    // Simulator framework
    val buildIOSGLSimulatorFramework by tasks.creating(Exec::class.java) {
        commandLine(
            "xcodebuild",
            "-project", "./GDXseer-ios-GL/GDXseer_simulator.xcodeproj",
            "-scheme", "GDXseer",
            "-configuration", "Release",
            "-destination=\"generic/platform=iOS Simulator\"",
            "-derivedDataPath", "./GDXseer-ios-GL/build_simulator",
            "-archivePath", "./GDXseer-ios-GL/build_simulator",
            "ONLY_ACTIVE_ARCH=NO",
            "SKIP_INTSALL=NO",
            "BUILD_LIBRARY_FOR_DISTRBUTION=YES",
            "-sdk", "iphonesimulator"
        )
    }
    dependsOn(buildIOSGLSimulatorFramework)
    buildIOSGLSimulatorFramework.mustRunAfter(cleanIOSGLFrameworks)

    // XCFramework of both the device and simulator frameworks
    val buildIOSGLXCFramework by tasks.creating(Exec::class.java) {
        commandLine(
            "xcodebuild", "-create-xcframework",
            "-framework", "./GDXseer-ios-GL/build_device/Build/Products/Release-iphoneos/GDXseer.framework",
            "-framework", "./GDXseer-ios-GL/build_simulator/Build/Products/Release-iphonesimulator/GDXseer.framework",
            "-output", "./GDXseer-ios-GL/build/GDXseer.xcframework"
        )
    }
    dependsOn(buildIOSGLXCFramework)
    buildIOSGLXCFramework.mustRunAfter(buildIOSGLDeviceFramework).mustRunAfter(buildIOSGLSimulatorFramework)

    // Get the code sign identity
    fun getCodeSignIdentityFromLocalProperties(): String? {
        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))
        return localProperties.getProperty("iOS_CODE_SIGN_IDENTITY")
    }
    val codeSignIdentity = getCodeSignIdentityFromLocalProperties() ?: "-"

    // Sign the built XCFramework
    val signIOSGLXCFramework by tasks.creating(Exec::class.java) {
        commandLine(
            "codesign", "-s", codeSignIdentity, "./GDXseer-ios-GL/build/GDXseer.xcframework"
        )
    }
    dependsOn(signIOSGLXCFramework)
    signIOSGLXCFramework.mustRunAfter(buildIOSGLXCFramework)

    // Sign the built XCFramework
    val signIOSGLXCFramework2 by tasks.creating(Exec::class.java) {
        commandLine(
            "codesign", "-s", codeSignIdentity, "./GDXseer-ios-GL/build/GDXseer.xcframework/ios-arm64/GDXseer.framework/Frameworks/libGDXseer_Effekseer.dylib"
        )
    }
    dependsOn(signIOSGLXCFramework2)
    signIOSGLXCFramework2.mustRunAfter(signIOSGLXCFramework)
}

//endregion

//region Final Builds

/**
 * Builds the C++ and Jar for the desktop library.
 */
val buildDesktopLibrary by tasks.creating {
    dependsOn(":GDXseer-desktop:jar")
}

/**
 * Builds the C++ and Jar for the Android library.
 */
val buildAndroidLibrary by tasks.creating {
    dependsOn(":GDXseer-android:jar")
}

/**
 * Builds the C++ and Jar for the iOS GL library.
 */
val buildIOSGLLibrary by tasks.creating {
    dependsOn(":GDXseer-ios-GL:jar")
}

//endregion

//region Local Maven Installs

/**
 * Installs the final core GDXseer library to local maven. Does not depend on the build task so the build task must be called before this, if the library hasn't been built yet.
 */
val localMavenInstallCore by tasks.creating(Exec::class.java) {
    commandLine(
        "mvn",
        "install:install-file",
        "-Dfile=./build/libs/GDXseer.jar",
        "-DgroupId=io.github.niraj_rayalla",
        "-DartifactId=GDXseer",
        "-Dversion=$libraryVersion",
        "-Dpackaging=jar",
        "-DgeneratePom=true"
    )
}

/**
 * Installs the final desktop GDXseer library to local maven.
 * Does not depend on the build task [buildDesktopLibrary] so the build task must be called before this, if the library hasn't been built yet.
 * Does depend on [localMavenInstallCore] so it's not necessary to call that.
 */
val localMavenInstallDesktop by tasks.creating(Exec::class.java) {
    dependsOn(localMavenInstallCore)
    commandLine(
        "mvn",
        "install:install-file",
        "-Dfile=./GDXseer-desktop/build/libs/GDXseer-desktop.jar",
        "-DgroupId=io.github.niraj_rayalla",
        "-DartifactId=GDXseer-desktop",
        "-Dversion=$libraryVersion",
        "-Dpackaging=jar",
        "-DgeneratePom=true"
    )
}

/**
 * Installs the final Android GDXseer library to local maven.
 * Does not depend on the build task [buildAndroidLibrary] so the build task must be called before this, if the library hasn't been built yet.
 * Does depend on [localMavenInstallCore] so it's not necessary to call that.
 */
val localMavenInstallAndroid by tasks.creating(Exec::class.java) {
    dependsOn(localMavenInstallCore)
    commandLine(
        "mvn",
        "install:install-file",
        "-Dfile=./GDXseer-android/build/libs/GDXseer-android.jar",
        "-DgroupId=io.github.niraj_rayalla",
        "-DartifactId=GDXseer-android",
        "-Dversion=$libraryVersion",
        "-Dpackaging=jar",
        "-DgeneratePom=true"
    )
}

/**
 * Installs the final iOS using GL GDXseer library to local maven.
 * Does not depend on the build task [buildIOSGLLibrary] so the build task must be called before this, if the library hasn't been built yet.
 * Does depend on [localMavenInstallCore] so it's not necessary to call that.
 */
val localMavenInstallIOSGL by tasks.creating(Exec::class.java) {
    dependsOn(localMavenInstallCore)
    commandLine(
        "mvn",
        "install:install-file",
        "-Dfile=./GDXseer-ios-GL/build/libs/GDXseer-ios-GL.jar",
        "-DgroupId=io.github.niraj_rayalla",
        "-DartifactId=GDXseer-ios-GL",
        "-Dversion=$libraryVersion",
        "-Dpackaging=jar",
        "-DgeneratePom=true"
    )
}

//endregion