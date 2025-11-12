public class ThreePrisonersDilemma {

    /*
     * This Java program models the two-player Prisoner's Dilemma game.
     * We use the integer "0" to represent cooperation, and "1" to represent
     * defection.
     * 
     * Recall that in the 2-players dilemma, U(DC) > U(CC) > U(DD) > U(CD), where
     * we give the payoff for the first player in the list. We want the three-player
     * game
     * to resemble the 2-player game whenever one player's response is fixed, and we
     * also want symmetry, so U(CCD) = U(CDC) etc. This gives the unique ordering
     * 
     * U(DCC) > U(CCC) > U(DDC) > U(CDC) > U(DDD) > U(CDD)
     * 
     * The payoffs for player 1 are given by the following matrix:
     */

    static int[][][] payoff = {
            { { 6, 3 }, // payoffs when first and second players cooperate
                    { 3, 0 } }, // payoffs when first player coops, second defects
            { { 8, 5 }, // payoffs when first player defects, second coops
                    { 5, 2 } } };// payoffs when first and second players defect

    /*
     * So payoff[i][j][k] represents the payoff to player 1 when the first
     * player's action is i, the second player's action is j, and the
     * third player's action is k.
     * 
     * In this simulation, triples of players will play each other repeatedly in a
     * 'match'. A match consists of about 100 rounds, and your score from that match
     * is the average of the payoffs from each round of that match. For each round,
     * your
     * strategy is given a list of the previous plays (so you can remember what your
     * opponent did) and must compute the next action.
     */

    abstract class Player {
        // This procedure takes in the number of rounds elapsed so far (n), and
        // the previous plays in the match, and returns the appropriate action.
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            throw new RuntimeException("You need to override the selectAction method.");
        }

        // Used to extract the name of this player class.
        final String name() {
            String result = getClass().getName();
            return result.substring(result.indexOf('$') + 1);
        }
    }

    /* Here are four simple strategies: */

    class NicePlayer extends Player {
        // NicePlayer always cooperates
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return 0;
        }
    }

    class NastyPlayer extends Player {
        // NastyPlayer always defects
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return 1;
        }
    }

    class RandomPlayer extends Player {
        // RandomPlayer randomly picks his action each time
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (Math.random() < 0.5)
                return 0; // cooperates half the time
            else
                return 1; // defects half the time
        }
    }

    class TolerantPlayer extends Player {
        // TolerantPlayer looks at his opponents' histories, and only defects
        // if at least half of the other players' actions have been defects
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            int opponentCoop = 0;
            int opponentDefect = 0;
            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop = opponentCoop + 1;
                else
                    opponentDefect = opponentDefect + 1;
            }
            for (int i = 0; i < n; i++) {
                if (oppHistory2[i] == 0)
                    opponentCoop = opponentCoop + 1;
                else
                    opponentDefect = opponentDefect + 1;
            }
            if (opponentDefect > opponentCoop)
                return 1;
            else
                return 0;
        }
    }

    class FreakyPlayer extends Player {
        // FreakyPlayer determines, at the start of the match,
        // either to always be nice or always be nasty.
        // Note that this class has a non-trivial constructor.
        int action;

        FreakyPlayer() {
            if (Math.random() < 0.5)
                action = 0; // cooperates half the time
            else
                action = 1; // defects half the time
        }

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return action;
        }
    }

    class T4TPlayer extends Player {
        // Picks a random opponent at each play,
        // and uses the 'tit-for-tat' strategy against them
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // cooperate by default
            if (Math.random() < 0.5)
                return oppHistory1[n - 1];
            else
                return oppHistory2[n - 1];
        }
    }

    class Bhatia_Ritik_Player extends Player {

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // at the start of the game, cooperate to show that you are nice
            if (n == 0) {
                return 0;
            }

            // since the current round is the last round, opponents cannot retaliate
            // hence, be selfish and defect for maximum payoff
            else if (n == 109) {
                return 1;
            }

            // if both competitors did the same move in the previous round
            // follow the same to punish / show friendliness
            if (oppHistory1[n - 1] == oppHistory2[n - 1]) {
                return oppHistory1[n - 1];
            }

            // generate a random number to decide type of player
            double randomNum = Math.random();

            // for 30% of the times, be a tolerant player
            // decision is made by seeing number of cooperations and defections of the
            // opponents as a whole
            if (randomNum < 0.3) {

                // initialize the total number of cooperations and defections of the opponent
                int numOppCoops = 0;
                int numOppDefects = 0;

                // iterate through the histories of both the opponents
                for (int i = 0; i < n; i++) {
                    if (oppHistory1[i] == 0)
                        numOppCoops += 1;
                    else
                        numOppDefects += 1;

                    if (oppHistory2[i] == 0)
                        numOppCoops += 1;
                    else
                        numOppDefects += 1;
                }

                // if the opponents as whole have made more defections than cooperations, defect
                // else cooperate
                if (numOppDefects > numOppCoops)
                    return 1;
                return 0;
            }

            // iterate through your history for the rest 70% of the times
            else {

                // initialize variables to track number of defections
                int numOpp1Defects = 0;
                int numOpp2Defects = 0;
                int numMyDefects = 0;

                // iterate through all histories to get number of defections of each
                for (int i = 0; i < n; i++) {
                    numOpp1Defects += oppHistory1[i];
                    numOpp2Defects += oppHistory2[i];
                    numMyDefects += myHistory[i];
                }

                // if my agent has been defecting more than both, cooperate to break cycle
                if (numMyDefects > numOpp1Defects && numMyDefects > numOpp2Defects)
                    return 0;
                return 1;
            }
        }
    }

    class AlternatingT4TPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // cooperate by default

            if (n >= 109)
                return 1; // opponents cannot retaliate

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // n starts at 0, so compare history first

            if (n % 2 != 0) { // odd round - be tolerant
                // TolerantPlayer
                int opponentCoop = 0;
                int opponentDefect = 0;

                for (int i = 0; i < n; i++) {
                    if (oppHistory1[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;

                    if (oppHistory2[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;
                }

                return (opponentDefect > opponentCoop) ? 1 : 0;
            }
            // else: even round - compare history

            // HistoryPlayer
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    class TolerantT4TPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect > opponentCoop) ? 1 : 0;
        }
    }

    class AggresiveT4TPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect >= opponentCoop) ? 1 : 0;
        }
    }

    class LenientHistoryRectificationAndT4TPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // HistoryPlayer
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    class HistoryRectificationAndT4TPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // cooperate by default

            if (n >= 109)
                return 1; // opponents cannot retaliate

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            // n starts at 0, so compare history first
            if (n % 2 != 0) { // odd round - be tolerant
                // TolerantPlayer
                int opponentCoop = 0;
                int opponentDefect = 0;

                for (int i = 0; i < n; i++) {
                    if (oppHistory1[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;

                    if (oppHistory2[i] == 0)
                        opponentCoop += 1;
                    else
                        opponentDefect += 1;
                }

                return (opponentDefect > opponentCoop) ? 1 : 0;
            }
            // else: even round - compare history

            // HistoryPlayer
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    class TolerantSlyT4TPlayer extends Player {
        private int numRoundsThreshold = 10;

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            if (n >= numRoundsThreshold) {
                int iDefect = 0;
                int oppDefect1 = 0;
                int oppDefect2 = 0;

                for (int index = n - 1; index > n - 1 - numRoundsThreshold; --index) {
                    iDefect += myHistory[index];
                    oppDefect1 += oppHistory1[index];
                    oppDefect2 += oppHistory2[index];
                }

                if (iDefect == 0 && oppDefect1 == 0 && oppDefect2 == 0)
                    return 1; // take advantage
            }

            // Performance becomes worse when trying to punish others
            // for punish self, for ownself defecting when taking advantage.

            // if (oppHistory1[n-1] == oppHistory2[n-1])
            // return oppHistory1[n-1];

            // Use modified tit-for-tat instead.

            if (oppHistory1[n - 1] == 0 && oppHistory2[n - 1] == 0)
                return 0; // cooperate along

            if (oppHistory1[n - 1] == 1 && oppHistory2[n - 1] == 1 && myHistory[n - 1] != 1)
                return 1; // both defect while i cooperate

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect > opponentCoop) ? 1 : 0;
        }
    }

    class LenientHistoryRectificationPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return (myHistory[n - 1] == 0) ? 1 : 0;
        }
    }

    class AggressiveHistoryRectificationPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return myHistory[n - 1];
        }
    }

    class LenientT4TDefaultCoopPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return 0;
        }
    }

    class LenientT4TDefaultDefectPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // https://www.sciencedirect.com/science/article/abs/pii/S0096300316301011
            if (oppHistory1[n - 1] == oppHistory2[n - 1])
                return oppHistory1[n - 1];

            return 1;
        }
    }

    class HistoryRectificationPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            int myNumDefections = 0;
            int oppNumDefections1 = 0;
            int oppNumDefections2 = 0;

            for (int index = 0; index < n; ++index) {
                myNumDefections += myHistory[index];
                oppNumDefections1 += oppHistory1[index];
                oppNumDefections2 += oppHistory2[index];
            }

            if (myNumDefections >= oppNumDefections1 && myNumDefections >= oppNumDefections2)
                return 0;
            else
                return 1;
        }
    }

    class AggressivePlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            // TolerantPlayer
            int opponentCoop = 0;
            int opponentDefect = 0;

            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;

                if (oppHistory2[i] == 0)
                    opponentCoop += 1;
                else
                    opponentDefect += 1;
            }

            return (opponentDefect >= opponentCoop) ? 1 : 0;
        }
    }

    class LenientPlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // cooperate by default
            if (n == 0)
                return 0;

            return (myHistory[n - 1] == 0) ? 1 : 0;
        }
    }

    class ProbabilisticPlayer extends Player {
        // Helper function to calculate percentage of cooperation
        float calCoopPercentage(int[] history) {
            int cooperates = 0;
            int length = history.length;

            for (int i = 0; i < length; i++)
                if (history[i] == 0)
                    cooperates++;

            return (float) cooperates / length * 100;
        }

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0)
                return 0; // First round: Cooperate

            /* 1. Calculate percentage of cooperation */
            float perOpp1Coop = calCoopPercentage(oppHistory1);
            float perOpp2Coop = calCoopPercentage(oppHistory2);

            /* 2. If both players are mostly cooperating */
            if (perOpp1Coop > 90 && perOpp2Coop > 90) {
                int range = (10 - 5) + 1; // Max: 10, Min: 5
                int random = (int) (Math.random() * range) + 5;

                if (n > (90 + random)) // Selfish: Last min defect
                    return 1;
                else
                    return 0; // First ~90 rounds: Cooperate
            }

            /* 3. Defect by default */
            return 1;
        }
    }

    class DefectionRatePlayer extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            // First round - be nice, always cooperate.
            if (n == 0)
                return 0;

            // If any agent defected in the previous round,
            // punish immediately by defecting.
            if (oppHistory1[n - 1] == 1 || oppHistory2[n - 1] == 1)
                return 1;

            // Calculate the trustworthiness of each agent based on defection rate.
            // Trustworthy --> rate < 5%
            // Untrustworthy --> rate >= 20%
            // Neutral --> in between
            // Defection has a heavier emphasis than cooperation.
            // However, punishment should use "measured force", so there's a neutral range
            // set up.
            int defections1 = 0;
            int defections2 = 0;
            for (int action : oppHistory1)
                defections1 += action;
            for (int action : oppHistory2)
                defections2 += action;
            double defectRate1 = 1.0 * defections1 / n;
            double defectRate2 = 1.0 * defections2 / n;

            // If any agent is not trustworthy, defect.
            // Even if both cooperated in the previous round.
            if (defectRate1 >= 0.2 || defectRate2 >= 0.2)
                return 1;

            // If both agents are trustworthy, cooperate.
            if (defectRate1 < 0.05 && defectRate2 < 0.05)
                return 0;

            // If any agent is neutral, go back one more round and check for defection.

            // Second round, can't go back any further, so just cooperate.
            // Shouldn't reach this code though given the above conditions.
            if (n == 1)
                return 0;

            // If any of the neutral agents defected, punish by defecting.
            // Otherwise, cooperate.
            if (defectRate1 >= 0.05 && oppHistory1[n - 2] == 1)
                return 1;
            if (defectRate2 >= 0.05 && oppHistory2[n - 2] == 1)
                return 1;
            return 0;
        }
    }

    class Arora_Srishti_Player extends Player {
        int numOfCooperation1 = 0;
        int numOfCooperation2 = 0;
        int numOfDefects1 = 0;
        int numOfDefects2 = 0;
        int opp1Expectation = 0;
        int opp2Expectation = 0;

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n == 0) {
                return 0; // cooperate by default
            }
            if (n >= 109)
                return 1; // defect in last round

            // Determine the expected move and resulting score of both opponents
            for (int i = 0; i < n; i++) {
                if (oppHistory1[i] == 0)
                    numOfCooperation1 += 1;
                else
                    numOfDefects1 += 1;
                opp1Expectation += oppHistory1[i];

                if (oppHistory2[i] == 0)
                    numOfCooperation2 += 1;
                else
                    numOfDefects2 += 1;

                opp2Expectation += oppHistory2[i];
            }
            // expected move calculation of opponents
            opp1Expectation = Math.min(1, Math.round(opp1Expectation / n));
            opp2Expectation = Math.min(1, Math.round(opp2Expectation / n));

            // LAW 1: If all three players cooperated in the last move, keep up the
            // cooperation
            if (myHistory[n - 1] == 0 && oppHistory1[n - 1] == 0 && oppHistory2[n - 1] == 0)
                return 0;

            // LAW 2a: Beware of agents which always defect
            if (Math.random() > 0.5) {
                if (numOfDefects1 >= 0.8 * n)
                    return 1;
            } else {
                if (numOfDefects2 >= 0.8 * n)
                    return 1;
            }
            // LAW 2b: If you know they will mostly cooperate, play it for your benefit
            if (Math.random() > 0.5) {
                if (numOfCooperation1 >= 0.8 * n)
                    return 1;
            } else {
                if (numOfCooperation2 >= 0.8 * n)
                    return 1;
            }

            // // LAW 3: DEFECT IF THE TOTAL NUMBER OF COOPERATIONS ARE LESS THAN TOTAL
            // NUMBER
            // // OF DEFECTS
            if (numOfCooperation1 + numOfCooperation2 <= numOfDefects1 + numOfDefects2)
                return 1;

            // LAW 5: If either of your opponents is expected to defect, do tit for tat
            if (opp1Expectation == 1)
                return oppHistory1[n - 1];
            else if (opp2Expectation == 1)
                return oppHistory2[n - 1];

            // Defect by default
            return 1;

        }
    }

    float[] scoresOfMatch(Player A, Player B, Player C, int rounds) {
        int[] HistoryA = new int[0], HistoryB = new int[0], HistoryC = new int[0];
        float ScoreA = 0, ScoreB = 0, ScoreC = 0;

        for (int i = 0; i < rounds; i++) {
            int PlayA = A.selectAction(i, HistoryA, HistoryB, HistoryC);
            int PlayB = B.selectAction(i, HistoryB, HistoryC, HistoryA);
            int PlayC = C.selectAction(i, HistoryC, HistoryA, HistoryB);
            ScoreA = ScoreA + payoff[PlayA][PlayB][PlayC];
            ScoreB = ScoreB + payoff[PlayB][PlayC][PlayA];
            ScoreC = ScoreC + payoff[PlayC][PlayA][PlayB];
            HistoryA = extendIntArray(HistoryA, PlayA);
            HistoryB = extendIntArray(HistoryB, PlayB);
            HistoryC = extendIntArray(HistoryC, PlayC);
        }
        float[] result = { ScoreA / rounds, ScoreB / rounds, ScoreC / rounds };
        return result;
    }

    // This is a helper function needed by scoresOfMatch.
    int[] extendIntArray(int[] arr, int next) {
        int[] result = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        result[result.length - 1] = next;
        return result;
    }

    /*
     * The procedure makePlayer is used to reset each of the Players
     * (strategies) in between matches. When you add your own strategy,
     * you will need to add a new entry to makePlayer, and change numPlayers.
     */

    int numPlayers = 23;

    Player makePlayer(int which) {
        switch (which) {
            case 0:
                return new NicePlayer();
            case 1:
                return new NastyPlayer();
            case 2:
                return new RandomPlayer();
            case 3:
                return new TolerantPlayer();
            case 4:
                return new FreakyPlayer();
            case 5:
                return new T4TPlayer();
            case 6:
                return new Bhatia_Ritik_Player();
            case 7:
                return new AlternatingT4TPlayer();
            case 8:
                return new TolerantT4TPlayer();
            case 9:
                return new AggresiveT4TPlayer();
            case 10:
                return new LenientHistoryRectificationAndT4TPlayer();
            case 11:
                return new HistoryRectificationAndT4TPlayer();
            case 12:
                return new TolerantSlyT4TPlayer();
            case 13:
                return new LenientHistoryRectificationPlayer();
            case 14:
                return new AggressiveHistoryRectificationPlayer();
            case 15:
                return new LenientT4TDefaultCoopPlayer();
            case 16:
                return new LenientT4TDefaultDefectPlayer();
            case 17:
                return new HistoryRectificationPlayer();
            case 18:
                return new AggressivePlayer();
            case 19:
                return new LenientPlayer();
            case 20:
                return new ProbabilisticPlayer();
            case 21:
                return new DefectionRatePlayer();
            case 22:
                return new Arora_Srishti_Player();
        }
        throw new RuntimeException("Bad argument passed to makePlayer");
    }

    /* Finally, the remaining code actually runs the tournament. */

    public static void main(String[] args) {
        int[] ranks = new int[22];

        int numTournaments = 200;
        for (int i = 0; i < numTournaments; i++) {
            System.out.println("-------------------------------");
            System.out.println("Tournament: " + Integer.toString(i));
            System.out.println("-------------------------------");
            System.out.println();

            ThreePrisonersDilemma instance = new ThreePrisonersDilemma();
            int currentRank = instance.runTournament();
            ranks[currentRank]++;
        }

        System.out.println("-------------------------------");
        System.out.println("Results");
        System.out.println("-------------------------------");
        for (int i = 0; i < 22; i++) {
            System.out.println("Rank " + Integer.toString(i + 1) + " with probability: "
                    + Float.toString((ranks[i] / (float) numTournaments) * 100) + "%");
        }
    }

    boolean verbose = false; // set verbose = false if you get too much text output

    int runTournament() {
        float[] totalScore = new float[numPlayers];

        // This loop plays each triple of players against each other.
        // Note that we include duplicates: two copies of your strategy will play once
        // against each other strategy, and three copies of your strategy will play
        // once.

        for (int i = 0; i < numPlayers; i++)
            for (int j = i; j < numPlayers; j++)
                for (int k = j; k < numPlayers; k++) {

                    Player A = makePlayer(i); // Create a fresh copy of each player
                    Player B = makePlayer(j);
                    Player C = makePlayer(k);
                    int rounds = 90 + (int) Math.rint(20 * Math.random()); // Between 90 and 110 rounds
                    float[] matchResults = scoresOfMatch(A, B, C, rounds); // Run match
                    totalScore[i] = totalScore[i] + matchResults[0];
                    totalScore[j] = totalScore[j] + matchResults[1];
                    totalScore[k] = totalScore[k] + matchResults[2];
                    if (verbose)
                        System.out.println(A.name() + " scored " + matchResults[0] +
                                " points, " + B.name() + " scored " + matchResults[1] +
                                " points, and " + C.name() + " scored " + matchResults[2] + " points.");
                }
        int[] sortedOrder = new int[numPlayers];
        // This loop sorts the players by their score.
        for (int i = 0; i < numPlayers; i++) {
            int j = i - 1;
            for (; j >= 0; j--) {
                if (totalScore[i] > totalScore[sortedOrder[j]])
                    sortedOrder[j + 1] = sortedOrder[j];
                else
                    break;
            }
            sortedOrder[j + 1] = i;
        }

        int ritikRank = 0;

        // Finally, print out the sorted results.
        if (verbose)
            System.out.println();
        System.out.println("Tournament Results");
        for (int i = 0; i < numPlayers; i++) {
            if (makePlayer(sortedOrder[i]).name().equals("Arora_Srishti_Player")) {
                ritikRank = i;
            }
            System.out.println(makePlayer(sortedOrder[i]).name() + ": "
                    + totalScore[sortedOrder[i]] + " points.");
        }

        return ritikRank;
    } // end of runTournament()

}
// end of class PrisonersDilemma
