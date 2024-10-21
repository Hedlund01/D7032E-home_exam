package game.score;

import card.Card;
import card.ICard;
import card.Vegetable;
import org.junit.jupiter.api.Test;
import player.Bot;
import player.Participant;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class VeggieScorerTest {

    @Test
    void calculateScoreExampleFromRuleBook() {
        VeggieScorer scorer = new VeggieScorer();

        var cards1 = new ArrayList<ICard>();

        for (int i = 0; i < 5; i++) {
            var card = new Card<>(Vegetable.CARROT, "TEST");
            card.setCriteriaSideUp(false);
            cards1.add(card);
        }

        for (int i = 0; i < 2; i++) {
            var card = new Card<>(Vegetable.TOMATO, "TEST");
            card.setCriteriaSideUp(false);
            cards1.add(card);
        }

        for (int i = 0; i < 3; i++) {
            var card = new Card<>(Vegetable.LETTUCE, "TEST");
            card.setCriteriaSideUp(false);
            cards1.add(card);
        }

        for (int i = 0; i < 3; i++) {
            var card = new Card<>(Vegetable.ONION, "TEST");
            card.setCriteriaSideUp(false);
            cards1.add(card);
        }
        var card = new Card<>(Vegetable.CABBAGE, "TEST");
        card.setCriteriaSideUp(false);
        cards1.add(card);

        card = new Card<>(Vegetable.CARROT, "TOMATO + LETTUCE + CARROT = 8");
        cards1.add(card);

        card = new Card<>(Vegetable.CARROT, "CARROT + CARROT = 5");
        cards1.add(card);

        card = new Card<>(Vegetable.CARROT, "3 / CARROT,  -2 / ONION");
        cards1.add(card);

        card = new Card<>(Vegetable.CARROT, "MOST ONION = 10");
        cards1.add(card);

        card = new Card<>(Vegetable.CARROT, "5 / VEGETABLE TYPE >=3");
        cards1.add(card);

        var card2 = new Card<>(Vegetable.CARROT, "TEST");
        card2.setCriteriaSideUp(false);


        var participants = new ArrayList<Participant>();
        var bot1 = new Bot(0);
        var bot2 = new Bot(1);
        bot1.setTurnOrderIndex(0);
        bot2.setTurnOrderIndex(1);

        bot2.addCardToHand(card2);

        for (ICard c : cards1) {
            bot1.addCardToHand(c);
        }

        participants.add(bot1);
        participants.add(bot2);


        assertEquals(60, scorer.calculateScore(participants, bot1, bot1.getHand()));


    }

    @Test
    void calculateScoreIfMostAndTheSameAmountOfCardsWhenStartedBefore() {
        VeggieScorer scorer = new VeggieScorer();

        var cards1 = new ArrayList<ICard>();

        for (int i = 0; i < 3; i++) {
            var card = new Card<>(Vegetable.ONION, "TEST");
            card.setCriteriaSideUp(false);
            cards1.add(card);
        }

        var card = new Card<>(Vegetable.CARROT, "MOST ONION = 10");
        card.setCriteriaSideUp(true);
        cards1.add(card);


        var cards2 = new ArrayList<ICard>();

        for (int i = 0; i < 3; i++) {
            card = new Card<>(Vegetable.ONION, "TEST");
            card.setCriteriaSideUp(false);
            cards2.add(card);
        }


        var participants = new ArrayList<Participant>();
        var bot1 = new Bot(0);
        var bot2 = new Bot(1);
        bot1.setTurnOrderIndex(0);
        bot2.setTurnOrderIndex(1);

        for (ICard c : cards1) {
            bot1.addCardToHand(c);
        }

        for (ICard c : cards2) {
            bot2.addCardToHand(c);
        }

        participants.add(bot1);
        participants.add(bot2);


        assertEquals(0, scorer.calculateScore(participants, bot1, bot1.getHand()));
    }

    @Test
    void calculateScoreIfMostAndTheSameAmountOfCardsWhenStartedAfter() {
        VeggieScorer scorer = new VeggieScorer();

        var cards1 = new ArrayList<ICard>();

        for (int i = 0; i < 3; i++) {
            var card = new Card<>(Vegetable.ONION, "TEST");
            card.setCriteriaSideUp(false);
            cards1.add(card);
        }

        var card = new Card<>(Vegetable.CARROT, "MOST ONION = 10");
        cards1.add(card);


        var cards2 = new ArrayList<ICard>();

        for (int i = 0; i < 3; i++) {
            card = new Card<>(Vegetable.ONION, "TEST");
            card.setCriteriaSideUp(false);
            cards2.add(card);
        }



        var participants = new ArrayList<Participant>();
        var bot1 = new Bot(0);
        var bot2 = new Bot(1);
        bot1.setTurnOrderIndex(1);
        bot2.setTurnOrderIndex(0);

        for (ICard c : cards1) {
            bot1.addCardToHand(c);
        }

        for (ICard c : cards2) {
            bot2.addCardToHand(c);
        }

        participants.add(bot1);
        participants.add(bot2);


        assertEquals(10, scorer.calculateScore(participants, bot1, bot1.getHand()));
    }
}