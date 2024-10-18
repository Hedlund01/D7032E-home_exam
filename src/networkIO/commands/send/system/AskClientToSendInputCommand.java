package networkIO.commands.send.system;

import networkIO.commands.ICommand;

public record AskClientToSendInputCommand(String message, String inputLineMessage) implements ICommand {

}
