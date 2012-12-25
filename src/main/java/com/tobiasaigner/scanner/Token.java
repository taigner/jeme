package com.tobiasaigner.scanner;

/**
 * @author Tobias Aigner
 */
public class Token<E> {
    public static enum TokenType {
        NUMBER, SYMBOL, STRING, LEFT_PARENTHESES, RIGHT_PARENTHESES, BOOLEAN, EOF
    }

    private TokenType type;
    private E value;
    
    public Token(TokenType type, E value) {
        this.type = type;
        this.value = value;
    }
    
    public TokenType type() {
        return type;
    }

    public E value() {
        return value;
    }

    @Override
    public String toString() {
        return "Type: " + type + " Value: " + value;
    }
}