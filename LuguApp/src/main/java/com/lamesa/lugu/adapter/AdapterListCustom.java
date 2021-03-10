package com.lamesa.lugu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amplitude.api.Amplitude;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;
import com.lamesa.lugu.model.ModelListCustom;
import com.lamesa.lugu.otros.statics.Animacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.activity.act_main.getListas;
import static com.lamesa.lugu.activity.act_main.mlistCategoria;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.metodos.setLogInfo;
import static com.lamesa.lugu.otros.mob.inter.CargarInterAleatorio;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnumeroCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.mixCategoriaClic;


/**
 * Created by Aws on 28/01/2018.
 */

public class AdapterListCustom extends RecyclerView.Adapter<AdapterListCustom.MyViewHolder> {

    private final Context mContext;
    private final List<ModelListCustom> mListCustom;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;


    public AdapterListCustom(Context mContext, List<ModelListCustom> mListCustom) {

        this.mContext = mContext;
        this.mListCustom = mListCustom;

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
        view = mInflater.inflate(R.layout.item_categoria, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        setAnimation(holder.rlCategoria, position);

        holder.tvCategoria.setText(mListCustom.get(position).getNombre());

        Glide.with(mContext)
                .load(mListCustom.get(position).getImagen())
                //.error(R.drawable.error)
                //.placeholder(R.drawable.placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(holder.ivFondo);


        holder.tvCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.rlCategoria.startAnimation(Animacion.fading_out_real(mContext));
                holder.rlCategoria.setVisibility(View.VISIBLE);
                holder.rlCategoria.startAnimation(Animacion.fade_in_real(mContext));

                // metodo para cargar cancion de la categoria seleccionada


               List<ModelCancion> tinyListCustomxCustom = tinyDB.getListModelCancion(mListCustom.get(position).getNombre().toLowerCase().trim(), ModelListCustom.class);

                if(!tinyListCustomxCustom.isEmpty()){
                    setLogInfo(mContext, "Clic Categoria","tinyListCustomxCustom list custom "+mlistCategoria.get(position).getNombre()+" está vacia", false);
                }

                for (int i = 0; i < tinyListCustomxCustom.size() ; i++) {
                    setLogInfo(mContext, "Clic Categoria","tinyListCustomxCustom == "+tinyListCustomxCustom.get(i).getCancion(), false);
                }


                if(tinyListCustomxCustom!=null && tinyListCustomxCustom.size() !=0 ) {

                    // numero aletario de cancion
                    Random random = new Random();
                    int numCancionSonar = random.nextInt(tinyListCustomxCustom.size());
                    CargarInterAleatorio(mContext, 5);
                    getLinkAndPlay(mContext, tinyListCustomxCustom.get(numCancionSonar).getLinkYT(), 1);

                    // guardar datos de la cancion sonando en TinyDB
                    tinyDB.putInt(TBnumeroCancionSonando, numCancionSonar);
                    tinyDB.putString(TBidCancionSonando, tinyListCustomxCustom.get(numCancionSonar).getId());
                    tinyDB.putString(TBnombreCancionSonando, tinyListCustomxCustom.get(numCancionSonar).getCancion());
                    tinyDB.putString(TBartistaCancionSonando, tinyListCustomxCustom.get(numCancionSonar).getArtista());
                    tinyDB.putString(TBcategoriaCancionSonando, mListCustom.get(position).getNombre().toLowerCase().trim());
                    tinyDB.putString(TBlinkCancionSonando, tinyListCustomxCustom.get(numCancionSonar).getLinkYT());

                } else {
                    getListas(mContext);
                }


                //region MIX mixCompartirApp para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("Id", mListCustom.get(position).getId());
                    props.put("Nombre", mListCustom.get(position).getNombre());
                    Bundle params = new Bundle();
                    params.putString("Id",  mListCustom.get(position).getId());
                    params.putString("Nombre", mListCustom.get(position).getNombre());

                    mFirebaseAnalytics.logEvent(mixCategoriaClic, params);
                    mixpanel.track(mixCategoriaClic, props);
                    Amplitude.getInstance().logEvent(mixCategoriaClic, props);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //endregion
            }
        });

    }


    /*

    private void EliminarFavorito(int idfavorito) {

        //login aunth
        // get el usuario
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser u = mAuth.getCurrentUser();


        if (u != null) {


            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference clientesRef = ref.child("lofiradio").child("usuario").child(u.getUid()).child("favoritos").child("canciones").child(String.valueOf(idfavorito));


            ((DatabaseReference) clientesRef).child("idfavorito").removeValue();
            ((DatabaseReference) clientesRef).child("LinkYT").removeValue();
            ((DatabaseReference) clientesRef).child("NombreCancionSonando").removeValue();


            Toast.makeText(mContext, mContext.getString(R.string.cancionelimfavoritos), Toast.LENGTH_SHORT).show();


        }
    }


     */


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_float_window_enter);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mListCustom.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView rlCategoria;
        private final ImageView ivFondo;
        private final TextView tvCategoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivFondo = itemView.findViewById(R.id.iv_imagenFondo);
            tvCategoria = itemView.findViewById(R.id.tv_categoria);
            rlCategoria = itemView.findViewById(R.id.cd_categoria);

        }


    }


}

