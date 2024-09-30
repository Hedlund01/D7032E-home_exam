package Player;


import Card.Card;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Player<T extends Enum<T>> extends Participant<T> {
    private final Socket connection;
    private final ObjectInputStream inFromClient;
    private final ObjectOutputStream outToClient;


    public Player(int playerID, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient, Class<T> faceClass) {
        super(playerID, faceClass);
        this.connection = connection;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }



    public void sendMessage(Object message) {
        if (!isBot()) {
            try {
                outToClient.writeObject(message);
            } catch (Exception e) {
            }
        }
    }

    public String readMessage() {
        String word = "";
        if (!isBot()) {
            try {
                word = (String) inFromClient.readObject();
            } catch (Exception e) {
            }
        }
        return word;
    }

    @Override
    public boolean isBot() {
        return false;
    }
}