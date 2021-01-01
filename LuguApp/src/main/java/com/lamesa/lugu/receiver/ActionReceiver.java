package com.lamesa.lugu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lamesa.lugu.player.MediaNotificationManager;

import static com.lamesa.lugu.activity.act_main.mediaNotificationManager;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.otros.metodos.GuardarCancionFavoritos;
import static com.lamesa.lugu.otros.metodos.PlayOrPause;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.player.MediaNotificationManager.transportControls;



public class ActionReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context mContext, Intent intent) {
		//Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();

		String action=intent.getAction();

		switch (action){
			case MediaNotificationManager.ACTION_STOP:
				ActionStop();
				Toast.makeText(mContext, MediaNotificationManager.ACTION_STOP, Toast.LENGTH_SHORT).show();
				transportControls.stop();
				break;

			case MediaNotificationManager.ACTION_FAVORITE:
				ActionFavorito(mContext);
				Toast.makeText(mContext, MediaNotificationManager.ACTION_FAVORITE, Toast.LENGTH_SHORT).show();
				break;

			case MediaNotificationManager.ACTION_PLAY:
				ActionPlay(mContext);
				Toast.makeText(mContext, MediaNotificationManager.ACTION_PLAY, Toast.LENGTH_SHORT).show();
				transportControls.play();
				break;

			case MediaNotificationManager.ACTION_PAUSE:
				ActionPause(mContext);
				Toast.makeText(mContext, MediaNotificationManager.ACTION_PAUSE, Toast.LENGTH_SHORT).show();
				transportControls.pause();
				break;

		}


		//This is used to close the notification tray
		/*
		Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		context.sendBroadcast(it);
		 */
	}

	private void ActionPause(Context mContext) {
		PlayOrPause(mContext,MediaNotificationManager.STATE_PAUSE);
		if(mediaNotificationManager!=null){
			mediaNotificationManager.startNotify(MediaNotificationManager.STATE_PAUSE);
		}
	}

	private void ActionPlay(Context mContext) {
		PlayOrPause(mContext,MediaNotificationManager.STATE_PLAY);
		if(mediaNotificationManager!=null){
			mediaNotificationManager.startNotify(MediaNotificationManager.STATE_PLAY);
		}
	}

	private void ActionStop() {
		if(mediaNotificationManager!=null){
			mediaNotificationManager.cancelNotify();
		}
	}

	private void ActionFavorito(Context mContext) {
		GuardarCancionFavoritos(mContext, tinyDB.getString(TBidCancionSonando), true);
	}



}
