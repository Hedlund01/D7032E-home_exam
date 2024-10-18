package networkIO.commands.send.display;

import card.ICard;
import networkIO.commands.ICommand;

import java.util.ArrayList;

public record DisplayParticipantHandCommand(String playerName, ArrayList<ICard> hand) implements ICommand {

}
