package com.simonhelmle.hoelzle;

import java.util.Random;

public class Player {

    // Attributes of player

    private String playerName; // Name of player.
    private int hölzle; // Number of Hölzle each player gets at start of game. (Usually 3 Hölzle)
    private int hölzleInMiddle; // Number of Hölzle the player thinks are put into the middle in total by all
    // players.
    private int hölzleInHand; // Number of Hölzle a player puts in his hands each round. The min. is 0 and the
    // max is the current numbers of variable "hölzle".
    private int position; // Is the position of the player in the current round. Pos1 starts the round.
    private boolean hölzleInHandSet; // This bool is used to indicate if a player has already put Hölzle in hand for
    // the method determining a random Hölzle in Hand per player.
    private boolean playerIsHuman; // Indicates if the player is a human player (and therefore input is given via
    // GUI, or if player is a computer.
    private boolean hölzleInMiddleSet; // This bool is used to indicate if the player has already announced the
    // HölzleInMiddle value.
    private boolean activePlayer; // Indicates if player is still active or already has 0 Hölzle and thus is out
    // of game.

    // Konstruktor

    public Player() {

        // Give player object a random real name from simple array.

        String[] nameList = new String[] { "Udo", "Berta", "Hilde", "Manfred", "Christian Streich", "Adalbert",
                "Alt Lampert", "Maria", "Nora", "Friedegunde", "Willi", "Brunhilde", "Monika", "Kevin", "Schackeline", "Jon Snow", "Percy" , "Mosfet", "Kim", "Manny"};

        Random random = new Random();
        int randomPlayerName = random.nextInt(nameList.length);

        this.playerName = nameList[randomPlayerName] + " (KI)";

        // Initialize other player attributes.

        this.hölzle = 3;
        this.hölzleInMiddle = 999;
        this.hölzleInHand = 999;
        this.hölzleInHandSet = false;
        this.activePlayer = true;

    }

    // Further methods for manipulating player data.

    public static void resetPlayer() {

    }

    // Getters and Setters for player attributes.

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getHölzle() {
        return hölzle;
    }

    public void setHölzle(int hölzle) {
        this.hölzle = hölzle;
    }

    public int getHölzleInMiddle() {
        return hölzleInMiddle;
    }

    public void setHölzleInMiddle(int hölzleInMiddle) {
        this.hölzleInMiddle = hölzleInMiddle;
    }

    public int getHölzleInHand() {
        return hölzleInHand;
    }

    public void setHölzleInHand(int hölzleInHand) {
        this.hölzleInHand = hölzleInHand;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHölzleInHandSet() {
        return hölzleInHandSet;
    }

    public void setHölzleInHandSet(boolean hölzleInHandSet) {
        this.hölzleInHandSet = hölzleInHandSet;
    }

    public boolean isPlayerIsHuman() {
        return playerIsHuman;
    }

    public void setPlayerIsHuman(boolean playerIsHuman) {
        this.playerIsHuman = playerIsHuman;
    }

    public boolean isHölzleInMiddleSet() {
        return hölzleInMiddleSet;
    }

    public void setHölzleInMiddleSet(boolean hölzleInMiddleSet) {
        this.hölzleInMiddleSet = hölzleInMiddleSet;
    }

    public boolean isActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer = activePlayer;
    }

}