package com.lamesa.lugu.activity;


import static com.lamesa.lugu.activity.act_main.getListas;
import static com.lamesa.lugu.otros.metodos.DialogoPoliticas2;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.statics.constantes.TBimagenFondo;
import static com.lamesa.lugu.otros.statics.constantes.TBpoliticas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.otros.TinyDB;


public class splash extends AppCompatActivity {

    public TinyDB tinydb;

    public static void SubirCancion(Context mContext, ModelCancion modelCancion) {

        //  WaitDialog.show((AppCompatActivity) mContext, "Enviando canción...").setCancelable(true);
        Toast.makeText(mContext, "Enviando canción...", Toast.LENGTH_SHORT).show();

        if (!modelCancion.getId().contains("[-\\\\[\\\\]^/,'*:.!><~@#$%+=?|\\\"\\\\\\\\()]")) {
            if (!modelCancion.getId().contains(".")) {
                if (!modelCancion.getId().contains("#")) {
                    if (!modelCancion.getId().contains("[")) {
                        if (!modelCancion.getId().contains("]")) {

                            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child("cancion").child(modelCancion.getId().trim());
                            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    dataSnapshot.getRef().child("linkYT").setValue(modelCancion.getLinkYT().trim());
                                    dataSnapshot.getRef().child("id").setValue(modelCancion.getId().trim());
                                    dataSnapshot.getRef().child("cancion").setValue(modelCancion.getCancion().trim());
                                    dataSnapshot.getRef().child("artista").setValue(modelCancion.getArtista().trim());
                                    dataSnapshot.getRef().child("categoria").setValue(modelCancion.getCategoria().trim());


                                    setLogInfo(mContext, "SubirCancion", "Cancion subida :: " + modelCancion.getId() + " :: " + modelCancion.getCancion(), false);

                                    WaitDialog.dismiss();
                                    //TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);
                                    Toast.makeText(mContext, "Cancion subida", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    WaitDialog.dismiss();
                                    TipDialog.show((AppCompatActivity) mContext, "Error al enviar cancion", TipDialog.TYPE.ERROR);
                                }
                            });
                        } else {
                            setLogInfo(mContext, "SubirCancion", "NO SE SUBIO :: " + modelCancion.getId() + " :: " + modelCancion.getCancion(), false);
                        }
                    }
                }
            }
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tinydb = new TinyDB(this);

        ImageView ivFondoSplash = findViewById(R.id.iv_fondoSplash);
        Glide.with(this)
                .load(tinydb.getString(TBimagenFondo))
                //   .error(R.drawable.ic_alert)
                //.placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(ivFondoSplash);
        AppVersion();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (tinydb.getBoolean(TBpoliticas)) {
                    startActivity(new Intent(splash.this, act_main.class));
                    finish();
                } else {
                    Toast.makeText(splash.this, "Loading content...", Toast.LENGTH_LONG).show();
                    getListas(splash.this, tinydb);
                    DialogoPoliticas2(splash.this, tinydb);
                }

            }
        }, 2000);
    }

    private void AppVersion() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;

            TextView tvAppVersion = findViewById(R.id.tv_appversion);
            tvAppVersion.setText("Version: " + versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
    //endregion
}
