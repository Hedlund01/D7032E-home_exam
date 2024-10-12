package game.score;

import card.ICard;
import card.Vegetable;
import player.Participant;

import java.util.ArrayList;

public class VeggieScorer implements IScorer {


    public int calculateScore(ArrayList<Participant> participants, Participant currentParticipant, ArrayList<ICard> hand) {
        int score = 0;
        for (ICard card : hand) {
            if (card.isCriteriaSideUp()) {
                String criteria = card.getCriteria();
                String[] criteriaParts = criteria.split(",");

                if (criteria.contains("TOTAL") || criteria.contains("TYPE") || criteria.contains("SET")) {
                    //ID 18
                    if (criteria.contains("TOTAL")) {
                        score += hanleTotalCriteria(criteria, participants, currentParticipant, hand);
                    }
                    if (criteria.contains("TYPE")) {
                        score += handleTypeCriteria(criteria, hand);
                    }
                    if (criteria.contains("SET")) {
                        score += handleSetCriteria(hand);
                    }

                } else if (criteria.contains("MOST") || criteria.contains("FEWEST")) {
                    //ID 1 and ID 2
                    score += handleMostFewestCriteria(criteria, participants, currentParticipant, hand);
                } else if (criteriaParts.length > 1 || criteria.contains("+") || criteriaParts[0].contains("/")) {
                    //ID3, ID4, ID5, ID6, ID7, ID8, ID9, ID10, ID11, ID12, ID13, ID14, ID15, ID16, ID17

                    if (criteria.contains("+")) { //ID5, ID6, ID7, ID11, ID12, ID13
                        score+= handleAdditionCriteria(criteria, currentParticipant.getHand());
                    } else if (criteriaParts[0].contains("=")) { //ID3, ID4, ID8, ID9, ID10, ID14, ID15, ID16, ID17
                        score += handleEqualCriteria(criteriaParts, hand);
                    } else {
                        score += handleDivisionCriteria(criteriaParts, hand);

                    }
                }
            }
        }
        return score;
    }



    /**
     * Handles criteria that contains "/" in it by multiplying the number of face cards the hand has with
     * the face type specified in the criteria.
     * @param criteriaParts criteria to be evaluated
     * @param hand hand to be evaluated
     * @return Number of points according to the criteria
     */

    private int handleDivisionCriteria(String[] criteriaParts, ArrayList<ICard> hand) {
        int totalScore = 0;
        for (String criteriaPart : criteriaParts) {
            String[] veg = criteriaPart.split("/");
            totalScore += Integer.parseInt(veg[0].trim()) * Participant.countFaceCards(Vegetable.valueOf(veg[1].trim()), hand);
        }
        return totalScore;
    }


    /**
     * Handles criteria that contains "=" in it by checking if the current participant meets the criteria of
     * having an even or odd number of face cards of a certain face type and returns the points accordingly.
     * @param criteriaParts criteria to be evaluated
     * @param hand hand to be evaluated
     * @return Number of points according to the criteria
     */
    private int handleEqualCriteria( String[] criteriaParts, ArrayList<ICard> hand) {
        String veg = criteriaParts[0].substring(0, criteriaParts[0].indexOf(":"));
        int countVeg = Participant.countFaceCards(Vegetable.valueOf(veg), hand);
        //System.out.print("ID3: "+((countVeg%2==0)?7:3) + " ");
        return (countVeg % 2 == 0) ? 7 : 3;
    }



/**
 * Handles criteria that contains "+" in it by checking if the current participant meets the criteria of
 * having a certain combination of face cards of different vegetable types and returns the points accordingly.
 *
 * @param criteria The criteria to be evaluated, in the format "veg1+veg2+...=points".
 * @param hand The current participant's hand to be evaluated.
 * @return Number of points according to the criteria.
 */
private int handleAdditionCriteria(String criteria, ArrayList<ICard> hand) {
    String expr = criteria.split("=")[0].trim();
    String[] vegs = expr.split("\\+");
    int[] vegCount = new int[vegs.length];
    int countSameKind = 1;

    // Count how many times the first vegetable type appears in the criteria
    for (int j = 1; j < vegs.length; j++) {
        if (vegs[0].trim().equals(vegs[j].trim())) {
            countSameKind++;
        }
    }

    // If the same vegetable type appears more than once, calculate the score based on the count
    if (countSameKind > 1) {
        return (Participant.countFaceCards(Vegetable.valueOf(vegs[0].trim()), hand) / countSameKind) * Integer.parseInt(criteria.split("=")[1].trim());
    } else {
        // Count the number of face cards for each vegetable type in the criteria
        for (int i = 0; i < vegs.length; i++) {
            vegCount[i] = Participant.countFaceCards(Vegetable.valueOf(vegs[i].trim()), hand);
        }

        // Find the lowest number of face cards among the vegetable types
        int min = vegCount[0];
        for (int x = 1; x < vegCount.length; x++) {
            if (vegCount[x] < min) {
                min = vegCount[x];
            }
        }

        // Calculate the score based on the lowest number of face cards and the points specified in the criteria
        return min * Integer.parseInt(criteria.split("=")[1].trim());
    }
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
     * @param hand the current participant's hand to be evaluated
     * @return Number of points according to the criteria
     */

    private int handleSetCriteria(ArrayList<ICard> hand) {
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
