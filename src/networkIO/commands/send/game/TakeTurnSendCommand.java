package networkIO.commands.send.game;

import card.ICard;
import networkIO.commands.SendCommand;

import java.util.ArrayList;

/**
 * Command to handle a player's turn.
 * This command sends the details of the player's turn, including the cards taken and the current hand.
 */
public class TakeTurnSendCommand extends SendCommand {

    private final ArrayList<ICard> pointCards;

    private final ArrayList<ICard> faceCards;

    private final ArrayList<ICard> hand;

    private final int takenCards;

    /**
     * Constructor to initialize the command with the player's turn details.
     *
     * @param pointCards List of point cards in the market.
     * @param faceCards  List of face cards in the market.
     * @param hand       List of cards in the player's hand.
     * @param takenCards Number of cards taken by the player.
     */
    public TakeTurnSendCommand(ArrayList<ICard> pointCards, ArrayList<ICard> faceCards, ArrayList<ICard> hand, int takenCards) {
        this.pointCards = pointCards;
        this.faceCards = faceCards;
        this.hand = hand;
        this.takenCards = takenCards;
    }

    /**
     * Gets the list of point cards in the market.
     *
     * @return List of point cards.
     */
    public ArrayList<ICard> getPointCards() {
        return pointCards;
    }

    /**
     * Gets the number of cards taken by the player.
     *
     * @return Number of cards taken.
     */
    public int getTakenCards() {
        return takenCards;
    }

    /**
     * Gets the list of cards in the player's hand.
     *
     * @return List of cards in hand.
     */
    public ArrayList<ICard> getHand() {
        return hand;
    }

    /**
     * Gets the list of face cards taken by the player.
     *
     * @return List of face cards.
     */
    public ArrayList<ICard> getFaceCards() {
        return faceCards;
    }
}