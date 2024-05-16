package xyz.nucte.Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IRCServer {
    public static final Map<String, Channel> channels = new ConcurrentHashMap<>();
    private static final int SERVER_PORT = 6667;
    private static final Logger logger = LogManager.getLogger(IRCServer.class);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            logger.info("IRC server started on port " + SERVER_PORT);
            channels.put("#default", new Channel("#default"));
            channels.put("#general", new Channel("#general"));
            while (true) {
                Socket clientSocket = serverSocket.accept();
                UUID clientId = UUID.randomUUID();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientId);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            logger.error("Error starting the IRC server", e);
        }
    }


}





