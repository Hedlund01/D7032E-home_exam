package networkIO;

import exceptions.TooFewPlayersExcpetion;
import exceptions.TooManyPlayerException;
import org.junit.jupiter.api.Test;
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
                participants.setRelease(new Server(1234).startAcceptingConnections(1, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        x.start();

        Thread.sleep(1000);

        new Thread(() -> {
            try {
                new Client("127.0.0.1", 1234);
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
        Server server = new Server(2345);
        assertAll(() -> {
            assertThrows(TooFewPlayersExcpetion.class, () -> server.startAcceptingConnections(0, 0));
            assertThrows(TooFewPlayersExcpetion.class, () -> server.startAcceptingConnections(0, 1));
            assertThrows(TooFewPlayersExcpetion.class, () -> server.startAcceptingConnections(1, 0));
        });
    }

    @Test
    void serverRejectsWhenTooManyPlayers(){
        Server server = new Server(3456);
        assertAll(() -> {
            assertThrows(TooManyPlayerException.class, () -> server.startAcceptingConnections(7, 0));
            assertThrows(TooManyPlayerException.class, () -> server.startAcceptingConnections(6, 1));
            assertThrows(TooManyPlayerException.class, () -> server.startAcceptingConnections(1, 6));
        });
    }


}