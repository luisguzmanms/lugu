package com.lamesa.lugu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;

import java.util.Collections;
import java.util.List;

import static com.lamesa.lugu.activity.act_main.bottomNavigationHis_Fav;
import static com.lamesa.lugu.activity.act_main.mAdapterFavoritos;
import static com.lamesa.lugu.activity.act_main.tinydb;
import static com.lamesa.lugu.otros.metodos.CategoriaAleatoria;
import static com.lamesa.lugu.otros.metodos.DialogoEliminarLista;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.mob.inter.CargarInterAleatorio;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBimagenFondo;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCategorias;
import static com.lamesa.lugu.otros.statics.constantes.TBlistFavoritos;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBnumeroCancionSonando;

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


        //region obtener nombre de categoria desde la id y mostrar en tvCategoria
        List<ModelCategoria> list = tinydb.getListModelCategoria(TBlistCategorias, ModelCategoria.class);
        for (ModelCategoria categoria : list) {
            if (categoria.getId().equals(mListFavoritos.get(position).getCategoria())) {
                holder.tvCategoria.setText(categoria.getNombre());
            }
        }
        //endregion

        //region cambiar color del texto de la cancion que está sonando
        if (holder.tvCancion.getText().equals(tinydb.getString(TBnombreCancionSonando))) {
            holder.tvCancion.setTextColor(mContext.getResources().getColor(R.color.learn_colorPrimary));
            holder.tvArtista.setTextColor(mContext.getResources().getColor(R.color.learn_colorPrimary));
        } else {
            holder.tvCancion.setTextColor(mContext.getResources().getColor(R.color.learn_gradient_grey_1));
            holder.tvArtista.setTextColor(mContext.getResources().getColor(R.color.learn_gradient_grey_1));
        }
        //endregion

        //region extraer colores de imagenes
        Glide.with(mContext)
                .asBitmap()
                .load(tinydb.getString(TBimagenFondo))
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
                                if (holder.tvCancion.getText().equals(tinydb.getString(TBnombreCancionSonando))) {
                                    holder.tvCancion.setTextColor(palette.getLightMutedColor(mContext.getResources().getColor(R.color.learn_colorPrimary)));
                                    holder.tvArtista.setTextColor(palette.getLightVibrantColor(mContext.getResources().getColor(R.color.learn_colorPrimary)));
                                }

                            }
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // setLogInfo(mContext,"MediaNotificationManager.startNotify.onLoadCleared","Cargar imagen en Notificacion",false);
                    }
                });
        //endregion

        holder.cdCancionFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // cambiar modo de categoria a apagado, se reproducira solo las canciones de la categoria seleccionada
                CategoriaAleatoria(mContext, false, tinydb);
                // metodo para cargar cancion de la categoria seleccionada


                CargarInterAleatorio(mContext, 10);
                getLinkAndPlay(mContext, mListFavoritos.get(position).getLinkYT(), 1);


                //region guardar datos de la cancion sonando en TinyDB
                tinydb.putString(TBidCancionSonando, mListFavoritos.get(position).getId());
                tinydb.putString(TBnombreCancionSonando, mListFavoritos.get(position).getCancion());
                tinydb.putString(TBartistaCancionSonando, mListFavoritos.get(position).getArtista());
                tinydb.putString(TBcategoriaCancionSonando, TBlistFavoritos);
                tinydb.putString(TBlinkCancionSonando, mListFavoritos.get(position).getLinkYT());
                tinydb.putInt(TBnumeroCancionSonando, position);
                //endregion

                //region actualizar lista de favoritos
                mAdapterFavoritos.setUpdateFavoritos(tinydb.getListModelCancion(TBlistFavoritos, ModelCancion.class));
                //endregion

            }
        });
        holder.cdCancionFavoritos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogoEliminarLista(mContext, tinydb.getListModelCancion(TBlistFavoritos, ModelCancion.class), TBlistFavoritos);
                return false;
            }
        });

        holder.ivOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.ivOption);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu_historial, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_eliminar_item:
                                mListFavoritos.remove(position);
                                tinydb.putListModelCancion(TBlistFavoritos, mListFavoritos);
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);

                                Toast.makeText(mContext, R.string.item_eliminado, Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.option_eliminar_lista:
                                mListFavoritos.removeAll(mListFavoritos);
                                tinydb.putListModelCancion(TBlistFavoritos, mListFavoritos);
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_favoritos);

                                Toast.makeText(mContext, R.string.lista_eliminada, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
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

    public void setUpdateFavoritos(List<ModelCancion> mListFavoritos) {

        this.mListFavoritos.removeAll(this.mListFavoritos);
        this.mListFavoritos.addAll(mListFavoritos);
        Collections.reverse(this.mListFavoritos);
        if (mAdapterFavoritos != null) {
            mAdapterFavoritos.notifyDataSetChanged();
        }


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView cdCancionFavoritos;
        private final TextView tvCancion;
        private final TextView tvArtista;
        private final TextView tvCategoria;
        private final ImageView ivOption;

        public MyViewHolder(View itemView) {
            super(itemView);
            cdCancionFavoritos = itemView.findViewById(R.id.cd_cancionHistorial);
            tvCancion = itemView.findViewById(R.id.tv_cancion);
            tvArtista = itemView.findViewById(R.id.tv_artista);
            tvCategoria = itemView.findViewById(R.id.tv_categoria);
            ivOption = itemView.findViewById(R.id.iv_option);
        }
    }


}

