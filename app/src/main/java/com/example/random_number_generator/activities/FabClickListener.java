package com.example.random_number_generator.activities;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.random_number_generator.databinding.ActivityMainBinding;
import com.example.random_number_generator.model.RandomNumber;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FabClickListener implements View.OnClickListener {
    private static final String OUTPUT = "Your random number is %d";

    //Data we need from the main activity.
    private final ActivityMainBinding binding;
    private final RandomNumber mRandomNumber;
    private final ArrayList<Integer> mNumberHistory;



    public FabClickListener(ActivityMainBinding binding, RandomNumber mRandomNumber, ArrayList<Integer> mNumberHistory) {
        this.mRandomNumber = mRandomNumber;
        this.mNumberHistory = mNumberHistory;
        this.binding = binding;
    }


    @Override
    public void onClick(View v) {
        String from = binding.contentMain.minInputSection.minimumValue.getText().toString();;
        String to = binding.contentMain.maxInputSection.maximumValue.getText().toString();;



        if(!allDataHasBeenEntered(from, to)){
            makeErrorSnackBar(v, "Please make sure you entered all the required data");
            return;
        }


        int fromAsInt;
        int toAsInt;
        try{
            fromAsInt = Integer.parseInt(from);
            toAsInt = Integer.parseInt(to);
        }
        catch (NumberFormatException e){
            Log.println(Log.ERROR, "User Error", "Could not parse user entry to int");

            makeErrorSnackBar(v, "Please enter only valid numbers");
            return;
        }


        if(fromAsInt >= toAsInt){
            makeErrorSnackBar(v, "Please make sure your maximum is larger than your minimum");
            return;
        }


        mRandomNumber.setFromTo(fromAsInt, toAsInt);
        int result = mRandomNumber.getCurrentRandomNumber();
        mNumberHistory.add(result);

        binding.contentMain.outputFile.output.setText(  String.format(OUTPUT, result)  );
    }


    /**
     * Checks that all the specified strings have been initailized to a non null
     * String with a length greater than zero. I made it general so it can support
     * any number if strings.
     *
     * @param data the strings to test.
     * @return true if all the strings have been initailized correctly, and false otherwise.
     */
    private boolean allDataHasBeenEntered(String... data){
        for(String item : data){
            if(item == null || item.length() == 0){
                return false;
            }
        }

        return true;
    }


    /**
     * Displays a snackBar with the specified error message.
     */
    private void makeErrorSnackBar(View parent, String message){
        Snackbar.make(parent, message, Snackbar.LENGTH_LONG)
                .show();
    }
}
