package player;


import networkIO.commands.ISendCommand;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Player extends Participant {
    private static final Logger logger = LogManager.getLogger();
    private final Socket connection;
    private final ObjectInputStream inFromClient;
    private final ObjectOutputStream outToClient;


    public Player(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient) {
        super(playerID);
        this.connection = connection;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    public void setReadTimeout(int timeout) {
        try {
            connection.setSoTimeout(timeout);
        } catch (Exception e) {
            logger.error("Error setting read timeout for player {}. Error: {}", getPlayerID(), e.getMessage());
        }
    }


    public void sendCommand(ISendCommand command) {
        command.execute(outToClient);
    }

    public ISendCommand readCommand() {
        try(var _ = CloseableThreadContext.put("playerID", String.valueOf(getPlayerID()))) {

            try {
                var input = inFromClient.readObject();
                if (input instanceof ISendCommand cmd) {
                    return cmd;
                } else {
                    logger.error("Error reading command from player. Error: {}", "Command is not an instance of ICommand");
                }
            } catch (SocketTimeoutException e) {
                logger.warn("Player did not respond in time");
            } catch (Exception e) {
                logger.error("Error reading command from player. Error: {}", e.getMessage());
            }
            return null;
        }
    }

}