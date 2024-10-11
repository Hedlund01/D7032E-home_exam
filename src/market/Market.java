package market;

import card.ICard;
import card.Vegetable;
import exceptions.PileOutOfCardsException;
import card.AbstractCardFactory;
import card.ICardFactory;

import java.util.*;

public abstract class Market<T extends Enum<T>> implements IMarket {
    protected ArrayList<Pile> piles = new ArrayList<Pile>();
    private final ICardFactory<T> cardFactory;
    private final Class<T> enumType;

    public Market(Class<T> enumType) {
        this.enumType = enumType;
        AbstractCardFactory abstractCardFactory = new AbstractCardFactory();
        cardFactory = abstractCardFactory.getCardFactory(enumType.getTypeName());
    }


    /**
     * Sets up the piles of cards for the market based on the number of players.
     *
     * @param nrOfPlayers the number of players
     */
    public void setPiles(int nrOfPlayers, String path) {
        Map<String, ArrayList<ICard>> decks = cardFactory.getDecksFromFile(path);

        //shuffle the decks
        for (var c : enumType.getEnumConstants()) {
            _shuffleDeck(decks.get(c.toString()));
        }
        int cardsPerVeggie = nrOfPlayers / 2 * 6;

        ArrayList<ICard> deck = new ArrayList<>();
        for (int i = 0; i < cardsPerVeggie; i++) {
            for (var c : Vegetable.values()) {
                deck.add(decks.get(c.toString()).removeFirst());
            }
        }

        _shuffleDeck(deck);

        //divide the deck into 3 piles
        ArrayList<ICard> pile1 = new ArrayList<>();
        ArrayList<ICard> pile2 = new ArrayList<>();
        ArrayList<ICard> pile3 = new ArrayList<>();
        for (int i = 0; i < deck.size(); i++) {
            if (i % 3 == 0) {
                pile1.add(deck.get(i));
            } else if (i % 3 == 1) {
                pile2.add(deck.get(i));
            } else {
                pile3.add(deck.get(i));
            }
        }
        piles.add(new Pile(pile1));
        piles.add(new Pile(pile2));
        piles.add(new Pile(pile3));
    }




    public ICard buyPointCard(int pileIndex) {
        try {
            return piles.get(pileIndex).removeFirstPointCard();
        } catch (PileOutOfCardsException e) {
            //remove from the bottom of the biggest of the other piles
            Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
            if (biggestPileIndex != null) {
                try {
                    return piles.get(biggestPileIndex).removeLastPointCard();
                } catch (PileOutOfCardsException pileOutOfCardsException) {
                    //should never happen
                    return null;
                }
            } else { // we can't remove active point cards from other piles
                return null;
            }
        }
    }

    public ICard getPointCard(int pileIndex){
        try {
            return piles.get(pileIndex).getPointCard();
        } catch (PileOutOfCardsException e) {
            return null;
        }
    }


    public ICard buyVeggieCard(int pileIndex, int cardIndex) {
        try {
            return piles.get(pileIndex).removeFaceCard(cardIndex);
        } catch (PileOutOfCardsException e) {
            //remove from the bottom of the biggest of the other piles
            Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
            if (biggestPileIndex != null) {
                try {
                    piles.get(biggestPileIndex).addPointCard(piles.get(pileIndex).removeLastPointCard());
                    return piles.get(pileIndex).removeFaceCard(cardIndex);
                } catch (PileOutOfCardsException pileOutOfCardsException) {
                    //should never happen
                    return null;
                }
            } else { // we can't remove active point cards from other piles
                return piles.get(pileIndex).forceRemoveFaceCard(cardIndex);
            }
        }
    }

    public ICard getVeggieCard(int pileIndex, int cardIndex){
        return piles.get(pileIndex).getFaceCard(cardIndex);

    }



    private Integer _getBiggestPileThatHasGreaterThenOneCardIndex(int exceptIndex) {
        int biggestPileIndex = 0;
        int biggestSize = 0;
        for (int i = 0; i < piles.size(); i++) {
            if (i != exceptIndex && piles.get(i).getPointCardCount() > biggestSize) {
                biggestSize = piles.get(i).getPointCardCount();
                biggestPileIndex = i;
            }
        }
        if (biggestSize <= 1) {
            return null;
        }
        return biggestPileIndex;
    }

    /**
     * Checks whether there are any cards left in the market.
     *
     * @return True if all piles are empty, False if at least one pile has a card.
     */
    @Override
    public boolean isAllPilesEmpty() {
        //Check if all piles are empty if one has a card return false
        for (Pile pile : piles) {
            if (!pile.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Shuffles the given deck of cards.
     *
     * @param deck the deck of cards to shuffle
     */
    private void _shuffleDeck(ArrayList<ICard> deck) {
        Collections.shuffle(deck);
    }

}
