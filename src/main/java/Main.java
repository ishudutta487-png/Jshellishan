import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final Set<String> BUILTINS = Set.of("echo", "exit", "type");

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            if (!sc.hasNextLine()) {
                break;
            }

            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            if (command.equals("exit")) {
                break;
            } else if (command.equals("echo")) {
                if (tokens.length > 1) {
                    System.out.println(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
                } else {
                    System.out.println();
                }
            } else if (command.equals("type")) {
                if (tokens.length > 1) {
                    handleType(tokens[1]);
                }
            } else {
                Path execPath = findExecutable(command);
                if (execPath != null) {
                    // FIX: Pass the original tokens directly. 
                    // ProcessBuilder will use PATH to find the executable and pass the short name as arg 0.
                    Process process = new ProcessBuilder(tokens).inheritIO().start();
                    process.waitFor();
                } else {
                    System.out.println(command + ": command not found");
                }
            }
        }
    }

    private static void handleType(String cmd) {
        if (BUILTINS.contains(cmd)) {
            System.out.println(cmd + " is a shell builtin");
            return;
        }

        Path execPath = findExecutable(cmd);
        if (execPath != null) {
            System.out.println(cmd + " is " + execPath);
        } else {
            System.out.println(cmd + ": not found");
        }
    }

    private static Path findExecutable(String cmd) {
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null) {
            return null;
        }

        String[] directories = pathEnv.split(File.pathSeparator);
        for (String dir : directories) {
            Path filePath = Paths.get(dir, cmd);
            if (Files.isRegularFile(filePath) && Files.isExecutable(filePath)) {
                return filePath;
            }
        }
        return null;
    }
}