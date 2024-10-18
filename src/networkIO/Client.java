package networkIO;

import networkIO.commands.ICommand;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Client {
    private final Socket socket;
    protected final ObjectOutputStream outToServer;
    protected final ObjectInputStream inFromServer;

    public Client(String ipAddress, int port) throws Exception {
        this.socket = new Socket(ipAddress, port);
        this.outToServer = new ObjectOutputStream(socket.getOutputStream());
        this.inFromServer = new ObjectInputStream(socket.getInputStream());
        start();
    }

    protected abstract void start();
    public void close() throws Exception {
        inFromServer.close();
        outToServer.close();
        socket.close();
    }

    void sendCommand(ICommand command) {
        try {
            outToServer.writeObject(command);
        } catch (Exception e) {
            System.out.println("Error sending command to server. Error: " + e.getMessage());
        }
    }

}
