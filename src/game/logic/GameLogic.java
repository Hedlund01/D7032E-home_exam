package game.logic;

import game.state.common.StateContext;
import game.state.veggie.VeggieInitState;
import player.Participant;

import java.util.ArrayList;

public class GameLogic {

    private ArrayList<Participant> participants;
    public StateContext stateContext;

    public GameLogic(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public void runGame() {
        stateContext = new StateContext(participants);
        stateContext.setNextState(new VeggieInitState(stateContext));

        stateContext.executeNextState();




    }

}
