package com.android16_team.caro_project;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfirmDialog extends Dialog {

    private TextView txtTitle, txtMessage;
    private Button btnPositive, btnNegative;
    public static final int PositiveButton = 0;
    public static final int NegativeButton = 1;

    public ConfirmDialog(Context context) {
        super(context);
        this.setContentView(R.layout.alert_dialog);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtTitle = this.findViewById(R.id.dialogTitle);
        txtMessage = this.findViewById(R.id.dialogMessage);
        btnPositive = this.findViewById(R.id.btnPositive);
        btnNegative = this.findViewById(R.id.btnNegative);

        btnPositive.setVisibility(View.GONE);
        btnNegative.setVisibility(View.GONE);
    }

    public void setButton(int which, String text, View.OnClickListener l) {
        switch (which) {
            case PositiveButton:
                btnPositive.setVisibility(View.VISIBLE);
                btnPositive.setText(text);
                btnPositive.setOnClickListener(l);
                break;
            case NegativeButton:
                btnNegative.setVisibility(View.VISIBLE);
                btnNegative.setText(text);
                btnNegative.setOnClickListener(l);
                break;
        }
    }

    public void setMessage(String message) {
        txtMessage.setText(message);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }


}
