import java.util.Scanner;

public class RegexEngine {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            // Read the regular expression from standard input
            String regex = scanner.nextLine();

            // Parse the regex using Lexer and Parser
            Lexer lexer = new Lexer(regex);
            Parser parser = new Parser(lexer);
            ENFA enfa = parser.parse();

            // Check for verbose mode
            boolean verbose = args.length > 0 && args[0].equals("-v");

            if (verbose) {
                enfa.printTransitionTable();
            }

            System.out.println("ready");

            // Process each input string
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                boolean match = enfa.matches(input);

                if (verbose) {
                    System.out.println("Input: " + input);
                    System.out.println("Match: " + match);
                } else {
                    System.out.println(match);
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
