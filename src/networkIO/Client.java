package networkIO;

import networkIO.commands.ISendCommand;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Abstract class representing a client that connects to a server via a socket.
 */
public abstract class Client {
    private final Socket socket;
    protected final ObjectOutputStream outToServer;
    protected final ObjectInputStream inFromServer;

    /**
     * Constructs a Client object and initializes the socket connection.
     *
     * @param ipAddress the IP address of the server to connect to
     * @param port the port number of the server to connect to
     * @throws Exception if an error occurs during socket creation or stream initialization
     */
    public Client(String ipAddress, int port) throws Exception {
        this.socket = new Socket(ipAddress, port);
        this.outToServer = new ObjectOutputStream(socket.getOutputStream());
        this.inFromServer = new ObjectInputStream(socket.getInputStream());
        start();
    }

    /**
     * Starts the client and processes commands received from the server.
     * This method should be implemented by subclasses to define specific behavior.
     */
    protected abstract void start();

    /**
     * Closes the client connection, including input and output streams and the socket.
     *
     * @throws Exception if an error occurs while closing the streams or socket
     */
    public void close() throws Exception {
        inFromServer.close();
        outToServer.close();
        socket.close();
    }

    /**
     * Sends a command to the server.
     *
     * @param command the command to send to the server
     */
    void sendCommand(ISendCommand command) {
        try {
            command.execute(outToServer);
        } catch (Exception e) {
            System.out.println("Error sending command to server. Error: " + e.getMessage());
        }
    }
}