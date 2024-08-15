import java.util.Scanner;

public class RegexEngine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String regex = scanner.nextLine();

        Lexer lexer = new Lexer(regex);
        Parser parser = new Parser(lexer);
        ENFA enfa = parser.parse();

        System.out.println("ready");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            boolean match = enfa.matches(input);
            System.out.println(match);
        }
    }
}
