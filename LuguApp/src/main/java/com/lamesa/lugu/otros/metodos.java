package com.lamesa.lugu.otros;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Logger;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.lugu.BuildConfig;
import com.lamesa.lugu.R;
import com.lamesa.lugu.activity.act_main;
import com.lamesa.lugu.adapter.AdapterFavoritos;
import com.lamesa.lugu.adapter.AdapterHistorial;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;
import com.lamesa.lugu.otros.numberPicker.NumberPicker;
import com.lamesa.lugu.otros.statics.Animacion;
import com.lamesa.lugu.player.MediaNotificationManager;
import com.naveed.ytextractor.ExtractorException;
import com.naveed.ytextractor.YoutubeStreamExtractor;
import com.naveed.ytextractor.model.YTMedia;
import com.naveed.ytextractor.model.YTSubtitles;
import com.naveed.ytextractor.model.YoutubeMeta;
import com.opencsv.CSVReader;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import okhttp3.OkHttpClient;

import static android.content.Context.POWER_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.activity.act_main.CargarImagenFondo;
import static com.lamesa.lugu.activity.act_main.bottomNavigationHis_Fav;
import static com.lamesa.lugu.activity.act_main.contenidoHome;
import static com.lamesa.lugu.activity.act_main.contenidoSearch;
import static com.lamesa.lugu.activity.act_main.getListas;
import static com.lamesa.lugu.activity.act_main.ivFondoGif;
import static com.lamesa.lugu.activity.act_main.ivLikeDislike;
import static com.lamesa.lugu.activity.act_main.ivOpcionBucle;
import static com.lamesa.lugu.activity.act_main.ivSleep;
import static com.lamesa.lugu.activity.act_main.ivStyle;
import static com.lamesa.lugu.activity.act_main.mAdapterFavoritos;
import static com.lamesa.lugu.activity.act_main.mAdapterHistorial;
import static com.lamesa.lugu.activity.act_main.mrvFavoritos;
import static com.lamesa.lugu.activity.act_main.mrvHistorial;
import static com.lamesa.lugu.activity.act_main.musicPlayer;
import static com.lamesa.lugu.activity.act_main.pbCargandoRadio;
import static com.lamesa.lugu.activity.act_main.soundVHS;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.activity.act_main.tvSleep;
import static com.lamesa.lugu.activity.splash.tinydb;
import static com.lamesa.lugu.otros.Firebase.EnviarSolicitud;
import static com.lamesa.lugu.otros.mob.inter.CargarInterAleatorio;
import static com.lamesa.lugu.otros.mob.video.createAndLoadRewardedAd;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_ALEATORIO;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_BUCLE;
import static com.lamesa.lugu.otros.statics.constantes.TBCategoriaAleatoria;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBfechaCambiosData;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBimagenFondo;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCanciones;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCategorias;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBlistHistorial;
import static com.lamesa.lugu.otros.statics.constantes.TBmodoReproductor;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnumeroCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBpoliticas;
import static com.lamesa.lugu.otros.statics.constantes.UrlAppPlayStore;
import static com.lamesa.lugu.otros.statics.constantes.UrlEncuestaSugerencia;
import static com.lamesa.lugu.otros.statics.constantes.UrlEnviarMiCancion;
import static com.lamesa.lugu.otros.statics.constantes.mixActualizarApp;
import static com.lamesa.lugu.otros.statics.constantes.mixCompartirApp;
import static com.lamesa.lugu.otros.statics.constantes.mixExtractionGoesWrong;
import static com.lamesa.lugu.otros.statics.constantes.mixFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.mixLogInfoError;
import static com.lamesa.lugu.otros.statics.constantes.mixPlaySong;
import static com.lamesa.lugu.otros.statics.constantes.setDebugActivo;


public class metodos {

    public static CountDownTimer countDownTimer;
    public static NumberPicker numberPicker;

