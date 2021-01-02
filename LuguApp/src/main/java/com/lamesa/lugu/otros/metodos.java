package com.lamesa.lugu.otros;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amplitude.api.Amplitude;
import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Logger;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.lugu.BuildConfig;
import com.lamesa.lugu.R;
import com.lamesa.lugu.adapter.AdapterFavoritos;
import com.lamesa.lugu.adapter.AdapterHistorial;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.otros.numberPicker.NumberPicker;
import com.lamesa.lugu.otros.statics.Animacion;
import com.lamesa.lugu.player.MediaNotificationManager;
import com.naveed.ytextractor.ExtractorException;
import com.naveed.ytextractor.YoutubeStreamExtractor;
import com.naveed.ytextractor.model.YTMedia;
import com.naveed.ytextractor.model.YTSubtitles;
import com.naveed.ytextractor.model.YoutubeMeta;
import com.opencsv.CSVReader;

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
import java.util.Collections;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.activity.act_main.andExoPlayerView;
import static com.lamesa.lugu.activity.act_main.bottomNavigationHis_Fav;
import static com.lamesa.lugu.activity.act_main.contenidoHome;
import static com.lamesa.lugu.activity.act_main.contenidoSearch;
import static com.lamesa.lugu.activity.act_main.getListas;
import static com.lamesa.lugu.activity.act_main.ivLikeDislike;
import static com.lamesa.lugu.activity.act_main.ivOpcionBucle;
import static com.lamesa.lugu.activity.act_main.ivPlayPause;
import static com.lamesa.lugu.activity.act_main.ivSleep;
import static com.lamesa.lugu.activity.act_main.mAdapterFavoritos;
import static com.lamesa.lugu.activity.act_main.mAdapterHistorial;
import static com.lamesa.lugu.activity.act_main.mediaNotificationManager;

import static com.lamesa.lugu.activity.act_main.mrvFavoritos;
import static com.lamesa.lugu.activity.act_main.mrvHistorial;
import static com.lamesa.lugu.activity.act_main.pbCargandoRadio;
import static com.lamesa.lugu.activity.act_main.spinBuffering;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.activity.act_main.tvSleep;
import static com.lamesa.lugu.activity.act_main.waveBlack;
import static com.lamesa.lugu.activity.act_main.waveColor;
import static com.lamesa.lugu.otros.Firebase.EnviarSolicitud;
import static com.lamesa.lugu.otros.Firebase.EnviarSugerencia;
import static com.lamesa.lugu.otros.Firebase.ReportarEpisodio;
import static com.lamesa.lugu.otros.Firebase.ReportarFilm;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_ALEATORIO;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_BUCLE;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBfechaCambiosData;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBlistHistorial;
import static com.lamesa.lugu.otros.statics.constantes.TBmodoReproductor;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnumeroCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.mixActualizarApp;
import static com.lamesa.lugu.otros.statics.constantes.mixCompartirApp;
import static com.lamesa.lugu.otros.statics.constantes.setDebugActivo;


public class metodos {


    public static FirebaseUser user;
    public static ArrayList<String> listEpisodiosListView;
    private static FirebaseAuth mAuth;
    private static PermissionListener permissionlistener;
    public static CountDownTimer countDownTimer;
    public static NumberPicker numberPicker;


    public static void CargarHome(Context mContext) {
        if(contenidoHome.getVisibility()== GONE) {
            contenidoSearch.startAnimation(Animacion.alpha_out(mContext));
            contenidoSearch.setVisibility(GONE);
        }
        contenidoHome.setVisibility(VISIBLE);
    }

