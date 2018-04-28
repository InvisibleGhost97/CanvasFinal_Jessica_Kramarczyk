package com.example.voytenko.lesson3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;


public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    //private Sprite sprite;
    private MediaPlayer mp1, mp2, mp3;

    private List<Sprite> sprites = new ArrayList<Sprite>();
    private long hit;

    //blood stain
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private Bitmap bmpBlood;


    //for score
    private static int count = 0;   //static will accumulate the score upon rotation


    //for the timer
    public int seconds = 1000;
    public int minutes = 1;

    private static long millisUntilFinished = 30000;

    //create variable
    private SharedPreferences myPrefs;
    private SharedPreferences TimerPref;

    int goodguys = 0;
    int badguys = 0;


    public GameView(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        mp1 = MediaPlayer.create(context, R.raw.ah);
        mp2 = MediaPlayer.create(context, R.raw.scream2);
        mp3 = MediaPlayer.create(context, R.raw.whoop);

        Timer t = new Timer();



        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });


        // add sprite element, first we initialise bitmap

        // and then declare object of Sprite class

        //add sprite element to the ArrayList
        sprites.add(createSprite(R.drawable.grin));
        sprites.add(createSprite(R.drawable.grin));
        sprites.add(createSprite(R.drawable.grin));
        sprites.add(createSprite(R.drawable.grin));
        sprites.add(createSprite(R.drawable.grin));


        sprites.add(createSprite(R.drawable.distraction));


        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));
        sprites.add(createSprite(R.drawable.player));




        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);


        myPrefs = context.getSharedPreferences("Score", Context.MODE_PRIVATE);
        TimerPref = context.getSharedPreferences("Timer", Context.MODE_PRIVATE);

    }


    //this method will return a single sprite
    private Sprite createSprite(int resource) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this, bmp);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);

            for (int i = temps.size() - 1; i >= 0; i--) { //  iterate backwards to avoid errors
                temps.get(i).onDraw(canvas);
            }

            for (Sprite sprite : sprites) {
                sprite.onDraw(canvas);
                // Log.d("Sprite : ", "i= " + sprites.indexOf(sprite));
            }

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            canvas.drawText("Score: " + count, 0, 75, paint);


            Paint ptimer = new Paint();
            ptimer.setStyle(Paint.Style.FILL);
            ptimer.setTextSize(40);

            Paint pResult = new Paint();
            pResult.setStyle(Paint.Style.FILL);
            pResult.setColor(Color.RED);
            pResult.setTextSize(60);
            pResult.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));


            if (millisUntilFinished>=0){
                canvas.drawText("Time: " + millisUntilFinished/1000, 250, 75, ptimer);
                millisUntilFinished-=19;

                SharedPreferences.Editor editor2 = myPrefs.edit();
                editor2.putLong("Timer", millisUntilFinished);
                editor2.commit();
            }

            if (millisUntilFinished<=0){
                canvas.drawText("Time: OVER", 250, 75, ptimer);
                millisUntilFinished-=0;

                canvas.drawColor(Color.WHITE);

                canvas.drawText("GAME OVER", 100, 350, pResult);
                canvas.drawText("You killed:", 100, 400, ptimer);
                canvas.drawText("Good Guys:" + goodguys, 100, 450, ptimer);
                canvas.drawText("Bad Guys:" + badguys, 100, 500, ptimer);

                SharedPreferences.Editor editor2 = myPrefs.edit();
                editor2.putLong("Timer", millisUntilFinished);
                editor2.commit();
            }

            //millisUntilFinished--;

            /*if (minutes <= 1 && seconds <= 1000 ){
                seconds--;
            }


            if (minutes == 1 && seconds == 0){
                minutes =0;
                seconds = 1000;
            }

            if (minutes == 0 && seconds == 0){
                minutes =0;
                seconds = 0;
            }*/

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - hit > 100) {
            hit = System.currentTimeMillis();
            synchronized (getHolder()) {
                float x = event.getX();
                float y = event.getY();
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollision(x, y)) {
                        if (i < 5) {
                            //mp2.pause();
                            mp1.start();
                            //green hair
                            //Log.d("Sprite", "BAD choice");
                            count--;
                            SharedPreferences.Editor editor = myPrefs.edit();
                            editor.putInt("Score", count);
                            editor.commit();

                            goodguys++;
                        }

                        if(i==5){
                            mp3.start();

                        }

                       else {
                            mp2.start();
                            //mp1.pause();
                            //red eye
                            //Log.d("Sprite", "GOOD choice");
                            count++;
                            SharedPreferences.Editor editor = myPrefs.edit();
                            editor.putInt("Score", count);
                            editor.commit();

                            badguys++;
                        }
                        sprites.remove(sprite);
                        temps.add(new TempSprite(temps, this, x, y, bmpBlood));
                        break;
                    }
                }

            }
        }
        return super.onTouchEvent(event);
    }
}


/*
    As we run the project, we notice not ALL sprites are visible on the stage. Why?

    All sprites are not visible on stage because a new sprite is being drawn over an old sprite.
    So, because we first declared this line, sprites.add(createSprite(R.drawable.grin));, followed by
    sprites.add(createSprite(R.drawable.player)); the program is only drawing the player sprite since
    it is the most recent item to be drawn.
 */


