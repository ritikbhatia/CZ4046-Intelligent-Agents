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