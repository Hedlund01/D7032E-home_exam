package networkIO.commands.send;

import networkIO.commands.CommandTypeEnum;
import networkIO.commands.ICommand;

public class TerminateCommand implements ICommand {

    /**
     * @return
     */
    @Override
    public CommandTypeEnum getCommand() {
        return CommandTypeEnum.TERMINATE;
    }
}
