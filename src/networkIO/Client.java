package networkIO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final ObjectOutputStream outToServer;
    private final ObjectInputStream inFromServer;

    public Client(String ipAddress, int port) throws Exception {
        this.socket = new Socket(ipAddress, port);
        this.outToServer = new ObjectOutputStream(socket.getOutputStream());
        this.inFromServer = new ObjectInputStream(socket.getInputStream());
        start();

    }

    private void start() throws Exception {
        String nextMessage = "";
        while (!nextMessage.contains("winner")) {
            nextMessage = (String) inFromServer.readObject();
            System.out.println(nextMessage);
            if (nextMessage.contains("Take") || nextMessage.contains("into")) {
                Scanner in = new Scanner(System.in);
                outToServer.writeObject(in.nextLine());
            }
        }
    }

    public void close() throws Exception {
        inFromServer.close();
        outToServer.close();
        socket.close();
    }

}
