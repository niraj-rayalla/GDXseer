package io.github.niraj_rayalla.gdxseer

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.reflect.ClassReflection

/**
 * Used to run any initialization needed for GDXseer.
 */
@Suppress("unused")
object GDXseer {

    //region Private Methods

    private fun loadNativeLibsUsingLoader(appType: ApplicationType, iOSIsOnSimulator: Boolean? = null) {
        val platformType: String = if (appType == ApplicationType.iOS) "ios" else "desktop"
        val className = "io.github.niraj_rayalla.gdxseer.$platformType.NativeLibsLoader"

        try {
            val nativeLibsLoaderClass = ClassReflection.forName(className)
            val nativeLibsLoaderCompanionClass = ClassReflection.forName("${className}\$Companion")

            // Companion instance
            val nativeLibsLoaderClassCompanionField = nativeLibsLoaderClass.getDeclaredField("Companion")
            nativeLibsLoaderClassCompanionField.isAccessible = true
            val nativeLibsLoaderClassCompanionInstance = nativeLibsLoaderClassCompanionField.get(null)

            // Companion class
            if (iOSIsOnSimulator != null) {
                nativeLibsLoaderCompanionClass.getMethod("load", Boolean::class.java).invoke(nativeLibsLoaderClassCompanionInstance, iOSIsOnSimulator)
            }
            else {
                nativeLibsLoaderCompanionClass.getMethod("load").invoke(nativeLibsLoaderClassCompanionInstance)
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to load the native libraries needed for GDXseer with GDX app type of $appType.", e)
        }
    }

    //endregion

    //region Public Methods

    /**
     * Used to initialize everything needed for GDXseer. Throws exception if failed to initialize due to some error or due to the library not
     * being supported on the operating system being run on.
     */
    fun init() {
        when (val appType = Gdx.app.type) {
            ApplicationType.Android -> System.loadLibrary("GDXseer_Effekseer")
            ApplicationType.Desktop, ApplicationType.HeadlessDesktop -> loadNativeLibsUsingLoader(appType)
            ApplicationType.iOS -> {
                // No need to manually load the native library directly using java, since RoboVM will automatically load the generated frameworks.
                // Uncomment to manually load the native library using Java like in the desktop app.
                // loadNativeLibsUsingLoader(appType, iOSIsOnSimulator)
            }
            else -> throw UnsupportedOperationException("GDXseer is not supported for the operating system it's being run on.")
        }
    }

    //endregion
}