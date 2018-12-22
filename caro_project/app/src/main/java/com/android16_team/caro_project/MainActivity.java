package com.android16_team.caro_project;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button playWithBot;
    Button playWithFriend;
    Button exit;
    Button btnOptions;
    ImageButton setUp;
    Context context = this;
    OptionDialog optionDialog;
    InfoPlay infoPlay;
    private Intent musicBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playWithBot = findViewById(R.id.btn_playWithBot);
        playWithFriend = findViewById(R.id.btn_playWithFriend);
        exit = findViewById(R.id.btn_exit_main);
        setUp = findViewById(R.id.btn_setting_main);
        btnOptions = findViewById(R.id.btnOptions);

        playWithBot.setOnClickListener(this);
        playWithFriend.setOnClickListener(this);
        exit.setOnClickListener(this);
        setUp.setOnClickListener(this);
        btnOptions.setOnClickListener(this);

        infoPlay = InfoPlay.getInstance();
        readData();
        startService(new Intent(this, MusicService.class));
        optionDialog = new OptionDialog(context, infoPlay);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_playWithBot:
                    Intent intentBot = new Intent(MainActivity.this, PlayWithBot.class);
                    startActivity(intentBot);

                break;
            case R.id.btn_playWithFriend:
                Intent intentFriends = new Intent(MainActivity.this, PlayWithFriend.class);
                startActivity(intentFriends);
                break;
            case R.id.btn_exit_main:
                finish();
                break;
            case R.id.btn_setting_main:
                Toast.makeText(this,"set up",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnOptions:
                optionDialog.show();
                break;
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if ((resultCode == MainActivity.RESULT_OK)) {
//                Bundle myResultBundle = data.getExtras();
//                int exit = myResultBundle.getInt("exit");
//                if (exit == 1) finish();
//            }
//        }
//        catch(Exception e){
//
//        }
//    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
        stopService(new Intent(this, MusicService.class));
    }

    private void saveData() {
        FileOutputStream os = null;
        try {
            os = openFileOutput("InfoPlay", MODE_PRIVATE);
            os.write(infoPlay.toString().getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        FileInputStream in = null;
        try {
            in = openFileInput("InfoPlay");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            infoPlay.setInfoPlay(bufferedReader.readLine());
            bufferedReader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
