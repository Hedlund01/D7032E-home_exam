package card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VegetableCardFactoryTest {
    VegetableCardFactory factory;
    @BeforeEach
    void setUp() {
        factory = new VegetableCardFactory();
    }

    @Test
    void createCard() {
        VegetableCard card = factory.createCard(Vegetable.CARROT, "CARROT/3");

        assertAll(() ->{
            assertNotNull(card);
            assertEquals(Vegetable.CARROT, card.getFace());
            assertEquals("CARROT/3", card.getCriteria());
        });

    }

    @Test
    void getDecksOfFacesFromFile() {
        Map<String, ArrayList<ICard>> decks = factory.getDecksOfFacesFromFile("resources/PointSaladManifest.json");

        Set<String> expectedFaces = new HashSet<>();
        for(Vegetable face: Vegetable.values()){
            expectedFaces.add(face.toString());
        }

        int totalCards = 0;
        for (var deck : decks.values()) {
            totalCards += deck.size();
        }
        final int finalTotalCards = totalCards;

        Set<String> faces = decks.keySet();

        assertAll(() -> {
            assertNotNull(decks);
            assertEquals(6, decks.size());
            assertEquals(108, finalTotalCards);
            assertEquals(expectedFaces, faces);
        });


    }
}