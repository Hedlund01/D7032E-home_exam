package networkIO.commands.send.game;

import card.ICard;
import networkIO.commands.SendCommand;

import java.util.ArrayList;

/**
 * Command to ask the player to flip a face card.
 * This command sends the hand of cards to the player.
 */
public class AskFlipFaceCardSendCommand extends SendCommand {

    private final ArrayList<ICard> hand;

    /**
     * Constructor to initialize the command with the hand of cards.
     *
     * @param hand the hand of cards
     */
    public AskFlipFaceCardSendCommand(ArrayList<ICard> hand) {
        this.hand = hand;
    }

    /**
     * Gets the hand of cards.
     *
     * @return the hand of cards
     */
    public ArrayList<ICard> getHand() {
        return hand;
    }
}
