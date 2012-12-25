package com.tobiasaigner.environment;

import com.tobiasaigner.ast.Expression;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tobias Aigner
 */
public class Scope {
    private final String name;
    private Scope parent;
    private final Map<String, Expression> definitions = new HashMap<String, Expression>();

    public Scope(String name) {
        this.name = name;
        this.parent = null;
    }

    public Scope(String name, Scope parent) {
        this.name = name;
        this.parent = parent;
    }

    public void addDefinition(String name, Expression expression) {
        definitions.put(name, expression);
    }

    public Expression findDefinitionFrom(String name) {
        return definitions.get(name);
    }

    public Expression lookup(String name) {
        Expression result = null;
        Scope search = this;

        while (search != null) {
            result = search.findDefinitionFrom(name);
            if (result == null)
                search = search.parent();
            else
                return result;
        }

        return result;
    }

    public Scope parent() {
        return parent;
    }

    Map<String, Expression> definitions() {
        return definitions;
    }
}
