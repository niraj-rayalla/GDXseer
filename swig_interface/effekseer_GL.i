// Import (NOT include) everything generated by adapter_effekseer.i
%import "adapter_effekseer.i"

// Make sure the generated adapter classes have the imported package
%typemap(javaimports) SWIGTYPE %{
import io.github.niraj_rayalla.gdxseer.effekseer.*;
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.*;
%}

// Make sure the generated JNI class has the imported package
%pragma(java) jniclassimports=%{
import io.github.niraj_rayalla.gdxseer.effekseer.*;
import io.github.niraj_rayalla.gdxseer.adapter_effekseer.*;
%}

%module EffekseerGL
%{
#include "/Effekseer/Dev/Cpp/EffekseerRendererGL/EffekseerRenderer/EffekseerRendererGL.Base.Pre.h"
#include "/EffekseerAdapters/GL/EffekseerGLManagerAdapter.h"
%}

// Stop ignoring
%rename("%s") "";

%rename("OpenGLDeviceType", fullname=1) "EffekseerRendererGL::OpenGLDeviceType";
%include "Effekseer/Dev/Cpp/EffekseerRendererGL/EffekseerRenderer/EffekseerRendererGL.Base.Pre.h"

%include "/cpp/EffekseerAdapters/GL/EffekseerGLManagerAdapter.h"