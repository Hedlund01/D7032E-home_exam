package card;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VegetableCardFactory implements ICardFactory<Vegetable> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public VegetableCard createCard(Vegetable face, String criteria) {
        return new VegetableCard(face, criteria);
    }

    /**
     * Reads the cards from a JSON file and organizes them into decks based on their faces
     *
     * @param path the path to the JSON file
     * @return a dictionary containing the decks of cards
     */
    public Map<String, ArrayList<ICard>> getDecksOfFacesFromFile(String path){
        logger.info("Reading card manifest from file: {}", path);
        Map<String, ArrayList<ICard>> decks = new HashMap<>();
        try (InputStream fInputStream = new FileInputStream(path);
             Scanner scanner = new Scanner(fInputStream, StandardCharsets.UTF_8).useDelimiter("\\A")) {

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

                for(var face: criteriaObj.keySet()){
                    decks.putIfAbsent(face, new ArrayList<>());
                    var card = createCard(Vegetable.valueOf(face), criteriaObj.getString(face));
                    decks.get(face).add(card);
                }


            }

        } catch (IOException e) {
            logger.error("Error reading card manifest from file: {}. - Error: {}", path, e.getMessage());
        }
        return decks;
    }
}
