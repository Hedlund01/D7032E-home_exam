package Card;

public class GenericCardFactory {
    private final ICardFactory cardFactory;

    public GenericCardFactory(ICardFactory cardFactory) {
        this.cardFactory = cardFactory;
    }

    public Card createCard(Enum face, String criteria) {
        return cardFactory.createCard(face, criteria);
    }

}