    public static void initFirebase(final Context mContext, TinyDB tinyDB) {

        try {

            DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("otros");
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


                            //si es version la mesa,, remplazar los valores
                            if (BuildConfig.APPLICATION_ID.toLowerCase().contains("mesa")) {
                                versionNueva = Integer.parseInt(dataSnapshot.child("actualizacion").child("app-lamesa").child("version").getValue().toString());
                                estado = dataSnapshot.child("actualizacion").child("app-lamesa").child("estado").getValue(Boolean.class);
                                urlDescarga = dataSnapshot.child("actualizacion").child("app-lamesa").child("urlDescarga").getValue(String.class);
                                cancelable = dataSnapshot.child("actualizacion").child("app-lamesa").child("cancelable").getValue(Boolean.class);
                                mensaje = dataSnapshot.child("actualizacion").child("app-lamesa").child("mensaje").getValue().toString();
                            }


                            int versionActual = BuildConfig.VERSION_CODE;

                            if (estado == true && versionNueva > versionActual) {

                                try {


                                    if (mContext != null) {
                                        setDebug("metodos", "initFirebase", "d", "Mostrando dialogo de actualización...", setDebugActivo);


                                        DialogoActualizar(mContext, versionNueva, mensaje, urlDescarga, cancelable);


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
                            if (!fechaCambioData.toLowerCase().contains(fechaUltimoCambio) || fechaUltimoCambio.isEmpty()) {

                            } else {
                                setDebug("metodos", "initFirebase", "d", "No hay contenido por actualizar..", setDebugActivo);
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
        setDebug("metodos", "DialogoActualizar", "d", "Mostrando DialogoActualizar...", setDebugActivo);


        String obligatorio = "Actualización obligatoria";
        String titulo = "¡Actualización disponible! \nversión " + version;
        if (cancelable) {
            obligatorio = "(Actualización opcional)";
        } else {
            obligatorio = "(Actualización obligatoria)";
        }

        titulo = titulo + "\n" + obligatorio;
        mensaje = mensaje + "\n(Es necesario el permiso de almacenamiento y de instalación desde " + mContext.getResources().getString(R.string.app_name) + ")"+"\n Nota: Si la actualización es obligatoria y NO funciona, puede descargar la ultima versión desde la pagina web.";

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        MessageDialog.show((AppCompatActivity) mContext, titulo, mensaje, "ACTUALIZAR").setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN)).setButtonOrientation(LinearLayout.VERTICAL).setOtherButton("VISITAR PAGINA").setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                //    Toast.makeText(mContext, "sdasdasdd", Toast.LENGTH_SHORT).show();
                AbrirPagina(mContext);
                return false;
            }
        }).setCancelable(cancelable).setOnOkButtonClickListener((baseDialog, v) -> {
            DescargarActualizacion(mContext, urlDescarga);
            return false;
        });


    }

    public static <mContext> void DescargarActualizacion(Context mContext, String urlDescarga) {

        if (urlDescarga.contains("http") || urlDescarga.contains("https")) {

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


                    File toInstall = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));

                    if (!toInstall.exists()) {
                        toInstall.mkdirs();
                    }

