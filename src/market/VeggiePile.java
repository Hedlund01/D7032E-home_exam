package market;

import card.ICard;
import exceptions.InvalidArgumentException;
import exceptions.PileOutOfCardsException;

import java.util.ArrayList;

public class VeggiePile implements IPile {

    private final ArrayList<ICard> pointCards;
    private final ICard[] faceCards = new ICard[2];

    public VeggiePile(ArrayList<ICard> pointCards) {
        this.pointCards = pointCards;

        if(pointCards.size() >= 2){
            for (int i = 0; i < 2; i++) {
                this.faceCards[i] = pointCards.removeFirst();
                this.faceCards[i].setCriteriaSideUp(false);
            }
        }

    }

    @Override
    public ICard getPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.getFirst();
    }

    @Override
    public ICard removeFirstPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.removeFirst();
    }

    @Override
    public ICard removeLastPointCard() throws PileOutOfCardsException {
        if (pointCards.isEmpty()) {
            throw new PileOutOfCardsException();
        }
        return pointCards.removeLast();
    }

    @Override
    public ICard getFaceCard(int index) {
        if(faceCards[index] == null && getPointCardCount() > 1){
            faceCards[index] = pointCards.removeLast();
            faceCards[index].setCriteriaSideUp(false);
        }

        return faceCards[index];
    }

    @Override
    public ICard removeFaceCard(int index) throws PileOutOfCardsException {
        if (getPointCardCount() <= 1) {
            throw new PileOutOfCardsException();
        } else {
            if(faceCards[index] == null) {
                faceCards[index] = pointCards.removeLast();
                faceCards[index].setCriteriaSideUp(false);
            }
            var card = faceCards[index];
            faceCards[index] = pointCards.removeFirst();
            faceCards[index].setCriteriaSideUp(false);
            return card;
        }
    }

    @Override
    public ICard forceRemoveFaceCard(int index) {
        ICard ICard = faceCards[index];
        if(getPointCardCount() <= 0){
            faceCards[index] = null;
            return ICard;
        }
        faceCards[index] = pointCards.removeFirst();
        faceCards[index].setCriteriaSideUp(false);
        return ICard;
    }

    @Override
    public void addPointCard(ICard card) {
        card.setCriteriaSideUp(true);
        pointCards.add(card);
    }

    @Override
    public void addFaceCard(ICard card, int index) throws InvalidArgumentException {
        if(faceCards[index] != null){
            throw new InvalidArgumentException("Face card already exists in this pile");
        }
        card.setCriteriaSideUp(false);
        faceCards[index] = card;
    }


    @Override
    public boolean isEmpty() {
        return pointCards.isEmpty() && faceCards[0] == null && faceCards[1] == null;
    }

    @Override
    public int getPointCardCount() {
        return pointCards.size();
    }

    @Override
    public int getFaceCardCount() {
        return faceCards[0] != null && faceCards[1] != null ? 2 : faceCards[0] == null && faceCards[1] == null ? 0 : 1;
    }
}
