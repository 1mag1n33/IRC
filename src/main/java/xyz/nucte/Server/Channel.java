package xyz.nucte.Server;

import xyz.nucte.msg.Message;
import xyz.nucte.msg.MessageType;

import java.util.HashSet;
import java.util.Set;

public class Channel {
    private final String name;
    private final Set<ClientHandler> clients;

    public Channel(String name) {
        this.name = name;
        this.clients = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
        Message msg = new Message(MessageType.MESSAGE, "User " + client.getClientId() + " has joined the channel.");
        broadcastMessage(msg);
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        Message msg = new Message(MessageType.MESSAGE, "User " + client.getClientId() + " has left the channel.");
        broadcastMessage(msg);
    }

    public synchronized void broadcastMessage(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}

