package com.android16_team.caro_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class PlayWithBot extends AppCompatActivity  {
    private DrawView drawView;
    private TextView txtCountDownTimer;
    private float cx,cy;
    private ImageButton btnMessage;
    private AlphaBeta bot;
    private int[][] tmpBoard=new int[30][30];
    private ScaleGestureDetector scale;
    private boolean flagmove = false;
    private boolean pinch = false;
    private int mPtrCount = 0;
    private int space = 100;
    private int padding = 10;
    private float mPosX = 0;
    private float mPosY = 0;
    private ScaleGestureDetector mScaleDetector;
    private float mLastTouchX;
    private float mLastTouchY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caro_table);
        drawView = findViewById(R.id.drawView);
        bot = new AlphaBeta(20,3);
        mScaleDetector = new ScaleGestureDetector(PlayWithBot.this,new zoom());
        btnMessage = findViewById(R.id.btnMessage);
        for(int i = 0 ; i < 30*30;i++){
            tmpBoard[i/30][i%30] = 0;
        }
        bot.setBoard(tmpBoard);


//        btnMessage.hide();


        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                mScaleDetector.onTouchEvent(event);
                switch (action) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN: {
                        mPtrCount++;
                        final float x = event.getX();
                        final float y = event.getY();

                        // Remember where we started
                        mLastTouchX = x;
                        mLastTouchY = y;
                        break;
                    }
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP: {
                        if (flagmove || pinch) {
                            mPtrCount--;
                            pinch = false;
                            flagmove = false;
                        } else { //danh o day
                            mPtrCount--;
                            cx = event.getX();
                            cy = event.getY();
                            String msg = drawView.check(cx, cy);
                            if (drawView.getCurState() == CheckedState.O && !drawView.isFinish() && msg != null) {//neu vi tri hop le
                                Node node = drawView.createNode(cx, cy);
                                drawView.setCheckedStates(node);
                                drawView.invalidate();
                                //check won?
                                if (drawView.isFinish()) {
                                    //win
                                    Toast.makeText(PlayWithBot.this, "win", Toast.LENGTH_SHORT).show();

                                } else {
                                    tmpBoard[node.getY()][node.getX()] = drawView.getCheckedStates()[node.getY()][node.getX()];
                                    Thread run = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            drawView.clearNextStack();
                                            // not win -> bot play
                                            Node nodes = bot.getBestNode();
                                            nodes.swapnode();
                                            tmpBoard[nodes.getY()][nodes.getX()] = drawView.getCurState();
                                            drawView.setCheckedStates(nodes);
                                            drawView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    drawView.invalidate();
                                                }
                                            });
                                            if (drawView.isFinish()) { // neu khong danh duoc ma game da ket thuc co nghia la thua
                                                //lose
                                                drawView.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(PlayWithBot.this, "lose", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    run.start();
                                }
                            }
                        }


                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                        if (!mScaleDetector.isInProgress()) {

                            final float x = event.getX();
                            final float y = event.getY();

                            // Calculate the distance moved
                            final float dx = x - mLastTouchX;
                            final float dy = y - mLastTouchY;
                            if (Math.abs((int) dx) > 30 || Math.abs((int) dy) > 30) {
                                flagmove = true;
                                // Move the object
                                mPosX += dx;
                                mPosY += dy;
                                mPosX = mPosX > 0 ? 0 : mPosX;
                                mPosY = mPosY > 0 ? 0 : mPosY;
                                int minwidth = -1 * (30 * space - drawView.getWidth());
                                int minheight = -1 * (30 * space - drawView.getHeight());
                                mPosX = mPosX < minwidth ? minwidth : mPosX;
                                mPosY = mPosY < minheight ? minheight : mPosY;
                                // Remember this touch position for the next move event
                                mLastTouchX = x;
                                mLastTouchY = y;

                                // Invalidate to request a redraw
                                drawView.updatePosition((int) mPosX, (int) mPosY);
                                break;
                            }
                        }
                }



                return false;
            }
        });

        drawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }


        });



        txtCountDownTimer = findViewById(R.id.countTimer);
        CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtCountDownTimer.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public class zoom extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mScaleFactor = 1;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            pinch = true;
            mScaleFactor = detector.getScaleFactor();
            space = Math.round(mScaleFactor * space) ;
            padding = Math.round(mScaleFactor* padding);

            space = space > 200 ? 200 : space;
            space = space < 80 ? 80 :space;
            drawView.setSpace(space,padding);
            return true;
        }
    }
}
