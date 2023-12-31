cmake_minimum_required(VERSION 3.16)

project(GDXseer_Effekseer)

OPTION(GDXSEER_RENDERER "The type of backend renderer to use for GDXseer. Can be GL or Metal." "GL")
OPTION(iOS_JAVA_HOME "The JDK home to use for iOS when trying to find JNI. Example: /Users/username/jdk-13.0.2.jdk/Contents/Home/")
MESSAGE("Using GDXseer Backend Renderer: " ${GDXSEER_RENDERER})

set(BUILD_EXAMPLES OFF)
set(BUILD_DX12 OFF)
set(CMAKE_EXE_LINKER_FLAGS "-ljvm")

# Add the original Effekseer source directory
add_subdirectory(${CMAKE_CURRENT_SOURCE_DIR}/../Effekseer ${CMAKE_CURRENT_BINARY_DIR}/Effekseer)

if (${GDXSEER_RENDERER} STREQUAL "Metal")
    set(GDXSEER_RENDERER_C_FILE_TYPE mm)
else()
    set(GDXSEER_RENDERER_C_FILE_TYPE cpp)
endif()

# Create the library
add_library(
        ${PROJECT_NAME}
        SHARED
        Effekseer_Swig.cpp

        EffekseerAdapters/RefWrappers.cpp
        EffekseerAdapters/GDXMatrixAdapter.cpp
        EffekseerAdapters/EffekseerManagerAdapter.cpp
        EffekseerAdapters/EffekseerEffectAdapter.cpp
        Adapter_Effekseer_Swig.cpp

        EffekseerAdapters/${GDXSEER_RENDERER}/Effekseer${GDXSEER_RENDERER}ManagerAdapter.${GDXSEER_RENDERER_C_FILE_TYPE}
        Effekseer_${GDXSEER_RENDERER}_Swig.${GDXSEER_RENDERER_C_FILE_TYPE}
)

# Need JNI
if ((DEFINED CMAKE_SYSTEM_NAME) AND (${CMAKE_SYSTEM_NAME} STREQUAL "iOS"))
    set(JAVA_AWT_LIBRARY ${iOS_JAVA_HOME}/lib/libjawt.dylib)
    set(JAVA_JVM_LIBRARY ${iOS_JAVA_HOME}/lib/server/libjvm.dylib)
    set(JAVA_INCLUDE_PATH ${iOS_JAVA_HOME}/include)
    set(JAVA_INCLUDE_PATH2 ${iOS_JAVA_HOME}/include/darwin)
    set(JAVA_AWT_INCLUDE_PATH ${iOS_JAVA_HOME}/include)
endif()
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
        EffekseerAdapters/${GDXSEER_RENDERER}/
)
target_include_directories(
        ${PROJECT_NAME}
        PUBLIC
        ../
        ../Effekseer/Dev/Cpp/Effekseer/
        ../Effekseer/Dev/Cpp/EffekseerRenderer${GDXSEER_RENDERER}/
        ../Effekseer/Dev/Cpp/3rdParty/LLGI/src/
        ../Effekseer/Dev/Cpp/3rdParty/LLGI/src/Metal/
        ../Effekseer/Dev/Cpp/EffekseerRendererLLGI/
        EffekseerAdapters/
        EffekseerAdapters/${GDXSEER_RENDERER}/
        ${JNI_INCLUDE_DIRS}
)

# Link the sub-libraries needed for this project
# First get any extra Apple frameworks to link if building for MacOS or iOS
if(APPLE)
    if (${GDXSEER_RENDERER} STREQUAL "GL")
        if ((DEFINED CMAKE_SYSTEM_NAME) AND (${CMAKE_SYSTEM_NAME} STREQUAL "iOS"))
            set(APPLE_FRAMEWORKS "-framework OpenGLES")
        else()
            set(APPLE_FRAMEWORKS "-framework OpenGL")
        endif()
    else()
        set(APPLE_FRAMEWORKS "-framework Foundation -framework Metal")
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
        EffekseerRenderer${GDXSEER_RENDERER}
)

# Build Effekseer and renderer logic first before this project
add_dependencies(
        ${PROJECT_NAME}
        Effekseer
        EffekseerRenderer${GDXSEER_RENDERER}
)