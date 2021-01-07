package com.lamesa.lugu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.github.matteobattilana.weather.WeatherView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;
import com.lamesa.lugu.otros.Firebase;
import com.lamesa.lugu.otros.TinyDB;
import com.lamesa.lugu.player.MediaNotificationManager;
import com.lamesa.lugu.player.library.MusicPlayer;
import com.narayanacharya.waveview.WaveView;

import net.khirr.android.privacypolicy.PrivacyPolicyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.activity.splash.DialogoPoliticas;
import static com.lamesa.lugu.otros.Firebase.getListaImagenes;
import static com.lamesa.lugu.otros.metodos.AboutUS;
import static com.lamesa.lugu.otros.metodos.AbrirPagina;
import static com.lamesa.lugu.otros.metodos.ApagarAutoApagado;
import static com.lamesa.lugu.otros.metodos.CheckIsFavorite;
import static com.lamesa.lugu.otros.metodos.CompartirApp;
import static com.lamesa.lugu.otros.metodos.DialogoSalir;
import static com.lamesa.lugu.otros.metodos.DialogoSugerencia;
import static com.lamesa.lugu.otros.metodos.DialogoTemporizador;
import static com.lamesa.lugu.otros.metodos.GuardarCancionFavoritos;
import static com.lamesa.lugu.otros.metodos.OpcionReproductor;
import static com.lamesa.lugu.otros.metodos.SolicitarFilm;
import static com.lamesa.lugu.otros.metodos.UpdateAdapterFavoritos;
import static com.lamesa.lugu.otros.metodos.UpdateAdapterHistorial;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.metodos.initFirebase;
import static com.lamesa.lugu.otros.mob.inter.loadInterstitial;
import static com.lamesa.lugu.otros.mob.inter.showInterstitial;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_ALEATORIO;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_BUCLE;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBimagenFondo;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBlistHistorial;
import static com.lamesa.lugu.otros.statics.constantes.TBlistImagenes;
import static com.lamesa.lugu.otros.statics.constantes.TBmodoReproductor;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBreproduciendoRadio;
import static com.lamesa.lugu.otros.statics.constantes.mixAdClic;

public class act_main extends AppCompatActivity {

