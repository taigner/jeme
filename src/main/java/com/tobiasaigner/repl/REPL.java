package com.tobiasaigner.repl;

import com.tobiasaigner.ast.AST;
import com.tobiasaigner.ast.Primitives;
import com.tobiasaigner.ast.visitor.PrintVisitor;
import com.tobiasaigner.ast.visitor.Visitor;
import com.tobiasaigner.environment.Environment;
import com.tobiasaigner.environment.Scope;
import com.tobiasaigner.parser.Parser;
import com.tobiasaigner.parser.exceptions.ParserException;
import com.tobiasaigner.scanner.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Tobias Aigner
 */
public class REPL {
    private final Logger log = LoggerFactory.getLogger(REPL.class);
    private static final String PROMPT = "jeme> ";

    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        Environment env = new Environment(new Scope("global"));
        env.addPrimitives(new Primitives());
        Parser parser = new Parser(new Scanner());
        Visitor printVisitor = new PrintVisitor(env);

        while (input != null) {
            System.out.print(PROMPT);
            try {
                AST ast = parser.parse((input = in.readLine()));
                printVisitor.visit(ast);
            } catch (IOException e) {
                log.warn("IO error occurred during reading input", e);
                return;
            } catch (ParserException e) {
                log.warn("Parser error occurred", e);
            }
        }
    }
}
