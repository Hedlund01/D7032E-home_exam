package test.networkIO;

import exceptions.TooFewPlayersExcpetion;
import exceptions.TooManyPlayerException;
import networkIO.Client;
import networkIO.Server;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import player.Bot;
import player.Participant;
import player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void serverConnectsPlayersAndBotsCorrectly() throws Exception {
        AtomicReference<ArrayList<Participant>> participants = new AtomicReference<>();
        Thread x = new Thread(() -> {
            try {
                participants.setRelease(new Server(4499).startAcceptingConnections(1, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        x.start();

        Thread.sleep(1000);

        new Thread(() -> {
            try {
                new Client("127.0.0.1", 4499);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        while (x.isAlive()) {
            Thread.sleep(100);
        }


        assertAll(() -> {
            assertEquals(2, participants.get().size());
            assertInstanceOf(Player.class, participants.get().get(0));
            assertInstanceOf(Bot.class, participants.get().get(1));
        });

    }

    @Test
    void serverRejectsWhenFewerThen2Players() {
        Server server = new Server(44999);
        assertAll(() -> {
            assertThrows(TooFewPlayersExcpetion.class, () -> server.startAcceptingConnections(0, 0));
            assertThrows(TooFewPlayersExcpetion.class, () -> server.startAcceptingConnections(0, 1));
            assertThrows(TooFewPlayersExcpetion.class, () -> server.startAcceptingConnections(1, 0));
        });
    }

    @Test
    void serverRejectsWhenTooManyPlayers(){
        Server server = new Server(4499);
        assertAll(() -> {
            assertThrows(TooManyPlayerException.class, () -> server.startAcceptingConnections(7, 0));
            assertThrows(TooManyPlayerException.class, () -> server.startAcceptingConnections(6, 1));
            assertThrows(TooManyPlayerException.class, () -> server.startAcceptingConnections(1, 6));
        });
    }


}