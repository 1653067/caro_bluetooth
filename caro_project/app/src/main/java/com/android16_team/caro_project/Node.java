package com.android16_team.caro_project;

public class Node {
    private int x;
    private int y;
    private int value;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }



    public Node(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    public void swapnode(){
        int tmp = this.x;
        this.x = this.y;
        this.y = tmp;
    }

    public int getX () {
        return this.x;
    }
    public  int getY(){
        return this.y;
    }
    public  int getValue(){
        return  this.value;
    }
}
