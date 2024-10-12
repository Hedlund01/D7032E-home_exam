package player;


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



    public void sendMessage(Object message) {
        if (!isBot()) {
            try {
                outToClient.writeObject(message);
            } catch (Exception e) {
                logger.error("Error sending message to player {}. Error: {}", getPlayerID(), e.getMessage());
            }
        }
    }

    public String readMessage() {
        String word = "";
        if (!isBot()) {
            try {
                word = (String) inFromClient.readObject();
            } catch (Exception e) {
                logger.error("Error reading message from player {}. Error: {}", getPlayerID(), e.getMessage());
            }
        }
        return word;
    }

    @Override
    public boolean isBot() {
        return false;
    }
}