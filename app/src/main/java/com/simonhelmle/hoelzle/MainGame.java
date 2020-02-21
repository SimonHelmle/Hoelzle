package com.simonhelmle.hoelzle;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.simonhelmle.hoelzle.databinding.ActivityGameBinding;

public class MainGame extends AppCompatActivity {

    private ActivityGameBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Create view binding for GUI access.
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Start game.

        initializeNewGame();

    }
    // START OF ACTUAL CONTROLLER

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeNewGame() {

        // Clear all elements of main runtime data storage.
        Model.playerMap.clear();

        // Number of players are determined.

        int numberPlayers = Model.determineNumberOfPlayers(); // Actually not used for MVP with 4 players.

        // Create player objects and write them into Map according to number of players.

        Model.createPlayers(numberPlayers);

        // Set GUI elements in initial status.

        binding.viewP1.setBackgroundColor(Color.parseColor("#19B388FF"));
        binding.viewP2.setBackgroundColor(Color.parseColor("#19B388FF"));
        binding.viewP3.setBackgroundColor(Color.parseColor("#19B388FF"));
        binding.viewP4.setBackgroundColor(Color.parseColor("#19B388FF"));

        binding.p1hoelzle.setText("3");
        binding.p2hoelzle.setText("3");
        binding.p3hoelzle.setText("3");
        binding.p4hoelzle.setText("3");

        binding.p1hoelzleInMiddle.setText("");
        binding.p2hoelzleInMiddle.setText("");
        binding.p3hoelzleInMiddle.setText("");
        binding.p4hoelzleInMiddle.setText("");

        binding.p1hoelzleInHand.setText("");
        binding.p2hoelzleInHand.setText("");
        binding.p3hoelzleInHand.setText("");
        binding.p4hoelzleInHand.setText("");

        binding.gameButton.setVisibility(View.INVISIBLE);

        binding.startButtonP1.setVisibility(View.INVISIBLE);
        binding.startButtonP2.setVisibility(View.INVISIBLE);
        binding.startButtonP3.setVisibility(View.INVISIBLE);
        binding.startButtonP4.setVisibility(View.INVISIBLE);

        //TODO: Add initial color for buttons. Check color change code.

        binding.hoelzleInMiddleSliderProgress.setVisibility(View.INVISIBLE);
        binding.hoelzleInHandSliderProgress.setVisibility(View.INVISIBLE);

        // Start player is randomly determined.

        int startPlayer = Model.determineStartPlayer(numberPlayers);

        switch (startPlayer) {

            case 1: {
                binding.startButtonP1.setVisibility(View.VISIBLE);
                break;
            }
            case 2: {
                binding.startButtonP2.setVisibility(View.VISIBLE);
                break;
            }
            case 3: {
                binding.startButtonP3.setVisibility(View.VISIBLE);
                break;
            }
            case 4: {
                binding.startButtonP4.setVisibility(View.VISIBLE);
                break;
            }
        }

        String startPlayerID = "player" + startPlayer;
        binding.middleCenterText.setText(Model.playerMap.get(startPlayerID).getPlayerName() + " (b)ist Startspieler.");

        // Player position are written into Player classes based on start player.

        Model.setTablePositions(startPlayer);

        // Deactivate sliders and confirmation buttons for human player input as long as
        // not required.

        // TODO: Here separate buttons could be indluded to confirm slider values.

        binding.sliderHoelzleInHand.setEnabled(false);
        binding.sliderHoelzleInMiddle.setEnabled(false);

        // Put all player names on GUI.


        binding.player1name.setText(Model.playerMap.get("player1").getPlayerName());
        binding.player2name.setText(Model.playerMap.get("player2").getPlayerName());
        binding.player3name.setText(Model.playerMap.get("player3").getPlayerName());
        binding.player4name.setText(Model.playerMap.get("player4").getPlayerName());

        // Initialize and update further buttons and displays and indicate start round.

        Model.setRound(1);
        binding.topCenterText.setText("Runde " + Model.getRound());
        binding.bottomCenterText.setVisibility(View.INVISIBLE);
        binding.gameButton.setVisibility(View.INVISIBLE);

        // Set sliders to initial values.

        binding.sliderHoelzleInHand.setMin(0);
        binding.sliderHoelzleInHand.setMax(Model.playerMap.get("player1").getHölzle());

        binding.sliderHoelzleInMiddle.setMin(0);
        binding.sliderHoelzleInMiddle.setMax(Model.getTotalHölzleInGame());

        // Assign randomly the number of HölzleInHand per player.

        Model.setKIhölzleInHand();

        // GUI change to indicate that KI players have taken Hölzle in hand but values
        // are hidden on display.

        binding.p2hoelzleInHand.setText("X");
        binding.p3hoelzleInHand.setText("X");
        binding.p4hoelzleInHand.setText("X");

        // Activate input elements for HölzleInHand.

        binding.bottomCenterText.setText("Verstecke deine Hölzle!");
        binding.bottomCenterText.setVisibility(View.VISIBLE);
        binding.sliderHoelzleInHand.setEnabled(true);

        binding.sliderHoelzleInHand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                binding.hoelzleInHandSliderProgress.setText(String.valueOf(progress));
                binding.hoelzleInHandSliderProgress.setX(seekBar.getX() + seekBar.getThumbOffset() / 2);
                binding.gameButton.setText(progress + " Hölzle\nverstecken!");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                binding.hoelzleInHandSliderProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.hoelzleInHandSliderProgress.setVisibility(View.INVISIBLE);

            }
        });

        binding.gameButton.setText("Verstecken!");
        binding.gameButton.setVisibility(View.VISIBLE);
        binding.gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHumanHölzleInHand();
            }
        });

        // End new game initiation and start first round with event for first user
        // input. After this method the process waits for the user to input Hölzle in
        // hand.
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setHumanHölzleInHand() {

        // This method is called when the user submits the selected Hölzle in hand.

        // Set center button insible.

        binding.gameButton.setVisibility(View.INVISIBLE);

        // Catch current slider value.
        Model.playerMap.get("player1").setHölzleInHand(binding.sliderHoelzleInHand.getProgress());

        // Write slider value to user display.
        binding.p1hoelzleInHand.setText(Integer.toString(Model.playerMap.get("player1").getHölzleInHand()));

        // Update center display announcement.
        binding.middleCenterText.setText("");
        binding.bottomCenterText.setText("Alle Hölzle wurden versteckt.");

        // Deactivate HölzleInHand input elements.
        binding.sliderHoelzleInHand.setEnabled(false);
        binding.gameButton.setVisibility(View.INVISIBLE);

        // Retrieve start player from playerMap and display start player for
        // HölzleInMiddle.
        String startPlayerKey = Model.readStartPlayer();
        binding.middleCenterText.setText(Model.playerMap.get(startPlayerKey).getPlayerName() + " beginnt.");

        // Call method to process the choice of Hölzle in middle for all players.
        Model.processHölzleInMiddle();

        // Display the so far processed KI player Hölzle in middle announcements.
        displayHölzleInMiddle();

        // End initial round of KI players selecting Hölzle in middle and open user
        // inputs.
        enableUserInputHölzleInMiddle();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enableUserInputHölzleInMiddle() {

        // This method is called after initial Hölzle in middle values have been
        // selected by KI players.

        // Recalculate the allowed slider values for Hölzle in middle. (Max is the
        // currently total Hölzle in Game)
        binding.sliderHoelzleInMiddle.setMin(0);
        binding.sliderHoelzleInMiddle.setMax(Model.getTotalHölzleInGame());

        // Activate input elements and display player turn info.
        binding.bottomCenterText.setVisibility(View.VISIBLE);
        binding.middleCenterText.setText("Du bist dran!");

        binding.bottomCenterText.setVisibility(View.VISIBLE);
        binding.bottomCenterText.setText("Sag die Anzahl an Hölzle in der Mitte an!");

        binding.sliderHoelzleInMiddle.setEnabled(true);
        binding.gameButton.setText("Ansagen!");
        binding.gameButton.setVisibility(View.VISIBLE);

        // Change listener for the slider indicator and button value changer.

        binding.sliderHoelzleInMiddle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                binding.hoelzleInMiddleSliderProgress.setText(String.valueOf(progress));
                binding.hoelzleInMiddleSliderProgress.setX(seekBar.getX() + seekBar.getThumbOffset() / 2);
                binding.gameButton.setText(progress + " Hölzle\nansagen!");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                binding.hoelzleInMiddleSliderProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                binding.hoelzleInMiddleSliderProgress.setVisibility(View.INVISIBLE);

            }
        });

        // Further processing on click.

        binding.gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterUserHölzleInMiddle();
            }
        });

        // Further waiting for the user to input Hölzle in middle value.
    }

    public void enterUserHölzleInMiddle() {

        // This method is called as soon as the user inputs the Hölzle in middle value.

        // The user input is validated against already announced Hölzle in middle
        // values.
        boolean valueAvailability = Model.checkUserInputHölzleInMiddleAvailability(binding.sliderHoelzleInMiddle.getProgress());

        if (valueAvailability == false) {
            binding.bottomCenterText.setText("Zahl bereits angesagt!\n\nAndere Zahl wählen!");
            binding.bottomCenterText.setTextColor(Color.RED);
            return;
        }

        // Reset color to black.

        binding.bottomCenterText.setTextColor(Color.DKGRAY);

        Model.playerMap.get("player1").setHölzleInMiddle(binding.sliderHoelzleInMiddle.getProgress());
        Model.playerMap.get("player1").setHölzleInMiddleSet(true);

        String s = Integer.toString(binding.sliderHoelzleInMiddle.getProgress());
        binding.p1hoelzleInMiddle.setText(s);

        binding.sliderHoelzleInMiddle.setEnabled(false);
        binding.gameButton.setVisibility(View.INVISIBLE);

        Model.processHölzleInMiddle();
        displayHölzleInMiddle();
        enableUserInputForHölzleInMiddleEvaluation();

    }

    public void displayHölzleInMiddle() {

        for (String key : Model.playerMap.keySet()) {

            if (Model.playerMap.get(key).isHölzleInMiddleSet()) {

                switch (key) {
                    case "player1": {
                        binding.p1hoelzleInMiddle.setText(String.valueOf(Model.playerMap.get("player1").getHölzleInMiddle()));
                    }
                    case "player2": {
                        binding.p2hoelzleInMiddle.setText(String.valueOf(Model.playerMap.get("player2").getHölzleInMiddle()));
                    }
                    case "player3": {
                        binding.p3hoelzleInMiddle.setText(String.valueOf(Model.playerMap.get("player3").getHölzleInMiddle()));
                    }
                    case "player4": {
                        binding.p4hoelzleInMiddle.setText(String.valueOf(Model.playerMap.get("player4").getHölzleInMiddle()));
                    }
                }
            }
            if (Model.checkHölzleInMiddleUnset() == false) {

                binding.bottomCenterText.setText("Alle Spieler haben Hölzle in der Mitte angesagt!");
                binding.middleCenterText.setText("Rundenende.");

            }
        }
    }

    public void enableUserInputForHölzleInMiddleEvaluation() {

        binding.gameButton.setText("Auswerten!");
        binding.gameButton.setVisibility(View.VISIBLE);
        binding.gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processHölzleInMiddleEvaluation();
            }
        });

    }

    public void processHölzleInMiddleEvaluation() {

        // Set all HölzleInHand player announcements visible.

        binding.p1hoelzleInHand.setText(String.valueOf(Model.playerMap.get("player1").getHölzleInHand()));
        binding.p2hoelzleInHand.setText(String.valueOf(Model.playerMap.get("player2").getHölzleInHand()));
        binding.p3hoelzleInHand.setText(String.valueOf(Model.playerMap.get("player3").getHölzleInHand()));
        binding.p4hoelzleInHand.setText(String.valueOf(Model.playerMap.get("player4").getHölzleInHand()));

        // Call method to evaluate if there is a winner for this round.

        boolean isWinnerThisRound = Model.checkIfRoundWinner();

        if (isWinnerThisRound == true) {

            String roundWinner = Model.evaluateRoundWinner();
            Model.reduceTotalPlayerHölzle(roundWinner);

            binding.middleCenterText.setText("Auswertung:");
            binding.bottomCenterText.setText(Model.totalHölzleInHandThisRound() + " Hölzle in der Mitte!\n\n" + Model.playerMap.get(roundWinner).getPlayerName() + " gewinn(s)t!");

            binding.p1hoelzle.setText(String.valueOf(Model.playerMap.get("player1").getHölzle()));
            binding.p2hoelzle.setText(String.valueOf(Model.playerMap.get("player2").getHölzle()));
            binding.p3hoelzle.setText(String.valueOf(Model.playerMap.get("player3").getHölzle()));
            binding.p4hoelzle.setText(String.valueOf(Model.playerMap.get("player4").getHölzle()));
        } else {
            binding.middleCenterText.setText("Auswertung:");
            binding.bottomCenterText.setText(Model.totalHölzleInHandThisRound() + " Hölzle in der Mitte!\n\nKein Gewinner!");

        }

        // Check if there is a match winner.

        boolean ifMatchWinner = Model.checkIfMatchWinner();

        if (ifMatchWinner) {

            String matchWinner = Model.evaluateMatchWinner();

            binding.topCenterText.setText("SPIELENDE");
            binding.bottomCenterText.setText(Model.playerMap.get(matchWinner).getPlayerName() + " ha(s)t das Spiel in " + Model.getRound() + " Runden gewonnen!");

            // Indicate winner by green button.

            binding.startButtonP1.setVisibility(View.INVISIBLE);
            binding.startButtonP2.setVisibility(View.INVISIBLE);
            binding.startButtonP3.setVisibility(View.INVISIBLE);
            binding.startButtonP4.setVisibility(View.INVISIBLE);

            switch (matchWinner) {

                case "player1": {
                    binding.startButtonP1.setVisibility(View.VISIBLE);
                    binding.viewP1.setBackgroundColor(Color.GREEN);
                    //TODO: Enter code for color change of button!
                    break;
                }
                case "player2": {
                    binding.startButtonP2.setVisibility(View.VISIBLE);
                    binding.viewP2.setBackgroundColor(Color.GREEN);
                    //TODO: Enter code for color change of button!
                    break;
                }
                case "player3": {
                    binding.startButtonP3.setVisibility(View.VISIBLE);
                    binding.viewP3.setBackgroundColor(Color.GREEN);
                    //TODO: Enter code for color change of button!
                    break;
                }
                case "player4": {
                    binding.startButtonP4.setVisibility(View.VISIBLE);
                    binding.viewP4.setBackgroundColor(Color.GREEN);
                    //TODO: Enter code for color change of button!
                    break;
                }
            }

            // Set new game button.

            binding.gameButton.setText("Neues Spiel!");
            binding.gameButton.setVisibility(View.VISIBLE);
            binding.gameButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    initializeNewGame();
                }
            });
        } else {

            // Set new Round button.

            binding.gameButton.setText("Nächste Runde");
            binding.gameButton.setVisibility(View.VISIBLE);
            binding.gameButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    processNextRound();
                }
            });
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void processNextRound() {

        // Increase round counter.
        int round = Model.getRound();
        round++;
        Model.setRound(round);

        // Set relevant GUI elements in initial status.

        binding.p1hoelzleInMiddle.setText("");
        binding.p2hoelzleInMiddle.setText("");
        binding.p3hoelzleInMiddle.setText("");
        binding.p4hoelzleInMiddle.setText("");

        binding.p1hoelzleInHand.setText("");
        binding.p2hoelzleInHand.setText("");
        binding.p3hoelzleInHand.setText("");
        binding.p4hoelzleInHand.setText("");

        binding.gameButton.setVisibility(View.INVISIBLE);

        binding.startButtonP1.setVisibility(View.INVISIBLE);
        binding.startButtonP2.setVisibility(View.INVISIBLE);
        binding.startButtonP3.setVisibility(View.INVISIBLE);
        binding.startButtonP4.setVisibility(View.INVISIBLE);

        // The player next (clockwise) to the previous start player becomes new start
        // player.

        String previousStartPlayer = Model.readStartPlayer();
        int newStartPlayer = Model.assignNewStartPlayer(previousStartPlayer);

        // New table positions are assigned according to new start player.

        Model.setTablePositions(newStartPlayer);

        // Set start player button visible for new start player.

        switch (newStartPlayer) {

            case 1: {
                binding.startButtonP1.setVisibility(View.VISIBLE);
                break;
            }
            case 2: {
                binding.startButtonP2.setVisibility(View.VISIBLE);
                break;
            }
            case 3: {
                binding.startButtonP3.setVisibility(View.VISIBLE);
                break;
            }
            case 4: {
                binding.startButtonP4.setVisibility(View.VISIBLE);
                break;
            }
        }

        String newStartPlayerID = Model.readStartPlayer();
        binding.middleCenterText.setText(Model.playerMap.get(newStartPlayerID).getPlayerName() + " beginn(s)t.");

        // Deactivate sliders and confirmation buttons for human player input as long as
        // not required.

        binding.gameButton.setVisibility(View.INVISIBLE);
        binding.sliderHoelzleInHand.setEnabled(false);
        binding.sliderHoelzleInMiddle.setEnabled(false);

        // Put all player names on GUI.

        binding.player1name.setText(Model.playerMap.get("player1").getPlayerName());
        binding.player2name.setText(Model.playerMap.get("player2").getPlayerName());
        binding.player3name.setText(Model.playerMap.get("player3").getPlayerName());
        binding.player4name.setText(Model.playerMap.get("player4").getPlayerName());

        // Update round indication display.

        binding.topCenterText.setText("Runde " + Model.getRound());
        binding.bottomCenterText.setVisibility(View.INVISIBLE);
        binding.gameButton.setVisibility(View.INVISIBLE);

        // Set sliders to current values.

        binding.sliderHoelzleInHand.setMin(0);
        binding.sliderHoelzleInHand.setMax(Model.playerMap.get("player1").getHölzle());

        binding.sliderHoelzleInMiddle.setMin(0);
        binding.sliderHoelzleInMiddle.setMax(Model.getTotalHölzleInGame());

        // Reset HölzleInMiddle values of all players.

        Model.resetPlayerHölzleValues();

        // Assign randomly the number of HölzleInHand per player.

        Model.setKIhölzleInHand();

        binding.p2hoelzleInHand.setText("X");
        binding.p3hoelzleInHand.setText("X");
        binding.p4hoelzleInHand.setText("X");

        // Activate input elements for HölzleInHand.

        binding.gameButton.setText("Verstecken!");
        binding.bottomCenterText.setVisibility(View.VISIBLE);
        binding.bottomCenterText.setText("Verstecke deine Hölzle!");
        binding.sliderHoelzleInHand.setEnabled(true);
        binding.gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHumanHölzleInHand();
            }
        });
        binding.gameButton.setVisibility(View.VISIBLE);

    }
}
