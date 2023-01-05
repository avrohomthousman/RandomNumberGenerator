package com.example.random_number_generator.activities;

import static androidx.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.random_number_generator.R;
import com.example.random_number_generator.lib.Utils;
import com.example.random_number_generator.model.RandomNumber;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.random_number_generator.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String HISTORY_KEY = "history";

    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarFile.toolbar);

        mRandomNumber = new RandomNumber();

        initializeHistoryList(savedInstanceState);

        setupFab();
    }


    /**
     * Loads the history list from the data saved when the app was last closed.
     * This is the method you gave us, except that I took out the key argument and
     * made it a final variable.
     *
     * @param savedInstanceState the state of the app we need to restore.
     */
    private void initializeHistoryList (Bundle savedInstanceState)
    {
        if (savedInstanceState != null) {
            mNumberHistory = savedInstanceState.getIntegerArrayList (HISTORY_KEY);
        }
        else {
            String history = getDefaultSharedPreferences (this).getString (HISTORY_KEY, null);
            mNumberHistory = history == null ?
                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
        }
    }


    /**
     * Sets up the fab.
     */
    private void setupFab() {
        binding.fabFile.fab.setOnClickListener(
                new FabClickListener( this.binding, this.mRandomNumber, this.mNumberHistory));
    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getDefaultSharedPreferences (this);
        SharedPreferences.Editor editor = prefs.edit();


        editor.putString(HISTORY_KEY, Utils.getJSONStringFromNumberList(this.mNumberHistory));
        editor.apply();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList(HISTORY_KEY, mNumberHistory);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_history) {
            showHistory();
            return true;
        }
        else if(id == R.id.action_clear_history){
            clearHistory();
            return true;
        }
        else if(id == R.id.action_about){
            displayAbout();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Displays all the numbers generated so far.
     */
    private void showHistory() {
        Utils.showInfoDialog (MainActivity.this,
                "History", mNumberHistory.toString());
    }


    /**
     * Clears the history of random numbers generated so far.
     */
    private void clearHistory(){
        mNumberHistory.clear();
    }


    /**
     * Displays a popup with information about this app.
     */
    private void displayAbout(){
        Snackbar.make(binding.getRoot(), getString(R.string.about_text), Snackbar.LENGTH_LONG)
                .show();
    }
}