package com.lamesa.lugu.receiver;

import static com.lamesa.lugu.activity.act_main.mediaNotificationManager;
import static com.lamesa.lugu.activity.act_main.musicPlayer;
import static com.lamesa.lugu.otros.metodos.GuardarCancionFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lamesa.lugu.R;
import com.lamesa.lugu.otros.TinyDB;
import com.lamesa.lugu.player.MediaNotificationManager;


public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent intent) {
        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

        String action = intent.getAction();

        switch (action) {
            case MediaNotificationManager.ACTION_STOP:
                ActionStop();
                break;

            case MediaNotificationManager.ACTION_FAVORITE:
                ActionFavorito(mContext);
                //	Toast.makeText(mContext, MediaNotificationManager.ACTION_FAVORITE, Toast.LENGTH_SHORT).show();
                break;

            case MediaNotificationManager.ACTION_PLAY:
                Toast.makeText(mContext, mContext.getResources().getString(R.string.reproduciendo), Toast.LENGTH_SHORT).show();
                ActionPlay(mContext);

                break;

            case MediaNotificationManager.ACTION_PAUSE:
                Toast.makeText(mContext, mContext.getResources().getString(R.string.pausando), Toast.LENGTH_SHORT).show();
                ActionPause(mContext);
                break;

        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void ActionPause(Context mContext) {
        if (musicPlayer != null) {
            musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PAUSE);
        }
    }

    private void ActionPlay(Context mContext) {
        if (musicPlayer != null) {
            musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PLAY);
        }
    }

    private void ActionStop() {
        if (mediaNotificationManager != null) {
            mediaNotificationManager.cancelNotify();
            if (musicPlayer != null) {
                musicPlayer.PlayOrPause(MediaNotificationManager.STATE_STOP);
            }
        }
    }

    private void ActionFavorito(Context mContext) {
        TinyDB tinyDB = new TinyDB(mContext);
        GuardarCancionFavoritos(mContext, tinyDB.getString(TBidCancionSonando), true);
    }

}
