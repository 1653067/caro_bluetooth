package com.android16_team.caro_project;

import android.icu.text.IDNA;

import java.io.Serializable;

public class InfoPlay implements Serializable {
    private boolean haveStone;
    private boolean haveSwapTurn;
    private int noStones;
    private int noTurns;
    private String name;

    public InfoPlay() {
        haveStone = false;
        haveSwapTurn = false;
        noStones = 0;
        noTurns = 0;
        name = "Người chơi";
    }

    public boolean isHaveStone() {
        return haveStone;
    }

    public void setHaveStone(boolean haveStone) {
        this.haveStone = haveStone;
    }

    public boolean isHaveSwapTurn() {
        return haveSwapTurn;
    }

    public void setHaveSwapTurn(boolean haveSwapTurn) {
        this.haveSwapTurn = haveSwapTurn;
    }

    public int getNoStones() {
        return noStones;
    }

    public void setNoStones(int noStones) {
        this.noStones = noStones;
    }

    public int getNoTurns() {
        return noTurns;
    }

    public void setNoTurns(int noTurns) {
        this.noTurns = noTurns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
