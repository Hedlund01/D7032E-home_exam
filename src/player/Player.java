package player;


import networkIO.commands.ICommand;
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


    public void sendCommand(ICommand command) {
        try {
            outToClient.writeObject(command);
            outToClient.reset();
        } catch (Exception e) {
            logger.error("Error sending command to player {}. Error: {}", getPlayerID(), e.getMessage());
        }
    }

    public ICommand readCommand() {
        try {
            var input = inFromClient.readObject();
            if (input instanceof ICommand) {
                return (ICommand) input;
            } else {
                logger.error("Error reading command from player {}. Error: {}", getPlayerID(), "Command is not an instance of ICommand");
                return null;
            }
        } catch (SocketTimeoutException e) {
            logger.warn("Player {} did not respond in time", getPlayerID());
            return null;
        } catch (Exception e) {
            logger.error("Error reading command from player {}. Error: {}", getPlayerID(), e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isBot() {
        return false;
    }
}