package card;

import exceptions.NotImplementedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CardFactoryTest {

    @Test
    void getCardFactory() {
        CardFactory factory = new CardFactory();
        assertAll(() -> {
            assertInstanceOf(VegetableCardFactory.class, factory.getCardFactory(Vegetable.class.getName()));
            assertThrows(NotImplementedException.class, () -> factory.getCardFactory("card.Point"));
            assertThrows(NotImplementedException.class, () -> factory.getCardFactory("other"));
        });
    }
}