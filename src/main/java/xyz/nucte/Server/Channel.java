package xyz.nucte.Server;

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
        broadcastMessage("User " + client.getClientId() + " has joined the channel.");
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcastMessage("User " + client.getClientId() + " has left the channel.");
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}

