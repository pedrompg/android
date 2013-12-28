package com.shooter;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.View;

public class GameEngine {
    public static final int GAME_THREAD_DELAY = 3000;
    public static final int MENU_BUTTON_ALPHA = 0;
    public static final boolean HAPTIC_BUTTON_FEEDBACK = true;
    public static final int SPLASH_SCREEN_MUSIC = R.raw.warfieldedit;
    public static final int R_VOLUME = 100;
    public static final int L_VOLUME = 100;
    public static final boolean LOOP_BACKGROUND_MUSIC = true;
    public static final int GAME_THREAD_FPS_SLEEP = (1000/60);

    //Backgrounds
    public static final int BACKGROUND_LAYER_ONE = R.drawable.backgroundstars;
    public static float SCROLL_BACKGROUND_1 = .002f;
    public static final int BACKGROUND_LAYER_TWO = R.drawable.debris;
    public static float SCROLL_BACKGROUND_2 = .007f;

    //PLayer
    public static int playerFlightAction = 0;
    public static final int PLAYER_SHIP = R.drawable.good_sprite;
    public static final int PLAYER_BANK_LEFT_1 = 1;
    public static final int PLAYER_RELEASE = 3;
    public static final int PLAYER_BANK_RIGHT_1 = 4;
    public static final int PLAYER_FRAMES_BETWEEN_ANIMATION = 9;
    public static final float PLAYER_BANK_SPEED = 0.1f;
    public static float playerBankPosX = 1.75f;

    public static Context context;
    public static Display display;
    public static Thread musicThread;

    public boolean onExit(View v) {
        try {
            Intent bgMusic = new Intent(context, Music.class);
            context.stopService(bgMusic);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean onExit() {
        try {
            Intent bgMusic = new Intent(context, Music.class);
            context.stopService(bgMusic);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
