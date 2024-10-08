package Card;

public class Card<T extends Enum<T>>  {
    private final T face;
    private final String criteria;
    private boolean criteriaSideUp = true;

    public Card(T face, String criteria) {
        this.face = face;
        this.criteria = criteria;
    }

    public String toString() {
        if (criteriaSideUp) {
            return criteria + " (" + face.toString() + ")";
        } else {
            return face.toString();
        }
    }

   public void setCriteriaSideUp(boolean criteriaSideUp) {
        this.criteriaSideUp = criteriaSideUp;
    }

    public T getFace() {
        return face;
    }

    public String getCriteria() {
        return criteria;
    }

    public boolean isCriteriaSideUp() {
        return criteriaSideUp;
    }

}


