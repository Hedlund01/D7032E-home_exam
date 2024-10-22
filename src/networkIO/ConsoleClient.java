package networkIO;

import card.ICard;
import networkIO.commands.recive.ServerReceiveClientInputCommand;
import networkIO.commands.send.game.AskFlipFaceCardSendCommand;
import networkIO.commands.send.game.DisplayParticipantHandAndScoreSendCommand;
import networkIO.commands.send.game.DisplayParticipantHandSendCommand;
import networkIO.commands.send.game.TakeTurnSendCommand;
import networkIO.commands.send.system.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The ConsoleClient class extends the Client class and handles the communication
 * with the server, processing various commands received from the server.
 */
public class ConsoleClient extends Client {

    private int playerID;

    /**
     * Constructs a new ConsoleClient with the specified IP address and port.
     *
     * @param ipAddress the IP address of the server
     * @param port the port number of the server
     * @throws Exception if an error occurs during the connection
     */
    public ConsoleClient(String ipAddress, int port) throws Exception {
        super(ipAddress, port);
    }


    @Override
    protected void start() {
        while (true) {
            try {
                var command = inFromServer.readObject();

                switch (command) {
                    case DisplayConnectionMessageSendCommand cmd -> {
                        playerID = cmd.getPlayerID();
                        consoleOutput("You are connected to the server as " + cmd.getPlayerName());
                    }
                    case TakeTurnSendCommand cmd -> {
                        if (cmd.getTakenCards() > 0) {
                            consoleBreakLine("It's your turn again!");
                            consoleOutput("You have taken " + cmd.getTakenCards() + " vegetable cards.");
                            consoleOutput("Take 1 more vegetable cards.");
                        } else {
                            consoleBreakLine("It's your turn!");
                        }
                        consoleOutput(getHandDisplayString(cmd.getHand(), null));
                        consoleOutput(getMarketDisplayString(cmd.getPointCards(), cmd.getFaceCards()));
                        System.out.print("Card/s to take: ");
                        var input = System.console().readLine();
                        sendCommand(new ServerReceiveClientInputCommand(input));
                    }
                    case DisplayMessageSendCommand cmd -> {
                        consoleOutput(cmd.getMessage());
                    }
                    case AskClientToInputSendCommand cmd -> {
                        consoleOutput(cmd.getMessage());
                        System.out.print("Input: ");
                        var input = System.console().readLine();
                        sendCommand(new ServerReceiveClientInputCommand(input));
                    }
                    case DisplayParticipantHandSendCommand cmd -> {
                        consoleBreakLine("Player " + cmd.getName() + "'s hand");
                        consoleOutput(getHandDisplayString(cmd.getHand(), cmd.getName()));
                    }
                    case DisplayErrorMessageSendCommand cmd -> {
                        consoleBreakLine("Error");
                        consoleOutput(cmd.getMessage());
                    }
                    case DisplayParticipantHandAndScoreSendCommand cmd -> {
                        if (cmd.getPlayerID() == playerID && cmd.isWinner()) {
                            consoleBreakLine("You are the winner!");
                        } else if (cmd.isWinner()) {
                            consoleBreakLine("Player " + cmd.getName() + " is the winner!");
                        } else {
                            consoleBreakLine("Player " + cmd.getName() + "'s hand and score");
                        }
                        consoleOutput(getHandDisplayString(cmd.getHand(), cmd.getName()));
                        consoleOutput("Score: " + cmd.getScore());
                    }
                    case AskFlipFaceCardSendCommand cmd -> {
                        consoleOutput("You have a criteria card in your hand. Would you like to flip it to a vegetable card?");
                        consoleOutput(getHandDisplayString(cmd.getHand(), null));
                        System.out.print("Flip a card? (syntax [0/n]): ");
                        var input = System.console().readLine();
                        sendCommand(new ServerReceiveClientInputCommand(input));
                    }
                    case DisplayInvalidInputSendCommand _ -> {
                        consoleOutput("Invalid input. Please try again.");
                    }
                    case TerminateSendCommand _ -> {
                        consoleOutput("Game has ended.");
                        close();
                        System.exit(0);
                    }
                    default -> {
                        System.out.println("Unknown command received from server, command: " + command.getClass().getName());
                    }
                }

            } catch (ClassCastException e) {
                System.out.println("Error reading command from server. Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error reading from server. Error: " + e.getMessage());
            }
        }
    }

    /**
     * Outputs a message to the console.
     *
     * @param message the message to output
     */
    private void consoleOutput(String message) {
        System.out.println("\n" + message);
    }

    /**
     * Outputs a message to the console with a break line.
     *
     * @param message the message to output
     */
    private void consoleBreakLine(String message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (80 - message.length()) / 2; i++) {
            sb.append("*");
        }
        sb.append(" " + message + " ");
        for (int i = 0; i < (80 - message.length()) / 2; i++) {
            sb.append("*");
        }
        System.out.println(sb);
    }

    /**
     * Generates a string representation of the market display.
     *
     * @param pointCards the list of point cards
     * @param faceCards the list of face cards
     * @return the string representation of the market display
     */
    private String getMarketDisplayString(ArrayList<ICard> pointCards, ArrayList<ICard> faceCards) {
        StringBuilder pileString = new StringBuilder();
        pileString.append("Market:\n");
        pileString.append("Point Cards:\t");
        for (int i = 0; i < pointCards.size(); i++) {
            if (pointCards.get(i) != null) {
                pileString.append("[").append(i).append("]").append(String.format("%-43s", pointCards.get(i))).append("\t");
            } else {
                pileString.append("[").append(i).append("]").append(String.format("%-43s", "Empty")).append("\t");
            }
        }
        pileString.append("\nVeggie Cards:\t");
        char veggieCardIndex = 'A';
        for (int i = 0; i < faceCards.size(); i++) {
            if (faceCards.get(i) != null) {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", faceCards.get(i))).append("\t");
            } else {
                pileString.append("[").append(veggieCardIndex).append("]").append(String.format("%-43s", "Empty")).append("\t");
            }
            veggieCardIndex++;
        }
        return pileString.toString();
    }

    /**
     * Generates a string representation of the hand display.
     *
     * @param hand the list of cards in hand
     * @param playerName the name of the player
     * @return the string representation of the hand display
     */
    private String getHandDisplayString(ArrayList<ICard> hand, String playerName) {
        StringBuilder handString = new StringBuilder();
        if (playerName != null) {
            handString.append("Player ").append(playerName).append("'s hand is:\n");
        } else {
            handString.append("Your hand is:\n");
        }
        handString.append("Criteria:\t");
        for (ICard card : hand) {
            if (card.isCriteriaSideUp() && card.getFace() != null) {
                handString.append(String.format("[%d] %s (%s)\t", hand.indexOf(card), card.getCriteria(), card.getFace().toString()));
            }
        }
        handString.append("\nVegetables:\n");
        Map<String, Integer> faceCount = new HashMap<>();
        for (ICard card : hand) {
            if (!card.isCriteriaSideUp()) {
                faceCount.put(card.getFace().toString(), faceCount.getOrDefault(card.getFace().toString(), 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : faceCount.entrySet()) {
            handString.append(String.format("%s: %d\t", entry.getKey(), entry.getValue()));
        }
        return handString.toString();
    }
}