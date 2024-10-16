package card;

import java.util.ArrayList;
import java.util.Map;

public interface ICardFactory<T extends Enum<T>> {

     ICard createCard(T face, String criteria);

    Map<String, ArrayList<ICard>> getDecksOfFacesFromFile(String path);
}
