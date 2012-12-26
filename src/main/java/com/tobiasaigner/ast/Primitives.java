package com.tobiasaigner.ast;

import com.tobiasaigner.ast.visitor.Visitor;
import com.tobiasaigner.environment.Environment;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tobias Aigner
 */
public class Primitives {
    private Map<String, Expression> primitives = new HashMap<String, Expression>();

    public Primitives() {
        primitives.put("*", new Mult());
        primitives.put("+", new Add());
        primitives.put("-", new Minus());
        primitives.put("list", new JemeList());
        primitives.put("first", new First());
        primitives.put("rest", new Rest());
        primitives.put("cons", new Cons());
        primitives.put("empty?", new Empty());
        primitives.put("number?", new IsNumber());
        primitives.put("string?", new IsString());
        primitives.put("<", new LT());
        primitives.put(">", new GT());
        primitives.put("<=", new LTE());
        primitives.put(">=", new GTE());
        primitives.put("=", new Equals());
        primitives.put("println", new Println());
    }

    public Map<String, Expression> allDefinitions() {
        return primitives;
    }

    public abstract static class PrimitiveProcedure extends Lambda {
        protected List<Expression> arguments = new ArrayList<Expression>();

        public abstract int minNumberOfArguments();
        public abstract int maxNumberOfArguments();

        public List<Expression> allArguments() {
            return arguments;
        }

        public int numberOfArguments() {
            return arguments.size();
        }

        public Expression argumentAt(int position) {
            return arguments.get(position);
        }

        public void setArguments(List<Expression> arguments) {
            this.arguments = arguments;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Add extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Minus extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Mult extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class JemeList extends PrimitiveProcedure {
        private List<Expression> elements = new ArrayList<Expression>();

        public JemeList() {
        }

        public JemeList(List<Expression> elements) {
            this.elements = elements;
        }

        @Override
        public int minNumberOfArguments() {
            return 0;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }

        public void addElement(Expression element) {
            elements.add(element);
        }

        public void addToFront(Expression element) {
            List<Expression> newList = new ArrayList<Expression>();
            newList.add(element);
            newList.addAll(elements);
            elements = newList;
        }

        public Expression get(int num) {
            return elements.get(num);
        }

        public int size() {
            return elements.size();
        }

        @Override
        public String toString() {
            String str = "";
            String delim = elements.size() > 1 ? " " : "";
            for (int i = 0; i < elements.size(); i++) {
                str += elements.get(i);
                str += delim;
                if (i == elements.size() - 2) {
                    delim = "";
                }
            }
            
            return "'(" + str + ")";
        }

        public JemeList rest() {
            return new JemeList(elements.subList(1, elements.size()));
        }
    }

    public static class First extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 1;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Rest extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 1;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Cons extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return 2;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Empty extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 1;
        }

        @Override
        public int maxNumberOfArguments() {
            return 1;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class IsNumber extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 1;
        }

        @Override
        public int maxNumberOfArguments() {
            return 1;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class IsString extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 1;
        }

        @Override
        public int maxNumberOfArguments() {
            return 1;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class LT extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return 2;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class LTE extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return 2;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class GT extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return 2;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class GTE extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return 2;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Equals extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 2;
        }

        @Override
        public int maxNumberOfArguments() {
            return 2;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    public static class Println extends PrimitiveProcedure {
        @Override
        public int minNumberOfArguments() {
            return 0;
        }

        @Override
        public int maxNumberOfArguments() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object accept(Visitor v) {
            return v.visit(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Primitives) {
            Primitives primitives = (Primitives) o;

            return new EqualsBuilder()
                    .append(this.primitives, primitives.primitives)
                    .isEquals();
        }

        return false;
    }
}
