package com.lamesa.lugu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.otros.TinyDB;

import java.util.List;

import static com.lamesa.lugu.activity.act_main.mAdapterFavoritos;
import static com.lamesa.lugu.otros.metodos.DialogoEliminarLista;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnumeroCancionSonando;

/**
 * Created by Aws on 28/01/2018.
 */

public class AdapterFavoritos extends RecyclerView.Adapter<AdapterFavoritos.MyViewHolder> {

    private Context mContext;
    private List<ModelCancion> mListFavoritos;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;
    private TinyDB tinyDB;


    public AdapterFavoritos(Context mContext, List<ModelCancion> mListFavoritos) {

        this.mContext = mContext;
        this.mListFavoritos = mListFavoritos;

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


        tinyDB = new TinyDB(mContext);


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
        this.mListFavoritos.addAll(mListFavoritos);
        if(mAdapterFavoritos!=null) {
            mAdapterFavoritos.notifyDataSetChanged();
        }


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cdCancionFavoritos;
        private TextView tvCancion;
        private TextView tvArtista;


        public MyViewHolder(View itemView) {
            super(itemView);
            cdCancionFavoritos = itemView.findViewById(R.id.cd_cancionHistorial);
            tvCancion = itemView.findViewById(R.id.tv_cancion);
            tvArtista = itemView.findViewById(R.id.tv_artista);

        }
    }


}

