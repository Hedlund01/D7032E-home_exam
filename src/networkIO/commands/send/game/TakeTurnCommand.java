package networkIO.commands.send.game;

import card.ICard;
import networkIO.commands.ICommand;

import java.util.ArrayList;

public record TakeTurnCommand(ArrayList<ICard> pointCards,
                              ArrayList<ICard> faceCards,
                              ArrayList<ICard> hand,
                              int takenCards)
        implements ICommand {

}
