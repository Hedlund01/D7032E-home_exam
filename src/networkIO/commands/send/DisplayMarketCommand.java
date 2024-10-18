package networkIO.commands.send;

import card.ICard;
import networkIO.commands.CommandTypeEnum;
import networkIO.commands.ICommand;

import java.util.ArrayList;

public class DisplayMarketCommand implements ICommand {
    public CommandTypeEnum command;
    public ArrayList<ICard> pointCards;
    public ArrayList<ICard> faceCards;
    public DisplayMarketCommand(ArrayList<ICard> pointCards, ArrayList<ICard> faceCards) {
        this.pointCards = pointCards;
        this.faceCards = faceCards;
        this.command = CommandTypeEnum.DISPLAY_MARKET;
    }

    @Override
    public CommandTypeEnum getCommand() {
        return command;
    }
}
