%option noyywrap yylineno
%{
#define TYPE 1
#define FUNCTION 2
#define LBRACKET 3
#define RBRACKET 4
#define COMMA 5
#define SPACE 6
#define IDENTITY 7
#define UNKNOWN 8
%}
id [a-zA-Z_][a-zA-Z0-9_]*
%%
"int" { return TYPE; }
"function" { return FUNCTION; }
"(" { return LBRACKET; }
")" { return RBRACKET; }
"," { return COMMA; }
" " { return SPACE; }
{id} { return IDENTITY; }
. {printf("\nНеизвестный символ [%c] !\n",yytext[0]); return UNKNOWN; }
%%
int main(int argc, char **argv) {
  // ищем файл
  if(argc < 2) {
    printf("\nНедостаточно аргументов. Пожалуйста, укажите входной файл.\n");
    return -1;
  }
  // пробуем открыть файл
  if((yyin = fopen(argv[1], "r")) == NULL) {
    printf("\nНе удалось открыть файл %s.\n", argv[1]);
    return -1;
  }
  // перебираем все лексемы
  // Особенности:
  // 1. yylineno инкрементируется автоматические с помощью
  // опции yylineno (доступно во flex)
  // 2. при достижении конца файла функция заканчивает работу
  // с пмощью опции noyywrap
  //
  // yylex возвращает токен
  yylineno = 1;
  while (1) {
    int token = yylex();
    if (token == 0)
      break;
    printf("%d ", token);
  }
  fclose(yyin);
  return 0;
}
