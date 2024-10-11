package market;

import card.Vegetable;
import exceptions.PileOutOfCardsException;

public class VeggieMarket extends Market<Vegetable> {

    public VeggieMarket() {
        super(Vegetable.class);
    }

    @Override
    public String getDisplayString() {

        StringBuilder pileString = new StringBuilder("Point Cards:\t");
        for (int p = 0; p < piles.size(); p++) {
            try {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", piles.get(p).getPointCard())).append("\t");
            } catch (PileOutOfCardsException e) {
                pileString.append("[").append(p).append("]").append(String.format("%-43s", "Empty")).append("\t");
            }
        }
        pileString.append("\nVeggie Cards:\t");
        char veggieCardIndex = 'A';
        for (Pile pile : piles) {
            pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", pile.getFaceCard(0))).append("\t");
            veggieCardIndex++;
        }
        pileString.append("\n\t\t");
        for (Pile pile : piles) {
            pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", pile.getFaceCard(1))).append("\t");
            veggieCardIndex++;
        }
        return pileString.toString();
    }
}
