package networkIO;

import exceptions.TooFewPlayersExcpetion;
import exceptions.TooManyPlayerException;
import networkIO.commands.send.DisplayStringCommand;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Player;
import player.Participant;
import player.Bot;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private ServerSocket serverSocket;
    private static final Logger logger = LogManager.getLogger();

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            logger.error("Failed to create server socket: {}", e.getMessage());
        }
    }

    public ArrayList<Participant> startAcceptingConnections(int players, int bots) {
        try(final CloseableThreadContext.Instance ctc = CloseableThreadContext
                .put("server", "acceptingConnections")
                .put("nrOfPlayers", Integer.toString(players))
                .put("nrOfBots", Integer.toString(bots))
        ) {
            if(players + bots < 2) {
                logger.error("Cannot start game with fewer than 2 players");
                throw new TooFewPlayersExcpetion("Cannot start game with fewer than 2 players");
            }

            if(players + bots > 6){
                logger.error("Cannot start game with more than 6 players");
                throw new TooManyPlayerException("Cannot start game with more than 6 players");
            }

            ArrayList<Participant> playerList = new ArrayList<>();
            int playerId = 1;
            // Accept players
            logger.info("Waiting for {} players to connect...", players);
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
    }

    private Participant handleConnection(int playerId, boolean isBot) {
        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext
                .put("server", "handleConnection")
                .put("playerId", Integer.toString(playerId))
                .put("isBot", Boolean.toString(isBot))
        ) {
            try {
                Socket connectionSocket = isBot ? null : serverSocket.accept();
                ObjectInputStream inFromClient = isBot ? null : new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = isBot ? null : new ObjectOutputStream(connectionSocket.getOutputStream());
                if (!isBot) {
                    logger.info("Player connected");
                    Player player = new Player(playerId, connectionSocket, inFromClient, outToClient);
                    player.sendCommand(new DisplayStringCommand("You connected to the server as player " + playerId + "\n"));
                    return player;

                } else {
                    logger.info("Bot connected");
                    return new Bot(playerId);
                }
            } catch (Exception e) {
                logger.error("Failed to connect to player, error: {}", e.getMessage());
            }
            return null;
        }
    }
}
