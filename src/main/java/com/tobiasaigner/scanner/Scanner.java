package com.tobiasaigner.scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tobias Aigner
 */
public class Scanner {
    private String input;
    private int position = 0;
    private Token<?> token;

    private List<Character> separators = Arrays.asList(' ', '(', ')');

    public void init(String input) {
        position = 0;
        this.input = input;
        
        token = next();
    }

    public Token<?> currentToken() {
        return token;
    }

    public Token<?> next() {
        if (position >= input.length()) {
            return (token = new Token<String>(Token.TokenType.EOF, ""));
        }

        if (position < input.length()) {
            skipBlanks();

            if (input.charAt(position) == '(') {
                token = new Token<String>(Token.TokenType.LEFT_PARENTHESES, "(");
                position++;
            } else if (input.charAt(position) == ')') {
                token = new Token<String>(Token.TokenType.RIGHT_PARENTHESES, ")");
                position++;
            } else if (input.charAt(position) == '"') {
                token = readString();
                position++;
            } else if (input.charAt(position) == '#') {
                token = new Token<String>(Token.TokenType.BOOLEAN, String.valueOf(input.charAt(++position)));
                position++;
            } else if (StringUtils.isNumeric(String.valueOf(input.charAt(position)))) {
                token = readNumber();
            } else {
                token = readSymbol();
            }
        }

        return token;
    }

    private Token<BigDecimal> readNumber() {
        int start = position;

        skipSeparators();

        return new Token<BigDecimal>(Token.TokenType.NUMBER, new BigDecimal(input.substring(start, position)));
    }

    private Token<String> readString() {
        int start = ++position;

        while (!(input.charAt(position) == '"'))
            position++;

        return new Token<String>(Token.TokenType.STRING, input.substring(start, position));
    }

    private Token<String> readSymbol() {
        int start = position;

        skipSeparators();

        return new Token<String>(Token.TokenType.SYMBOL, input.substring(start, position));
    }

    private boolean isSeparator() {
        if (position >= input.length() || separators.contains(input.charAt(position)))
            return true;

        return false;
    }

    private void skipSeparators() {
        while (!isSeparator())
            position++;
    }

    private void skipBlanks() {
        while (StringUtils.isBlank(String.valueOf(input.charAt(position))))
            position++;
    }
}
