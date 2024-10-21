package networkIO.commands.send.game;

import card.ICard;
import networkIO.commands.SendCommand;

import java.util.ArrayList;

/**
 * Command to display a participant's hand.
 * This command sends the participant's name and their current hand of cards.
 */
public class DisplayParticipantHandSendCommand extends SendCommand {

    private final String name;

    private final ArrayList<ICard> hand;

    /**
     * Constructor to initialize the command with the participant's name and hand of cards.
     *
     * @param name The name of the participant.
     * @param hand The hand of cards held by the participant.
     */
    public DisplayParticipantHandSendCommand(String name, ArrayList<ICard> hand) {
        this.name = name;
        this.hand = hand;
    }

    /**
     * Gets the name of the participant.
     *
     * @return The name of the participant.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the hand of cards held by the participant.
     *
     * @return The hand of cards.
     */
    public ArrayList<ICard> getHand() {
        return hand;
    }
}