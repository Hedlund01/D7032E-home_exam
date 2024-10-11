package card;

import java.util.ArrayList;
import java.util.Map;

public interface ICardFactory<E extends Enum<E>> {

    ICard createCard(E face, String criteria);

    Map<String, ArrayList<ICard>> getDecksFromFile(String path);
}
