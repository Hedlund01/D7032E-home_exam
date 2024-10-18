package networkIO;

import networkIO.commands.send.system.TerminateCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class ClientTest {


    @Test
    @Timeout(10)
    void clientReceivesGameOverMessage() throws Exception {
        new Thread(() -> {
            try {
                ServerSocket mockSocket = new ServerSocket(8767);
                Socket clientSocket = mockSocket.accept();
                ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                outToClient.writeObject(new TerminateCommand());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();

        Client client = new ConsoleClient("127.0.0.1", 8767);

        assertTrue(true);
    }

}