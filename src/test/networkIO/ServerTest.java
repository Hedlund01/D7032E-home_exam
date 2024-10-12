package test.networkIO;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerTest {

    @Test
    void startAcceptingConnectionsWithPlayersAndBots() throws Exception {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        Socket mockSocket = mock(Socket.class);
        ObjectInputStream mockInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream mockOutputStream = mock(ObjectOutputStream.class);

        when(mockServerSocket.accept()).thenReturn(mockSocket);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        Server server = new Server(8080);
        server.serverSocket = mockServerSocket;

        ArrayList<Participant> participants = server.startAcceptingConnections(2, 1);

        assertEquals(3, participants.size());
        assertTrue(participants.get(0) instanceof Player);
        assertTrue(participants.get(1) instanceof Player);
        assertTrue(participants.get(2) instanceof Bot);
    }

    @Test
    void handleConnectionWithPlayer() throws Exception {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        Socket mockSocket = mock(Socket.class);
        ObjectInputStream mockInputStream = mock(ObjectInputStream.class);
        ObjectOutputStream mockOutputStream = mock(ObjectOutputStream.class);

        when(mockServerSocket.accept()).thenReturn(mockSocket);
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        Server server = new Server(8080);
        server.serverSocket = mockServerSocket;

        Participant participant = server.handleConnection(1, false);

        assertNotNull(participant);
        assertTrue(participant instanceof Player);
    }

    @Test
    void handleConnectionWithBot() {
        Server server = new Server(8080);

        Participant participant = server.handleConnection(1, true);

        assertNotNull(participant);
        assertTrue(participant instanceof Bot);
    }

    @Test
    void handleConnectionFailsGracefully() throws Exception {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenThrow(new RuntimeException("Connection failed"));

        Server server = new Server(8080);
        server.serverSocket = mockServerSocket;

        Participant participant = server.handleConnection(1, false);

        assertNull(participant);
    }
}