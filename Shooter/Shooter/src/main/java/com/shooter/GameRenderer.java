package com.shooter;

import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameRenderer implements Renderer {
    private Background background1 = new Background();
    private Background background2 = new Background();
    private GoodGuy player1 = new GoodGuy();

    private float bgScroll1;
    private float bgScroll2;
    private int goodGuyBankFrames = 0;

    private long loopStart = 0;
    private long loopEnd = 0;
    private long loopRuntime = 0;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);

        background1.loadTexture(gl, GameEngine.BACKGROUND_LAYER_ONE, GameEngine.context);
        background2.loadTexture(gl, GameEngine.BACKGROUND_LAYER_TWO, GameEngine.context);
        player1.loadTexture(gl, GameEngine.PLAYER_SHIP, GameEngine.context);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        loopStart = System.currentTimeMillis();
        try {
            if(loopRuntime < GameEngine.GAME_THREAD_FPS_SLEEP) {
                Thread.sleep(GameEngine.GAME_THREAD_FPS_SLEEP);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        scrollBackground1(gl);
        scrollBackground2(gl);

        movePlayer1(gl);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
        loopEnd = System.currentTimeMillis();
        loopRuntime = loopEnd - loopStart;
    }

    private void scrollBackground1(GL10 gl) {
        if(bgScroll1 == Float.MAX_VALUE) {
            bgScroll1 = 0f;
        }

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(1f, 1f, 1f);
        gl.glTranslatef(0f, 0f, 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f,bgScroll1, 0.0f);

        background1.draw(gl);
        gl.glPopMatrix();
        bgScroll1 += GameEngine.SCROLL_BACKGROUND_1;
        gl.glLoadIdentity();
    }

    private void scrollBackground2(GL10 gl){
        if (bgScroll2 == Float.MAX_VALUE){
            bgScroll2 = 0f;
        }

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glScalef(.5f, 1f, 1f);
        gl.glTranslatef(1.5f, 0f, 0f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef( 0.0f,bgScroll2, 0.0f);

        background2.draw(gl);
        gl.glPopMatrix();
        bgScroll2 +=  GameEngine.SCROLL_BACKGROUND_2;
        gl.glLoadIdentity();
    }

    private void movePlayer1(GL10 gl) {
        switch (GameEngine.playerFlightAction){
            case GameEngine.PLAYER_BANK_LEFT_1:
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glPushMatrix();
                gl.glScalef(.25f, .25f, 1f);
                if (goodGuyBankFrames < GameEngine.PLAYER_FRAMES_BETWEEN_ANIMATION && GameEngine.playerBankPosX > 0){
                    GameEngine.playerBankPosX -= GameEngine.PLAYER_BANK_SPEED;
                    gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                    gl.glMatrixMode(GL10.GL_TEXTURE);
                    gl.glLoadIdentity();
                    gl.glTranslatef(0.75f,0.0f, 0.0f);
                    goodGuyBankFrames += 1;
                }else if (goodGuyBankFrames >= GameEngine.PLAYER_FRAMES_BETWEEN_ANIMATION && GameEngine.playerBankPosX > 0){
                    GameEngine.playerBankPosX -= GameEngine.PLAYER_BANK_SPEED;
                    gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                    gl.glMatrixMode(GL10.GL_TEXTURE);
                    gl.glLoadIdentity();
                    gl.glTranslatef(0.0f,0.25f, 0.0f);
                }else{
                    gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                    gl.glMatrixMode(GL10.GL_TEXTURE);
                    gl.glLoadIdentity();
                    gl.glTranslatef(0.0f,0.0f, 0.0f);
                }
                player1.draw(gl);
                gl.glPopMatrix();
                gl.glLoadIdentity();

                break;

            case GameEngine.PLAYER_BANK_RIGHT_1:
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glPushMatrix();
                gl.glScalef(.25f, .25f, 1f);
                if (goodGuyBankFrames < GameEngine.PLAYER_FRAMES_BETWEEN_ANIMATION && GameEngine.playerBankPosX < 3){
                    GameEngine.playerBankPosX += GameEngine.PLAYER_BANK_SPEED;
                    gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                    gl.glMatrixMode(GL10.GL_TEXTURE);
                    gl.glLoadIdentity();
                    gl.glTranslatef(0.25f,0.0f, 0.0f);
                    goodGuyBankFrames += 1;
                }else if (goodGuyBankFrames >= GameEngine.PLAYER_FRAMES_BETWEEN_ANIMATION && GameEngine.playerBankPosX < 3){
                    gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                    gl.glMatrixMode(GL10.GL_TEXTURE);
                    gl.glLoadIdentity();
                    gl.glTranslatef(0.50f,0.0f, 0.0f);
                    GameEngine.playerBankPosX += GameEngine.PLAYER_BANK_SPEED;
                }else{
                    gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                    gl.glMatrixMode(GL10.GL_TEXTURE);
                    gl.glLoadIdentity();
                    gl.glTranslatef(0.0f,0.0f, 0.0f);
                }
                player1.draw(gl);
                gl.glPopMatrix();
                gl.glLoadIdentity();

                break;

            case GameEngine.PLAYER_RELEASE:
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glPushMatrix();
                gl.glScalef(0.25f, 0.25f, 1f);
                gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                gl.glMatrixMode(GL10.GL_TEXTURE);
                gl.glLoadIdentity();
                gl.glTranslatef(0.0f, 0.0f, 0.0f);
                goodGuyBankFrames += 1;
                player1.draw(gl);
                gl.glPopMatrix();
                gl.glLoadIdentity();

                break;

            default:
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glPushMatrix();
                gl.glScalef(0.25f, 0.25f, 1f);
                gl.glTranslatef(GameEngine.playerBankPosX, 0f, 0f);
                gl.glMatrixMode(GL10.GL_TEXTURE);
                gl.glLoadIdentity();
                gl.glTranslatef(0.0f,0.0f, 0.0f);
                player1.draw(gl);
                gl.glPopMatrix();
                gl.glLoadIdentity();

                break;
        }
    }
}
