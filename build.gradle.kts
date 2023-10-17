plugins {
    kotlin("jvm")
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
    delete(javaWrappedEffekseerDir)
    javaWrappedEffekseerDir.mkdirs()
}

/**
 * The directory all the Swig generated Java classes of the Effekseer adapter logic will be placed.
 */
val javaAdapterEffekseerDir = File("${projectDir}/src/main/java/io/github/niraj_rayalla/gdxseer/adapter_effekseer")
/**
 * Task used to create the directory pointed by [javaAdapterEffekseerDir].
 */
val createJavaAdapterEffekseerDir by tasks.creating {
    delete(javaAdapterEffekseerDir)
    javaAdapterEffekseerDir.mkdirs()
}

/**
 * The directory all the Swig generated Java classes of the Effekseer GL logic will be placed.
 */
val javaEffekseerGLDir = File("${projectDir}/GL/src/main/java/io/github/niraj_rayalla/gdxseer/effekseer_gl")
/**
 * Task used to create the directory pointed by [javaEffekseerGLDir].
 */
val createJavaEffekseerGLDir by tasks.creating {
    delete(javaEffekseerGLDir)
    javaEffekseerGLDir.mkdirs()
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
    copiedAndTransformedEffekseerDir.mkdirs()
}
/**
 * Task used to copy the Effekseer source and do any transformations before getting Swigged.
 */
val copyAndTransformEffekseerSourceForSwig by tasks.creating {
    // Delete existing copy
    delete(copiedAndTransformedEffekseerDir)
    createCopiedAndTransformedEffekseerDir

    // Get a new copy of Effekseer source
    val sourceCppDir = File("${projectDir}/Effekseer/Dev/Cpp/")
    copy {
        from(sourceCppDir)
            .into(copiedAndTransformedEffekseerDir)
            .exclude("EditorCommon")
            .exclude("EffekseerMaterial*")
            .exclude("EffekseerRendererDX*")
            .exclude("TakeScreenshots*")
            .exclude("Test*")
            .exclude("Viewer")
    }

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

/**
 * Task used to generate the Java logic that wraps the C++ Effekseer logic using Swig.
 */
val generateWrapperEffekseer by tasks.creating {
    dependsOn(copyAndTransformEffekseerSourceForSwig)

    // The effekseer logic
    dependsOn(createJavaWrappedEffekseerDir)
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

    // The effekseer adapter logic
    dependsOn(createJavaAdapterEffekseerDir)
    val adapterEffekseerGenerateTask by tasks.creating(Exec::class.java) {
        commandLine(
            "swig", "-c++", "-java",
            "-package", "io.github.niraj_rayalla.gdxseer.adapter_effekseer",
            "-outdir", javaAdapterEffekseerDir.absolutePath,
            "-o", "${projectDir}/cpp/Adapter_Effekseer_Swig.cpp",
            "${projectDir}/swig_interface/adapter_effekseer.i"
        )
    }
    dependsOn(adapterEffekseerGenerateTask)

    // The effekseer GL logic
    dependsOn(createJavaEffekseerGLDir)
    val effekseerGLGenerateTask by tasks.creating(Exec::class.java) {
        commandLine(
            "swig", "-c++", "-java",
            "-package", "io.github.niraj_rayalla.gdxseer.effekseer_gl",
            "-outdir", javaEffekseerGLDir.absolutePath,
            "-o", "${projectDir}/cpp/Effekseer_GL_Swig.cpp",
            "${projectDir}/swig_interface/effekseer_GL.i"
        )
    }
    dependsOn(effekseerGLGenerateTask)
}

//endregion