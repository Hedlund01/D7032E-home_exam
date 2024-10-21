package networkIO.commands;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface ISendCommand extends Serializable {
    boolean execute(ObjectOutputStream outToClient);
}
