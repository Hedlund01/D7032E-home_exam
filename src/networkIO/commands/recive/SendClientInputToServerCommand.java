package networkIO.commands.recive;

import networkIO.commands.CommandTypeEnum;
import networkIO.commands.ICommand;
import networkIO.commands.ICommandHasMessage;

public class SendClientInputToServerCommand implements ICommand, ICommandHasMessage {
    private final String message;

    public SendClientInputToServerCommand(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public CommandTypeEnum getCommand() {
        return CommandTypeEnum.SEND_CLIENT_INPUT_TO_SERVER;
    }
}
