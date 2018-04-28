package com.example.voytenko.lesson3;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

public class Sprite {

    private Random rand = new Random();

    private int x = rand.nextInt(100); // sprite coordinate x
    private int y = rand.nextInt(100); // sprite coordinate y
    private int xSpeed = rand.nextInt(25)+3 ; // sprite x speed
    private int ySpeed = rand.nextInt(25)+3 ; // sprite y speed
    private int width;
    private int height;

    private GameView gameView;  // reference to GameView
    private Bitmap bmp;         // sprite Bitmap

    public boolean collision;

    //constructor
    public Sprite(GameView gameView, Bitmap bmp) {
        this.gameView=gameView;
        this.bmp=bmp;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
    }


    // boundaries collision for a single bitmap
    private void update() {
        // boundaries collision for east / west
        spriteCollision();
        collision = false;
        if (x > gameView.getWidth() - bmp.getWidth() - xSpeed) {
            xSpeed = -xSpeed;
        }
        if (x + xSpeed< 0) {
            xSpeed = rand.nextInt(25)+3;
        }
        x = x + xSpeed;

        // boundaries collision for north /south
        if (y > gameView.getHeight() - bmp.getHeight() - ySpeed) {
            ySpeed = -ySpeed;
        }
        if (y + ySpeed< 0) {
            ySpeed = rand.nextInt(25)+3;
        }
        y = y + ySpeed;

    }

    public void onDraw(Canvas canvas) {
        update();
        // canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, x, y, null);
    }

    public boolean isCollision(float x2, float y2) {

        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

    public Rect getBounds(){
        return new Rect(x,y,x+width,y+height);
    }

    public void spriteCollision() {
        Rect mySprite = getBounds();
        Rect myCollisionObject = getBounds();
        if (mySprite.intersect(myCollisionObject)) {
            collision = true;

        }
        else{
            collision = false;
        }
    }
}

