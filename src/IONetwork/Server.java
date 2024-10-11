package IONetwork;

import Player.Player;
import Player.Participant;
import Player.Bot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Participant> startAcceptingConnections(int players, int bots) {
        ArrayList<Participant> playerList = new ArrayList<>();
        int playerId = 1;
        // Accept players
        System.out.println("Waiting for " + players + " players to connect...");
        for (int i = 0; i < players; i++) {
            Participant player = handleConnection(playerId, false);
            playerList.add(player);
            playerId++;
        }
        // Connect bots
        for (int i = 0; i < bots; i++) {
            Participant player = handleConnection(playerId, true);
            playerList.add(player);
            playerId++;
        }
        return playerList;
    }

    private Participant handleConnection(int playerId, boolean isBot) {
        try {
            Socket connectionSocket = isBot ? null : serverSocket.accept();
            ObjectInputStream inFromClient = isBot ? null : new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = isBot ? null : new ObjectOutputStream(connectionSocket.getOutputStream());
            if (!isBot) {
                System.out.println("Connected to player " + playerId);
                Player player = new Player(playerId, connectionSocket, inFromClient, outToClient);
                outToClient.writeObject("You connected to the server as player " + playerId + "\n");
                player.sendMessage("You connected to the server as player " + playerId + "\n");
                return player;

            } else {
                System.out.println("Bot " + playerId + " connected");
                return new Bot(playerId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
