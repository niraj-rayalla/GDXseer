plugins {
    kotlin("jvm")
}

val gdxVersion: String by project

dependencies {
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
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

//region CMAKE Build

/**
 * Builds the desktop GDXseer library.
 */
val buildDesktopLibrary by tasks.creating(Exec::class.java) {
    dependsOn(configureDesktopBuilding)
    dependsOn(tasks.jar)
    tasks.jar.get().shouldRunAfter(configureDesktopBuilding)

    commandLine(
        "cmake",
        "--build", desktopCmakeBuildDir.absolutePath,
        "--config", "Release"
    )
}

//endregion