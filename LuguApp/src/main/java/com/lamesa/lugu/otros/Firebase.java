package com.lamesa.lugu.otros;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amplitude.api.Amplitude;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.lugu.R;
import com.lamesa.lugu.adapter.AdapterCategoria;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.otros.metodos.EliminarDuplicadosModelCancion;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCanciones;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCategorias;
import static com.lamesa.lugu.otros.statics.constantes.TBlistImagenes;
import static com.lamesa.lugu.otros.statics.constantes.mixFalloEpisodio;
import static com.lamesa.lugu.otros.statics.constantes.mixReporteFilm;


public class Firebase extends AppCompatActivity {


    //region ENVIAR A Firebase


    public static void EnviarSolicitud(final Context mContext, String mensaje) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando solicitud...").setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("solicitud");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(nombreRandom).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(nombreRandom).child("mensaje").setValue(mensaje);

                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Solicitud enviada.", TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar la solicitud.", TipDialog.TYPE.ERROR);

            }
        });

    }


    public static void EnviarSugerencia(final Context mContext, String mensaje) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();
        WaitDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.enviando_sugerencia)).setCancelable(true);


        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otro").child("reporte").child("sugerencia");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(nombreRandom).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(nombreRandom).child("mensaje").setValue(mensaje);

                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.sugerencia_enviada), TipDialog.TYPE.SUCCESS);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.error_sugerencia), TipDialog.TYPE.ERROR);

            }
        });

    }

    public static void ReportarEpisodio(final Context mContext, String mensaje, String idFilm, String idEpisodio, String nombreEpisodio, String nombreFilm, String idioma) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando reporte...").setCancelable(true);


        /*
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

         */


        String nombreGuardar = idFilm + "-" + idEpisodio + "-" + idioma;

        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("falloEpisodio");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child(nombreGuardar).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(nombreGuardar).child("mensaje").setValue(mensaje);
                dataSnapshot.getRef().child(nombreGuardar).child("idFilm").setValue(idFilm);
                dataSnapshot.getRef().child(nombreGuardar).child("nombreEpisodio").setValue(nombreEpisodio);
                dataSnapshot.getRef().child(nombreGuardar).child("nombreFilm").setValue(nombreFilm);
                dataSnapshot.getRef().child(nombreGuardar).child("idEpisodio").setValue(idEpisodio);
                dataSnapshot.getRef().child(nombreGuardar).child("idioma").setValue(idioma);


                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Reporte enviado.", TipDialog.TYPE.SUCCESS);

                //region MIX FalloEpisodio para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("IdFilm", idFilm);
                    props.put("NombreFilm", nombreFilm);
                    props.put("Idioma", idioma);
                    props.put("IdEpisodio", idEpisodio);
                    props.put("NombreEpisodio", nombreEpisodio);
                    props.put("Mensaje", mensaje);
                    Bundle params = new Bundle();
                    params.putString("IdFilm", idFilm);
                    params.putString("NombreFilm", nombreFilm);
                    params.putString("Idioma", idioma);
                    params.putString("IdEpisodio", idEpisodio);
                    params.putString("NombreEpisodio", nombreEpisodio);
                    params.putString("Mensaje", mensaje);


                    mFirebaseAnalytics.logEvent(mixFalloEpisodio, params);
                    mixpanel.track(mixFalloEpisodio, props);
                    Amplitude.getInstance().logEvent(mixFalloEpisodio, props);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //endregion


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar reporte.", TipDialog.TYPE.ERROR);

            }
        });

    }

    public static void ReportarFilm(final Context mContext, String mensaje, String idFilm, String nombreFilm) {


        //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


        WaitDialog.show((AppCompatActivity) mContext, "Enviando reporte...").setCancelable(true);


        /*
        DateFormat df2 = new SimpleDateFormat("MM-yyyy--HH-mm-ss");
        String nombreRandom = df2.format(Calendar.getInstance().getTime()).replace(".", "");

         */


        // para la fecha
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a");
        String fecha = df.format(Calendar.getInstance().getTime());

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("reportes").child("reporteFilm").child(idFilm);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Random random = new Random();
                int nombreGuardar = random.nextInt(99999);

                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("fecha").setValue(fecha);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("mensaje").setValue(mensaje);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("idFilm").setValue(idFilm);
                dataSnapshot.getRef().child(String.valueOf(nombreGuardar)).child("nombreFilm").setValue(nombreFilm);


                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Reporte enviado.", TipDialog.TYPE.SUCCESS);

                //region MIX ReporteFilm para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("IdFilm", idFilm);
                    props.put("NombreFilm", nombreFilm);
                    props.put("Mensaje", mensaje);
                    props.put("Fecha", fecha);
                    Bundle params = new Bundle();
                    params.putString("IdFilm", idFilm);
                    params.putString("NombreFilm", nombreFilm);
                    params.putString("Mensaje", mensaje);
                    params.putString("Fecha", fecha);


                    mFirebaseAnalytics.logEvent(mixReporteFilm, params);
                    mixpanel.track(mixReporteFilm, props);
                    Amplitude.getInstance().logEvent(mixReporteFilm, props);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //endregion

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar reporte.", TipDialog.TYPE.ERROR);

            }
        });

    }


    //endregion

    public static void getListaCategorias(Context mContext, List<ModelCategoria> mlistCategoria, AdapterCategoria mAdapterCategoria, TinyDB tinyDB) {


        if (mlistCategoria == null) {
            mlistCategoria = new ArrayList<>();
        }
        List<ModelCategoria> finalMlistCategoria = mlistCategoria;


        Query database = FirebaseDatabase.getInstance().getReference().child("data").child("categoria").orderByChild("nombre");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finalMlistCategoria.removeAll(finalMlistCategoria);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {

                    ModelCategoria categoria = snapshot.getValue(ModelCategoria.class);
                    finalMlistCategoria.add(categoria);

                }
                mAdapterCategoria.notifyDataSetChanged();
                tinyDB.putListModelCategoria(TBlistCategorias, finalMlistCategoria);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setLogInfo(mContext, "getListaCategorias", "DatabaseError: " + databaseError, false);

            }


        });


    }

    public static void getListaCanciones(Context mContext, List<ModelCategoria> mlistCategoria, List<ModelCancion> mlistCancion, TinyDB tinyDB) {


        if (mlistCancion == null) {
            mlistCancion = new ArrayList<>();
        }

        if (mlistCategoria == null) {
            mlistCategoria = new ArrayList<>();
        }


        List<ModelCancion> finalMlistCancion = mlistCancion;
        List<ModelCategoria> finalMlistCategoria = mlistCategoria;


        Query database = FirebaseDatabase.getInstance().getReference().child("data").child("cancion").orderByChild("nombre");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finalMlistCancion.removeAll(finalMlistCancion);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {

                    ModelCancion cancion = snapshot.getValue(ModelCancion.class);
                    finalMlistCancion.add(cancion);


                }

                // guardar una lista con todas las canciones
                tinyDB.putListModelCancion(TBlistCanciones, finalMlistCancion);

                // se usa para crear una lista por categoria
                Firebase.getListaPorCategoria(mContext, finalMlistCategoria, finalMlistCancion, tinyDB);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    // agregar canciones a una lista con su categoria
    public static void getListaPorCategoria(Context mContext, List<ModelCategoria> mlistCategorias, List<ModelCancion> mlistCanciones, TinyDB tinyDB) {


        if (mlistCanciones == null) {
            mlistCanciones = new ArrayList<>();
        }

        if (mlistCategorias == null) {
            mlistCategorias = new ArrayList<>();
        }

        if (!mlistCategorias.isEmpty() && !mlistCanciones.isEmpty()) {

            // agreagr canciones a una lista con su categoria
            for (ModelCancion cancion : mlistCanciones) {

                // si la cancion contiene alguna categoria guardarla en la respectiva lista de categoria
                for (ModelCategoria categoria : mlistCategorias) {
                    if (cancion != null) {
                        if (cancion.getCategoria().toLowerCase().trim().contains(categoria.getId().toLowerCase().trim())) {
                            List<ModelCancion> tinyListCancionxCategoria = tinyDB.getListModelCancion(categoria.getNombre().toLowerCase().trim(), ModelCancion.class);
                            tinyListCancionxCategoria.add(cancion);
                            tinyDB.putListModelCancion(categoria.getNombre().toLowerCase().trim(), EliminarDuplicadosModelCancion(tinyListCancionxCategoria));
                        }
                    }
                }
            }
        }


    }

    // traer imagenes gif
    public static void getListaImagenes(TinyDB tinyDB) {


        ArrayList<String> mlistImagenes = tinyDB.getListString(TBlistImagenes);

        Query database = FirebaseDatabase.getInstance().getReference().child("data").child("imagen");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistImagenes.removeAll(mlistImagenes);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {

                    if (snapshot.exists()) {

                        String linkImagen = snapshot.getValue().toString();
                        mlistImagenes.add(linkImagen);
                        System.out.println("linkImagen: " + linkImagen);

                    }
                }

                tinyDB.putListString(TBlistImagenes, mlistImagenes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

}
