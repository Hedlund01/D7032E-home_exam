package Market;

import Card.Vegetable;
import Exceptions.PileOutOfCardsException;

public class VeggieMarket extends Market<Vegetable> {

    public VeggieMarket() {
        super(Vegetable.class);
    }

    @Override
    public String getDisplayString() {

        String pileString = "Point Cards:\t";
        for (int p = 0; p < piles.size(); p++) {
            try {
                pileString += "[" + p + "]" + String.format("%-43s", piles.get(p).getPointCard()) + "\t";
            } catch (PileOutOfCardsException e) {
                pileString += "[" + p + "]" + String.format("%-43s", "Empty") + "\t";
            }
        }
        pileString += "\nVeggie Cards:\t";
        char veggieCardIndex = 'A';
        for (Pile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getFaceCard(0)) + "\t";
            veggieCardIndex++;
        }
        pileString += "\n\t\t";
        for (Pile pile : piles) {
            pileString += "[" + veggieCardIndex + "]" + String.format("%-43s", pile.getFaceCard(1)) + "\t";
            veggieCardIndex++;
        }
        return pileString;
    }
}
