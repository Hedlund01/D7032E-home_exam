package player;

import java.util.ArrayList;

import card.ICard;

public abstract class Participant {
    private final int playerID;
    private String name;
    private final ArrayList<ICard> hand = new ArrayList<>();
    private Integer turnOrderIndex = null;

    public Participant(int playerID) {
        this.playerID = playerID;
    }

    public Integer getTurnOrderIndex() {
        return turnOrderIndex;
    }

    public void setTurnOrderIndex(int index) {
        turnOrderIndex = index;
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

    public String getName() {
        if (name != null) {
            return name;
        }
        return "Player " + playerID;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int countCriteriaCardsInHand() {
        return countCriteriaCards(hand);
    }

    public static int countCriteriaCards(ArrayList<ICard> hand) {
        int count = 0;
        for (ICard card : hand) {
            if (card.isCriteriaSideUp()) {
                count++;
            }
        }
        return count;
    }

    public void setCriteriaSideDown(int index) {
        hand.get(index).setCriteriaSideUp(false);
    }

    public int countFaceCardsInHand() {
        return countFaceCards(hand);
    }

    public <T extends Enum<T>> int countFaceCardsInHand(T face) {
        return countFaceCards(face, hand);
    }

    public static <T extends Enum<T>> int countFaceCards(T face, ArrayList<ICard> hand) {
        int count = 0;
        for (ICard card : hand) {
            if (card.getFace() == face && !card.isCriteriaSideUp()) {
                count++;
            }
        }
        return count;
    }

    public static int countFaceCards(ArrayList<ICard> hand) {
        int count = 0;
        for (ICard card : hand) {
            if (!card.isCriteriaSideUp()) {
                count++;
            }
        }
        return count;
    }

    public abstract boolean isBot();
}
