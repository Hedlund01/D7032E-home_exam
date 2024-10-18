package market;

import card.ICard;
import exceptions.InvalidArgumentException;
import exceptions.PileOutOfCardsException;
import card.CardFactory;
import card.IConcreteCardFactory;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class Market<T extends Enum<T>> implements IMarket {
    private final Logger logger = LogManager.getLogger();
    protected ArrayList<IPile> piles = new ArrayList<>();
    protected final IConcreteCardFactory cardFactory;
    protected final Class<T> enumType;

    public Market(Class<T> enumType) {
        this.enumType = enumType;
        CardFactory cardFactory = new CardFactory();
        this.cardFactory = cardFactory.getCardFactory(enumType.getTypeName());
    }


    public abstract void initializeMarket(int nrOfPlayers, String path);


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
                        logger.warn("Pile out of cards, after rebalancing from biggest pile");
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
                        var card = piles.get(biggestPileIndex).removeLastPointCard();
                        piles.get(pileIndex).addPointCard(card);

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
            logger.trace("Getting face card");
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
        for (IPile pile : piles) {
            if (!pile.isEmpty()) {
                return false;
            }
        }
        return true;
    }




    public int countTotalVisibleFaceCards() {
        int count = 0;
        for (IPile pile : piles) {
            count += pile.getFaceCardCount();
        }
        return count;
    }

}
