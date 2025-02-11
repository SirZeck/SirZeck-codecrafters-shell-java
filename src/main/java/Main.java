import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String TYPE_COMMAND = "type";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        List<String> shellBuiltinCommands = Arrays.asList("echo", "type", "exit");

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();

            if (input.equals("exit 0")) {
                System.exit(0);
            } else if (input.startsWith(TYPE_COMMAND)) {
                handleTypeCommand(input, shellBuiltinCommands);
            } else if (input.startsWith("echo")) {
                handleEchoCommand(input);
            } else {
                System.out.println(input + ": command not found");
            }
        }
    }

    private static void handleTypeCommand(String input, List<String> shellBuiltinCommands) {
        String[] tokens = input.split(" ");
        if (tokens.length < 2) {
            System.out.println("Error: type command requires an argument");
            return;
        }

        String command = tokens[1];

        // Check if the command is a shell builtin
        if (shellBuiltinCommands.contains(command)) {
            System.out.println(command + " is a shell builtin");
            return;
        }

        // Check if the command exists in the PATH
        String pathEnv = System.getenv("PATH");
        if (pathEnv == null || pathEnv.isEmpty()) {
            System.out.println(command + ": not found");
            return;
        }

        String[] pathDirectories = pathEnv.split(":");
        for (String dir : pathDirectories) {
            File file = new File(dir, command);
            if (file.exists() && file.canExecute()) {
                System.out.println(command + " is " + file.getAbsolutePath());
                return;
            }
        }

        // Command not found
        System.out.println(command + ": not found");
    }

    private static void handleEchoCommand(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length > 1) {
            System.out.println(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
        }
    }
}