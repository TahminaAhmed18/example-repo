public class Parser {
    private Lexer lexer;
    private Token currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public ENFA parse() {
        ENFA nfa = expression();
        if (currentToken.getType() != TokenType.EOF) {
            throw new IllegalArgumentException("Unexpected token: " + currentToken.getValue());
        }
        return nfa;
    }

    private ENFA expression() {
        ENFA termNFA = term();
        while (currentToken.getValue().equals("|")) {
            currentToken = lexer.nextToken();
            ENFA rightTermNFA = term();
            termNFA = ENFA.union(termNFA, rightTermNFA);
        }
        return termNFA;
    }

    private ENFA term() {
        ENFA factorNFA = factor();
        while (currentToken.getType() == TokenType.CHAR || currentToken.getValue().equals("(")) {
            ENFA nextFactorNFA = factor();
            factorNFA = ENFA.concat(factorNFA, nextFactorNFA);
        }
        return factorNFA;
    }

    private ENFA factor() {
        ENFA baseNFA = base();
        if (currentToken.getValue().equals("*")) {
            currentToken = lexer.nextToken();
            baseNFA = ENFA.star(baseNFA);
        } else if (currentToken.getValue().equals("+")) {
            currentToken = lexer.nextToken();
            baseNFA = ENFA.plus(baseNFA);
        }
        return baseNFA;
    }

    private ENFA base() {
        if (currentToken.getValue().equals("(")) {
            currentToken = lexer.nextToken();
            ENFA exprNFA = expression();
            if (!currentToken.getValue().equals(")")) {
                throw new IllegalArgumentException("Expected closing parenthesis");
            }
            currentToken = lexer.nextToken();
            return exprNFA;
        } else if (currentToken.getType() == TokenType.CHAR) {
            ENFA charNFA = ENFA.character(currentToken.getValue().charAt(0));
            currentToken = lexer.nextToken();
            return charNFA;
        } else {
            throw new IllegalArgumentException("Unexpected token: " + currentToken.getValue());
        }
    }
}
