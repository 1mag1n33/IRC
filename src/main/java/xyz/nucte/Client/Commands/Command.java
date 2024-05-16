package xyz.nucte.Client.Commands;

import java.io.PrintWriter;

public interface Command {
    void execute(PrintWriter out, String[] args);
}
