package com.example.physlab;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.Locale;

public class DrawThread extends Thread {

    int h0;
    float g;
    private final float DIV_H1 = 0.20f;
    private final float DIV_H2 = 0.90f;
    private final float DIV_V = 0.85f;
    private final float LINE_SIZE = 0.05f;
    private final float BALL_SIZE = 0.05f;
    Context context;

    private boolean running = true, move = false;

    private SurfaceHolder surfaceHolder;
    private Paint paint = new Paint();
    {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setSubpixelText(true);
        paint.setTextSize(40);
    }

    private long bTime, eTime;
    private float secTime;

    public DrawThread(Context context, SurfaceHolder surfaceHolder) {
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        Canvas canvas = surfaceHolder.lockCanvas();
        Ball.x = canvas.getWidth() * DIV_V / 2;
        Ball.y = canvas.getHeight() * (DIV_H1 - BALL_SIZE);
        Ball.r = canvas.getHeight() * BALL_SIZE;
        moveBall(canvas);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void startMove(int h0, float g) {
        this.h0 = h0;
        this.g = g;
        move = true;
    }

    public void stopMove() {
        move = false;
    }

    public void requestStopThread() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            bTime = System.currentTimeMillis();
            while (move) {
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    try {
                        eTime = System.currentTimeMillis();
                        secTime = (eTime - bTime) / 1000f;
                        if (secTime > (float) Math.sqrt(2 * h0 / g)) {
                            secTime = (float) Math.sqrt(2 * h0 / g);
                        }
                        Ball.y = canvas.getHeight() * (DIV_H1 - BALL_SIZE + (DIV_H2 - DIV_H1) * secTime / ((float) Math.sqrt(2 * h0 / g)));

                        if (Ball.y + Ball.r >= canvas.getHeight() * DIV_H2) {
                            Ball.y = canvas.getHeight() * DIV_H2 - Ball.r;
                            move = false;
                        }
                        moveBall(canvas);
                    } finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void drawBackground(Canvas canvas) {
        int h = canvas.getHeight();
        int w = canvas.getWidth();
        Resources res = context.getResources();
        paint.setColor(res.getColor(R.color.grass, null));
        canvas.drawRect(0, h * DIV_H2, w, h, paint);
        paint.setColor(res.getColor(R.color.sky, null));
        canvas.drawRect(0, 0, w * DIV_V, h * DIV_H2, paint);
        paint.setColor(res.getColor(R.color.wood, null));
        canvas.drawRect(w * DIV_V, 0, w, h * DIV_H2, paint);
        paint.setColor(res.getColor(R.color.black, null));
        for (int i = 0; i < 4; i++) {
            canvas.drawLine(w * DIV_V, h * (DIV_H1 + (DIV_H2 - DIV_H1) * i / 4),
                    w * (DIV_V + LINE_SIZE), h * (DIV_H1 + (DIV_H2 - DIV_H1) * i / 4), paint);
            canvas.drawText(i * 25 + "%", w * (DIV_V + 1.25f * LINE_SIZE),
                    h * (DIV_H1 + (DIV_H2 - DIV_H1) * i / 4) + 10, paint);
        }
    }

    private void drawBall(Canvas canvas) {
        paint.setColor(context.getResources().getColor(R.color.purple_500, null));
        canvas.drawCircle(Ball.x, Ball.y, Ball.r, paint);
    }

    private void drawText(Canvas canvas) {
        paint.setColor(context.getResources().getColor(R.color.black, null));
        canvas.drawText(context.getResources().getText(R.string.time) + " " + String.format(Locale.getDefault(),"%.3f", secTime),
                canvas.getWidth() * LINE_SIZE, canvas.getHeight() * LINE_SIZE + 30, paint);
    }

    private void moveBall(Canvas canvas) {
        drawBackground(canvas);
        drawBall(canvas);
        drawText(canvas);
    }
}
