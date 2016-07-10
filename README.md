# Compiler-for-Algol68


● Gramática Sintática


Program ::= BEGIN ( ProgramCommand )* END

ProgramCommand ::= Func_Dec | Command

Var_Dec ::= TYPE ID ( , ID )*

Func_Dec ::= PROC ID = ( ( Var_Dec ( , Var_Dec)*)? ) (TYPE | VOID) :

BEGIN ( Command )* END

Expression ::= Arithmetic_Exp ( ( BOOL_OP | EQ_COMP ) Arithmetic_Exp)?

Arithmetic_Exp ::= Term (( + | ­ ) Term)*

Term ::= Factor (( * | / ) Factor)*

Factor ::= NUMBER | ID (Arguments?)? | ( Arithmetic_Exp ) | LOGIC

Arguments ::= Expression (, Expression)*

Command ::= Var_Dec ;

| IF Expression THEN ( Command )* (ELSE (Command )* )? FI

| WHILE Expression DO (Command )* OD

| BREAK ;

| CONTINUE ;

| ID ( := Expression | ( Arguments? ) ) ;

| PRINT( Expression ) ;

| RETURN Expression ;



● Gramática Léxica


LETTER ­> [A­Za­z]

DIGIT ­> [0­9]

NUMBER ­> DIGIT+

ID ­> LETTER(LETTER | DIGIT)*

TYPE ­> INT | BOOL

LOGIC ­> TRUE | FALSE

EQ_COMP ­> = | /=

BOOL_OP ­> < | <= | > | >=

TOKEN ­> TYPE | IF | ELSE | THEN | DO | FI | OD | PROC | WHILE | VOID

| BEGIN | END | LOGIC | ID | NUMBER | BREAK | CONTINUE

| PRINT | RETURN | BOOL_OP | EQ_COMP | := | + | ­ | * | / | ( | )

| ; | : | , | EOT



● Esta linguagem aceita blocos de comentários, sendo esses marcados pelo caractere

‘#’ ​no início e no final do bloco.

● Os tokens representados na gramática sintática que realmente fazem parte da

linguagem estão representados na cor vermelha.
