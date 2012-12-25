package com.tobiasaigner.ast;

import com.tobiasaigner.ast.visitor.Visitor;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author Tobias Aigner
 */
public class Symbol extends Expression {
    private String name;
    
    public Symbol(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Symbol) {
            Symbol sym = (Symbol) other;

            return new EqualsBuilder()
                    .append(name, sym.name)
                    .isEquals();
        }

        return false;
    }
}
