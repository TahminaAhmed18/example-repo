import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void testParsing() {
        Lexer lexer = new Lexer("(ab)*|c+");
        Parser parser = new Parser(lexer);
        ENFA enfa = parser.parse();
        // Add assertions to verify the structure of the generated ENFA
    }
}
