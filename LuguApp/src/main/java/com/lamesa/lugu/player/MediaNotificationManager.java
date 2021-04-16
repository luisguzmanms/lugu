package com.lamesa.lugu.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.lamesa.lugu.R;
import com.lamesa.lugu.activity.act_main;
import com.lamesa.lugu.otros.TinyDB;
import com.lamesa.lugu.receiver.ActionReceiver;

import java.util.Random;

import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;


public class MediaNotificationManager {


    public static final String STATE_PLAY = "STATE_PLAY";
    public static final String STATE_PAUSE = "STATE_PAUSE";
    public static final String STATE_STOP = "STATE_STOP";
    public static final String STATE_BUFFERING = "STATE_BUFFERING";
    public static final String STATE_READY = "STATE_READY";


    public static final String ACTION_FAVORITE = "ACTION_FAVORITE";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static MediaControllerCompat.TransportControls transportControls;
    public static NotificationManager manager;
    private static Context mContext;
    private static MediaSessionCompat mediaSession;
    private static NotificationManagerCompat notificationManager;
    public final int NOTIFICATION_ID = 25014;
    private final String PRIMARY_CHANNEL = "PRIMARY_ID";
    private final String strAppName;
    private final Resources resources;
    private String PRIMARY_CHANNEL_NAME = "LOFI RADIO";
    private String strCancionNombre;
    private MediaSessionCompat.Callback mediasSessionCallback;
    private NotificationChannel channel;


    public MediaNotificationManager(Context mContext) {

        MediaNotificationManager.mContext = mContext;
        this.resources = mContext.getResources();

        strAppName = resources.getString(R.string.app_name);
        PRIMARY_CHANNEL_NAME = strAppName;
        notificationManager = NotificationManagerCompat.from(mContext);
        TinyDB tinyDB = new TinyDB(mContext);

    }

    public void startNotify(String playbackStatus) {

        // large icon
        final Bitmap[] bitmapIcon = {BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)};


        int icon = R.drawable.ic_pause_white;
        Intent intentPlayPause = new Intent(mContext, ActionReceiver.class);
        intentPlayPause.setAction(STATE_PAUSE);
        PendingIntent actionPlayPause = PendingIntent.getBroadcast(mContext, 1, intentPlayPause, 0);


        String contentTitle = tinyDB.getString(TBnombreCancionSonando);
        String contentText = tinyDB.getString(TBartistaCancionSonando);

        //image = Bitmap.CreateScaledBitmap(image, (int)(image.Width * multiplier), (int)(image.Height * multiplier), false);
        switch (playbackStatus) {
            case STATE_PLAY:
                setLogInfo(mContext, "MediaNotificationManager.startNotify", "STATE_PLAY", false);
                icon = R.drawable.ic_pause_white;
                intentPlayPause.setAction(ACTION_PAUSE);
                actionPlayPause = PendingIntent.getBroadcast(mContext, 1, intentPlayPause, 0);
                break;
            case STATE_BUFFERING:
                setLogInfo(mContext, "MediaNotificationManager.startNotify", "STATE_BUFFERING", false);
                contentTitle = "Cargando...";
                contentText = "...";
                break;
            case STATE_PAUSE:
                setLogInfo(mContext, "MediaNotificationManager.startNotify", "STATE_PAUSE", false);
                icon = R.drawable.ic_play_white;
                intentPlayPause.setAction(ACTION_PLAY);
                actionPlayPause = PendingIntent.getBroadcast(mContext, 2, intentPlayPause, 0);
                break;
            case STATE_STOP:
                setLogInfo(mContext, "MediaNotificationManager.startNotify", "STATE_STOP", false);

                // this.cancelNotify();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + playbackStatus);
        }


        //region media session
        mediaSession = new MediaSessionCompat(mContext, mContext.getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, tinyDB.getString(TBartistaCancionSonando))
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, tinyDB.getString(TBnombreCancionSonando)) // strLiveBroadcast
                .build());
        mediaSession.setCallback(mediasSessionCallback);

        //endregion


        Intent stopIntent = new Intent(mContext, ActionReceiver.class);
        stopIntent.setAction(MediaNotificationManager.ACTION_STOP);
        PendingIntent stopAction = PendingIntent.getBroadcast(mContext, 3, stopIntent, 0);


        // intent de clic favorito
        Intent intentFavorito = new Intent(mContext, ActionReceiver.class);
        intentFavorito.setAction(MediaNotificationManager.ACTION_FAVORITE);
        PendingIntent actionFavorito = PendingIntent.getBroadcast(mContext, 3, intentFavorito, 0);


        Intent intentMainActivity = new Intent(mContext, act_main.class);
        intentMainActivity.setAction(Intent.ACTION_MAIN);
        intentMainActivity.addCategory(Intent.CATEGORY_LAUNCHER);
        // para que no se creee una activity nueva si ya está creada
        intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, new Random().nextInt(), intentMainActivity, 0);


        // notificationManager.cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);

        }

        // Load bitmap from image url on background thread and display image notification

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, PRIMARY_CHANNEL);
        builder.setAutoCancel(false);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setContentInfo("content");
        builder.setContentIntent(pendingIntent);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setSmallIcon(R.drawable.ic_delfin);
        builder.setSound(null);
        builder.setOngoing(true);
        builder.setNotificationSilent();
        builder.addAction(icon, "play", actionPlayPause);
        builder.addAction(R.drawable.ic_favorite_black_24dp, "favorite", actionFavorito);
        builder.addAction(R.drawable.ic_stop_white, "stop", stopAction);
        builder.setPriority(NotificationCompat.PRIORITY_LOW);
        builder.setWhen(System.currentTimeMillis());
        builder.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSession.getSessionToken())
                .setShowActionsInCompactView(0, 1, 2)
                .setShowCancelButton(true)
                .setCancelButtonIntent(stopAction));

        // this.startForeground(NOTIFICATION_ID, builder.build());

        //region cargar imagen en notification

        /*
        Glide.with(mContext)
                .asBitmap()
                .load("https://cdnb.artstation.com/p/assets/images/images/022/740/889/large/hiromi-_11-concept3-min.jpg?1576528739")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setLogInfo(mContext,"MediaNotificationManager.startNotify.onResourceReady","Cargar imagen en Notificacion",false);
                        largeIcon[0] = resource;
                        // TODO Do some work: pass this bitmap
                        Bitmap bitmapIcon = Bitmap.createScaledBitmap(largeIcon[0], 150, 120, false);
                        builder.setLargeIcon(bitmapIcon);
                       notificationManager.notify(String.valueOf(NOTIFICATION_ID), 1, builder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        setLogInfo(mContext,"MediaNotificationManager.startNotify.onLoadCleared","Cargar imagen en Notificacion",false);
                    }
                });

         */

        //endregion

        // mostrar notificacion solo si se cargo los datos de la cancion en tinydb
        if (!tinyDB.getString(TBnombreCancionSonando).isEmpty() && !tinyDB.getString(TBartistaCancionSonando).isEmpty()) {
            notificationManager.notify(String.valueOf(NOTIFICATION_ID), 1, builder.build());
        }


    }

    public void cancelNotify() {
        setLogInfo(mContext, "MediaNotificationManager.cancelNotify.", "Cancelar notificación", false);

        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager != null) {
                manager.cancelAll();
            }
        }
    }

}
