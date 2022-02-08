package com.lamesa.lugu.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.otros.metodos.AboutUS;
import static com.lamesa.lugu.otros.metodos.AbrirPagina;
import static com.lamesa.lugu.otros.metodos.ApagarAutoApagado;
import static com.lamesa.lugu.otros.metodos.CategoriaAleatoria;
import static com.lamesa.lugu.otros.metodos.CheckIsFavorite;
import static com.lamesa.lugu.otros.metodos.CompartirApp;
import static com.lamesa.lugu.otros.metodos.Dialogo5Estrellas;
import static com.lamesa.lugu.otros.metodos.DialogoMiCancion;
import static com.lamesa.lugu.otros.metodos.DialogoModoOffline;
import static com.lamesa.lugu.otros.metodos.DialogoOpBateria;
import static com.lamesa.lugu.otros.metodos.DialogoPoliticas2;
import static com.lamesa.lugu.otros.metodos.DialogoReport;
import static com.lamesa.lugu.otros.metodos.DialogoSalir;
import static com.lamesa.lugu.otros.metodos.DialogoSugerencia;
import static com.lamesa.lugu.otros.metodos.DialogoSupArt;
import static com.lamesa.lugu.otros.metodos.DialogoSupArtista;
import static com.lamesa.lugu.otros.metodos.DialogoTemporizador;
import static com.lamesa.lugu.otros.metodos.GuardarCancionFavoritos;
import static com.lamesa.lugu.otros.metodos.OpcionReproductor;
import static com.lamesa.lugu.otros.metodos.SonidoVHS;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.metodos.initFirebase;
import static com.lamesa.lugu.otros.mob.inter.CargarInterAleatorio;
import static com.lamesa.lugu.otros.mob.inter.loadInterstitial;
import static com.lamesa.lugu.otros.mob.inter.showInterstitial;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_ALEATORIO;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_BUCLE;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBimagenFondo;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCategorias;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCustom;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBlistHistorial;
import static com.lamesa.lugu.otros.statics.constantes.TBlistImagenes;
import static com.lamesa.lugu.otros.statics.constantes.TBmodoReproductor;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBreproduciendoRadio;
import static com.lamesa.lugu.otros.statics.constantes.TBsizeReproductor;
import static com.lamesa.lugu.otros.statics.constantes.mixAdOpened;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
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
import com.github.matteobattilana.weather.WeatherView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.MessageDialog;
import com.lamesa.lugu.R;
import com.lamesa.lugu.adapter.AdapterCategoria;
import com.lamesa.lugu.adapter.AdapterFavoritos;
import com.lamesa.lugu.adapter.AdapterHistorial;
import com.lamesa.lugu.adapter.AdapterListCustom;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;
import com.lamesa.lugu.model.ModelListCustom;
import com.lamesa.lugu.otros.Firebase;
import com.lamesa.lugu.otros.TinyDB;
import com.lamesa.lugu.otros.statics.Animacion;
import com.lamesa.lugu.player.MediaNotificationManager;
import com.lamesa.lugu.player.library.MusicPlayer;
import com.narayanacharya.waveview.WaveView;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class act_main extends AppCompatActivity {


    public static List<ModelListCustom> mlistCustom;

    public static AdapterCategoria mAdapterCategoria;
    public static RecyclerView mrvCategoria;
    public static NestedScrollView contenidoHome;
    public static LinearLayout contenidoSearch;
    public static TinyDB tinyDB;
    public static ImageView ivFondoGif;
    public static ImageView ivFondoVHS;
    public static MusicPlayer musicPlayer;
    public static MediaNotificationManager mediaNotificationManager;
    public static List<ModelCancion> mlistCancion;

    public static ProgressBar pbCargandoRadio;

    //region  recycler views
    public static RecyclerView mrvHistorial;
    public static RecyclerView mrvFavoritos;
    public static RecyclerView mrvMisListas;
    //endregion

    //region adapters
    public static AdapterHistorial mAdapterHistorial;
    public static AdapterFavoritos mAdapterFavoritos;
    public static AdapterListCustom mAdapterListCustom;
    public static BottomNavigationView bottomNavigationHis_Fav;
    //endregion

    // otros
    public static ImageView ivPlayPause;
    public static SpinKitView spinBuffering;
    public static WaveView waveColor;
    public static WaveView waveBlack;
    public static ImageView ivLikeDislike;
    public static LinearLayout ContenedorVacio;
    public static TextView tvVacio;
    public static ImageView ivSleep;
    public static TextView tvSleep;
    public static ImageView ivOpcionBucle;
    public static WeatherView weatherView;
    public static ImageView ivLupa;
    public static List<ModelCategoria> mlistCategoria = new ArrayList<>();
    public static ImageView ivStyle;
    public static MediaPlayer soundVHS;
    public static CardView cdMusicSeek;
    private ImageView ivOpcionPlayer;
    private MaterialCardView cdOpPlayer;
    private MaterialCardView cdPlayer;
    private int siguienteSize;
    private ImageView ivOffline;
    private int versionNumber;
    private String versionName;
    public static com.hanks.htextview.evaporate.EvaporateTextView tvCancion;
    public static com.hanks.htextview.evaporate.EvaporateTextView tvCategoria;
    public static com.hanks.htextview.evaporate.EvaporateTextView tvArtista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main2);

        // INICAR MEDIANOTIFICACTION
        mediaNotificationManager = new MediaNotificationManager(this);
        // reproductor exoplayer
        musicPlayer = findViewById(R.id.musicPlayer);

        // SolicitarPermisos(this);

        tinyDB = new TinyDB(this);
        // imagen por defecto de fondo
        tinyDB.putString(TBimagenFondo, "https://i.pinimg.com/originals/76/09/46/7609468e97e15d1da8d14d534be7366c.gif");

        initFirebase(act_main.this, tinyDB);

        VistasHome();
        CargarRecyclerHome();

        // Traer todas las listas desde Firebase
        new CargarListas().execute();

        CheckIsFavorite(act_main.this, tinyDB.getString(TBidCancionSonando));

        // cargar adinter para ser mostrada
        loadInterstitial(act_main.this);
        CargarInterAleatorio(act_main.this, 4);
        // cargar banner
        CargarBanner();

        // dialogo para desactivar la optimizacion de la app
        DialogoOpBateria(act_main.this);


        AppVersion();

        soundVHS = MediaPlayer.create(act_main.this, R.raw.tv09);

    }

    //traer listas de firebase
    public static void getListas(Context mContext) {
        Firebase.getListaCanciones(mContext, mlistCategoria, mlistCancion, tinyDB);
        Firebase.getListaCategorias(mContext, mlistCategoria, mAdapterCategoria, tinyDB);
        Firebase.getListaPorCategoria(mContext, mlistCategoria, mlistCancion, tinyDB);
        Firebase.getListaImagenes(tinyDB);
    }

    public static void CargarImagenFondo(Context mContext) {

        SonidoVHS(mContext, soundVHS, false);

        // cargar una imagen random
        if (!tinyDB.getListString(TBlistImagenes).isEmpty() && tinyDB.getListString(TBlistImagenes) != null) {
            Random random = new Random();
            // obtener link alaeatorio
            int numRandom = random.nextInt(tinyDB.getListString(TBlistImagenes).size());
            // guardar link de imagen en tiny
            tinyDB.putString(TBimagenFondo, tinyDB.getListString(TBlistImagenes).get(numRandom));
        }

        // cargar imagen en fondo
        Glide.with(mContext)
                .load(tinyDB.getString(TBimagenFondo))
                //.error(R.drawable.ic_alert)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .into(ivFondoGif);
        // extraer colores de imagenes
        Glide.with(mContext)
                .asBitmap()
                .load(tinyDB.getString(TBimagenFondo))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        //   setLogInfo(this,"MediaNotificationManager.startNotify.onResourceReady","Cargar imagen en Notificacion",false);

                        // TODO Do some work: pass this bitmap

                        //  Toast.makeText(act_main.this, getDominantColor(resource), Toast.LENGTH_SHORT).show();
                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            public void onGenerated(Palette palette) {
                                // Do something with colors...
                                waveBlack.setWaveColor(palette.getDominantColor(mContext.getResources().getColor(R.color.learn_colorPrimary)));
                                tvCancion.setTextColor(palette.getLightMutedColor(Color.WHITE));
                                tvCategoria.setTextColor(palette.getMutedColor(Color.WHITE));
                                tvArtista.setTextColor(palette.getLightVibrantColor(Color.WHITE));
                                waveColor.setWaveColor(palette.getDominantColor(Color.WHITE));
                                spinBuffering.setColor(palette.getLightMutedColor(mContext.getResources().getColor(R.color.learn_colorPrimary)));
                                mContext.getResources().getDrawable(R.drawable.ic_pausa).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                mContext.getResources().getDrawable(R.drawable.ic_play).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                mContext.getResources().getDrawable(R.drawable.ic_bucle).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                mContext.getResources().getDrawable(R.drawable.ic_aleatorio).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                mContext.getResources().getDrawable(R.drawable.ic_dislike).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                mContext.getResources().getDrawable(R.drawable.ic_like).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.learn_colorPrimary)));
                                mContext.getResources().getDrawable(R.drawable.ic_expandir).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                mContext.getResources().getDrawable(R.drawable.ic_contraer).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));
                                // mContext.getResources().getDrawable(R.drawable.ic_no_signal).setTint(palette.getLightMutedColor(mContext.getResources().getColor(R.color.item_disable)));


                                bottomNavigationHis_Fav.setItemRippleColor(ColorStateList.valueOf(palette.getDarkVibrantColor(mContext.getResources().getColor(R.color.gray))));
                                //   bottomNavigationHis_Fav.setItemIconTintList(ColorStateList.valueOf(palette.getLightMutedColor(mContext.getResources().getColor(R.color.learn_colorPrimary))));


                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // setLogInfo(mContext,"MediaNotificationManager.startNotify.onLoadCleared","Cargar imagen en Notificacion",false);
                    }
                });

    }

    private void AppVersion() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumber = pinfo.versionCode;
            versionName = pinfo.versionName;

            TextView tvVersion = findViewById(R.id.tv_version);
            tvVersion.setText("v" + versionName.replace("beta", "").replace("admin", "").trim());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DialogoSalir(act_main.this);
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

    @Override
    protected void onStart() {
        if (tvCategoria != null) {
            tvCategoria.animateText(tinyDB.getString(TBcategoriaCancionSonando));
        }
        if (tvCancion != null) {
            tvCancion.animateText(tinyDB.getString(TBnombreCancionSonando));
        }
        if (tvArtista != null) {
            tvArtista.animateText(tinyDB.getString(TBartistaCancionSonando));
        }
        super.onStart();
    }

    private void CargarBanner() {

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
                //region MIX mixAdClic para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("TipoAd", "Banner");
                    //para FB
                    Bundle params = new Bundle();
                    params.putString("TipoAd", "Banner");


                    mFirebaseAnalytics.logEvent(mixAdOpened, params);
                    mixpanel.track(mixAdOpened, props);
                    Amplitude.getInstance().logEvent(mixAdOpened, props);

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


    }

    private void CargarRecyclerHome() {

        mlistCustom = tinyDB.getListModelListCustom(TBlistCustom, ModelListCustom.class);
        mlistCustom = new ArrayList<>();
        ModelListCustom listCustom = new ModelListCustom("dsasd", "https://indiehoy.com/wp-content/uploads/2020/05/playlists-lofi-hip-hop-1.jpg", "Mi lista para dormir");

        mlistCustom.add(listCustom);

        tinyDB.putListModelListCustom(TBlistCustom, mlistCustom);

        //region LISTA CATEGORIAS
        mrvCategoria = findViewById(R.id.rv_categorias);
        mrvCategoria.setHasFixedSize(true);
        mrvCategoria.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvCategoria.setItemAnimator(new DefaultItemAnimator());
        mlistCategoria = tinyDB.getListModelCategoria(TBlistCategorias, ModelCategoria.class);
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

        //region LISTA CUSTOM

        mrvMisListas = findViewById(R.id.rv_mislistas);
        mrvMisListas.setHasFixedSize(true);
        mrvMisListas.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mrvMisListas.setItemAnimator(new DefaultItemAnimator());
        mAdapterListCustom = new AdapterListCustom(this, tinyDB.getListModelListCustom(TBlistCustom, ModelListCustom.class));
        mrvMisListas.setAdapter(mAdapterListCustom);

        //endregion


    }

    private void VistasHome() {

        contenidoHome = findViewById(R.id.contenidoHome);

        ImageView ivMenu = findViewById(R.id.ivMenu);

        TextView mtvVerEstrenos = findViewById(R.id.tvVerEstrenos);
        ImageView ivLogo = findViewById(R.id.ivLogo);


        tvCancion = findViewById(R.id.tv_cancion);
        tvCancion.animateText(tinyDB.getString(TBnombreCancionSonando));
        tvCancion.setVisibility(VISIBLE);
        tvArtista = findViewById(R.id.tv_artista);
        tvArtista.setVisibility(VISIBLE);
        tvArtista.animateText(tinyDB.getString(TBartistaCancionSonando));
        tvCategoria = findViewById(R.id.tv_categoria);
        tvCategoria.setVisibility(VISIBLE);
        tvCategoria.animateText(tinyDB.getString(TBcategoriaCancionSonando));
        pbCargandoRadio = findViewById(R.id.pb_cargandoradio);

        ivPlayPause = findViewById(R.id.iv_playPause);

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

        ImageView ivReport = findViewById(R.id.iv_report);
        ImageView ivSupArt = findViewById(R.id.iv_support_art);


        ivLupa = findViewById(R.id.iv_lupa);

        ivStyle = findViewById(R.id.iv_style);
        ivOpcionBucle = findViewById(R.id.iv_opcionBucle2);

        // cargar gif de fondo
        ivFondoGif = findViewById(R.id.iv_fondoGif);
        //region guardar ivOpcionBucle segun el icono
        if (ivOpcionBucle.getTag().toString().contains("ic_aleatorio")) {
            // guardar modo de reproductor REPRODUCTOR_ALEATORIO
            tinyDB.putString(TBmodoReproductor, REPRODUCTOR_ALEATORIO);
        } else {
            // guardar modo de reproductor REPRODUCTOR_BUCLE
            tinyDB.putString(TBmodoReproductor, REPRODUCTOR_BUCLE);
        }
        OpcionReproductor(act_main.this, tinyDB.getString(TBmodoReproductor));
        //endregion


        ivOpcionPlayer = findViewById(R.id.iv_opReproductor);
        ivOpcionPlayer.setVisibility(GONE);
        cdOpPlayer = findViewById(R.id.cd_opPlayer);
        cdPlayer = findViewById(R.id.cd_player);


        weatherView = findViewById(R.id.weather_view);
        ivOffline = findViewById(R.id.iv_offline);
        cdMusicSeek = findViewById(R.id.cd_musicSeek);


        bottomNavigationHis_Fav = findViewById(R.id.bottomNavigationHis_Fav);
        bottomNavigationHis_Fav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.navigation_historial:
                        menuItem.setChecked(true);

                        mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));


                        if (mrvFavoritos != null) {
                            mrvFavoritos.setVisibility(GONE);
                        }


                        // comprobar que la lista de histoirla no esté vacia
                        if (tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class).isEmpty()) {
                            //   Toast.makeText(mContext, "lista vacia", Toast.LENGTH_SHORT).show();
                            mrvHistorial.setVisibility(GONE);
                            ContenedorVacio.setVisibility(VISIBLE);
                            tvVacio.setText(R.string.sin_recientes);
                            ContenedorVacio.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ContenedorVacio.setVisibility(VISIBLE);
                            ContenedorVacio.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        } else {
                            if (mrvHistorial != null) {
                                mrvHistorial.startAnimation(Animacion.exit_ios_anim(act_main.this));
                                mrvHistorial.setVisibility(VISIBLE);
                                mrvHistorial.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            }
                            ContenedorVacio.setVisibility(GONE);
                        }

                        break;

                    case R.id.navigation_favoritos:
                        menuItem.setChecked(true);

                        mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));

                        if (mrvHistorial != null) {
                            mrvHistorial.setVisibility(GONE);
                        }

                        if (tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class).isEmpty() || tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class) == null) {
                            tvVacio.setText(R.string.sin_favoritos);
                            ContenedorVacio.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ContenedorVacio.setVisibility(VISIBLE);
                            ContenedorVacio.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        } else {
                            ContenedorVacio.setVisibility(GONE);
                            if (mrvFavoritos != null) {
                                mrvFavoritos.startAnimation(Animacion.exit_ios_anim(act_main.this));
                                mrvFavoritos.setVisibility(VISIBLE);
                                mrvFavoritos.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            }
                        }
                        break;


                        /*
                    case R.id.navigation_offline:

                        Dialogo5Estrellas(act_main.this);
                        DialogoModoOffline(act_main.this);

                        break;

                         */
                }
                return false;
            }
        });
        bottomNavigationHis_Fav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_historial:

                        mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));


                        if (mrvFavoritos != null) {
                            mrvFavoritos.setVisibility(GONE);
                        }

                        if (mrvHistorial != null) {
                            mrvHistorial.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            mrvHistorial.setVisibility(VISIBLE);
                            mrvHistorial.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        }

                        // comprobar que la lista de histoirla no esté vacia
                        if (tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class).isEmpty()) {
                            //   Toast.makeText(mContext, "lista vacia", Toast.LENGTH_SHORT).show();
                            mrvHistorial.setVisibility(GONE);
                            ContenedorVacio.setVisibility(VISIBLE);
                            tvVacio.setText(R.string.sin_recientes);
                            ContenedorVacio.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ContenedorVacio.setVisibility(VISIBLE);
                            ContenedorVacio.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        } else {
                            mrvHistorial.setVisibility(VISIBLE);
                            ContenedorVacio.setVisibility(GONE);
                        }

                        break;

                    case R.id.navigation_favoritos:

                        mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));


                        if (mrvHistorial != null) {
                            mrvHistorial.setVisibility(GONE);
                        }

                        if (tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class).isEmpty() || tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class) == null) {
                            tvVacio.setText(R.string.sin_favoritos);
                            ContenedorVacio.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ContenedorVacio.setVisibility(VISIBLE);
                            ContenedorVacio.startAnimation(Animacion.enter_ios_anim(act_main.this));
                        } else {
                            ContenedorVacio.setVisibility(GONE);
                            if (mrvFavoritos != null) {
                                mrvFavoritos.startAnimation(Animacion.exit_ios_anim(act_main.this));
                                mrvFavoritos.setVisibility(VISIBLE);
                                mrvFavoritos.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            }

                        }

                        break;
                }
            }
        });

        // listener para las vistas
        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {

                switch (v.getId()) {


                    case R.id.ivLogo:

                        AbrirPagina(act_main.this, "https://lugumusic.page.link/website");

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


                        opcionMenu.add(getString(R.string.visitar_website));
                        opcionMenu.add(getString(R.string.submitsong));
                        opcionMenu.add(getString(R.string.enviar_sugerencia));
                        opcionMenu.add(getString(R.string.compartir_app));
                        opcionMenu.add("ABOUT " + act_main.this.getResources().getString(R.string.app_name));
                        opcionMenu.add(getString(R.string.aviso_legal));
                        opcionMenu.add(getString(R.string.title_terms));


                        DialogSettings.style = DialogSettings.STYLE.STYLE_MIUI;
                        DialogSettings.theme = DialogSettings.THEME.DARK;
                        DialogSettings.backgroundColor = getResources().getColor(R.color.fondo_main);

                        BaseAdapter baseAdapter = new ArrayAdapter(act_main.this, com.kongzue.dialog.R.layout.item_bottom_menu_material, opcionMenu);

                        BottomMenu.show(act_main.this, baseAdapter, new OnMenuItemClickListener() {
                            @Override
                            public void onClick(String text, int index) {
                                //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
                                //   Toast.makeText(act_main.this, "MAIN", Toast.LENGTH_SHORT).show();

                                switch (index) {


                                    case 0:

                                        AbrirPagina(act_main.this, "https://lugumusic.page.link/website");

                                        break;


                                    case 1:

                                        DialogoMiCancion(act_main.this);

                                        break;

                                    case 2:

                                        DialogoSugerencia(act_main.this);

                                        break;

                                    case 3:

                                        CompartirApp(act_main.this);

                                        break;


                                    case 4:

                                        AboutUS(act_main.this, tinyDB, false);

                                        break;

                                    case 5:

                                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                                        DialogSettings.theme = DialogSettings.THEME.DARK;
                                        DialogSettings.backgroundColor = getResources().getColor(R.color.black);

                                        MessageDialog.show(act_main.this, "DMCA", "" +
                                                "" +
                                                "No multimedia file is being hosted by us on this app.\n" +
                                                "\n" +
                                                "We are not associated with the list of contents found on remote servers. We have no connection or association with such content.\n" +
                                                "The mp3, jpg, png files that are available are not hosted on (" + act_main.this.getResources().getString(R.string.app_name) + ") app and are hosted on other servers (therefore, not our host service).\n" +
                                                "This app (" + act_main.this.getResources().getString(R.string.app_name) + ") functions as a lofi music search engine and does not store or host any files or other copyrighted material. We follow copyright laws, but if you find any search results that you feel are illegal, you are asked to complete the form and send an email to lugulofimusic@gmail.com\n" +
                                                "In fact, we adhere to the rights of producers and artists. We assure you that your work will be safe and legal, which will result in a positive experience for each of you, whether you are a creator or a musical artist. Please note that if any person knowingly or intentionally misrepresents any material or activity listed in Section 512(f), it would be considered a violation of copyright law. Then, if you are doing so, you are liable for your own harm. But keep one thing in mind: Don’t make any false claims about the infringed content!\n" +
                                                "\n" +
                                                "The complete information contained in the legal notice may also be sent to the interested party providing the content that is being infringed.", "SI", "CANCELAR")
                                                .setOkButton(getString(R.string.open_email)).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {

                                                Intent intent = new Intent(Intent.ACTION_SEND);
                                                intent.setType("message/rfc822");
                                                intent.putExtra(Intent.EXTRA_EMAIL, "lugulofimusic@gmail.com");
                                                intent.putExtra(Intent.EXTRA_SUBJECT, "DMCA");
                                                intent.putExtra(Intent.EXTRA_STREAM, "");
                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                    startActivity(intent);
                                                }

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

                                    case 6:

                                        DialogoPoliticas2(act_main.this);

                                        break;

                                }

                            }
                        }).setCancelButtonText(act_main.this.getResources().getString(R.string.cerrar)).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("MENU\n" + "LUGU - lofi music v" + versionName.replace("beta", "").replace("admin", "").trim());

                        break;

                    case R.id.iv_playPause:

                        // cambiar icono entre play y pause
                        if (ivPlayPause.getTag().toString().contains("ic_play")) {
                            // reproducir
                            // comprobar si es la primera vez que se da clic a play
                            if (!tinyDB.getString(TBidCancionSonando).isEmpty()) {
                                musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PLAY);
                                // reproducir la ultima cancion reproducida unicamente si se incia la app sin clickear alguna lista
                                if (!musicPlayer.isPlaying()) {
                                    // comprobar que si haya una cancion guardada
                                    getLinkAndPlay(act_main.this, tinyDB.getString(TBlinkCancionSonando), 1);
                                    tvCancion.animateText(tinyDB.getString(TBnombreCancionSonando));
                                    tvArtista.animateText(tinyDB.getString(TBartistaCancionSonando));
                                }
                            } else {
                                // primera ves que se da clic a play
                                Toast.makeText(act_main.this, R.string.elija_categoria, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // pausar
                            musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PAUSE);
                        }


                        break;


                    case R.id.iv_likeDislike:

                        // comprobar si es la primera vez que se da clic a favorito
                        if (!tinyDB.getString(TBidCancionSonando).isEmpty()) {
                            if (ivLikeDislike.getTag().toString().contains("ic_like")) {
                                // Quitar de lista de favoritos
                                //  Toast.makeText(act_main.this, "learn_ic_like", Toast.LENGTH_SHORT).show();
                                GuardarCancionFavoritos(act_main.this, tinyDB.getString(TBidCancionSonando), false);


                            } else if (ivLikeDislike.getTag().toString().contains("ic_dislike")) {
                                // Guardar en favoritos
                                //  Toast.makeText(act_main.this, "ic_dislike", Toast.LENGTH_SHORT).show();
                                GuardarCancionFavoritos(act_main.this, tinyDB.getString(TBidCancionSonando), true);

                                /**
                                 *  fix bug release v5
                                 */

                                // alerta informar a usuario sobre las listas
                                if (tinyDB.getBoolean("TBalertListas") == false) {
                                    Alerter.create(act_main.this).setTitle(R.string.title_alert_list)
                                            .setText(R.string.msg_alert_list)
                                            //.setBackgroundResource(R.drawable.shape_controller_top_gradient)
                                            // .setIcon(R.drawable.uvv_on_error)
                                            .setDuration(10000)
                                            .setTextTypeface(Typeface.createFromAsset(act_main.this.getAssets(), "poppins_regular.ttf"))
                                            .setBackgroundColorRes(R.color.fondo_blank) // or setBackgroundColorInt(Color.CYAN)
                                            .addButton("OK", R.style.TabTextStyle, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    tinyDB.putBoolean("TBalertListas", true);
                                                    if (Alerter.isShowing()) {
                                                        Alerter.hide();
                                                    }
                                                }
                                            }).show();
                                }
                            }
                        } else {
                            // primera ves que se da clic a favorito
                            Toast.makeText(act_main.this, R.string.sin_cancion_reproduciendo, Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.iv_sleep:

                        // comprobar si es la primera vez que se da clic a favorito

                        showInterstitial(act_main.this);

                        //  video.createAndLoadRewardedAd(act_main.this);

                        /* si el anuncio de video cargó, mostrar :
                        if (rewardedAd!=null && rewardedAd.isLoaded()) {
                            Activity activityContext = act_main.this;
                            RewardedAdCallback adCallback = new RewardedAdCallback() {
                                @Override
                                public void onRewardedAdOpened() {
                                    // Ad opened.
                                }

                                @Override
                                public void onRewardedAdClosed() {
                                    // Ad closed.
                                    video.onRewardedAdClosed(act_main.this);
                                }

                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem reward) {
                                    // User earned reward.
                                    if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_off).getConstantState()) {
                                        // Iniciar dialogo de temporizador
                                        DialogoTemporizador(act_main.this);

                                    } else if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_on).getConstantState()) {
                                        // apagar temporizador antes de iniciar uno nuevo
                                        ApagarAutoApagado(act_main.this);
                                        DialogoTemporizador(act_main.this);

                                    }
                                }

                                @Override
                                public void onRewardedAdFailedToShow(AdError adError) {
                                    // Ad failed to display.
                                    video.onRewardedAdClosed(act_main.this);
                                    if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_off).getConstantState()) {
                                        // Iniciar dialogo de temporizador
                                        DialogoTemporizador(act_main.this);

                                    } else if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_on).getConstantState()) {
                                        // apagar temporizador antes de iniciar uno nuevo
                                        ApagarAutoApagado(act_main.this);
                                        DialogoTemporizador(act_main.this);

                                    }

                                }
                            };
                            rewardedAd.show(activityContext, adCallback);
                        } else {
                            Toast.makeText(act_main.this, "The rewarded ad wasn't loaded yet.", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                            if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_off).getConstantState()) {
                                // Iniciar dialogo de temporizador
                                DialogoTemporizador(act_main.this);

                            } else if (ivSleep.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_moon_on).getConstantState()) {
                                // apagar temporizador antes de iniciar uno nuevo
                                ApagarAutoApagado(act_main.this);
                                DialogoTemporizador(act_main.this);

                            }

                        }

                         */

                        if (ivSleep.getTag().toString().contains("ic_moon_off")) {
                            // Iniciar dialogo de temporizador
                            DialogoTemporizador(act_main.this);

                        } else {
                            // apagar temporizador antes de iniciar uno nuevo
                            ApagarAutoApagado(act_main.this);
                            DialogoTemporizador(act_main.this);
                        }


                        break;


                    case R.id.iv_opcionBucle2:

                        // comprobar si es la primera vez que se da clic a favorito

                        if (ivOpcionBucle.getTag().toString().contains("ic_aleatorio")) {
                            // activar modo bucle
                            OpcionReproductor(act_main.this, REPRODUCTOR_BUCLE);
                            Toast.makeText(act_main.this, R.string.modo_bucle_on, Toast.LENGTH_SHORT).show();
                        } else {
                            // activar modo aleatorio
                            OpcionReproductor(act_main.this, REPRODUCTOR_ALEATORIO);
                            Toast.makeText(act_main.this, R.string.modo_aleatorio, Toast.LENGTH_SHORT).show();
                        }

                        break;


                    case R.id.iv_report:

                        DialogoReport(act_main.this);

                        break;

                    case R.id.iv_support_art:

                        DialogoSupArt(act_main.this);

                        break;


                    case R.id.tv_categoria:
                        Toast.makeText(act_main.this, getString(R.string.repro_lista) + tvCategoria.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.tv_cancion:
                    case R.id.tv_artista:
                    case R.id.iv_lupa:
                        DialogoSupArtista(act_main.this);
                        break;


                    case R.id.iv_style:

                        if (ivStyle.getTag().toString().contains("ic_intercambiar_off")) {
                            // activar modo categoria aleatoria
                            CategoriaAleatoria(act_main.this, true, tinyDB);

                        } else {
                            // desactivar modo categoria aleatoria
                            CategoriaAleatoria(act_main.this, false, tinyDB);
                        }

                        break;


                    case R.id.iv_opReproductor:

                        if (ivOpcionPlayer.getTag().toString().contains("ic_expandir")) {

                            // mostrar opcines de reproductor
                            ivOpcionPlayer.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ivOpcionPlayer.setImageDrawable(AppCompatResources.getDrawable(act_main.this, R.drawable.ic_contraer));
                            ivOpcionPlayer.setTag("ic_contraer");
                            ivOpcionPlayer.startAnimation(Animacion.enter_ios_anim(act_main.this));

                            ivReport.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ivReport.setVisibility(VISIBLE);
                            ivReport.startAnimation(Animacion.enter_ios_anim(act_main.this));

                            ivSupArt.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ivSupArt.setVisibility(VISIBLE);
                            ivSupArt.startAnimation(Animacion.enter_ios_anim(act_main.this));

                            ivLikeDislike.setVisibility(VISIBLE);
                            ivOffline.setVisibility(VISIBLE);
                            ivOpcionBucle.setVisibility(VISIBLE);

                            cdOpPlayer.startAnimation(Animacion.fading_out_real(act_main.this));
                            cdOpPlayer.setVisibility(VISIBLE);
                            cdOpPlayer.startAnimation(Animacion.fade_in_real(act_main.this));

                        } else {
                            // mostrar opcines de reproductor
                            ivOpcionPlayer.startAnimation(Animacion.exit_ios_anim(act_main.this));
                            ivOpcionPlayer.setImageDrawable(AppCompatResources.getDrawable(act_main.this, R.drawable.ic_expandir));
                            ivOpcionPlayer.setTag("ic_expandir");
                            ivOpcionPlayer.startAnimation(Animacion.enter_ios_anim(act_main.this));

                            ivReport.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            ivReport.setVisibility(GONE);
                            ivReport.startAnimation(Animacion.exit_ios_anim(act_main.this));

                            ivSupArt.startAnimation(Animacion.enter_ios_anim(act_main.this));
                            ivSupArt.setVisibility(GONE);
                            ivSupArt.startAnimation(Animacion.exit_ios_anim(act_main.this));

                            cdOpPlayer.startAnimation(Animacion.fade_in_real(act_main.this));
                            cdOpPlayer.setVisibility(GONE);
                            cdOpPlayer.startAnimation(Animacion.fading_out_real(act_main.this));

                            ivLikeDislike.setVisibility(GONE);
                            ivOffline.setVisibility(GONE);
                            ivOpcionBucle.setVisibility(GONE);

                        }

                        break;

                    case R.id.iv_fondoGif:

                        ComprobarSizePlayer(siguienteSize);

                        break;

                    case R.id.iv_offline:

                        DialogoModoOffline(act_main.this);
                        Dialogo5Estrellas(act_main.this);


                        break;


                }

            }
        };


        //region asignar listener a views
        ivMenu.setOnClickListener(listener);
        ivLogo.setOnClickListener(listener);
        mtvVerEstrenos.setOnClickListener(listener);
        ivPlayPause.setOnClickListener(listener);
        ivLikeDislike.setOnClickListener(listener);
        ivSleep.setOnClickListener(listener);
        ivOpcionBucle.setOnClickListener(listener);
        ivReport.setOnClickListener(listener);
        tvCancion.setOnClickListener(listener);
        tvArtista.setOnClickListener(listener);
        ivSupArt.setOnClickListener(listener);
        ivLupa.setOnClickListener(listener);
        tvCategoria.setOnClickListener(listener);
        ivStyle.setOnClickListener(listener);
        ivOpcionPlayer.setOnClickListener(listener);
        cdPlayer.setOnClickListener(listener);
        ivFondoGif.setOnClickListener(listener);
        ivOffline.setOnClickListener(listener);
        //endregion


        ComprobarSizePlayer(tinyDB.getInt(TBsizeReproductor));
        // traer link de imagen
        CargarImagenFondo(this);


    }

    private void ComprobarSizePlayer(int opcion) {

        if (ivOpcionPlayer.getVisibility() == GONE) {
            ivOpcionPlayer.setVisibility(VISIBLE);
        }

        cdPlayer.startAnimation(Animacion.exit_ios_anim(act_main.this));
        if (opcion == 0) {
            cdPlayer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) act_main.this.getResources().getDimension(R.dimen.dimen_player_300dp)));
            tinyDB.putInt(TBsizeReproductor, opcion);
            siguienteSize = 1;
        } else if (opcion == 1) {
            cdPlayer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tinyDB.putInt(TBsizeReproductor, opcion);
            siguienteSize = 2;
        } else if (opcion == 2) {
            cdPlayer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) act_main.this.getResources().getDimension(R.dimen.dimen_player_200dp)));
            tinyDB.putInt(TBsizeReproductor, opcion);
            siguienteSize = 0;
        }
        cdPlayer.startAnimation(Animacion.enter_ios_anim(act_main.this));

    }

    private void ComprobarSizePlayerClic() {
        if (ivOpcionPlayer.getVisibility() == GONE) {
            ivOpcionPlayer.setVisibility(VISIBLE);
        }

        switch (tinyDB.getInt(TBsizeReproductor)) {
            case 0:
                cdPlayer.startAnimation(Animacion.exit_ios_anim(act_main.this));
                cdPlayer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) act_main.this.getResources().getDimension(R.dimen.dimen_player_300dp)));
                cdPlayer.startAnimation(Animacion.enter_ios_anim(act_main.this));
                tinyDB.putInt(TBsizeReproductor, 1);
                break;
            case 2:
                cdPlayer.startAnimation(Animacion.exit_ios_anim(act_main.this));
                cdPlayer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                cdPlayer.startAnimation(Animacion.enter_ios_anim(act_main.this));
                ivOpcionPlayer.setVisibility(GONE);
                tinyDB.putInt(TBsizeReproductor, 0);
                break;
            default:
                cdPlayer.startAnimation(Animacion.exit_ios_anim(act_main.this));
                cdPlayer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) act_main.this.getResources().getDimension(R.dimen.dimen_player_200dp)));
                cdPlayer.startAnimation(Animacion.enter_ios_anim(act_main.this));
                tinyDB.putInt(TBsizeReproductor, 2);
        }

    }

    private class CargarListas extends AsyncTask<Void, Integer, String> {
        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG + " PreExceute", "On pre Exceute......");
            Toast.makeText(act_main.this, R.string.cargando, Toast.LENGTH_SHORT).show();

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
    //endregion
}
