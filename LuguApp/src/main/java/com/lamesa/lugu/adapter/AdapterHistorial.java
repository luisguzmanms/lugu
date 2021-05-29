package com.lamesa.lugu.adapter;

import android.content.Context;
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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lamesa.lugu.R;
import com.lamesa.lugu.model.ModelCancion;
import com.lamesa.lugu.model.ModelCategoria;

import java.util.Collections;
import java.util.List;

import static com.lamesa.lugu.activity.act_main.bottomNavigationHis_Fav;
import static com.lamesa.lugu.activity.act_main.mAdapterHistorial;
import static com.lamesa.lugu.activity.act_main.tinyDB;
import static com.lamesa.lugu.otros.metodos.CategoriaAleatoria;
import static com.lamesa.lugu.otros.metodos.DialogoEliminarLista;
import static com.lamesa.lugu.otros.metodos.getLinkAndPlay;
import static com.lamesa.lugu.otros.mob.inter.CargarInterAleatorio;
import static com.lamesa.lugu.otros.statics.constantes.TBartistaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBcategoriaCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBidCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlinkCancionSonando;
import static com.lamesa.lugu.otros.statics.constantes.TBlistCategorias;
import static com.lamesa.lugu.otros.statics.constantes.TBlistHistorial;
import static com.lamesa.lugu.otros.statics.constantes.TBnombreCancionSonando;


public class AdapterHistorial extends RecyclerView.Adapter<AdapterHistorial.MyViewHolder> {

    private final Context mContext;
    private final List<ModelCancion> mListHistorial;

    //   private InterstitialAd mInterstitialAd;

    private int lastPosition = -1;


    public AdapterHistorial(Context mContext, List<ModelCancion> mListHistorial) {

        this.mContext = mContext;
        this.mListHistorial = mListHistorial;

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
        view = mInflater.inflate(R.layout.item_historial, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


     /*   List<Integer> ListaHistorial = tinyDB.getListInt("historial-" + tbIdFilm);

        for (int historial : ListaHistorial) {
            if (historial == position) {
                holder.ivVisto.setVisibility(View.VISIBLE);
                // setAnimation(holder.ivVisto, position);
                // break;
            }
        }

      */


        setAnimation(holder.cdCancionHistorial, position);
        holder.tvCancion.setText(mListHistorial.get(position).getCancion());
        holder.tvArtista.setText(mListHistorial.get(position).getArtista());


        if (holder.tvCancion.getText().equals(tinyDB.getString(TBnombreCancionSonando))) {
            holder.tvCancion.setTextColor(mContext.getResources().getColor(R.color.learn_colorPrimary));
            holder.tvArtista.setTextColor(mContext.getResources().getColor(R.color.learn_colorPrimary));
        } else {
            holder.tvCancion.setTextColor(mContext.getResources().getColor(R.color.learn_gradient_grey_1));
            holder.tvArtista.setTextColor(mContext.getResources().getColor(R.color.learn_gradient_grey_1));
        }

        //region obtener nombre de categoria desde la id y mostrar en tvCategoria
        List<ModelCategoria> list = tinyDB.getListModelCategoria(TBlistCategorias, ModelCategoria.class);
        for (ModelCategoria categoria : list) {
            if (categoria.getId().equals(mListHistorial.get(position).getCategoria())) {
                holder.tvCategoria.setText(categoria.getNombre());
            }
        }
        //endregion

        holder.cdCancionHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // cambiar modo de categoria a apagado, se reproducira solo las canciones de la categoria seleccionada
                CategoriaAleatoria(mContext, false, tinyDB);
                // metodo para cargar cancion de la categoria seleccionada

                CargarInterAleatorio(mContext, 15);
                getLinkAndPlay(mContext, mListHistorial.get(position).getLinkYT(), 1);

                // guardar datos de la cancion sonando en TinyDB
                tinyDB.putString(TBidCancionSonando, mListHistorial.get(position).getId());
                tinyDB.putString(TBnombreCancionSonando, mListHistorial.get(position).getCancion());
                tinyDB.putString(TBartistaCancionSonando, mListHistorial.get(position).getArtista());
                tinyDB.putString(TBcategoriaCancionSonando, TBlistHistorial);
                tinyDB.putString(TBlinkCancionSonando, mListHistorial.get(position).getLinkYT());


            }
        });
        holder.cdCancionHistorial.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogoEliminarLista(mContext, tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class), TBlistHistorial);
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
                                mListHistorial.remove(position);
                                tinyDB.putListModelCancion(TBlistHistorial, mListHistorial);
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);

                                Toast.makeText(mContext, mContext.getResources().getString(R.string.item_eliminado), Toast.LENGTH_SHORT).show();

                                break;

                            case R.id.option_eliminar_lista:
                                mListHistorial.removeAll(mListHistorial);
                                List<ModelCancion> listhisto = tinyDB.getListModelCancion(TBlistHistorial, ModelCancion.class);
                                listhisto.removeAll(listhisto);
                                tinyDB.putListModelCancion(TBlistHistorial, listhisto);
                                bottomNavigationHis_Fav.setSelectedItemId(R.id.navigation_historial);

                                Toast.makeText(mContext, mContext.getResources().getString(R.string.lista_eliminada), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
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
        return mListHistorial.size();
    }


    public void setUpdateHistorial(List<ModelCancion> mListHistorial) {

        this.mListHistorial.removeAll(this.mListHistorial);
        this.mListHistorial.addAll(mListHistorial);
        Collections.reverse(this.mListHistorial);
        if (mAdapterHistorial != null) {

            mAdapterHistorial.notifyDataSetChanged();
        }


    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CardView cdCancionHistorial;
        private final TextView tvCancion;
        private final TextView tvArtista;
        private final TextView tvCategoria;
        private final ImageView ivOption;

        public MyViewHolder(View itemView) {
            super(itemView);
            cdCancionHistorial = itemView.findViewById(R.id.cd_cancionHistorial);
            tvCancion = itemView.findViewById(R.id.tv_cancion);
            tvArtista = itemView.findViewById(R.id.tv_artista);
            tvCategoria = itemView.findViewById(R.id.tv_categoria);
            ivOption = itemView.findViewById(R.id.iv_option);
        }
    }


}

