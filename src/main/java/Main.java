import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
          System.out.print("$ ");
            String c = sc.nextLine();
            if (c.equals("exit")) {
                break;
            }
            else if (c.startsWith("echo ")) {
                System.out.println(c.substring(5));
            }
            else {
                System.out.println(c + ": command not found");
            }
        }
    }
}
