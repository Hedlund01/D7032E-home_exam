package networkIO.commands.send.display;

import networkIO.commands.ICommand;

public record DisplayErrorMessageCommand(String message) implements ICommand {

}
