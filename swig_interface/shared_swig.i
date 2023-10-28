%include "arrays_java.i"
%include "operator_overload.i"

%include "char16.i"
%include "stdint.i"
%include "typemaps.i"
%include "various.i"
%include "std_vector.i"
%include "std_array.i"
%include "carrays.i"
%include "./ibyte.i"

//%include "swigmove.i"

// Some applies
%apply float[] {float *};
%apply char *BYTE { char* data };
%apply unsigned char *UBYTE { unsigned char *data };