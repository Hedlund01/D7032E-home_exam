package market;

import card.ICard;
import card.Vegetable;
import exceptions.InvalidArgumentException;
import exceptions.PileOutOfCardsException;
import card.AbstractCardFactory;
import card.ICardFactory;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class Market<T extends Enum<T>> implements IMarket {
    private Logger logger = LogManager.getLogger();
    protected ArrayList<Pile> piles = new ArrayList<Pile>();
    private final ICardFactory<T> cardFactory;
    private final Class<T> enumType;

    public Market(Class<T> enumType) {
        this.enumType = enumType;
        AbstractCardFactory abstractCardFactory = new AbstractCardFactory();
        cardFactory = abstractCardFactory.getCardFactory(enumType.getTypeName());
    }


    public void setPiles(int nrOfPlayers, String path) {
        Map<String, ArrayList<ICard>> decks = cardFactory.getDecksOfFacesFromFile(path);

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
        try (final CloseableThreadContext.Instance _ = CloseableThreadContext
                .put("method", "buyPointCard")
                .put("pileIndex", Integer.toString(pileIndex))
        ) {

            try {
                logger.trace("Buying point card");
                return piles.get(pileIndex).removeFirstPointCard();
            } catch (PileOutOfCardsException e) {
                logger.trace("Pile out of cards");
                //remove from the bottom of the biggest of the other piles
                Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
                if (biggestPileIndex != null) {
                    try {
                        logger.trace("Removing point card from biggest pile {}", biggestPileIndex);
                        return piles.get(biggestPileIndex).removeLastPointCard();
                    } catch (PileOutOfCardsException pileOutOfCardsException) {
                        //should never happen
                        logger.error("Pile out of cards, after rebalancing from biggest pile");
                        return null;
                    }
                } else { // we can't remove active point cards from other piles
                    logger.trace("No other pile to take from");
                    return null;
                }
            }
        }
    }


    public ICard getPointCard(int pileIndex) {
        try (final CloseableThreadContext.Instance _ = CloseableThreadContext
                .put("method", "getPointCard")
                .put("pileIndex", Integer.toString(pileIndex))
        ) {

            try {
                logger.trace("Getting point card");
                return piles.get(pileIndex).getPointCard();
            } catch (PileOutOfCardsException e) {
                logger.trace("Pile out of cards");
                //remove from the bottom of the biggest of the other piles
                Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
                if (biggestPileIndex != null) {
                    try {
                        logger.trace("Removing point card from biggest pile {}", biggestPileIndex);
                        piles.get(pileIndex).addPointCard(piles.get(biggestPileIndex).removeLastPointCard());
                        return getPointCard(pileIndex);
                    } catch (PileOutOfCardsException pileOutOfCardsException) {
                        //should never happen
                        logger.error("Pile out of cards, after rebalancing from biggest pile");
                        return null;
                    }
                } else { // we can't remove active point cards from other piles
                    logger.trace("No other pile to take from");
                    return null;
                }
            }
        }
    }


    public ICard buyFaceCard(int pileIndex, int cardIndex) {
        try (final CloseableThreadContext.Instance _ = CloseableThreadContext
                .put("method", "buyFaceCard")
                .put("pileIndex", Integer.toString(pileIndex))
                .put("cardIndex", Integer.toString(cardIndex))
        ) {
            try {
                logger.trace("Buying face card");
                return piles.get(pileIndex).removeFaceCard(cardIndex);
            } catch (PileOutOfCardsException e) {
                logger.trace("Pile out of cards");
                //remove from the bottom of the biggest of the other piles
                Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
                if (biggestPileIndex != null) {
                    try {
                        logger.trace("Removing face card from biggest pile {} and try to buy again", biggestPileIndex);
                        piles.get(pileIndex).addPointCard(piles.get(biggestPileIndex).removeLastPointCard());

                        return buyFaceCard(pileIndex, cardIndex);
                    } catch (PileOutOfCardsException pileOutOfCardsException) {
                        logger.error("Pile out of cards, after rebalancing from biggest pile");
                        return null;
                    }
                } else { // we can't remove active point cards from other piles
                    logger.trace("No other pile to take from");
                    return piles.get(pileIndex).forceRemoveFaceCard(cardIndex);
                }
            }
        }
    }


    public ICard getFaceCard(int pileIndex, int cardIndex) {
        try (final CloseableThreadContext.Instance _ = CloseableThreadContext
                .put("method", "getFaceCard")
                .put("pileIndex", Integer.toString(pileIndex))
                .put("cardIndex", Integer.toString(cardIndex))
        ) {
            logger.trace("Buying face card");
            if (piles.get(pileIndex).getFaceCard(cardIndex) == null) {
                logger.trace("Pile out of cards");
                //remove from the bottom of the biggest of the other piles
                Integer biggestPileIndex = _getBiggestPileThatHasGreaterThenOneCardIndex(pileIndex);
                ICard card = null;
                if (biggestPileIndex != null) {
                    try {
                        logger.trace("Removing face card from biggest pile {} and try to buy again", biggestPileIndex);
                        card = piles.get(biggestPileIndex).removeLastPointCard();
                        card.setCriteriaSideUp(false);
                        piles.get(pileIndex).addFaceCard(card, cardIndex);
                        return getFaceCard(pileIndex, cardIndex);
                    } catch (PileOutOfCardsException pileOutOfCardsException) {
                        logger.error("Pile out of cards, after rebalancing from biggest pile");
                        return null;
                    } catch (InvalidArgumentException e) {
                        logger.warn("Invalid argument exception when setting new face card", e);
                        if(card != null){
                            logger.warn("Adding point card back to the original pile");
                            card.setCriteriaSideUp(true);
                            piles.get(biggestPileIndex).addPointCard(card);
                        }
                        return null;
                    }
                } else { // we can't remove active point cards from other piles
                    logger.trace("No other pile to take from");
                    return piles.get(pileIndex).forceRemoveFaceCard(cardIndex);
                }
            }else{
                return piles.get(pileIndex).getFaceCard(cardIndex);
            }
        }

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


    public int countTotalVisibleFaceCards() {
        int count = 0;
        for (Pile pile : piles) {
            if (pile.getFaceCard(0) != null) {
                count++;
            }
            if (pile.getFaceCard(1) != null) {
                count++;
            }
        }
        return count;
    }

}
