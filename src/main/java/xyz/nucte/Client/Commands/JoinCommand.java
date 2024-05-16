package xyz.nucte.Client.Commands;

import xyz.nucte.msg.Message;
import xyz.nucte.msg.MessageType;

import java.io.IOException;
import java.io.PrintWriter;

public class JoinCommand implements Command {
    @Override
    public void execute(PrintWriter out, String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: /join <channel>");
            return;
        }
        try {
            Message msg = new Message(MessageType.PROTOCOL, "JOIN " + args[1]);
            System.out.println(msg.toJSON());
            out.println(msg.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
