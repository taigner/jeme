package com.tobiasaigner.ast;

import com.tobiasaigner.ast.visitor.Visitor;

/**
 * @author Tobias Aigner
 */
public class Bool extends Expression {
    private Boolean value;

    public Bool(Boolean value) {
        this.value = value;
    }

    public Boolean value() {
        return value;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return value ? "#t" : "#f";
    }
}
