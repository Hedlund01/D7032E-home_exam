package networkIO.commands;

import java.io.Serializable;

public interface ICommand extends Serializable {
    CommandTypeEnum getCommand();
}
