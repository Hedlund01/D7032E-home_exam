package card;

public interface ICard{
    String toString();

    void setCriteriaSideUp(boolean criteriaSideUp);


    <R extends Enum<R>> R getFace();

    String getCriteria();

    boolean isCriteriaSideUp();

}
