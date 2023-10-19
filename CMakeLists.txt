cmake_minimum_required(VERSION 3.16)

set(CMAKE_POSITION_INDEPENDENT_CODE ON)

project(GDXseer LANGUAGES C CXX)

set (CMAKE_CXX_STANDARD 14)

option(USE_MSVC_RUNTIME_LIBRARY_DLL "Whether Multithreaded DLL" OFF)

# Flags
if (WIN32)
    if(USE_MSVC_RUNTIME_LIBRARY_DLL)
        cmake_policy(SET CMP0091 NEW)
        set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>DLL")
    else()
        cmake_policy(SET CMP0091 NEW)
        set(CMAKE_MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>")
    endif()
    message(${CMAKE_MSVC_RUNTIME_LIBRARY})
endif()

if(UNIX AND NOT APPLE)
    set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--start-group ")
endif()

if(ANDROID)
    set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--start-group ")
endif()

IF(APPLE)
    set(CMAKE_THREAD_LIBS_INIT "-lpthread")
    set(CMAKE_HAVE_THREADS_LIBRARY 1)
    set(CMAKE_USE_WIN32_THREADS_INIT 0)
    set(CMAKE_USE_PTHREADS_INIT 1)
    set(THREADS_PREFER_PTHREAD_FLAG ON)
ENDIF()

add_subdirectory(cpp)