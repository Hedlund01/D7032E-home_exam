package game.score;

import card.ICard;
import player.Participant;

import java.util.ArrayList;

public interface IScorer {

    int calculateScore(ArrayList<Participant> participants, Participant currentParticipant, ArrayList<ICard> hand);
}
