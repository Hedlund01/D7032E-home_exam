package networkIO.commands;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ObjectOutputStream;

public abstract class SendCommand implements ISendCommand {
    private static final Logger logger = LogManager.getLogger();



    public boolean execute(ObjectOutputStream outToClient) {
        try (final var _ = CloseableThreadContext.put("command", this.getClass().getSimpleName()))
        {
            try {
                outToClient.writeObject(this);
                outToClient.reset(); // Reset the stream, because otherwise it will try to cache the object and send it as a reference instead of a new object.
                return true;
            } catch (Exception e) {
                logger.error("Error sending command to player. Error: {}",  e.getMessage());
                return false;
            }
        }
    }
}
