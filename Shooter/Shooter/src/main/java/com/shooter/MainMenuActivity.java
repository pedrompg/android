package com.shooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final GameEngine gameEngine = new GameEngine();

        GameEngine.musicThread = new Thread() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                Intent bgMusic = new Intent(context, Music.class);
                startService(bgMusic);
                GameEngine.context = context;
            }
        };

        GameEngine.musicThread.start();

        ImageButton startButton = (ImageButton) findViewById(R.id.start_button);
        ImageButton exitButton = (ImageButton) findViewById(R.id.exit_button);

        startButton.getBackground().setAlpha(GameEngine.MENU_BUTTON_ALPHA);
        startButton.setHapticFeedbackEnabled(GameEngine.HAPTIC_BUTTON_FEEDBACK);

        exitButton.getBackground().setAlpha(GameEngine.MENU_BUTTON_ALPHA);
        exitButton.setHapticFeedbackEnabled(GameEngine.HAPTIC_BUTTON_FEEDBACK);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean clean = gameEngine.onExit(v);
                if(clean) {
                    finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
