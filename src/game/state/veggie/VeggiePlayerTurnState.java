package game.state.veggie;

import exceptions.NotImplementedException;
import game.state.common.StateContext;
import game.state.common.GameState;
import player.Participant;
import player.Player;

public class VeggiePlayerTurnState extends GameState {
    private final Player player;
    public VeggiePlayerTurnState(StateContext stateContext, Player player) {
        super( stateContext);
        this.player = player;
    }

    //[TODO] Clean up this method
    @Override
    public void executeState() {
        printInformation();
        boolean validChoice = false;
        while (!validChoice) {

            player.sendMessage("\n\nTake either one point card (Syntax example: 2) or up to two vegetable cards (Syntax example: CF).\n");
            String input = player.readMessage();
            if(input.matches("\\d")){
                int pileIndex = Integer.parseInt(input);
                var pointCard = market.getPointCard(pileIndex);
                if(pointCard == null){
                    player.sendMessage("\nThis pile is empty. Please choose another pile.\n");
                } else {
                    player.addCardToHand(market.buyPointCard(pileIndex));
                    player.sendMessage("\nYou took a card from pile " + pileIndex + " and added it to your hand.\n");
                    validChoice = true;
                }
            }else{
                int takenVeggies = 0;
                //[TODO] [BUG] If the player sends AÖ and then AB it will get 3 cards
                for(int charIndex = 0; charIndex < input.length(); charIndex++) {
                    if(Character.toUpperCase(input.charAt(charIndex)) < 'A' || Character.toUpperCase(input.charAt(charIndex)) > 'F') {
                        player.sendMessage("\nInvalid choice. Please choose up to two veggie cards from the market.\n");
                        validChoice = false;
                        break;
                    }
                    int choice = Character.toUpperCase(input.charAt(charIndex)) - 'A';
                    int pileIndex = (choice == 0 || choice == 3) ? 0 : (choice == 1 || choice == 4) ? 1 : (choice == 2 || choice == 5) ? 2:-1;
                    int veggieIndex = (choice == 0 || choice == 1 || choice == 2) ? 0 : (choice == 3 || choice == 4 || choice == 5) ? 1 : -1;
                    var veggieCard = market.buyVeggieCard(pileIndex, veggieIndex);
                    if(veggieCard == null) {
                        player.sendMessage("\nThis veggie is empty. Please choose another pile.\n");
                        validChoice = false;
                        break;
                    } else {
                        if(takenVeggies == 2) {
                            validChoice = true;
                            break;
                        } else {
                            player.addCardToHand(veggieCard);
                            takenVeggies++;
                            //thisPlayer.sendMessage("\nYou took a card from pile " + (pileIndex) + " and added it to your hand.\n");
                            validChoice = true;
                        }
                    }
                }
            }

        }

        //Check if the player has any criteria cards in their hand
        if(player.countCriteriaCardsInHand() > 0) {
            //Give the player an option to turn a criteria card into a veggie card
            player.sendMessage("\n"+player.getHandString()+"\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)");
            String choice = player.readMessage();
            if(choice.matches("\\d")) {
                int cardIndex = Integer.parseInt(choice);
                player.setCriteraSideDown(cardIndex);
            }
        }
        player.sendMessage("\nYour turn is completed\n****************************************************************\n\n");
        sendToAllPlayers("player " + player.getPlayerID() + "'s hand is now: \n"+player.getHandString() + "\n");

    }

    @Override
    public void executeNextStage() {
        if (!market.isAllPilesEmpty()){
            stateContext.setNextState(new VeggieNextPlayerState( stateContext, player));
        } else {
            throw new NotImplementedException("Game over state not implemented");
        }

    }

    //[TODO] Extract this to a another place
    private void sendToAllPlayers(String message){
        for(Participant player: participants){
            if(player instanceof Player){
                ((Player) player).sendMessage(message);
            }
        }
    }

    private void printInformation(){
        player.sendMessage("\n\n****************************************************************\nIt's your turn! Your hand is:\n");
        player.sendMessage(player.getHandString());
        player.sendMessage("\nThe piles are: ");

        player.sendMessage(market.getDisplayString());
    }
}
