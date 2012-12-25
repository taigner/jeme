package com.tobiasaigner.parser;

import com.tobiasaigner.ast.*;
import com.tobiasaigner.parser.exceptions.ParserException;
import com.tobiasaigner.scanner.Scanner;
import com.tobiasaigner.scanner.Token;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias Aigner
 */
public class Parser {
    public static final String DEFINE = "define";
    public static final String LAMBDA = "lambda";
    public static final String COND = "cond";
    public static final String ELSE = "else";
    private final Scanner scanner;
    private final AST ast = new AST();
    private Token<?> token;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
    }

    public AST parse(String stream) {
        scanner.init(stream);
        ast.clear();

        parseProgram();

        return ast;
    }

    private void parseProgram() {
        token = scanner.currentToken();

        while ((token = scanner.currentToken()).type() != Token.TokenType.EOF) {
            ast.add(parseSexp());
            token = scanner.next();
        }
    }

    private Expression parseSexp() {
        token = scanner.currentToken();

        if (Token.TokenType.NUMBER.equals(token.type()))
            return new Atom<BigDecimal>((BigDecimal) token.value());
        else if (Token.TokenType.STRING.equals(token.type()))
            return new Atom<String>((String) token.value());
        else if (Token.TokenType.SYMBOL.equals(token.type()))
            return new Symbol((String) token.value());
        else if (Token.TokenType.BOOLEAN.equals(token.type())) {
            String value = (String) token.value();
            if ("t".equals(value))
                return new Bool(true);
            else if ("f".equals(value))
                return new Bool(false);
            throw new ParserException("Provided wrong boolean value. Either #t or #f.");
        }

        return parseList();
    }

    private Expression parseList() {
        if (!Token.TokenType.LEFT_PARENTHESES.equals(token.type()))
            throw new ParserException("( expected");

        token = scanner.next();

        if (DEFINE.equals(token.value())) {
            return parseDefinition();
        } else if (LAMBDA.equals(token.value())) {
            return parseLambda();
        } else if (COND.equals(token.value())) {
            return parseCond();
        } else if (Token.TokenType.SYMBOL.equals(token.type())) {
            return parseProcedureCall();
        } else {
            ProcedureCall call = new ProcedureCall();
            call.setProcedure(parseSexp());

            while (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
                call.setArgument(parseSexp());

            return call;
        }
    }

    private Expression parseProcedureCall() {
        ProcedureCall call = new ProcedureCall();
        call.setProcedure(parseSexp());

        while (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
            call.setArgument(parseSexp());

        return call;
    }

    private Lambda parseLambda() {
        Lambda lambda = new Lambda();

        token = scanner.next();

        if (!Token.TokenType.LEFT_PARENTHESES.equals(token.type()))
            throw new ParserException(") expected");

        while (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
            lambda.addArgument((String) token.value());

        List<Expression> expressions = new ArrayList<Expression>();
        while (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
            expressions.add(parseSexp());

        lambda.setBody(expressions);

        return lambda;
    }

    private Cond parseCond() {
        Cond cond = new Cond();

        while (Token.TokenType.LEFT_PARENTHESES.equals((token = scanner.next()).type())) {
            token = scanner.next();

            if (ELSE.equals(token.value())) {
                token = scanner.next();
                cond.setElseClause(parseSexp());
            } else {
                Expression clause = parseSexp();

                token = scanner.next();

                cond.addClause(clause, parseSexp());
            }
            if (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
                throw new ParserException(") expected");
        }

        if (!Token.TokenType.RIGHT_PARENTHESES.equals(token.type()))
            throw new ParserException(") expected");

        return cond;
    }

    private Definition parseDefinition() {
        Definition definition = new Definition();

        token = scanner.next();
        // e.g. (define (square x) (* x x))
        if (Token.TokenType.LEFT_PARENTHESES.equals(token.type())) {
            token = scanner.next();
            definition.setName((String) token.value());

            Lambda lambda = new Lambda();

            while (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
                lambda.addArgument((String) token.value());

            List<Expression> expressions = new ArrayList<Expression>();
            while (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
                expressions.add(parseSexp());

            lambda.setBody(expressions);
            definition.setExpression(lambda);

            return definition;
        } else {
            // e.g. (define x 10)
            definition.setName((String) token.value());
            scanner.next();

            definition.setExpression(parseSexp());

            if (!Token.TokenType.RIGHT_PARENTHESES.equals((token = scanner.next()).type()))
                throw new ParserException(") expected");
        }

        return definition;
    }
}
