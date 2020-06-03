grammar BooleanExpr;
@header {
    package LogicCircuitSimulator.BooleanExpressionParser.GrammarParser;
}


BLANK:         [ \t\r\n] -> skip;
AND:           'AND';
OR:            'OR';

LEFT_PAREN:     '(';
RIGHT_PAREN:    ')';
NOT:            'NOT';
IDENTIFIER:      [a-zA-Z]+;

booleanExpression   : orExpression EOF
                     ;

orExpression        : orExpression OR andExpression
                     | andExpression
                     ;


andExpression       : andExpression AND valueExpression
                     | valueExpression
                     ;

valueExpression     : IDENTIFIER
                     | NOT orExpression
                     | LEFT_PAREN orExpression RIGHT_PAREN
                     ;