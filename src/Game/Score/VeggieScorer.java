package Game.Score;

import Card.ICard;
import Card.Vegetable;
import Player.Participant;

import java.util.ArrayList;

public class VeggieScorer implements IScorer {


    public int calculateScore(ArrayList<Participant> participants, Participant currentParticipant, ArrayList<ICard> hand) {
        int score = 0;
        for (ICard card : hand) {
            if (card.isCriteriaSideUp()) {
                String criteria = card.getCriteria();
                String[] criteriaParts = criteria.split(",");

                //ID 18
                if (criteria.contains("TOTAL") || criteria.contains("TYPE") || criteria.contains("SET")) {
                    if (criteria.contains("TOTAL")) {
                        score += hanleTotalCriteria(criteria, participants, currentParticipant, hand);
                    }
                    if (criteria.contains("TYPE")) {
                        score += handleTypeCriteria(criteria, hand);
                    }
                    if (criteria.contains("SET")) {
                        score += handleSetCriteria(criteria, hand);
                    }
                    //ID 1 and ID 2
                } else if (criteria.contains("MOST") || criteria.contains("FEWEST")) {
                    score += handleMostFewestCriteria(criteria, participants, currentParticipant, hand);
                } else if (criteriaParts.length > 1 || criteria.contains("+") || criteriaParts[0].contains("/")) {
                    score += handleOtherCriteria(criteriaParts, currentParticipant);
                }
            }
        }
        return score;
    }


    /**
     * Handles criteria that contains "MOST" or "FEWEST" in it by checking if the current participant meets the criteria of
     * having the most or least face cards of a certain face type.
     *
     * @param criteria           criteria to be evaluated
     * @param participants       list of all participants
     * @param currentParticipant the current participant
     * @param hand               the current participant's hand to be evaluated
     * @return Number of points according to the criteria or 0 if the current participant does not meet the criteria
     */
    private int handleMostFewestCriteria(String criteria, ArrayList<Participant> participants, Participant currentParticipant, ArrayList<ICard> hand) {
        int vegIndex = criteria.contains("MOST") ? criteria.indexOf("MOST") + 5 : criteria.indexOf("FEWEST") + 7;
        String veg = criteria.substring(vegIndex, criteria.indexOf("=")).trim();
        int currentParticipantsVegCount = Participant.countFaceCards(Vegetable.valueOf(veg), hand);

        for (Participant participant : participants) {
            if (participant.getPlayerID() != currentParticipant.getPlayerID()) {
                int participantVegCount = participant.countFaceCardsInHand(Vegetable.valueOf(veg));
                if (criteria.contains("MOST") && participantVegCount > currentParticipantsVegCount) {
                    return 0;
                } else if (criteria.contains("FEWEST") && participantVegCount < currentParticipantsVegCount) {
                    return 0;
                }
            }
        }
        return Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());
    }

    /**
     * Handles criteria that contains "SET" in it by checking if the current participant meets the criteria of
     * having at least one face card of each vegetable type.
     *
     * @param criteria criteria to be evaluated
     * @param hand     the current participant's hand to be evaluated
     * @return Number of points according to the criteria
     */

    private int handleSetCriteria(String criteria, ArrayList<ICard> hand) {
        int addScore = 12;
        for (Vegetable veg : Vegetable.values()) {
            if (Participant.countFaceCards(veg, hand) == 0) {
                addScore = 0;
                break;
            }
        }
        return addScore;
    }

    /**
     * Handles criteria that contains "TYPE" in it by checking if the current participant meets the criteria of
     * having at least a certain number of face cards of each vegetable type or if the current participant has any
     * missing face cards of any vegetable type.
     *
     * @param criteria criteria to be evaluated
     * @param hand     the current participant's hand to be evaluated
     * @return Number of points according to the criteria
     */
    private int handleTypeCriteria(String criteria, ArrayList<ICard> hand) {
        String[] expr = criteria.split("/");
        int addScore = Integer.parseInt(expr[0].trim());
        if (expr[1].contains("MISSING")) {
            int missing = 0;
            for (Vegetable veg : Vegetable.values()) {
                if (Participant.countFaceCards(veg, hand) <= 0) {
                    missing++;
                }
            }
            return missing * addScore;
        } else {
            int atLeastPerVegType = Integer.parseInt(expr[1].substring(expr[1].indexOf(">=") + 2).trim());
            int totalType = 0;
            for (Vegetable veg : Vegetable.values()) {
                if (Participant.countFaceCards(veg, hand) >= atLeastPerVegType) {
                    totalType++;
                }
            }
            return totalType * addScore;
        }
    }

    /**
     * Handles criteria that contains "TOTAL" in it by checking if the current participant meets the criteria of
     * having the most or least face cards in hand.
     *
     * @param criteria           criteria to be evaluated
     * @param participants       list of all participants
     * @param currentParticipant the current participant
     * @return Number of points according to the criteria or 0 if the current participant does not meet the criteria
     */
    private static int hanleTotalCriteria(String criteria, ArrayList<Participant> participants, Participant currentParticipant, ArrayList<ICard> hand) {
        int currentParticipantFaceCards = Participant.countFaceCards(hand);


        for (Participant participant : participants) {
            if (participant.getPlayerID() != currentParticipant.getPlayerID()) {
                int participantFaceCards = participant.countFaceCardsInHand();
                if (criteria.contains("MOST")) {
                    if (participantFaceCards > currentParticipantFaceCards) {
                        return 0;
                    } else {
                        break;
                    }
                } else if (criteria.contains("LEAST")) {
                    if (participantFaceCards < currentParticipantFaceCards) {
                        return 0;
                    } else {
                        break;
                    }
                }
            }
        }
        return Integer.parseInt(criteria.substring(criteria.indexOf("=") + 1).trim());

    }
}
