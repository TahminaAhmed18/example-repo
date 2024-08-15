public class Lexer {
    private String regex;
    private int position;

    public Lexer(String regex) {
        this.regex = regex;
        this.position = 0;
    }

    public Token nextToken() {
        while (position < regex.length()) {
            char current = regex.charAt(position);
            position++;

            switch (current) {
                case '(':
                case ')':
                case '*':
                case '+':
                case '|':
                    return new Token(TokenType.SYMBOL, String.valueOf(current));
                default:
                    if (Character.isLetterOrDigit(current)) {
                        return new Token(TokenType.CHAR, String.valueOf(current));
                    } else {
                        throw new IllegalArgumentException("Invalid character: " + current);
                    }
            }
        }
        return new Token(TokenType.EOF, "");
    }
}

enum TokenType {
    SYMBOL, CHAR, EOF
}

class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
