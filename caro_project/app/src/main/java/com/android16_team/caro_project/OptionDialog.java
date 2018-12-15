package com.android16_team.caro_project;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

public class OptionDialog extends Dialog {

    private InfoPlay infoPlay;

    public OptionDialog(Context context, InfoPlay infoPlay) {
        super(context);
        this.infoPlay = infoPlay;
        this.setContentView(R.layout.options_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
