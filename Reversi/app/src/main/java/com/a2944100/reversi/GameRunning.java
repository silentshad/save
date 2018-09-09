package com.a2944100.reversi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Main and only activity responsible for displaying the HUD and the game
 */
public class GameRunning extends AppCompatActivity implements View.OnTouchListener {
    View my_view;
    @Override


    /**
     * create the activity and set the layout
     */
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_running);
        my_view = findViewById(R.id.board_view);

    }

    @Override
    /**
     * initialize the onTouch listener
     */
    public boolean onTouch(View v, MotionEvent event) {
        return my_view.onTouchEvent(event);
    }

    /**
     * function called when the restart button is clicked, restart the game
     * @param view the main view
     */
    public void Restart(View view)
    {
        ((BoardView)my_view).restart();
    }
}
