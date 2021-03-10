package com.lamesa.lugu.player.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.github.matteobattilana.weather.PrecipType;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.otros.statics.Animacion;
import com.lamesa.lugu.player.MediaNotificationManager;
import com.lamesa.lugu.player.library.globalEnums.EnumAspectRatio;
import com.lamesa.lugu.player.library.globalEnums.EnumLoop;
import com.lamesa.lugu.player.library.globalEnums.EnumResizeMode;
import com.lamesa.lugu.player.library.globalInterfaces.ExoPlayerCallBack;
import com.lamesa.lugu.player.library.utils.PublicFunctions;
import com.lamesa.lugu.player.library.utils.PublicValues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.lamesa.lugu.activity.act_main.ivPlayPause;
import static com.lamesa.lugu.activity.act_main.mediaNotificationManager;
import static com.lamesa.lugu.activity.act_main.pbCargandoRadio;
import static com.lamesa.lugu.activity.act_main.musicPlayer;
import static com.lamesa.lugu.activity.act_main.spinBuffering;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.activity.act_main.tvArtista;
import static com.lamesa.lugu.activity.act_main.tvCancion;
import static com.lamesa.lugu.activity.act_main.tvCategoria;
import static com.lamesa.lugu.activity.act_main.waveBlack;
import static com.lamesa.lugu.activity.act_main.waveColor;
import static com.lamesa.lugu.activity.act_main.weatherView;
import static com.lamesa.lugu.otros.metodos.CheckIsFavorite;
import static com.lamesa.lugu.otros.metodos.GuardarCancionHistorial;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_ALEATORIO;
import static com.lamesa.lugu.otros.statics.constantes.REPRODUCTOR_BUCLE;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBmodoReproductor;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;


