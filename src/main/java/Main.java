import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("$ ");

            String input = scanner.nextLine();
            if(input.equals("exit 0")) {
                System.exit(0);
            }
            else if(input.contains("echo")) {
                String[] st = input.split(" ");
                for(int i = 1; i < st.length; i++) {
                    System.out.print(st[i] + " ");
                }
                System.out.println();
            }
            else {
                System.out.println(input + ": command not found");
            }
        }
    }
}
