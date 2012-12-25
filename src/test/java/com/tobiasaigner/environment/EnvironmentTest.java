package com.tobiasaigner.environment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.tobiasaigner.ast.Expression;
import com.tobiasaigner.ast.Lambda;
import com.tobiasaigner.ast.Primitives;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author toby
 */
public class EnvironmentTest {
    private Environment env;

    @Mock
    private Scope scope;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        env = new Environment(scope);
    }

    @Test
    public void addDefinitionToCurrentScope() {
        String name = "name";
        Expression exp = mock(Lambda.class);
        
        env.addDefinition(name, exp);

        verify(scope).addDefinition(name, exp);
    }

    @Test
    public void lookupDefinition() {
        String expectedName = "name";

        env.lookup(expectedName);

        verify(scope).lookup(expectedName);
    }

    @Test
    public void addPrimitives() {
        final String firstPrimitiveName = "name"; final Expression firstPrimitiveExpression = new Lambda();
        final String secondPrimitiveName = "name"; final Expression secondPrimitiveExpression = new Lambda();

        Map<String, Expression> expectedPrimitives = new HashMap<String, Expression>() {{
            put(firstPrimitiveName, firstPrimitiveExpression);
            put(secondPrimitiveName, secondPrimitiveExpression);
        }};
        Primitives primitives = mock(Primitives.class);
        when(primitives.allDefinitions()).thenReturn(expectedPrimitives);

        env.addPrimitives(primitives);

        verify(scope).addDefinition(firstPrimitiveName, firstPrimitiveExpression);
        verify(scope).addDefinition(secondPrimitiveName, secondPrimitiveExpression);
    }

    @Test
    public void returnsCurrentScope() {
        assertEquals(scope, env.currentScope());
    }

    @Test
    public void enterScopeWithEnclosingScope() {
        Scope closure = mock(Scope.class);
        Stack<Scope> tmpScopes = mock(Stack.class);
        env.setTmpScopes(tmpScopes);

        env.enter(closure);

        verify(tmpScopes).push(scope);
        assertEquals(closure, env.currentScope().parent());
    }

    @Test
    public void leaveScope() {
        Stack tmpScopes = mock(Stack.class);
        env.setTmpScopes(tmpScopes);

        env.leaveScope();

        verify(tmpScopes).pop();
    }
}
