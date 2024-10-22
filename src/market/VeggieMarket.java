package market;

import card.Card;
import card.ICard;
import card.Vegetable;
import exceptions.PileOutOfCardsException;

import java.util.ArrayList;
import java.util.Map;

public class VeggieMarket extends Market<Vegetable> {

    public VeggieMarket() {
        super(Vegetable.class);
    }



    /**
     * Initializes the market with the veggie cards from the file at the given path.
     * @param nrOfPlayers the number of players
     * @param path
     */
    @Override
    public void initializeMarket(int nrOfPlayers, String path) {
        Map<String, ArrayList<ICard>> decks = cardFactory.getDecksOfFacesFromFile(path);

        //shuffle the decks
        for (var c : enumType.getEnumConstants()) {
            Card.shuffleDeck(decks.get(c.toString()));
        }
        int cardsPerFace = (int) (((float) nrOfPlayers / 2) * 6);

        ArrayList<ICard> deck = new ArrayList<>();
        for (int i = 0; i < cardsPerFace; i++) {
            for (var c : Vegetable.values()) {
                deck.add(decks.get(c.toString()).removeFirst());
            }
        }

        Card.shuffleDeck(deck);

        // Divide the deck into 3 piles
        for (int i = 0; i < 3; i++) {
            piles.add(new VeggiePile(new ArrayList<>()));
        }
        for (int i = 0; i < deck.size(); i++) {
            piles.get(i % 3).addPointCard(deck.get(i));
        }

    }
}
