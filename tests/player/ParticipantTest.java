/*
 * Copyright (c) 2024 Hugo Hedlund
 */

package player;

import card.Card;
import card.TestEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    Participant participant;

    @BeforeEach
    void setUp() {
        participant = new Participant(1) {
            @Override
            public boolean isBot() {
                return false;
            }
        };
    }

    @Test
    void getPlayerID() {
        assertEquals(1, participant.getPlayerID());
    }

    @Test
    void addCardToHandAndGetHand() {
        var card = new Card<TestEnum>(TestEnum.TEST, "TEST");
        participant.addCardToHand(card);
        assertAll(() -> {
            assertEquals(1, participant.getHand().size());
            assertEquals(card, participant.getHand().getFirst());
        });
    }


    @Test
    void countCriteriaCardsAndCountCriteriaCardsInHand() {
        var card = new Card<TestEnum>(TestEnum.TEST, "TEST");
        participant.addCardToHand(card);

        assertEquals(1, participant.countCriteriaCardsInHand());
    }

    @Test
    void setCriteriaSideDown() {
        var card = new Card<TestEnum>(TestEnum.TEST, "TEST");
        participant.addCardToHand(card);
        participant.setCriteriaSideDown(0);

        assertFalse(participant.getHand().getFirst().isCriteriaSideUp());
    }

    @Test
    void countFaceCardsAndCountFaceCardsInHand() {
        var card = new Card<>(TestEnum.TEST, "TEST");
        participant.addCardToHand(card);
        card.setCriteriaSideUp(false);

        assertAll(() -> {
            assertEquals(1, participant.countFaceCardsInHand());
            assertEquals(1, participant.countFaceCardsInHand(TestEnum.TEST));
        });

    }


}