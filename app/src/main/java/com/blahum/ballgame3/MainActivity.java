package com.blahum.ballgame3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final int MAX_BOUNCE = 10;
    public int BORDER_LEFT = 0;
    public int BORDER_RIGHT = Resources.getSystem().getDisplayMetrics().widthPixels - 50;
    public int BORDER_TOP = -70;
    public int BORDER_BOTTOM = Resources.getSystem().getDisplayMetrics().heightPixels - 300;
    public int fromX;
    public int fromY;
    private Timer ballTime;
    private int z = 0;
    private View x;
    private double theta = 30;
    private int xDirection = 1;
    private int yDirection = -1;
    private final int MOVE_X = 50;
    private final int MOVE_Y = 100;
    int bounces = 0;
    boolean win = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ballTime = new Timer();
    }

    public void startGame(View view) {
        EditText text = (EditText) findViewById(R.id.degrees);
        Button b = (Button) findViewById(R.id.starter);
        String degree = text.getText().toString();
        //Log.i("enter", "theta");
        try {
            double deg = Double.parseDouble(degree);
            if ((deg >= 0.0) && (deg <= 90.0)) {
                theta = deg;
            }
        } catch (Exception e) {
        }
        text.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);

        startTime(view);
    }

    public void startTime(View view) {
        x = view;
        if (z == 0) {
            ballTime.schedule(new TimerTask() {
                @Override
                public void run() {
                    ballRoll(x, (ImageView) findViewById(R.id.redBall));
                }

            }, 0, 100);
            z++;
        }
    }

    private void message() {
        TextView wT = (TextView) findViewById(R.id.winText);
        TextView lT = (TextView) findViewById(R.id.lossText);
        //Log.i("bounce", "bounces: " + bounces);
        if (bounces >= MAX_BOUNCE) {
            lT.setVisibility(View.VISIBLE);
        } else if (win) {
            wT.setVisibility(View.VISIBLE);
        }
    }

    public void ballRoll(View view, ImageView b) {
        //Log.i("", "")
        ImageView ball = b;
        fromX = (int) (ball.getX());
        fromY = (int) (ball.getY());
        int transX = getTranslateX();
        int transY = getTranslateY();
        if (notOverlapBall(ball) && bounces < MAX_BOUNCE) {
            ball.animate().translationXBy(transX).translationYBy(transY).setDuration(25);
        } else {
            if (!notOverlapBall(ball)) {
                win = true;
            }
            ballTime.cancel();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    message();
                }
            });
        }
    }

    private boolean notOverlapBall(ImageView ball) {
        ImageView area = (ImageView) findViewById(R.id.area);
        int ballCenterX = (int) (ball.getX() + ball.getWidth() / 2);
        int ballCenterY = (int) (ball.getY() + ball.getHeight() / 2);
        int areaMAXX = (int) (area.getX() + area.getWidth());
        int areaMAXY = (int) (area.getY() + area.getHeight());
        if (isOverlapping(ballCenterX, ballCenterY, areaMAXX, areaMAXY, area)) {
            return false;
        }
        return true;
    }

    private boolean isOverlapping(int ballCenterX, int ballCenterY, int areaMAXX, int areaMAXY, ImageView area) {
        if ((ballCenterX >= area.getX() && ballCenterX <= areaMAXX) && (ballCenterY >= area.getY() && ballCenterY <= areaMAXY)) {
            return true;
        }
        return false;
    }

    private int getTranslateX() {
        checkBoundaryX(fromX + MOVE_X * xDirection);
        return MOVE_X * xDirection;
    }

    private void checkBoundaryX(int x) {
        if (x > BORDER_RIGHT) {
            xDirection = -1;
            theta = 90 - theta;
            bounces++;
        }
        if (x < BORDER_LEFT) {
            xDirection = 1;
            theta = 90 - theta;
            bounces++;
        }
    }

    private int getTranslateY() {
        checkBoundaryY(fromY + MOVE_Y * yDirection);
        return Math.min(80, (int) (Math.tan((theta * Math.PI) / (180)) * MOVE_X)) * yDirection;
    }

    private void checkBoundaryY(int y) {
        if (y > BORDER_BOTTOM) {
            yDirection = -1;
            theta = 90 - theta;
            bounces++;
        }
        if (y < BORDER_TOP) {
            yDirection = 1;
            theta = 90 - theta;
            bounces++;
        }
    }
}