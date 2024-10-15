package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import card.ICard;

public abstract class Participant  {
    private final int playerID;
    private final ArrayList<ICard> hand = new ArrayList<ICard>();

    public Participant(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void addCardToHand(ICard ICard) {
        hand.add(ICard);
    }

    public ArrayList<ICard> getHand() {
        return hand;
    }

    public String getHandString(){
        StringBuilder handString = new StringBuilder();
        handString.append("Criteria:\t");
        for (ICard card : hand) {
            if(card.isCriteriaSideUp() && card.getFace() != null){
                handString.append(String.format("[%d] %s (%s)\t", hand.indexOf(card), card.getCriteria(), card.getFace().toString()));
            }
        }
        handString.append("\nVegetables:\n");
        Map<String, Integer> faceCount = new HashMap<>();
        for(ICard card: hand){
            if(!card.isCriteriaSideUp()){
                faceCount.put(card.getFace().toString(), faceCount.getOrDefault(card.getFace().toString(), 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : faceCount.entrySet()) {
            handString.append(String.format("%s: %d\t", entry.getKey(), entry.getValue()));
        }

        return handString.toString();
    }


    public int countCriteriaCardsInHand(){
        int count = 0;
        for(ICard card : hand){
            if(card.isCriteriaSideUp()){
                count++;
            }
        }
        return count;
    }

    public void setCriteriaSideDown(int index){
        hand.get(index).setCriteriaSideUp(false);
    }

    public int countFaceCardsInHand(){
        return countFaceCards(hand);
    }

    public <T extends Enum<T>> int  countFaceCardsInHand(T face){
        return countFaceCards(face, hand);
    }

    public static <T extends Enum<T>> int  countFaceCards(T face, ArrayList<ICard> hand){
        int count = 0;
        for(ICard card : hand){
            if(card.getFace() == face){
                count++;
            }
        }
        return count;
    }

    public static int  countFaceCards(ArrayList<ICard> hand){
        int count = 0;
        for(ICard card : hand){
            if(!card.isCriteriaSideUp()){
                count++;
            }
        }
        return count;
    }

    public abstract boolean isBot();
}
