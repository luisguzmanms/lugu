package com.lamesa.lugu.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
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
import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.otros.TinyDB;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import java.util.List;
import java.util.Random;

import static com.lamesa.lugu.otros.metodos.DialogoPoliticas2;
import static com.lamesa.lugu.otros.metodos.ListaCSV;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.statics.constantes.TBimagenFondo;
import static com.lamesa.lugu.otros.statics.constantes.TBpase;
import static com.lamesa.lugu.otros.statics.constantes.TBpoliticas;


public class splash extends AppCompatActivity {

    public static TinyDB tinydb;

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

        ivFondoSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgregarCancion(splash.this,"lugu.csv");

            }
        });

        AppVersion();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (tinydb.getBoolean(TBpoliticas)) {
                    startActivity(new Intent(splash.this, act_main.class));
                    finish();
                } else {
                    DialogoPoliticas2(splash.this);
                }

            }
        }, 2000);


    }

    public static void DialogoPoliticas(Context mContext) {

        //region Dialogo terminos y condiciones
        DialogSettings.theme = DialogSettings.THEME.DARK;
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;

        Toast.makeText(mContext, R.string.title_terms, Toast.LENGTH_LONG).show();
        TipDialog.show((AppCompatActivity) mContext, R.string.title_terms, TipDialog.TYPE.WARNING);
        PrivacyPolicyDialog dialog = new PrivacyPolicyDialog((AppCompatActivity) mContext,
                "https://lugumusic.page.link/terms",
                "https://lugumusic.page.link/privacy");
        dialog.setTitle(mContext.getResources().getString(R.string.title_terms));
        dialog.setTitleTextColor(mContext.getResources().getColor(R.color.learn_gradient_grey_1));
        dialog.addPoliceLine("Terms and Conditions");
        dialog.addPoliceLine("The words of which the initial letter is capitalized have meanings defined under the following conditions. The following definitions shall have the same meaning regardless of whether they appear in singular or in plural.");
        dialog.addPoliceLine("For the purposes of these Terms and Conditions:\n" +
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
                "You means the individual accessing or using the Service, or the company, or other legal entity on behalf of which such individual is accessing or using the Service, as applicable.");
        dialog.addPoliceLine("Acknowledgment \n " + "These are the Terms and Conditions governing the use of this Service and the agreement that operates between You and the Company. These Terms and Conditions set out the rights and obligations of all users regarding the use of the Service.\n" +
                "\n" +
                "Your access to and use of the Service is conditioned on Your acceptance of and compliance with these Terms and Conditions. These Terms and Conditions apply to all visitors, users and others who access or use the Service.\n" +
                "\n" +
                "By accessing or using the Service You agree to be bound by these Terms and Conditions. If You disagree with any part of these Terms and Conditions then You may not access the Service.\n" +
                "\n" +
                "You represent that you are over the age of 18. The Company does not permit those under 18 to use the Service.\n" +
                "\n" +
                "Your access to and use of the Service is also conditioned on Your acceptance of and compliance with the Privacy Policy of the Company. Our Privacy Policy describes Our policies and procedures on the collection, use and disclosure of Your personal information when You use the Application or the Website and tells You about Your privacy rights and how the law protects You. Please read Our Privacy Policy carefully before using Our Service.");


        dialog.addPoliceLine("Links to Other Websites \n " + "Our Service may contain links to third-party web sites or services that are not owned or controlled by the Company.\n" +
                "\n" +
                "The Company has no control over, and assumes no responsibility for, the content, privacy policies, or practices of any third party web sites or services. You further acknowledge and agree that the Company shall not be responsible or liable, directly or indirectly, for any damage or loss caused or alleged to be caused by or in connection with the use of or reliance on any such content, goods or services available on or through any such web sites or services.\n" +
                "\n" +
                "We strongly advise You to read the terms and conditions and privacy policies of any third-party web sites or services that You visit.");

        dialog.addPoliceLine("\"AS IS\" and \"AS AVAILABLE\" Disclaimer \n" + "The Service is provided to You \"AS IS\" and \"AS AVAILABLE\" and with all faults and defects without warranty of any kind. To the maximum extent permitted under applicable law, the Company, on its own behalf and on behalf of its Affiliates and its and their respective licensors and service providers, expressly disclaims all warranties, whether express, implied, statutory or otherwise, with respect to the Service, including all implied warranties of merchantability, fitness for a particular purpose, title and non-infringement, and warranties that may arise out of course of dealing, course of performance, usage or trade practice. Without limitation to the foregoing, the Company provides no warranty or undertaking, and makes no representation of any kind that the Service will meet Your requirements, achieve any intended results, be compatible or work with any other software, applications, systems or services, operate without interruption, meet any performance or reliability standards or be error free or that any errors or defects can or will be corrected.\n" +
                "\n" +
                "Without limiting the foregoing, neither the Company nor any of the company's provider makes any representation or warranty of any kind, express or implied: (i) as to the operation or availability of the Service, or the information, content, and materials or products included thereon; (ii) that the Service will be uninterrupted or error-free; (iii) as to the accuracy, reliability, or currency of any information or content provided through the Service; or (iv) that the Service, its servers, the content, or e-mails sent from or on behalf of the Company are free of viruses, scripts, trojan horses, worms, malware, timebombs or other harmful components.\n" +
                "\n" +
                "Some jurisdictions do not allow the exclusion of certain types of warranties or limitations on applicable statutory rights of a consumer, so some or all of the above exclusions and limitations may not apply to You. But in such a case the exclusions and limitations set forth in this section shall be applied to the greatest extent enforceable under applicable law.");

        dialog.addPoliceLine("Limitation of Liability \n" + "Notwithstanding any damages that You might incur, the entire liability of the Company and any of its suppliers under any provision of this Terms and Your exclusive remedy for all of the foregoing shall be limited to the amount actually paid by You through the Service or 100 USD if You haven't purchased anything through the Service.\n" +
                "\n" +
                "To the maximum extent permitted by applicable law, in no event shall the Company or its suppliers be liable for any special, incidental, indirect, or consequential damages whatsoever (including, but not limited to, damages for loss of profits, loss of data or other information, for business interruption, for personal injury, loss of privacy arising out of or in any way related to the use of or inability to use the Service, third-party software and/or third-party hardware used with the Service, or otherwise in connection with any provision of this Terms), even if the Company or any supplier has been advised of the possibility of such damages and even if the remedy fails of its essential purpose.\n" +
                "\n" +
                "Some states do not allow the exclusion of implied warranties or limitation of liability for incidental or consequential damages, which means that some of the above limitations may not apply. In these states, each party's liability will be limited to the greatest extent permitted by law.");
        dialog.addPoliceLine("Governing Law \n The laws of the Country, excluding its conflicts of law rules, shall govern this Terms and Your use of the Service. Your use of the Application may also be subject to other local, state, national, or international laws.");

        dialog.addPoliceLine("Disputes Resolution \n" + "If You have any concern or dispute about the Service, You agree to first try to resolve the dispute informally by contacting the Company.");


        dialog.addPoliceLine("For European Union (EU) \nUsers If You are a European Union consumer, you will benefit from any mandatory provisions of the law of the country in which you are resident in.");

        dialog.addPoliceLine("United States Legal Compliance \n" + "You represent and warrant that (i) You are not located in a country that is subject to the United States government embargo, or that has been designated by the United States government as a \"terrorist supporting\" country, and (ii) You are not listed on any United States government list of prohibited or restricted parties.");

        dialog.addPoliceLine("Severability and Waiver \nSeverability\n" +
                "If any provision of these Terms is held to be unenforceable or invalid, such provision will be changed and interpreted to accomplish the objectives of such provision to the greatest extent possible under applicable law and the remaining provisions will continue in full force and effect.\n" +
                "\n" +
                "Waiver\n" +
                "Except as provided herein, the failure to exercise a right or to require performance of an obligation under this Terms shall not effect a party's ability to exercise such right or require such performance at any time thereafter nor shall be the waiver of a breach constitute a waiver of any subsequent breach.");

        dialog.addPoliceLine("Translation Interpretation \n" + "These Terms and Conditions may have been translated if We have made them available to You on our Service. You agree that the original English text shall prevail in the case of a dispute.");

        dialog.addPoliceLine("Changes to These Terms and Conditions \nWe reserve the right, at Our sole discretion, to modify or replace these Terms at any time. If a revision is material We will make reasonable efforts to provide at least 30 days' notice prior to any new terms taking effect. What constitutes a material change will be determined at Our sole discretion.\n" +
                "\n" +
                "By continuing to access or use Our Service after those revisions become effective, You agree to be bound by the revised terms. If You do not agree to the new terms, in whole or in part, please stop using the website and the Service.");
        dialog.addPoliceLine("Contact Us \n" + "If you have any questions about these Terms and Conditions, You can contact us:\n" +
                "\n" +
                "By email: lugulofimusic@gmail.com");


        dialog.setBackgroundColor(mContext.getResources().getColor(R.color.fondo_blancoazul));
        dialog.setLinkTextColor(mContext.getResources().getColor(R.color.learn_colorPrimary));

        dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
            @Override
            public void onAccept(boolean isFirstTime) {
                Log.e("MainActivity", "Policies accepted");

                //endregion
                MessageDialog.show((AppCompatActivity) mContext, mContext.getString(R.string.accept_terms), "").setOkButton("OK").setCancelButton("NO")
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


            }

            @Override
            public void onCancel() {
                Log.e("MainActivity", "Policies not accepted");
                tinydb.putBoolean(TBpoliticas, false);
                ((Activity) mContext).finish();
            }
        });


        dialog.show();
        //endregion
    }

    public static void SubirCancion(Context mContext, ModelCancion modelCancion) {

        //  WaitDialog.show((AppCompatActivity) mContext, "Enviando canción...").setCancelable(true);
        Toast.makeText(mContext, "Enviando canción...", Toast.LENGTH_SHORT).show();

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child("cancion").child(modelCancion.getId());
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
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar film", TipDialog.TYPE.ERROR);
            }
        });

    }


    private void DialogoPase(Context mContext, TinyDB tiniDB) {


        String pase = tinydb.getString(TBpase);
        String contrasena = "pplus";

        if (!pase.toLowerCase().contains(contrasena.toLowerCase())) {

            // new TraerListas(this).execute();

            // por defecto se añadira un token de usuario al tema todos donde se enviara notificaciones a todos
            FirebaseMessaging.getInstance().subscribeToTopic("todos").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Toast.makeText(getApplicationContext(),"Notificaciones activadas a todos",Toast.LENGTH_LONG).show();
                }
            });


            String titulo = "DIGITE LA CONTRASEÑA";
            String mensaje = "La contraseña es " + contrasena + "\n\n\n" + "Esta app aun sigue en desarrollo y se actualiza el contenido segun las sugerencias de los usuarios dando prioridad a las peliculas o series solicitadas, por favor participe enviando sugerencias y/o reportes de problemas para mejorar esta app." + "\n\n" + "apoyenos compartiendo esta app con sus amigos y familiares :,)";


            DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
            DialogSettings.theme = DialogSettings.THEME.DARK;


            InputDialog.build((AppCompatActivity) mContext)
                    //   .setButtonTextInfo(new TextInfo().setFontColor(Color.RED))
                    .setTitle(titulo).setMessage(mensaje)
                    .setOkButton("ENTRAR", new OnInputDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                            if (inputStr.equals("") || !inputStr.toLowerCase().contains(contrasena.toLowerCase())) {
                                TipDialog.show((AppCompatActivity) mContext, "Error de contraseña", TipDialog.TYPE.ERROR);

                                return false;
                            } else {

                                tinydb.putString(TBpase, contrasena);
                                startActivity(new Intent(splash.this, act_main.class));
                                finish();

                                return true;
                            }
                        }
                    })
                    //  .setCancelButton("CANCELAR")
                    .setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN))
                    .setInputInfo(new InputInfo()
                            .setInputType(InputType.TYPE_CLASS_TEXT)
                            .setTextInfo(new TextInfo()
                                    .setFontColor(mContext.getResources().getColor(R.color.learn_colorPrimary))
                            )
                    )
                    .setCancelable(false)
                    .show();

        } else {
            startActivity(new Intent(splash.this, act_main.class));
            finish();
        }
    }

    private void EliminarDato(Context mContext) {

        WaitDialog.show((AppCompatActivity) mContext, "Eliminando dato...").setCancelable(true);


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  dataSnapshot.getRef().child("data").removeValue();


                WaitDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al eliminar dato", TipDialog.TYPE.ERROR);

            }
        });
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

    private void SubirVideoFilmPel(Context mContext) {

        // List<modelAddVideoFilm> mlistAddFilm = new ArrayList<>();


        //region region


        //modelAddVideoFilm  modelFilm729040 = new modelAddVideoFilm("729040","latino","T1-EP1","T1-EP1","");


        //endregion



        /*
        for (int i = 0; mlistAddFilm.size() > i; i++) {
            SubirVideo(mContext, mlistAddFilm.get(i).getId(), mlistAddFilm.get(i).getIdioma(), mlistAddFilm.get(i).getIdEpisodio(), mlistAddFilm.get(i).getNombreEpisodio(), mlistAddFilm.get(i).getLinkVideo());
       }

         */


    }

    private void SubirVideo(Context mContext, String id, String idioma, String idEpisodio, String nombreEpisodio, String linkVideo) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                WaitDialog.show((AppCompatActivity) mContext, "Enviando video...").setCancelable(true);

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(id).child("episodios").child(idioma).child(idEpisodio);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Random random = new Random();
                        int keyLink = random.nextInt(9999);


                        if (!dataSnapshot.exists()) {
                            dataSnapshot.getRef().child("id").setValue(id);
                            dataSnapshot.getRef().child("idEpisodio").setValue(idEpisodio);
                            dataSnapshot.getRef().child("nombre").setValue(nombreEpisodio);
                            dataSnapshot.getRef().child("links").child("op1").setValue(linkVideo);


                            WaitDialog.dismiss();
                            //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                            System.out.println("FILM video SUBIDO == " + id + " - " + nombreEpisodio);
                            //   Toast.makeText(mContext, "FILM video SUBIDO == \"+id+\" - \"+nombreEpisodio", Toast.LENGTH_LONG).show();

                        } else {
                            System.out.println("FILM YA CARGADO == " + id + " - " + nombreEpisodio);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        WaitDialog.dismiss();
                        TipDialog.show((AppCompatActivity) mContext, "Error al enviar video", TipDialog.TYPE.ERROR);

                    }
                });

            }
        }, 20000);
    }

    //endregion

    private void SubirFilmSerie(Context mContext, String idFilm, String ano, String calidad, String categoria, String descrip, String imagen, String nombre, String tipo, String puntaje, String red, String fechaActualizado) {

        WaitDialog.show((AppCompatActivity) mContext, "Enviando Serie...").setCancelable(true);


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("data").child(String.valueOf(idFilm));
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.getRef().child("info").child("ano").setValue(ano);
                dataSnapshot.getRef().child("info").child("calidad").setValue(calidad);
                dataSnapshot.getRef().child("info").child("categoria").setValue(categoria);
                dataSnapshot.getRef().child("info").child("descrip").setValue(descrip);
                dataSnapshot.getRef().child("info").child("id").setValue(idFilm);
                dataSnapshot.getRef().child("info").child("imagen").setValue(imagen);
                dataSnapshot.getRef().child("info").child("nombre").setValue(nombre);
                dataSnapshot.getRef().child("info").child("tipo").setValue(tipo);
                dataSnapshot.getRef().child("info").child("puntaje").setValue(puntaje);
                dataSnapshot.getRef().child("info").child("red").setValue(red);
                dataSnapshot.getRef().child("info").child("fechaActualizado").setValue(fechaActualizado);


                WaitDialog.dismiss();
                //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                System.out.println("FILM SUBIDO == " + idFilm + " - " + nombre);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar film", TipDialog.TYPE.ERROR);

            }
        });
    }

    private void SubirCategoria(Context mContext) {


    }

    private void EnviarCategoria(Context mContext, String imagen, String nombre) {
        WaitDialog.show((AppCompatActivity) mContext, "Enviando categoria...").setCancelable(true);


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("otros").child("categorias");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Random random = new Random();
                int keyLink = random.nextInt(9999);


                dataSnapshot.getRef().child(String.valueOf(keyLink)).child("imagen").setValue(imagen);
                dataSnapshot.getRef().child(String.valueOf(keyLink)).child("nombre").setValue(nombre);


                WaitDialog.dismiss();
                //  TipDialog.show((AppCompatActivity) mContext, "Sugerencia enviada.", TipDialog.TYPE.SUCCESS);

                System.out.println("CATEGORIA video SUBIDO == " + nombre);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                WaitDialog.dismiss();
                TipDialog.show((AppCompatActivity) mContext, "Error al enviar video", TipDialog.TYPE.ERROR);

            }
        });

    }

    private void AgregarCancion(Context mContext, String nombreFileCSV) {


        List<String[]> listCSV = ListaCSV(mContext, nombreFileCSV);


        //    List<modelAddFilm> mlistAddFilm = new ArrayList<>();


        if (listCSV != null && !listCSV.isEmpty())
            for (int i = 0; i < listCSV.size(); i++) {

                String linkYT = listCSV.get(i)[0];
                String id = listCSV.get(i)[1];
                String cancion = listCSV.get(i)[2];
                String artista = listCSV.get(i)[3];
                String categoria = listCSV.get(i)[4];

                ModelCancion modelCancion = new ModelCancion(id, artista, cancion, categoria, linkYT);
           //     SubirCancion(mContext, modelCancion);

            }


    }

    // subir item de lista a FB

    private void AgregarVideo(Context mContext, String nombreFileCSV) {


        List<String[]> listCSV = ListaCSV(mContext, nombreFileCSV);


        //   List<modelAddVideoFilm> mlistAddVideo = new ArrayList<>();


        if (listCSV != null && !listCSV.isEmpty())
            for (int i = 0; i < listCSV.size(); i++) {


                String id = listCSV.get(i)[0];
                String idioma = listCSV.get(i)[2];
                String idEpisodio = "EP1";
                String nombreEpisodio = "VER AHORA";
                String linkVideo = listCSV.get(i)[3];


                //      modelAddVideoFilm film = new modelAddVideoFilm(id, idioma, idEpisodio, nombreEpisodio, linkVideo);

                //    mlistAddVideo.add(film);


            }

        // System.out.println("jajajajajaja"+mlistAddVideo.get(12).getSipnosis());


        /*

        for (int i = 0; i < mlistAddVideo.size(); i++) {
         //   System.out.println("LISTAAA " + mlistAddFilm.get(i).getId() + "-" + mlistAddFilm.get(i).getNombre() + "-" + mlistAddFilm.get(i).getCalidad() + "-" + mlistAddFilm.get(i).getFechaActualizado());
            SubirVideo(mContext,mlistAddVideo.get(i).getId(),mlistAddVideo.get(i).getIdioma(),mlistAddVideo.get(i).getIdEpisodio(),mlistAddVideo.get(i).getNombreEpisodio(),mlistAddVideo.get(i).getLinkVideo());
        }


         */


    }

    private void AgregarVideoSerie(Context mContext, String nombreFileCSV) {


        List<String[]> listCSV = ListaCSV(mContext, nombreFileCSV);


        //    List<modelAddVideoFilm> mlistAddVideo = new ArrayList<>();


        if (listCSV != null && !listCSV.isEmpty())
            for (int i = 0; i < listCSV.size(); i++) {


                String id = listCSV.get(i)[0];
                String idioma = listCSV.get(i)[7];
                String idEpisodio = listCSV.get(i)[4];
                String nombreEpisodio = listCSV.get(i)[4] + " " + listCSV.get(i)[5];
                String linkVideo = listCSV.get(i)[8];


                //    modelAddVideoFilm film = new modelAddVideoFilm(id, idioma, idEpisodio, nombreEpisodio, linkVideo);

                //     mlistAddVideo.add(film);


            }

        // System.out.println("jajajajajaja"+mlistAddVideo.get(12).getSipnosis());



        /*
        for (int i = 0; i < mlistAddVideo.size(); i++) {
            //   System.out.println("LISTAAA " + mlistAddFilm.get(i).getId() + "-" + mlistAddFilm.get(i).getNombre() + "-" + mlistAddFilm.get(i).getCalidad() + "-" + mlistAddFilm.get(i).getFechaActualizado());
            SubirVideo(mContext,mlistAddVideo.get(i).getId(),mlistAddVideo.get(i).getIdioma(),mlistAddVideo.get(i).getIdEpisodio(),mlistAddVideo.get(i).getNombreEpisodio(),mlistAddVideo.get(i).getLinkVideo());
        }


         */


    }


}
