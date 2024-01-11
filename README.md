# GDXseer
This is a library written in Kotlin to be used in conjunction with libGDX (https://github.com/libgdx/libgdx) to be able to render particle effects with Effekseer (https://github.com/effekseer/Effekseer).

Currently based on versions of:

* Effekseer: `1.70e`
* libGDX: `1.12.0`

Basic example that loads, renders, and modifies the effect's color properties at runtime every few seconds:


https://github.com/niraj-rayalla/GDXseer/assets/5606501/d025ecc2-08c2-4e4f-8744-a30519c0b213



Based on https://github.com/SrJohnathan/gdx-effekseer.

All shown source code below is in Kotlin. All shown gradle script content uses the Kotlin DSL API.

# Supported Platforms

* Windows (OpenGL)
* MacOS (OpenGL)
* Android (OpenGLES)
* iOS (OpenGLES or Metal)
  * Currently doesn't render the particle effects on Metal, but you can still build the library for Metal so that you can include it with the Metal backend for libGDX for testing purposes.
  Why doesn't it work for Metal? Cause I'm dum... Someone please halp. I haven't figured out how to use Effekseer Metal with metalAngle that GDX robovm metal backend uses.

# Future Platforms

* Linux (Might already work without much if any changes, but I don't have a Linux distribution installed to build this)

# Usage

## Gradle:
Make sure you're using libGDX version 1.12.0.

### Core:
`api("io.github.niraj-rayalla:GDXseer:1.0.0")`
### Desktop:
```
api("io.github.niraj-rayalla:GDXseer-GL:1.0.0")
api("io.github.niraj-rayalla:GDXseer-desktop:1.0.0")
```
### Android:
```
val native by configurations.creating

android {
   dependencies {
      api("io.github.niraj-rayalla:GDXseer-GL:1.0.0")
      api("io.github.niraj-rayalla:GDXseer-android:1.0.0")
      native("io.github.niraj-rayalla:GDXseer-android:1.0.0")
   }
}

val copyEffekseerNatives by tasks.creating {
    dependsOn(createNativesLibDirectories)
    doFirst {
        native.copy().files.forEach { nativesFile ->
            if (nativesFile.name.endsWith("GDXseer-android-1.0.0.jar")) {
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
        this.dependsOn(copyEffekseerNatives)
    }
}
```
### iOS:
If you're using RoboVM backend:
```
api("io.github.niraj-rayalla:GDXseer-GL:1.0.0")
api("io.github.niraj-rayalla:GDXseer-ios-GL:1.0.0")
```

OR

If you're using RoboVM-Metal backend:

```
api("io.github.niraj-rayalla:GDXseer-Metal:1.0.0")
api("io.github.niraj-rayalla:GDXseer-ios-Metal:1.0.0-WIP")
```

## iOS `robovm.xml`:
If you're using RoboVM backend:
```
<frameworks>
    <framework>OpenGLES</framework>
</frameworks>
```

OR

If you're using RoboVM-Metal backend:
```
<frameworks>
    <framework>Metal</framework>
</frameworks>
```

## Source Code (How to use it with a libGDX project):
1. Call `GDXseer.init()` in the core libGDX module during the creation of the app.
2. Set the GDX asset loaders used to load the assets used by Effekseer in a GDX manner:
    ```
    val assetManager = AssetManager(fileHandleResolver)
    
    assetManager.apply {
        setLoader(EffekseerParticleSubAssetLoader.Companion.Result::class.java, null, EffekseerParticleSubAssetLoader(this.fileHandleResolver))
        
        setLoader(EffekseerParticleAssetLoader.Companion.Result::class.java, null, EffekseerParticleAssetLoader(this.fileHandleResolver))
        // Optionally pass in an instance of EffekseerIsMipMapEnabledDecider to override if an effect texture should use mimaps. By default all textures use mipmaps.
        setLoader(EffekseerParticleAssetLoader.Companion.Result::class.java, null, EffekseerParticleAssetLoader(this.fileHandleResolver, object: EffekseerIsMipMapEnabledDecider {
            override fun isMipMapEnabledForTextureFile(textureFileHandle: FileHandle): Boolean {
                ...
            }
        }))
    }
    ```
3. Create and initialize the GDXseer manager object:
    ```
    // For GL
    val gdxseerManager = GDXseerGLManager(1000, true, renderContext, OpenGLDeviceType.OpenGLES3)
    // For Metal
    val gdxseerManager = GDXseerMetalManager(1000, true, renderContext)
    
   // Initialize the manager
    gdxseerManager.initializeAll()
    -- OR -- Look at GDXseerManager initialize steps for more info
    gdxseerManager.initializeStep1CreateManagerAdapter()
    gdxseerManager.initializeStep2CreateRenderer()
    gdxseerManager.initializeStep3CreateSubRenderers()
    gdxseerManager.initializeStep4CreateLoaders()
    gdxseerManager.initializeStep5Finish()
    ```
4. Load particle effects by doing the following:
    ```
    val particleEffect = GDXseerParticleEffect(gdxseerManager)
    // For immediate loading
    particleEffect.syncLoad(assetManager, "data/tu.efk")
    // For asynchronous loading
    particleEffect.asyncLoad(assetManager, "data/tu.efk", object: GDXseerParticleEffect.Companion.LoadingListener {
        override fun onEffectSuccessfullyLoaded(gdXseerParticleEffect: GDXseerParticleEffect) {
            ...
        }
   
        override fun onEffectFailedToLoad(gdXseerParticleEffect: GDXseerParticleEffect, throwable: Throwable?) {
            ...
        }
    })
    ```
5. Play, modify, and stop effects by doing the following:
    ```
    particleEffect.play()
    // Do any operations such as setting the color
    (particleEffect.rootNode.getChild(0) as GDXseerEffectNodeSprite).fixed.value = io.github.niraj_rayalla.gdxseer.effekseer.Color(r, g, b, a)
    particleEffect.stop()
    ```
6. Rendering the particle effects:
   ```
   fun render() {
      ...
   
      manager.draw(Gdx.graphics.getDeltaTime())
   
      --OR--
   
      manager.update(Gdx.graphics.getDeltaTime())
      // Can call this draw as many times as needed. For example one draw for post-processing and then the normal draw.
      manager.drawAfterUpdate()
   }
   ```  
7. Dispose the effects and manager when no longer needed:
    ```
    // Dispose a single effect
    particleEffect.dispose()
    
    // Dispose entire manager
    gdxseerManager.dispose()
    ```
   
### Runtime Caveats
* When using the GL renderer on iOS, and creating GDXseerGLManager with OpenGLDeviceType.OpenGLES3 causes a crash when rendering. Using OpenGLDeviceType.OpenGLES2 doesn't cause
the crash. I'm don't have any clue as to why this happens. 

# Examples
Look in path `/examples` for all the example sub-projects available.

Currently there's just one example called `Basic` with one sub-project for each of: desktop, Android, and iOS.
It shows how to initialize GDXseer, load a particle effect, render it, and also change color properties at runtime every few seconds.

This repository also includes IntelliJ run configurations for quickly running each of the examples sub-projects.

# Building
If you want to build the libraries yourself, follow these steps, or not, your choice:

1. Install latest Swig (https://www.swig.org/download.html)
2. Install Cmake (https://cmake.org/download/) and the desired C++ compiler, such as g++ 
3. Clone this repository
4. Run `git submodule update --init --recursive` to pull the Effekseer repository along with all its submodules.
5. Run `./gradlew publishToMavenLocal` to build all sub-projects, or call one of the specific build Gradle tasks such as `./gradlew :buildDesktopLibrary`. The following are all possible build tasks:
    * `buildDesktopLibrary`
    * `buildAndroidLibrary`
    * `buildIOSGLLibrary`
    * `buildIOSMetalLibrary`

### Building Caveats
* For IOS building, set `iOS_JAVA_HOME` (optional) and `iOS_CODE_SIGN_IDENTITY` in local.properties file. 
  * `iOS_JAVA_HOME` line should point to the JDK path to use for iOS building and be like this `iOS_JAVA_HOME=/Users/username/jdk-13.0.2.jdk/Contents/Home/`. If not provided `JAVA_HOME` is used. JDK 13 is used for the built libraries.
  * `iOS_CODE_SIGN_IDENTITY` is the identity you would pass to `codesign -s ... framework` to sign the iOS frameworks.

# Project Structure
How is the project structured? I would like to know as well. Nvm, it's right here:

1. The root module contains the common GDXseer logic as well as the Swig generated Java of the core Effekseer logic which all the sub-modules use.
2. Then, there are two modules that directly use the root module `GL` and `Metal`, which contain the Swig generated and written Kotlin classes for the renderer being used.
3. Finally, we have the modules that build into the library (jar) file, which depend on `GL` or `Metal` module:
   * `GDXseer-desktop`
   * `GDXseer-android`
   * `GDXseer-ios-GL`
   * `GDXseer-ios-Metal`

### `/build.gradle.kts`:
Contains tasks (named above) that build the Effekseer C++ code, bridge C++ code for GDXseer, for the appropriate platform, and then build the jar file that contains the built shared library.
Also contains Maven publishing tasks.

### Swig (in `/swig_interface/`)
Contains the Swig interface files, that determine how Swig should generate the Java wrapper classes from the C++ code.
The highest level Swig interface files are:
* `effekseer.i` which generates `/cpp/Effekseer_Swig.cpp` and the Java classes in `/src/main/java/io/github/niraj_rayalla/gdxseer/effekseer/`
* `adapter_effekseer.i` which generates `/cpp/Adapter_Effekseer_Swig.cpp` and the Java classes in `/src/main/java/io/github/niraj_rayalla/gdxseer/adapter_effekseer/`
* `effekseer_GL.i` which generates `/cpp/Effekseer_GL_Swig.cpp` and the Java classes in `/GL/src/main/java/io/github/niraj_rayalla/gdxseer/effekseer_gl/`
* `effekseer_Metal.i` which generates `/cpp/Effekseer_Metal_Swig.mm` and the Java classes in `/Metal/src/main/java/io/github/niraj_rayalla/gdxseer/effekseer_metal/`

### Cmake (in `/CMakeLists.txt` and `/cpp/CMakeLists.txt`)
Used to build the swig generated C++ and the non-generated Effekseer C++ logic.
Builds into one of these directories for the corresponding platform being built for:
`/cmake-windows/`
`/cmake-macos/`
`/cmake-linux/`
`/android-build/`
`/cmake-ios-device-gl/`
`/cmake-ios-device-metal/`
`/cmake-ios-simulator-gl/`
`/cmake-ios-simulator-metal/`

### Jar
Each module is a JVM library. When running the `:jar` task for that module, it builds using the Swig generation, and Cmake building described above.

Then, the jar is built with the built C++ shared libraries included in it.

For iOS, the shared library files are not included directly in the jar like for the other platforms. Instead the frameworks for device and simulator are created from the built shared libraries first, then the XCFramework from those frameworks, and that is what's included in the jar file.

# License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Contributions
If you would like to contribute to this project, please feel free to send pull requests.
