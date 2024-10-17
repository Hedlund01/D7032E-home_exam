package game.state.veggie;

import exceptions.InvalidArgumentException;
import exceptions.NotEnoughCardsInMarketException;
import game.state.common.StateContext;
import game.state.common.GameState;
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

            printStartInformation();

            int takenVeggies = 0;

            while (takenVeggies < 2) {
                if(market.isAllPilesEmpty()){
                    break;
                }
                player.sendMessage(market.getDisplayString());
                if(takenVeggies > 0){
                    player.sendMessage("\nYou have taken " + takenVeggies + " vegetable cards.");
                    player.sendMessage("Take " + (2 - takenVeggies) + " more vegetable cards.\n");
                }else{
                    player.sendMessage("\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
                }
                String playerInput = player.readMessage();
                logger.trace("Player input: {}", playerInput);
                if (playerInput.matches("[0-2]") && takenVeggies == 0) {
                    try {
                        buyPointCard(playerInput);
                        break;
                    } catch (InvalidArgumentException e) {
                        player.sendMessage("\n" + e.getMessage() + "\n");
                    }
                } else if (playerInput.matches("[A-F]{2}") || playerInput.matches("[A-F]")) {
                    try {
                        takenVeggies += buyVeggieCards(playerInput);
                    } catch (InvalidArgumentException | NotEnoughCardsInMarketException e) {
                        player.sendMessage("\n" + e.getMessage() + "\n");
                    }
                } else {
                    player.sendMessage("\nInvalid input. Please enter a valid input. Either [A-F][A-F], [A-F] or [1-2].\n");
                }
            }

            logger.trace("Checking if player has criteria cards in hand");
            if (player.countCriteriaCardsInHand() > 0) {
                //Give the player an option to turn a criteria card into a veggie card
                player.sendMessage("\n" + player.getHandString() + "\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");
                String choice = player.readMessage();
                if (choice.matches("\\d")) {
                    int cardIndex = Integer.parseInt(choice);
                    player.setCriteriaSideDown(cardIndex);
                }
            }

            printEndInformation();
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

        player.sendMessage("\nYou have taken a card from pile " + input + " and added it to your hand.\n");
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

            int pileIndex = (choice == 0 || choice == 3) ? 0 : (choice == 1 || choice == 4) ? 1 : (choice == 2 || choice == 5) ? 2 : -1;
            int veggieIndex = (choice == 0 || choice == 1 || choice == 2) ? 0 : (choice == 3 || choice == 4 || choice == 5) ? 1 : -1;

            veggieCardPileIndexes.add(pileIndex);
            veggieCardIndexes.add(veggieIndex);
        }

        for (int i = 0; i <= veggieCardIndexes.size() - 1; i++) {
            var veggieCard = market.buyFaceCard(veggieCardPileIndexes.get(i), veggieCardIndexes.get(i));
            if (veggieCard == null) {
                player.sendMessage("\n Veggie pile " + input.toCharArray()[i] + " is empty. Please choose another pile\n");
            } else {
                player.sendMessage("\nYou have taken a card from pile " + input.toCharArray()[i] + " and added it to your hand.\n");
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


    private void printStartInformation() {
        player.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
        player.sendMessage(player.getHandString());
        player.sendMessage("\nThe piles are: ");


    }


    private void printEndInformation() {
        player.sendMessage("\nYour turn is completed\n****************************************************************\n\n");
        stateContext.sendToAllPlayers("player " + player.getPlayerID() + "'s hand is now: \n" + player.getHandString() + "\n");
    }
}
