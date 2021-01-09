package com.lamesa.lugu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amplitude.api.Amplitude;
import com.lamesa.lugu.R;
import com.lamesa.lugu.activity.act_main;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.otros.TinyDB;
import com.lamesa.lugu.otros.statics.Animacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.activity.act_main.ContenedorVacio;
import static com.lamesa.lugu.activity.act_main.mAdapterFavoritos;
import static com.lamesa.lugu.activity.act_main.mrvFavoritos;
import static com.lamesa.lugu.activity.act_main.mrvHistorial;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.activity.act_main.tvVacio;
import static com.lamesa.lugu.otros.metodos.DialogoEliminarLista;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.mob.inter.CargarInterAleatorio;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnumeroCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.mixFalloEpisodio;
import static com.lamesa.lugu.otros.statics.constantes.mixPlaySong;

/**
 * Created by Aws on 28/01/2018.
 */

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.MyViewHolder> {

    private final Context mContext;
    private final List<ModelCancion> mListFavoritos;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;



    public AdapterFavoritos(Context mContext, List<ModelCancion> mlistfavoritos) {

        this.mContext = mContext;
        this.mListFavoritos = mlistfavoritos;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /*
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        //     MobileAds.initialize(mContext, "ca-app-pub-4887224789758978~2509724130");
        Amplitude.getInstance().initialize(mContext, "d261f53264579f9554bd244eef7cc2e1").enableForegroundTracking((Application) mContext.getApplicationContext());

         */

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_favorito, parent, false);


       // tinyDB = new TinyDB(mContext);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setAnimation(holder.cdCancionFavoritos, position);
        holder.tvCancion.setText(mListFavoritos.get(position).getCancion());
        holder.tvArtista.setText(mListFavoritos.get(position).getArtista());

        holder.cdCancionFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CargarInterAleatorio(mContext, 5);
                getLinkAndPlay(mContext, mListFavoritos.get(position).getLinkYT(),1);



                //region guardar datos de la cancion sonando en TinyDB
                tinyDB.putString(TBidCancionSonando, mListFavoritos.get(position).getId());
                tinyDB.putString(TBnombreCancionSonando, mListFavoritos.get(position).getCancion());
                tinyDB.putString(TBartistaCancionSonando, mListFavoritos.get(position).getArtista());
                tinyDB.putString(TBcategoriaCancionSonando, TBlistFavoritos);
                tinyDB.putString(TBlinkCancionSonando, mListFavoritos.get(position).getLinkYT());
                tinyDB.putInt(TBnumeroCancionSonando, position);
                //endregion



            }
        });
        holder.cdCancionFavoritos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogoEliminarLista(mContext, tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class),TBlistFavoritos);
                return false;
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.enter_ios_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mListFavoritos.size();
    }

    public void setUpdateFavoritos(List<ModelCancion> mListFavoritos){

        this.mListFavoritos.removeAll(this.mListFavoritos);
        Collections.reverse(mListFavoritos);
        this.mListFavoritos.addAll(mListFavoritos);
        if(mAdapterFavoritos!=null) {
            mAdapterFavoritos.notifyDataSetChanged();
        }



        if (mrvHistorial != null) {
            mrvHistorial.setVisibility(GONE);
        }


        if (tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class).isEmpty() || tinyDB.getListModelCancion(TBlistFavoritos, ModelCancion.class)==null) {
            tvVacio.setText(R.string.sin_favoritos);
            ContenedorVacio.startAnimation(Animacion.exit_ios_anim(mContext));
            ContenedorVacio.setVisibility(VISIBLE);
            ContenedorVacio.startAnimation(Animacion.enter_ios_anim(mContext));
        } else {
            ContenedorVacio.setVisibility(GONE);
            if (mrvFavoritos != null) {
                mrvFavoritos.startAnimation(Animacion.exit_ios_anim(mContext));
                mrvFavoritos.setVisibility(VISIBLE);
                mrvFavoritos.startAnimation(Animacion.enter_ios_anim(mContext));
            }

        }


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView cdCancionFavoritos;
        private final TextView tvCancion;
        private final TextView tvArtista;


        public MyViewHolder(View itemView) {
            super(itemView);
            cdCancionFavoritos = itemView.findViewById(R.id.cd_cancionHistorial);
            tvCancion = itemView.findViewById(R.id.tv_cancion);
            tvArtista = itemView.findViewById(R.id.tv_artista);

        }
    }


}

