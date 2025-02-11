import java.io.File;
import java.io.IOException;
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
            String input = scanner.nextLine().trim();

            if (input.equals("exit 0")) {
                System.exit(0);
            } else if (input.startsWith(TYPE_COMMAND)) {
                handleTypeCommand(input, shellBuiltinCommands);
            } else if (input.startsWith("echo")) {
                handleEchoCommand(input);
            } else {
                handleExternalCommand(input, shellBuiltinCommands);
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

        String commandPath = findCommandInPath(command);
        if(commandPath != null) {
            System.out.println(command + " is " + commandPath);
        } else {
            System.out.println(command + ": not found");
        }
    }

    private static String findCommandInPath(String command) {
        String pathEnv = System.getenv("PATH");
        if(pathEnv == null || pathEnv.isEmpty()) {
            return null;
        }

        String[] pathDirectories = pathEnv.split(":");
        for(String dir : pathDirectories) {
            File file = new File(dir, command);
            if(file.exists() && file.canExecute()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    private static void handleExternalCommand(String input, List<String> shellBuiltinCommands) {
        String[] tokens = input.split(" ");
        String command = tokens[0];

        if(shellBuiltinCommands.contains(command)) {
            System.out.println("Unrecognized builtin command: " + command);
            return;
        }

        String commandPath = findCommandInPath(command);
        if(commandPath == null) {
            System.out.println(command + ": not found");
            return;
        }

        //Execute the external program
        try {
            List<String> commandWithArgs = new ArrayList<>();
            commandWithArgs.add(commandPath);
            commandWithArgs.addAll(Arrays.asList(tokens).subList(1, tokens.length));

            ProcessBuilder processBuilder = new ProcessBuilder(commandWithArgs);
            processBuilder.redirectErrorStream(true); //Merge stdout and stderr
            Process process = processBuilder.start();

            //Print the output from the executed command
            Scanner processOutput = new Scanner(process.getInputStream());
            while(processOutput.hasNextLine()) {
                String outputLine = processOutput.nextLine();

                //Replace the absolute path with the command name
                if (outputLine.startsWith("Arg #0 (program name): ")) {
                    outputLine = outputLine.replace(commandPath, command); // Swap full path with command name
                }

                System.out.println(outputLine);
            }
            process.waitFor();
        } catch(IOException | InterruptedException e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }

    private static void handleEchoCommand(String input) {
        String[] tokens = input.split(" ");
        if (tokens.length > 1) {
            System.out.println(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
        }
    }
}