package com.tobiasaigner.parser;

import com.tobiasaigner.ast.*;
import com.tobiasaigner.environment.Scope;
import com.tobiasaigner.parser.exceptions.ParserException;
import org.junit.Before;
import org.junit.Test;
import com.tobiasaigner.environment.Environment;
import com.tobiasaigner.scanner.Scanner;

import java.math.BigDecimal;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author toby
 */
public class ParserTest {
    private Parser parser;

    @Before
    public void setUp() {
        parser = new Parser(new Scanner());
    }

    @Test
    public void parseVariableDefinition() throws Exception {
        String input = "(define x 10)";

        AST result = parser.parse(input);

        assertEquals(result, new AST().add(new Definition("x", new Atom<BigDecimal>(new BigDecimal(10)))));
    }

    @Test
    public void parseLambda() throws Exception {
        String input = "(lambda (x) x)";
        
        AST result = parser.parse(input);

        assertEquals(result, new AST().add(new Lambda(Arrays.asList("x"), Arrays.<Expression>asList(new Symbol("x")))));
    }

    @Test
    public void parseAnonymousProcedureCall() throws Exception {
        String input = "((lambda (x) x) 10)";

        AST result = parser.parse(input);

        assertTrue(result.expressionAt(0) instanceof ProcedureCall);
    }

    @Test
    public void parseSquareProcedure() throws Exception {
        String input = "(define (square x) (* x x))";

        AST result = parser.parse(input);
        assertTrue(result.expressionAt(0) instanceof Definition);
    }

    @Test
    public void parseCond() throws Exception {
        String input = "(define (test x) (cond ((= x 1) (* x x)) (else (+ x x))))";

        AST result = parser.parse(input);

        Definition def = ((Definition) result.expressionAt(0));
        assertEquals("test" , def.name());
        assertTrue(((Lambda) def.expression()).body().get(0) instanceof Cond);
    }

    @Test
    public void parseString() throws Exception {
        parser.parse("\"string\"");
    }

    @Test
    public void parseBoolean() throws Exception {
        parser.parse("#t");
        parser.parse("#f");
    }

    @Test(expected = ParserException.class)
    public void missingRightParenthesis() throws Exception {
        String input = "(define (test x) x";

        parser.parse(input);
    }

    @Test(expected = ParserException.class)
    public void missingParenthesisInLambdaExpression() throws Exception {
        String input = "(define test (lambda x))";

        parser.parse(input);
    }

    @Test(expected = ParserException.class)
    public void missingRightParenthesisInDefine() throws Exception {
        String input = "(define test 1";

        parser.parse(input);
    }

    @Test(expected = ParserException.class)
    public void missingRightParenthesisInCond() throws Exception {
        String input = "(cond ((= 0 0)";

        parser.parse(input);
    }

    @Test(expected = ParserException.class)
    public void wrongBooleanValue() throws Exception {
        parser.parse("#y");
    }
}
