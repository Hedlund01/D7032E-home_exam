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

    @Override
    public String getDisplayString() {

        StringBuilder pileString = new StringBuilder("Point Cards:\t");
        for (int p = 0; p < piles.size(); p++) {
            try {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", piles.get(p).getPointCard())).append("\t");
            } catch (PileOutOfCardsException e) {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", "Empty")).append("\t");
            }
        }
        pileString.append("\nVeggie Cards:\t");
        char veggieCardIndex = 'A';
        for (IPile pile : piles) {
            pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", pile.getFaceCard(0))).append("\t");
            veggieCardIndex++;
        }
        pileString.append("\n\t\t");
        for (IPile pile : piles) {
            pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", pile.getFaceCard(1))).append("\t");
            veggieCardIndex++;
        }
        return pileString.toString();
    }

    /**
     * @param nrOfPlayers the number of players
     * @param path
     */
    @Override
    public void setPiles(int nrOfPlayers, String path) {
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
