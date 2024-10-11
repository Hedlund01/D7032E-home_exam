package Market;

import Card.ICard;
import Exceptions.PileOutOfCardsException;

import java.util.ArrayList;

public class Pile {

    private ArrayList<ICard> pointICards;
    private ICard[] faceICards = new ICard[2];

    public Pile(ArrayList<ICard> pointICards) {
        this.pointICards = pointICards;

        for (int i = 0; i < 2; i++) {
            this.faceICards[i] = pointICards.removeFirst();
            this.faceICards[i].setCriteriaSideUp(false);
        }
    }

    protected ICard getPointCard() throws PileOutOfCardsException {
        if (pointICards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointICards.getFirst();
    }

    protected ICard removeFirstPointCard() throws PileOutOfCardsException {
        if (pointICards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointICards.removeFirst();
    }

    protected ICard removeLastPointCard() throws PileOutOfCardsException {
        if (pointICards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointICards.removeLast();
    }

    protected ICard getFaceCard(int index) {
        return faceICards[index];
    }

    protected ICard removeFaceCard(int index) throws PileOutOfCardsException {
        ICard ICard = faceICards[index];
        if (getPointCardCount() <= 1) {
            throw new PileOutOfCardsException();
        } else {
            faceICards[index] = pointICards.removeFirst();
            faceICards[index].setCriteriaSideUp(false);
            return ICard;
        }
    }

    protected ICard forceRemoveFaceCard(int index) {
        ICard ICard = faceICards[index];
        faceICards[index] = pointICards.removeFirst();
        if (faceICards[index] != null) {
            faceICards[index].setCriteriaSideUp(false);
        }
        return ICard;
    }

    protected void addPointCard(ICard card) {
        pointICards.add(card);
    }


    protected boolean isEmpty() {
        return pointICards.isEmpty() && faceICards[0] == null && faceICards[1] == null;
    }

    protected int getPointCardCount() {
        return pointICards.size();
    }
}
