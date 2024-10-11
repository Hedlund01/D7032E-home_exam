package game.state.veggie;

import card.ICard;
import exceptions.FatalGameErrorException;
import game.score.IScorer;
import game.score.VeggieScorer;
import game.state.common.GameState;
import game.state.common.StateContext;
import player.Bot;
import player.Participant;

import java.util.ArrayList;

public class VeggieBotTurnState extends GameState {
    private final Bot bot;
    private final IScorer scorer = new VeggieScorer();

    public VeggieBotTurnState(StateContext stateContext, Participant participant) {
        super(stateContext);
        this.bot = (Bot) participant;
    }

    @Override
    public void executeState() {
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


    }

    private boolean buyVeggieCards() {
        int cardsPicked = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= 1; j++) {

                if (market.getVeggieCard(i, j) != null && cardsPicked < 2) {
                    bot.addCardToHand(market.buyVeggieCard(i, j));
                    cardsPicked++;
                }
            }
        }
        return cardsPicked > 0;

    }


    /**
     * Buys point cards for the bot if available.
     *
     * @return true if a point card was successfully bought, false otherwise
     */
    private boolean buyPointCard() {
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
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void executeNextStage() {
        stateContext.setNextState(new VeggieNextPlayerState(stateContext, bot));
    }
}
