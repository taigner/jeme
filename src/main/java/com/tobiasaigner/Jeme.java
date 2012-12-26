package com.tobiasaigner;

import com.tobiasaigner.ast.Primitives;
import com.tobiasaigner.ast.visitor.InterpretingVisitor;
import com.tobiasaigner.ast.visitor.PrintVisitor;
import com.tobiasaigner.environment.Environment;
import com.tobiasaigner.environment.Scope;
import com.tobiasaigner.parser.Parser;
import com.tobiasaigner.repl.REPL;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Tobias Aigner
 */
public class Jeme {
    public static void main(String args[]) throws FileNotFoundException {
        if (args.length == 1)
            eval(readContentFrom(args[0]));
        else
            new REPL().run();
    }

    private static void eval(String program) {
        Environment env = new Environment(new Scope("global"));
        env.addPrimitives(new Primitives());
        Parser parser = new Parser(new com.tobiasaigner.scanner.Scanner());
        System.out.println(new InterpretingVisitor(env).visit(parser.parse(program)));
    }

    private static String readContentFrom(String filename) throws FileNotFoundException {
        return new Scanner(new File(filename)).useDelimiter("\\Z").next();
    }
}