    public static List<ModelCategoria> mlistCategoria;
    public static AdapterCategoria mAdapterCategoria;
    public static RecyclerView mrvCategoria;
    public static LinearLayout contenidoCargando;
    public static NestedScrollView contenidoHome;
    public static LinearLayout contenidoSearch;
    public static TinyDB tinyDB;
    public static ArrayList<String> mlistUrls;
    public static SpinKitView animacionCargandoTop;
    public static EditText etBuscar;
    private TextView mtvVerPeliculas;
    private TextView mtvVerSeries;
    private ImageView ivMenu;
    private ImageView ivLogo;
    private TextView mtvVerNetflix;
    private TextView mtvVerEstrenos;
    public static ImageView ivFondoGif;
    public static MusicPlayer musicPlayer;
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
    public static  LinearLayout ContenedorVacio;
    public static  TextView tvVacio;
    public static ImageView ivSleep;
    public static TextView tvSleep;
    public static ImageView ivOpcionBucle;
    public static WeatherView weatherView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_main);


        // INICAR MEDIANOTIFICACTION
        mediaNotificationManager = new MediaNotificationManager(this);
        // reproductor exoplayer
        musicPlayer = findViewById(R.id.musicPlayer);

        // SolicitarPermisos(this);

        tinyDB = new TinyDB(this);
        // imagen por defecto de fondo
        tinyDB.putString(TBimagenFondo,"https://i.pinimg.com/originals/76/09/46/7609468e97e15d1da8d14d534be7366c.gif");

        initFirebase(act_main.this, tinyDB);

        VistasHome();
        etBuscar = findViewById(R.id.etBuscar);
        CargarRecyclerHome();

        // Traer todas las listas desde Firebase
        new CargarListas().execute();

        CheckIsFavorite(act_main.this, tinyDB.getString(TBidCancionSonando));

        // cargar adinter para ser mostrada
        loadInterstitial(act_main.this);
        // cargar banner
        CargarBanner();

        // Initialice your dialog, first param is your terms of service url, and second param is your privacy policy url

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
        //region guardar ivOpcionBucle segun el icono
        if (ivOpcionBucle.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_bucle).getConstantState()) {
            // guardar modo de reproductor REPRODUCTOR_BUCLE
           tinyDB.putString(TBmodoReproductor,REPRODUCTOR_BUCLE);

        } else if (ivOpcionBucle.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_aleatorio).getConstantState()) {
            // guardar modo de reproductor REPRODUCTOR_ALEATORIO
            tinyDB.putString(TBmodoReproductor,REPRODUCTOR_ALEATORIO);
        }
        //endregion

        OpcionReproductor(act_main.this, tinyDB.getString(TBmodoReproductor));

        weatherView = findViewById(R.id.weather_view);


        bottomNavigationHis_Fav = findViewById(R.id.bottomNavigationHis_Fav);
        bottomNavigationHis_Fav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.navigation_historial:
                        menuItem.setChecked(true);

                        mAdapterHistorial.setUpdateHistorial(tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class));

                        break;

                    case R.id.navigation_favoritos:
                        menuItem.setChecked(true);

                        mAdapterFavoritos.setUpdateFavoritos(tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class));

                        break;


                }
                return false;
            }
        });

        bottomNavigationHis_Fav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_historial:

                        UpdateAdapterHistorial(act_main.this);

                        break;

                    case R.id.navigation_favoritos:

                        UpdateAdapterFavoritos(act_main.this);

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

                        AbrirPagina(act_main.this);

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

                        opcionMenu.add(getString(R.string.enviar_sugerencia));
                        opcionMenu.add(getString(R.string.compartir_app));
                        opcionMenu.add("About "+act_main.this.getResources().getString(R.string.app_name));
                        opcionMenu.add(getString(R.string.aviso_legal));


                        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
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

                                        DialogoSugerencia(act_main.this);

                                        break;

                                    case 1:

                                        CompartirApp(act_main.this);

                                        break;


                                    case 2:

                                        AboutUS(act_main.this, tinyDB, false);

                                        break;

                                    case 3:

                                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                                        DialogSettings.theme = DialogSettings.THEME.DARK;
                                        DialogSettings.backgroundColor = getResources().getColor(R.color.black);

                                        MessageDialog.show(act_main.this, "DMCA", "" +
                                                "" +
                                                "No multimedia file is being hosted by us on this app.\n" +
                                                "\n" +
                                                "We are not associated with the list of contents found on remote servers. We have no connection or association with such content.\n" +
                                                "The mp3, jpg, png files that are available are not hosted on ("+act_main.this.getResources().getString(R.string.app_name)+") app and are hosted on other servers (therefore, not our host service).\n" +
                                                "This app ("+act_main.this.getResources().getString(R.string.app_name)+") functions as a lofi music search engine and does not store or host any files or other copyrighted material. We follow copyright laws, but if you find any search results that you feel are illegal, you are asked to complete the form and send an email to lugulofimusic@gmail.com\n" +
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

                                }

                            }
                        }).setCancelButtonText(act_main.this.getResources().getString(R.string.cerrar)).setMenuTextInfo(new TextInfo().setGravity(Gravity.CENTER).setFontColor(Color.GRAY)).setTitle("MENU");


                        break;

                    case R.id.iv_playPause:


                        // cambiar icono entre play y pause
                        if (ivPlayPause.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_play).getConstantState()) {
                            // reproducir
                            // comprobar si es la primera vez que se da clic a play
                            if (!tinyDB.getString(TBidCancionSonando).isEmpty()) {
                                musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PLAY);
                                // reproducir la ultima cancion reproducida unicamente si se incia la app sin clickear alguna lista
                                if (!musicPlayer.isPlaying()) {
                                    // comprobar que si haya una cancion guardada
                                    getLinkAndPlay(act_main.this, tinyDB.getString(TBlinkCancionSonando), 1);
                                    tvCancion.setText(tinyDB.getString(TBnombreCancionSonando));
                                    tvArtista.setText(tinyDB.getString(TBartistaCancionSonando));
                                }
                            } else {
                                // primera ves que se da clic a play
                                Toast.makeText(act_main.this, R.string.elija_categoria, Toast.LENGTH_SHORT).show();
                            }

                        } else if (ivPlayPause.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_pausa).getConstantState()) {
                            // pausar
                            musicPlayer.PlayOrPause(MediaNotificationManager.STATE_PAUSE);
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
                            OpcionReproductor(act_main.this, REPRODUCTOR_ALEATORIO);
                            Toast.makeText(act_main.this, R.string.modo_aleatorio, Toast.LENGTH_SHORT).show();

                        } else if (ivOpcionBucle.getDrawable().getConstantState() == act_main.this.getResources().getDrawable(R.drawable.ic_aleatorio).getConstantState()) {
                            // activar modo bucle
                            OpcionReproductor(act_main.this, REPRODUCTOR_BUCLE);
                            Toast.makeText(act_main.this, R.string.modo_bucle_on, Toast.LENGTH_SHORT).show();

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




        // cargar gif de fondo
        ivFondoGif = findViewById(R.id.iv_fondoGif);
        // traer link de imagen
        CargarImagenFondo();
        // cargar imagen en fondo
        Glide.with(this)
                    .load(tinyDB.getString(TBimagenFondo))
                    //.error(R.drawable.ic_alert)
                    .transition(DrawableTransitionOptions.withCrossFade(200))
                    .into(ivFondoGif);
            // extraer colores de imagenes
            Glide.with(this)
                    .asBitmap()
                    .load(tinyDB.getString(TBimagenFondo))
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

    private void CargarImagenFondo() {


        // cargar una imagen random
        if(!tinyDB.getListString(TBlistImagenes).isEmpty() && tinyDB.getListString(TBlistImagenes)!=null) {
            Random random = new Random();
            // obtener link alaeatorio
            int numRandom = random.nextInt(tinyDB.getListString(TBlistImagenes).size());
            // guardar link de imagen en tiny
            tinyDB.putString(TBimagenFondo,tinyDB.getListString(TBlistImagenes).get(numRandom));
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

    public static void getListas(Context mContext) {
        Firebase.getListaCanciones(mContext, mlistCategoria, mlistCancion, tinyDB);
        Firebase.getListaCategorias(mContext, mlistCategoria, mAdapterCategoria);
        Firebase.getListaPorCategoria(mContext, mlistCategoria, mlistCancion, tinyDB);
        getListaImagenes(tinyDB);
    }


}
