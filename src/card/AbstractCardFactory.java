package card;

import exceptions.NotImplementedException;

public class AbstractCardFactory {
    public  ICardFactory getCardFactory(String type) {
        if (type.equals("card.Vegetable")) {
            return new VegetableCardFactory();
        } else if (type.equals("card.Point")) {
            throw new NotImplementedException("Point card factory not implemented");
        } else {
            throw new NotImplementedException("Invalid card type: " + type);
        }
    }
}
