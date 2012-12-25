package com.tobiasaigner.ast;

import org.apache.commons.lang.builder.EqualsBuilder;
import com.tobiasaigner.ast.visitor.Visitor;

/**
 * @author Tobias Aigner
 */
public class Atom<E> extends Expression {
    private E value;

    public Atom(E value) {
        this.value = value;
    }

    public E value() {
        return value;
    }

    public E accept(Visitor v) {
        return (E) v.visit(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Atom) {
            Atom atom = (Atom) other;

            return new EqualsBuilder()
                    .append(value, atom.value)
                    .isEquals();
        }

        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
