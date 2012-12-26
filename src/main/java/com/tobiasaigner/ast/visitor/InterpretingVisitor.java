package com.tobiasaigner.ast.visitor;

import com.tobiasaigner.ast.AST;
import com.tobiasaigner.ast.Expression;
import com.tobiasaigner.environment.Environment;

/**
 * @author Tobias Aigner
 */
public class InterpretingVisitor extends PrintVisitor {
    public InterpretingVisitor(Environment environment) {
        super(environment);
    }

    @Override
    public Object visit(AST ast) {
        Object result = null;

        for (Expression exp : ast.expressions()) {
            result = exp.accept(this);
        }

        return result;
    }
}
