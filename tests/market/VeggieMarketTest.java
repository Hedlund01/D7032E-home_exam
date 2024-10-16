package market;

import card.ICard;
import card.Vegetable;
import card.VegetableCard;
import exceptions.PileOutOfCardsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VeggieMarketTest {

    private Market<Vegetable> market;

    @BeforeEach
    void setUp() {
        market = new VeggieMarket();
    }


    @Test
    void setPilesFor2Players() {
        market.setPiles(2, "resources/PointSaladManifest.json");

        Map<String, ArrayList<ICard>> decks = new HashMap<>();

        int totalCards = 0;
        int numberOfArrays = market.piles.size();


        for (IPile veggiePile : market.piles) {
            while (veggiePile.getPointCardCount() > 0) {
                try {
                    var card = veggiePile.removeFirstPointCard();
                    decks.putIfAbsent(card.getFace().toString(), new ArrayList<>());
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                } catch (PileOutOfCardsException _) {

                }

            }
            for (int i = 0; i <= 1; i++) {
                var card = veggiePile.forceRemoveFaceCard(i);
                if(card != null){
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                }
            }

        }

        boolean roughlySameSize = _roughlySameSize(market.piles, 0.1);


        final int finalTotalCards = totalCards;
        final boolean finalRoughlySameSize = roughlySameSize;

        ArrayList<Integer> nrOfEachFace = new ArrayList<>();
        decks.forEach((_, cards) -> nrOfEachFace.add(cards.size()));
        final int expectedNrOfEachFace = 6;
        assertAll(() -> {
            assertTrue(finalRoughlySameSize);
            assertEquals(3, numberOfArrays);
            assertEquals(36, finalTotalCards);
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(0));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(1));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(2));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(3));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(4));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(5));
        });

    }

    @Test
    void setPilesFor3Players() {
        market.setPiles(3, "resources/PointSaladManifest.json");

        Map<String, ArrayList<ICard>> decks = new HashMap<>();

        int totalCards = 0;
        int numberOfArrays = market.piles.size();


        for (IPile veggiePile : market.piles) {
            while (veggiePile.getPointCardCount() > 0) {
                try {
                    var card = veggiePile.removeFirstPointCard();
                    decks.putIfAbsent(card.getFace().toString(), new ArrayList<>());
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                } catch (PileOutOfCardsException _) {

                }

            }
            for (int i = 0; i <= 1; i++) {
                var card = veggiePile.forceRemoveFaceCard(i);
                if(card != null){
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                }
            }

        }
        boolean roughlySameSize = _roughlySameSize(market.piles, 0.1);

        final int finalTotalCards = totalCards;
        final boolean finalRoughlySameSize = roughlySameSize;

        ArrayList<Integer> nrOfEachFace = new ArrayList<>();
        decks.forEach((_, cards) -> nrOfEachFace.add(cards.size()));
        final int expectedNrOfEachFace = 9;
        assertAll(() -> {
            assertTrue(finalRoughlySameSize);
            assertEquals(3, numberOfArrays);
            assertEquals(54, finalTotalCards);
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(0));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(1));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(2));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(3));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(4));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(5));
        });

    }




    @Test
    void setPilesFor4Players() {
        market.setPiles(4, "resources/PointSaladManifest.json");

        Map<String, ArrayList<ICard>> decks = new HashMap<>();

        int totalCards = 0;
        int numberOfArrays = market.piles.size();


        for (IPile veggiePile : market.piles) {
            while (veggiePile.getPointCardCount() > 0) {
                try {
                    var card = veggiePile.removeFirstPointCard();
                    decks.putIfAbsent(card.getFace().toString(), new ArrayList<>());
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                } catch (PileOutOfCardsException _) {

                }

            }
            for (int i = 0; i <= 1; i++) {
                var card = veggiePile.forceRemoveFaceCard(i);
                if(card != null){
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                }
            }

        }
        boolean roughlySameSize = _roughlySameSize(market.piles, 0.1);

        final int finalTotalCards = totalCards;
        final boolean finalRoughlySameSize = roughlySameSize;

        ArrayList<Integer> nrOfEachFace = new ArrayList<>();
        decks.forEach((_, cards) -> nrOfEachFace.add(cards.size()));
        final int expectedNrOfEachFace = 12;
        assertAll(() -> {
            assertTrue(finalRoughlySameSize);
            assertEquals(3, numberOfArrays);
            assertEquals(72, finalTotalCards);
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(0));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(1));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(2));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(3));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(4));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(5));
        });

    }

    @Test
    void setPilesFor5Players() {
        market.setPiles(5, "resources/PointSaladManifest.json");

        Map<String, ArrayList<ICard>> decks = new HashMap<>();

        int totalCards = 0;
        int numberOfArrays = market.piles.size();


        for (IPile veggiePile : market.piles) {
            while (veggiePile.getPointCardCount() > 0) {
                try {
                    var card = veggiePile.removeFirstPointCard();
                    decks.putIfAbsent(card.getFace().toString(), new ArrayList<>());
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                } catch (PileOutOfCardsException _) {

                }

            }
            for (int i = 0; i <= 1; i++) {
                var card = veggiePile.forceRemoveFaceCard(i);
                if(card != null){
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                }
            }

        }
        boolean roughlySameSize = _roughlySameSize(market.piles, 0.1);

        final int finalTotalCards = totalCards;
        final boolean finalRoughlySameSize = roughlySameSize;

        ArrayList<Integer> nrOfEachFace = new ArrayList<>();
        decks.forEach((_, cards) -> nrOfEachFace.add(cards.size()));
        final int expectedNrOfEachFace = 15;
        assertAll(() -> {
            assertTrue(finalRoughlySameSize);
            assertEquals(3, numberOfArrays);
            assertEquals(90, finalTotalCards);
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(0));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(1));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(2));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(3));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(4));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(5));
        });

    }

    @Test
    void setPilesFor6Players() {
        market.setPiles(6, "resources/PointSaladManifest.json");

        Map<String, ArrayList<ICard>> decks = new HashMap<>();

        int totalCards = 0;
        int numberOfArrays = market.piles.size();

        for (IPile veggiePile : market.piles) {
            while (veggiePile.getPointCardCount() > 0) {
                try {
                    var card = veggiePile.removeFirstPointCard();
                    decks.putIfAbsent(card.getFace().toString(), new ArrayList<>());
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                } catch (PileOutOfCardsException _) {

                }

            }
            for (int i = 0; i <= 1; i++) {
                var card = veggiePile.forceRemoveFaceCard(i);
                if(card != null){
                    decks.get(card.getFace().toString()).add(card);
                    totalCards += 1;
                }
            }

        }
        boolean roughlySameSize = _roughlySameSize(market.piles, 0.1);

        final int finalTotalCards = totalCards;
        final boolean finalRoughlySameSize = roughlySameSize;

        ArrayList<Integer> nrOfEachFace = new ArrayList<>();
        decks.forEach((_, cards) -> nrOfEachFace.add(cards.size()));
        final int expectedNrOfEachFace = 18;
        assertAll(() -> {
            assertTrue(finalRoughlySameSize);
            assertEquals(3, numberOfArrays);
            assertEquals(108, finalTotalCards);
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(0));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(1));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(2));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(3));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(4));
            assertEquals(expectedNrOfEachFace, nrOfEachFace.get(5));
        });

    }

    @Test
    void buyPointCardOutOfCards() {
        var cards = new ArrayList<ICard>();
        market.piles = new ArrayList<>();
        market.piles.add(new VeggiePile(cards));

        var card = market.buyPointCard(0);
        assertNull(card);
    }

    @Test
    void buyPointCard() {
        var cards = new ArrayList<ICard>();
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        market.piles = new ArrayList<>();
        market.piles.add(new VeggiePile(cards));

        var card = market.buyPointCard(0);
        assertAll(() ->{
            assertNull(market.getPointCard(0));
            assertEquals(Vegetable.CARROT, card.getFace());
            assertEquals("TEST", card.getCriteria());
        });
    }

    @Test
    void buyPointCardTakeFromAnotherPile() {
        var cards1 = new ArrayList<ICard>();
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));

        var cards2 = new ArrayList<ICard>();
        cards2.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards2.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards2.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards2.add(new VegetableCard(Vegetable.CARROT, "TEST"));


        var cards3 = new ArrayList<ICard>();
        cards3.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards3.add(new VegetableCard(Vegetable.CARROT, "TEST"));

        market.piles = new ArrayList<>();
        market.piles.add(new VeggiePile(cards1));
        market.piles.add(new VeggiePile(cards2));
        market.piles.add(new VeggiePile(cards3));


        var card = market.buyPointCard(2);
        assertAll(() -> {
            assertEquals(Vegetable.CARROT, card.getFace());
            assertEquals("TEST", card.getCriteria());
            assertTrue(card.isCriteriaSideUp());
        });
    }



    @Test
    void buyFaceCardOutOfCards() {
        var cards = new ArrayList<ICard>();
        market.piles = new ArrayList<>();
        market.piles.add(new VeggiePile(cards));

        var card = market.buyFaceCard(0, 0);
        assertNull(card);
    }

    @Test
    void buyFaceCard() {
        var cards = new ArrayList<ICard>();
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        market.piles = new ArrayList<>();
        market.piles.add(new VeggiePile(cards));

        var card = market.buyFaceCard(0, 0);
        assertAll(() ->{
            assertNull(market.getPointCard(0));
            assertEquals(Vegetable.CARROT, card.getFace());
            assertEquals("TEST", card.getCriteria());
            assertFalse( card.isCriteriaSideUp());
        });
    }

    @Test
    void buyFaceCardTakeFromAnotherPile() {
        var cards1 = new ArrayList<ICard>();
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));

        var cards2 = new ArrayList<ICard>();
        cards2.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards2.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards1.add(new VegetableCard(Vegetable.CARROT, "TEST"));


        var cards3 = new ArrayList<ICard>();


        market.piles = new ArrayList<>();
        market.piles.add(new VeggiePile(cards1));
        market.piles.add(new VeggiePile(cards2));
        market.piles.add(new VeggiePile(cards3));


        var card = market.buyFaceCard(2,1);
        assertAll(() -> {
            assertEquals(Vegetable.CARROT, card.getFace());
            assertEquals("TEST", card.getCriteria());
            assertFalse(card.isCriteriaSideUp());
        });
    }

    @Test
    void isAllPilesEmptyWhenEmpty() {
        assertTrue(market.isAllPilesEmpty());
    }

    @Test
    void isAllPilesEmptyWhenNotEmpty() {

        var cards = new ArrayList<ICard>();
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));

        market.piles.add(new VeggiePile(cards));

        assertFalse(market.isAllPilesEmpty());
    }


    @Test
    void countTotalVisibleFaceCards() {
        var cards = new ArrayList<ICard>();
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));
        cards.add(new VegetableCard(Vegetable.CARROT, "TEST"));

        market.piles.add(new VeggiePile(cards));

        assertEquals(2, market.countTotalVisibleFaceCards());
    }

    private boolean _roughlySameSize(ArrayList<IPile> piles, double toleranceFraction) {
        int totalCards = 0;
        int numberOfArrays = piles.size();

        for (IPile pile : piles) {
            totalCards += pile.getPointCardCount();
            totalCards += pile.getFaceCardCount();
        }

        double averageSize = (double) totalCards / numberOfArrays;

        double tolerance = averageSize * toleranceFraction;

        for (IPile pile: piles) {
            var size = pile.getPointCardCount() + pile.getFaceCardCount();
            if (Math.abs(size - averageSize) > tolerance) {
                return false;
            }
        }

        return true;
    }
}