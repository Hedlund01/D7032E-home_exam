package card;

public interface ICard{
    String toString();

    void setCriteriaSideUp(boolean criteriaSideUp);


    <T extends Enum<T>> T getFace();

    String getCriteria();

    boolean isCriteriaSideUp();

}
