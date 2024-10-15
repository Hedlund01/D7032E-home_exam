package card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private static final String criteria = "TEST";

    ICard card;
    @BeforeEach
    void setUp() {
        card = new Card<>(TestEnum.TEST, criteria);
    }

    @Test
    void testToString() {
        String expectedCriteria = criteria + " (" + TestEnum.TEST.toString() + ")";
        String expectedFace = TestEnum.TEST.toString();

        String actualCriteria = card.toString();
        card.setCriteriaSideUp(false);
        String actualFace = card.toString();

        assertAll(() -> {
            assertEquals(expectedCriteria, actualCriteria);
            assertEquals(expectedFace, actualFace);
        });
    }

    @Test
    void setAndTestIsCriteriaSideUp() {
        boolean defaultCriteriaSideUp = card.isCriteriaSideUp();
        card.setCriteriaSideUp(false);

        assertAll(() -> {
            assertFalse(card.isCriteriaSideUp());
            assertTrue(defaultCriteriaSideUp);
        });

    }

    @Test
    void getFace() {
        assertEquals(TestEnum.TEST, card.getFace());
    }

    @Test
    void getCriteria() {
        assertEquals(criteria, card.getCriteria());
    }

}