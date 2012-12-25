package com.tobiasaigner.environment;

import com.tobiasaigner.ast.*;
import com.tobiasaigner.environment.Scope;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * @author toby
 */
public class ScopeTest {
    public static final String NAME = "root";
    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(NAME);
    }

    @Test
    public void addDefinitionsToEnvironment() {
        final Expression exp1 = mock(Symbol.class);
        final Expression exp2 = mock(Atom.class);
        final Expression exp3 = mock(Lambda.class);

        Map<String, Expression> expected = new HashMap<String, Expression>() {{
            put("exp1", exp1);
            put("exp2", exp2);
            put("exp3", exp3);
        }};

        scope.addDefinition("exp1", exp1);
        scope.addDefinition("exp2", exp2);
        scope.addDefinition("exp3", exp3);

        assertEquals(expected, scope.definitions());
    }

    @Test
    public void lookupDefinitions() {
        Expression exp1 = mock(Definition.class);
        Expression exp2 = mock(Lambda.class);
        Expression exp3 = mock(ProcedureCall.class);

        scope.addDefinition("exp1", exp1);
        scope.addDefinition("exp2", exp2);
        scope.addDefinition("exp3", exp3);

        assertEquals(exp1, scope.lookup("exp1"));
        assertEquals(exp2, scope.lookup("exp2"));
        assertEquals(exp3, scope.lookup("exp3"));
    }

    @Test
    public void lookupNonExistentDefinition() {
        assertNull(scope.lookup("expression"));
    }

    @Test
    public void lookupInParentScope() {
        Lambda expectedExpression = new Lambda();
        scope.addDefinition("def", expectedExpression);

        Scope child = new Scope("child", scope);

        assertEquals(expectedExpression, child.lookup("def"));
    }
}
