package market;

import card.Card;
import card.ICard;
import card.TestEnum;
import exceptions.PileOutOfCardsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PileTest {

    Pile pile;

    @BeforeEach
    void setUp() {
        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(new Card<>(TestEnum.TEST, "face_1"));
        cards.add(new Card<>(TestEnum.TEST, "face_2"));
        cards.add(new Card<>(TestEnum.TEST, "1"));
        cards.add(new Card<>(TestEnum.TEST, "2"));
        pile = new Pile(cards);
    }

    @Test
    void getPointCard() {
        assertAll(() -> {
            assertEquals("1", pile.getPointCard().getCriteria());
            assertEquals(TestEnum.TEST, pile.getPointCard().getFace());
        });
    }

    @Test
    void getPointCardWhenEmpty() {
        pile = new Pile(new ArrayList<>());
        assertThrows(PileOutOfCardsException.class, () -> {
            pile.getPointCard();
        });
    }

    @Test
    void removeFirstAndLastPointCardWhenEmpty() {
        pile = new Pile(new ArrayList<>());
        assertAll(() -> {
            assertThrows(PileOutOfCardsException.class, () -> {
                pile.removeFirstPointCard();
            });
            assertThrows(PileOutOfCardsException.class, () -> {
                pile.removeLastPointCard();
            });
        });
    }



    @Test
    void removeFirstPointCardAndGetPointCardCount() {
        try {
            pile.removeFirstPointCard();
            assertAll(() -> {
                assertEquals(1, pile.getPointCardCount());
                assertEquals("2", pile.getPointCard().getCriteria());
            });
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void removeLastPointCard() {
        try {
            pile.removeLastPointCard();
            assertAll(() -> {
                assertEquals(1, pile.getPointCardCount());
                assertEquals("1", pile.getPointCard().getCriteria());
            });
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getFaceCard() {
        assertAll(() -> {
            assertEquals("face_1", pile.getFaceCard(0).getCriteria());
            assertEquals(TestEnum.TEST, pile.getFaceCard(0).getFace());
        });
    }

    @Test
    void removeFaceCard() {
        try {
            pile.removeFirstPointCard();

        } catch (Exception _) {

        }
        assertThrows(PileOutOfCardsException.class, () -> {
            pile.removeFaceCard(0);
        });
    }

    @Test
    void forceRemoveFaceCard() {
        var card = pile.forceRemoveFaceCard(0);
        assertAll(() -> {
            assertEquals("face_1", card.getCriteria());
            assertEquals(TestEnum.TEST, card.getFace());
        });
    }

    @Test
    void addPointCard() {
        var card = new Card<>(TestEnum.TEST, "3");
        pile.addPointCard(card);
        assertAll(() -> {
            assertEquals(3, pile.getPointCardCount());
        });
    }

    @Test
    void addFaceCardInvalidArgument() {


        var card = new Card<>(TestEnum.TEST, "face_3");
        assertThrows(Exception.class, () -> {
            pile.addFaceCard(card, 0);
        });
    }

    @Test
    void addFaceCard() {
        try {
            pile.removeFirstPointCard();
            pile.removeFirstPointCard();
            pile.forceRemoveFaceCard(0);
        } catch (Exception _) {
            System.out.println("Error");
        }
        var card = new Card<>(TestEnum.TEST, "face_3");

        try {
            pile.addFaceCard(card, 0);
        } catch (Exception e) {
            fail();
        }

        assertAll(() -> {
            assertEquals("face_3", pile.getFaceCard(0).getCriteria());
            assertEquals(TestEnum.TEST, pile.getFaceCard(0).getFace());
            assertFalse(pile.getFaceCard(0).isCriteriaSideUp());
        });
    }


    @Test
    void isEmptyWhenNotEmpty() {
        assertFalse(pile.isEmpty());
    }

    @Test
    void isEmptyWhenEmpty() {
        try {
            pile.removeFirstPointCard();
            pile.removeFirstPointCard();
            pile.forceRemoveFaceCard(0);
            pile.forceRemoveFaceCard(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(pile.isEmpty());
    }
}