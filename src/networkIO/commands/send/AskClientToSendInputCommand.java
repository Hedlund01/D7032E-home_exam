package networkIO.commands.send;

import networkIO.commands.CommandTypeEnum;
import networkIO.commands.ICommand;
import networkIO.commands.ICommandHasMessage;

public class AskClientToSendInputCommand implements ICommand, ICommandHasMessage {

    private final String message;

    public AskClientToSendInputCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public CommandTypeEnum getCommand() {
        return CommandTypeEnum.ASk_CLIENT_TO_SEND_INPUT;
    }
}
