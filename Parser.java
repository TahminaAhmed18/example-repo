public class Parser {
    private final Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public ENFA parse() {
        return expression();
    }

    private ENFA expression() {
        ENFA term = term();
        while (currentToken.getType() == TokenType.OR) {  // Handle '|'
            currentToken = lexer.nextToken();
            ENFA nextTerm = term();
            term = ENFA.union(term, nextTerm);
        }
        return term;
    }

    private ENFA term() {
        ENFA factor = factor();
        while (currentToken.getType() == TokenType.CHAR) {  // Handle concatenation
            ENFA nextFactor = factor();
            factor = ENFA.concatenate(factor, nextFactor);
        }
        return factor;
    }

    private ENFA factor() {
        ENFA base = base();
        while (currentToken.getType() == TokenType.STAR || currentToken.getType() == TokenType.PLUS) {
            if (currentToken.getType() == TokenType.STAR) {
                base = ENFA.kleeneStar(base);
            } else if (currentToken.getType() == TokenType.PLUS) {
                base = ENFA.oneOrMore(base);
            }
            currentToken = lexer.nextToken();
        }
        return base;
    }

    private ENFA base() {
        if (currentToken.getType() == TokenType.CHAR ) {
            char c = currentToken.getValue().charAt(0);
            currentToken = lexer.nextToken();
            return ENFA.character(c);
        } else if (currentToken.getType() == TokenType.LPAREN) {  // Handle '(' expression ')'
            currentToken = lexer.nextToken();
            ENFA expr = expression();
            if (currentToken.getType() != TokenType.RPAREN) {
                throw new IllegalArgumentException("Expected closing parenthesis");
            }
            currentToken = lexer.nextToken();
            return expr;
        } else if (currentToken.getType() == TokenType.STAR) {
            char c = currentToken.getValue().charAt(0);
            currentToken = lexer.nextToken();
            return ENFA.kleeneStar(ENFA.character(c));
        }
        throw new IllegalArgumentException("Unexpected token: " + currentToken.getValue());
    }
}
