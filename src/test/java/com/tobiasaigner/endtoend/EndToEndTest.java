package com.tobiasaigner.endtoend;

import com.tobiasaigner.environment.Scope;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.tobiasaigner.ast.AST;
import com.tobiasaigner.ast.Primitives;
import com.tobiasaigner.ast.visitor.PrintVisitor;
import com.tobiasaigner.ast.visitor.Visitor;
import com.tobiasaigner.environment.Environment;
import com.tobiasaigner.parser.Parser;
import com.tobiasaigner.scanner.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * @author Tobias Aigner
 */
public class EndToEndTest {
    private Environment env;
    private Parser parser;
    private Visitor printVisitor;

    @Before
    public void setUp() {
        env = new Environment(new Scope("global"));
        env.addPrimitives(new Primitives());
        parser = new Parser(new Scanner());
        printVisitor = new PrintVisitor(env);
    }

    @Test
    public void evaluateSimpleVariableDefinition() {
        assertEquals("10", eval("(define x 10) x"));
    }

    @Test
    public void higherOrderFunctions() {
        String program = "(define x (lambda (y) y))" +
                         "(define (h-o-f z)" +
                            "(z 20))" +
                         "(h-o-f x)";

        assertEquals("20", eval(program));
    }

    @Test
    public void lexicalScoping() {
        String program = "(define x 10) " +
                         "(define (a)" +
                            "x)" +
                         "(define (b p)" +
                            "(define x 20)" +
                            "(a))" +
                         "(b 10)";

        assertEquals("10", eval(program));
    }

    @Test
    public void closure() {
        String program = "(define x 20)" +
                         "(define (closure)" +
                             "(define x 10)" +
                             "(lambda () x))" +
                         "((closure))";

        assertEquals("10", eval(program));
    }

    @Test
    public void closureDoesNotInferGlobalEnvironment() {
        String program = "(define x 20)" +
                         "(define (closure)" +
                            "(define x 10)" +
                            "(lambda () x))" +
                         "((closure)) x";

        assertEquals("20", eval(program));
    }

    @Test
    public void variableRedefinition() {
        String program = "(define x 10)" +
                         "(define (func2) x)" +
                         "(func2)" +
                         "(define x 30)" +
                         "(func2)";

        assertEquals("30", eval(program));
    }

    @Test
    public void listOfLambdas() {
        String program = "((first (list (lambda (x) (* x x)))) 10)";

        assertEquals("100", eval(program));
    }

    @Test
    public void anonymousFunctionInvocation() {
        String program = "((lambda (x) (* x x)) 100)";

        assertEquals("10000", eval(program));
    }

    @Test
    public void twoAnonymousFunctions() {
        String program = "(((lambda () (lambda () 25))))";

        assertEquals("25", eval(program));
    }

    @Test
    public void anonymousFunctionShouldNotInferGlobalEnvironment() {
        String program = "(define x 25)" +
                         "((first (list (lambda (x) (* x x)))) 10)" +
                         "x";

        assertEquals("25", eval(program));
    }

    @Test
    public void localFunctionDefinitionAndInvocation() {
        String program = "(define (func1)" +
                            "(define (func2)" +
                                "10)" +
                            "(func2))" +
                         "(func1)";

        assertEquals("10", eval(program));
    }

    @Test
    public void localHigherOrderFunction() {
        String program = "(define (func2 x)" +
                            "(x 10))" +
                         "(define (h-o-f)" +
                            "(define (func1 x)" +
                                "(* x x))" +
                            "(func2 func1))" +
                         "(h-o-f)";

        assertEquals("100", eval(program));
    }

    @Test
    public void localHigherOrderAnonymousFunction() {
        String program = "(define (func2 x)" +
                            "(x 10))" +
                         "(define (h-o-f)" +
                            "(func2 (lambda (x) (* x x))))" +
                         "(h-o-f)";

        assertEquals("100", eval(program));
    }

    @Test
    public void localHigherOrderAnonymousFunctionAccessesEnvironment() {
        String program = "(define (func2 x)" +
                            "(x 10))" +
                         "(define (h-o-f)" +
                            "(define y 15)" +
                            "(func2 (lambda (x) (* x y))))" +
                         "(h-o-f)";

        assertEquals("150", eval(program));
    }

    @Test
    public void multipleLocalHigherOrderAnonymousFunctions() {
        String program = "(define (func2 x)" +
                            "(define z 20)" +
                            "((lambda ()" +
                                "(x z))))" +
                         "(define (h-o-f)" +
                            "(define y 10)" +
                            "(func2 (lambda (x) (* x y))))" +
                         "(h-o-f)";

        assertEquals("200", eval(program));
    }

    @Test
    public void multipleNestedLocalHigherOrderAnonymousFunctions() {
        String program = "(define (func2 x)" +
                            "(define a 25)" +
                            "(((lambda ()"+
                                "(define b 31)" +
                                "(lambda ()" +
                                    "(x (+ a b)))))))" +
                         "(define (h-o-f)" +
                            "(define y 10)" +
                            "(func2 (lambda (x) (* x y))))" +
                         "(h-o-f)";

        assertEquals("560", eval(program));
    }

    @Test
    public void multipleNestedLocalHigherOrderAnonymousFunctionsWithShadowedVars() {
        String program = "(define (func2 x)" +
                            "(define a 25)" +
                            "(((lambda ()"+
                                "(define a 31)" +
                                "(lambda ()" +
                                    "(x a))))))" +
                         "(define (h-o-f)" +
                            "(define y 10)" +
                            "(func2 (lambda (x) (* x y))))" +
                         "(h-o-f)";

        assertEquals("310", eval(program));
    }

    @Test
    public void scoping() {
        String program = "(define g 20)" +
                         "(define (f1 x)" +
                            "(define a 123)" +
                            "(define local 111)" +
                            "(define (f2 z)" +
                                "(define local 24)" +
                                "(define (f3 z)" +
                                    "(define local 25)" +
                                    "(+ z local a g))" +
                                "(* g (f3 z) local))" +
                            "(+ local (f2 x) a))" +
                         "(f1 g)";

        assertEquals("90474", eval(program));
    }

    @Test(expected = RuntimeException.class)
    public void primitiveProcedureCallWithWrongNumberOfArguments() {
        printVisitor.visit(parser.parse("(< 1 2 3)"));
    }

    @Test(expected = RuntimeException.class)
    public void undefinedSymbol() {
        printVisitor.visit(parser.parse("test"));
    }


    private String eval(String program) {
        return printVisitor.visit(parser.parse(program)).toString();
    }
}
