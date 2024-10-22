package card;

import java.io.Serializable;

public interface ICard extends Serializable {
    String toString();

    void setCriteriaSideUp(boolean criteriaSideUp);


    <R extends Enum<R>> R getFace();

    String getCriteria();

    boolean isCriteriaSideUp();

}
