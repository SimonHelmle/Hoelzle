package com.simonhelmle.hoelzle;

import java.util.*;

public class Model {

    // ATTRIBUTES
    // Main data storage as Map of all player information as instances of Player()

    public static Map<String, Player> playerMap = new HashMap<String, Player>();
    private static int round;

    // Getters and Setters

    public static int getRound() {
        return round;
    }

    public static void setRound(int round) {
        Model.round = round;
    }

    // METHODs

    public static int determineNumberOfPlayers() {

        int numberPlayers = 4; // Currently there are 4 players fix.
        return numberPlayers;
    }

    public static int determineStartPlayer(int numberPlayers) {

        Random random = new Random();

        int startPlayer = 1 + random.nextInt(numberPlayers);
        return startPlayer;

    }

    public static void createPlayers(int numberPlayers) {

        for (int i = 0; i < numberPlayers; i++) {

            String playerKey = "player" + (i + 1);

            Model.playerMap.put(playerKey, new Player());

        }

        // For human player change name and indicate player as human.

        Model.playerMap.get("player1").setPlayerName("Du");
        Model.playerMap.get("player1").setPlayerIsHuman(true);

    }

    public static int getTotalHölzleInGame() {

        int totalHölzleInGame = 0;

        for (String key : Model.playerMap.keySet()) {

            int keyValue = Model.playerMap.get(key).getHölzle();
            totalHölzleInGame += keyValue;
        }
        return totalHölzleInGame;
    }

    public static double getAverageHölzleInGame(int totalHölzleInGame, int numberOfActivePlayers) {

        double averageHölzleInGame = totalHölzleInGame / 2;
        return averageHölzleInGame;
    }

    public static void setTablePositions(int startPlayer) {

        switch (startPlayer) {

            case 1: {
                Model.playerMap.get("player1").setPosition(1);
                Model.playerMap.get("player2").setPosition(2);
                Model.playerMap.get("player3").setPosition(3);
                Model.playerMap.get("player4").setPosition(4);
            }
            break;
            case 2: {
                Model.playerMap.get("player1").setPosition(4);
                Model.playerMap.get("player2").setPosition(1);
                Model.playerMap.get("player3").setPosition(2);
                Model.playerMap.get("player4").setPosition(3);
            }
            break;
            case 3: {
                Model.playerMap.get("player1").setPosition(3);
                Model.playerMap.get("player2").setPosition(4);
                Model.playerMap.get("player3").setPosition(1);
                Model.playerMap.get("player4").setPosition(2);
            }
            break;
            case 4: {
                Model.playerMap.get("player1").setPosition(2);
                Model.playerMap.get("player2").setPosition(3);
                Model.playerMap.get("player3").setPosition(4);
                Model.playerMap.get("player4").setPosition(1);
            }
            break;
        }

    }

    public static void setKIhölzleInHand() {

        // This method randomly assigns HölzleInHand to all computer players.

        Random random = new Random();

        for (String key : Model.playerMap.keySet()) {

            int hölzle = Model.playerMap.get(key).getHölzle();
            boolean hölzleInHandSet = Model.playerMap.get(key).isHölzleInHandSet();
            boolean playerIsHuman = Model.playerMap.get(key).isPlayerIsHuman();

            if (playerIsHuman == false && hölzleInHandSet == false) {

                Model.playerMap.get(key).setHölzleInHand(random.nextInt(hölzle + 1)); // In random.nextInt the
                // int-boundary is
                // EXclusive. Therefore +1 needs to be
                // added.
                Model.playerMap.get(key).setHölzleInHandSet(true);

            }
            if (playerMap.get(key).getPosition() == 4) {

                // Here in 80% of all cases the last player will take either all or no Hölzle.

                boolean decision = randomDecision(80);

                if (decision) {

                    boolean minOrMax = random.nextBoolean();
                    if (minOrMax) {

                        // minOrMax = true bedeutet, es wid der Maximalwert genommen. = false bedeutet
                        // der Minimalwert.

                        playerMap.get(key).setHölzleInHand(playerMap.get(key).getHölzle());

                    } else {
                        playerMap.get(key).setHölzleInHand(0);
                    }
                }

            }

        }
    }

