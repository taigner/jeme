package com.tobiasaigner.ast;

import org.apache.commons.lang.builder.EqualsBuilder;
import com.tobiasaigner.ast.visitor.Visitor;

/**
 * @author Tobias Aigner
 */
public class Argument extends Expression {
    private String name;

    public Argument(String name) {
        this.name = name;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Argument) {
            Argument argument = (Argument) other;
            return new EqualsBuilder()
                    .append(name, argument.name)
                    .isEquals();
        }

        return false;
    }
}
