package Market;

import Card.Card;
import Card.Vegetable;
import Exceptions.PileOutOfCardsException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public abstract class Market<T extends Enum<T>> {
    protected ArrayList<Pile<T>> piles = new ArrayList<Pile<T>>();
    private final Class<T> enumType;
    public Market(Class<T> enumType, String path) {
        this.enumType = enumType;
    }


    /**
     * Sets up the piles of cards for the market based on the number of players.
     *
     * @param nrOfPlayers the number of players
     */
    public void setPiles(int nrOfPlayers, String path) {
        Dictionary<String, ArrayList<Card<T>>> decks = new Hashtable<>();
        decks = _getCards(path);

        //shuffle the decks
        for (var c : enumType.getEnumConstants()) {
            _shuffleDeck(decks.get(c.toString()));
        }
        int cardsPerVeggie = nrOfPlayers / 2 * 6;

        ArrayList<Card<T>> deck = new ArrayList<>();
        for (int i = 0; i < cardsPerVeggie; i++) {
            for (var c : Vegetable.values()) {
                deck.add(decks.get(c.toString()).removeFirst());
            }
        }

        _shuffleDeck(deck);

        //divide the deck into 3 piles
        ArrayList<Card<T>> pile1 = new ArrayList<>();
        ArrayList<Card<T>> pile2 = new ArrayList<>();
        ArrayList<Card<T>> pile3 = new ArrayList<>();
        for (int i = 0; i < deck.size(); i++) {
            if (i % 3 == 0) {
                pile1.add(deck.get(i));
            } else if (i % 3 == 1) {
                pile2.add(deck.get(i));
            } else {
                pile3.add(deck.get(i));
            }
        }
        piles.add(new Pile<T>(pile1));
        piles.add(new Pile<T>(pile2));
        piles.add(new Pile<T>(pile3));
    }

    /**
     * Reads the cards from a JSON file and organizes them into decks.
     *
     * @param path the path to the JSON file
     * @return a dictionary containing the decks of cards
     */
    private Dictionary<String, ArrayList<Card<T>>> _getCards(String path) {
        Dictionary<String, ArrayList<Card<T>>> decks = new Hashtable<>();
        try (InputStream fInputStream = new FileInputStream(path);
             Scanner scanner = new Scanner(fInputStream, "UTF-8").useDelimiter("\\A")) {

            // Read the entire JSON file into a String
            String jsonString = scanner.hasNext() ? scanner.next() : "";

            // Parse the JSON string into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Get the "cards" array from the JSONObject
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            // Iterate over each card in the array
            for (int i = 0; i < cardsArray.length(); i++) {
                JSONObject cardJson = cardsArray.getJSONObject(i);

                // Get the criteria object from the card JSON
                JSONObject criteriaObj = cardJson.getJSONObject("criteria");

                // Add each vegetable card to the deck with its corresponding criteria
                for (var c : enumType.getEnumConstants()) {
                    decks.put(c.toString(), new ArrayList<Card<T>>());
                    decks.get(c.toString()).add(new Card<T>(c, criteriaObj.getString(c.toString())));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return decks;
    }

    public Card<T> buyPointCard(int pileIndex)  {
        try {
            return piles.get(pileIndex).removeFirstPointCard();
        } catch (PileOutOfCardsException e) {
            //remove from the bottom of the biggest of the other piles
           Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
            if(biggestPileIndex != null) {
                try {
                    return piles.get(biggestPileIndex).removeLastPointCard();
                } catch (PileOutOfCardsException pileOutOfCardsException) {
                    //should never happen
                    return null;
                }
            } else { // we can't remove active point cards from other piles
                return null;
            }
        }
    }

    public Card<T> buyVeggieCard(int pileIndex, int cardIndex) {
        try {
            return piles.get(pileIndex).removeFaceCard(cardIndex);
        } catch (PileOutOfCardsException e) {
            //remove from the bottom of the biggest of the other piles
            Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
            if(biggestPileIndex != null) {
                try {
                    piles.get(biggestPileIndex).addPointCard(piles.get(pileIndex).removeLastPointCard());
                    return piles.get(pileIndex).removeFaceCard(cardIndex);
                } catch (PileOutOfCardsException pileOutOfCardsException) {
                    //should never happen
                    return null;
                }
            } else { // we can't remove active point cards from other piles
                return piles.get(pileIndex).forceRemoveFaceCard(cardIndex);
            }
        }
    }

    private Integer _getBiggestPileThatHasGreaterThenOneCardIndex(int exceptIndex){
        int biggestPileIndex = 0;
        int biggestSize = 0;
        for(int i = 0; i < piles.size(); i++) {
            if(i != exceptIndex && piles.get(i).getPointCardCount() > biggestSize) {
                biggestSize = piles.get(i).getPointCardCount();
                biggestPileIndex = i;
            }
        }
        if(biggestSize <= 1) {
            return null;
        }
        return biggestPileIndex;
    }

    /**
     * Returns a string representation of the market, including point cards and veggie cards.
     *
     * @return a string representation of the market
     */
    public abstract String getDisplayString();

    /**
     * Checks whether there are any cards left in the market.
     * @return True if all piles are empty, False if at least one pile has a card.
     */
    public boolean IsAllPilesEmpty() {
        //Check if all piles are empty if one has a card return false
        for (Pile<T> pile : piles) {
            if (!pile.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Shuffles the given deck of cards.
     *
     * @param deck the deck of cards to shuffle
     */
    private void _shuffleDeck(ArrayList<Card<T>> deck) {
        Collections.shuffle(deck);
    }

}
