package com.android16_team.caro_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button play;
    Button exit;
    ImageButton bluetooth;
    ImageButton setUp;
    RadioButton playWithBot;
    RadioButton playWithFriend;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = findViewById(R.id.btn_play);
        exit = findViewById(R.id.btn_exit_main);
        bluetooth = findViewById(R.id.btn_bluetooth_main);
        setUp = findViewById(R.id.btn_setting_main);
        playWithBot = findViewById(R.id.radio_play_bot);
        playWithFriend = findViewById(R.id.radio_play_man);

        play.setOnClickListener(this);
        exit.setOnClickListener(this);
        setUp.setOnClickListener(this);
        bluetooth.setOnClickListener(this);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                if (playWithBot.isChecked()==true){
                    //open activity play with bot
                }
                else if(playWithFriend.isChecked()==true){
                    //open activity play with friend
                    Intent intent = new Intent(MainActivity.this, PlayWithFriend.class);
                    startActivity(intent);
                }

                break;
            case R.id.btn_exit_main:
                finish();
                break;
            case R.id.btn_setting_main:
                Toast.makeText(this,"set up",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_bluetooth_main:
                Toast.makeText(this,"bluetooth",Toast.LENGTH_SHORT).show();
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


}
