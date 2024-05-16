package xyz.nucte;

import xyz.nucte.Client.Commands.*;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    private final Map<String, Command> commands;

    public CommandParser() {
        commands = new HashMap<>();
        commands.put("/help", new HelpCommand());
        commands.put("/exit", new ExitCommand());
        commands.put("/join", new JoinCommand());
        commands.put("/leave", new LeaveCommand());
    }

    public void parseAndExecute(String input, PrintWriter out) {
        String[] tokens = input.split(" ");
        String command = tokens[0];
        Command cmd = commands.get(command);

        if (cmd != null) {
            cmd.execute(out, tokens);
        } else {
            out.println("Unknown command: " + command);
        }
    }
}

