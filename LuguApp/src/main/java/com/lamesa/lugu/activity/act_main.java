package com.lamesa.lugu.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.lamesa.lugu.BuildConfig;
import com.lamesa.lugu.R;
import com.lamesa.lugu.adapter.AdapterCategoria;
import com.lamesa.lugu.adapter.AdapterFavoritos;
import com.lamesa.lugu.adapter.AdapterHistorial;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;
import com.lamesa.lugu.otros.Firebase;
import com.lamesa.lugu.otros.TinyDB;
import com.lamesa.lugu.otros.statics.Animacion;
import com.lamesa.lugu.player.MediaNotificationManager;
import com.lamesa.lugu.player.library.AndExoPlayerView;
import com.lamesa.lugu.utils.PlayPauseView;
import com.narayanacharya.waveview.WaveView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.otros.metodos.AboutUS;
import static com.lamesa.lugu.otros.metodos.AbrirPagina;
import static com.lamesa.lugu.otros.metodos.ApagarAutoApagado;
import static com.lamesa.lugu.otros.metodos.CargarHome;
import static com.lamesa.lugu.otros.metodos.CargarInterAd;
import static com.lamesa.lugu.otros.metodos.CheckIsFavorite;
import static com.lamesa.lugu.otros.metodos.CompartirApp;
import static com.lamesa.lugu.otros.metodos.DialogoSugerencia;
import static com.lamesa.lugu.otros.metodos.DialogoTemporizador;
import static com.lamesa.lugu.otros.metodos.GuardarCancionFavoritos;
import static com.lamesa.lugu.otros.metodos.OpcionReproductor;
import static com.lamesa.lugu.otros.metodos.PlayOrPause;
import static com.lamesa.lugu.otros.metodos.SolicitarFilm;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.metodos.initFirebase;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_ALEATORIO;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_BUCLE;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBlistHistorial;
import static com.lamesa.lugu.otros.statics.constantes.TBmodoReproductor;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBreproduciendoRadio;
import static com.lamesa.lugu.otros.statics.constantes.mixAdClic;

public class act_main extends AppCompatActivity {


    // act_main2
    public static List<ModelCategoria> mlistCategoria;
    public static RecyclerView mrvBusqueda;
    public static AdapterCategoria mAdapterCategoria;
    public static RecyclerView mrvDestacado;
    public static RecyclerView mrvCategoria;
    public static RecyclerView mrvFilmPeliculas;
    public static RecyclerView mrvFilmSeries;
    public static LinearLayout contenidoCargando;
    public static NestedScrollView contenidoHome;
    public static LinearLayout contenidoSearch;
    public static TinyDB tinyDB;
    public static ArrayList<String> mlistUrls;
    public static SpinKitView animacionCargandoTop;
    public static TextView tvTituloBusqueda;
    public static EditText etBuscar;
    private TextView mtvVerPeliculas;
    private TextView mtvVerSeries;
    private ImageView ivLimpiarBusqueda;
    private BottomNavigationView bottomNavigation;
    private ImageView ivAtrasSearch;
    private ImageView ivMenu;
    private ImageView ivLogo;
    public static int anchoItemFilm = 125;
    private FrameLayout flPeliculas;
    private FrameLayout flSeries;
    public static RecyclerView mrvFilmNetflix;
    private TextView mtvVerNetflix;
    public static RecyclerView mrvFilmEstrenos;
    private FrameLayout flEstrenos;
    private FrameLayout flNetflix;
    private TextView mtvVerEstrenos;
    public static ImageView ivFondoGif;
    private PlayPauseView playPause;
    public static AndExoPlayerView andExoPlayerView;
    public static MediaNotificationManager mediaNotificationManager;
    public static List<ModelCancion> mlistCancion;
    public static TextView tvCancion;
    public static TextView tvArtista;
    public static ProgressBar pbCargandoRadio;

    // recycler views
    public static RecyclerView mrvHistorial;
    public static RecyclerView mrvFavoritos;


