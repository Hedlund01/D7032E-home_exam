package Market;

import Card.Card;
import Card.Pile;
import Card.Vegetable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class VeggieMarket implements IMarket {
    private ArrayList<Pile<Vegetable>> piles = new ArrayList<Pile<Vegetable>>();

    /**
     * Sets up the piles of cards for the market based on the number of players.
     *
     * @param nrOfPlayers the number of players
     */
    public void setPiles(int nrOfPlayers) {
        Dictionary<String, ArrayList<Card<Vegetable>>> decks = new Hashtable<>();
        decks = _getCards("resources/PointSaladManifest.json");

        //shuffle the decks
        for (var c : Vegetable.values()) {
            _shuffleDeck(decks.get(c.toString()));
        }
        int cardsPerVeggie = nrOfPlayers / 2 * 6;

        ArrayList<Card<Vegetable>> deck = new ArrayList<>();
        for (int i = 0; i < cardsPerVeggie; i++) {
            for (var c : Vegetable.values()) {
                deck.add(decks.get(c.toString()).removeFirst());
            }
        }

        _shuffleDeck(deck);

        //divide the deck into 3 piles
        ArrayList<Card<Vegetable>> pile1 = new ArrayList<>();
        ArrayList<Card<Vegetable>> pile2 = new ArrayList<>();
        ArrayList<Card<Vegetable>> pile3 = new ArrayList<>();
        for (int i = 0; i < deck.size(); i++) {
            if (i % 3 == 0) {
                pile1.add(deck.get(i));
            } else if (i % 3 == 1) {
                pile2.add(deck.get(i));
            } else {
                pile3.add(deck.get(i));
            }
        }
        piles.add(new Pile<Vegetable>(pile1));
        piles.add(new Pile<Vegetable>(pile2));
        piles.add(new Pile<Vegetable>(pile3));
    }

    /**
     * Reads the cards from a JSON file and organizes them into decks.
     *
     * @param path the path to the JSON file
     * @return a dictionary containing the decks of cards
     */
    private Dictionary<String, ArrayList<Card<Vegetable>>> _getCards(String path) {
        Dictionary<String, ArrayList<Card<Vegetable>>> decks = new Hashtable<>();
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
                for (var c : Vegetable.values()) {
                    decks.put(c.toString(), new ArrayList<Card<Vegetable>>());
                    decks.get(c.toString()).add(new Card<Vegetable>(Vegetable.valueOf(c.toString()), criteriaObj.getString(c.toString())));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return decks;
    }

    /**
     * Returns a string representation of the market, including point cards and veggie cards.
     *
     * @return a string representation of the market
     */
    public String getDisplayString() {
        String pileString = "Point Cards:\t";
        for (int p = 0; p < piles.size(); p++) {
            if (piles.get(p).getPointCard() == null) {
                pileString += "[" + p + "]" + String.format("%-43s", "Empty") + "\t";
            } else
                pileString += "[" + p + "]" + String.format("%-43s", piles.get(p).getPointCard()) + "\t";
        }
        pileString += "\nVeggie Cards:\t";
        char veggieCardIndex = 'A';
        for (Pile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getFaceCard(0)) + "\t";
            veggieCardIndex++;
        }
        pileString += "\n\t\t";
        for (Pile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getFaceCard(1)) + "\t";
            veggieCardIndex++;
        }
        return pileString;
    }

    /**
     * Checks whether there are any cards left in the market.
     * @return True if all piles are empty, False if at least one pile has a card.
     */
    public boolean pilesIsEmpty() {
        //Check if all piles are empty if one has a card return false
        for (Pile<Vegetable> pile : piles) {
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
    private void _shuffleDeck(ArrayList<Card<Vegetable>> deck) {
        Collections.shuffle(deck);
    }

}
