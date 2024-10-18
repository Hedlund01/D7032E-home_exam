package networkIO.commands.send.display;

import networkIO.commands.ICommand;

public record DisplayConnectionMessageCommand(String playerName, int playerID) implements ICommand {


}
