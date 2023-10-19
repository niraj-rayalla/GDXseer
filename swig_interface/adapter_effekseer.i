%include "shared_swig_declaration.i"

// Import (NOT include cause that would duplicate the generated logic from effekseer.i) everything generated by effekseer.i
%import "effekseer.i"

// Make sure the generated adapter classes have the imported package
%typemap(javaimports) SWIGTYPE %{
import io.github.niraj_rayalla.gdxseer.effekseer.*;
%}

// Make sure the generated JNI class has the imported package
%pragma(java) jniclassimports=%{
import io.github.niraj_rayalla.gdxseer.effekseer.*;
%}

%module AdapterEffekseer
%{
    const int FillMeInAsSizeCannotBeDeterminedAutomatically = 16;

    #include "EffekseerAdapters/RefWrappers.h"
    #include "EffekseerAdapters/GDXMatrixAdapter.h"
    #include "EffekseerAdapters/EffekseerEffectAdapter.h"
    #include "EffekseerAdapters/EffekseerManagerAdapter.h"
%}

// Stop ignoring
%rename("%s") "";

%include "/cpp/EffekseerAdapters/RefWrappers.h"
%include "/cpp/EffekseerAdapters/GDXMatrixAdapter.h"
%include "/cpp/EffekseerAdapters/EffekseerEffectAdapter.h"
%include "/cpp/EffekseerAdapters/EffekseerManagerAdapter.h"