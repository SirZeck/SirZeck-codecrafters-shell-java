import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("$ ");

            List<String> shellBuiltinCommands = new ArrayList<>();
            shellBuiltinCommands.add("echo");
            shellBuiltinCommands.add("type");
            shellBuiltinCommands.add("exit");

            String input = scanner.nextLine();
            if(input.equals("exit 0")) {
                System.exit(0);
            }
            else if(input.contains("type")) {
                String[] st = input.split(" ");
                if(shellBuiltinCommands.contains(st[1])) {
                    System.out.println(st[1] + " is a shell builtin");
                } else {
                    System.out.println(st[1] + ": not found");
                }
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
