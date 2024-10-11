package market;

import card.ICard;

public interface IMarket {
    void setPiles(int nrOfPlayers, String path);

    ICard buyPointCard(int pileIndex);

    ICard getPointCard(int pileIndex);

    ICard buyVeggieCard(int pileIndex, int cardIndex);

    ICard getVeggieCard(int pileIndex, int cardIndex);

    /**
     * Returns a string representation of the market, including point cards and veggie cards.
     *
     * @return a string representation of the market
     */
    String getDisplayString();

    boolean isAllPilesEmpty();
}
