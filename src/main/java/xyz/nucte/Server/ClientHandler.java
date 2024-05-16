package xyz.nucte.Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.nucte.CommandParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.nucte.msg.Message;
import xyz.nucte.msg.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final UUID clientId;
    private Channel currentChannel;
    private final Logger logger = LogManager.getLogger(IRCServer.class);
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket, UUID clientId) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            logger.info("Handling client " + clientId);
            Message WelcomeMessage = new Message(MessageType.INFO, "Welcome! Your client ID is " + clientId);
            out.println(WelcomeMessage.toJSON());

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                try {
                    Message message = Message.deserialize(inputLine);
                    if (MessageType.PROTOCOL.equals(message.getType())) {
                        ProtocolHandler(message.getContent());
                    } else {
                        if (currentChannel != null) {
                            Message msg = new Message(MessageType.INFO, "[" + clientId + "]: " + message.getContent());
                            currentChannel.broadcastMessage(msg);
                        } else {
                            Message msg = new Message(MessageType.ERROR, "You are not in any channel. Use /join <channel> to join a channel.");
                            sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    logger.fatal("Failed to deserialize message: " + e.getMessage());
                    out.println("Error: Invalid message format.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(Message msg) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println(msg.toJSON());
        } catch (IOException e) {
            logger.error("Error sending message ", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void ProtocolHandler(String command) {
        String[] tokens = command.split(" ");
        switch (tokens[0]) {
            case "JOIN":

                if (tokens.length < 2) {
                    out.println("Usage: /join <channel>");
                    return;
                }
                joinChannel(tokens[1]);
                break;
            case "LEAVE":
                leaveChannel();
                break;
            default:
                out.println("Unknown protocol command: " + tokens[0]);
                break;
        }
    }

    private void joinChannel(String channelName) {
        Channel channel = IRCServer.channels.get(channelName);
        if (channel == null) {
            Message msg = new Message(MessageType.ERROR, "Channel " + channelName + " does not exist.");
            sendMessage(msg);
            return;
        }
        synchronized (IRCServer.channels) {
            if (currentChannel != null) {
                currentChannel.removeClient(this);
            }
            currentChannel = channel;
            currentChannel.addClient(this);
        }
        Message msg = new Message(MessageType.INFO, "Joined channel " + channelName);
        sendMessage(msg);
    }


    private void leaveChannel() {
        if (currentChannel != null) {
            currentChannel.removeClient(this);
            Message msg = new Message(MessageType.INFO, "Left channel " + currentChannel.getName());
            sendMessage(msg);
            currentChannel = null;
        } else {
            Message msg = new Message(MessageType.ERROR, "You are not in any channel.");
            sendMessage(msg);
        }
    }

    public UUID getClientId() {
        return clientId;
    }


}
