package com.example.voytenko.lesson3;

/**
 * Created by jkram on 3/22/2017.
 */
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TempSprite {
    private float x;
    private float y;
    private Bitmap bmp;
    // after 15 ticks aka 15 frame/ sec, or 15 calls of onDraw())sprite will disappear
    private int life = 15;
    private List<TempSprite> temps; // list of temp sprites

    public TempSprite(List<TempSprite> temps, GameView gameView, float x, float y, Bitmap bmp) {
        this.x = Math.min(Math.max(x-bmp.getWidth()/2,0),
                gameView.getWidth() - bmp.getWidth());
        this.y = Math.min(Math.max(y-bmp.getHeight()/2,0),
                gameView.getHeight() - bmp.getHeight());
        this.bmp = bmp;
        this.temps = temps;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x, y, null);
    }

    private void update() {
        if (--life < 1) { // every tick we decrease the life property of temp sprite
            temps.remove(this);
        }
    }
}

