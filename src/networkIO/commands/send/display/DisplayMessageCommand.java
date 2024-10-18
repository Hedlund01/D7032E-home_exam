package networkIO.commands.send.display;

import networkIO.commands.ICommand;

public record DisplayMessageCommand(String message) implements ICommand {

}