public class AndExoPlayerView3  extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private String currSource = "";
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean isPreparing = false;
    private TypedArray typedArray = null;
    private boolean currPlayWhenReady = false;
    private final boolean showController = true;
    private final EnumResizeMode currResizeMode = EnumResizeMode.FILL;
    private EnumAspectRatio currAspectRatio = EnumAspectRatio.ASPECT_16_9;
    private EnumLoop currLoop = EnumLoop.Finite;

    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    private ComponentListener componentListener;
    private LinearLayout linearLayoutRetry, linearLayoutLoading;
    private AppCompatButton buttonRetry;
    private FrameLayout frameLayoutFullScreenContainer;

    private BandwidthMeter bandwidthMeter;
    private ExtractorsFactory extractorsFactory;
    private TrackSelection.Factory trackSelectionFactory;
    private TrackSelector trackSelector;

    private ExoPlayerCallBack exoPlayerCallBack;
    private boolean isPlaying = false;
    private String source;


    public class ComponentListener implements Player.EventListener {

        String TAG = AndExoPlayerView3.ComponentListener.class.getSimpleName();

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    setLogInfo(mContext,"ComponentListener.Player.STATE_BUFFERING","Player.STATE_BUFFERING",false);
                    PlayOrPause(MediaNotificationManager.STATE_BUFFERING);

                    break;
                case Player.STATE_READY:

                   isPlaying = true;


                    if (isPreparing) {
                        // this is accurate

                        isPreparing = false;
                    }



                    // mostrar notificacion
                    if(mediaNotificationManager !=null) {
                        mediaNotificationManager.startNotify(MediaNotificationManager.STATE_PLAY);
                    }


                    // mostrar y animar texview dde cancion y artista solo si es diferente
                    if(tvCancion!=null && tvArtista!=null && tvCategoria!=null ) {
                        tvCancion.startAnimation(Animacion.anim_slide_bottom_out(mContext));
                        tvCancion.setText(tinyDB.getString(TBnombreCancionSonando));
                        tvCancion.startAnimation(Animacion.anim_slide_bottom_in(mContext));

                        tvArtista.startAnimation(Animacion.anim_slide_bottom_out(mContext));
                        tvArtista.setText(tinyDB.getString(TBartistaCancionSonando));
                        tvArtista.startAnimation(Animacion.anim_slide_bottom_in(mContext));


                        tvCategoria.startAnimation(Animacion.anim_slide_bottom_out(mContext));
                        tvCategoria.setText(tinyDB.getString(TBcategoriaCancionSonando));
                        tvCategoria.startAnimation(Animacion.anim_slide_bottom_in(mContext));
                    }


                    // al reproducirse guardar la cacnion en la lista de historila
                    GuardarCancionHistorial(mContext, tinyDB.getString(TBidCancionSonando));

                    // ocultar icono de buffering
                    PlayOrPause(MediaNotificationManager.STATE_READY);
                    // reproducir cancion y animar icono a pausa

                    // Checkear si la cancion que esta sonando esta en favoritos para marcarlo
                    CheckIsFavorite(mContext, tinyDB.getString(TBidCancionSonando));



                    hideProgress();
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    setLogInfo(mContext,"","",false);
                    if(ivPlayPause!=null) {
                        ivPlayPause.startAnimation(Animacion.exit_ios_anim(mContext));
                        ivPlayPause.setVisibility(VISIBLE);
                        ivPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                        ivPlayPause.startAnimation(Animacion.enter_ios_anim(mContext));
                    }
                    isPlaying = false;

                    //region REPRODUCIR SEGUN LA OPCION DE BUCLE O ALEATORIO
                    // reproducir segun la opcion de bucle o aleatorio

                    if(tinyDB.getString(TBmodoReproductor).equals(REPRODUCTOR_BUCLE)){
                        // reproducir la misma cancion
                        setLogInfo(mContext,"Player.STATE_ENDED","REPRODUCTOR_BUCLE",false);
                        getLinkAndPlay(mContext, tinyDB.getString(TBlinkCancionSonando),1);
                        Toast.makeText(mContext, "REPRODUCTOR_BUCLE", Toast.LENGTH_SHORT).show();


                    } else if (tinyDB.getString(TBmodoReproductor).equals(REPRODUCTOR_ALEATORIO)){
                        // reproducir otra cancion de la misma lista
                        setLogInfo(mContext,"Player.STATE_ENDED","REPRODUCTOR_ALEATORIO",false);
                        Toast.makeText(mContext, "REPRODUCTOR_ALEATORIO", Toast.LENGTH_SHORT).show();
                        List<ModelCancion> listSonando = tinyDB.getListModelCancion(tinyDB.getString(TBcategoriaCancionSonando), ModelCancion.class);

                        if(listSonando!=null && listSonando.size() !=0 ) {

                            Random random = new Random();
                            int numRandom = random.nextInt(listSonando.size());
                            //region guardar datos de la cancion sonando en TinyDB
                            tinyDB.putString(TBidCancionSonando, listSonando.get(numRandom).getId());
                            tinyDB.putString(TBnombreCancionSonando, listSonando.get(numRandom).getCancion());
                            tinyDB.putString(TBartistaCancionSonando, listSonando.get(numRandom).getArtista());
                            tinyDB.putString(TBcategoriaCancionSonando, tinyDB.getString(TBcategoriaCancionSonando));
                            tinyDB.putString(TBlinkCancionSonando, listSonando.get(numRandom).getLinkYT());
                            //endregion
                            getLinkAndPlay(mContext, listSonando.get(numRandom).getLinkYT(), 1);

                        }

                    }

                    //endregion

                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    isPlaying = false;
                    break;

            }

            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
            setLogInfo(mContext,"AndExoPlayerView.onPlayerStateChanged","changed state to " + stateString
                    + " playWhenReady: " + playWhenReady,false);
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            showRetry();

            Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("newlofi error.getMessage() "+error.getMessage());
            setLogInfo(mContext, "AndExoPlayerView.onPlayerError", error.getMessage(),true);

            // volver a reproducir
            musicPlayer.setSource(source);

            if (exoPlayerCallBack != null)
                exoPlayerCallBack.onError();

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }

    }

    public AndExoPlayerView3(Context context) {
        super(context);
        this.mContext = context;
        initializeView(context);
    }

    public AndExoPlayerView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AndExoPlayerView,
                0, 0);
        initializeView(context);
    }

    public AndExoPlayerView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AndExoPlayerView,
                0, 0);
        initializeView(context);
    }

    private void initializeView(Context context) {
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_player_base, this, true);

        playerView = view.findViewById(R.id.simpleExoPlayerView);
        linearLayoutLoading = findViewById(R.id.linearLayoutLoading);
        linearLayoutRetry = findViewById(R.id.linearLayoutRetry);
        buttonRetry = findViewById(R.id.appCompatButton_try_again);
        frameLayoutFullScreenContainer = playerView.findViewById(R.id.container_fullscreen);
        // boton se quito del layout :
        // imageViewEnterFullScreen = playerView.findViewById(R.id.exo_enter_fullscreen);
        // boton se quitó del layout :
        // imageViewExitFullScreen = playerView.findViewById(R.id.exo_exit_fullscreen);

        componentListener = new ComponentListener();

        linearLayoutRetry.setOnClickListener(this);
        // boton se quito del layout :
        // imageViewEnterFullScreen.setOnClickListener(this);
        // boton se quitó del layout :
        // imageViewExitFullScreen.setOnClickListener(this);
        buttonRetry.setOnClickListener(this);

        if (typedArray != null) {

            if (typedArray.hasValue(R.styleable.AndExoPlayerView_andexo_resize_mode)) {
                int resizeMode = typedArray.getInteger(R.styleable.AndExoPlayerView_andexo_resize_mode, EnumResizeMode.FILL.getValue());
                setResizeMode(EnumResizeMode.get(resizeMode));
            }

            if (typedArray.hasValue(R.styleable.AndExoPlayerView_andexo_aspect_ratio)) {
                int aspectRatio = typedArray.getInteger(R.styleable.AndExoPlayerView_andexo_aspect_ratio, EnumAspectRatio.ASPECT_16_9.getValue());
                setAspectRatio(EnumAspectRatio.get(aspectRatio));
            }

            if (typedArray.hasValue(R.styleable.AndExoPlayerView_andexo_full_screen)) {
                setShowFullScreen(typedArray.getBoolean(R.styleable.AndExoPlayerView_andexo_full_screen, false));
            }

            if (typedArray.hasValue(R.styleable.AndExoPlayerView_andexo_play_when_ready)) {
                setPlayWhenReady(typedArray.getBoolean(R.styleable.AndExoPlayerView_andexo_play_when_ready, false));
            }

            if (typedArray.hasValue(R.styleable.AndExoPlayerView_andexo_show_controller)) {
                setShowController(typedArray.getBoolean(R.styleable.AndExoPlayerView_andexo_show_controller, true));
            }

            if (typedArray.hasValue(R.styleable.AndExoPlayerView_andexo_loop)) {
                EnumLoop enumLoop = EnumLoop.get(typedArray.getInteger(R.styleable.AndExoPlayerView_andexo_loop, EnumLoop.Finite.getValue()));
                setLoopMode(enumLoop);
            }

            typedArray.recycle();
        }

        initializePlayer();
    }

    public SimpleExoPlayer getPlayer() {
        return simpleExoPlayer;
    }

    private void initializePlayer() {

        if (simpleExoPlayer == null) {

            bandwidthMeter = new DefaultBandwidthMeter();
            extractorsFactory = new DefaultExtractorsFactory();
            trackSelectionFactory = new AdaptiveTrackSelection.Factory();
            trackSelector = new DefaultTrackSelector();

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

            playerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(currPlayWhenReady);
            simpleExoPlayer.seekTo(currentWindow, playbackPosition);
            simpleExoPlayer.addListener(componentListener);
        }
    }

    public void setSource(String source) {
        this.source = source;
        MediaSource mediaSource = buildMediaSource(source, null);
        if (mediaSource != null) {
            if (simpleExoPlayer != null) {
                showProgress();

                switch (currLoop) {
                    case INFINITE: {
                        LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
                        simpleExoPlayer.prepare(loopingSource, true, false);
                        break;
                    }

                    case Finite:
                    default: {
                        simpleExoPlayer.prepare(mediaSource, true, false);
                        break;
                    }
                }
            }
        }
    }



    public void setSource(String source, HashMap<String, String> extraHeaders) {
        MediaSource mediaSource = buildMediaSource(source, extraHeaders);
        if (mediaSource != null) {
            if (simpleExoPlayer != null) {
                showProgress();

                switch (currLoop) {
                    case INFINITE: {
                        LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
                        simpleExoPlayer.prepare(loopingSource, true, false);
                        break;
                    }

                    case Finite:
                    default: {
                        simpleExoPlayer.prepare(mediaSource, true, false);
                        break;
                    }
                }
            }
        }
    }

    private MediaSource buildMediaSource(String source, HashMap<String, String> extraHeaders) {

        if (source == null) {
            Toast.makeText(mContext, "Input Is Invalid.", Toast.LENGTH_SHORT).show();
            return null;
        }

        this.currSource = source;

        boolean validUrl = URLUtil.isValidUrl(source);

        Uri uri = Uri.parse(source);
        if (uri == null || uri.getLastPathSegment() == null) {
            Toast.makeText(mContext, "Uri Converter Failed, Input Is Invalid.", Toast.LENGTH_SHORT).show();
            return null;
        }

        // para reproducir un link mp3
        DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(PublicValues.KEY_USER_AGENT);
        if (extraHeaders != null) {
            for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
        }

        return new ProgressiveMediaSource.Factory(sourceFactory)
                .createMediaSource(uri);

        /* los siguientes metodos valida el tipo de link

        if (validUrl && uri.getLastPathSegment().contains(PublicValues.KEY_MP4)) {

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(PublicValues.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }

            return new ProgressiveMediaSource.Factory(sourceFactory)
                    .createMediaSource(uri);

        } else if (!validUrl && uri.getLastPathSegment().contains(PublicValues.KEY_MP4)) {
            return new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(context, PublicValues.KEY_USER_AGENT))
                    .createMediaSource(uri);

        } else if (uri.getLastPathSegment().contains(PublicValues.KEY_HLS)) {
            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(PublicValues.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }

            return new HlsMediaSource.Factory(sourceFactory)
                    .createMediaSource(uri);

        } else if (uri.getLastPathSegment().contains(PublicValues.KEY_MP3)) {

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory(PublicValues.KEY_USER_AGENT);
            if (extraHeaders != null) {
                for (Map.Entry<String, String> entry : extraHeaders.entrySet())
                    sourceFactory.getDefaultRequestProperties().set(entry.getKey(), entry.getValue());
            }

            return new ProgressiveMediaSource.Factory(sourceFactory)
                    .createMediaSource(uri);

        } else {
            DefaultDashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(new DefaultHttpDataSourceFactory("ua", new DefaultBandwidthMeter()));
            DefaultHttpDataSourceFactory manifestDataSourceFactory = new DefaultHttpDataSourceFactory(PublicValues.KEY_USER_AGENT);
            return new DashMediaSource.Factory(dashChunkSourceFactory, manifestDataSourceFactory)
                    .createMediaSource(uri);

        }

        */
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            currPlayWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.removeListener(componentListener);
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.currPlayWhenReady = playWhenReady;
        if (simpleExoPlayer != null)
            simpleExoPlayer.setPlayWhenReady(playWhenReady);
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public void stopPlayer() {
        if (simpleExoPlayer != null)
            simpleExoPlayer.stop();
    }

    public void pausePlayer() {
        if (simpleExoPlayer != null)
            simpleExoPlayer.setPlayWhenReady(false);
    }

    public void setShowController(boolean showController) {
        if (playerView == null)
            return;

        if (showController) {
            playerView.showController();
            playerView.setUseController(true);
        } else {
            playerView.hideController();
            playerView.setUseController(false);
        }
    }

    public void setResizeMode(EnumResizeMode resizeMode) {
        switch (resizeMode) {

            case FIT:
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                break;

            case FILL:
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                break;

            case ZOOM:
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                break;

            default:
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }
    }

    public void setShowFullScreen(boolean showFullScreen) {
        if (showFullScreen)
            frameLayoutFullScreenContainer.setVisibility(VISIBLE);
        else
            frameLayoutFullScreenContainer.setVisibility(GONE);
    }

    public void setAspectRatio(EnumAspectRatio aspectRatio) {
        this.currAspectRatio = aspectRatio;
        int value = PublicFunctions.getScreenWidth();

        switch (aspectRatio) {

            case ASPECT_1_1:
                playerView.setLayoutParams(new FrameLayout.LayoutParams(value, value));
                break;

            case ASPECT_4_3:
                playerView.setLayoutParams(new FrameLayout.LayoutParams(value, (3 * value) / 4));
                break;

            case ASPECT_16_9:
                playerView.setLayoutParams(new FrameLayout.LayoutParams(value, (9 * value) / 16));
                break;

            case ASPECT_MATCH:
                playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                break;

            case ASPECT_MP3:
                playerView.setControllerShowTimeoutMs(0);
                playerView.setControllerHideOnTouch(false);
                int mp3Height = getContext().getResources().getDimensionPixelSize(R.dimen.player_controller_base_height);
                playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mp3Height));
                break;

            case UNDEFINE:
            default:
                int baseHeight = (int) getResources().getDimension(R.dimen.player_base_height);
                playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, baseHeight));
                break;
        }
    }

    private void setLoopMode(EnumLoop loopMode) {
        this.currLoop = loopMode;
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releasePlayer();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // First Hide other objects (listview or recyclerview), better hide them using Gone.
            hideSystemUi();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // unhide your objects here.
            showSystemUi();
            setAspectRatio(currAspectRatio);
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        if (playerView == null)
            return;

        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @SuppressLint("InlinedApi")
    private void showSystemUi() {
        if (playerView == null)
            return;

        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onClick(View v) {

        int targetViewId = v.getId();
        if (targetViewId == R.id.appCompatButton_try_again) {
            hideRetry();
            setSource(currSource);
            /* se quitó exo_enter_fullscreen  del layout
        } else if (targetViewId == R.id.exo_enter_fullscreen) {
            enterFullScreen();
        } else if (targetViewId == R.id.exo_exit_fullscreen) {
            exitFullScreen();

             */
        }
    }

    public void setExoPlayerCallBack(ExoPlayerCallBack exoPlayerCallBack) {
        this.exoPlayerCallBack = exoPlayerCallBack;
    }

    private void enterFullScreen() {
        // boton se quitó del layout :
        // imageViewExitFullScreen.setVisibility(VISIBLE);
        // boton se quito del layout :
        // imageViewEnterFullScreen.setVisibility(GONE);

        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void exitFullScreen() {
        // boton se quitó del layout :
        // imageViewExitFullScreen.setVisibility(GONE);
        // boton se quito del layout :
        // imageViewEnterFullScreen.setVisibility(VISIBLE);

        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void showProgress() {
        hideAll();
        /*
        if (linearLayoutLoading != null) {
            linearLayoutLoading.startAnimation(Animacion.anim_alpha_out(mContext));
            linearLayoutLoading.setVisibility(VISIBLE);
            linearLayoutLoading.startAnimation(Animacion.anim_alpha_in(mContext));
        }

         */
        PlayOrPause(MediaNotificationManager.STATE_BUFFERING);
        if(pbCargandoRadio!=null){
            pbCargandoRadio.startAnimation(Animacion.anim_alpha_out(mContext));
            pbCargandoRadio.setVisibility(VISIBLE);
            pbCargandoRadio.startAnimation(Animacion.anim_alpha_in(mContext));
        }
    }

    private void hideProgress() {
        if (linearLayoutLoading != null){
            linearLayoutLoading.startAnimation(Animacion.anim_alpha_in(mContext));
            linearLayoutLoading.setVisibility(GONE);
            linearLayoutLoading.startAnimation(Animacion.anim_alpha_out(mContext));
       }

        if(pbCargandoRadio!=null){
            pbCargandoRadio.startAnimation(Animacion.anim_alpha_in(mContext));
            pbCargandoRadio.setVisibility(GONE);
            pbCargandoRadio.startAnimation(Animacion.anim_alpha_out(mContext));
        }

    }

    private void showRetry() {
        hideAll();
        if (linearLayoutRetry != null)
            linearLayoutRetry.setVisibility(VISIBLE);
    }

    private void hideRetry() {
        if (linearLayoutRetry != null)
            linearLayoutRetry.setVisibility(GONE);
    }

    private void hideAll() {
        if (linearLayoutRetry != null)
            linearLayoutRetry.setVisibility(GONE);
        if (linearLayoutLoading != null)
            linearLayoutLoading.setVisibility(GONE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void PlayOrPause(String state)   {

        switch (state){


            case MediaNotificationManager.STATE_PLAY:

                // reproducir
                if(musicPlayer !=null) {
                    musicPlayer.setPlayWhenReady(true);
                }
                if(waveColor!=null && waveBlack!=null){
                    waveBlack.play();
                    waveColor.play();
                }

                if(ivPlayPause!=null) {
                    ivPlayPause.startAnimation(Animacion.exit_ios_anim(mContext));
                    ivPlayPause.setVisibility(VISIBLE);
                    ivPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pausa));
                    ivPlayPause.startAnimation(Animacion.enter_ios_anim(mContext));
                }

                if(mediaNotificationManager!=null) {
                    mediaNotificationManager.startNotify(MediaNotificationManager.STATE_PLAY);
                }

                if(weatherView!=null){
                    Random random = new Random();
                    int numRandom = random.nextInt(2);
                    switch (numRandom){
                        case 0:
                            weatherView.setWeatherData(PrecipType.RAIN);
                            break;
                        case 1:
                            weatherView.setWeatherData(PrecipType.SNOW);
                            break;

                    }

                }

                break;

            case MediaNotificationManager.STATE_PAUSE:

                // pausar
                if(musicPlayer !=null) {
                    musicPlayer.pausePlayer();
                }

                if(waveColor!=null && waveBlack!=null){
                    waveBlack.pause();
                    waveColor.pause();
                }


                    if(ivPlayPause!=null) {
                        ivPlayPause.startAnimation(Animacion.exit_ios_anim(mContext));
                        ivPlayPause.setVisibility(VISIBLE);
                        ivPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                        ivPlayPause.startAnimation(Animacion.enter_ios_anim(mContext));
                    }

                if(spinBuffering!=null) {
                    spinBuffering.startAnimation(Animacion.enter_ios_anim(mContext));
                    spinBuffering.setVisibility(GONE);
                    spinBuffering.startAnimation(Animacion.exit_ios_anim(mContext));
                }


                if(mediaNotificationManager!=null) {
                    mediaNotificationManager.startNotify(MediaNotificationManager.STATE_PAUSE);
                }


                if(weatherView!=null){
                    weatherView.setWeatherData(PrecipType.CLEAR);
                }


                break;

            case MediaNotificationManager.STATE_BUFFERING:

                if(spinBuffering!=null) {
                    if(ivPlayPause!=null) {
                        ivPlayPause.startAnimation(Animacion.enter_ios_anim(mContext));
                        ivPlayPause.setVisibility(View.INVISIBLE);
                        ivPlayPause.startAnimation(Animacion.exit_ios_anim(mContext));
                    }

                    spinBuffering.startAnimation(Animacion.exit_ios_anim(mContext));
                    spinBuffering.setVisibility(VISIBLE);
                    spinBuffering.startAnimation(Animacion.enter_ios_anim(mContext));
                }

                if(mediaNotificationManager!=null) {
                    mediaNotificationManager.startNotify(MediaNotificationManager.STATE_BUFFERING);
                }

                break;


            case MediaNotificationManager.STATE_READY:


                // ocultar mstate buffering

                if(spinBuffering!=null) {
                    spinBuffering.startAnimation(Animacion.enter_ios_anim(mContext));
                    spinBuffering.setVisibility(GONE);
                    spinBuffering.startAnimation(Animacion.exit_ios_anim(mContext));
                }


                if(ivPlayPause!=null) {
                    ivPlayPause.startAnimation(Animacion.exit_ios_anim(mContext));
                    ivPlayPause.setVisibility(VISIBLE);
                    ivPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_pausa));
                    ivPlayPause.startAnimation(Animacion.enter_ios_anim(mContext));
                }



                break;


            case MediaNotificationManager.STATE_STOP:


                if(mediaNotificationManager!=null) {
                    mediaNotificationManager.cancelNotify();
                }

                if(musicPlayer !=null){
                    musicPlayer.pausePlayer();
                }

                if(ivPlayPause!=null) {
                    ivPlayPause.startAnimation(Animacion.exit_ios_anim(mContext));
                    ivPlayPause.setVisibility(VISIBLE);
                    ivPlayPause.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_play));
                    ivPlayPause.startAnimation(Animacion.enter_ios_anim(mContext));
                }

                if(waveColor!=null && waveBlack!=null){
                    waveBlack.pause();
                    waveColor.pause();
                }


                break;





        }


    }


}
