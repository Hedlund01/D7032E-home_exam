package IONetwork;

import Player.Player;
import Player.Participant;
import Player.Bot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server<T extends Enum<T>> {
    private final Class<T> faceClass;
    public ServerSocket serverSocket;

    public Server(int port, Class<T> faceClass) {
        this.faceClass = faceClass;
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Participant<T>> startAcceptingConnections(int players, int bots) {
        ArrayList<Participant<T>> playerList = new ArrayList<>();
        new Thread(() -> {
            int playerId = 1;
            // Accept players
            for (int i = 0; i < players; i++) {
                Participant<T> player = handleConnection(playerId, false);
                playerList.add(player);
                playerId++;
            }
            // Connect bots
            for (int i = 0; i < bots; i++) {
                Participant<T> player = handleConnection(playerId, true);
                playerList.add(player);
                playerId++;
            }
        }).start();
        return playerList;
    }

    private Participant<T> handleConnection(int playerId, boolean isBot) {
        try {
            Socket connectionSocket = isBot ? null : serverSocket.accept();
            ObjectInputStream inFromClient = isBot ? null : new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = isBot ? null : new ObjectOutputStream(connectionSocket.getOutputStream());
            if (!isBot) {
                System.out.println("Connected to player " + playerId);
                Player<T> player = new Player<T>(playerId, connectionSocket, inFromClient, outToClient, faceClass);
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
