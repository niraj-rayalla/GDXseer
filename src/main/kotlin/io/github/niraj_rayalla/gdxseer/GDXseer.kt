package io.github.niraj_rayalla.gdxseer

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.reflect.ClassReflection

/**
 * Used to run any initialization needed for GDXseer.
 */
object GDXseer {

    //region Private Methods

    private fun loadNativeLibsUsingLoader(appType: ApplicationType) {
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
            nativeLibsLoaderCompanionClass.getMethod("load").invoke(nativeLibsLoaderClassCompanionInstance)
        }
        catch (e: Exception) {
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
            ApplicationType.iOS -> throw UnsupportedOperationException("GDXseer is not yet supported for iOS.")
            else -> throw UnsupportedOperationException("GDXseer is not supported for the operating system it's being run on.")
        }
    }

    //endregion
}