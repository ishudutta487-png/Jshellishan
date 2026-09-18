import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Set<String> BUILTINS = Set.of("echo", "exit", "type");

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equals("exit") || input.startsWith("exit ")) {
                break;
            } else if (input.startsWith("echo ")) {
                System.out.println(input.substring(5));
            } else if (input.startsWith("type ")) {
                String target = input.substring(5).trim();
                if (BUILTINS.contains(target)) {
                    System.out.println(target + " is a shell builtin");
                } else {
                    System.out.println(target + ": not found");
                }
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }
}