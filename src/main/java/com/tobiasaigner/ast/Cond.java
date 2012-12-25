package com.tobiasaigner.ast;

import com.tobiasaigner.ast.visitor.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tobias Aigner
 */
public class Cond extends Expression {
    private Map<Expression, Expression> clauses = new HashMap<Expression, Expression>();
    private Expression elseClause;

    public Map<Expression, Expression> clauses() {
        return clauses;
    }

    public Expression elseClause() {
        return elseClause;
    }

    public void setElseClause(Expression elseClause) {
        this.elseClause = elseClause;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public void addClause(Expression clause, Expression body) {
        clauses.put(clause, body);
    }
}
