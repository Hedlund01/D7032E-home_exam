package game.score;

import card.Card;
import card.ICard;
import card.Vegetable;
import org.junit.jupiter.api.Test;
import player.Bot;
import player.Participant;
import player.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VeggieScorerTest {

    @Test
    void calculateScore() {
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

        bot2.addCardToHand(card2);

        for (ICard c : cards1) {
            bot1.addCardToHand(c);
        }

        participants.add(bot1);
        participants.add(bot2);


        assertEquals(60, scorer.calculateScore(participants, bot1, bot1.getHand()));


    }
}