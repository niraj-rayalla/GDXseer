package io.github.niraj_rayalla.gdxseer.desktop

/**
 * Used to check the OS this desktop library is running on.
 */
object OSChecker {

    /**
     * All the type of operating systems that can be represented by [OSChecker].
     */
    enum class OperatingSystem {
        Windows, MacOS, Linux, Unknown
    }

    /**
     * The representation of the operating system this desktop library is running on.
     */
    val operatingSystem: OperatingSystem by lazy {
        val os = System.getProperty("os.name", "generic").lowercase()
        when {
            os.indexOf("win") >= 0 -> OperatingSystem.Windows
            os.indexOf("mac") >= 0 || os.indexOf("darwin") >= 0 -> OperatingSystem.MacOS
            os.indexOf("nux") >= 0 -> OperatingSystem.Linux
            else -> OperatingSystem.Unknown
        }
    }
}