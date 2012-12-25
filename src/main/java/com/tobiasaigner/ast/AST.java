package com.tobiasaigner.ast;

import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias Aigner
 */
public class AST {
    private List<Expression> exps = new ArrayList<Expression>();

    public AST add(Expression exp) {
        exps.add(exp);

        return this;
    }

    public List<Expression> expressions() {
        return exps;
    }

    public Expression expressionAt(int position) {
        return exps.get(position);
    }

    public void clear() {
        exps.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AST) {
            AST ast = (AST) other;
            return new EqualsBuilder()
                    .append(exps, ast.exps)
                    .isEquals();
        }

        return false;
    }
}
