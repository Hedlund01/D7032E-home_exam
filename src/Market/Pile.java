package Market;

import Card.Card;
import Exceptions.PileOutOfCardsException;

import java.util.ArrayList;

public class Pile<T extends Enum<T>> {

    private ArrayList<Card<T>> pointCards;
    private Card<T>[] faceCards = new Card[2];

    public Pile(ArrayList<Card<T>> pointCards) {
        this.pointCards = pointCards;

        for (int i = 0; i < 2; i++) {
            this.faceCards[i] = pointCards.removeFirst();
            this.faceCards[i].setCriteriaSideUp(false);
        }
    }

    protected Card<T> getPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.getFirst();
    }

    protected Card<T> removeFirstPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.removeFirst();
    }

    protected Card<T> removeLastPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.removeLast();
    }

    protected Card<T> getFaceCard(int index) {
        return faceCards[index];
    }

    protected Card<T> removeFaceCard(int index) throws PileOutOfCardsException {
        Card<T> card = faceCards[index];
        if (getPointCardCount() <= 1) {
            throw new PileOutOfCardsException();
        } else {
            faceCards[index] = pointCards.removeFirst();
            faceCards[index].setCriteriaSideUp(false);
            return card;
        }
    }

    protected Card<T> forceRemoveFaceCard(int index) {
        Card<T> card = faceCards[index];
        faceCards[index] = pointCards.removeFirst();
        if (faceCards[index] != null) {
            faceCards[index].setCriteriaSideUp(false);
        }
        return card;
    }

    protected void addPointCard(Card<T> card) {
        pointCards.add(card);
    }


    protected boolean isEmpty() {
        return pointCards.isEmpty() && faceCards[0] == null && faceCards[1] == null;
    }

    protected int getPointCardCount() {
        return pointCards.size();
    }
}
