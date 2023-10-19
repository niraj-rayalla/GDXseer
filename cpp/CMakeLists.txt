cmake_minimum_required(VERSION 3.16)

project(GDXseer_Effekseer)

set(BUILD_EXAMPLES OFF)
set(BUILD_DX12 OFF)
set(CMAKE_EXE_LINKER_FLAGS "-ljvm")

# Add the original Effekseer source directory
add_subdirectory(${CMAKE_CURRENT_SOURCE_DIR}/../Effekseer ${CMAKE_CURRENT_BINARY_DIR}/Effekseer)

# Create the library
add_library(
        ${PROJECT_NAME}
        SHARED
        Shared_Swig.cpp

        Effekseer_Swig.cpp

        EffekseerAdapters/RefWrappers.cpp
        EffekseerAdapters/GDXMatrixAdapter.cpp
        EffekseerAdapters/EffekseerManagerAdapter.cpp
        EffekseerAdapters/EffekseerEffectAdapter.cpp
        Adapter_Effekseer_Swig.cpp

        EffekseerAdapters/GL/EffekseerGLManagerAdapter.cpp
        Effekseer_GL_Swig.cpp
)

# Need JNI
find_package(JNI)

# Definitions
if (USE_OPENGLES2)
    add_definitions(-D__EFFEKSEER_RENDERER_GLES2__)
endif()

if (USE_OPENGLES3)
    add_definitions(-D__EFFEKSEER_RENDERER_GLES3__)
endif()

if (USE_OPENGL2)
    add_definitions(-D__EFFEKSEER_RENDERER_GL2__)
endif()

# Include directories
include_directories(
        ${CMAKE_CURRENT_SOURCE_DIR}
        EffekseerAdapters/
        EffekseerAdapters/GL/
)
target_include_directories(
        ${PROJECT_NAME}
        PUBLIC
        ../Effekseer/Dev/Cpp/Effekseer/
        ../Effekseer/Dev/Cpp/EffekseerRendererGL/
        EffekseerAdapters/
        EffekseerAdapters/GL/
        ${JNI_INCLUDE_DIRS}
)

# Link the sub-libraries needed for this project
# First get any extra Apple frameworks to link if building for MacOS or iOS
if(APPLE)
    if ((DEFINED CMAKE_SYSTEM_NAME) AND (${CMAKE_SYSTEM_NAME} STREQUAL "iOS"))
        set(APPLE_FRAMEWORKS "-framework OpenGLES")
    else()
        set(APPLE_FRAMEWORKS "-framework OpenGL")
    endif()
elseif()
    set(APPLE_FRAMEWORKS "")
endif()
# Link the libraries
target_link_libraries(
        ${PROJECT_NAME}
        PRIVATE
        ${APPLE_FRAMEWORKS}
        Effekseer
        EffekseerRendererGL
)

# Build Effekseer and renderer logic first before this project
add_dependencies(
        ${PROJECT_NAME}
        Effekseer
        EffekseerRendererGL
)