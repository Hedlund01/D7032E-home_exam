package networkIO.commands.recive;

import networkIO.commands.ICommand;

public record SendClientInputToServerCommand(String message) implements ICommand {
}
