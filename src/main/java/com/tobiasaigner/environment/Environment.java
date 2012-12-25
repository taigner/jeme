package com.tobiasaigner.environment;

import com.tobiasaigner.ast.Expression;
import com.tobiasaigner.ast.Primitives;

import java.util.Map;
import java.util.Stack;

/**
 * @author Tobias Aigner
 */
public class Environment {
    private Scope currentScope;
    private Stack<Scope> tmpScopes = new Stack<Scope>();

    public Environment(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public void addDefinition(String name, Expression expression) {
        currentScope.addDefinition(name, expression);
    }

    public void addPrimitives(Primitives primitives) {
        Map<String, Expression> definitions = primitives.allDefinitions();
        for (String key : definitions.keySet()) {
            currentScope.addDefinition(key, definitions.get(key));
        }
    }

    public Expression lookup(String name) {
        return currentScope.lookup(name);
    }

    public Scope currentScope() {
        return currentScope;
    }

    public void enter(Scope closure) {
        tmpScopes.push(currentScope);
        currentScope = new Scope("procedure", closure);
    }

    public void leaveScope() {
        currentScope = tmpScopes.pop();
    }

    void setTmpScopes(Stack<Scope> scopes) {
        tmpScopes = scopes;
    }
}
