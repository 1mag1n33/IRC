package xyz.nucte.Client.Commands;


import xyz.nucte.msg.Message;
import xyz.nucte.msg.MessageType;

import java.io.PrintWriter;

public class LeaveCommand implements Command {
    @Override
    public void execute(PrintWriter out, String[] args) {
        try {
            Message msg = new Message(MessageType.PROTOCOL, "LEAVE");
            out.println(msg.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}