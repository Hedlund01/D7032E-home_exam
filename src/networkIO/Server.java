package networkIO;

import exceptions.TooFewPlayersExcpetion;
import exceptions.TooManyPlayerException;
import networkIO.commands.send.system.DisplayConnectionMessageSendCommand;
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

/**
 * Server class to handle player and bot connections.
 * This class manages the server socket and handles the connection logic for players and bots.
 */
public class Server {
    private ServerSocket serverSocket;
    private static final Logger logger = LogManager.getLogger();

    /**
     * Constructs a Server object with the specified port.
     *
     * @param port the port number to bind the server socket to
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            logger.error("Failed to create server socket: {}", e.getMessage());
        }
    }

    /**
     * Starts accepting connections for the specified number of players and bots.
     * This method waits for the specified number of players and bots to connect to the server.
     * It then creates a socket connection, input/output streams, and a player or bot object for each participant.
     *
     *
     * @param players the number of players to accept
     * @param bots the number of bots to connect
     * @return a list of participants (players and bots)
     * @throws TooFewPlayersExcpetion if the total number of players and bots is less than 2
     * @throws TooManyPlayerException if the total number of players and bots is more than 6
     */
    public ArrayList<Participant> startAcceptingConnections(int players, int bots) {
        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext
                .put("server", "acceptingConnections")
                .put("nrOfPlayers", Integer.toString(players))
                .put("nrOfBots", Integer.toString(bots))
        ) {
            if (players + bots < 2) {
                logger.error("Cannot start game with fewer than 2 players");
                throw new TooFewPlayersExcpetion("Cannot start game with fewer than 2 players");
            }

            if (players + bots > 6) {
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

    /**
     * Handles the connection for a player or bot by creating a socket connection, input/output streams and
     * a player or bot object.
     *
     * @param playerId the ID of the player or bot
     * @param isBot whether the connection is for a bot
     * @return the connected participant (player or bot)
     */
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
                    player.sendCommand(new DisplayConnectionMessageSendCommand(player.getName(), player.getPlayerID()));
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