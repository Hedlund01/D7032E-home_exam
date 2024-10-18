package networkIO.commands.send.game;

import card.ICard;
import networkIO.commands.ICommand;

import java.util.ArrayList;

public record AskFlipFaceCardCommand(ArrayList<ICard> hand) implements ICommand {
}
