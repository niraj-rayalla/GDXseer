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

%module EffekseerMetal
%{
#include <Effekseer/Dev/Cpp/EffekseerRendererMetal/EffekseerRendererMetal.h>
#include "EffekseerAdapters/Metal/EffekseerMetalManagerAdapter.h"
%}

// Stop ignoring
%rename("%s") "";

%include "/cpp/EffekseerAdapters/Metal/EffekseerMetalManagerAdapter.h"