package io.github.niraj_rayalla.gdxseer.utils

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle

/**
 * Extends [FileHandle] so that the constructor with the file type is public.
 */
private class FileHandleWithType(fileName: String, type: Files.FileType): FileHandle(fileName, type)


/**
 * @return A new [FileHandle] with the new path but the same file type as this.
 */
fun FileHandle.withNewPath(newPath: String): FileHandle {
    return FileHandleWithType(newPath, this.type())
}