    // adapters
    public static AdapterHistorial mAdapterHistorial;
    public static AdapterFavoritos mAdapterFavoritos;
    public static BottomNavigationView bottomNavigationHis_Fav;

    // otros
    public static ImageView ivPlayPause;
    public static SpinKitView spinBuffering;
    public static WaveView waveColor;
    public static WaveView waveBlack;
    public static ImageView ivLikeDislike;
    public static TextView tvCategoria;
    private LinearLayout ContenedorVacio;
    private TextView tvVacio;
    public static ImageView ivSleep;
    public static TextView tvSleep;
    public static ImageView ivOpcionBucle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main2);

        // iniciar notificacion de musica
        mediaNotificationManager = new MediaNotificationManager(this);
        // reproductor exoplayer
        andExoPlayerView = findViewById(R.id.andExoPlayerView);

        SolicitarPermisos(this);

        tinyDB = new TinyDB(this);

        initFirebase(act_main.this, tinyDB);

        VistasHome();
        etBuscar = findViewById(R.id.etBuscar);
        CargarRecyclerHome();

        // Traer todas las listas desde Firebase
        new CargarListas().execute();

        CargarAdMain();  // solo en releaseeeeeeeeeeeeeeeeeeeeeeeeeee

        CheckIsFavorite(act_main.this, tinyDB.getString(TBidCancionSonando));

    }


    @Override
    public void onBackPressed() {

        if (contenidoHome.getVisibility() == GONE) {
            CargarHome(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mixpanel.flush();
        if (mediaNotificationManager != null) {
            mediaNotificationManager.cancelNotify();
        }
        // guardar boolean de eque el reproducotor no estará sonando
        tinyDB.putBoolean(TBreproduciendoRadio, false);
        super.onDestroy();
    }



    private void SolicitarPermisos(Context mContext) {

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

    private void CargarAdMain() {

        //   new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("830648A2D5D5AF09D0FAED08D38E2353"));
        AdView mAdView = findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("830648A2D5D5AF09D0FAED08D38E2353").build();
        mAdView.loadAd(adRequest);
        // mAdView.setAdSize(AdSize.FLUID);
        // listener
        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub


                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                // TODO Auto-generated method stub

                super.onAdClosed();

            }

            @Override
            public void onAdOpened() {
                // TODO Auto-generated method stub
                //region MIX mixEpisodioClic para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("TipoAd", "Banner");
                    //para FB
                    Bundle params = new Bundle();
                    params.putString("TipoAd", "Banner");


                    mFirebaseAnalytics.logEvent(mixAdClic, params);
                    mixpanel.track(mixAdClic, props);
                    Amplitude.getInstance().logEvent(mixAdClic, props);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
                super.onAdOpened();
            }

            @Override
            public void onAdLeftApplication() {
                // TODO Auto-generated method stub


                super.onAdLeftApplication();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // TODO Auto-generated method stub


                super.onAdFailedToLoad(errorCode);
            }

        });


        String idAd = "ca-app-pub-3040756318290255/2989304154";

        if (BuildConfig.APPLICATION_ID.toLowerCase().contains("mesa")) {
            idAd = "ca-app-pub-3040756318290255/3714825369";
        }


        CargarInterAd(act_main.this, idAd, 10);


        Random numRandom2 = new Random();
        int numPosibilidad2 = numRandom2.nextInt(30);


        if (numPosibilidad2 == 20) {
            DialogSettings.theme = DialogSettings.THEME.DARK;
            DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
            MessageDialog.show((AppCompatActivity) act_main.this, "INFORMACIÓN", "Recuerda, si una pelicula o serie no funciona por algún motivo reportalo con la respectiva razón. \n\n*Los videos no se almacenan con nuestros datos ni tenemos control de ellos, por lo tanto solucionar esta clase de inconvenientes es unicamente posible con los reportes* \n\nGracias por tú colaboración, sigue disfrutando del contenido. :)", "VALE").setButtonPositiveTextInfo(new TextInfo().setFontColor(Color.GREEN)).setButtonOrientation(LinearLayout.VERTICAL).setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
                @Override
                public boolean onClick(BaseDialog baseDialog, View v) {
                    Toast.makeText(act_main.this, "¡Gracias!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

    }

    private void CargarRecyclerHome() {

        //region LISTA CATEGORIAS
        mrvCategoria = findViewById(R.id.rv_categorias);
        mrvCategoria.setHasFixedSize(true);
        mrvCategoria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvCategoria.setItemAnimator(new DefaultItemAnimator());
        if (mlistCategoria == null) {
            mlistCategoria = new ArrayList<>();
        }
        mAdapterCategoria = new AdapterCategoria(this, mlistCategoria);
        mrvCategoria.setAdapter(mAdapterCategoria);

        //endregion

        //region LISTA HISTORIAL
        mrvHistorial = findViewById(R.id.rv_historial);
        mrvHistorial.setHasFixedSize(true);
        mrvHistorial.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mrvHistorial.setItemAnimator(new DefaultItemAnimator());
        mAdapterHistorial = new AdapterHistorial(this, tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
        mrvHistorial.setAdapter(mAdapterHistorial);

        // comprobar que la lista de histoirla no esté vacia
        if (tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class).isEmpty()) {
            mrvHistorial.setVisibility(GONE);
            ContenedorVacio.setVisibility(VISIBLE);
        } else {
            mrvHistorial.setVisibility(VISIBLE);
            ContenedorVacio.setVisibility(GONE);
        }

        //endregion

        //region LISTA FAVORITOS
        mrvFavoritos = findViewById(R.id.rv_favoritos);
        mrvFavoritos.setHasFixedSize(true);
        mrvFavoritos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mrvFavoritos.setItemAnimator(new DefaultItemAnimator());
        mAdapterFavoritos = new AdapterFavoritos(this, tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));
        mrvFavoritos.setAdapter(mAdapterFavoritos);

        //endregion



        /*
        //region LISTA CONTENIDO
        if (mlistContenido == null) {
            mlistContenido = new ArrayList<>();
        }
        //   getListaDeFirebase(this);

        //endregion

        //region LISTA NETFLIX
        if (mlistNetflix == null) {
            mlistNetflix = new ArrayList<>();
        }

        mrvFilmNetflix = findViewById(R.id.rvNetflix);
        mrvFilmNetflix.setHasFixedSize(true);

        mrvFilmNetflix.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmNetflix.setItemAnimator(new DefaultItemAnimator());
        mAdapterNetflix= new adapterFilm(this, mlistNetflix);
        mrvFilmNetflix.setAdapter(mAdapterNetflix);

        //   getListaDeFirebase(this);

        //endregion



        //region LISTA GENERO
        mlistGenero = new ArrayList<>();

        //endregion

        //region LISTA DESTACADOS
        mrvDestacado = findViewById(R.id.rvDestacado);
        mrvDestacado.setHasFixedSize(true);
        mrvDestacado.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvDestacado.setItemAnimator(new DefaultItemAnimator());
        if (mlistDestacado == null) {
            mlistDestacado = new ArrayList<>();
        }
        mAdapterDestacado = new adapterDestacado(this, mlistDestacado);
        mrvDestacado.setAdapter(mAdapterDestacado);
        //  getListaDestacados(this);
        //endregion





        //region LISTA SERIES
        mrvFilmSeries = findViewById(R.id.rvSeries);
        mrvFilmSeries.setHasFixedSize(true);

        mrvFilmSeries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmSeries.setItemAnimator(new DefaultItemAnimator());
        if (mlistSeries == null) {
            mlistSeries = new ArrayList<>();
        }
        mAdapterSerie = new adapterFilm(this, mlistSeries);
        mrvFilmSeries.setAdapter(mAdapterSerie);
        //   getListaDeFirebase(this);
        //endregion


        //region LISTA PELICULAS
        mrvFilmPeliculas = findViewById(R.id.rvPeliculas);
        mrvFilmPeliculas.setHasFixedSize(true);

        mrvFilmPeliculas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvFilmPeliculas.setItemAnimator(new DefaultItemAnimator());
        if (mlistPeliculas == null) {
            mlistPeliculas = new ArrayList<>();
        }
        mAdapterPelicula = new adapterFilm(this, mlistPeliculas);
        mrvFilmPeliculas.setAdapter(mAdapterPelicula);
        //  getListaDeFirebase(this);
        //endregion


        //region LISTA FAVORITOS
        mlistFavoritos = new ArrayList<>();

        //endregion

        */

        //region LISTA URLS

        mlistUrls = new ArrayList<>();


    }

    private void VistasHome() {

        contenidoHome = findViewById(R.id.contenidoHome);
        contenidoSearch = findViewById(R.id.contenidoSearch);
        contenidoCargando = findViewById(R.id.contenidoCargando);
        animacionCargandoTop = findViewById(R.id.animacionCargandoTop);


        ivMenu = findViewById(R.id.ivMenu);

        mtvVerPeliculas = findViewById(R.id.tvVerPeliculas);
        mtvVerSeries = findViewById(R.id.tvVerSeries);
        mtvVerNetflix = findViewById(R.id.tvVerNetflix);
        mtvVerEstrenos = findViewById(R.id.tvVerEstrenos);
        ivLogo = findViewById(R.id.ivLogo);
        flPeliculas = findViewById(R.id.flPeliculas);
        flSeries = findViewById(R.id.flSeries);
        flEstrenos = findViewById(R.id.flEstrenos);
        flNetflix = findViewById(R.id.flNetflix);
        flNetflix = findViewById(R.id.flNetflix);

        tvCancion = findViewById(R.id.tv_cancion);
        tvCancion.setText(tinyDB.getString(TBnombreCancionSonando));
        tvCancion.setVisibility(VISIBLE);
        tvArtista = findViewById(R.id.tv_artista);
        tvArtista.setVisibility(VISIBLE);
        tvArtista.setText(tinyDB.getString(TBartistaCancionSonando));
        tvCategoria = findViewById(R.id.tv_categoria);
        tvCategoria.setVisibility(VISIBLE);
        tvCategoria.setText(tinyDB.getString(TBcategoriaCancionSonando));
        pbCargandoRadio = findViewById(R.id.pb_cargandoradio);

        ivPlayPause = findViewById(R.id.iv_playPause);
        if (andExoPlayerView.isPlaying()) {
            PlayOrPause(act_main.this, MediaNotificationManager.STATE_READY);
        }

        spinBuffering = findViewById(R.id.spinBuffering);

        waveColor = findViewById(R.id.waveColor);
        waveColor.pause();
        waveBlack = findViewById(R.id.waveBlack);
        waveBlack.pause();

        ivLikeDislike = findViewById(R.id.iv_likeDislike);

        ContenedorVacio = findViewById(R.id.contenedorVacio);
        tvVacio = findViewById(R.id.tv_textVacio);

        ivSleep = findViewById(R.id.iv_sleep);
        tvSleep = findViewById(R.id.tv_sleep);

        ivOpcionBucle = findViewById(R.id.iv_opcionBucle);
        OpcionReproductor(act_main.this,tinyDB.getString(TBmodoReproductor));

        bottomNavigationHis_Fav = findViewById(R.id.bottomNavigationHis_Fav);
        bottomNavigationHis_Fav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.navigation_historial:
                        menuItem.setChecked(true);
                        if(mrvFavoritos!=null) {
                            mrvFavoritos.setVisibility(GONE);
                        }

                        if(mrvHistorial!=null) {
                            mrvHistorial.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            mrvHistorial.setVisibility(VISIBLE);
                            mrvHistorial.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        }
                        if(mAdapterHistorial!=null) {
                            mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));
                        }

                        if(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class).isEmpty()){
                            if(ContenedorVacio!=null && tvVacio!=null){
                                tvVacio.setText("Sin recientes.");
                                ContenedorVacio.startAnimation(Animacion.exit_ios_anim(act_main.this));
                                ContenedorVacio.setVisibility(VISIBLE);
                                ContenedorVacio.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            }
                        }

                        break;

                    case R.id.navigation_favoritos:
                        menuItem.setChecked(true);

                        if(mrvHistorial!=null) {
                            mrvHistorial.setVisibility(GONE);
                        }
                        if(mrvFavoritos!=null) {
                            mrvFavoritos.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            mrvFavoritos.setVisibility(VISIBLE);
                            mrvFavoritos.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        }

                        if(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class).isEmpty()){
                            if(ContenedorVacio!=null && tvVacio!=null){
                                tvVacio.setText("Sin favoritos.");
                                ContenedorVacio.startAnimation(Animacion.exit_ios_anim(act_main.this));
                                ContenedorVacio.setVisibility(VISIBLE);
                                ContenedorVacio.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            }
                        }

                        break;


                }
                return false;
            }
        });


        // listener para las vistas
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {


                    case R.id.ivLogo:

                        AbrirPagina(act_main.this);
                        //  SubirFilmPelicula(act_main.this);
                        playPause.setState(PlayPauseView.STATE_PAUSE);
                        // videoView.pause();
                        playPause.fadeIn();

                        break;



                    case R.id.ivMenu:



                        /*
                                MessageDialog.build(act_main.this)
                                        .setStyle(DialogSettings.STYLE.STYLE_IOS)
                                        .setTheme(DialogSettings.THEME.DARK)
                                        .setTitle("定制化对话框")
                                        .setMessage("我是内容")
                                        .setOkButton("OK", new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                Toast.makeText(act_main.this, "", Toast.LENGTH_SHORT).show();
                                                return false;
                                            }
                                        })
                                        .show();

                         */


                        //        WaitDialog.show(act_main.this, "请稍候...").setCancelable(true).setTip(R.drawable.learn_ic_check_mark);


                        List<String> opcionMenu = new ArrayList<>();
                        opcionMenu.add("Solicitar pelicula/serie");
                        opcionMenu.add("Enviar sugerencia");
                        opcionMenu.add("Compartir aplicación");
                        opcionMenu.add("Sobre PelisPlusHD");
                        opcionMenu.add("AVISO LEGAL - DMCA");

//您自己的Adapter

                        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                        DialogSettings.theme = DialogSettings.THEME.DARK;
                        DialogSettings.backgroundColor = getResources().getColor(R.color.black);

                        BaseAdapter baseAdapter = new ArrayAdapter(act_main.this, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);


                        BottomMenu.show(act_main.this, baseAdapter, new OnMenuItemClickListener() {
                            @Override
                            public void onClick(String text, int index) {
                                //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
                                //   Toast.makeText(act_main.this, "MAIN", Toast.LENGTH_SHORT).show();

                                switch (index) {

                                    case 0:

                                        SolicitarFilm(act_main.this);
                                        andExoPlayerView.setPlayWhenReady(true);

                                        break;

                                    case 1:

                                        DialogoSugerencia(act_main.this);
                                        andExoPlayerView.pausePlayer();

                                        break;

                                    case 2:
                                        CompartirApp(act_main.this);
                                        break;

                                    case 3:


                                        AboutUS(act_main.this, tinyDB, false);


                                        String youtubeLink = "https://www.youtube.com/watch?v=7LodLbe_XIA";

                                        getLinkAndPlay(act_main.this, youtubeLink, 1);


                                        break;

                                    case 4:

                                        DialogSettings.theme = DialogSettings.THEME.DARK;
                                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                                        DialogSettings.backgroundColor = getResources().getColor(R.color.black);

                                        MessageDialog.show((AppCompatActivity) act_main.this, "DMCA", "" +
                                                "" +
                                                "No multimedia file is being hosted by us on this app.\n" +
                                                "\n" +
                                                "We are not associated with the list of contents found on remote servers. We have no connection or association with such content.\n" +
                                                "The mp4, avi, mkv files that are available for download are not hosted on PelisPLsuHD app and are hosted on other servers (therefore, not our host service).\n" +
                                                "This app (PelisPlusHD) functions as a movie search engine and does not store or host any files or other copyrighted material. We follow copyright laws, but if you find any search results that you feel are illegal, you are asked to complete the form and send an email to appbuho@gmail.com\n" +
                                                "In fact, we adhere to the rights of producers and artists. We assure you that your work will be safe and legal, which will result in a positive experience for each of you, whether you are a creator or a musical artist. Please note that if any person knowingly or intentionally misrepresents any material or activity listed in Section 512(f), it would be considered a violation of copyright law. Then, if you are doing so, you are liable for your own harm. But keep one thing in mind: Don’t make any false claims about the infringed content!\n" +
                                                "\n" +
                                                "The complete information contained in the legal notice may also be sent to the interested party providing the content that is being infringed.", "SI", "CANCELAR")
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


                                        break;
                                }

                            }
                        }).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("MENU");


                        break;

                    case R.id.iv_playPause:


                        //    Toast.makeText(act_main.this, "iv_playPause", Toast.LENGTH_SHORT).show();


                        // cambiar icono entre play y pause
                        if (ivPlayPause.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_play).getConstantState()) {
                            // reproducir

                            // comprobar si es la primera vez que se da clic a play
                            if (!tinyDB.getString(TBidCancionSonando).isEmpty()) {
                                PlayOrPause(act_main.this, MediaNotificationManager.STATE_PLAY);
                                // reproducir la ultima cancion reproducida unicamente si se incia la app sin clickear alguna lista
                                if (!andExoPlayerView.isPlaying()) {
                                    // comprobar que si haya una cancion guardada
                                    getLinkAndPlay(act_main.this, tinyDB.getString(TBlinkCancionSonando), 1);
                                    tvCancion.setText(tinyDB.getString(TBnombreCancionSonando));
                                    tvArtista.setText(tinyDB.getString(TBartistaCancionSonando));
                                }
                            } else {
                                // primera ves que se da clic a play
                                Toast.makeText(act_main.this, "Elija una categoria", Toast.LENGTH_SHORT).show();
                            }

                        } else if (ivPlayPause.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_pausa).getConstantState()) {
                            // pausar
                            PlayOrPause(act_main.this, MediaNotificationManager.STATE_PAUSE);
                        }


                        break;


                    case R.id.iv_likeDislike:

                        // comprobar si es la primera vez que se da clic a favorito
                        if (!tinyDB.getString(TBidCancionSonando).isEmpty()) {
                            if (ivLikeDislike.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.learn_ic_like).getConstantState()) {
                                // Quitar de lista de favoritos
                              //  Toast.makeText(act_main.this, "learn_ic_like", Toast.LENGTH_SHORT).show();
                                GuardarCancionFavoritos(act_main.this, tinyDB.getString(TBidCancionSonando), false);


                            } else if (ivLikeDislike.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.learn_ic_dislike).getConstantState()) {
                                // Guardar en favoritos
                              //  Toast.makeText(act_main.this, "learn_ic_dislike", Toast.LENGTH_SHORT).show();
                                GuardarCancionFavoritos(act_main.this, tinyDB.getString(TBidCancionSonando), true);

                            }
                        } else {
                            // primera ves que se da clic a favorito
                            Toast.makeText(act_main.this, "No hay ninguna canción reproduciendoce", Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.iv_sleep:

                        // comprobar si es la primera vez que se da clic a favorito

                        if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_off).getConstantState()) {
                            // Iniciar dialogo de temporizador
                            DialogoTemporizador(act_main.this);

                        } else if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_on).getConstantState()) {
                            // apagar temporizador antes de iniciar uno nuevo
                            ApagarAutoApagado(act_main.this);
                            DialogoTemporizador(act_main.this);

                        }


                        break;


                    case R.id.iv_opcionBucle:

                        // comprobar si es la primera vez que se da clic a favorito

                        if (ivOpcionBucle.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_bucle).getConstantState()) {
                            // activar modo aleatorio
                            OpcionReproductor(act_main.this,REPRODUCTOR_ALEATORIO);
                            Toast.makeText(act_main.this, "Modo Aleatorio activado", Toast.LENGTH_SHORT).show();

                        } else if (ivOpcionBucle.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_aleatorio).getConstantState()) {
                            // activar modo bucle
                            OpcionReproductor(act_main.this,REPRODUCTOR_BUCLE);
                            Toast.makeText(act_main.this, "Modo Bucle activado", Toast.LENGTH_SHORT).show();

                        }


                        break;

                }


            }
        };


        // asignar listener a views
        mtvVerPeliculas.setOnClickListener(listener);
        mtvVerSeries.setOnClickListener(listener);
        ivMenu.setOnClickListener(listener);
        ivLogo.setOnClickListener(listener);
        mtvVerNetflix.setOnClickListener(listener);
        mtvVerEstrenos.setOnClickListener(listener);
        animacionCargandoTop.setOnClickListener(listener);
        ivPlayPause.setOnClickListener(listener);
        ivLikeDislike.setOnClickListener(listener);
        ivSleep.setOnClickListener(listener);
        ivOpcionBucle.setOnClickListener(listener);

        ivLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                DialogSettings.theme = DialogSettings.THEME.DARK;

                InputDialog.show((AppCompatActivity) act_main.this, "¿Que te ha parecido PelisPlsHD?", "Cuentanos que te ha gustado de esta aplicaciòn, sientase libre de opinar", "ENVIAR", "CERRAR")
                        .setCancelable(false)
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                // Toast.makeText(act_main.this, "ok", Toast.LENGTH_SHORT).show();
                                //  if(inputStr.contains(""))
                                return false;
                            }
                        }).setOnOtherButtonClickListener(new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {

                        return false;
                    }
                });

                return false;
            }

        });




        // cargar gif de fondo
        ivFondoGif = (ImageView) findViewById(R.id.iv_fondoGif);
        Glide.with(this)
                .load("https://cdnb.artstation.com/p/assets/images/images/022/740/889/large/hiromi-_11-concept3-min.jpg?1576528739")
                //   .error(R.drawable.ic_alert)
                //.placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(ivFondoGif);

        Glide.with(this)
                .asBitmap()
                .load("https://cdnb.artstation.com/p/assets/images/images/022/740/889/large/hiromi-_11-concept3-min.jpg?1576528739")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                     //   setLogInfo(this,"MediaNotificationManager.startNotify.onResourceReady","Cargar imagen en Notificacion",false);

                        // TODO Do some work: pass this bitmap

                      //  Toast.makeText(act_main.this, getDominantColor(resource), Toast.LENGTH_SHORT).show();

                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                // Do something with colors...
                                waveColor.setWaveColor(palette.getLightMutedColor(Color.WHITE));
                                tvCancion.setTextColor(palette.getMutedColor(Color.WHITE));
                                tvArtista.setTextColor(palette.getDarkVibrantColor(Color.WHITE));
                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // setLogInfo(mContext,"MediaNotificationManager.startNotify.onLoadCleared","Cargar imagen en Notificacion",false);
                    }
                });

    }


    private class CargarListas extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute", "On pre Exceute......");
            Toast.makeText(act_main.this, "Cargando...", Toast.LENGTH_SHORT).show();

        }

        protected String doInBackground(Void... arg0) {
            Log.d(TAG + " DoINBackGround", "On doInBackground...");


            Thread thread = new Thread() {
                @Override
                public void run() {


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //do stuff like remove view etc


                            // traer lista de categorias desde FB
                            getListas(act_main.this);
                            // getListaCategorias(act_main.this);
                            // getListaDeFirebase(act_main.this);
                            // getListaDestacados(act_main.this);
                        }
                    });


                }

                ;
            };
            thread.start();

            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // WaitDialog.show(act_main.this, "Cargando contenido...").setCancelable(true);

            Log.d(TAG + " onPostExecute", "" + result);
        }
    }

    public static void getListas(Context mContext) {
        Firebase.getListaCanciones(mContext, mlistCategoria, mlistCancion, tinyDB);
        Firebase.getListaCategorias(mContext, mlistCategoria, mAdapterCategoria);
        Firebase.getListaPorCategoria(mContext, mlistCategoria, mlistCancion, tinyDB);
    }


}
