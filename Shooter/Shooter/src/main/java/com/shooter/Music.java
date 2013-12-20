package com.shooter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class Music extends Service {
    public static boolean isRunning = false;
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setMusicOptions(this,
                        GameEngine.LOOP_BACKGROUND_MUSIC,
                        GameEngine.R_VOLUME,
                        GameEngine.L_VOLUME,
                        GameEngine.SPLASH_SCREEN_MUSIC);
    }

    public void setMusicOptions(Context context, boolean isLooped, int rVolume, int lVolume, int soundFile) {
        player = MediaPlayer.create(context,soundFile);
        player.setLooping(isLooped);
        player.setVolume(lVolume, rVolume);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            player.start();
            isRunning = true;
        } catch (Exception e) {
            isRunning = false;
            player.stop();
        }
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        player.stop();
    }
}
