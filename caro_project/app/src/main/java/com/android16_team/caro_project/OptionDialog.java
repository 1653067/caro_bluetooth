package com.android16_team.caro_project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.OutputStream;

import static android.content.Context.MODE_APPEND;

public class OptionDialog extends Dialog implements View.OnClickListener {

    private InfoPlay infoPlay;
    private EditText txtName, txtStones, txtChangeTurn;
    private Button btnMinusStone, btnPlusStone, btnMinusTurn, btnPlusTurn, btnApply, btnCancel;
    private CheckBox cbStone, cbChange, cbMusic, cbSound, cbEffect;

    private Context context;

    public OptionDialog(Context context, InfoPlay infoPlay) {
        super(context);
        this.infoPlay = infoPlay;
        this.setContentView(R.layout.options_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtName = this.findViewById(R.id.txtName);
        txtChangeTurn = this.findViewById(R.id.txtChange);
        txtStones = this.findViewById(R.id.txtStones);

        btnMinusStone = this.findViewById(R.id.btnMinusStone);
        btnPlusStone = this.findViewById(R.id.btnPlusStone);

        btnMinusTurn = this.findViewById(R.id.btnMinusTurn);
        btnPlusTurn = this.findViewById(R.id.btnPlusTurn);

        btnApply = this.findViewById(R.id.btnApply);
        btnCancel = this.findViewById(R.id.btnCancel);

        cbStone = this.findViewById(R.id.cbStone);
        cbChange = this.findViewById(R.id.cbChangeTurn);
        cbMusic = this.findViewById(R.id.cbMusic);
        cbSound = this.findViewById(R.id.cbSound);
        cbEffect = this.findViewById(R.id.cbEffect);

        btnMinusStone.setOnClickListener(this);
        btnPlusStone.setOnClickListener(this);
        btnMinusTurn.setOnClickListener(this);
        btnPlusTurn.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtName.setText(infoPlay.getName());
        txtStones.setText(String.valueOf(infoPlay.getNoStones()));
        txtChangeTurn.setText(String.valueOf(infoPlay.getNoTurns()));

        cbStone.setChecked(infoPlay.isHaveStone());
        cbChange.setChecked(infoPlay.isHaveSwapTurn());
        cbMusic.setChecked(infoPlay.isMusic());
        cbSound.setChecked(infoPlay.isSound());
        cbEffect.setChecked(infoPlay.isEffect());

        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMinusTurn:
            {
                Integer noTurn = Integer.parseInt(txtChangeTurn.getText().toString());
                if (noTurn > 0) {
                    noTurn--;
                    txtChangeTurn.setText(noTurn.toString());
                }
                break;
            }
            case R.id.btnPlusTurn:
            {
                Integer noTurn = Integer.parseInt(txtChangeTurn.getText().toString());
                if (noTurn < 100) {
                    noTurn++;
                    txtChangeTurn.setText(noTurn.toString());
                }
                break;
            }

            case R.id.btnPlusStone:
            {
                Integer noStone = Integer.parseInt(txtStones.getText().toString());
                if (noStone < 100) {
                    noStone++;
                    txtStones.setText(noStone.toString());
                }
                break;
            }

            case R.id.btnMinusStone:
            {
                Integer noStone = Integer.parseInt(txtStones.getText().toString());
                if (noStone > 0) {
                    noStone--;
                    txtStones.setText(noStone.toString());
                }
                break;
            }

            case R.id.btnApply:
                infoPlay.setName(txtName.getText().toString());
                infoPlay.setHaveStone(cbStone.isChecked());
                infoPlay.setHaveSwapTurn(cbChange.isChecked());
                infoPlay.setSound(cbSound.isChecked());
                infoPlay.setMusic(cbMusic.isChecked());
                infoPlay.setEffect(cbEffect.isChecked());
                infoPlay.setNoStones(Integer.parseInt(txtStones.getText().toString()));
                infoPlay.setNoTurns(Integer.parseInt(txtChangeTurn.getText().toString()));

                Intent intent = new Intent("com.android16_team.caro_project.MusicService");
                int option = cbMusic.isChecked() ? MusicService.PLAY : MusicService.STOP;
                intent.putExtra(MusicService.OPTION_MUSIC, option);
                context.sendBroadcast(intent);

                ((Activity)context).findViewById(R.id.snowfall).setVisibility(infoPlay.isEffect() ? View.VISIBLE : View.GONE);

                this.dismiss();
                break;
            case R.id.btnCancel:
                update();
                this.dismiss();
                break;
        }
    }

    public void update() {
        txtName.setText(infoPlay.getName());
        txtStones.setText(String.valueOf(infoPlay.getNoStones()));
        txtChangeTurn.setText(String.valueOf(infoPlay.getNoTurns()));

        cbStone.setChecked(infoPlay.isHaveStone());
        cbChange.setChecked(infoPlay.isHaveSwapTurn());
        cbMusic.setChecked(infoPlay.isMusic());
        cbSound.setChecked(infoPlay.isSound());
        cbEffect.setChecked(infoPlay.isEffect());
    }

    @Override
    public void show() {
        update();
        super.show();
    }
}
