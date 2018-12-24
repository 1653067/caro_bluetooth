package com.android16_team.caro_project;

import android.icu.text.IDNA;

import java.io.Serializable;

public class InfoPlay implements Serializable {
    private boolean haveStone;
    private boolean haveSwapTurn;
    private int noStones;
    private int noTurns;
    private String name;
    private boolean music;
    private boolean sound;
    private boolean effect;

    public boolean isEffect() {
        return effect;
    }

    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    private static InfoPlay instance = null;

    private InfoPlay() {
        haveStone = false;
        haveSwapTurn = false;
        music = true;
        sound = true;
        effect = true;
        noStones = 0;
        noTurns = 0;
        name = "Người chơi";
    }

    public static InfoPlay getInstance() {
        if(instance == null) {
            instance = new InfoPlay();
        }
        return instance;
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

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public void setInfoPlay(String data) {
        String []items = data.split(";");
        if(items.length == 8) {
            instance.setName(items[0]);
            instance.setHaveStone(Boolean.parseBoolean(items[1]));
            instance.setHaveSwapTurn(Boolean.parseBoolean(items[2]));
            instance.setMusic(Boolean.parseBoolean(items[3]));
            instance.setSound(Boolean.parseBoolean(items[4]));
            instance.setEffect(Boolean.parseBoolean(items[5]));
            instance.setNoStones(Integer.parseInt(items[6]));
            instance.setNoTurns(Integer.parseInt(items[7]));
        }
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s;%d;%d", name, haveStone, haveSwapTurn, music, sound, effect, noStones, noTurns);
    }
}
