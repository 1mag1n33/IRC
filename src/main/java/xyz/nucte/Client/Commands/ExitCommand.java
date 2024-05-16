package xyz.nucte.Client.Commands;

import java.io.PrintWriter;

public class ExitCommand implements Command {
    @Override
    public void execute(PrintWriter out, String[] args) {
        System.out.println("Exiting...");
        System.exit(0);
    }
}
