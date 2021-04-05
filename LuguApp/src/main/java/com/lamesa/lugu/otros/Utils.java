package com.lamesa.lugu.otros;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.AUDIO_SERVICE;
import static com.lamesa.lugu.otros.constantes.TINYi_DIA_GUARDADO;

public class Utils {


    public static void setLogCat(String TAG, String msg, String tipo, boolean activo) {
        if (activo) {
            switch (tipo) {
                case "e":
                    Log.e(TAG, msg);
                    break;

                case "w":
                    Log.w(TAG, msg);
                    break;

                case "d":
                    Log.d(TAG, msg);
                    break;

                case "i":
                    Log.i(TAG, msg);
                    break;

                case "v":
                    Log.v(TAG, msg);
                    break;


            }
        }

    }

    public static void setError(Context mContext, String TAG, String msg, boolean error) {
        String errormsg = "";
        if (error) {
            errormsg = "error : | ";
        }
        // para la fecha
        DateFormat df = new SimpleDateFormat("EEEE, d MMM yyyy hh:mm a ");
        String time = df.format(Calendar.getInstance().getTime());
        DateFormat df2 = new SimpleDateFormat("HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");
        //  String dato = errormsg = time + " | " + sp_get_childactivo(mContext) + " | " + TAG + " | msg :: " + msg;
        // fuego.setData.StringFB(RUTA_PADRE + "admin" + "/logcat/info/" + nombreRandom, dato, mContext);
        Log.d("setLogInfo", errormsg + TAG + " | msg :: " + msg);
    }

    public static void setLogInfo(Context mContext, String TAG, String msg, boolean error) {

        String errormsg = "";
        if (error) {
            errormsg = "error : | ";
        }
        // para la fecha
        DateFormat df = new SimpleDateFormat("EEEE, d MMM yyyy hh:mm a ");
        String time = df.format(Calendar.getInstance().getTime());
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");
        //   String dato = errormsg = time + " | " + sp_get_childactivo(mContext) + " \n| " + TAG + " | \nmsg :: " + msg;
        //  fuego.setData.StringFB(RUTA_PADRE + "admin" + "/logcat/info/" + nombreRandom, dato, mContext);
        Log.d("setLogInfo", errormsg + TAG + " | msg :: " + msg);
    }

    public static void sonidoTelefono(Context mContext, boolean activo) {

        if (activo) {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    public static class Fecha {

        public static String getFechaActual(Context mContext) {
            // para la fecha
            DateFormat df = new SimpleDateFormat("EEEE, d MMM yyyy hh:mm a ");
            String time = df.format(Calendar.getInstance().getTime());
            return time;
        }


        public static String getDiaGuardado(Context mContext) {
            // obtener fecha guardada en tinydb
            TinyDB tinyDB = new TinyDB(mContext);
            String diaGuardado = tinyDB.getString(TINYi_DIA_GUARDADO);

            //si no se ha gyardado se guardara la fecha actual
            if (tinyDB.getString(TINYi_DIA_GUARDADO).equals("") || tinyDB.getString(TINYi_DIA_GUARDADO) == null || tinyDB.getString(TINYi_DIA_GUARDADO).isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                int diaActual = calendar.get(Calendar.DAY_OF_YEAR);
                tinyDB.putString(TINYi_DIA_GUARDADO, String.valueOf(diaActual));
                diaGuardado = tinyDB.getString(TINYi_DIA_GUARDADO);
            }


            return diaGuardado;
        }


        public static String getDiaActual(Context mContext) {
            Calendar calendar = Calendar.getInstance();
            String diaActual = String.valueOf(calendar.get(Calendar.DAY_OF_YEAR));

            return diaActual;
        }

    }
}


