import java.util.Properties

val kotlinVersion: String by project
val gdxVersion: String by project
val libraryVersion: String by project

plugins {
    id("com.android.application")
}

val native by configurations.creating

android {
    namespace = "io.github.niraj_rayalla.gdxseer.example"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    sourceSets {
        this.getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            kotlin.srcDirs("src")
            aidl.srcDirs("src")
            renderscript.srcDirs("src")
            res.srcDirs("res")
            assets.srcDirs("assets")
            jniLibs.srcDirs("libs")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/robovm/ios/robovm.xml"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            proguardFiles.addAll(listOf( getDefaultProguardFile("proguard-android-optimize.txt"), File("proguard-rules.pro")))
            ndk.debugSymbolLevel = "FULL"
        }

        debug {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    dependencies {
        api(project(":Example-Basic:core"))

        api("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
        native( "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
        native( "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
        native( "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
        native( "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
        api("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
        native( "com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-armeabi-v7a")
        native( "com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-arm64-v8a")
        native( "com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-x86")
        native( "com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-x86_64")
        api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
        native( "com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
        native( "com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
        native( "com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
        native( "com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
        api("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        native( "com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-armeabi-v7a")
        native( "com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-arm64-v8a")
        native( "com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-x86")
        native( "com.badlogicgames.gdx:gdx-bullet-platform:$gdxVersion:natives-x86_64")

        api("io.github.niraj-rayalla:GDXseer-GL:$libraryVersion")
        api("io.github.niraj-rayalla:GDXseer-android:$libraryVersion")
        native("io.github.niraj-rayalla:GDXseer-android:$libraryVersion")

        implementation("androidx.appcompat:appcompat:1.2.0")
    }
}

val createNativesLibDirectories by tasks.creating {
    file("libs/armeabi/").mkdirs()
    file("libs/armeabi-v7a/").mkdirs()
    file("libs/arm64-v8a/").mkdirs()
    file("libs/x86_64/").mkdirs()
    file("libs/x86/").mkdirs()
}

// Called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
val copyAndroidNatives by tasks.creating {
    dependsOn(createNativesLibDirectories)
    doFirst {
        native.copy().files.forEach { nativesFile ->
            var outputDir: File? = null
            if (nativesFile.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
            if (nativesFile.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
            if (nativesFile.name.endsWith("natives-armeabi.jar")) outputDir = file("libs/armeabi")
            if (nativesFile.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
            if (nativesFile.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
            if(outputDir != null) {
                copy {
                    from(zipTree(nativesFile)).into(outputDir).include("*.so")
                }
                println("Copied LibGDX Android native files at path: ${outputDir!!.path} natives from jar: ${nativesFile.name}")
            }
        }
    }
}

val copyEffekseerNatives by tasks.creating {
    dependsOn(createNativesLibDirectories)
    doFirst {
        native.copy().files.forEach { nativesFile ->
            if (nativesFile.name.endsWith("GDXseer-android-${libraryVersion}.jar")) {
                println("Copying Effekseer natives from jar: ${nativesFile.name}")

                zipTree(nativesFile).files.forEach { file ->
                    var wasCopied = false

                    if (file.path.contains("arm64-v8a")) {
                        wasCopied = true
                        copy {
                            from(file.path).into("libs/arm64-v8a").include("*.so")
                        }
                    }
                    if (file.path.contains("armeabi-v7a")) {
                        wasCopied = true
                        copy {
                            from(file.path).into("libs/armeabi-v7a").include("*.so")
                        }
                    }
                    if (file.path.contains("x86")) {
                        wasCopied = true
                        copy {
                            from(file.path).into("libs/x86").include("*.so")
                        }
                    }
                    if (file.path.contains("x86_64")) {
                        wasCopied = true
                        copy {
                            from(file.path).into("libs/x86_64").include("*.so")
                        }
                    }

                    if (wasCopied) {
                        println("Copied Effekseer native files at path: ${file.path} natives from jar: ${nativesFile.name}")
                    }
                }
            }
        }
    }
}

tasks.whenTaskAdded {
    if (this.name.contains("package")) {
        this.dependsOn(copyAndroidNatives)
        this.dependsOn(copyEffekseerNatives)
    }
}

val run by tasks.creating(Exec::class) {
    val path: String
    val localProperties = project.file("../../local.properties")
    path = if (localProperties.exists()) {
        val properties = Properties()
        properties.load(localProperties.inputStream())
        val sdkDir = properties.getProperty("sdk.dir")
        sdkDir ?: "${System.getenv("ANDROID_HOME")}"
    }
    else {
        System.getenv("ANDROID_HOME")
    }

    val adb = "$path/platform-tools/adb"
    commandLine(adb, "shell", "am", "start", "-n", "io.github.niraj_rayalla.gdxseer.example/io.github.niraj_rayalla.gdxseer.example.AndroidLauncher")
}