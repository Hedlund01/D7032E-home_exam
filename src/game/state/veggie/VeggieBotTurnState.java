package game.state.veggie;

import card.ICard;
import exceptions.FatalGameErrorException;
import game.score.IScorer;
import game.score.VeggieScorer;
import game.state.common.GameState;
import game.state.common.StateContext;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Bot;
import player.Participant;

import java.util.ArrayList;

public class VeggieBotTurnState extends GameState {
    private final Logger logger = LogManager.getLogger();
    private final Bot bot;
    private final IScorer scorer = new VeggieScorer();

    public VeggieBotTurnState(StateContext stateContext, Participant participant) {
        super(stateContext);
        this.bot = (Bot) participant;
    }

    @Override
    public void executeState() {
        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("botID", Integer.toString(bot.getPlayerID()))

        ) {
            logger.info("Bot is taking its turn");
            int choice = (int) (Math.random() * 2);

            switch (choice) {
                case 0:
                    var buyPointCardSuccess = buyPointCard();
                    if (!buyPointCardSuccess) {
                        buyVeggieCards();
                    }
                    break;
                case 1:
                    var buyVeggieCardsSuccess = buyVeggieCards();
                    if (!buyVeggieCardsSuccess) {
                        buyPointCard();
                    }
                    break;
                default:
                    throw new FatalGameErrorException("Bot Choice not implemented");
            }

            stateContext.sendToAllPlayers("player " + bot.getPlayerID() + "'s hand is now: \n" + bot.getHandString() + "\n");
        }
    }

    private boolean buyVeggieCards() {
        logger.trace("Bot is trying to buy veggie cards");
        int cardsPicked = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= 1; j++) {

                if (market.getFaceCard(i, j) != null && cardsPicked < 2) {
                    bot.addCardToHand(market.buyFaceCard(i, j));
                    cardsPicked++;
                }
            }
        }
        if(cardsPicked > 0){
            logger.trace("Bot successfully bought {} veggie cards", cardsPicked);
            return true;
        }else{
            logger.trace("Bot failed to buy veggie cards");
            return false;
        }
    }


    /**
     * Buys point cards for the bot if available.
     *
     * @return true if a point card was successfully bought, false otherwise
     */
    private boolean buyPointCard() {
        logger.trace("Bot is trying to buy a point card");
        int highestPointCardIndex = 0;
        int highestPointCard = 0;
        for (int i = 0; i < 3; i++) {
            if (market.getPointCard(i) != null) {
                ArrayList<ICard> tmpHand = (ArrayList<ICard>) bot.getHand().clone();
                tmpHand.add(market.getPointCard(i));
                int score = scorer.calculateScore(participants, bot, tmpHand);
                if (score > highestPointCard) {
                    highestPointCard = score;
                    highestPointCardIndex = i;
                }
            }
        }
        var card = market.getPointCard(highestPointCardIndex);
        if (card != null) {
            bot.addCardToHand(card);
            logger.trace("Bot successfully bought a point card");
            return true;
        } else {
            logger.trace("Bot failed to buy a point card");
            return false;
        }
    }

    @Override
    public void executeNextState() {
        stateContext.setNextState(new VeggieNextPlayerState(stateContext, bot));
        stateContext.executeNextState();
    }
}