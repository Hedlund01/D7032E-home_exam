package market;

import card.Card;
import card.ICard;
import card.TestEnum;
import exceptions.PileOutOfCardsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VeggiePileTest {

    VeggiePile veggiePile;

    @BeforeEach
    void setUp() {
        ArrayList<ICard> cards = new ArrayList<>();
        cards.add(new Card<>(TestEnum.TEST, "face_1"));
        cards.add(new Card<>(TestEnum.TEST, "face_2"));
        cards.add(new Card<>(TestEnum.TEST, "1"));
        cards.add(new Card<>(TestEnum.TEST, "2"));
        veggiePile = new VeggiePile(cards);
    }

    @Test
    void getPointCard() {
        assertAll(() -> {
            assertEquals("1", veggiePile.getPointCard().getCriteria());
            assertEquals(TestEnum.TEST, veggiePile.getPointCard().getFace());
        });
    }

    @Test
    void getPointCardWhenEmpty() {

        veggiePile = new VeggiePile(new ArrayList<>());
        assertThrows(PileOutOfCardsException.class, () -> {
            veggiePile.getPointCard();
        });
    }

    @Test
    void removeFirstAndLastPointCardWhenEmpty() {
        veggiePile = new VeggiePile(new ArrayList<>());
        assertAll(() -> {
            assertThrows(PileOutOfCardsException.class, () -> {
                veggiePile.removeFirstPointCard();
            });
            assertThrows(PileOutOfCardsException.class, () -> {
                veggiePile.removeLastPointCard();
            });
        });
    }



    @Test
    void removeFirstPointCardAndGetPointCardCount() {
        try {
            veggiePile.removeFirstPointCard();
            assertAll(() -> {
                assertEquals(1, veggiePile.getPointCardCount());
                assertEquals("2", veggiePile.getPointCard().getCriteria());
            });
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void removeLastPointCard() {
        try {
            veggiePile.removeLastPointCard();
            assertAll(() -> {
                assertEquals(1, veggiePile.getPointCardCount());
                assertEquals("1", veggiePile.getPointCard().getCriteria());
            });
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getFaceCard() {
        assertAll(() -> {
            assertEquals("face_1", veggiePile.getFaceCard(0).getCriteria());
            assertEquals(TestEnum.TEST, veggiePile.getFaceCard(0).getFace());
        });
    }

    @Test
    void removeFaceCardWhenOnlyOneCardLeft() {
        try {
            veggiePile.removeFirstPointCard();

        } catch (Exception _) {

        }
        assertThrows(PileOutOfCardsException.class, () -> {
            veggiePile.removeFaceCard(0);
        });
    }

    @Test
    void removeFaceCard(){
        try{
            veggiePile.removeFaceCard(0);
        }catch (PileOutOfCardsException e){
            fail();
        }
        assertAll(() -> {
            assertEquals("1", veggiePile.getFaceCard(0).getCriteria());
            assertEquals(TestEnum.TEST, veggiePile.getFaceCard(0).getFace());
        });

    }

    @Test
    void forceRemoveFaceCard() {
        var card = veggiePile.forceRemoveFaceCard(0);
        assertAll(() -> {
            assertEquals("face_1", card.getCriteria());
            assertEquals(TestEnum.TEST, card.getFace());
        });
    }

    @Test
    void addPointCard() {
        var card = new Card<>(TestEnum.TEST, "3");
        veggiePile.addPointCard(card);
        assertAll(() -> {
            assertEquals(3, veggiePile.getPointCardCount());
        });
    }

    @Test
    void addFaceCardInvalidArgument() {


        var card = new Card<>(TestEnum.TEST, "face_3");
        assertThrows(Exception.class, () -> {
            veggiePile.addFaceCard(card, 0);
        });
    }

    @Test
    void addFaceCard() {
        try {
            veggiePile.removeFirstPointCard();
            veggiePile.removeFirstPointCard();
            veggiePile.forceRemoveFaceCard(0);
        } catch (Exception _) {
            System.out.println("Error");
        }
        var card = new Card<>(TestEnum.TEST, "face_3");

        try {
            veggiePile.addFaceCard(card, 0);
        } catch (Exception e) {
            fail();
        }

        assertAll(() -> {
            assertEquals("face_3", veggiePile.getFaceCard(0).getCriteria());
            assertEquals(TestEnum.TEST, veggiePile.getFaceCard(0).getFace());
            assertFalse(veggiePile.getFaceCard(0).isCriteriaSideUp());
        });
    }


    @Test
    void isEmptyWhenNotEmpty() {
        assertFalse(veggiePile.isEmpty());
    }

    @Test
    void isEmptyWhenEmpty() {
        try {
            veggiePile.removeFirstPointCard();
            veggiePile.removeFirstPointCard();
            veggiePile.forceRemoveFaceCard(0);
            veggiePile.forceRemoveFaceCard(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(veggiePile.isEmpty());
    }

    @Test
    void getFaceCardCount(){
        int originalFaceCardCount = veggiePile.getFaceCardCount();
        veggiePile.forceRemoveFaceCard(0);
        veggiePile.forceRemoveFaceCard(0);
        veggiePile.forceRemoveFaceCard(0);
        assertAll(() ->{
            assertEquals(2, originalFaceCardCount);
            assertEquals(1, veggiePile.getFaceCardCount());
            veggiePile.forceRemoveFaceCard(1);
            assertEquals(0, veggiePile.getFaceCardCount());
        });


    }
}