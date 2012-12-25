package com.tobiasaigner.ast;

import com.tobiasaigner.ast.visitor.Visitor;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias Aigner
 */
public class ProcedureCall extends Expression {
    protected String name;
    protected Expression procedure;
    protected List<Expression> arguments = new ArrayList<Expression>();

    public void setProcedure(Expression procedure) {
        this.procedure = procedure;
    }

    public ProcedureCall setArgument(Expression argument) {
        arguments.add(argument);

        return this;
    }

    public Expression procedure() {
        return procedure;
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    public String name() {
        return name;
    }

    public List<Expression> arguments() {
        return arguments;
    }

    public Expression argumentAt(int position) {
        return arguments.get(position);
    }

    public int numberOfArguments() {
        return arguments.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ProcedureCall) {
            ProcedureCall call = (ProcedureCall) other;

            return new EqualsBuilder()
                    .append(name, call.name)
                    .append(arguments, call.arguments)
                    .append(procedure, call.procedure)
                    .isEquals();
        }

        return false;
    }
}
