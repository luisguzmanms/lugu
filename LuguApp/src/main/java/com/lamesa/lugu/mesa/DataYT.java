package com.lamesa.lugu.mesa;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;
import com.lamesa.lugu.otros.TinyDB;
import com.naveed.ytextractor.ExtractorException;
import com.naveed.ytextractor.YoutubeStreamExtractor;
import com.naveed.ytextractor.model.YTMedia;
import com.naveed.ytextractor.model.YTSubtitles;
import com.naveed.ytextractor.model.YoutubeMeta;

import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static android.view.View.VISIBLE;
import static com.lamesa.lugu.activity.splash.SubirCancion;
import static com.lamesa.lugu.otros.metodos.isNetworkAvailable;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCategorias;


public class DataYT extends AppCompatActivity {


    private static ImageView ivExiste;
    private String idCancion;
    private String nombreCancion;
    private String nombreArtista;
    private String linkMp3;
    private String nombreCanal;
    private String nombreLink;
    private String youtubeLink;
    private EditText etLinkYT;
    private EditText etArtistaCancion;
    private EditText etIdCancion;
    private EditText etNombreCancion;
    private String idCategoria = "";

    public static void ComprobarSiExisteCancion(String idCancion) {

        Query database = FirebaseDatabase.getInstance().getReference().child("data").child("cancion").orderByChild("id");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.child(idCancion).exists()) {
                    if (ivExiste != null) {
                        ivExiste.setVisibility(VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


    }

    /**
     * DataYT solo en versiones admin
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_vacio);

        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        String receivedType = receiverdIntent.getType();

        if (receivedAction.equals(Intent.ACTION_SEND)) {

            // check mime type

            if (savedInstanceState == null && Intent.ACTION_SEND.equals(getIntent().getAction())
                    && getIntent().getType() != null && "text/plain".equals(getIntent().getType())) {

                String ytLink = receiverdIntent
                        .getStringExtra(Intent.EXTRA_TEXT);


                if (ytLink != null
                        && (ytLink.contains("://youtu.be/") || ytLink.contains("youtube.com/watch?v="))) {
                    youtubeLink = ytLink;
                    // We have a valid link
                    //   getYoutubeDownloadUrl(youtubeLink);
                    Toast.makeText(this, youtubeLink, Toast.LENGTH_SHORT).show();

                    getDataYT(youtubeLink, 1);
                } else {
                    Toast.makeText(this, "error no link YT", Toast.LENGTH_LONG).show();
                    finish();
                }


            } else if (receivedAction.equals(Intent.ACTION_MAIN)) {

                Log.e("TAG", "onSharedIntent: nothing shared");
                Toast.makeText(this, "onSharedIntent: nothing shared", Toast.LENGTH_SHORT).show();

            }
        }


    }

    private void getDataYT(String linkYT, int opcion) {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, this.getResources().getString(R.string.coneccion_lenta), Toast.LENGTH_SHORT).show();
        } else {


            if (opcion == 1) {
                setLogInfo(this, "getLinkAndPlay.YouTubeExtractor", "Inicia extracion opcion 1", false);

                /**
                 * Opcion 1 de extracción reparada en la version 4
                 */

                new YouTubeExtractor(this) {

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


                                        nombreCancion = vMeta.getTitle();
                                        nombreArtista = vMeta.getTitle();

                                        idCancion = vMeta.getVideoId();
                                        nombreLink = vMeta.getTitle();

                                        if (vMeta.getTitle().contains(" - ") || vMeta.getTitle().contains("-")) {
                                            nombreCancion = vMeta.getTitle().replace(vMeta.getTitle().substring(0, vMeta.getTitle().indexOf(" - ")), "").replace(vMeta.getTitle().substring(0, vMeta.getTitle().indexOf("-")), "").replace("-", "").replace(" - ", "").replace("[lofi hip hop/relaxing beats]", "").trim();
                                            nombreArtista = vMeta.getTitle().substring(0, vMeta.getTitle().indexOf(" - ")).replace("-", "").replace(" - ", "").trim();
                                        }

                                        linkMp3 = ytFiles.get(itag).getUrl();
                                        nombreCanal = vMeta.getAuthor();

                                        DialogoData(linkYT);


                                        System.out.println("newlofi downloadUrl itag == " + " ytFiles " + ytFiles.get(itag).getFormat().getAudioBitrate() + linkMp3);


                                        break;
                                    }


                                } else {
                                    System.out.println("no hay itag de audio == ");
                                }
                            }

                        } else {
                            getDataYT(linkYT, 2);
                        }
                    }

                }.extract(linkYT, false, false);


            } else if (opcion == 2) {

                setLogInfo(this, "getLinkAndPlay.YoutubeStreamExtractor", "Inicia extracion opcion 2", false);
                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        //    setLogInfo(this, "getLinkAndPlay.YoutubeStreamExtractor", "onExtractionDone", false);
                        //url to get subtitle

                        for (YTMedia media : adativeStream) {

                            // solo extraer link de audio
                            if (media.getAudioQuality() != null && media.getAudioQuality().contains("AUDIO")) {

                                System.out.println("newlofi downloadUrl  media.getUrl() == " + media.getUrl());
                                System.out.println("newlofi downloadUrl  media.getAudioQuality() == " + media.getAudioQuality());
                                System.out.println("newlofi downloadUrl  media.getBitrate() == " + media.getBitrate());
                                System.out.println("newlofi downloadUrl  media.getItag() == " + media.getItag());
                                // guardar datos


                                nombreCancion = meta.getTitle();
                                nombreArtista = meta.getTitle();

                                idCancion = meta.getVideoId();
                                nombreLink = meta.getTitle();
                                if (meta.getTitle().contains(" - ") || meta.getTitle().contains("-")) {
                                    nombreCancion = meta.getTitle().replace(meta.getTitle().substring(0, meta.getTitle().indexOf(" - ")), "").replace(meta.getTitle().substring(0, meta.getTitle().indexOf("-")), "").replace("-", "").replace(" - ", "").replace("[lofi hip hop/relaxing beats]", "").trim();
                                    nombreArtista = meta.getTitle().substring(0, meta.getTitle().indexOf(" - ")).replace("-", "").replace(" - ", "").trim();
                                }
                                linkMp3 = media.getUrl();
                                nombreCanal = meta.getAuthor();


                                DialogoData(linkYT);


                                break;
                            }

                        }
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException error) {
                        Toast.makeText(DataYT.this, "onExtractionGoesWrong:" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).Extract(linkYT);
                //use .useDefaultLogin() to extract age restricted videos
            }

        }
    }

    private void DialogoData(String youtubeLink) {

        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
        DialogSettings.theme = DialogSettings.THEME.DARK;

        //对于未实例化的布局：
        MessageDialog.show(this, this.getString(R.string.app_name), nombreLink, "ENVIAR")
                .setCustomView(R.layout.layout_datayt, new MessageDialog.OnBindView() {

                    @Override
                    public void onBind(MessageDialog dialog, View view) {

                        Spinner spCategorias = view.findViewById(R.id.sp_categorias);
                        ivExiste = view.findViewById(R.id.iv_existe);
                        ComprobarSiExisteCancion(idCancion);

                        ArrayList listCategorias = new ArrayList();

                        TinyDB tinyDB = new TinyDB(DataYT.this);

                        List<ModelCategoria> listModelCategoria = tinyDB.getListModelCategoria(TBlistCategorias, ModelCategoria.class);
                        if (listModelCategoria != null) {
                            for (ModelCategoria categoria : listModelCategoria) {
                                listCategorias.add(categoria.getNombre());
                            }
                        }

                        ArrayAdapter<CharSequence> spinnerMenuList = new ArrayAdapter<CharSequence>(DataYT.this, android.R.layout.simple_spinner_item, listCategorias);
                        spinnerMenuList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spCategorias.setAdapter(spinnerMenuList);
                        spCategorias.setSelection(tinyDB.getInt("TBCategoriaSelection"));
                        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                tinyDB.putInt("TBCategoriaSelection", position);
                                idCategoria = listModelCategoria.get(position).getId();
                                Toast.makeText(DataYT.this, listCategorias.get(position).toString(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        etArtistaCancion = view.findViewById(R.id.et_artistaCancion);
                        etNombreCancion = view.findViewById(R.id.et_nombreCancion);
                        etIdCancion = view.findViewById(R.id.et_idCancion);
                        etLinkYT = view.findViewById(R.id.et_linkyt);

                        etArtistaCancion.setText(nombreArtista);
                        etNombreCancion.setText(nombreCancion);
                        etIdCancion.setText(idCancion);
                        etLinkYT.setText(youtubeLink);
                        idCategoria = listModelCategoria.get(spCategorias.getSelectedItemPosition()).getId();


                    }
                }).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {

                ModelCancion cancion = new ModelCancion(etIdCancion.getText().toString(), etArtistaCancion.getText().toString(), etNombreCancion.getText().toString(), idCategoria, etLinkYT.getText().toString());
                SubirCancion(DataYT.this, cancion);

                return false;
            }
        });

    }

}
