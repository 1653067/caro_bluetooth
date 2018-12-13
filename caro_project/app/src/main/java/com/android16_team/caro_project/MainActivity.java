package com.android16_team.caro_project;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button playWithBot;
    Button playWithFriend;
    Button exit;
    ImageButton setUp;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playWithBot = findViewById(R.id.btn_playWithBot);
        playWithFriend = findViewById(R.id.btn_playWithFriend);
        exit = findViewById(R.id.btn_exit_main);
        setUp = findViewById(R.id.btn_setting_main);

        playWithBot.setOnClickListener(this);
        playWithFriend.setOnClickListener(this);
        exit.setOnClickListener(this);
        setUp.setOnClickListener(this);

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
                startActivity(new Intent(MainActivity.this, PlayWithFriend.class));
                break;
            case R.id.btn_exit_main:
                finish();
                break;
            case R.id.btn_setting_main:
                Toast.makeText(this,"set up",Toast.LENGTH_SHORT).show();
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
