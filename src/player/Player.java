package player;


import networkIO.commands.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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




    public void sendCommand(ICommand command) {
        if (!isBot()) {
           try {
               outToClient.writeObject(command);
              } catch (Exception e) {
                logger.error("Error sending command to player {}. Error: {}", getPlayerID(), e.getMessage());
           }
        }
    }

    @Override
    public boolean isBot() {
        return false;
    }
}