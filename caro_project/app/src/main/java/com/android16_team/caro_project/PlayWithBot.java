package com.android16_team.caro_project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class PlayWithBot extends AppCompatActivity {

    public static final int WIN = 1;
    public static final int LOSE = 2;
    public static final int CHANGE_ICON = 3;

    private DrawView drawView;
    private TextView txtCountDownTimer;
    private float cx, cy;
    private ImageButton btnMessage;
    private AlphaBeta bot;
    private int[][] tmpBoard = new int[30][30];
    private ScaleGestureDetector scale;
    private boolean flagmove = false;
    private boolean changeturn = false;
    private int pinch = 0;
    private int space = 100;
    private int padding = 10;
    private float mPosX = 0;
    private float mPosY = 0;
    private ScaleGestureDetector mScaleDetector;
    private float mLastTouchX;
    private float mLastTouchY;
    private Toolbar myToolbar;
    private boolean toggleMode = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caro_table);
        setContentView(R.layout.caro_table);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("BOT ĐẸP TRAI");
        drawView = findViewById(R.id.drawView);
        drawView.createstack();
        bot = new AlphaBeta(20, 3);
        mScaleDetector = new ScaleGestureDetector(PlayWithBot.this, new zoom());
        btnMessage = findViewById(R.id.btnMessage);
        btnMessage.setVisibility(View.INVISIBLE);
        findViewById(R.id.txtWaitingMsg).setVisibility(View.INVISIBLE);
        for (int i = 0; i < 30 * 30; i++) {
            tmpBoard[i / 30][i % 30] = 0;
        }
        bot.setBoard(tmpBoard);





        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                mScaleDetector.onTouchEvent(event);
                switch (action) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN: {
                        final float x = event.getX();
                        final float y = event.getY();

                        // Remember where we started
                        mLastTouchX = x;
                        mLastTouchY = y;
                        break;
                    }
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP: {
                        if (flagmove || pinch!=0) {
                            if(flagmove == false){
                                pinch--;
                            }
                            flagmove = false;
                        } else { //danh o day
                            cx = event.getX();
                            cy = event.getY();
                            String msg = drawView.check(cx, cy);
                            if (((!toggleMode && drawView.getCurState() == CheckedState.X)||(toggleMode && drawView.getCurState() == CheckedState.O)) && !drawView.isFinish() && msg != null) {//neu vi tri hop le
                                Node node = drawView.createNode(cx, cy);
                                drawView.setCheckedStates(node);
                                drawView.invalidate();
                                //check won?
                                if (drawView.isFinish()) {
                                    //win
//                                    showAlertDialog();
                                    mHandler.obtainMessage(WIN).sendToTarget();
                                    Toast.makeText(PlayWithBot.this, "win", Toast.LENGTH_SHORT).show();

                                } else {
                                    if(toggleMode)tmpBoard[node.getY()][node.getX()] = drawView.getCheckedStates()[node.getY()][node.getX()];
                                    else tmpBoard[node.getY()][node.getX()] = drawView.getCheckedStates()[node.getY()][node.getX()]==1?2:1;
                                    Thread run = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            drawView.clearNextStack();
                                            // not win -> bot play
                                            Node nodes = bot.getBestNode();
                                            nodes.swapnode();
                                            if(toggleMode)
                                            tmpBoard[nodes.getY()][nodes.getX()] = drawView.getCurState();
                                            else
                                            tmpBoard[nodes.getY()][nodes.getX()] = drawView.getCurState()==1?2:1;
                                            drawView.setCheckedStates(nodes);
                                            drawView.clearNextStack();
                                            drawView.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    drawView.invalidate();
                                                }
                                            });
                                            if (drawView.isFinish()) { // neu khong danh duoc ma game da ket thuc co nghia la thua
//                                                showAlertDialog();
                                                //lose
                                                mHandler.obtainMessage(LOSE).sendToTarget();
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
        txtCountDownTimer.setVisibility(View.INVISIBLE);


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
            pinch = 2;
            mScaleFactor = detector.getScaleFactor();
            space = Math.round(mScaleFactor * space);
            padding = Math.round(mScaleFactor * padding);

            space = space > 200 ? 200 : space;
            space = space < 80 ? 80 : space;
            drawView.setSpace(space, padding);
            return true;
        }
    }

    private void showAlertDialog(String title, String message) {

        final ConfirmDialog dialog = new ConfirmDialog(PlayWithBot.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setButton(ConfirmDialog.PositiveButton, "Ván mới", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.init();
                dialog.dismiss();
                if(changeturn){
                    toggleMode = toggleMode?false:true;
                    invalidateOptionsMenu();
                }
                resetboard();
                if(!toggleMode){
                    //nguoi danh sau
                    Node n = new Node(7,12);
                    drawView.setCheckedStates(n);

                }
                drawView.invalidate();
            }
        });

        dialog.setButton(ConfirmDialog.NegativeButton, "Thoát", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PlayWithBot.this.finish();
            }
        });

        dialog.show();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WIN:
                    showAlertDialog("Thông báo", "Bạn đã chiến thắng");
                    break;
                case LOSE:
                    showAlertDialog("Thông báo", "Bạn đã thua");
                    break;
                case CHANGE_ICON:
                    showAlertDialog("Thông báo", "Đổi lượt và Reset bàn cờ?");
                    break;
            }
        }
    };

    private void resetboard(){
          flagmove = false;
          pinch = 0;
          space = 100;
          padding = 10;
          mPosX = 0;
          mPosY = 0;
          changeturn = false;
        for(int i = 0 ; i < 30*30;i++){
            tmpBoard[i/30][i%30] = 0;
        }
        drawView.clearNextStack();
        drawView.clearPreStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bot, menu);
        MenuItem toggleModeItem = menu.findItem(R.id.change_icon);
        toggleModeItem.setTitle(toggleMode ? "O" : "X");
        toggleModeItem.setIcon(toggleMode ? R.drawable.ic_o_24dp : R.drawable.ic_x_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.undo:
                if(!toggleMode){
                    if (drawView.checkBotFirst()){
                        drawView.undo();
                        drawView.undo();
                    }
                }
                else {
                    drawView.undo();
                    drawView.undo();
                }
                updatetmpboard();
                return true;
            case R.id.redo:
                drawView.redo();
                drawView.redo();
                updatetmpboard();
                return true;
            case R.id.change_icon:
                changeturn = true;
                mHandler.obtainMessage(CHANGE_ICON).sendToTarget();
                return true;
            case R.id.option:

        }
        return false;

    }
    private void updatetmpboard(){
        for(int i = 0 ; i < 30*30;i++){
            if(toggleMode)
            this.tmpBoard[i/30][i%30] = drawView.getCheckedStates()[i/30][i%30];
            else{
                if (drawView.getCheckedStates()[i/30][i%30]==0) this.tmpBoard[i/30][i%30]=0;
                else tmpBoard[i/30][i%30] = drawView.getCheckedStates()[i/30][i%30]==1?2:1;
            }
        }
    }

}