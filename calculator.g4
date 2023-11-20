grammar calculator;

EQ : '=' ;
SEMI : ';' ;
PLUS : '+' ;
MULT : '*' ;
LPAREN : '(' ;
RPAREN : ')' ;

INT : [0-9]+ ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;
WS: [ \t\n\r\f]+ -> skip ;

start : stat+               # statements
      ;

stat  : var SEMI            # definition
      | expr SEMI           # expression
      ;

expr  : LPAREN expr RPAREN  # brackets
      | expr MULT expr      # multiplication
      | expr PLUS expr      # sum
      | ID                  # variable
      | INT                 # number
      ;

var   : ID EQ expr          # assign
      ;
