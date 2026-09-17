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
            System.out.println(c + ": command not found");
        }
    }
}
