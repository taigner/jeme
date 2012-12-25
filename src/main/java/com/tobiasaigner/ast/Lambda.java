package com.tobiasaigner.ast;

import org.apache.commons.lang.builder.EqualsBuilder;
import com.tobiasaigner.ast.visitor.Visitor;
import com.tobiasaigner.environment.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias Aigner
 */
public class Lambda extends Expression {
    private List<String> expectedArgs = new ArrayList<String>();
    private List<Expression> body;
    // used for closures
    private Scope enclosingScope;

    public Lambda() { }

    public Lambda(List<String> expectedArgs, List<Expression> body) {
        this.expectedArgs = expectedArgs;
        this.body = body;
    }

    public void addArgument(String name) {
        expectedArgs.add(name);
    }

    public void setBody(List<Expression> body) {
        this.body = body;
    }

    public List<Expression> body() {
        return body;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public int numberOfExpectedArgs() {
        return expectedArgs.size();
    }

    public String expectedArgumentAt(int num) {
        return expectedArgs.get(num);
    }

    public void setEnclosingScope(Scope enclosingScope) {
        this.enclosingScope = enclosingScope;
    }

    public Scope enclosingScope() {
        return enclosingScope;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Lambda) {
            Lambda lambda = (Lambda) o;

            return new EqualsBuilder()
                    .append(expectedArgs, lambda.expectedArgs)
                    .append(enclosingScope, lambda.enclosingScope)
                    .append(body, lambda.body)
                    .isEquals();
        }

        return false;
    }
}
