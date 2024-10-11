package Game.Score;

import Card.ICard;
import Exceptions.NotImplementedException;

import java.util.ArrayList;

public class VeggieScorer implements IScorer {


    public int calculateScore(ArrayList<ICard> hand) {
        System.out.println("Calculating score for VeggieScorer");
        throw new NotImplementedException("VeggieScorer not implemented");
    }
}