                    String destinoUpdate = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update.apk";


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
                                            ReportarEpisodio(mContext, "Error en descarga, " + errMsg, "0001", "0", "", "", "");
                                            Toast.makeText(mContext, "Error en la descarga, " + errMsg, Toast.LENGTH_SHORT).show();
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

                }


                //endregion


                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    TipDialog.show((AppCompatActivity) mContext, "Son necesarios  permisos de almacenamiento para realizar la descarga.", TipDialog.TYPE.WARNING).setTipTime(60000).setCancelable(true);
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


        } else {
            TipDialog.show((AppCompatActivity) mContext, "Error al descargar, por favor intente mas tarde.", TipDialog.TYPE.ERROR).setCancelable(true).setTipTime(10000);

        }

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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name) + " - " + "La mejor alternativa de Netflix totalmente gratis");
            String infoApp = "Disfruta de: " + saltoDeLinea + "*Ver peliculas y series" + saltoDeLinea + "*Añadir a tus favoritos" + saltoDeLinea + "*Videos en calidad HD" + saltoDeLinea + "*Ver video en ventana flotante" + saltoDeLinea + "y mucho mas..." + saltoDeLinea;
            String shareMessage = "Para mas información, descarga la app aqui:" + "\n\n";
            shareMessage = shareMessage + "https://repelisplusapp.page.link/verpelisonline";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);


            mContext.startActivity(Intent.createChooser(shareIntent, "Compartir con:"));
        } catch (Exception e) {
            //e.toString();
        }


    }

    public static void AbrirPagina(Context mContext) {


        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://repelisplusapp.page.link/verpelisonline")));
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

    public static void DialogoSugerencia(Context mContext) {

        String saltoDeLinea = "\n";
        String titulo = "Enviar sugerencia";
        String mensaje = "Aqui puedes enviar cualquier sugerencia que desees" + saltoDeLinea + saltoDeLinea + "Esto nos ayudara a implementar nuevo contenido y/o mejoras";
        String hintText = "Sugerencia";


        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        InputDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setTitle(titulo).setMessage(mensaje)
                //  .setInputText("111111")
                .setOkButton("ENVIAR", new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        if (inputStr.equals("")) {
                            TipDialog.show((AppCompatActivity) mContext, "El espacio no puede estar vacío", TipDialog.TYPE.ERROR);
                            SolicitarFilm(mContext);
                            return false;
                        } else {
                            EnviarSugerencia(mContext, inputStr);
                            return true;
                        }
                    }
                })
                .setCancelButton("CERRAR")
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

    public static void DialogoEliminarLista(Context mContext, List<ModelCancion> mList, String keyTinyDB) {

        String saltoDeLinea = "\n";
        String titulo = "¿De sea eliminar el contenido de esta lista?";

        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        MessageDialog.build((AppCompatActivity) mContext)
                .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                .setOkButton("SI")
                .setMessage("Esta acción no se puede deshacer")
                .setTitle(titulo)
                .setCancelButton("CERRAR")
                .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                .setCancelable(true)
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        // eliminar contenido
                        mList.removeAll(mList);

                        tinyDB.putListModelCancion(keyTinyDB, mList);
                        TipDialog.show((AppCompatActivity) mContext,"Contenido eliminado.", TipDialog.TYPE.SUCCESS);
                        if(keyTinyDB.contains(TBlistFavoritos)){
                            UpdateAdapterFavoritos(mContext);
                            if(bottomNavigationHis_Fav!=null) {
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);
                            }
                        } else if(keyTinyDB.contains(TBlistHistorial)) {
                            UpdateAdapterHistorial(mContext);
                            if(bottomNavigationHis_Fav!=null) {
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);
                            }
                        }


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


    public static void DialogoTemporizador(Context mContext){

        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        //对于未实例化的布局：
        MessageDialog.show((AppCompatActivity) mContext, "TEMPORIZADOR DE APAGADO", "Seleccione el tiempo en el cual la musicá se apagará","OK")
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
                if(numberPicker.getValue()>0) {
                    ApagarAutoApagado(mContext);
                    EncenderAutoApagado(mContext, numberPicker.getValue());
                }
                return false;
            }
        });

    }


    public static void DialogoEnviarReporteFilm(Context mContext, String idFilm, String nombreFilm) {


        List<String> opcionMenu = new ArrayList<>();
        opcionMenu.add("Información incorrecta");
        opcionMenu.add("NO carga algún elemento");
        opcionMenu.add("Falta información");
        opcionMenu.add("Falta episodios");
        opcionMenu.add("Fallan varios episodios");


//您自己的Adapter

        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
        DialogSettings.theme = DialogSettings.THEME.DARK;


        BaseAdapter baseAdapter = new ArrayAdapter(mContext, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);


        BottomMenu.show((AppCompatActivity) mContext, baseAdapter, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
                //   Toast.makeText(act_main.this, "MAIN", Toast.LENGTH_SHORT).show();
                ReportarFilm(mContext, opcionMenu.get(index), idFilm, nombreFilm);
            }


        }).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("SELECCIONE LA CAUSA DEL REPORTE:");


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
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


        if (mostrarSoloUnaVez == false) {
            String saltoDeLinea = "\n";

            MessageDialog.show((AppCompatActivity) mContext, "Sobre PelisPlusHD", "PelisPlusHD app , es una aplicación que funciona como interfaz y buscador de series y peliculas que son alojadas por terceros.." + saltoDeLinea
                    + saltoDeLinea +
                    "Esta app aun esta en desarrollo, se actualiza el contenido diareamente pero con el envio de peticiones, sugerencias y reportes ayudara a mantener esta aplicacíon en desarrollo, el uso de esta app es totalmente gratuito. " +
                    "¡Bienvenido a PelisPlusHD, disfrutalo!" +
                    saltoDeLinea + saltoDeLinea +
                    "-Trabajando en:" + saltoDeLinea +
                    "* Modo offline (descargar episodios)" + saltoDeLinea +
                    "* Notificaciones" + saltoDeLinea +
                    "* Personalización" + saltoDeLinea +
                    "* Login" + saltoDeLinea + saltoDeLinea + saltoDeLinea +
                    "¨*Con tu apoyo será posible :,)*¨", "OK")
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

        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;


        String saltoDeLinea = "\n";

        MessageDialog.show((AppCompatActivity) mContext, "ENVIAR CAMBIOS A FB", "Se enviara la fecha a 'fechaCambiosData' y hara que los usuarios actualizen la lista contenido", "ENVIAR").setCancelButton("NO")
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {

                        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("otros").child("utilidad").child("fechaCambiosData");
                        // para la fecha
                        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
                        String fecha = df.format(Calendar.getInstance().getTime());
                        mref.setValue(fecha);
                        TipDialog.show((AppCompatActivity) mContext,"Fecha de cambio enviada.", TipDialog.TYPE.SUCCESS);

                        return false;
                    }
                })
                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        Toast.makeText(mContext, "Vale.", Toast.LENGTH_SHORT).show();
                        baseDialog.doDismiss();
                        return true;                    //位于“取消”位置的按钮点击后无法关闭对话框

                    }
                });





    }

    public static void CargarInterAd(Context mContext, String ad_unit_id, int cantidadAleatoria){

        Random numRandom = new Random();
        int numPosibilidad = numRandom.nextInt(cantidadAleatoria);

        //   Toast.makeText(this, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();


        if (numPosibilidad == 3) {

            AdMesa.createLoadInterstitial(mContext, ad_unit_id, null);
        }


     //   Toast.makeText(mContext, String.valueOf(numPosibilidad), Toast.LENGTH_SHORT).show();




    }

    public static void getLinkAndPlay(Context mContext, String linkYT, int opcion){



        if(pbCargandoRadio!=null){
            pbCargandoRadio.startAnimation(Animacion.anim_alpha_out(mContext));
            pbCargandoRadio.setVisibility(VISIBLE);
            pbCargandoRadio.startAnimation(Animacion.anim_alpha_in(mContext));
        }

        if (opcion == 1) {
            setLogInfo(mContext,"getLinkAndPlay.YouTubeExtractor","Inicia extracion opcion 1",false);

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

                    } else {
                        getLinkAndPlay(mContext, linkYT, 2);
                    }
                }

            }.extract(linkYT, false, false);


        } else if (opcion == 2){

            setLogInfo(mContext,"getLinkAndPlay.YoutubeStreamExtractor","Inicia extracion opcion 2",false);
            new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){
                @Override
                public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                    setLogInfo(mContext,"getLinkAndPlay.YoutubeStreamExtractor","onExtractionDone",false);
                    //url to get subtitle

                    for (YTMedia media:adativeStream) {

                        // solo extraer link de audio
                        if (media.getAudioQuality() != null && media.getAudioQuality().contains("AUDIO")){
                            String linkCancion = media.getUrl();
                            System.out.println("newlofi downloadUrl  media.getUrl() == "+media.getUrl());
                            System.out.println("newlofi downloadUrl  media.getAudioQuality() == "+media.getAudioQuality());
                            System.out.println("newlofi downloadUrl  media.getBitrate() == "+media.getBitrate());
                            System.out.println("newlofi downloadUrl  media.getItag() == "+media.getItag());
                            // reproducir cancion
                            ReproducirCancion(mContext, linkCancion);
                            break;
                        }

                    }
                }
                @Override
                public void onExtractionGoesWrong(final ExtractorException e) {
                    // Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(mContext, "Hubo un error con esta canción :(", Toast.LENGTH_LONG).show();
                    setLogInfo(mContext,"getLinkAndPlay.YoutubeStreamExtractor.onExtractionGoesWrong",e.getMessage(),true);

                    // al onExtractionGoesWrong, cargar otra cancion de la misma categoria seleccionanda por el usuario
                    Random random = new Random();
                    List<ModelCancion> mlistCategoriaSonando = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);

                    if(mlistCategoriaSonando.size() != 0 && mlistCategoriaSonando!=null) {
                        int numCancionSonar = random.nextInt(mlistCategoriaSonando.size());

                        // obetneer link de la nueva cancion seleccionada
                        getLinkAndPlay(mContext, mlistCategoriaSonando.get(numCancionSonar).getLinkYT(), 1);


                        // guardar datos de la cancion sonando en TinyDB
                        tinyDB.putInt(TBnumeroCancionSonando, numCancionSonar);
                        tinyDB.putString(TBidCancionSonando, mlistCategoriaSonando.get(numCancionSonar).getId());
                        tinyDB.putString(TBnombreCancionSonando, mlistCategoriaSonando.get(numCancionSonar).getCancion());
                        tinyDB.putString(TBartistaCancionSonando, mlistCategoriaSonando.get(numCancionSonar).getArtista());
                        tinyDB.putString(TBcategoriaCancionSonando, tinyDB.getString(TBcategoriaCancionSonando));
                        tinyDB.putString(TBlinkCancionSonando, mlistCategoriaSonando.get(numCancionSonar).getLinkYT());


                    } else {
                        getListas(mContext);
                    }




                    // ocultar progressBar de act_main
                    if(pbCargandoRadio!=null){
                        pbCargandoRadio.startAnimation(Animacion.anim_alpha_in(mContext));
                        pbCargandoRadio.setVisibility(GONE);
                        pbCargandoRadio.startAnimation(Animacion.anim_alpha_out(mContext));
                    }
                }
            }).Extract(linkYT);
            //use .useDefaultLogin() to extract age restricted videos
        }


    }
    
    public static void ReproducirCancion(Context mContext,String linkCancion){
        if(andExoPlayerView!=null) {
            setLogInfo(mContext,"ReproducirCancion","Inicia ExoPlayerView.setSource",false);
            andExoPlayerView.setSource(linkCancion);
            // reproducir
            andExoPlayerView.PlayOrPause(MediaNotificationManager.STATE_PLAY);
        }

    }

    public static void setLogInfo(Context mContext, String TAG, String msg, boolean error) {

        String errormsg = "";
        if (error) {
            errormsg = "ErrorBug : | ";
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

    public static void GuardarCancionHistorial(Context mContext, String idCancionSonando){
        // agregar cancion a historial

        List<ModelCancion> tinyListCancionxCategoria = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);
        List<ModelCancion> tinyListHistorial = tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class);

        for(ModelCancion cancion : tinyListCancionxCategoria){
            if(cancion.getId().equals(idCancionSonando)){
                tinyListHistorial.add(cancion);
            }
        }
        if(tinyListHistorial.size()<=1){
            bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);
        }
        // para que se muestre de primeras las ultimas agregadas
        // Collections.reverse(tinyListHistorial);
        // guardar lista en tiny db y sin duplicados
        Collections.reverse(tinyListHistorial);
        tinyDB.putListModelCancion(TBlistHistorial,  EliminarDuplicadosModelCancion(tinyListHistorial));

        //region actualizar lista de historial
        mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
        bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);
        //endregion

    }

    public static void GuardarCancionFavoritos(Context mContext, String idCancionSonando, Boolean favorito){


        // buscar  el numero de la cancion en la catergoria que está sonando
        List<ModelCancion> tinyListCancionxCategoria = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);
        // obtener lista de favoritos desde tinydb
        List<ModelCancion> tinyListFavoritos = tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class);

        if(favorito) {
            // agregar cancion a favoritos


                // Collections para que se muestre de primeras las ultimas agregadas
            Collections.reverse(tinyListFavoritos);


            for(int i = 0; i<tinyListCancionxCategoria.size(); i++) {
                if (tinyListCancionxCategoria.get(i).getId().equals(idCancionSonando)) {
                    tinyListFavoritos.add(tinyListCancionxCategoria.get(i));
                    Toast.makeText(mContext, "Se agregó a favoritos", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

                // guardar lista en tiny db y sin duplicados
                tinyDB.putListModelCancion(TBlistFavoritos, EliminarDuplicadosModelCancion(tinyListFavoritos));

                // actualizar lista de favoritos
              //  UpdateAdapterFavoritos(mContext);

                // cambiar icono de ivLikeDislike a like

                ivLikeDislike.startAnimation(Animacion.exit_ios_anim(mContext));
                ivLikeDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_like));
                ivLikeDislike.startAnimation(Animacion.enter_ios_anim(mContext));

            //region actualizar lista de favoritos
            mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
            bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);
            //endregion



            } else {

                // eliminar de favoritos


                // si en la lista de favoritos se encuentra la cancion que esta sonando se eliminará

                for(int i = 0; i<tinyListFavoritos.size(); i++){
                    if (tinyListFavoritos.get(i).getId().equals(idCancionSonando)) {
                        tinyListFavoritos.remove(i);
                        Toast.makeText(mContext, "Se eliminó de favoritos", Toast.LENGTH_SHORT).show();
                    }
                }


                // guardar lista en tiny db y sin duplicados
                tinyDB.putListModelCancion(TBlistFavoritos, EliminarDuplicadosModelCancion(tinyListFavoritos));
                // actualizar lista de favoritos
              //  UpdateAdapterFavoritos(mContext);

                // cambiar icono de ivLikeDislike a like

                ivLikeDislike.startAnimation(Animacion.exit_ios_anim(mContext));
                ivLikeDislike.setImageDrawable(mContext.getResources().getDrawable(R.drawable.learn_ic_dislike));
                ivLikeDislike.startAnimation(Animacion.enter_ios_anim(mContext));

                //region actualizar lista de favoritos
                mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);
                //endregion

            }

    }

    public static void CheckIsFavorite(Context mContext, String idCancionSonando){
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

    public static List<ModelCancion> EliminarDuplicadosModelCancion(List<ModelCancion> mListModelCancion){

            Map<String, ModelCancion> cleanMap = new LinkedHashMap<String, ModelCancion>();
            for (int i = 0; i < mListModelCancion.size(); i++) {
                cleanMap.put(mListModelCancion.get(i).getId(), mListModelCancion.get(i));
            }
            List<ModelCancion> list = new ArrayList<ModelCancion>(cleanMap.values());
            return list;

    }

    public static void UpdateAdapterHistorial(Context mContext){


        if(mAdapterHistorial!=null && mrvHistorial!=null) {

            mAdapterHistorial = new AdapterHistorial(mContext, tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
            mrvHistorial.setAdapter(mAdapterHistorial);
            mAdapterHistorial.notifyDataSetChanged();

            mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
        } else {
            Toast.makeText(mContext, "NULO", Toast.LENGTH_SHORT).show();
        }

    }

    public static void UpdateAdapterFavoritos(Context mContext){

        if(mAdapterFavoritos!=null && mrvFavoritos!=null) {

            mAdapterFavoritos = new AdapterFavoritos(mContext, tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
            mrvFavoritos.setAdapter(mAdapterFavoritos);
            mAdapterFavoritos.notifyDataSetChanged();

         //   mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));

        } else {
            Toast.makeText(mContext, "NULO", Toast.LENGTH_SHORT).show();
        }

    }

    public static void EncenderAutoApagado(Context mContext, int minutos){


        // cambiar icono sleep a encendido
        if(ivSleep!=null){
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

                if(ivSleep!=null){
                    ivSleep.startAnimation(Animacion.exit_ios_anim(mContext));
                    ivSleep.setVisibility(VISIBLE);
                    ivSleep.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_moon_off));
                    ivSleep.startAnimation(Animacion.enter_ios_anim(mContext));
                }
                if(tvSleep!=null){
                    tvSleep.setVisibility(GONE);
                }

                andExoPlayerView.PlayOrPause(MediaNotificationManager.STATE_STOP);

            }

        }.start();


        countDownTimer.start();
        ivSleep.setImageResource(R.drawable.ic_moon_on);

        tvSleep.startAnimation(Animacion.exit_ios_anim(mContext));
        tvSleep.setVisibility(VISIBLE);
        tvSleep.startAnimation(Animacion.enter_ios_anim(mContext));



    }

    public static void ApagarAutoApagado(Context mContext){


        if (countDownTimer != null){
            countDownTimer.cancel();
            tvSleep.setText("00:00:00");
            // cambiar icono sleep a apagado
            if(ivSleep!=null){
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

        tinyDB.putString(TBmodoReproductor,modoReproductor);

        if(tinyDB.getString(TBmodoReproductor).equals(REPRODUCTOR_BUCLE)){
            if(ivOpcionBucle!=null) {
                ivOpcionBucle.startAnimation(Animacion.exit_ios_anim(mContext));
                ivOpcionBucle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bucle));
                ivOpcionBucle.startAnimation(Animacion.enter_ios_anim(mContext));
            }
        } else if(tinyDB.getString(TBmodoReproductor).equals(REPRODUCTOR_ALEATORIO)) {
            if(ivOpcionBucle!=null) {
                ivOpcionBucle.startAnimation(Animacion.exit_ios_anim(mContext));
                ivOpcionBucle.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_aleatorio));
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

}









