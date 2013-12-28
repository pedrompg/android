package com.shooter;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;

public class Game extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        Point size = new Point();
        GameEngine.display.getSize(size);
        int height = size.y / 4;
        int playableArea = size.y - height;
        if(y > playableArea) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    if(x < size.x / 2) {
                        GameEngine.playerFlightAction = GameEngine.PLAYER_BANK_LEFT_1;
                    } else {
                        GameEngine.playerFlightAction = GameEngine.PLAYER_BANK_RIGHT_1;
                    }
                break;

                case MotionEvent.ACTION_UP:
                    GameEngine.playerFlightAction = GameEngine.PLAYER_RELEASE;

                break;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }
}
