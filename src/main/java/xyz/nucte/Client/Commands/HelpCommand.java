package xyz.nucte.Client.Commands;

import java.io.PrintWriter;

public class HelpCommand implements Command {
    @Override
    public void execute(PrintWriter out, String[] args) {
        System.out.println("Available commands:");
        System.out.println("/help - Show this help message");
        System.out.println("/exit - Exit the chat");
        // Add other commands here
    }
}
