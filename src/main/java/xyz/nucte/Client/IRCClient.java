package xyz.nucte.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.nucte.msg.Message;
import xyz.nucte.CommandParser;
import xyz.nucte.msg.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class IRCClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6667;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CommandParser commandParser = new CommandParser();

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to IRC server");

            Thread messageListener = new Thread(() -> {
                try {
                    Message message;
                    while ((message = Message.deserialize(in.readLine())) != null) {
                        switch (message.getType()) {
                            case INFO:
                                System.out.println("Info: " + message.getContent());
                                break;
                            case MESSAGE:
                                System.out.println("Message: " + message.getContent());
                                break;
                            case ERROR:
                                System.out.println("Error: " + message.getContent());
                                break;
                            case PROTOCOL:
                                System.out.println("Protocol: " + message.getContent());
                                break;
                            default:
                                System.out.println("Unknown message type: " + message.getType());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            messageListener.start();


            // Read input from the user and send it to the server or execute a command
            String userInput;
            while (true) {
                System.out.print("Enter message to send to server (or type '/help' for commands): ");
                userInput = scanner.nextLine();
                if (userInput.startsWith("/")) {
                    commandParser.parseAndExecute(userInput, out);
                } else {
                    sendMessage(out, userInput);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(PrintWriter out, String message) {
        try {
            Message jsonMessage = new Message(MessageType.MESSAGE, message);
            String jsonString = objectMapper.writeValueAsString(jsonMessage);
            out.println(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



