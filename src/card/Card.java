package card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Card<T extends Enum<T>> implements ICard {
    private final T face;
    private final String criteria;
    private boolean criteriaSideUp = true;

    public  Card(T face, String criteria) {
        this.face = face;
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        if (criteriaSideUp) {
            return criteria + " (" + face.toString() + ")";
        } else {
            return face.toString();
        }
    }

   @Override
   public void setCriteriaSideUp(boolean criteriaSideUp) {
        this.criteriaSideUp = criteriaSideUp;
    }



    @Override
    public T getFace() {
        return face;
    }

    @Override
    public String getCriteria() {
        return criteria;
    }

    @Override
    public boolean isCriteriaSideUp() {
        return criteriaSideUp;
    }

    /**
     * Shuffles the given deck of cards.
     *
     * @param deck the deck of cards to shuffle
     */
    public static void shuffleDeck(ArrayList<ICard> deck) {
        Collections.shuffle(deck);
    }

}


