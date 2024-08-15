import org.junit.Test;
import static org.junit.Assert.*;

public class ENFATest {
    @Test
    public void testMatching() {
        ENFA enfa = ENFA.character('a');
        assertTrue(enfa.matches("a"));
        assertFalse(enfa.matches("b"));
        // Add more tests for union, concat, star, and plus
    }
}
