package market;

import card.ICard;
import exceptions.PileOutOfCardsException;

import java.util.ArrayList;

public class Pile {

    private final ArrayList<ICard> pointCards;
    private final ICard[] faceCards = new ICard[2];

    public Pile(ArrayList<ICard> pointCards) {
        this.pointCards = pointCards;

        for (int i = 0; i < 2; i++) {
            this.faceCards[i] = pointCards.removeFirst();
            this.faceCards[i].setCriteriaSideUp(false);
        }
    }

    protected ICard getPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.getFirst();
    }

    protected ICard removeFirstPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.removeFirst();
    }

    protected ICard removeLastPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.removeLast();
    }

    protected ICard getFaceCard(int index) {
        return faceCards[index];
    }

    protected ICard removeFaceCard(int index) throws PileOutOfCardsException {
        ICard ICard = faceCards[index];
        if (getPointCardCount() <= 1) {
            throw new PileOutOfCardsException();
        } else {
            faceCards[index] = pointCards.removeFirst();
            faceCards[index].setCriteriaSideUp(false);
            return ICard;
        }
    }

    protected ICard forceRemoveFaceCard(int index) {
        ICard ICard = faceCards[index];
        faceCards[index] = pointCards.removeFirst();
        if (faceCards[index] != null) {
            faceCards[index].setCriteriaSideUp(false);
        }
        return ICard;
    }

    protected void addPointCard(ICard card) {
        pointCards.add(card);
    }


    protected boolean isEmpty() {
        return pointCards.isEmpty() && faceCards[0] == null && faceCards[1] == null;
    }

    protected int getPointCardCount() {
        return pointCards.size();
    }
}
