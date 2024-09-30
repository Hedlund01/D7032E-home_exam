package Player;

import Card.Card;
import Card.Vegetable;

import java.util.ArrayList;

public abstract class Participant<T extends Enum<T>> implements IIsBot {
    private int playerID;
    private Class<T> faceClass;
    private ArrayList<Card<T>> hand = new ArrayList<Card<T>>();

    public Participant(int playerID, Class<T> faceClass) {
        this.playerID = playerID;
        this.faceClass = faceClass;
    }

    public void addCardToHand(Card<T> card) {
        hand.add(card);
    }

    public ArrayList<Card<T>> getHand() {
        return hand;
    }

    public String getHandString(){
        StringBuilder handString = new StringBuilder();
        handString.append("Criteria:\t");
        for (Card<T> card : hand) {
            if(card.isCriteriaSideUp() && card.getFace() != null){
                handString.append(String.format("[%d] %s (%s)\t", hand.indexOf(card), card.getCriteria(), card.getFace().toString()));
            }
        }
        handString.append("\nVegetables:\n");
        for (var c : faceClass.getEnumConstants()) {
            int count = countFaceCardInHand(c);
            if(count > 0){
                handString.append(String.format("%s: %d\t", c.toString(), count));
            }
        }
        return handString.toString();
    }

    public int countFaceCardInHand(T face){
        int count = 0;
        for (Card<T> card : hand) {
            if(card.getFace() == face && card.isCriteriaSideUp()){
                count++;
            }
        }
        return count;

    }
}
