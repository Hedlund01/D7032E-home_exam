package market;

import card.ICard;

public interface IMarket {

    /**
     * Sets up the piles of cards for the market based on the number of players.
     * Must be called before any other methods in this interface.
     *
     * @param nrOfPlayers the number of players
     */
    void setPiles(int nrOfPlayers, String path);

    /**
     * Buys a point card from the specified pile.
     *
     * @param pileIndex the index of the pile
     * @return the point card bought, or null if no card could be bought
     */
    ICard buyPointCard(int pileIndex);

    /**
     * Gets a point card from the specified pile without removing it.
     *
     * @param pileIndex the index of the pile
     * @return the point card, or null if no card is available
     */
    ICard getPointCard(int pileIndex);

    /**
     * Buys a veggie card from the specified pile and card index.
     *
     * @param pileIndex the index of the pile
     * @param cardIndex the index of the card within the pile
     * @return the veggie card bought, or null if no card could be bought
     */
    ICard buyFaceCard(int pileIndex, int cardIndex);

    /**
     * Gets a face card from the specified pile and card index without removing it.
     *
     * @param pileIndex the index of the pile
     * @param cardIndex the index of the card within the pile
     * @return the veggie card, or null if no card is available
     */
    ICard getFaceCard(int pileIndex, int cardIndex);

    /**
     * Returns a string representation of the market, including point cards and veggie cards.
     *
     * @return a string representation of the market
     */
    String getDisplayString();

    /**
     * Checks whether there are any cards left in the market.
     *
     * @return True if all piles are empty, False if at least one pile has a card.
     */
    boolean isAllPilesEmpty();

    /**
     * Counts the total number of visible face cards in the market.
     *
     * @return the total number of visible face cards
     */
    int countTotalVisibleFaceCards();
}
