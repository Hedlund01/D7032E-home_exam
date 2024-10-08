package Card;

public interface ICardFactory<T extends Enum<T>> {

    Card<T> createCard(T face, String criteria);
}
