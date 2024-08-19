public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public ENFA parse() {
        return expression();
    }

    private ENFA expression() {
        return term();
    }

    private ENFA term() {
        if (currentToken.getType() == TokenType.CHAR) {
            char c = currentToken.getValue().charAt(0);
            currentToken = lexer.nextToken();
            return ENFA.character(c);
        }
        // Handle other cases like |, *, etc.
        throw new IllegalArgumentException("Unexpected token: " + currentToken.getValue());
    }
}
