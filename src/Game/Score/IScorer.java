package Game.Score;

import Card.ICard;
import Player.Participant;

import java.util.ArrayList;

public interface IScorer {

    public int calculateScore(ArrayList<Participant> participants, Participant currentParticipant);
    }
