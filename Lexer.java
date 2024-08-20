// Lexer class to tokenize a regular expression string
public class Lexer {
    // The regex string to be tokenized
    private final String regex;
    // The current position in the regex string
    private int position;

    // Constructor to initialize the regex string and position
    public Lexer(String regex) {
        this.regex = regex;
        this.position = 0; // Start position is set to 0
    }

    // Method to get the next token from the regex string
    public Token nextToken() {
        // Loop through the regex string until the end is reached
        while (position < regex.length()) {
            // Get the current character at the position
            char current = regex.charAt(position);
            // Move to the next position
            position++;


            // Switch statement to determine the type of the current character
            switch (current) {
                case '(':
                    return new Token(TokenType.LPAREN, String.valueOf(current));
                case ')':
                    return new Token(TokenType.RPAREN, String.valueOf(current));
                case '*':
                    return new Token(TokenType.STAR, String.valueOf(current));
                case '+':
                    return new Token(TokenType.PLUS, String.valueOf(current));
                case '|':
                    return new Token(TokenType.OR, String.valueOf(current));
                // Default case for other characters
                default:
                    // If the character is a letter or digit, create a CHAR token
                    if (Character.isLetterOrDigit(current)) {
                        return new Token(TokenType.CHAR, String.valueOf(current));
                    } else {
                        // Throw an exception if the character is invalid
                        throw new IllegalArgumentException("Invalid character: " + current);
                    }
            }
        }
        // Return an EOF token when the end of the regex string is reached
        return new Token(TokenType.EOF, "");
    }
}

// Class to represent a token
class Token {
    // The type of the token
    private TokenType type;
    // The value of the token
    private String value;

    // Constructor to initialize the token type and value
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getter method to get the type of the token
    public TokenType getType() {
        return type;
    }

    // Getter method to get the value of the token
    public String getValue() {
        return value;
    }
}
