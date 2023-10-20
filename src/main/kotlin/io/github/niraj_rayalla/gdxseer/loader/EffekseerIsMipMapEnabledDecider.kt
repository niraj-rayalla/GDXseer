package io.github.niraj_rayalla.gdxseer.loader

import com.badlogic.gdx.files.FileHandle

/**
 * Used to determine if a texture at the given texture path should utilize mipmaps.
 */
interface EffekseerIsMipMapEnabledDecider {
    /**
     * @return True, if the texture at the given [FileHandle] should utilize mipmaps, otherwise false.
     */
    fun isMipMapEnabledForTextureFile(textureFileHandle: FileHandle): Boolean
}