package networkIO.commands.send;

import networkIO.commands.CommandTypeEnum;
import networkIO.commands.ICommand;
import networkIO.commands.ICommandHasMessage;

public class DisplayConnectionMessageCommand extends ICommand, ICommandHasMessage {

    private final String message;

    public DisplayConnectionMessageCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public CommandTypeEnum getCommand() {
        return CommandTypeEnum.DISPLAY_CONNECTION_MESSAGE;
    }
}
