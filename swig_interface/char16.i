%{

int char16_len(const char16_t* s)
{
	int cnt = 0;
	while(*s++) cnt++;
	return cnt;
}
%}

%include "char16_typemap.i"