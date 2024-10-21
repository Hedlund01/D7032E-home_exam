package networkIO.commands.send.system;

import networkIO.commands.SendCommand;


/**
 * Command to ask the client to send input.
 * This command sends a message to the client and prompts for an input line.
 */
public class AskClientToInputSendCommand extends SendCommand {

    private final String message;
    private final String inputLineMessage;

    /**
     * Command to ask the client to send input.
     * This command contains a message and an input line message.
     *
     * @param message the message to be sent to the client
     * @param inputLineMessage the message prompting the client for input
     */
    public AskClientToInputSendCommand(String message, String inputLineMessage) {
        this.message = message;
        this.inputLineMessage = inputLineMessage;
    }

    public String getMessage() {
        return message;
    }

    public String getInputLineMessage() {
        return inputLineMessage;
    }
}
