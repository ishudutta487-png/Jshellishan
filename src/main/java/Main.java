import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                handleType(target);
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }

    private static void handleType(String command) {
        if (BUILTINS.contains(command)) {
            System.out.println(command + " is a shell builtin");
            return;
        }

        String pathEnv = System.getenv("PATH");
        if (pathEnv != null) {
            String[] directories = pathEnv.split(File.pathSeparator);
            for (String dir : directories) {
                Path filePath = Paths.get(dir, command);
                if (Files.isRegularFile(filePath) && Files.isExecutable(filePath)) {
                    System.out.println(command + " is " + filePath);
                    return;
                }
            }
        }

        System.out.println(command + ": not found");
    }
}