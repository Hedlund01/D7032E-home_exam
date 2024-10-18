package networkIO;

import card.ICard;
import networkIO.commands.recive.SendClientInputToServerCommand;
import networkIO.commands.send.display.*;
import networkIO.commands.send.game.AskFlipFaceCardCommand;
import networkIO.commands.send.game.TakeTurnCommand;
import networkIO.commands.send.system.AskClientToSendInputCommand;
import networkIO.commands.send.system.TerminateCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleClient extends Client {

    private int playerID;

    public ConsoleClient(String ipAddress, int port) throws Exception {
        super(ipAddress, port);
    }


    /**
     *
     */
    @Override
    protected void start() {
        while (true) {
            try {
                var command = inFromServer.readObject();

                switch (command) {
                    case DisplayConnectionMessageCommand cmd -> {
                        playerID = cmd.playerID();
                        consoleOutput("You are connected to the server as " + cmd.playerName());
                    }
                    case TakeTurnCommand cmd -> {


                        if (cmd.takenCards() > 0) {
                            consoleBreakLine("It's your turn again!");
                            consoleOutput("You have taken " + cmd.takenCards() + " vegetable cards.");
                            consoleOutput("Take 1 more vegetable cards.");
                        } else {
                            consoleBreakLine("It's your turn!");
                        }
                        consoleOutput(getHandDisplayString(cmd.hand(), null));
                        consoleOutput(getMarketDisplayString(cmd.pointCards(), cmd.faceCards()));
                        System.out.print("Card/s to take: ");
                        var input = System.console().readLine();
                        sendCommand(new SendClientInputToServerCommand(input));

                    }

                    case DisplayMessageCommand cmd -> {
                        consoleOutput(cmd.message());
                    }

                    case AskClientToSendInputCommand cmd -> {
                        consoleOutput(cmd.message());
                        System.out.print(cmd.inputLineMessage());
                        var input = System.console().readLine();
                        sendCommand(new SendClientInputToServerCommand(input));
                    }

                    case DisplayParticipantHandCommand cmd -> {
                        consoleBreakLine("Player " + cmd.playerName() + "'s hand");
                        consoleOutput(getHandDisplayString(cmd.hand(), cmd.playerName()));
                    }

                    case DisplayErrorMessageCommand cmd -> {
                        consoleBreakLine("Error");
                        consoleOutput(cmd.message());

                    }

                    case DisplayParticipantHandAndScoreCommand cmd -> {

                        if (cmd.isWinner()) {
                            consoleBreakLine("You are the winner!");
                        } else {
                            consoleBreakLine("Player " + cmd.name() + "'s hand and score");
                        }
                        consoleOutput(getHandDisplayString(cmd.hand(), cmd.name()));
                        consoleOutput("Score: " + cmd.score());
                    }

                    case AskFlipFaceCardCommand cmd -> {
                        consoleOutput("You have a criteria card in your hand. Would you like to flip it to a vegetable card?");
                        consoleOutput(getHandDisplayString(cmd.hand(), null));
                        System.out.print("Flip a card? (syntax [0/n]): ");

                        var input = System.console().readLine();
                        sendCommand(new SendClientInputToServerCommand(input));
                    }

                    case DisplayInvalidInputCommand _ -> {
                        consoleOutput("Invalid input. Please try again.");
                    }

                    case TerminateCommand _ -> {
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

    private void consoleOutput(String message) {
        System.out.println("\n" + message);
    }

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
