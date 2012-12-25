package com.tobiasaigner.ast;

import com.tobiasaigner.ast.visitor.Visitor;

/**
 * @author Tobias Aigner
 */
public abstract class Expression {
    public abstract Object accept(Visitor v);
}
