package networkIO.commands.send;

import networkIO.commands.CommandTypeEnum;
import networkIO.commands.ICommand;
import networkIO.commands.ICommandHasMessage;

public class DisplayStringCommand implements ICommand, ICommandHasMessage {
    private final String message;

    public DisplayStringCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public CommandTypeEnum getCommand() {
        return CommandTypeEnum.DISPLAY_STRING;
    }
}
