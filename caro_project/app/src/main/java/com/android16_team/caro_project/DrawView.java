package com.android16_team.caro_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Stack;

public class DrawView extends View {

    private Paint paint = new Paint();
    private int[][] checkedStates = new int[100][100];

    private Stack<Node> preStack;
    private Stack<Node> nextStack;
    private int size = 100;
    private int space;
    private int height;
    private int width;
    private int nCols;
    private int nRows;
    private boolean enabled = true;
    private int curState = CheckedState.O;
    private boolean finish = false;


    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        for (int i = 0; i < 100; i++)
            for (int j = 0; j < 100; j++)
                checkedStates[i][j] = CheckedState.NONE;
        checkedStates[8][7] = CheckedState.O;
        checkedStates[7][7] = CheckedState.X;
    }

    public int[][] getCheckedStates() {
        return checkedStates;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        space = 100;
        height = canvas.getHeight();
        width = canvas.getWidth();
        nCols = canvas.getWidth() / space + 1;
        nRows = canvas.getHeight() / space + 1;
        Drawable d = getResources().getDrawable(R.drawable.bg_board, null);
//as getDrawable(int drawable) is deprecated
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        canvas.drawARGB(76, 255, 255, 255);
        paint.setColor(Color.rgb(51, 51, 51));
        paint.setStrokeWidth(5);
        //vẽ dọc
        for (int i = 0; i < nCols; i++) {
            canvas.drawLine(i * space, 0, i * space, height, paint);
        }

        //Vẽ ngang
        for (int i = 0; i < nRows; i++) {
            canvas.drawLine(0, i * space, width, i * space, paint);
        }

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (checkedStates[i][j] != CheckedState.NONE) {
                    Node node = new Node(i, j);
                    paintCheckedState(canvas, checkedStates[i][j], node);
                }
            }
        }
    }


    // paint icon at position node with checkedState
    private void paintCheckedState(Canvas canvas, int checkedState, Node node) {
        int padding = 20;
        if (checkedState == CheckedState.O) {
            paint.setColor(Color.rgb(255, 0, 0));
            paint.setStyle(Paint.Style.STROKE);
            int radius = space / 2 - padding + 5;
            canvas.drawCircle(node.getY() * space + space / 2, node.getX() * space + space / 2, radius, paint);
//            paint.setColor(Color.rgb(255, 255, 255));
//            canvas.drawCircle(node.getY() * space + space / 2, node.getX() * space + space / 2, radius - 10, paint);
            paint.setStyle(Paint.Style.FILL);
        } else if (checkedState == CheckedState.X) {
            paint.setStrokeWidth(10);
            paint.setColor(Color.rgb(0, 0, 255));
            canvas.drawLine(node.getY() * space + padding, node.getX() * space + padding, node.getY() * space + space - padding, node.getX() * 100 + 100 - padding, paint);
            canvas.drawLine(node.getY() * space + space - padding, node.getX() * space + padding, node.getY() * space + padding, node.getX() * 100 + 100 - padding, paint);
        }
    }

    public void setCheckedState(Float x, Float y) {
        int i = (int) (y / space);
        int j = (int) (x / space);
        if (checkedStates[i][j] == CheckedState.NONE) {
            checkedStates[i][j] = curState;
            curState = curState == CheckedState.O ? CheckedState.X : CheckedState.O;
        }
    }

    public void setCheckedStates(Node node) {
        if (checkedStates[node.getY()][node.getX()] == CheckedState.NONE) {
            checkedStates[node.getY()][node.getX()] = curState;
            if (checkWin(node)) {
                this.finish = true;
            }
            curState = curState == CheckedState.O ? CheckedState.X : CheckedState.O;
            enabled = true;
        }
        invalidate();
    }

    public String check(Float x, Float y) {
        int i = (int) (y / space);
        int j = (int) (x / space);
        if (enabled && checkedStates[i][j] == CheckedState.NONE) {
            return i + " " + j;
        }
        return null;
    }

    public Node createNode(Float x, Float y) {
        int i = (int) (y / space);
        int j = (int) (x / space);
        Node node = new Node(j, i);
        return node;
    }

    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }

    public void clearNextStack() {
        if (this.nextStack != null)
            this.nextStack.clear();
    }

    public boolean isFinish() {
        return finish;
    }

    public boolean checkValid(int y, int x) {
        return x > -1 && x < this.size && y > -1 && y < this.size;
    }

    public boolean checkWin(Node node) {
        int x = node.getX(), y = node.getY();
        // dọc
        int count1, count2;
        count1 = 1;
        count2 = 0;
        for (int i = 1; i < 7; i++) {
            if (checkValid(y - i, x)) {
                if (this.checkedStates[y - i][x] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y - i][x] != this.curState && this.checkedStates[y - i][x] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        for (int i = 1; i < 7; i++) {
            if (checkValid(y + i, x)) {
                if (this.checkedStates[y + i][x] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y + i][x] != this.curState && this.checkedStates[y - i][x] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        if (count1 == 5 && count2 < 2) {
            return true;
        }

        // ngang
        count1 = 1;
        count2 = 0;

        for (int i = 1; i < 7; i++) {
            if (checkValid(y, x - i)) {
                if (this.checkedStates[y][x - i] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y][x - i] != this.curState && this.checkedStates[y][x - i] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        for (int i = 1; i < 7; i++) {
            if (checkValid(y, x + i)) {
                if (this.checkedStates[y][x + i] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y][x + i] != this.curState && this.checkedStates[y][x + i] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        if (count1 == 5 && count2 < 2) {
            return true;
        }

        // chéo \

        count1 = 1;
        count2 = 0;

        for (int i = 1; i < 7; i++) {
            if (checkValid(y - i, x - i)) {
                if (this.checkedStates[y - i][x - i] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y - i][x - i] != this.curState && this.checkedStates[y - i][x - i] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        for (int i = 1; i < 7; i++) {
            if (checkValid(y + i, x + i)) {
                if (this.checkedStates[y + i][x + i] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y + i][x + i] != this.curState && this.checkedStates[y + i][x + i] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        if (count1 == 5 && count2 < 2) {
            return true;
        }

        // chéo /

        count1 = 1;
        count2 = 0;

        for (int i = 1; i < 7; i++) {
            if (checkValid(y - i, x + i)) {
                if (this.checkedStates[y - i][x + i] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y - i][x + i] != this.curState && this.checkedStates[y - i][x + i] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        for (int i = 1; i < 7; i++) {
            if (checkValid(y + i, x - i)) {
                if (this.checkedStates[y + i][x - i] == this.curState) {
                    count1++;
                } else {
                    if (this.checkedStates[y + i][x - i] != this.curState && this.checkedStates[y + i][x - i] != CheckedState.NONE) {
                        count2++;
                    }
                    break;
                }
            }
        }

        if (count1 == 5 && count2 < 2) {
            return true;
        }

        return false;
    }

    void resetBoard() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                checkedStates[i][j] = CheckedState.NONE;
            }
        }

        finish = false;
        invalidate();
    }
}
