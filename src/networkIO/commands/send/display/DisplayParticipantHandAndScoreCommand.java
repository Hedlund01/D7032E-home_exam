package networkIO.commands.send.display;

import card.ICard;
import networkIO.commands.ICommand;

import java.util.ArrayList;

public record DisplayParticipantHandAndScoreCommand(String name, int score, ArrayList<ICard> hand, boolean isWinner) implements ICommand {

}
