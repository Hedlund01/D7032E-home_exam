package Card;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public class Pile<T extends Enum<T>> {

    private ArrayList<Card<T>> pointCards;
    private Card<T>[] faceCards = new Card[2];

    public Pile(ArrayList<Card<T>> pointCards) {
        this.pointCards = pointCards;

        for(int i = 0; i < 2; i++) {
            this.faceCards[i] = pointCards.removeFirst();
            this.faceCards[i].setCriteriaSideUp(false);
        }
    }

    public Card<T> getPointCard() throws Exception {
        if (pointCards.isEmpty()){
            //[TODO] replace with maybe chain of responsibility, but it will only have one extra layer becuase market will fix it
            // Or maybe an observer or mediator pattern
            throw new Exception("Not implemented");
        }
        return pointCards.getFirst();
    }

    public Card<T> getFaceCard(int index) {
        return faceCards[index];
    }

    public boolean isEmpty(){
        return pointCards.isEmpty() && faceCards[0] == null && faceCards[1] == null;
    }
}
