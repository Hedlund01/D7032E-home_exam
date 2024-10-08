package Card;

public class VegetableCardFactory implements ICardFactory<Vegetable> {

    @Override
    public VegetableCard createCard(Vegetable face, String criteria) {
        return new VegetableCard(face, criteria);
    }
}
