package io.github.niraj_rayalla.gdxseer.ios

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.ProviderNotFoundException

/**
 * Used to load the native libraries needed for GDXseer.
 */
@Suppress("unused")
class NativeLibsLoader {

    companion object {

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
                NativeLibsLoader::class.java.getResourceAsStream(path)!!.use { inputStream ->
                    FileOutputStream(temp).use { outputStream ->
                        val buffer = ByteArray(8 * 1024)
                        var bytesRead: Int
                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                    }
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
        fun load(iOSIsOnSimulator: Boolean): Boolean {
            return try {
                if (iOSIsOnSimulator) {
                    loadLibraryFromJar("/libGDXseer_Effekseer_simulator.dylib")
                }
                else {
                    loadLibraryFromJar("/libGDXseer_Effekseer_device.dylib")
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
}