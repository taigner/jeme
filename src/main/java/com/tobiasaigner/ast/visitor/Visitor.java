package com.tobiasaigner.ast.visitor;

import com.tobiasaigner.ast.*;

/**
 * @author Tobias Aigner
 */
public interface Visitor {
    Object visit(AST ast);
    Object visit(Atom atom);
    Object visit(Definition def);
    Object visit(Lambda lambda);
    Object visit(ProcedureCall call);
    Object visit(Symbol symbol);
    Object visit(Argument argument);
    Object visit(Cond cond);
    Object visit(Bool bool);
    
    Object visit(Primitives.Add o);
    Object visit(Primitives.Minus o);
    Object visit(Primitives.Mult o);
    Object visit(Primitives.JemeList o);
    Object visit(Primitives.First o);
    Object visit(Primitives.Rest o);
    Object visit(Primitives.Cons o);
    Object visit(Primitives.Empty o);
    Object visit(Primitives.LT p);
    Object visit(Primitives.GT p);
    Object visit(Primitives.LTE p);
    Object visit(Primitives.GTE p);
    Object visit(Primitives.Equals p);

    Object visit(Primitives.IsNumber o);
    Object visit(Primitives.IsString o);
}
