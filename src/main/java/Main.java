import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class Main {
    // 1. Add "cd" to the set of builtins
    private static final Set<String> BUILTINS = Set.of("echo", "exit", "type", "pwd", "cd");

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
            } else if (command.equals("pwd")) {
                System.out.println(System.getProperty("user.dir"));
            } else if (command.equals("cd")) {
                // 2. Handle the cd command
                if (tokens.length > 1) {
                    String targetDir = tokens[1];
                    if (targetDir.equals("~")) {
                        targetDir = System.getenv("HOME");
                    }
                    Path currentDir = Paths.get(System.getProperty("user.dir"));
                    Path targetPath = currentDir.resolve(targetDir).normalize();
                    if (Files.isDirectory(targetPath)) {
                        // Change the directory by updating the system property
                        System.setProperty("user.dir", targetPath.normalize().toString());
                    } else {
                        // Print the required error format if it doesn't exist
                        System.out.println("cd: " + targetDir + ": No such file or directory");
                    }
                }
            } else {
                Path execPath = findExecutable(command);
                if (execPath != null) {
                    ProcessBuilder pb = new ProcessBuilder(tokens);
                    // 3. Ensure external processes run in the updated directory
                    pb.directory(new File(System.getProperty("user.dir")));
                    Process process = pb.inheritIO().start();
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