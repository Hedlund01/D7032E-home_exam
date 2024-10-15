package player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {

    Bot bot;

    @BeforeEach
    void setUp() {
        bot = new Bot(1);
    }

    @Test
    void isBot() {
        assertTrue(bot.isBot());
    }
}