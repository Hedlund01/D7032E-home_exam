import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPlayer {

    @Test
    public void testLessThenTwoPlayers() {
        assertThrows(Exception.class, () -> {
            new PointSalad(new String[]{"1", "0"});
        });
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    public void testMoreThenSixPlayers() {
        assertThrows(Exception.class, () -> {
            new PointSalad(new String[]{"1","6"});
        });
    }
}
