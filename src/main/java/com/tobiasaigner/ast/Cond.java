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
    private Map<Expression, List<Expression>> clauses = new HashMap<Expression, List<Expression>>();
    private List<Expression> elseClause;

    public Map<Expression, List<Expression>> clauses() {
        return clauses;
    }

    public List<Expression> elseClause() {
        return elseClause;
    }

    public void setElseClause(List<Expression> elseClause) {
        this.elseClause = elseClause;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public void addClause(Expression clause, List<Expression> body) {
        clauses.put(clause, body);
    }
}
