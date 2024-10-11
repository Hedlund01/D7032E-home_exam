package Game.Score;

import Card.ICard;

import java.util.ArrayList;

public interface IScorer {

    public int calculateScore(ArrayList<ICard> hand);
}