    public static void DialogoReport(Context mContext) {


        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.title_report_song);
        String mensaje = mContext.getString(R.string.msg_report_song);

        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                .setOkButton("EMAIL", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.setData(Uri.parse("mailto:lugulofimusic@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "REPORT SONG");
                        intent.putExtra(Intent.EXTRA_TEXT, "Report:\n\n song: " + tinyDB.getString(TBnombreCancionSonando) + "\nartist: " + tinyDB.getString(TBartistaCancionSonando) + " \n------------------------------------\nMessage:\n\n\n\n\n\n\n\n\n\n\n\n------------------------------------\n\n\n\n\n");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }

                        return false;
                    }
                })
                // .setCancelButton(mContext.getString(R.string.cerrar))
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                .setCancelable(true)
                .show();

    }

    public static void DialogoOpBateria(Context mContext) {


        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.titulo_opBateria);
        String mensaje = mContext.getString(R.string.msg_opBateria);

        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) mContext.getApplicationContext().getSystemService(POWER_SERVICE);
            String packageName = "com.lugumusic.lofi";
            Intent i = new Intent();
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {

                MessageDialog.build((AppCompatActivity) mContext)
                        .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                        .setTitle(titulo).setMessage(mensaje)
                        .setOkButton(mContext.getString(R.string.activar), new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                i.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                                i.setData(Uri.parse("package:" + packageName));
                                mContext.startActivity(i);
                                return false;
                            }
                        })
                        .setCancelButton(mContext.getString(R.string.cerrar))
                        .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                        .setCancelable(true)
                        .show();
            }
        }

    }

    public static void CargarHome(Context mContext) {
        if (contenidoHome.getVisibility() == GONE) {
            contenidoSearch.startAnimation(Animacion.alpha_out(mContext));
            contenidoSearch.setVisibility(GONE);
        }
        contenidoHome.setVisibility(VISIBLE);
    }

    public static void initFirebase(final Context mContext, TinyDB tinyDB) {

        try {

            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("otro");
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        //region ACTUALIZAR APP

                        if (dataSnapshot.child("actualizacion").exists()) {
                            setDebug("metodos", "initFirebase", "d", "Confirmando actualizacion disponible...", setDebugActivo);

                            //para la version pelisplus
                            int versionNueva = Integer.parseInt(dataSnapshot.child("actualizacion").child("version").getValue().toString());
                            Boolean estado = dataSnapshot.child("actualizacion").child("estado").getValue(Boolean.class);
                            String urlDescarga = dataSnapshot.child("actualizacion").child("urlDescarga").getValue(String.class);
                            Boolean cancelable = dataSnapshot.child("actualizacion").child("cancelable").getValue(Boolean.class);
                            String mensaje = dataSnapshot.child("actualizacion").child("mensaje").getValue().toString();
                            Boolean playStore = dataSnapshot.child("actualizacion").child("playStore").getValue(Boolean.class);

                            int versionActual = BuildConfig.VERSION_CODE;

                            if (estado == true && versionNueva > versionActual && versionActual != 1) {

                                try {
                                    if (mContext != null) {
                                        setLogInfo(mContext, "initFirebase", "Abriendo dialogo para actualizar", false);

                                        if (playStore) {
                                            DialogoActualizarPlayStore(mContext, versionNueva, mensaje, urlDescarga, cancelable);
                                        } else {
                                            DialogoActualizar(mContext, versionNueva, mensaje, urlDescarga, cancelable);
                                        }
                                    }
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                    /*
                                    File archivoUpdate = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/update.apk");
                                    if (archivoUpdate.exists()){
                                        archivoUpdate.delete();
                                    }
                                     */
                            }


                        }


                        //region POCESO DETECTAR CAMBIOS EN DATA

                        if (dataSnapshot.child("utilidad").child("fechaCambiosData").exists()) {
                            // se comprueba fechas
                            String fechaCambioData = (String) dataSnapshot.child("utilidad").child("fechaCambiosData").getValue();
                            String fechaUltimoCambio = tinyDB.getString(TBfechaCambiosData);

                            setDebug("metodos", "initFirebase", "d", "fechaCambioData == " + fechaCambioData, setDebugActivo);
                            setDebug("metodos", "initFirebase", "d", "fechaUltimoCambio == " + fechaUltimoCambio, setDebugActivo);

                            // si las fechas no coinciden, es porque hay cambios por realizar, se borra lista y se recarga
                            if (!fechaCambioData.toLowerCase().trim().contains(fechaUltimoCambio.toLowerCase().trim()) || fechaUltimoCambio.isEmpty()) {

                                // Toast.makeText(mContext, "actualizando contenido", Toast.LENGTH_SHORT).show();
                                getListas(mContext);
                                // guardar fecha nueva en tiny
                                tinyDB.putString(TBfechaCambiosData, fechaCambioData);

                            } else {
                                setDebug("metodos", "initFirebase", "d", "No hay contenido por actualizar..", setDebugActivo);
                            }
                        }

                        //endregion

                        //region MOSTRAR ALERTA DE MENSAJE

                        if (dataSnapshot.child("alerta").exists()) {
                            // se comprueba fechas
                            String titleAlert = (String) dataSnapshot.child("alerta").child("titulo").getValue();
                            String msgAlert = (String) dataSnapshot.child("alerta").child("mensaje").getValue();
                            Boolean estadoAlert = (Boolean) dataSnapshot.child("alerta").child("estado").getValue();
                            int versionAlert = Integer.parseInt(dataSnapshot.child("alerta").child("version").getValue().toString());
                            int versionActual = BuildConfig.VERSION_CODE;

                            if (estadoAlert && versionActual <= versionAlert) {
                                //  Toast.makeText(mContext, mContext.getResources().getString(R.string.coneccion_lenta), Toast.LENGTH_SHORT).show();
                                Alerter.create((AppCompatActivity) mContext).setTitle(titleAlert)
                                        .setText(msgAlert)
                                        // .setBackgroundResource(R.drawable.shape_controller_top_gradient)
                                        .setIcon(R.drawable.uvv_on_error)
                                        .setDuration(20000)
                                        .setTextTypeface(Typeface.createFromAsset(mContext.getAssets(), "poppins_regular.ttf"))
                                        .setBackgroundColorRes(R.color.fondo_black2) // or setBackgroundColorInt(Color.CYAN)
                                        .addButton("OK", R.style.TabTextStyle, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //region MIX mixExtractionGoesWrong para estadisticas
                                                if (Alerter.isShowing()) {
                                                    Alerter.hide();
                                                }
                                            }
                                        }).addButton("WEBSITE", R.style.TabTextStyle, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AbrirPagina(mContext, "https://lugumusic.page.link/website");
                                    }
                                })
                                        .show();
                            }

                        }

                        //endregion


                    } catch (NumberFormatException e) {
                        setDebug("metodos", "initFirebase", "e", e.getMessage(), setDebugActivo);
                        e.printStackTrace();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                }
            });

            //endregion


        } catch (Exception e) {
            setDebug("metodos", "initFirebase", "e", e.getMessage(), setDebugActivo);
            e.printStackTrace();
        }

    }

    public static void setDebug(String CLASS, String metodo, String tipo, String msg, boolean activo) {

        String TAG = "setDebug";
        String MENSAJE = CLASS + "-" + metodo + " = " + tipo + "-" + "msg: " + msg;


        if (activo) {
            switch (tipo) {
                case "e":
                    Log.e(TAG, MENSAJE);
                    break;

                case "w":
                    Log.w(TAG, MENSAJE);
                    break;

                case "d":
                    Log.d(TAG, MENSAJE);
                    break;

                case "i":
                    Log.i(TAG, MENSAJE);
                    break;

                case "v":
                    Log.v(TAG, MENSAJE);
                    break;


            }
        }

    }

    public static void DialogoActualizar(Context mContext, int version, String mensaje, String urlDescarga, boolean cancelable) {
        setLogInfo(mContext, "DialogoActualizar", "Mostrando DialogoActualizar...", false);

        String obligatorio = mContext.getString(R.string.act_obligatoria);
        String titulo = mContext.getString(R.string.act_disponible) + "\nversion : " + version;
        if (cancelable) {
            obligatorio = mContext.getString(R.string.act_opcional);
        } else {
            obligatorio = mContext.getString(R.string.act_obligatoria);
        }

        titulo = titulo + "\n" + obligatorio;
        mensaje = mensaje + "\n" + mContext.getString(R.string.necesaria_permiso) + mContext.getResources().getString(R.string.app_name) + ")" + "\n+" + mContext.getString(R.string.nota_actualizar);

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        MessageDialog.show((AppCompatActivity) mContext, titulo, mensaje, mContext.getString(R.string.acrualizar)).setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN)).setButtonOrientation(LinearLayout.VERTICAL).setOtherButton(mContext.getString(R.string.open_website)).setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                //    Toast.makeText(mContext, "sdasdasdd", Toast.LENGTH_SHORT).show();
                AbrirPagina(mContext, "https://lugumusic.page.link/website");
                return false;
            }
        }).setCancelable(cancelable).setOnOkButtonClickListener((baseDialog, v) -> {
            DescargarActualizacion(mContext, urlDescarga);
            return false;
        });


    }

    public static void DialogoActualizarPlayStore(Context mContext, int version, String mensaje, String urlDescarga, boolean cancelable) {
        setLogInfo(mContext, "DialogoActualizar", "Mostrando DialogoActualizar...", false);

        String obligatorio = mContext.getString(R.string.act_obligatoria);
        String titulo = mContext.getString(R.string.act_disponible) + "\nversion : " + version;
        if (cancelable) {
            obligatorio = mContext.getString(R.string.act_opcional);
        } else {
            obligatorio = mContext.getString(R.string.act_obligatoria);
        }

        titulo = titulo + "\n" + obligatorio;
        mensaje = mensaje + mContext.getString(R.string.msg_actua_playstore);

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        MessageDialog.show((AppCompatActivity) mContext, titulo, mensaje, mContext.getString(R.string.acrualizar)).setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN)).setButtonOrientation(LinearLayout.VERTICAL).setOtherButton(mContext.getString(R.string.open_website)).setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                //    Toast.makeText(mContext, "sdasdasdd", Toast.LENGTH_SHORT).show();
                AbrirPagina(mContext, "https://lugumusic.page.link/website");
                return false;
            }
        }).setCancelable(cancelable).setOnOkButtonClickListener((baseDialog, v) -> {
            // DescargarActualizacion(mContext, urlDescarga);
            AbrirPagina(mContext, "https://lugumusic.page.link/luguapp");
            return false;
        });


    }

    public static <mContext> void DescargarActualizacion(Context mContext, String urlDescarga) {

        if (urlDescarga.contains("http") || urlDescarga.contains("https")) {

            //region DESCARGAR ARCHIVO

            OkHttpClient client = new OkHttpClient.Builder().build();
            DownloadManager manager = new DownloadManager.Builder().context(mContext)
                    .downloader(OkHttpDownloader.create(client))
                    .threadPoolSize(3)
                    .logger(new Logger() {
                        @Override
                        public void log(String message) {
                            Log.d("TAG", message);
                        }
                    })
                    .build();


            String destinoUpdate = mContext.getFilesDir() + "/update.apk";

            // String destinoUpdate = mContext.getCacheDir() + "/update.apk";
            setLogInfo(mContext, "DescargarActualizacion", "destinoUpdate: " + destinoUpdate, false);
            //    Toast.makeText(mContext, destinoUpdate, Toast.LENGTH_SHORT).show();

            DownloadRequest request =
                    new DownloadRequest.Builder()
                            .url(urlDescarga)
                            .retryTime(5)
                            .retryInterval(2, TimeUnit.SECONDS)
                            .progressInterval(1, TimeUnit.SECONDS)
                            .priority(Priority.HIGH)
                            .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI)
                            .allowedNetworkTypes(DownloadRequest.NETWORK_MOBILE)
                            .destinationFilePath(destinoUpdate)
                            .downloadCallback(new DownloadCallback() {
                                @Override
                                public void onStart(int downloadId, long totalBytes) {
                                    WaitDialog.dismiss();
                                    WaitDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.iniciando_descarga));
                                }

                                @Override
                                public void onRetry(int downloadId) {
                                    WaitDialog.dismiss();
                                    WaitDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.intentando_denuevo));
                                }

                                @Override
                                public void onProgress(int downloadId, long bytesWritten, long totalBytes) {

                                    if (totalBytes != 0) {

                                        long porcentaje = ((bytesWritten * 100) / totalBytes);


                                        WaitDialog.dismiss();
                                        WaitDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.descargando) + porcentaje).setCancelable(false);
                                        System.out.println("bytesWritten " + bytesWritten + "  totalBytes " + totalBytes);
                                    } else {
                                        // Toast.makeText(mContext, "Error en la descarga, enviando reporte..", Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onSuccess(int downloadId, String filePath) {
                                    WaitDialog.dismiss();
                                    WaitDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.descarga_completa)).setCancelable(true).setTip(WaitDialog.TYPE.SUCCESS);
                                    InstalarApp(mContext, new File(filePath));

                                }

                                @Override
                                public void onFailure(int downloadId, int statusCode, String errMsg) {
                                    WaitDialog.dismiss();
                                    WaitDialog.show((AppCompatActivity) mContext, errMsg).setCancelable(true).setTip(WaitDialog.TYPE.ERROR);
                                    Toast.makeText(mContext, mContext.getString(R.string.error_descarga) + errMsg, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();


            manager.add(request);


                        /*

                    // stop single
                    manager.cancel(downloadId);
                    // stop all
                    manager.cancelAll();


                     */


            //endregion

            /* Metodo de descargar app pidiendo permisos de almacenamiento
            PermissionListener permissionlistener = new PermissionListener() {


                @Override
                public void onPermissionGranted() {

                    //region DESCARGAR ARCHIVO

                    OkHttpClient client = new OkHttpClient.Builder().build();
                    DownloadManager manager = new DownloadManager.Builder().context(mContext)
                            .downloader(OkHttpDownloader.create(client))
                            .threadPoolSize(3)
                            .logger(new Logger() {
                                @Override
                                public void log(String message) {
                                    Log.d("TAG", message);
                                }
                            })
                            .build();


                    //  File toInstall = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));

                    File toInstall = new File(mContext.getCacheDir().getAbsolutePath());

                    if (!toInstall.exists()) {
                        toInstall.mkdirs();
                    }

                    //  String destinoUpdate = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update.apk";

                    String destinoUpdate = mContext.getCacheDir().getAbsolutePath() + "/update.apk";
                    Toast.makeText(mContext, destinoUpdate, Toast.LENGTH_SHORT).show();

                    DownloadRequest request =
                            new DownloadRequest.Builder()
                                    .url(urlDescarga)
                                    .retryTime(5)
                                    .retryInterval(2, TimeUnit.SECONDS)
                                    .progressInterval(1, TimeUnit.SECONDS)
                                    .priority(Priority.HIGH)
                                    .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI)
                                    .allowedNetworkTypes(DownloadRequest.NETWORK_MOBILE)
                                    .destinationFilePath(destinoUpdate)
                                    .downloadCallback(new DownloadCallback() {
                                        @Override
                                        public void onStart(int downloadId, long totalBytes) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, "Iniciando descarga...");
                                        }

                                        @Override
                                        public void onRetry(int downloadId) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, "Intentando de nuevo...");
                                        }

                                        @Override
                                        public void onProgress(int downloadId, long bytesWritten, long totalBytes) {

                                            if (totalBytes != 0) {

                                                long porcentaje = ((bytesWritten * 100) / totalBytes);


                                                WaitDialog.dismiss();
                                                WaitDialog.show((AppCompatActivity) mContext, "Descargando... %" + porcentaje).setCancelable(false);
                                                System.out.println("bytesWritten " + bytesWritten + "  totalBytes " + totalBytes);
                                            } else {
                                                ReportarEpisodio(mContext, "Error en descarga, total bytes es 0", "0000", "0", "", "", "");
                                                Toast.makeText(mContext, "Error en la descarga, enviando reporte..", Toast.LENGTH_SHORT).show();
                                            }


                                        }

                                        @Override
                                        public void onSuccess(int downloadId, String filePath) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, "Descarga completa.").setCancelable(true).setTip(WaitDialog.TYPE.SUCCESS);
                                            InstalarApp(mContext, new File(filePath));

                                        }

                                        @Override
                                        public void onFailure(int downloadId, int statusCode, String errMsg) {
                                            WaitDialog.dismiss();
                                            WaitDialog.show((AppCompatActivity) mContext, errMsg).setCancelable(true).setTip(WaitDialog.TYPE.ERROR);
                                            Toast.makeText(mContext, "Error en la descarga, " + errMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .build();


                    manager.add(request);



                    //endregion

                }
                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    SolicitarPermisos(mContext);
                    TipDialog.show((AppCompatActivity) mContext, "Son necesarios permisos de almacenamiento para realizar la descarga.", TipDialog.TYPE.WARNING).setTipTime(60000).setCancelable(true);
                }


            };

            TedPermission.with(mContext)
                    .

                            setPermissionListener(permissionlistener)
                    .

                            setDeniedMessage("Si no habilita los permisos de almacenamiento no se podra descargar nuevas actualizaciones, ni usar cache para la carga de videos mas rapida\n\nPuede tambien hacerlo manualmente en [Setting] > [Permission]")
                    .

                            setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .

                            check();

            */

        } else {
            TipDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.error_al_descragar), TipDialog.TYPE.ERROR).setCancelable(true).setTipTime(10000);

        }

    }

    public static void SolicitarPermisos(Context mContext) {

        PermissionListener permissionlistener = new PermissionListener() {


            @Override
            public void onPermissionGranted() {

                //   TipDialog.show((AppCompatActivity) mContext, "Permisos concedidos.", TipDialog.TYPE.SUCCESS).setTipTime(60000).setCancelable(true);
            }


            //endregion


            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                TipDialog.show((AppCompatActivity) mContext, "Si no habilita los permisos de almacenamiento no se podra descargar nuevas actualizaciones, ni usar cache para la carga de videos mas rapida", TipDialog.TYPE.WARNING).setTipTime(60000).setCancelable(true);
            }


        };
        TedPermission.with(mContext)
                .setPermissionListener(permissionlistener).setDeniedMessage("Si no habilita los permisos de almacenamiento no se podra descargar nuevas actualizaciones, ni usar cache para la carga de videos mas rapida\n\nPuede tambien hacerlo manualmente en [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).
                check();
    }

    public static void InstalarApp(Context mContext, File ubicacionApk) {
        File toInstall = ubicacionApk;

        //region MIX mixActualizarApp AMPLITUDE

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        //region MIX mixCompartirApp para estadisticas
        JSONObject props = new JSONObject();
        try {
            props.put("Fecha", fecha);
            Bundle params = new Bundle();
            params.putString("Fecha", fecha);


            mFirebaseAnalytics.logEvent(mixActualizarApp, params);
            mixpanel.track(mixActualizarApp, props);
            Amplitude.getInstance().logEvent(mixActualizarApp, props);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //endregion

        if (toInstall.exists()) {

            Intent install;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(
                        mContext,
                        BuildConfig.APPLICATION_ID + ".provider",
                        new File(String.valueOf(toInstall))
                );
                install = new Intent(Intent.ACTION_VIEW);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                install.setData(contentUri);
                mContext.startActivity(install);
                // finish()
            } else {
                install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(Uri.fromFile(toInstall),
                        "application/vnd.android.package-archive");
                mContext.startActivity(install);

                // finish()
            }

        }

    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void CompartirApp(Context mContext) {
        try {


            //region MIX mixActualizarApp AMPLITUDE

            // para la fecha
            DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
            String fecha = df.format(Calendar.getInstance().getTime());

            //region MIX mixCompartirApp para estadisticas
            JSONObject props = new JSONObject();
            try {
                props.put("Fecha", fecha);
                //para FB
                Bundle params = new Bundle();
                params.putString("Fecha", fecha);

                mFirebaseAnalytics.logEvent(mixCompartirApp, params);
                mixpanel.track(mixCompartirApp, props);
                Amplitude.getInstance().logEvent(mixCompartirApp, props);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //endregion


            String saltoDeLinea = "\n";

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name) + " - " + mContext.getString(R.string.msg_shareapp));
            String shareMessage = mContext.getResources().getString(R.string.app_name) + "\n" + mContext.getString(R.string.msg_shareapp) + "\n" + mContext.getString(R.string.mas_info_descarga) + "\n\n";
            shareMessage = shareMessage + "\n" + UrlAppPlayStore;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

            mContext.startActivity(Intent.createChooser(shareIntent, "Share with:"));

        } catch (Exception e) {
            //e.toString();
        }
    }

    public static void AbrirPagina(Context mContext, String pagina) {


        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pagina)));
        } catch (Exception e) {
            //e.toString();
        }


    }

    public static void SolicitarFilm(Context mContext) {


        String saltoDeLinea = "\n";
        String titulo = "Solicitar contenido";
        String mensaje = "¿No está tú pelicula o serie favorita?" + saltoDeLinea + saltoDeLinea + "Dinos cual es y según la disponibilidad de el film, estará en " + mContext.getResources().getString(R.string.app_name) + " en un lapso de 24 horas como maximo" + saltoDeLinea + saltoDeLinea + "(Esta app es nueva, con tus peticiones nos ayudaras a incrementar el contenido y que haya solo de calidad)";
        String hintText = "¿Pelicula/Serie-Temporada?";


        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        InputDialog.build((AppCompatActivity) mContext)
                //   .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                //  .setInputText("111111")
                .setOkButton("SOLICITAR", new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        if (inputStr.equals("")) {
                            TipDialog.show((AppCompatActivity) mContext, "El espacio no puede estar vacío", TipDialog.TYPE.ERROR);
                            SolicitarFilm(mContext);
                            return false;
                        } else {
                            EnviarSolicitud(mContext, inputStr);
                            return true;
                        }
                    }
                })
                //  .setCancelButton("CANCELAR")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setHintText(hintText)
                .setInputInfo(new InputInfo()
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setTextInfo(new TextInfo()
                                .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                        )
                )
                .setCancelable(true)
                .show();
    }

    public static void DialogoMiCancion(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getResources().getString(R.string.submitsong);
        String mensaje = mContext.getResources().getString(R.string.msg_submitsong) + mContext.getResources().getString(+R.string.app_name);
        String hintText = mContext.getResources().getString(R.string.sugerencia);


        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                //.setInputText("111111")
                .setOkButton(mContext.getString(R.string.enviar), new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        //  EnviarSugerencia(mContext, inputStr);
                        /*
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.setData(Uri.parse("mailto:lugulofimusic@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.sugerencia));
                        intent.putExtra(Intent.EXTRA_TEXT, "Message: \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n------------------------------------\n\n\n\n\n\n\n\n\n ");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }

                         */

                        AbrirPagina(mContext, UrlEnviarMiCancion);

                        return false;

                    }
                })
                .setCancelButton(mContext.getString(R.string.cerrar))
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                .setCancelable(true)
                .show();
    }


    public static void DialogoSugerencia(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.enviar_sugenerencia);
        String mensaje = mContext.getString(R.string.msg_sugerencia);
        String hintText = mContext.getString(R.string.sugerencia);


        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                //  .setInputText("111111")
                .setOkButton(mContext.getString(R.string.enviar), new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        //  EnviarSugerencia(mContext, inputStr);
                        /*
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.setData(Uri.parse("mailto:lugulofimusic@gmail.com"));
                        intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.sugerencia));
                        intent.putExtra(Intent.EXTRA_TEXT, "Message: \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n------------------------------------\n\n\n\n\n\n\n\n\n ");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);
                        }

                         */

                        AbrirPagina(mContext, UrlEncuestaSugerencia);

                        return false;

                    }
                })
                .setCancelButton(mContext.getString(R.string.cerrar))
                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        return false;
                    }
                })
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                .setCancelable(true)
                .show();
    }

    public static void DialogoEliminarLista(Context mContext, List<ModelCancion> mList, String keyTinyDB) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.eliminar_lista);

        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setOkButton(mContext.getResources().getString(R.string.yes))
                .setMessage(mContext.getString(R.string.accion_deshacer))
                .setTitle(titulo)
                .setCancelButton("NO")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                .setCancelable(true)
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        // eliminar contenido
                        mList.removeAll(mList);

                        tinyDB.putListModelCancion(keyTinyDB, mList);
                        TipDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.contenido_eliminado), TipDialog.TYPE.SUCCESS);
                        if (keyTinyDB.contains(TBlistFavoritos)) {
                            UpdateAdapterFavoritos(mContext);
                            if (bottomNavigationHis_Fav != null) {
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);
                            }
                        } else if (keyTinyDB.contains(TBlistHistorial)) {
                            UpdateAdapterHistorial(mContext);
                            if (bottomNavigationHis_Fav != null) {
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);
                            }
                        }


                        return false;
                    }
                })
                .show();
    }

    /*
    public static void DialogoDonar(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.donate)+" "+mContext.getString(R.string.app_name);

        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setOkButton(mContext.getResources().getString(R.string.yes))
                .setMessage(mContext.getString(R.string.msg_donar))
                .setTitle(titulo)
                .setCancelButton("NO")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setCancelable(true)
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        // eliminar contenido

                        AbrirPagina(mContext,"https://www.paypal.com/paypalme/lugumusic");


                        return false;
                    }
                })
                .show();
    }

     */

    public static void DialogoSalir(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.salir_app);

        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setOkButton(mContext.getResources().getString(R.string.yes))
                .setMessage("")
                .setTitle(titulo)
                .setCancelButton("NO")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setCancelable(true)
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        ((AppCompatActivity) mContext).finish();

                        return false;
                    }
                })
                .show();
    }


    public static void DialogoEnviarReporte(Context mContext, String idFilm, String idEpisodio, String nombreEpisodio, String nombreFilm) {


        String saltoDeLinea = "\n";
        String titulo = "¿No funciona este episodio?";
        String mensajeDialogo = "Si este episodio no funciona, reportarlo ayudara a solucionarlo en un lapso de 24 horas como maximo." + saltoDeLinea + saltoDeLinea + "(Enviar reportes nos ayuda a mantener el contenido funcionando, gracias por su colaboración)";
        String hintText = "Mensaje (opcional)";


        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        InputDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensajeDialogo)
                //  .setInputText("111111")
                .setOkButton("ENVIAR", new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {


                        baseDialog.doDismiss();
                        return true;

                    }
                })
                .setCancelButton("SALIR")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setHintText(hintText)
                .setInputInfo(new InputInfo()
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .setTextInfo(new TextInfo()
                                .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                        )
                )
                .setCancelable(true)
                .show();
    }

    /*
    public static void getListaEpisodios(String idFilm, String Idioma, Context mContext) {




        WaitDialog.show((AppCompatActivity) mContext, "Cargando lista...").setCancelable(true);

        Query database = FirebaseDatabase.getInstance().getReference().child("data").child(String.valueOf(idFilm)).child("episodios").child(Idioma).orderByChild("idEpisodio");


        System.out.println("getListaEpisodios" + database.getRef().toString());

        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistEpisodios.removeAll(mlistEpisodios);


                try {

                    for (DataSnapshot snapshot :
                            dataSnapshot.getChildren()) {

                        System.out.println("SASASAS--- ");


                        if (snapshot.exists()) {


                            modelEpisodio episodio = snapshot.getValue(modelEpisodio.class);
                            mlistEpisodios.add(episodio);
                            System.out.println("PEISODIO--- " + episodio);

                        } else {
                            System.out.println("NO EXISTE--- ");
                        }


                    }

                } catch (Exception e) {
                    // This will catch any exception, because they are all descended from Exception
                    System.out.println("Error CATCH " + e.getMessage());

                }


                // metodo para ordenar lista


                if (mlistEpisodios != null && !mlistEpisodios.isEmpty()) {

                    Collections.sort(mlistEpisodios, new Comparator<modelEpisodio>() {
                        public int compare(modelEpisodio o1, modelEpisodio o2) {
                            return extractInt(o1.getIdEpisodio()) - extractInt(o2.getIdEpisodio());
                        }

                        int extractInt(String s) {
                            if (s != null) {
                                String num = s.replaceAll("\\D", "");
                                return num.isEmpty() ? 0 : Integer.parseInt(num);
                            }
                            return 0;
                        }
                    });

                }


                //region para la list view de edicion de episodios en act_add_episodio


                listEpisodiosListView = new ArrayList<>();


                for (int i = 0; mlistEpisodios.size() > i; i++) {
                    listEpisodiosListView.add(mlistEpisodios.get(i).getIdEpisodio() + " - " + mlistEpisodios.get(i).getNombre());
                }

                if (listEpisodiosListView != null && !listEpisodiosListView.isEmpty()) {

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.listview_text, listEpisodiosListView);


                    if (lvEpisodios != null) {
                        lvEpisodios.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }

                //endregion



                //depeniendo la cantidad de los episodios se mostrará el filtrar lista
                if (mlistEpisodios != null && !mlistEpisodios.isEmpty() && mlistEpisodios.size() > 10) {
                    etFiltrarEpisodios.setVisibility(VISIBLE);
                } else {
                    etFiltrarEpisodios.setVisibility(GONE);
                }


                mAdapterEpisodio.notifyDataSetChanged();
                WaitDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

     */

    public static void DialogoTemporizador(Context mContext) {


        // video mob
        createAndLoadRewardedAd(mContext);

        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        //对于未实例化的布局：
        MessageDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.temporizador_apagado), mContext.getString(R.string.seleccione_tiempo), "OK")
                .setBackgroundColor(mContext.getResources().getColor(R.color.fondo_blank)).setCustomView(R.layout.layout_send_notifi, new MessageDialog.OnBindView() {

            @Override
            public void onBind(MessageDialog dialog, View view) {


                numberPicker = view.findViewById(R.id.number_picker);

                numberPicker.setMaxValue(60);
                numberPicker.setMinValue(0);
                numberPicker.setValue(15);


                // OnClickListener
                numberPicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("numberPicker", "Click on current value " + numberPicker.getValue());


                    }
                });

                // OnValueChangeListener
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        Log.d("numberPicker", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                    }
                });

                // OnScrollListener
                numberPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
                    @Override
                    public void onScrollStateChange(NumberPicker picker, int scrollState) {
                        if (scrollState == SCROLL_STATE_IDLE) {
                            Log.d("numberPicker", String.format(Locale.US, "newVal: %d", picker.getValue()));
                        }
                    }
                });

            }
        }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                if (numberPicker.getValue() > 0) {
                    ApagarAutoApagado(mContext);
                    EncenderAutoApagado(mContext, numberPicker.getValue());
                }
                return false;
            }
        });

    }

    public static List<String[]> ListaCSV(Context mContext, String nombreFileCSV) {


        List<String[]> list = new ArrayList<String[]>();
        String[] next = {};

        try {
            InputStreamReader csvStreamReader = new InputStreamReader(
                    mContext.getAssets().open(
                            nombreFileCSV));

            CSVReader reader = new CSVReader(csvStreamReader);
            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                    System.out.println("EN CSV -- " + next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;


    }

    public static void AboutUS(Context mContext, TinyDB tinyDB, boolean mostrarSoloUnaVez) {

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;

        if (mostrarSoloUnaVez == false) {
            String saltoDeLinea = "\n";

            MessageDialog.show((AppCompatActivity) mContext, "About " + mContext.getResources().getString(R.string.app_name), mContext.getResources().getString(R.string.app_name) + mContext.getString(R.string.sobre_lugu) +
                    "¡Welcome to " + mContext.getResources().getString(R.string.app_name) + ", enjoy!" +
                    saltoDeLinea + saltoDeLinea +
                    mContext.getString(R.string.apoyo_posible), "OK")
                    .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {

                            return false;
                        }
                    })
                    .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
                        }
                    });
        }
    }

    public static int CalcularNumeroColumnas(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    public static void EnviarCambioFB(Context mContext) {

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("otro").child("utilidad").child("fechaCambiosData");
        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());
        mref.setValue(fecha);


    }

    public static void CargarInterAd(Context mContext, String ad_unit_id, int cantidadAleatoria) {

        Random numRandom = new Random();
        int numPosibilidad = numRandom.nextInt(cantidadAleatoria);

        //   Toast.makeText(this, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();


        if (numPosibilidad == 3) {

            AdMesa.createLoadInterstitial(mContext, ad_unit_id, null);
        }


        //   Toast.makeText(mContext, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();


    }

    public static void getLinkAndPlay(Context mContext, String linkYT, int opcion) {

        CargarInterAleatorio(mContext, 50);

        // cargar imagen en fondo VHS
        CargarImagenVHS(mContext);


        if (!isNetworkAvailable(mContext)) {
            //  Toast.makeText(mContext, mContext.getResources().getString(R.string.coneccion_lenta), Toast.LENGTH_SHORT).show();
            Alerter.create((Activity) mContext).setTitle(mContext.getResources().getString(R.string.coneccion_lenta))
                    // .setText("Se reproducirá canciones del estilo seleccionado.")
                    // .setBackgroundResource(R.drawable.shape_controller_top_gradient)
                    .setIcon(R.drawable.uvv_on_error)
                    .setTextTypeface(Typeface.createFromAsset(mContext.getAssets(), "poppins_regular.ttf"))
                    .setBackgroundColorRes(R.color.learn_light_red) // or setBackgroundColorInt(Color.CYAN)
                    .show();
        } else {

            /* start the timer for the event "Image Upload"
            if(mixpanel!=null){
                mixpanel.timeEvent("TimeExtractingLink");
            }

             */

            if (pbCargandoRadio != null) {
                pbCargandoRadio.startAnimation(Animacion.anim_alpha_out(mContext));
                pbCargandoRadio.setVisibility(VISIBLE);
                pbCargandoRadio.startAnimation(Animacion.anim_alpha_in(mContext));
            }

            if (opcion == 1) {
                setLogInfo(mContext, "getLinkAndPlay.YouTubeExtractor", "Inicia extración opcion 1", false);

                // https://github.com/HaarigerHarald/android-youtubeExtractor
                new YouTubeExtractor(mContext) {

                    @Override
                    public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                        if (ytFiles != null) {
                            //FORMAT_MAP.put(140, new Format(140, "m4a", Format.VCodec.NONE, Format.ACodec.AAC, 128, true));
                            //FORMAT_MAP.put(141, new Format(141, "m4a", Format.VCodec.NONE, Format.ACodec.AAC, 256, true));
                            //FORMAT_MAP.put(256, new Format(256, "m4a", Format.VCodec.NONE, Format.ACodec.AAC, 192, true));
                            //FORMAT_MAP.put(258, new Format(258, "m4a", Format.VCodec.NONE, Format.ACodec.AAC, 384, true));

                            // Iterate over itags
                            for (int i = 0, itag; i < ytFiles.size(); i++) {
                                itag = ytFiles.keyAt(i);
                                // ytFile represents one file with its url and meta data
                                YtFile ytFile = ytFiles.get(itag);

                                // si el itag el igual a una url de audio. cada itag es de audio con un birate diferente
                                if (itag == 258 || itag == 256 || itag == 141 || itag == 140) {

                                    System.out.println("newlofi ytFiles.get(itag).getAudioBitrate() == " + ytFiles.get(itag).getFormat().getAudioBitrate() + " kb/s");
                                    if (ytFiles.get(itag).getUrl() != null) {
                                        String linkCancion = ytFiles.get(itag).getUrl();
                                        System.out.println("newlofi downloadUrl itag == " + " ytFiles " + ytFiles.get(itag).getFormat().getAudioBitrate() + linkCancion);

                                        ReproducirCancion(mContext, linkCancion);

                                        break;
                                    }


                                } else {
                                    System.out.println("no hay itag de audio == ");
                                }
                            }
                            /*
                            if(mixpanel!=null) {
                                mixpanel.track("TimeExtractingLink");
                            }
                             */
                        } else {
                            setLogInfo(mContext, "getLinkAndPlay.YouTubeExtractor", "No funcionó extración opcion 1", false);
                            getLinkAndPlay(mContext, linkYT, 2);
                        }
                    }

                }.extract(linkYT);


            } else if (opcion == 2) {

                setLogInfo(mContext, "getLinkAndPlay.YoutubeStreamExtractor", "Inicia extracion opcion 2", false);

                // https://github.com/nhCoder/YouTubeExtractor
                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        setLogInfo(mContext, "getLinkAndPlay.YoutubeStreamExtractor", "onExtractionDone", false);
                        /*
                        if(mixpanel!=null) {
                            mixpanel.track("TimeExtractingLink");
                        }
                         */
                        //url to get subtitle

                        for (YTMedia media : adativeStream) {

                            // solo extraer link de audio
                            if (media.getAudioQuality() != null && media.getAudioQuality().contains("AUDIO")) {
                                String linkCancion = media.getUrl();
                                System.out.println("newlofi downloadUrl  media.getUrl() == " + media.getUrl());
                                System.out.println("newlofi downloadUrl  media.getAudioQuality() == " + media.getAudioQuality());
                                System.out.println("newlofi downloadUrl  media.getBitrate() == " + media.getBitrate());
                                System.out.println("newlofi downloadUrl  media.getItag() == " + media.getItag());
                                // reproducir cancion
                                ReproducirCancion(mContext, linkCancion);
                                break;
                            }

                        }
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException error) {
                        // Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                        //  Toast.makeText(mContext, "Hubo un error con esta canción :(", Toast.LENGTH_LONG).show();


                        setLogInfo(mContext, "getLinkAndPlay.YoutubeStreamExtractor.onExtractionGoesWrong", error.getMessage(), false);

                        //region MIX mixExtractionGoesWrong para estadisticas
                        JSONObject props = new JSONObject();
                        try {
                            props.put("LinkYT", linkYT);
                            props.put("Error", error.getMessage());
                            Bundle params = new Bundle();
                            params.putString("LinkYT", linkYT);
                            params.putString("Error", error.getMessage());


                            mFirebaseAnalytics.logEvent(mixExtractionGoesWrong, params);
                            mixpanel.track(mixExtractionGoesWrong, props);
                            Amplitude.getInstance().logEvent(mixExtractionGoesWrong, props);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //endregion


                        // al onExtractionGoesWrong, cargar otra cancion de la misma categoria segun selección
                        NextSong(mContext, tinyDB);

                        // ocultar progressBar de act_main
                        if (pbCargandoRadio != null) {
                            pbCargandoRadio.startAnimation(Animacion.anim_alpha_in(mContext));
                            pbCargandoRadio.setVisibility(GONE);
                            pbCargandoRadio.startAnimation(Animacion.anim_alpha_out(mContext));
                        }


                    }
                }).useDefaultLogin().Extract(linkYT);
                //use .useDefaultLogin() to extract age restricted videos
            }

        }
    }

    public static void CategoriaAleatoria(Context mContext, boolean encendido, TinyDB tinyDB) {

        if (tinyDB == null) {
            tinyDB = new TinyDB(mContext);
        }

        if (encendido) {
            tinyDB.putBoolean(TBCategoriaAleatoria, true);
            if (ivStyle != null) {
                ivStyle.startAnimation(Animacion.exit_ios_anim(mContext));
                ivStyle.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_intercambiar_on));
                ivStyle.startAnimation(Animacion.enter_ios_anim(mContext));
            }

            // reproducir cancion de una categoria aleatoria
            NextSong(mContext, tinyDB);

            Alerter.create((Activity) mContext).setTitle(R.string.text_cataleatoria_on)
                    .setText(R.string.msg_cataleatoria_on)
                    .setIcon(R.drawable.ic_intercambiar_on)
                    // .setBackgroundResource(R.drawable.fon)
                    .setTextTypeface(Typeface.createFromAsset(mContext.getAssets(), "poppins_regular.ttf"))
                    .setBackgroundColorRes(R.color.fondo_black3) // or setBackgroundColorInt(Color.CYAN)
                    .show();

            CargarInterAleatorio(mContext, 3);

        } else {
            tinyDB.putBoolean(TBCategoriaAleatoria, false);
            if (ivStyle != null) {
                if (ivStyle.getDrawable().getConstantState() == (AppCompatResources.getDrawable(mContext, R.drawable.ic_intercambiar_on).getConstantState())) {
                    // activar modo categoria aleatoria
                    Alerter.create((Activity) mContext).setTitle(R.string.text_cataleatoria_off)
                            .setText(R.string.msg_cataleatoria_off)
                            .setIcon(R.drawable.ic_intercambiar)
                            //.setBackgroundResource(R.drawable.shape_controller_top_gradient)
                            .setTextTypeface(Typeface.createFromAsset(mContext.getAssets(), "poppins_regular.ttf"))
                            .setBackgroundColorRes(R.color.fondo_black3) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                }
                ivStyle.startAnimation(Animacion.exit_ios_anim(mContext));
                ivStyle.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_intercambiar));
                ivStyle.startAnimation(Animacion.enter_ios_anim(mContext));
            }
        }

    }

    public static void ReproducirCancion(Context mContext, String linkCancion) {
        if (musicPlayer != null) {
            setLogInfo(mContext, "ReproducirCancion", "Inicia ExoPlayerView.setSource", false);
            if (mixpanel != null) {

            }
            musicPlayer.setSource(linkCancion);
            // reproducir
            musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PLAY);

            // cargar una imagende fondo totalmente aleatoria
            CargarImagenFondo(mContext);
            // recargar opcion de modo reproductor para que se cargue el icono con el nuevo color
            OpcionReproductor(mContext, tinyDB.getString(TBmodoReproductor));

            //region MIX mixPlaySong para estadisticas
            JSONObject props = new JSONObject();
            try {
                props.put("Id", tinyDB.getString(TBidCancionSonando));
                props.put("Nombre", tinyDB.getString(TBnombreCancionSonando));
                props.put("Artista", tinyDB.getString(TBartistaCancionSonando));
                props.put("Categoria", tinyDB.getString(TBcategoriaCancionSonando));
                props.put("LinkYT", tinyDB.getString(TBlinkCancionSonando));
                Bundle params = new Bundle();
                params.putString("Id", tinyDB.getString(TBidCancionSonando));
                params.putString("Nombre", tinyDB.getString(TBnombreCancionSonando));
                params.putString("Artista", tinyDB.getString(TBartistaCancionSonando));
                params.putString("Categoria", tinyDB.getString(TBcategoriaCancionSonando));
                params.putString("LinkYT", tinyDB.getString(TBlinkCancionSonando));


                mFirebaseAnalytics.logEvent(mixPlaySong, params);
                mixpanel.track(mixPlaySong, props);
                Amplitude.getInstance().logEvent(mixPlaySong, props);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //endregion

        }

    }

    public static void NextSong(Context mContext, TinyDB tinyDB) {

        // lista de la categoria que estaá sonando
        List<ModelCancion> listSonando = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);

        // cambiar a lista de canciones si la categoria es aleatoria
        if (tinyDB.getBoolean(TBCategoriaAleatoria)) {
            listSonando = tinyDB.getListModelCancion(TBlistCanciones, ModelCancion.class);
        }
        if (listSonando != null && listSonando.size() != 0) {

            Random random = new Random();
            int numCancionSonar = random.nextInt(listSonando.size());
            //region guardar datos de la cancion sonando en TinyDB

            tinyDB.putInt(TBnumeroCancionSonando, numCancionSonar);
            tinyDB.putString(TBidCancionSonando, listSonando.get(numCancionSonar).getId());
            tinyDB.putString(TBnombreCancionSonando, listSonando.get(numCancionSonar).getCancion());
            tinyDB.putString(TBartistaCancionSonando, listSonando.get(numCancionSonar).getArtista());
            tinyDB.putString(TBlinkCancionSonando, listSonando.get(numCancionSonar).getLinkYT());
            // obtener nombre de categoria
            if (tinyDB.getListModelCategoria(TBlistCategorias, ModelCategoria.class) != null) {
                for (ModelCategoria categoria : tinyDB.getListModelCategoria(TBlistCategorias, ModelCategoria.class)) {
                    if (categoria.getId().toLowerCase().trim().contains(listSonando.get(numCancionSonar).getCategoria().toLowerCase().trim())) {
                        tinyDB.putString(TBcategoriaCancionSonando, categoria.getNombre().toLowerCase().trim());
                    }
                }
            }
            //endregion
            getLinkAndPlay(mContext, listSonando.get(numCancionSonar).getLinkYT(), 1);

        } else {
            getListas(mContext);
        }

    }

    public static void setLogInfo(Context mContext, String TAG, String msg, boolean error) {

        String errormsg = "";
        if (error) {
            errormsg = "ErrorBug : | ";

            //region MIX ErrorBug para estadisticas
            JSONObject props = new JSONObject();
            try {

                props.put("Error", TAG + " | " + errormsg + msg);
                Bundle params = new Bundle();
                params.putString("Error", TAG + " | " + errormsg + msg);

                mFirebaseAnalytics.logEvent(mixLogInfoError, params);
                mixpanel.track(mixLogInfoError, props);
                Amplitude.getInstance().logEvent(mixLogInfoError, props);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //endregion

        }


        // para la fecha
        DateFormat df = new SimpleDateFormat("EEEE, d MMM yyyy hh:mm a ");
        String time = df.format(Calendar.getInstance().getTime());
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");
        //  String dato = errormsg = time + " | " + sp_get_childactivo(mContext) + " \n| " + TAG + " | \nmsg :: " + msg;
        //  fuego.setData.StringFB(RUTA_PADRE + "admin" + "/logcat/info/" + nombreRandom, dato, mContext);
        Log.d("setLogInfo", errormsg + TAG + " | msg :: " + msg);
    }

    public static void GuardarCancionHistorial(Context mContext, String idCancionSonando) {
        // agregar cancion a historial

        List<ModelCancion> tinyListCancionxCategoria = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);
        List<ModelCancion> tinyListHistorial = tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class);

        for (ModelCancion cancion : tinyListCancionxCategoria) {
            if (cancion.getId().equals(idCancionSonando)) {

                // si ya está en la lista eliminarlo para que el nuevo aparesca de primeras
                if (tinyListHistorial != null && tinyListHistorial.size() > 0) {
                    for (ModelCancion cancionHistorial : tinyListHistorial)

                        if (cancionHistorial.getId().toLowerCase().trim().contains(idCancionSonando.toLowerCase().trim())) {
                            tinyListHistorial.remove(cancionHistorial);
                            // Toast.makeText(mContext, "tinyListHistorial.remove(cancion)", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }


                // agregar cancion a historial
                tinyListHistorial.add(cancion);
                tinyDB.putListModelCancion(TBlistHistorial, EliminarDuplicadosModelCancion(tinyListHistorial));

                //region actualizar lista de historial
                mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
                //bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);
                //endregion

            }
        }
        if (tinyListHistorial.size() <= 1) {
            bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);
        }
        // para que se muestre de primeras las ultimas agregadas
        // Collections.reverse(tinyListHistorial);
        // guardar lista en tiny db y sin duplicados
        // Collections.reverse(tinyListHistorial);

    }

    public static void GuardarCancionFavoritos(Context mContext, String idCancionSonando, Boolean favorito) {


        // buscar  el numero de la cancion en la catergoria que está sonando
        List<ModelCancion> tinyListCancionxCategoria = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);
        // obtener lista de favoritos desde tinydb
        List<ModelCancion> tinyListFavoritos = tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class);

        if (favorito) {
            // agregar cancion a favoritos


            CargarInterAleatorio(mContext, 3);


            for (int i = 0; i < tinyListCancionxCategoria.size(); i++) {
                if (tinyListCancionxCategoria.get(i).getId().equals(idCancionSonando)) {
                    tinyListFavoritos.add(tinyListCancionxCategoria.get(i));
                    Toast.makeText(mContext, R.string.agrego_favoritos, Toast.LENGTH_SHORT).show();

                    //region MIX mixCompartirApp para estadisticas
                    JSONObject props = new JSONObject();
                    try {
                        props.put("Id", tinyListCancionxCategoria.get(i).getId());
                        props.put("Nombre", tinyListCancionxCategoria.get(i).getCancion());
                        props.put("Artista", tinyListCancionxCategoria.get(i).getArtista());
                        props.put("Categoria", tinyListCancionxCategoria.get(i).getCategoria());
                        props.put("LinkYT", tinyListCancionxCategoria.get(i).getLinkYT());
                        Bundle params = new Bundle();
                        params.putString("Id", tinyListCancionxCategoria.get(i).getId());
                        params.putString("Nombre", tinyListCancionxCategoria.get(i).getCancion());
                        params.putString("Artista", tinyListCancionxCategoria.get(i).getArtista());
                        params.putString("Categoria", tinyListCancionxCategoria.get(i).getCategoria());
                        params.putString("LinkYT", tinyListCancionxCategoria.get(i).getLinkYT());


                        mFirebaseAnalytics.logEvent(mixFavoritos, params);
                        mixpanel.track(mixFavoritos, props);
                        Amplitude.getInstance().logEvent(mixFavoritos, props);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //endregion

                    break;
                }
            }

            // guardar lista en tiny db y sin duplicados
            tinyDB.putListModelCancion(TBlistFavoritos, EliminarDuplicadosModelCancion(tinyListFavoritos));

            // actualizar lista de favoritos
            // UpdateAdapterFavoritos(mContext);

            // cambiar icono de ivLikeDislike a like

            ivLikeDislike.startAnimation(Animacion.exit_ios_anim(mContext));
            ivLikeDislike.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.learn_ic_like));
            ivLikeDislike.startAnimation(Animacion.enter_ios_anim(mContext));

            //region actualizar lista de favoritos
            mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
            bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);
            //endregion


        } else {

            // eliminar de favoritos


            // si en la lista de favoritos se encuentra la cancion que esta sonando se eliminará

            for (int i = 0; i < tinyListFavoritos.size(); i++) {
                if (tinyListFavoritos.get(i).getId().equals(idCancionSonando)) {
                    tinyListFavoritos.remove(i);
                    Toast.makeText(mContext, R.string.eliminado_favoritos, Toast.LENGTH_SHORT).show();
                }
            }


            // guardar lista en tiny db y sin duplicados
            tinyDB.putListModelCancion(TBlistFavoritos, EliminarDuplicadosModelCancion(tinyListFavoritos));
            // actualizar lista de favoritos
            //  UpdateAdapterFavoritos(mContext);

            // cambiar icono de ivLikeDislike a like

            ivLikeDislike.startAnimation(Animacion.exit_ios_anim(mContext));
            ivLikeDislike.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.learn_ic_dislike));
            ivLikeDislike.startAnimation(Animacion.enter_ios_anim(mContext));

            //region actualizar lista de favoritos
            mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
            bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);
            //endregion

        }

    }

    public static void CheckIsFavorite(Context mContext, String idCancionSonando) {
        // Checkear si la cancion que esta sonando esta en favoritos para marcarlo
        // buscar  el numero de la cancion en la catergoria que está sonando


        List<ModelCancion> tinyListCancionxCategoria = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);
        // obtener lista de favoritos desde tinydb
        List<ModelCancion> tinyListFavoritos = tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class);

        // Checkear si la cancion que esta sonando esta en favoritos para marcarlo
        if (tinyListFavoritos != null && tinyListFavoritos.size() > 0) {
            ivLikeDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_dislike));
            for (ModelCancion cancionEnFavoritos : tinyListFavoritos) {
                System.out.println("tinyListCancionxCategoria cancion " + cancionEnFavoritos.getCancion());
                if (cancionEnFavoritos.getId().equals(idCancionSonando)) {
                    ivLikeDislike.startAnimation(Animacion.anim_alpha_out(mContext));
                    ivLikeDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_like));
                    ivLikeDislike.startAnimation(Animacion.anim_alpha_in(mContext));
                    System.out.println("tinyListCancionxCategoria cancionSonando ===== cancion ");
                }
            }
        }
    }

    public static List<ModelCancion> EliminarDuplicadosModelCancion(List<ModelCancion> mListModelCancion) {

        Map<String, ModelCancion> cleanMap = new LinkedHashMap<String, ModelCancion>();
        for (int i = 0; i < mListModelCancion.size(); i++) {
            cleanMap.put(mListModelCancion.get(i).getId(), mListModelCancion.get(i));
        }
        List<ModelCancion> list = new ArrayList<ModelCancion>(cleanMap.values());
        return list;

    }

    public static void UpdateAdapterHistorial(Context mContext) {


        if (mAdapterHistorial != null && mrvHistorial != null) {

            mAdapterHistorial = new AdapterHistorial(mContext, tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
            mrvHistorial.setAdapter(mAdapterHistorial);
            mAdapterHistorial.notifyDataSetChanged();

            mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
        } else {
            Toast.makeText(mContext, "NULO", Toast.LENGTH_SHORT).show();
        }

    }

    public static void UpdateAdapterFavoritos(Context mContext) {

        if (mAdapterFavoritos != null && mrvFavoritos != null) {

            mAdapterFavoritos = new AdapterFavoritos(mContext, tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
            mrvFavoritos.setAdapter(mAdapterFavoritos);
            mAdapterFavoritos.notifyDataSetChanged();

            //   mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));

        } else {
            Toast.makeText(mContext, "NULO", Toast.LENGTH_SHORT).show();
        }

    }

    public static void EncenderAutoApagado(Context mContext, int minutos) {


        // cambiar icono sleep a encendido
        if (ivSleep != null) {
            ivSleep.startAnimation(Animacion.exit_ios_anim(mContext));
            ivSleep.setVisibility(VISIBLE);
            ivSleep.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_moon_on));
            ivSleep.startAnimation(Animacion.enter_ios_anim(mContext));
        }


        int minutes = minutos;
        int milliseconds = minutes * 60 * 1000;


        countDownTimer = new CountDownTimer(milliseconds, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                tvSleep.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                if (tvSleep.getText().toString().equals("00:05:00") || (tvSleep.getText().toString().equals("00:02:00"))) {
                    Toast.makeText(mContext, mContext.getString(R.string.se_apagara_en) + tvSleep.getText().toString() + mContext.getString(R.string.minutos), Toast.LENGTH_SHORT).show();
                }

                /*
                tvMinutosfaltantes.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                 */


            }

            @Override
            public void onFinish() {

                // cambiar icono sleep a apagado

                if (ivSleep != null) {
                    ivSleep.startAnimation(Animacion.exit_ios_anim(mContext));
                    ivSleep.setVisibility(VISIBLE);
                    ivSleep.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_moon_off));
                    ivSleep.startAnimation(Animacion.enter_ios_anim(mContext));
                }
                if (tvSleep != null) {
                    tvSleep.setVisibility(GONE);
                }

                musicPlayer.PlayOrPause(MediaNotificationManager.STATE_STOP);

            }

        }.start();


        countDownTimer.start();
        ivSleep.setImageResource(R.drawable.ic_moon_on);

        tvSleep.startAnimation(Animacion.exit_ios_anim(mContext));
        tvSleep.setVisibility(VISIBLE);
        tvSleep.startAnimation(Animacion.enter_ios_anim(mContext));


    }

    public static void ApagarAutoApagado(Context mContext) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
            tvSleep.setText("00:00:00");
            Toast.makeText(mContext, mContext.getString(R.string.apagando) + mContext.getResources().getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            // cambiar icono sleep a apagado
            if (ivSleep != null) {
                ivSleep.startAnimation(Animacion.exit_ios_anim(mContext));
                ivSleep.setVisibility(VISIBLE);
                ivSleep.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_moon_off));
                ivSleep.startAnimation(Animacion.enter_ios_anim(mContext));

                tvSleep.startAnimation(Animacion.enter_ios_anim(mContext));
                tvSleep.setVisibility(GONE);
                tvSleep.startAnimation(Animacion.exit_ios_anim(mContext));

            }

        }

    }

    public static void OpcionReproductor(Context mContext, String modoReproductor) {

        tinyDB.putString(TBmodoReproductor, modoReproductor);

        if (tinyDB.getString(TBmodoReproductor).equals(REPRODUCTOR_BUCLE)) {
            if (ivOpcionBucle != null) {
                ivOpcionBucle.startAnimation(Animacion.exit_ios_anim(mContext));
                ivOpcionBucle.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_bucle));
                ivOpcionBucle.startAnimation(Animacion.enter_ios_anim(mContext));
            }
        } else if (tinyDB.getString(TBmodoReproductor).equals(REPRODUCTOR_ALEATORIO)) {
            if (ivOpcionBucle != null) {
                ivOpcionBucle.startAnimation(Animacion.exit_ios_anim(mContext));
                ivOpcionBucle.setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.ic_aleatorio));
                ivOpcionBucle.startAnimation(Animacion.enter_ios_anim(mContext));
            }
        }
    }

    public static void GuardarDatosCancion(String categoria, ModelCancion modelCancion) {

        tinyDB.putString(TBidCancionSonando, modelCancion.getId());
        tinyDB.putString(TBnombreCancionSonando, modelCancion.getCancion());
        tinyDB.putString(TBartistaCancionSonando, modelCancion.getArtista());
        tinyDB.putString(TBcategoriaCancionSonando, categoria.toLowerCase().trim());
        tinyDB.putString(TBlinkCancionSonando, modelCancion.getLinkYT());
    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public static void DialogoSupArtista(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.support_artista);
        String mensaje = mContext.getString(R.string.msg_support_artista);


        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                .setOkButton(mContext.getString(R.string.serarch_artist), new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        AbrirPagina(mContext, "https://www.google.com/search?q=" + tinyDB.getString(TBartistaCancionSonando) + " - " + tinyDB.getString(TBnombreCancionSonando));

                        return false;
                    }
                })
                //     .setCancelButton(mContext.getString(R.string.cerrar))
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                .setCancelable(true)
                .show();
    }

    public static void DialogoSupArt(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.support_art);
        String mensaje = mContext.getString(R.string.msg_support_art);


        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                .setOkButton(mContext.getString(R.string.open_art), new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        AbrirPagina(mContext, tinyDB.getString(TBimagenFondo));

                        return false;
                    }
                })
                //  .setCancelButton(mContext.getString(R.string.cerrar))
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.WHITE))
                .setCancelable(true)
                .show();
    }

    public static void DialogoPoliticas2(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = mContext.getString(R.string.title_terms);

        //region mensaje de politicas en diferentes idiomas
        String mensaje = "Terms and Conditions\n" +
                "Last updated: January 06, 2021\n" +
                "\n" +
                "Please read these terms and conditions carefully before using Our Service.\n" +
                "\n" +
                "Interpretation and Definitions\n" +
                "Interpretation\n" +
                "The words of which the initial letter is capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or in plural.\n" +
                "\n" +
                "Definitions\n" +
                "For the purposes of these Terms and Conditions:\n" +
                "\n" +
                "Affiliate means an entity that controls, is controlled by or is under common control with a party, where \"control\" means ownership of 50% or more of the shares, equity interest or other securities entitled to vote for election of directors or other managing authority.\n" +
                "\n" +
                "Country refers to: Colombia\n" +
                "\n" +
                "Company (referred to as either \"the Company\", \"We\", \"Us\" or \"Our\" in this Agreement) refers to LUGU Music.\n" +
                "\n" +
                "Device means any device that can access the Service such as a computer, a cellphone or a digital tablet.\n" +
                "\n" +
                "Service refers to the Website.\n" +
                "\n" +
                "Terms and Conditions (also referred as \"Terms\") mean these Terms and Conditions that form the entire agreement between You and the Company regarding the use of the Service. This Terms and Conditions agreement has been created with the help of the Terms and Conditions Generator.\n" +
                "\n" +
                "Third-party Social Media Service means any services or content (including data, information, products or services) provided by a third-party that may be displayed, included or made available by the Service.\n" +
                "\n" +
                "Website refers to LUGU Music, accessible from lugumusic.com\n" +
                "\n" +
                "You means the individual accessing or using the Service, or the company, or other legal entity on behalf of which such individual is accessing or using the Service, as applicable.\n" +
                "\n" +
                "Acknowledgment\n" +
                "These are the Terms and Conditions governing the use of this Service and the agreement that operates between You and the Company. These Terms and Conditions set out the rights and obligations of all users regarding the use of the Service.\n" +
                "\n" +
                "Your access to and use of the Service is conditioned on Your acceptance of and compliance with these Terms and Conditions. These Terms and Conditions apply to all visitors, users and others who access or use the Service.\n" +
                "\n" +
                "By accessing or using the Service You agree to be bound by these Terms and Conditions. If You disagree with any part of these Terms and Conditions then You may not access the Service.\n" +
                "\n" +
                "You represent that you are over the age of 18. The Company does not permit those under 18 to use the Service.\n" +
                "\n" +
                "Your access to and use of the Service is also conditioned on Your acceptance of and compliance with the Privacy Policy of the Company. Our Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your personal information when You use the Application or the Website and tells You about Your privacy rights and how the law protects You. Please read Our Privacy Policy carefully before using Our Service.\n" +
                "\n" +
                "Links to Other Websites\n" +
                "Our Service may contain links to third-party web sites or services that are not owned or controlled by the Company.\n" +
                "\n" +
                "The Company has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that the Company shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with the use of or reliance on any such content, goods or services available on or through any such web sites or services.\n" +
                "\n" +
                "We strongly advise You to read the terms and conditions and privacy policies of any third-party web sites or services that You visit.\n" +
                "\n" +
                "Termination\n" +
                "We may terminate or suspend Your access immediately, without prior notice or liability, for any reason whatsoever, including without limitation if You breach these Terms and Conditions.\n" +
                "\n" +
                "Upon termination, Your right to use the Service will cease immediately.\n" +
                "\n" +
                "Limitation of Liability\n" +
                "Notwithstanding any damages that You might incur, the entire liability of the Company and any of its suppliers under any provision of this Terms and Your exclusive remedy for all of the foregoing shall be limited to the amount actually paid by You through the Service or 100 USD if You haven't purchased anything through the Service.\n" +
                "\n" +
                "To the maximum extent permitted by applicable law, in no event shall the Company or its suppliers be liable for any special, incidental, indirect, or consequential damages whatsoever (including, but not limited to, damages for loss of profits, loss of data or other information, for business interruption, for personal injury, loss of privacy arising out of or in any way related to the use of or inability to use the Service, third-party software and/or third-party hardware used with the Service, or otherwise in connection with any provision of this Terms), even if the Company or any supplier has been advised of the possibility of such damages and even if the remedy fails of its essential purpose.\n" +
                "\n" +
                "Some states do not allow the exclusion of implied warranties or limitation of liability for incidental or consequential damages, which means that some of the above limitations may not apply. In these states, each party's liability will be limited to the greatest extent permitted by law.\n" +
                "\n" +
                "\"AS IS\" and \"AS AVAILABLE\" Disclaimer\n" +
                "The Service is provided to You \"AS IS\" and \"AS AVAILABLE\" and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf and on behalf of its Affiliates and its and their respective licensors and service providers, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Service, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Service will meet Your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.\n" +
                "\n" +
                "Without limiting the foregoing, neither the Company nor any of the company's provider makes any representation or warranty of any kind, express or implied: (i) as to the operation or availability of the Service, or the information, content, and materials or products included thereon; (ii) that the Service will be uninterrupted or error-free; (iii) as to the accuracy, reliability, or currency of any information or content provided through the Service; or (iv) that the Service, its servers, the content, or e-mails sent from or on behalf of the Company are free of viruses, scripts, trojan horses, worms, malware, timebombs or other harmful components.\n" +
                "\n" +
                "Some jurisdictions do not allow the exclusion of certain types of warranties or limitations on applicable statutory rights of a consumer, so some or all of the above exclusions and limitations may not apply to You. But in such a case the exclusions and limitations set forth in this section shall be applied to the greatest extent enforceable under applicable law.\n" +
                "\n" +
                "Governing Law\n" +
                "The laws of the Country, excluding its conflicts of law rules, shall govern this Terms and Your use of the Service. Your use of the Application may also be subject to other local, state, national, or international laws.\n" +
                "\n" +
                "Disputes Resolution\n" +
                "If You have any concern or dispute about the Service, You agree to first try to resolve the dispute informally by contacting the Company.\n" +
                "\n" +
                "For European Union (EU) Users\n" +
                "If You are a European Union consumer, you will benefit from any mandatory provisions of the law of the country in which you are resident in.\n" +
                "\n" +
                "United States Legal Compliance\n" +
                "You represent and warrant that (i) You are not located in a country that is subject to the United States government embargo, or that has been designated by the United States government as a \"terrorist supporting\" country, and (ii) You are not listed on any United States government list of prohibited or restricted parties.\n" +
                "\n" +
                "Severability and Waiver\n" +
                "Severability\n" +
                "If any provision of these Terms is held to be unenforceable or invalid, such provision will be changed and interpreted to accomplish the objectives of such provision to the greatest extent possible under applicable law and the remaining provisions will continue in full force and effect.\n" +
                "\n" +
                "Waiver\n" +
                "Except as provided herein, the failure to exercise a right or to require performance of an obligation under this Terms shall not effect a party's ability to exercise such right or require such performance at any time thereafter nor shall be the waiver of a breach constitute a waiver of any subsequent breach.\n" +
                "\n" +
                "Translation Interpretation\n" +
                "These Terms and Conditions may have been translated if We have made them available to You on our Service. You agree that the original English text shall prevail in the case of a dispute.\n" +
                "\n" +
                "Changes to These Terms and Conditions\n" +
                "We reserve the right, at Our sole discretion, to modify or replace these Terms at any time. If a revision is material We will make reasonable efforts to provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at Our sole discretion.\n" +
                "\n" +
                "By continuing to access or use Our Service after those revisions become effective, You agree to be bound by the revised terms. If You do not agree to the new terms, in whole or in part, please stop using the website and the Service.\n" +
                "\n" +
                "Contact Us\n" +
                "If you have any questions about these Terms and Conditions, You can contact us:\n" +
                "\n" +
                "By email: lugulofimusic@gmail.com";

        if (Locale.getDefault().getLanguage().contains("es")) {
            mensaje = "Términos y Condiciones\n" +
                    "Última actualización: 6 de enero de 2021\n" +
                    "\n" +
                    "Lea estos términos y condiciones detenidamente antes de utilizar Nuestro Servicio.\n" +
                    "\n" +
                    "Interpretación y definiciones\n" +
                    "Interpretación\n" +
                    "Las palabras cuya letra inicial está en mayúscula tienen significados definidos en las siguientes condiciones. Las siguientes definiciones tendrán el mismo significado independientemente de que aparezcan en singular o en plural.\n" +
                    "\n" +
                    "Definiciones\n" +
                    "A los efectos de estos Términos y condiciones:\n" +
                    "\n" +
                    "Afiliado significa una entidad que controla, está controlada por o está bajo control común con una parte, donde \"control\" significa propiedad del 50% o más de las acciones, participación en el capital social u otros valores con derecho a voto para la elección de directores u otra autoridad administrativa .\n" +
                    "\n" +
                    "País se refiere a: Colombia\n" +
                    "\n" +
                    "Compañía (denominada \"la Compañía\", \"Nosotros\", \"Nos\" o \"Nuestro\" en este Acuerdo) se refiere a LUGU Music.\n" +
                    "\n" +
                    "Dispositivo significa cualquier dispositivo que pueda acceder al Servicio, como una computadora, un teléfono celular o una tableta digital.\n" +
                    "\n" +
                    "El servicio se refiere al sitio web.\n" +
                    "\n" +
                    "Términos y condiciones (también denominados \"Términos\") significan estos Términos y condiciones que forman el acuerdo completo entre Usted y la Compañía con respecto al uso del Servicio. Este acuerdo de términos y condiciones se ha creado con la ayuda del generador de términos y condiciones .\n" +
                    "\n" +
                    "Servicio de redes sociales de terceros significa cualquier servicio o contenido (incluidos datos, información, productos o servicios) proporcionado por un tercero que puede ser mostrado, incluido o puesto a disposición por el Servicio.\n" +
                    "\n" +
                    "El sitio web se refiere a LUGU Music, accesible desde lugumusic.com\n" +
                    "\n" +
                    "Usted significa la persona que accede o usa el Servicio, o la compañía u otra entidad legal en nombre de la cual dicha persona accede o usa el Servicio, según corresponda.\n" +
                    "\n" +
                    "Reconocimiento\n" +
                    "Estos son los Términos y Condiciones que rigen el uso de este Servicio y el acuerdo que opera entre Usted y la Compañía. Estos Términos y Condiciones establecen los derechos y obligaciones de todos los usuarios con respecto al uso del Servicio.\n" +
                    "\n" +
                    "Su acceso y uso del Servicio está condicionado a su aceptación y cumplimiento de estos Términos y Condiciones. Estos Términos y Condiciones se aplican a todos los visitantes, usuarios y otras personas que acceden o utilizan el Servicio.\n" +
                    "\n" +
                    "Al acceder o utilizar el Servicio, usted acepta estar sujeto a estos Términos y Condiciones. Si no está de acuerdo con alguna parte de estos Términos y condiciones, no podrá acceder al Servicio.\n" +
                    "\n" +
                    "Usted declara que es mayor de 18 años. La Compañía no permite que menores de 18 años utilicen el Servicio.\n" +
                    "\n" +
                    "Su acceso y uso del Servicio también está condicionado a su aceptación y cumplimiento de la Política de Privacidad de la Compañía. Nuestra Política de privacidad describe Nuestras políticas y procedimientos sobre la recopilación, uso y divulgación de Su información personal cuando usa la Aplicación o el Sitio web y le informa sobre Sus derechos de privacidad y cómo la ley lo protege. Lea nuestra Política de privacidad detenidamente antes de utilizar nuestro servicio.\n" +
                    "\n" +
                    "Enlaces a otros sitios web\n" +
                    "Nuestro Servicio puede contener enlaces a sitios web o servicios de terceros que no son propiedad ni están controlados por la Compañía.\n" +
                    "\n" +
                    "La Compañía no tiene control ni asume ninguna responsabilidad por el contenido, las políticas de privacidad o las prácticas de los sitios web o servicios de terceros. Además, reconoce y acepta que la Compañía no será responsable, directa o indirectamente, de ningún daño o pérdida causados \u200B\u200Bo presuntamente causados \u200B\u200Bpor o en conexión con el uso o la dependencia de dicho contenido, bienes o servicios disponibles en oa través de dichos sitios web o servicios.\n" +
                    "\n" +
                    "Le recomendamos encarecidamente que lea los términos y condiciones y las políticas de privacidad de cualquier sitio web o servicio de terceros que visite.\n" +
                    "\n" +
                    "Terminación\n" +
                    "Podemos rescindir o suspender su acceso de inmediato, sin previo aviso ni responsabilidad, por cualquier motivo, incluido, entre otros, si incumple estos Términos y condiciones.\n" +
                    "\n" +
                    "Tras la rescisión, su derecho a utilizar el Servicio cesará de inmediato.\n" +
                    "\n" +
                    "Limitación de responsabilidad\n" +
                    "Sin perjuicio de los daños en los que pueda incurrir, la responsabilidad total de la Compañía y cualquiera de sus proveedores bajo cualquier disposición de estos Términos y Su recurso exclusivo para todo lo anterior se limitará al monto realmente pagado por Usted a través del Servicio o 100 USD. si no ha comprado nada a través del Servicio.\n" +
                    "\n" +
                    "En la máxima medida permitida por la ley aplicable, en ningún caso la Compañía o sus proveedores serán responsables de ningún daño especial, incidental, indirecto o consecuente (incluidos, entre otros, daños por lucro cesante, pérdida de datos o otra información, por interrupción del negocio, por lesiones personales, pérdida de privacidad que surja de o de alguna manera relacionada con el uso o la imposibilidad de usar el Servicio, software de terceros y / o hardware de terceros utilizado con el Servicio, o de lo contrario en relación con cualquier disposición de estos Términos), incluso si la Compañía o cualquier proveedor han sido informados de la posibilidad de tales daños e incluso si el recurso no cumple con su propósito esencial.\n" +
                    "\n" +
                    "Algunos estados no permiten la exclusión de garantías implícitas o la limitación de responsabilidad por daños incidentales o consecuentes, lo que significa que algunas de las limitaciones anteriores pueden no aplicarse. En estos estados, la responsabilidad de cada parte estará limitada en la mayor medida permitida por la ley.\n" +
                    "\n" +
                    "Renuncia de responsabilidad \"TAL CUAL\" y \"SEGÚN DISPONIBILIDAD\"\n" +
                    "El Servicio se le proporciona \"TAL CUAL\" y \"SEGÚN DISPONIBILIDAD\" y con todas las fallas y defectos sin garantía de ningún tipo. En la medida máxima permitida por la ley aplicable, la Compañía, en su propio nombre y en nombre de sus Afiliadas y sus respectivos otorgantes de licencias y proveedores de servicios, renuncia expresamente a todas las garantías, ya sean expresas, implícitas, legales o de otro tipo, con respecto a la Servicio, incluidas todas las garantías implícitas de comerciabilidad, idoneidad para un propósito particular, título y no infracción, y garantías que puedan surgir del curso del trato, el curso del desempeño, el uso o la práctica comercial. Sin limitación a lo anterior, la Compañía no ofrece garantía ni compromiso, y no hace ninguna representación de ningún tipo de que el Servicio cumplirá con Sus requisitos, logrará los resultados previstos,\n" +
                    "\n" +
                    "Sin perjuicio de lo anterior, ni la Compañía ni ninguno de los proveedores de la compañía hacen ninguna representación o garantía de ningún tipo, expresa o implícita: (i) en cuanto al funcionamiento o disponibilidad del Servicio, o la información, contenido y materiales o productos. incluido en el mismo; (ii) que el Servicio será ininterrumpido o libre de errores; (iii) en cuanto a la precisión, confiabilidad o vigencia de cualquier información o contenido proporcionado a través del Servicio; o (iv) que el Servicio, sus servidores, el contenido o los correos electrónicos enviados desde o en nombre de la Compañía están libres de virus, scripts, troyanos, gusanos, malware, bombas de tiempo u otros componentes dañinos.\n" +
                    "\n" +
                    "Algunas jurisdicciones no permiten la exclusión de ciertos tipos de garantías o limitaciones sobre los derechos legales aplicables de un consumidor, por lo que algunas o todas las exclusiones y limitaciones anteriores pueden no aplicarse a usted. Pero en tal caso, las exclusiones y limitaciones establecidas en esta sección se aplicarán en la mayor medida exigible según la ley aplicable.\n" +
                    "\n" +
                    "Ley que rige\n" +
                    "Las leyes del País, excluyendo sus conflictos de reglas de leyes, regirán estos Términos y Su uso del Servicio. Su uso de la Aplicación también puede estar sujeto a otras leyes locales, estatales, nacionales o internacionales.\n" +
                    "\n" +
                    "Resolución de disputas\n" +
                    "Si tiene alguna inquietud o disputa sobre el Servicio, acepta primero intentar resolver la disputa de manera informal comunicándose con la Compañía.\n" +
                    "\n" +
                    "Para usuarios de la Unión Europea (UE)\n" +
                    "Si es un consumidor de la Unión Europea, se beneficiará de las disposiciones obligatorias de la ley del país en el que reside.\n" +
                    "\n" +
                    "Cumplimiento legal de Estados Unidos\n" +
                    "Usted declara y garantiza que (i) no se encuentra en un país que está sujeto al embargo del gobierno de los Estados Unidos, o que ha sido designado por el gobierno de los Estados Unidos como un país \"que apoya al terrorismo\", y (ii) no está incluido en cualquier lista del gobierno de los Estados Unidos de partes prohibidas o restringidas.\n" +
                    "\n" +
                    "Divisibilidad y renuncia\n" +
                    "Divisibilidad\n" +
                    "Si alguna disposición de estos Términos se considera inaplicable o inválida, dicha disposición se cambiará e interpretará para lograr los objetivos de dicha disposición en la mayor medida posible según la ley aplicable y las disposiciones restantes continuarán en pleno vigor y efecto.\n" +
                    "\n" +
                    "Renuncia\n" +
                    "Salvo lo dispuesto en el presente, el hecho de no ejercer un derecho o exigir el cumplimiento de una obligación en virtud de estos Términos no afectará la capacidad de una de las partes para ejercer dicho derecho o exigir dicho cumplimiento en cualquier momento posterior, ni constituirá una renuncia la renuncia a un incumplimiento. de cualquier incumplimiento posterior.\n" +
                    "\n" +
                    "Interpretación de traducción\n" +
                    "Estos Términos y Condiciones pueden haberse traducido si los hemos puesto a su disposición en nuestro Servicio. Usted acepta que el texto original en inglés prevalecerá en caso de disputa.\n" +
                    "\n" +
                    "Cambios a estos términos y condiciones\n" +
                    "Nos reservamos el derecho, a Nuestro exclusivo criterio, de modificar o reemplazar estos Términos en cualquier momento. Si una revisión es material, haremos los esfuerzos razonables para proporcionar un aviso de al menos 30 días antes de que entren en vigencia los nuevos términos. Lo que constituye un cambio material se determinará a Nuestro exclusivo criterio.\n" +
                    "\n" +
                    "Al continuar accediendo o utilizando Nuestro Servicio después de que esas revisiones entren en vigencia, usted acepta estar sujeto a los términos revisados. Si no está de acuerdo con los nuevos términos, en su totalidad o en parte, deje de usar el sitio web y el Servicio.\n" +
                    "\n" +
                    "Contáctenos\n" +
                    "Si tiene alguna pregunta sobre estos Términos y condiciones, puede contactarnos:\n" +
                    "\n" +
                    "Por correo electrónico: lugulofimusic@gmail.com";

        } else if (Locale.getDefault().getLanguage().contains("pt")) {
            mensaje = "\n" +
                    "Última atualização: 06 de janeiro de 2021\n" +
                    "\n" +
                    "Por favor, leia estes termos e condições cuidadosamente antes de usar nosso serviço.\n" +
                    "\n" +
                    "Interpretação e Definições\n" +
                    "Interpretação\n" +
                    "As palavras cuja letra inicial é maiúscula têm significados definidos nas seguintes condições. As seguintes definições devem ter o mesmo significado, independentemente de aparecerem no singular ou no plural.\n" +
                    "\n" +
                    "Definições\n" +
                    "Para os fins destes Termos e Condições:\n" +
                    "\n" +
                    "Afiliada significa uma entidade que controla, é controlada ou está sob o controle comum de uma parte, onde \"controle\" significa propriedade de 50% ou mais das ações, participação acionária ou outros títulos com direito a voto para eleição de diretores ou outra autoridade administrativa .\n" +
                    "\n" +
                    "País se refere a: Colômbia\n" +
                    "\n" +
                    "Company (referred to as either \"the Company\", \"We\", \"Us\" or \"Our\" in this Agreement) refers to LUGU Music.\n" +
                    "\n" +
                    "Device means any device that can access the Service such as a computer, a cellphone or a digital tablet.\n" +
                    "\n" +
                    "Service refers to the Website.\n" +
                    "\n" +
                    "Terms and Conditions (also referred as \"Terms\") mean these Terms and Conditions that form the entire agreement between You and the Company regarding the use of the Service. This Terms and Conditions agreement has been created with the help of the Terms and Conditions Generator.\n" +
                    "\n" +
                    "Third-party Social Media Service means any services or content (including data, information, products or services) provided by a third-party that may be displayed, included or made available by the Service.\n" +
                    "\n" +
                    "Website refers to LUGU Music, accessible from lugumusic.com\n" +
                    "\n" +
                    "You means the individual accessing or using the Service, or the company, or other legal entity on behalf of which such individual is accessing or using the Service, as applicable.\n" +
                    "\n" +
                    "Acknowledgment\n" +
                    "These are the Terms and Conditions governing the use of this Service and the agreement that operates between You and the Company. These Terms and Conditions set out the rights and obligations of all users regarding the use of the Service.\n" +
                    "\n" +
                    "Your access to and use of the Service is conditioned on Your acceptance of and compliance with these Terms and Conditions. These Terms and Conditions apply to all visitors, users and others who access or use the Service.\n" +
                    "\n" +
                    "By accessing or using the Service You agree to be bound by these Terms and Conditions. If You disagree with any part of these Terms and Conditions then You may not access the Service.\n" +
                    "\n" +
                    "You represent that you are over the age of 18. The Company does not permit those under 18 to use the Service.\n" +
                    "\n" +
                    "Your access to and use of the Service is also conditioned on Your acceptance of and compliance with the Privacy Policy of the Company. Our Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your personal information when You use the Application or the Website and tells You about Your privacy rights and how the law protects You. Please read Our Privacy Policy carefully before using Our Service.\n" +
                    "\n" +
                    "Links to Other Websites\n" +
                    "Our Service may contain links to third-party web sites or services that are not owned or controlled by the Company.\n" +
                    "\n" +
                    "The Company has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that the Company shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with the use of or reliance on any such content, goods or services available on or through any such web sites or services.\n" +
                    "\n" +
                    "We strongly advise You to read the terms and conditions and privacy policies of any third-party web sites or services that You visit.\n" +
                    "\n" +
                    "Termination\n" +
                    "We may terminate or suspend Your access immediately, without prior notice or liability, for any reason whatsoever, including without limitation if You breach these Terms and Conditions.\n" +
                    "\n" +
                    "Upon termination, Your right to use the Service will cease immediately.\n" +
                    "\n" +
                    "Limitation of Liability\n" +
                    "Notwithstanding any damages that You might incur, the entire liability of the Company and any of its suppliers under any provision of this Terms and Your exclusive remedy for all of the foregoing shall be limited to the amount actually paid by You through the Service or 100 USD if You haven't purchased anything through the Service.\n" +
                    "\n" +
                    "To the maximum extent permitted by applicable law, in no event shall the Company or its suppliers be liable for any special, incidental, indirect, or consequential damages whatsoever (including, but not limited to, damages for loss of profits, loss of data or other information, for business interruption, for personal injury, loss of privacy arising out of or in any way related to the use of or inability to use the Service, third-party software and/or third-party hardware used with the Service, or otherwise in connection with any provision of this Terms), even if the Company or any supplier has been advised of the possibility of such damages and even if the remedy fails of its essential purpose.\n" +
                    "\n" +
                    "Some states do not allow the exclusion of implied warranties or limitation of liability for incidental or consequential damages, which means that some of the above limitations may not apply. In these states, each party's liability will be limited to the greatest extent permitted by law.\n" +
                    "\n" +
                    "\"AS IS\" and \"AS AVAILABLE\" Disclaimer\n" +
                    "The Service is provided to You \"AS IS\" and \"AS AVAILABLE\" and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf and on behalf of its Affiliates and its and their respective licensors and service providers, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Service, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Service will meet Your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.\n" +
                    "\n" +
                    "Without limiting the foregoing, neither the Company nor any of the company's provider makes any representation or warranty of any kind, express or implied: (i) as to the operation or availability of the Service, or the information, content, and materials or products included thereon; (ii) that the Service will be uninterrupted or error-free; (iii) as to the accuracy, reliability, or currency of any information or content provided through the Service; or (iv) that the Service, its servers, the content, or e-mails sent from or on behalf of the Company are free of viruses, scripts, trojan horses, worms, malware, timebombs or other harmful components.\n" +
                    "\n" +
                    "Algumas jurisdições não permitem a exclusão de certos tipos de garantias ou limitações sobre os direitos legais aplicáveis \u200B\u200Bde um consumidor, portanto, algumas ou todas as exclusões e limitações acima podem não se aplicar a você. Mas, em tal caso, as exclusões e limitações estabelecidas nesta seção serão aplicadas na maior medida exequível sob a lei aplicável.\n" +
                    "\n" +
                    "Lei Aplicável\n" +
                    "As leis do país, excluindo seus conflitos de regras legais, regerão estes Termos e seu uso do Serviço. O uso do Aplicativo também pode estar sujeito a outras leis locais, estaduais, nacionais ou internacionais.\n" +
                    "\n" +
                    "Resolução de disputas\n" +
                    "Se você tiver qualquer dúvida ou dúvida sobre o Serviço, concorda em primeiro tentar resolver a disputa informalmente, entrando em contato com a Empresa.\n" +
                    "\n" +
                    "Para usuários da União Europeia (UE)\n" +
                    "Se for um consumidor da União Europeia, beneficiará de quaisquer disposições obrigatórias da lei do país em que reside.\n" +
                    "\n" +
                    "Conformidade Legal dos Estados Unidos\n" +
                    "Você declara e garante que (i) Você não está localizado em um país que está sujeito ao embargo do governo dos Estados Unidos, ou que foi designado pelo governo dos Estados Unidos como um país de \"apoio ao terrorismo\", e (ii) Você não está listado em qualquer lista do governo dos Estados Unidos de partes proibidas ou restritas.\n" +
                    "\n" +
                    "Divisibilidade e renúncia\n" +
                    "Separabilidade\n" +
                    "Se qualquer disposição destes Termos for considerada inexequível ou inválida, tal disposição será alterada e interpretada para atingir os objetivos de tal disposição na maior medida possível sob a lei aplicável e as disposições restantes continuarão em pleno vigor e efeito.\n" +
                    "\n" +
                    "Renúncia\n" +
                    "Exceto conforme disposto neste documento, a falha em exercer um direito ou em exigir o cumprimento de uma obrigação nos termos destes Termos não afetará a capacidade de uma parte de exercer tal direito ou exigir tal cumprimento em qualquer momento posterior, nem deverá ser a renúncia de uma violação constituirá uma renúncia de qualquer violação subsequente.\n" +
                    "\n" +
                    "Tradução de interpretação\n" +
                    "Estes Termos e Condições podem ter sido traduzidos se os tivermos disponibilizado para você em nosso serviço. Você concorda que o texto original em inglês deve prevalecer em caso de disputa.\n" +
                    "\n" +
                    "Mudanças nestes Termos e Condições\n" +
                    "Nós nos reservamos o direito, a nosso exclusivo critério, de modificar ou substituir estes Termos a qualquer momento. Se uma revisão for material, faremos todos os esforços razoáveis \u200B\u200Bpara fornecer um aviso com pelo menos 30 dias de antecedência à entrada em vigor de quaisquer novos termos. O que constitui uma alteração material será determinado a nosso exclusivo critério.\n" +
                    "\n" +
                    "Ao continuar a acessar ou usar o Nosso Serviço após essas revisões entrarem em vigor, Você concorda em obedecer aos termos revisados. Se você não concordar com os novos termos, no todo ou em parte, pare de usar o site e o Serviço.\n" +
                    "\n" +
                    "Contate-Nos\n" +
                    "Se você tiver alguma dúvida sobre estes Termos e Condições, pode entrar em contato conosco:\n" +
                    "\n" +
                    "Por email: lugulofimusic@gmail.com ";

        } else if (Locale.getDefault().getLanguage().contains("hi")) {
            mensaje = "\n" +
                    "अंतिम अद्यतन: ०६ जनवरी २०२१\n" +
                    "\n" +
                    "हमारी सेवा का उपयोग करने से पहले कृपया इन नियमों और शर्तों को ध्यान से पढ़ें।\n" +
                    "\n" +
                    "व्याख्या और परिभाषाएँ\n" +
                    "व्याख्या\n" +
                    "जिन शब्दों के प्रारंभिक अक्षर को बड़े अक्षरों में लिखा गया है, उनके अर्थ निम्न स्थितियों में परिभाषित होते हैं। निम्नलिखित परिभाषाओं का एक ही अर्थ होगा चाहे वे एकवचन में दिखाई दें या बहुवचन में।\n" +
                    "\n" +
                    "परिभाषाएं\n" +
                    "इन नियमों और शर्तों के प्रयोजनों के लिए:\n" +
                    "\n" +
                    "संबद्ध का मतलब एक ऐसी इकाई है जो किसी पार्टी द्वारा नियंत्रित, नियंत्रित या सामान्य नियंत्रण में है, जहां \"नियंत्रण\" का अर्थ है 50% या अधिक शेयरों का स्वामित्व, इक्विटी ब्याज या अन्य प्रतिभूतियों जो निदेशकों या अन्य प्रबंध प्राधिकरण के चुनाव के लिए वोट करने का हकदार हैं। ।\n" +
                    "\n" +
                    "देश को संदर्भित करता है: कोलंबिया\n" +
                    "\n" +
                    "Company (referred to as either \"the Company\", \"We\", \"Us\" or \"Our\" in this Agreement) refers to LUGU Music.\n" +
                    "\n" +
                    "Device means any device that can access the Service such as a computer, a cellphone or a digital tablet.\n" +
                    "\n" +
                    "Service refers to the Website.\n" +
                    "\n" +
                    "Terms and Conditions (also referred as \"Terms\") mean these Terms and Conditions that form the entire agreement between You and the Company regarding the use of the Service. This Terms and Conditions agreement has been created with the help of the Terms and Conditions Generator.\n" +
                    "\n" +
                    "Third-party Social Media Service means any services or content (including data, information, products or services) provided by a third-party that may be displayed, included or made available by the Service.\n" +
                    "\n" +
                    "Website refers to LUGU Music, accessible from lugumusic.com\n" +
                    "\n" +
                    "You means the individual accessing or using the Service, or the company, or other legal entity on behalf of which such individual is accessing or using the Service, as applicable.\n" +
                    "\n" +
                    "Acknowledgment\n" +
                    "These are the Terms and Conditions governing the use of this Service and the agreement that operates between You and the Company. These Terms and Conditions set out the rights and obligations of all users regarding the use of the Service.\n" +
                    "\n" +
                    "Your access to and use of the Service is conditioned on Your acceptance of and compliance with these Terms and Conditions. These Terms and Conditions apply to all visitors, users and others who access or use the Service.\n" +
                    "\n" +
                    "By accessing or using the Service You agree to be bound by these Terms and Conditions. If You disagree with any part of these Terms and Conditions then You may not access the Service.\n" +
                    "\n" +
                    "You represent that you are over the age of 18. The Company does not permit those under 18 to use the Service.\n" +
                    "\n" +
                    "Your access to and use of the Service is also conditioned on Your acceptance of and compliance with the Privacy Policy of the Company. Our Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your personal information when You use the Application or the Website and tells You about Your privacy rights and how the law protects You. Please read Our Privacy Policy carefully before using Our Service.\n" +
                    "\n" +
                    "Links to Other Websites\n" +
                    "Our Service may contain links to third-party web sites or services that are not owned or controlled by the Company.\n" +
                    "\n" +
                    "The Company has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that the Company shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with the use of or reliance on any such content, goods or services available on or through any such web sites or services.\n" +
                    "\n" +
                    "We strongly advise You to read the terms and conditions and privacy policies of any third-party web sites or services that You visit.\n" +
                    "\n" +
                    "Termination\n" +
                    "We may terminate or suspend Your access immediately, without prior notice or liability, for any reason whatsoever, including without limitation if You breach these Terms and Conditions.\n" +
                    "\n" +
                    "Upon termination, Your right to use the Service will cease immediately.\n" +
                    "\n" +
                    "Limitation of Liability\n" +
                    "Notwithstanding any damages that You might incur, the entire liability of the Company and any of its suppliers under any provision of this Terms and Your exclusive remedy for all of the foregoing shall be limited to the amount actually paid by You through the Service or 100 USD if You haven't purchased anything through the Service.\n" +
                    "\n" +
                    "To the maximum extent permitted by applicable law, in no event shall the Company or its suppliers be liable for any special, incidental, indirect, or consequential damages whatsoever (including, but not limited to, damages for loss of profits, loss of data or other information, for business interruption, for personal injury, loss of privacy arising out of or in any way related to the use of or inability to use the Service, third-party software and/or third-party hardware used with the Service, or otherwise in connection with any provision of this Terms), even if the Company or any supplier has been advised of the possibility of such damages and even if the remedy fails of its essential purpose.\n" +
                    "\n" +
                    "Some states do not allow the exclusion of implied warranties or limitation of liability for incidental or consequential damages, which means that some of the above limitations may not apply. In these states, each party's liability will be limited to the greatest extent permitted by law.\n" +
                    "\n" +
                    "\"AS IS\" and \"AS AVAILABLE\" Disclaimer\n" +
                    "The Service is provided to You \"AS IS\" and \"AS AVAILABLE\" and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf and on behalf of its Affiliates and its and their respective licensors and service providers, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Service, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Service will meet Your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.\n" +
                    "\n" +
                    "Without limiting the foregoing, neither the Company nor any of the company's provider makes any representation or warranty of any kind, express or implied: (i) as to the operation or availability of the Service, or the information, content, and materials or products included thereon; (ii) that the Service will be uninterrupted or error-free; (iii) as to the accuracy, reliability, or currency of any information or content provided through the Service; or (iv) that the Service, its servers, the content, or e-mails sent from or on behalf of the Company are free of viruses, scripts, trojan horses, worms, malware, timebombs or other harmful components.\n" +
                    "\n" +
                    "कुछ क्षेत्राधिकार किसी उपभोक्ता के लागू वैधानिक अधिकारों पर कुछ प्रकार की वारंटी या सीमाओं के बहिष्कार की अनुमति नहीं देते हैं, इसलिए कुछ या सभी उपरोक्त बहिष्करण और सीमाएं आपके लिए लागू नहीं हो सकती हैं। लेकिन ऐसे मामले में इस खंड में निर्धारित बहिष्करण और सीमाएं लागू कानून के तहत लागू होने वाली सबसे बड़ी सीमा तक लागू की जाएंगी।\n" +
                    "\n" +
                    "शासकीय कानून\n" +
                    "देश के कानून, कानून के नियमों के टकराव को छोड़कर, इस नियम और सेवा के आपके उपयोग को नियंत्रित करेंगे। एप्लिकेशन का आपका उपयोग अन्य स्थानीय, राज्य, राष्ट्रीय या अंतर्राष्ट्रीय कानूनों के अधीन भी हो सकता है।\n" +
                    "\n" +
                    "विवाद का समाधान\n" +
                    "यदि आपको सेवा के बारे में कोई चिंता या विवाद है, तो आप पहले कंपनी से संपर्क करके अनौपचारिक रूप से विवाद को हल करने का प्रयास करते हैं।\n" +
                    "\n" +
                    "यूरोपीय संघ (ईयू) के उपयोगकर्ताओं के लिए\n" +
                    "यदि आप एक यूरोपीय संघ के उपभोक्ता हैं, तो आप उस देश के कानून के किसी अनिवार्य प्रावधान से लाभान्वित होंगे, जिसमें आप निवासी हैं।\n" +
                    "\n" +
                    "संयुक्त राज्य अमेरिका कानूनी अनुपालन\n" +
                    "आप प्रतिनिधित्व करते हैं और वारंट करते हैं कि (i) आप उस देश में स्थित नहीं हैं जो संयुक्त राज्य सरकार के अधीन है, या जिसे संयुक्त राज्य सरकार ने \"आतंकवादी समर्थन\" देश के रूप में नामित किया है, और (ii) आप नहीं हैं निषिद्ध या प्रतिबंधित पार्टियों की किसी भी संयुक्त राज्य सरकार की सूची में सूचीबद्ध है।\n" +
                    "\n" +
                    "संवेदनशीलता और छूट\n" +
                    "विच्छेदनीयता\n" +
                    "यदि इन शर्तों के किसी भी प्रावधान को अप्राप्य या अमान्य ठहराया जाता है, तो इस तरह के प्रावधान को बदल दिया जाएगा और इस तरह के प्रावधान के उद्देश्यों को लागू कानून के तहत सबसे बड़ी हद तक पूरा करने के लिए व्याख्या की जाएगी और शेष प्रावधान पूर्ण बल और प्रभाव में जारी रहेंगे।\n" +
                    "\n" +
                    "त्याग\n" +
                    "इसके अतिरिक्त, इस नियम के तहत किसी अधिकार का प्रयोग करने या किसी दायित्व की आवश्यकता की पूर्ति करने में विफलता किसी पार्टी के ऐसे अधिकार का प्रयोग करने की क्षमता को प्रभावित नहीं करेगी या उसके बाद किसी भी समय ऐसे प्रदर्शन की आवश्यकता नहीं होगी और न ही किसी उल्लंघन की माफी एक छूट का गठन करेगी। किसी भी बाद के उल्लंघन के।\n" +
                    "\n" +
                    "अनुवाद व्याख्या\n" +
                    "इन नियमों और शर्तों का अनुवाद हो सकता है अगर हमने उन्हें हमारी सेवा में आपको उपलब्ध कराया है। आप सहमत हैं कि विवाद के मामले में मूल अंग्रेजी पाठ प्रबल होगा।\n" +
                    "\n" +
                    "इन नियमों और शर्तों में बदलाव\n" +
                    "हम किसी भी समय इन शर्तों को संशोधित या बदलने के लिए, अपने एकमात्र विवेक पर अधिकार सुरक्षित रखते हैं। यदि कोई संशोधन होता है, तो हम किसी भी नए नियम के प्रभावी होने से पहले कम से कम 30 दिनों का नोटिस प्रदान करने के लिए उचित प्रयास करेंगे। हमारे एकमात्र विवेक पर एक सामग्री परिवर्तन का गठन किया जाएगा।\n" +
                    "\n" +
                    "उन संशोधनों के प्रभावी होने के बाद हमारी सेवा तक पहुंच या उपयोग जारी रखने से, आप संशोधित शर्तों से बाध्य होने के लिए सहमत होते हैं। यदि आप पूरे या आंशिक रूप से नए शब्दों से सहमत नहीं हैं, तो कृपया वेबसाइट और सेवा का उपयोग करना बंद कर दें।\n" +
                    "\n" +
                    "संपर्क करें\n" +
                    "यदि आपके पास इन नियमों और शर्तों के बारे में कोई प्रश्न हैं, तो आप हमसे संपर्क कर सकते हैं:\n" +
                    "\n" +
                    "ईमेल द्वारा: lugulofimusic@gmail.com";
        }


        //endregion

        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        /*CustomDialog：
        CustomDialog.show((AppCompatActivity) mContext, R.layout.layout_full_dialog, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {


            }
        }).setFullScreen(true);

         */


        OnDialogButtonClickListener nextStepListener = new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                return false;
            }
        };

        /*
        FullScreenDialog
                .show((AppCompatActivity) mContext, R.layout.layout_full_dialog, new FullScreenDialog.OnBindView() {
                    @Override
                    public void onBind(FullScreenDialog dialog, View rootView) {

                    }
                })
                .setOkButton("OK", nextStepListener)
                .setCancelButton("CANCEL")
                .setTitle("TITLE")
        ;
         */

        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                .setBackgroundColor(mContext.getResources().getColor(R.color.fondo_blancoazul))
                .setOkButton(mContext.getString(R.string.accept_terms), new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                        DialogSettings.theme = DialogSettings.THEME.DARK;

                        MessageDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.accept_terms), "").setOkButton("YES").setCancelButton("NO")
                                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                        tinydb.putBoolean(TBpoliticas, true);
                                        mContext.startActivity(new Intent(mContext, act_main.class));
                                        ((Activity) mContext).finish();
                                        return false;
                                    }
                                })
                                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                        tinydb.putBoolean(TBpoliticas, false);
                                        ((Activity) mContext).finish();
                                        return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
                                    }
                                });

                        return false;
                    }
                })
                .setCancelButton(mContext.getString(R.string.cerrar))
                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        tinydb.putBoolean(TBpoliticas, false);
                        ((AppCompatActivity) mContext).finish();
                        return false;
                    }
                })
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setCancelable(true)
                .show();
    }

    public static void DialogoModoOffline(Context mContext) {

        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        MessageDialog
                .show((AppCompatActivity) mContext, mContext.getString(R.string.modo_offline), mContext.getString(R.string.trabajando_offline) + " \uD83D\uDE0A", "OK")
                .setButtonOrientation(LinearLayout.VERTICAL);
    }

    public static void Dialogo5Estrellas(Context mContext) {


        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        MessageDialog
                .show((AppCompatActivity) mContext, "\uD83C\uDF1F\uD83C\uDF1F\uD83C\uDF1F\uD83C\uDF1F\uD83C\uDF1F", mContext.getString(R.string.msg_etstrellas) + "\uD83D\uDE0A", "OK")
                .setCancelButton(mContext.getResources().getString(R.string.cerrar)).setButtonOrientation(LinearLayout.HORIZONTAL).setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {

                DialogoSugerencia(mContext);

                return false;
            }
        }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {

                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UrlAppPlayStore)));
                } catch (Exception e) {
                    //e.toString();
                }

                return false;
            }
        });


    }

    public static void CargarImagenVHS(Context mContext) {

        Glide.with(mContext)
                .load("https://lugumusic.page.link/5mEq")
                //.error(R.drawable.ic_alert)
                .transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(ivFondoGif);

        SonidoVHS(mContext, soundVHS, true);

    }

    public static void SonidoVHS(Context mContext, MediaPlayer mediaPlayer, boolean activo) {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(mContext, R.raw.tv09);
        } else {
            if (activo) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }


    }

}









