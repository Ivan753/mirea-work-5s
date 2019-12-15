%{
  #include <stdio.h>
  extern FILE *yyin;
  extern int yylineno;
  //extern int ch;
  extern char *yytext;
  void yyerror(char *);
  extern int yylex (void);
%}
%token TYPE FUNCTION LBRACKET RBRACKET COMMA SPACE IDENTITY PLUS NUMBER
%right ASSIGN
%left CMP
%%
  program:
    statement { printf("\nПрограмма\n"); }
    | program statement { printf("\nПрограмма\n"); }
  function:
    TYPE SPACE FUNCTION LBRACKET TYPE SPACE IDENTITY COMMA SPACE TYPE SPACE IDENTITY RBRACKET
    { printf("\nФункция\n"); }
  statement:
    expr { printf("\nВысказывание\n"); }
    | function { printf("\nВысказывание\n"); }
  expr:
    IDENTITY ASSIGN expr_cmp { printf("\nПрисваивание\n"); }
  expr_cmp:
    prim_expr PLUS prim_expr
    | prim_expr { printf("\nВыражение\n"); }
  prim_expr:
    IDENTITY
    | NUMBER
    | LBRACKET expr RBRACKET
    { printf("\nprimary expression\n"); }
%%

void yyerror(char *errmsg) {
  fprintf(stderr, "%s (%d): %s\n", errmsg, yylineno, yytext);
}


int main(int argc, char **argv) {
  if(argc < 2) {
    printf("\nНедостаточно аргументов. Пожалуйста, введите имя файла. \n");
    return -1;
  }
  if((yyin = fopen(argv[1], "r")) == NULL) {
    printf("\nНе удалось открыть файл %s.\n", argv[1]);
    return -1;
  }
  yylineno = 1;
  yyparse();
  return 0;
}