    public static String readStartPlayer() {

        // This method returns the current startPlayer. It reads which player is on
        // position 1 and returns the player number.

        String startPlayerKey = "initial";
        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).getPosition() == 1) {

                startPlayerKey = key;
            }
        }
        return startPlayerKey;
    }

    public static boolean processHölzleInMiddle() {

        boolean enableUserInput = false;
        int iteration = 1;

        do {

            for (String key : playerMap.keySet()) {

                boolean playerIsHuman = playerMap.get(key).isPlayerIsHuman();
                int position = playerMap.get(key).getPosition();
                boolean hölzleInMiddleSet = Model.playerMap.get(key).isHölzleInMiddleSet();

                // If the player on the position is CPU and hasnt set HölzleInMiddle already.

                if (position == iteration && playerIsHuman == false && hölzleInMiddleSet == false) {

                    // Here comes the logic of how to set HölzleInMiddle anouncement for CPU player.

                    Model.calculateOptimalHölzleInMiddle(key);

                } else {
                    if (position == iteration && playerIsHuman == true && hölzleInMiddleSet == false) {

                        enableUserInput = true;
                        return enableUserInput;
                    }
                }
            }
            iteration++;
        } while (checkHölzleInMiddleUnset());

        return enableUserInput;
    }

    public static boolean checkHölzleInMiddleUnset() {

        // This method checks if for any player the HölzleInMiddle is not set yet.

        boolean hölzleInMiddleUnset = false;
        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).isHölzleInMiddleSet() == false) {

                hölzleInMiddleUnset = true;
                return hölzleInMiddleUnset;
            }
        }
        return hölzleInMiddleUnset;

    }

    public static boolean checkIfStartPlayerIsHuman(Map<String, Player> playerMap, int startPlayer) {

        boolean startPlayerIsHuman;

        startPlayerIsHuman = startPlayer == 1;
        return startPlayerIsHuman;
    }

    public static void calculateOptimalHölzleInMiddle(String playerMapKey) {

        Random random = new Random();
        int inDekrementor = 0; // This variable is used to in or decrease the HölzleInMiddle number from
        // average.

        // Retrieve current average Hölzle in game.

        double averageHölzleInGame = Model.getAverageHölzleInGame(getTotalHölzleInGame(), getActivePlayers());

        // Try to set HölzleInMiddle value to average.

        do {

            int hölzleInMiddleAnnouncement = (int) (Math.round(averageHölzleInGame) + inDekrementor);

            for (String key : Model.playerMap.keySet()) {

                int hölzleInMiddleValue = playerMap.get(key).getHölzleInMiddle();

                if (!playerMap.get(playerMapKey).equals(playerMap.get(key))) {

                    if (hölzleInMiddleAnnouncement != hölzleInMiddleValue) {

                        playerMap.get(playerMapKey).setHölzleInMiddle(hölzleInMiddleAnnouncement);
                        playerMap.get(playerMapKey).setHölzleInMiddleSet(true);
                    } else {
                        playerMap.get(playerMapKey).setHölzleInMiddle(999);
                        playerMap.get(playerMapKey).setHölzleInMiddleSet(false);
                        break;
                    }
                }
            }

            // TODO: Here a further random method could be included, so that there is more
            // variation in CPU choice.

            if ((double) playerMap.get(playerMapKey)
                    .getHölzleInHand() < (double) ((playerMap.get(playerMapKey).getHölzleInHand() / 2))) {
                inDekrementor--;
            } else {
                if ((double) playerMap.get(playerMapKey)
                        .getHölzleInHand() > (double) ((playerMap.get(playerMapKey).getHölzleInHand() / 2))) {
                    inDekrementor++;
                } else {

                    boolean decision = random.nextBoolean();

                    if (decision) {
                        inDekrementor++;
                    } else {
                        inDekrementor--;
                    }
                }
            }
        } while (!playerMap.get(playerMapKey).isHölzleInMiddleSet());
    }

    public static int getActivePlayers() {

        int numberActivePlayers = 0;

        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).isActivePlayer() == true) {

                numberActivePlayers++;
            }
        }
        return numberActivePlayers;
    }

    public static boolean checkIfRoundWinner() {

        // This method evaluates if there is a winner in the current round.

        boolean isWinnerThisRound = false;
        int totalHölzleInHandThisRound = totalHölzleInHandThisRound();

        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).getHölzleInMiddle() == totalHölzleInHandThisRound) {

                isWinnerThisRound = true;
                break;
            } else {
                isWinnerThisRound = false;
            }
        }
        return isWinnerThisRound;
    }

    public static String evaluateRoundWinner() {

        // This method returns the player key of Map of the round winner.

        String roundWinner = "null";
        int totalHölzleInHandThisRound = totalHölzleInHandThisRound();

        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).getHölzleInMiddle() == totalHölzleInHandThisRound) {

                roundWinner = key;
                break;
            } else {
                roundWinner = "null";
            }
        }

        return roundWinner;
    }

    public static int totalHölzleInHandThisRound() {

        int totalHölzleInHandThisRound = 0;

        for (String key : Model.playerMap.keySet()) {

            totalHölzleInHandThisRound += playerMap.get(key).getHölzleInHand();
        }

        return totalHölzleInHandThisRound;
    }

    public static void reduceTotalPlayerHölzle(String roundWinner) {

        int currentHölzleWinner = Model.playerMap.get(roundWinner).getHölzle();
        currentHölzleWinner--;
        Model.playerMap.get(roundWinner).setHölzle(currentHölzleWinner);

    }

    public static int assignNewStartPlayer(String previousStartPlayer) {

        int newStartPlayer = 0;

        switch (previousStartPlayer) {

            case "player1": {
                newStartPlayer = 2;
                break;
            }
            case "player2": {
                newStartPlayer = 3;
                break;
            }
            case "player3": {
                newStartPlayer = 4;
                break;
            }
            case "player4": {
                newStartPlayer = 1;
                break;
            }

        }
        return newStartPlayer;
    }

    public static boolean checkIfMatchWinner() {

        // This method evaluates if there is a winner of the overall game.

        boolean isWinnerMatch = false;

        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).getHölzle() == 0) {

                isWinnerMatch = true;
                break;
            } else {
                isWinnerMatch = false;
            }
        }
        return isWinnerMatch;
    }

    public static String evaluateMatchWinner() {

        // This method returns the player key of Map of the match winner.

        String matchWinner = "null";

        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).getHölzle() == 0) {

                matchWinner = key;
                break;
            } else {
                matchWinner = "null";
            }
        }

        return matchWinner;

    }

    public static void resetPlayerHölzleValues() {

        for (String key : Model.playerMap.keySet()) {

            playerMap.get(key).setHölzleInHand(999);
            playerMap.get(key).setHölzleInHandSet(false);

            playerMap.get(key).setHölzleInMiddle(999);
            playerMap.get(key).setHölzleInMiddleSet(false);
        }
    }

    public static boolean randomDecision(int percentage) {

        // This method provides a random generator whereas the parameter percentage
        // (between 0 and 100) is the probability in how many cases the decision becomes
        // TRUE.

        boolean decision = false;

        Random random = new Random();

        int decisionNumber = random.nextInt(101);

        decision = decisionNumber < percentage;

        return decision;
    }

    public static boolean checkUserInputHölzleInMiddleAvailability(int userInputValue) {

        // This method checks if the value which the user has entered for Hölzle in middle is still available (not taken by KI players already).

        boolean valueAvailability = false;

        for (String key : Model.playerMap.keySet()) {

            if (userInputValue == playerMap.get(key).getHölzleInMiddle()) {
                valueAvailability = false;
                break;
            } else {
                valueAvailability = true;
            }

        }

        return valueAvailability;
    }

}

