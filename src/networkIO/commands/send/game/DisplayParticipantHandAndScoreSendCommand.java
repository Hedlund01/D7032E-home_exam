package networkIO.commands.send.game;

import card.ICard;
import networkIO.commands.SendCommand;

import java.util.ArrayList;

/**
 * Command to display a participant's hand and score.
 * This command sends the participant's name, score, hand of cards, and winner status.
 */
public class DisplayParticipantHandAndScoreSendCommand extends SendCommand {

    private final String name;
    private final int score;
    private final ArrayList<ICard> hand;
    private final boolean isWinner;
    private final int playerID;

    /**
     * Constructor to initialize the command with the participant's name, score, hand of cards, and winner status.
     *
     * @param name the name of the participant
     * @param score the score of the participant
     * @param hand the hand of cards of the participant
     * @param isWinner the winner status of the participant
     */
    public DisplayParticipantHandAndScoreSendCommand(int playerID, String name, int score, ArrayList<ICard> hand, boolean isWinner) {
        this.playerID = playerID;
        this.name = name;
        this.score = score;
        this.hand = hand;
        this.isWinner = isWinner;
    }

    /**
     * Gets the name of the participant.
     *
     * @return the name of the participant
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the score of the participant.
     *
     * @return the score of the participant
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the hand of cards held by the participant.
     *
     * @return the hand of cards
     */
    public ArrayList<ICard> getHand() {
        return hand;
    }

    /**
     * Gets the winner status of the participant.
     *
     * @return the winner status of the participant
     */
    public boolean isWinner() {
        return isWinner;
    }

    public int getPlayerID() {
        return playerID;
    }
}
