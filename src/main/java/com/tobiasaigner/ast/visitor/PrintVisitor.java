package com.tobiasaigner.ast.visitor;

import com.tobiasaigner.ast.*;
import com.tobiasaigner.environment.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Tobias Aigner
 */
public class PrintVisitor implements Visitor {
    private final Logger log = LoggerFactory.getLogger(PrintVisitor.class);

    private Environment environment;

    public PrintVisitor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object visit(AST ast) {
        Object result = null;

        for (Expression exp : ast.expressions()) {
            result = exp.accept(this);
            System.out.println(result);
        }

        return result;
    }

    @Override
    public Object visit(Atom atom) {
        log.debug("Atom value: {} ", atom.value());

        return atom;
    }

    @Override
    public Object visit(Definition def) {
        log.debug("Adding new definition '{}'", def.name());

        Expression exp = def.expression();
        if (exp instanceof Lambda)
            exp.accept(this);

        environment.addDefinition(def.name(), exp);

        log.debug("Definition '{}' ended", def.name());

        return def;
    }

    @Override
    public Object visit(Lambda lambda) {
        lambda.setEnclosingScope(environment.currentScope());

        return lambda;
    }

    @Override
    public Object visit(ProcedureCall call) {
        Lambda procedure;
        String name;

        if (call.procedure() instanceof Symbol) {
            name = ((Symbol) call.procedure()).name();
            procedure = (Lambda) environment.lookup(name);
            if (procedure == null) {
                throw new RuntimeException("Procedure not defined: " + call.name());
            }
        } else if (call.procedure() instanceof ProcedureCall) {
            procedure = (Lambda) call.procedure().accept(this);
        } else {
            return evaluateProcedureCall(call, (Lambda) call.procedure().accept(this));
        }

        if (procedure instanceof Primitives.PrimitiveProcedure) {
            return evaluatePrimitiveProcedure(call, (Primitives.PrimitiveProcedure) procedure);
        } else {
            return evaluateProcedureCall(call, procedure);
        }
    }

    @Override
    public Object visit(Symbol symbol) {
        log.debug("Symbol: {}", symbol.name());

        Expression result = environment.lookup(symbol.name());

        if (result == null) {
            throw new RuntimeException("Symbol '" + symbol.name() + "' not found");
        } else if (result instanceof Primitives.PrimitiveProcedure) {
            return result;
        }

        return result.accept(this);
    }

    @Override
    public Object visit(Cond cond) {
        Map<Expression, Expression> clauses = cond.clauses();
        for (Expression clause : clauses.keySet()) {
            Bool result = (Bool) clause.accept(this);

            if (result.value())
                return clauses.get(clause).accept(this);
        }

        return cond.elseClause().accept(this);
    }

    @Override
    public Object visit(Bool bool) {
        return bool;
    }

    @Override
    public Object visit(Argument argument) {
        return environment.lookup(argument.name());
    }

    @Override
    public Object visit(Primitives.Add o) {
        log.debug("Add");

        BigDecimal result = new BigDecimal(0);
        for (Expression exp : o.allArguments()) {
            result = result.add(((Atom<BigDecimal>) exp.accept(this)).value());
        }

        log.debug("Add result: {}", result);

        return new Atom<BigDecimal>(result);
    }

    @Override
    public Object visit(Primitives.Minus o) {
        log.debug("Minus");

        BigDecimal result = ((Atom<BigDecimal>) o.argumentAt(0).accept(this)).value();

        for (int i = 1; i < o.numberOfArguments(); i++) {
            result = result.subtract(((Atom<BigDecimal>) o.argumentAt(i).accept(this)).value());
        }

        log.debug("Minus result: {}", result);

        return new Atom<BigDecimal>(result);
    }

    @Override
    public Object visit(Primitives.Mult o) {
        log.debug("Multiply");

        BigDecimal result = new BigDecimal(1);
        for (Expression exp : o.allArguments()) {
            result = result.multiply(((Atom<BigDecimal>) exp.accept(this)).value());
        }

        log.debug("Multiply result: {}", result);

        return new Atom<BigDecimal>(result);
    }

    @Override
    public Object visit(Primitives.JemeList o) {
        Primitives.JemeList result = new Primitives.JemeList();

        for (Expression exp : o.allArguments()) {
            result.addElement((Expression) exp.accept(this));
        }

        return result;
    }

    @Override
    public Object visit(Primitives.First o) {
        Expression list = (Expression) o.argumentAt(0).accept(this);
        if (!(list instanceof Primitives.JemeList)) {
            throw new RuntimeException("first can only be applied to lists");
        }

        return ((Primitives.JemeList) list).get(0);
    }

    @Override
    public Object visit(Primitives.Rest o) {
        Expression list = (Expression) o.argumentAt(0).accept(this);
        if (!(list instanceof Primitives.JemeList)) {
            throw new RuntimeException("rest can only be applied to lists");
        }

        return ((Primitives.JemeList) list).rest();
    }

