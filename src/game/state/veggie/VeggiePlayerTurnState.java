package game.state.veggie;

import exceptions.InvalidArgumentException;
import exceptions.NotEnoughCardsInMarketException;
import game.state.common.StateContext;
import game.state.common.GameState;
import networkIO.commands.recive.ServerReceiveClientInputCommand;
import networkIO.commands.send.system.DisplayErrorMessageSendCommand;
import networkIO.commands.send.system.DisplayInvalidInputSendCommand;
import networkIO.commands.send.game.AskFlipFaceCardSendCommand;
import networkIO.commands.send.game.TakeTurnSendCommand;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Player;

import java.util.ArrayList;

public class VeggiePlayerTurnState extends GameState {
    private final Logger logger = LogManager.getLogger();
    private final Player player;

    public VeggiePlayerTurnState(StateContext stateContext, Player player) {
        super(stateContext);
        this.player = player;
    }

    @Override
    public void executeState() {
        try (final CloseableThreadContext.Instance _ = CloseableThreadContext
                .put("playerID", Integer.toString(player.getPlayerID()))) {

            logger.info("Player is taking its turn");


            int takenVeggies = 0;

            while (takenVeggies < 2) {
                if (market.isAllPilesEmpty()) {
                    break;
                }
                var pointCards = market.getAllVisiblePointCards();
                var faceCards = market.getAllVisibleFaceCards();
                var hand = player.getHand();
                var cmdTest  = new TakeTurnSendCommand(pointCards, faceCards, hand, takenVeggies);

                player.sendCommand(cmdTest);

                String playerInput;
                var command = player.readCommand();
                if(command instanceof ServerReceiveClientInputCommand cmd){
                    playerInput = cmd.getMessage();
                    playerInput = playerInput.toUpperCase();
                    logger.trace("Player input: {}", playerInput);
                }else{
                    logger.error("Invalid command type, expected SendClientInputToServerCommand but got: {}", command);
                    continue;
                }


                if (playerInput.matches("[0-2]") && takenVeggies == 0) {
                    try {
                        buyPointCard(playerInput);
                        break;
                    } catch (InvalidArgumentException e) {
                        player.sendCommand(new DisplayErrorMessageSendCommand(e.getMessage()));
                    }
                } else if (playerInput.matches("[A-F]{2}") || playerInput.matches("[A-F]")) {
                    try {
                        takenVeggies += buyVeggieCards(playerInput);
                    } catch (InvalidArgumentException | NotEnoughCardsInMarketException e) {
                        player.sendCommand(new DisplayErrorMessageSendCommand(e.getMessage()));
                    }
                } else {
                    player.sendCommand(new DisplayInvalidInputSendCommand());
                }
            }

            logger.trace("Checking if player has criteria cards in hand");
            if (player.countCriteriaCardsInHand() > 0) {
                //Give the player an option to turn a criteria card into a veggie card
                player.sendCommand(new AskFlipFaceCardSendCommand(player.getHand()));
                var inputCommand = player.readCommand();
                String choice = "";
                if(inputCommand instanceof ServerReceiveClientInputCommand cmd) {
                    choice = cmd.getMessage();
                }else {
                    logger.error("Invalid command type, expected SendClientInputToServerCommand but got: {}", inputCommand);
                }
                if (choice.matches("\\d")) {
                    int cardIndex = Integer.parseInt(choice);
                    player.setCriteriaSideDown(cardIndex);
                }
            }

            logger.trace("Player turn completed");
        }
    }

    private void buyPointCard(String input) throws InvalidArgumentException {
        if (!input.matches("[0-2]")) {
            logger.error("Invalid input, expected regex [1-3], but got: {}", input);
            throw new InvalidArgumentException("Invalid input, expected [1-3], but got: " + input);
        }
        if (market.getPointCard(Integer.parseInt(input)) == null) {
            throw new InvalidArgumentException("\nPile " + input + " is empty. Please choose another pile.\n");
        }
        var pointCard = market.buyPointCard(Integer.parseInt(input));
        player.addCardToHand(pointCard);
    }

    private int buyVeggieCards(String input) throws InvalidArgumentException, NotEnoughCardsInMarketException {
        int boughtCards = 0;
        if (!input.matches("[A-F]{2}|[A-F]") || input.length() > 2) {
            logger.error("Invalid input, expected [A-F]{2} or [A-F]{1} but got: [{}]", input);
            throw new InvalidArgumentException("Invalid input, expected [A-F]{2} or [A-F]{1} but got: " + input);
        }
        if (market.countTotalVisibleFaceCards() < input.length()) {
            throw new NotEnoughCardsInMarketException("\nThere are not enough veggie cards in the market to take " + input.length() + " cards. Please choose another option.\n");
        }

        ArrayList<Integer> veggieCardIndexes = new ArrayList<>();
        ArrayList<Integer> veggieCardPileIndexes = new ArrayList<>();
        for (char c : input.toCharArray()) {
            int choice = c - 'A';

            int pileIndex = (choice == 0 || choice == 1) ? 0 : (choice == 2 || choice == 3) ? 1 : (choice == 4 || choice == 5) ? 2 : -1;
            int veggieIndex = (choice == 0 || choice == 2 || choice == 4) ? 0 : (choice == 1 || choice == 3 || choice == 5) ? 1 : -1;

            veggieCardPileIndexes.add(pileIndex);
            veggieCardIndexes.add(veggieIndex);
        }

        for (int i = 0; i <= veggieCardIndexes.size() - 1; i++) {
            var veggieCard = market.buyFaceCard(veggieCardPileIndexes.get(i), veggieCardIndexes.get(i));
            if (veggieCard != null) {
                player.addCardToHand(veggieCard);
                boughtCards++;
            }
        }
        return boughtCards;
    }

    @Override
    public GameState getNextState() {
        return new VeggieNextPlayerState(stateContext, player);
    }


}
