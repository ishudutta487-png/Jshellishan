import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
      private static final Set<String> BUILTINS = Set.of("echo", "exit", "type");
        Scanner sc = new Scanner(System.in);
        while (true) {
          System.out.print("$ ");
            String c = sc.nextLine();
            if (c.equals("exit") || c.startsWith("exit ")) {
                break;
            }
            else if (c.startsWith("echo ")) {
                System.out.println(c.substring(5));
            }
            else if (c.startsWith("type ")) {
               String target = c.substring(5).trim();
               if (BUILTINS.contains(target)) {
                    System.out.println(target + " is a shell builtin");
               } else {
                    System.out.println(target + ": command not found");
               }
            }
            else {
                System.out.println(c + ": command not found");
            }
        }
    }
}