    @Override
    public Object visit(Primitives.Cons o) {
        Expression consed = (Expression) o.argumentAt(0).accept(this);
        Expression list = (Expression) o.argumentAt(1).accept(this);
        if (!(list instanceof Primitives.JemeList)) {
            throw new RuntimeException("second argument to cons must be a list");
        }

        ((Primitives.JemeList) list).addToFront(consed);

        return list;
    }

    @Override
    public Object visit(Primitives.Empty o) {
        Expression argument = (Expression) o.argumentAt(0).accept(this);
        if (argument instanceof Primitives.JemeList) {
            if (((Primitives.JemeList) argument).size() == 0)
                return new Bool(true);
        }

        return new Bool(false);
    }

    @Override
    public Object visit(Primitives.IsNumber o) {
        Atom<?> atom = (Atom<?>) o.argumentAt(0).accept(this);

        return new Bool(atom.value() instanceof Number);
    }

    @Override
    public Object visit(Primitives.IsString o) {
        Atom<?> atom = (Atom<?>) o.argumentAt(0).accept(this);

        return new Bool(atom.value() instanceof String);
    }

    @Override
    public Object visit(Primitives.LT p) {
        Atom<BigDecimal> first = (Atom<BigDecimal>) p.argumentAt(0).accept(this);
        Atom<BigDecimal> second = (Atom<BigDecimal>) p.argumentAt(1).accept(this);

        return new Bool(first.value().compareTo(second.value()) < 0);
    }

    @Override
    public Object visit(Primitives.GT p) {
        Atom<BigDecimal> first = (Atom<BigDecimal>) p.argumentAt(0).accept(this);
        Atom<BigDecimal> second = (Atom<BigDecimal>) p.argumentAt(1).accept(this);

        return new Bool(first.value().compareTo(second.value()) > 0);
    }

    @Override
    public Object visit(Primitives.LTE p) {
        Atom<BigDecimal> first = (Atom<BigDecimal>) p.argumentAt(0).accept(this);
        Atom<BigDecimal> second = (Atom<BigDecimal>) p.argumentAt(1).accept(this);

        return new Bool(first.value().compareTo(second.value()) <= 0);
    }

    @Override
    public Object visit(Primitives.GTE p) {
        Atom<BigDecimal> first = (Atom<BigDecimal>) p.argumentAt(0).accept(this);
        Atom<BigDecimal> second = (Atom<BigDecimal>) p.argumentAt(1).accept(this);

        return new Bool(first.value().compareTo(second.value()) >= 0);
    }

    @Override
    public Object visit(Primitives.Equals p) {
        Atom<?> first = (Atom<?>) p.argumentAt(0).accept(this);
        Atom<?> second = (Atom<?>) p.argumentAt(1).accept(this);

        return new Bool(first.equals(second));
    }

    private Object evaluateProcedureCall(ProcedureCall call, Lambda procedure) {
        List<Expression> arguments = evaluateProcedureArguments(call);

        environment.enter(procedure.enclosingScope());

        checkNumberOfArguments(procedure, call);
        addArgumentsToEnvironment(procedure, arguments);

        Object result = evaluateProcedureBody(procedure);

        environment.leaveScope();

        log.debug("Scope for procedure call ended");

        return result;
    }

    private Object evaluatePrimitiveProcedure(ProcedureCall call, Primitives.PrimitiveProcedure procedure) {
        if (call.numberOfArguments() < procedure.minNumberOfArguments() || call.numberOfArguments() > procedure.maxNumberOfArguments())
            throw new RuntimeException("Number of arguments does not match");

        procedure.setArguments(call.arguments());

        return procedure.accept(this);
    }

    private void addArgumentsToEnvironment(Lambda procedure, List<Expression> arguments) {
        for (int i = 0; i < procedure.numberOfExpectedArgs(); i++) {
            environment.addDefinition(procedure.expectedArgumentAt(i), arguments.get(i));
        }
    }

    private List<Expression> evaluateProcedureArguments(ProcedureCall call) {
        List<Expression> result = new ArrayList<Expression>();
        for (int i = 0; i < call.numberOfArguments(); i++) {
            result.add((Expression) call.argumentAt(i).accept(this));
        }
        return result;
    }

    private Object evaluateProcedureBody(Lambda procedure) {
        Object result = null;

        // evaluate all expressions and return the last one
        for (Expression expr : procedure.body()) {
            result = expr.accept(this);
        }

        return result;
    }

    private void checkNumberOfArguments(Lambda procedure, ProcedureCall call) {
        if (procedure.numberOfExpectedArgs() != call.numberOfArguments()) {
            String msg = "Procedure expects %s arguments. %s were provided";
            throw new RuntimeException(String.format(msg, procedure.numberOfExpectedArgs(), call.numberOfArguments()));
        }
    }
}
