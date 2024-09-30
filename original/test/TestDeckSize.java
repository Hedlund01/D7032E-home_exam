import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeckSize {

    @Test
    @Timeout(value = 5, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    public void testDeckSizeForTwoPlayers() {
        PointSalad pointSalad = new PointSalad(new String[]{"1", "1"});

        int totalDeckSize = 0;
        int nrOfPepperCards = 0;
        int nrOfLettuceCards = 0;
        int nrOfCarrotCards = 0;
        int nrOfCabbageCards = 0;
        int nrOfOnionCards = 0;
        int nrOfTomatoCards = 0;
        for (PointSalad.Player player : pointSalad.players) {
            totalDeckSize += player.hand.size();
            nrOfPepperCards += 1;
            for (PointSalad.Card card : player.hand) {
                switch (card.vegetable) {
                    case PEPPER:
                        nrOfPepperCards++;
                        break;
                    case LETTUCE:
                        nrOfLettuceCards++;
                        break;
                    case CARROT:
                        nrOfCarrotCards++;
                        break;
                    case CABBAGE:
                        nrOfCabbageCards++;
                        break;
                    case ONION:
                        nrOfOnionCards++;
                        break;
                    case TOMATO:
                        nrOfTomatoCards++;
                        break;
                }
            }
        }

        int finalTotalDeckSize = totalDeckSize;
        int finalNrOfPepperCards = nrOfPepperCards;
        int finalNrOfLettuceCards = nrOfLettuceCards;
        int finalNrOfCarrotCards = nrOfCarrotCards;
        int finalNrOfCabbageCards = nrOfCabbageCards;
        int finalNrOfOnionCards = nrOfOnionCards;
        int finalNrOfTomatoCards = nrOfTomatoCards;
        assertAll(
                () -> assertEquals(36, finalTotalDeckSize),
                () -> assertEquals(6, finalNrOfPepperCards),
                () -> assertEquals(6, finalNrOfLettuceCards),
                () -> assertEquals(6, finalNrOfCarrotCards),
                () -> assertEquals(6, finalNrOfCabbageCards),
                () -> assertEquals(6, finalNrOfOnionCards),
                () -> assertEquals(6, finalNrOfTomatoCards)
        );

    }
}
