package com.android16_team.caro_project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import java.util.Stack;

public class DrawView extends View {

    private Paint paint = new Paint();
    private int[][] checkedStates = new int[30][30];

    private Stack<Node> preStack;
    private Stack<Node> nextStack;
    private int size = 30;
    private int space = 100;
    int padding = 10;
    private int height;
    private int width;
    private int nCols;
    private int nRows;
    private boolean enabled = true;
    private int curState = CheckedState.O;
    private boolean finish = false;
    private int dx = 0;
    private int dy = 0;

    private int curX = -1;
    private int curY = -1;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                checkedStates[i][j] = CheckedState.NONE;

        finish = false;
    }

    public int[][] getCheckedStates() {
        return checkedStates;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        height = canvas.getHeight();
        width = canvas.getWidth();
        nCols = canvas.getWidth() / space + 1;
        nRows = canvas.getHeight() / space + 1;
        paint.setColor(Color.rgb(191, 203, 209));
        paint.setStrokeWidth(5);
        //vẽ dọc
        for (int i = 0; i < 30; i++) {
            canvas.drawLine(i * space + dx  , 0, i * space + dx , height, paint);
        }

        //Vẽ ngang
        for (int i = 0; i < 30; i++) {
            canvas.drawLine(0, i * space +dy , width, i * space +dy , paint);
        }

        if(curX != -1 && curY != -1) {
            paint.setColor(Color.argb(25, 0, 0, 0));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(new Rect(
                    curX * space + dx,
                    curY * space + dy,
                    curX * space + space + dx,
                    curY * space + space + dy), paint);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (checkedStates[i][j] != CheckedState.NONE) {
                    Node node = new Node(i,j);
                    paintCheckedState(canvas, checkedStates[i][j], node );
                }
            }
        }

        int stonePadding = 10;

//        Drawable d = getResources().getDrawable(R.drawable.ic_stone, null);
//        //as getDrawable(int drawable) is deprecated
//        d.setBounds(8 * space + dx + stonePadding ,
//                7 * space + dy + stonePadding,
//                8*space + space + dx - stonePadding ,
//                7*space + space  + dy -stonePadding );
//        d.draw(canvas);
    }



    // paint icon at position node with checkedState
    private void paintCheckedState(Canvas canvas, int checkedState, Node node) {

        if (checkedState == CheckedState.O) {
            Drawable d = getResources().getDrawable(R.drawable.o_red, null);
            //as getDrawable(int drawable) is deprecated
            d.setBounds(node.getY() * space + dx ,
                    node.getX() * space + dy,
                    node.getY()*space + space + dx ,
                    node.getX()*space + space  + dy );
            d.draw(canvas);

//            paint.setColor(Color.rgb(255, 0, 0));
//            int radius = space / 2 - padding + 5;
//            canvas.drawCircle(node.getY() * space + space / 2 + dx, node.getX() * space + space / 2 + dy, radius, paint);
//            paint.setColor(Color.rgb(255, 255, 255));
//            canvas.drawCircle(node.getY() * space + space / 2+dx, node.getX() * space + space / 2+dy, radius - 10, paint);




        } else if(checkedState == CheckedState.X) {

            Drawable d = getResources().getDrawable(R.drawable.x_blue, null);
            //as getDrawable(int drawable) is deprecated
            d.setBounds(node.getY() * space + dx ,
                    node.getX() * space + dy,
                    node.getY()*space + space + dx ,
                    node.getX()*space + space  + dy);
            d.draw(canvas);
//            paint.setStrokeWidth(10);
//            paint.setColor(Color.rgb(0, 0, 255));
//            canvas.drawLine(node.getY()  * space + padding +dx, dy+node.getX() * space + padding, dx+node.getY() * space + space - padding, dy+node.getX() * 100 + 100 - padding, paint);
//            canvas.drawLine(node.getY() * space + space - padding+dx, dy+node.getX() * space + padding, dx+node.getY() * space + padding, dy+node.getX() * 100 + 100 - padding, paint);
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
            if(checkWin(node)) {
                this.finish = true;
            }
            curState = curState == CheckedState.O ? CheckedState.X : CheckedState.O;
            enabled = true;
            curX = node.getX();
            curY = node.getY();
        }
        // postInvalidate();

    }

    public String check(Float x, Float y) {
        int i = (int) ((y+-1*dy) / space);
        int j = (int) ((x+-1*dx) / space);
        if(enabled && checkedStates[i][j] == CheckedState.NONE) {
            return i + " " + j;
        }
        return null;
    }
    public Node createNode(Float x , Float y){
        int i = (int) ((y+-1*dy) / space);
        int j = (int) ((x+-1*dx) / space);
        Node node = new Node(j,i,0);
        return node;
    }

    public void setEnable(boolean enabled) {
        this.enabled = enabled;
    }

    public void clearNextStack (){
        if(this.nextStack != null)
            this.nextStack.clear();
    }

    public boolean isFinish(){
        return finish;
    }

    public boolean checkValid(int y, int x) {
        return x > -1 && x < this.size && y > -1 && y < this.size;
    }

    public int getCurState() {
        return curState;
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

    public void updatePosition(int x, int y){
        this.dx = x;
        this.dy = y;
        invalidate();
    }

    public void setSpace(int space, int padding) {
        this.space = space;
        this.padding = padding;
        invalidate();
    }

    public void changeState() {
        curState = curState == CheckedState.O ? CheckedState.X : CheckedState.O;
    }
}