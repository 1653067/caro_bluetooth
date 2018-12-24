package com.android16_team.caro_project;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

public class DrawTool {
    public static void drawToolBarBg(Context context, Toolbar toolbar) {
        Picture picture = new Picture();
        Activity activity = (Activity) context;
        int wToolbar = activity.getWindowManager().getDefaultDisplay().getWidth();
        int hToolbar = 56;
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            hToolbar = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        Canvas canvas = picture.beginRecording(wToolbar, hToolbar);
        Paint p = new Paint();
        canvas.drawColor(Color.rgb(28, 167, 225));
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            Drawable dSnow = activity.getResources().getDrawable(R.drawable.snow, null);

            int x = rnd.nextInt(wToolbar);
            int y = rnd.nextInt(hToolbar);
            int space = (rnd.nextInt(30) + 10) * 2;

            dSnow.setBounds(x, y, x + space, y + space);

            dSnow.draw(canvas);
        }
        picture.endRecording();
        Drawable d = new PictureDrawable(picture);
        toolbar.setBackground(d);
    }

    public static void drawCaroBg(Context context, View view) {
        InfoPlay infoPlay = InfoPlay.getInstance();
        if(infoPlay.isEffect()) {
            Picture picture = new Picture();
            Activity activity = (Activity) context;
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            Canvas canvas = picture.beginRecording(size.x, size.y);
            Drawable d = context.getResources().getDrawable(R.drawable.bg, null);
            d.setBounds(0, 0, 465 * canvas.getHeight() / 305, canvas.getHeight());
            d.draw(canvas);
            picture.endRecording();
            view.setBackground(new PictureDrawable(picture));
        } else {
            view.setBackground(new ColorDrawable(Color.WHITE));
        }
    }
}
