package xyz.nucte;

import xyz.nucte.Client.IRCClient;
import xyz.nucte.Server.IRCServer;

import java.io.IOException;

public class IRC {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java IRC --server|--client");
            return;
        }

        if (args[0].equals("--server")) {
            IRCServer.main(args);
        } else if (args[0].equals("--client")) {
            IRCClient.main(args);
        } else {
            System.out.println("Invalid argument. Use --server or --client");
        }
    }
}