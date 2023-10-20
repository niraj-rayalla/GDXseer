package io.github.niraj_rayalla.gdxseer.utils

/**
 * Contains some functions to help with processing of file paths without using the Java classes
 * that might not be available on the current platform, so that this library works for all necessary platforms.
 */
object FilePath {
    /**
     * @param path The path to get the parent of.
     * @return The parent path of [path].
     */
    fun getParentPath(path: String): String {
        var parentPath = ""

        // First get all the components.
        val pathComponents = path.split('/')

        if (pathComponents.isNotEmpty()) {
            // Now build the normalized path string based on the kept components
            val parentPathBuilder = StringBuilder(path.length)
            if (path.startsWith("/")) {
                parentPathBuilder.append("/")
            }
            var i = 0
            while (i < pathComponents.size) {
                if (i < pathComponents.size - 1) {
                    if (i > 0) {
                        parentPathBuilder.append("/")
                    }
                    parentPathBuilder.append(pathComponents[i])
                }
                i += 1
            }
            if (path.endsWith("/")) {
                parentPathBuilder.append("/")
            }
            parentPath = parentPathBuilder.toString()
        }

        return parentPath
    }

    /**
     * Updates [componentIndicesToKeep] for the given [pathComponents] so that the kept components represent
     * the normalized equivalent path.
     */
    private fun calculatePathComponentsToKeep(pathComponents: List<String>, componentIndicesToKeep: BooleanArray) {
        var numComponentsToRemoveAtCursor = 0
        var i = pathComponents.size - 1
        while (i >= 0) {
            val componentValue = pathComponents[i]
            if (componentValue == ".") {
                componentIndicesToKeep[i] = false
            } else if (componentValue == "..") {
                componentIndicesToKeep[i] = false
                numComponentsToRemoveAtCursor += 1
            } else {
                if (numComponentsToRemoveAtCursor == 0) {
                    componentIndicesToKeep[i] = true
                } else {
                    numComponentsToRemoveAtCursor -= 1
                    componentIndicesToKeep[i - 1] = false
                }
            }
            i -= 1
        }
    }

    /**
     * @param path The path to normalized.
     * @return The normalized path (no "../" or "./") string of [path].
     */
    fun getNormalizedPath(path: String): String {
        var normalizedPath = ""
        // First get all the components.
        val pathComponents = path.split('/')
        if (pathComponents.isNotEmpty()) {
            // Go through each component and track if it should be kept for adding to the normalized path
            val componentIndicesToKeep = BooleanArray(pathComponents.size)
            calculatePathComponentsToKeep(pathComponents, componentIndicesToKeep)

            // Now build the normalized path string based on the kept components
            val normalizedPathBuilder = StringBuilder(path.length)
            if (path.startsWith("/")) {
                normalizedPathBuilder.append("/")
            }
            var i = 0
            while (i < pathComponents.size) {
                if (componentIndicesToKeep[i]) {
                    if (i > 0) {
                        normalizedPathBuilder.append("/")
                    }
                    normalizedPathBuilder.append(pathComponents[i])
                }
                i += 1
            }
            if (path.endsWith("/")) {
                normalizedPathBuilder.append("/")
            }
            normalizedPath = normalizedPathBuilder.toString()
        }
        return normalizedPath
    }
}