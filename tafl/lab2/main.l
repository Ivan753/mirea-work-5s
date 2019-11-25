%option noyywrap yylineno
%{
#include "y.tab.h"
#define UNKNOWN 400
%}
letter [a-zA-Z_]
digit [0-9]
%%
":=" { return ASSIGN; }
"+" { return PLUS; }
"int" { return TYPE; }
"function" { return FUNCTION; }
"(" { return LBRACKET; }
")" { return RBRACKET; }
"," { return COMMA; }
" " { return SPACE; }
{letter}({letter}|{digit})* { return IDENTITY; }
({digit})+ { return NUMBER; }
. { printf("Unknown character [%c]\n",yytext[0]); return UNKNOWN; }
%%
