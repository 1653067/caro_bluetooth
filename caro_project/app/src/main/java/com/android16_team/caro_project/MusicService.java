package com.android16_team.caro_project;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private BroadcastReceiver receiver;
    public static final String OPTION_MUSIC = "MusicService";
    public static final int STOP = 0;
    public static final int PLAY = 1;
    public static final int NONE = -1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
        mediaPlayer.setLooping(true);
        IntentFilter filter = new IntentFilter("com.android16_team.caro_project.MusicService");
        receiver = new MusicBroadcastReceiver();
        registerReceiver(receiver, filter);

        if(InfoPlay.getInstance().isMusic()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        unregisterReceiver(receiver);
    }

    public class MusicBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int option = intent.getIntExtra(OPTION_MUSIC, NONE);
            switch (option) {
                case STOP:
                    mediaPlayer.pause();
                    break;
                case PLAY:
                    mediaPlayer.start();
                    break;
            }
        }
    }
}
