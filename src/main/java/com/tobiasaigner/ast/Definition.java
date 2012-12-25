package com.tobiasaigner.ast;

import org.apache.commons.lang.builder.EqualsBuilder;
import com.tobiasaigner.ast.visitor.Visitor;

/**
 * @author Tobias Aigner
 */
public class Definition extends Expression {
    private String name;
    private Expression exp;

    public Definition() {
    }
    
    public Definition(String name, Expression exp) {
        this.name = name;
        this.exp = exp;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpression(Expression exp) {
        this.exp = exp;
    }

    public Expression expression() {
        return exp;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Definition) {
            Definition def = (Definition) o;

            return new EqualsBuilder()
                    .append(name, def.name)
                    .append(exp, def.exp)
                    .isEquals();
        }

        return false;
    }

    @Override
    public String toString() {
        return "Definition: " + name;
    }
}
