%typemap(jni) unsigned char *UBYTE "jbyteArray"
%typemap(jtype) unsigned char *UBYTE "byte[]"
%typemap(jstype) unsigned char *UBYTE "byte[]"
%typemap(in) unsigned char *UBYTE {
  $1 = (unsigned char *) JCALL2(GetByteArrayElements, jenv, $input, 0);
}

%typemap(argout) unsigned char *UBYTE {
  JCALL3(ReleaseByteArrayElements, jenv, $input, (jbyte *) $1, 0);
}

%typemap(javain) unsigned char *UBYTE "$javainput"

/* Prevent default freearg typemap from being used */
%typemap(freearg) unsigned char *UBYTE ""