package io.github.niraj_rayalla.gdxseer.desktop

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.ProviderNotFoundException
import java.nio.file.StandardCopyOption

/**
 * Used to load the native libraries needed for GDXseer.
 */
object NativeLibsLoader {

    //region Constants

    private const val MIN_PREFIX_LENGTH = 3
    private const val NATIVE_FOLDER_PATH_PREFIX = "nativeutils"

    //endregion

    //region State

    private var temporaryDir: File? = null

    //endregion

    //region Private Methods


    /**
     * @return True, if the file system this desktop library is running on is posix compliant.
     */
    private fun isPosixCompliant(): Boolean {
        return try {
            FileSystems.getDefault()
                .supportedFileAttributeViews()
                .contains("posix")
        } catch (e: FileSystemNotFoundException) {
            false
        } catch (e: ProviderNotFoundException) {
            false
        } catch (e: SecurityException) {
            false
        }
    }

    /**
     * @return The [File] which is a temporary directory with the given prefix.
     */
    @Throws(IOException::class)
    private fun createTempDirectory(prefix: String): File {
        val tempDir = System.getProperty("java.io.tmpdir")
        val generatedDir = File(tempDir, prefix + System.nanoTime())
        if (!generatedDir.mkdir()) throw IOException("Failed to create temp directory " + generatedDir.name)
        return generatedDir
    }

    @Throws(IOException::class)
    private fun loadLibraryFromJar(path: String) {
        require(path.startsWith("/")) { "The path has to be absolute (start with \"/\")." }

        // Obtain filename from path
        val parts = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }
        val filename = if (parts.size > 1) parts[parts.size - 1] else null

        // Check if the filename is okay
        require(filename!= null && filename.length >= MIN_PREFIX_LENGTH) { "The filename has to be at least 3 characters long." }

        // Copy the native libraries to a temporary file
        if (temporaryDir == null) {
            temporaryDir = createTempDirectory(NATIVE_FOLDER_PATH_PREFIX).apply {
                deleteOnExit()
            }
        }
        val temp = File(temporaryDir, filename)
        try {
            NativeLibsLoader::class.java.getResourceAsStream(path).use { inputStream ->
                Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        } catch (e: IOException) {
            temp.delete()
            throw e
        } catch (e: NullPointerException) {
            temp.delete()
            throw FileNotFoundException("File $path was not found inside GDXseer JAR.")
        }

        // Load the copied libraries
        try {
            System.load(temp.absolutePath)
        } finally {
            if (isPosixCompliant()) {
                // Assume POSIX compliant file system, can be deleted after loading
                temp.delete()
            } else {
                // Assume non-POSIX, and don't delete until last file descriptor closed
                temp.deleteOnExit()
            }
        }
    }

    //endregion

    //region Public Methods

    /**
     * Loads the native libraries needed for GDXseer.
     */
    fun load(): Boolean {
        return try {
            when (OSChecker.operatingSystem) {
                OSChecker.OperatingSystem.Windows -> loadLibraryFromJar("/GDXseer_Effekseer.dll")
                OSChecker.OperatingSystem.MacOS -> loadLibraryFromJar("/libGDXseer_Effekseer.dylib")
                OSChecker.OperatingSystem.Linux -> loadLibraryFromJar("/libGDXseer_Effekseer.so")
                OSChecker.OperatingSystem.Unknown -> throw RuntimeException("Can't load GDXseer native libraries for unknown operating system!")
            }
            true
        }
        catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    //endregion
}