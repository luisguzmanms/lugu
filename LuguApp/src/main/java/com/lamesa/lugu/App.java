package com.lamesa.lugu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.kongzue.dialog.util.DialogSettings;
import com.lamesa.lugu.otros.TinyDB;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.Calendar;
import java.util.Random;

import static com.lamesa.lugu.otros.statics.constantes.TBdiaIngreso;


/**
 * Created by Taurus on 2018/4/15.
 */

public class App extends MultiDexApplication {


    public static boolean ignoreMobile;
    public static MixpanelAPI mixpanel;
    public static String idFilm;
    public static String tbIdioma;
    public static String tbNombreFilm;
    public static String tbCategoriaFilm;
    public static String tbTipoFilm;
    public static String tbImagenFilm;
    public static String tbCalidadFilm;
    public static FirebaseAnalytics mFirebaseAnalytics;
    private static App instance;
    private TinyDB tinyDB;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this,
                "ca-app-pub-1553194436365145~2484820665");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        Amplitude.getInstance().initialize(this, "8461f9db2ddba9f48c66a867df909433").enableForegroundTracking(this);
        Amplitude.getInstance().enableLocationListening();


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
// Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


// Initialize the library with your
// Mixpanel project token, MIXPANEL_TOKEN, and a reference
// to your application context.
        mixpanel = MixpanelAPI.getInstance(this, "e96360802e958c55420b964f66ff0809");


        CondiguracionDialogos();
        tinyDB = new TinyDB(this);

        // Toast.makeText(this, "JAJAJAJ", Toast.LENGTH_SHORT).show();
        Random random = new Random();
        int numRandom = random.nextInt(5);

        if (numRandom == 5) {
            BorrarCache(this, tinyDB);
        }
    }

    private void BorrarCache(Context mContext, TinyDB tinyDB) {
        Calendar c = Calendar.getInstance();
        int DiaActual = c.get(Calendar.DAY_OF_WEEK);

        int tbDiaIngreso = tinyDB.getInt(TBdiaIngreso);

        if (DiaActual != tbDiaIngreso) {
            // This method must be called on a background thread.
            class TestAsync extends AsyncTask<Void, Integer, Void> {
                final String TAG = getClass().getSimpleName();

                protected void onPreExecute() {
                    super.onPreExecute();
                    Log.d(TAG + " PreExceute", "On pre Exceute......");
                }

                protected Void doInBackground(Void... arg0) {
                    Log.d(TAG + " DoINBackGround", "On doInBackground...");

                    Glide.get(App.this).clearDiskCache();

                    return null;
                }

                protected void onProgressUpdate(Integer... a) {
                    super.onProgressUpdate(a);
                    Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
                }

                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    Log.d(TAG + " onPostExecute", "" + result);
                }
            }

            new TestAsync().execute();

            tinyDB.putInt(TBdiaIngreso, DiaActual);

            System.out.println("Cache limpiada!!!!");

        }
    }

    public void CondiguracionDialogos() {
        DialogSettings.init();
        DialogSettings.DEBUGMODE = false;
        DialogSettings.isUseBlur = false;
        DialogSettings.autoShowInputKeyboard = true;
        //DialogSettings.backgroundColor = Color.BLUE;
        //DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
        //DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
        DialogSettings.theme = DialogSettings.THEME.DARK;


    }


}




