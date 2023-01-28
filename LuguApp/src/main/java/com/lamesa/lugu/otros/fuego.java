package com.lamesa.lugu.otros;


import static com.lamesa.lugu.otros.Utils.setLogCat;
import static com.lamesa.lugu.otros.constantes.LogActivo;
import static com.lamesa.lugu.otros.constantes.TAG_FUEGO;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fuego extends AppCompatActivity {


    public static FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    public static String RUTA_PADRE = "user" + "/" + mFirebaseUser.getUid() + "/";


    public static void EliminarDatoFB(String Ruta, final Context mContext) {
        setLogCat(TAG_FUEGO, "EliminarDatoFB", "d", LogActivo);


        final String valorString = "vacio";

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {

            // User Name
            String userNAME = mFirebaseUser.getDisplayName();
            // User ID
            String userUID = mFirebaseUser.getUid();
            // Email-ID
            String userEMAIL = mFirebaseUser.getEmail();
            // User-Profile (if available)
            Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

            //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


            final DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
            mref.getRef().removeValue();
            Toast.makeText(mContext, "DATO ELIMINADO", Toast.LENGTH_SHORT).show();
            System.out.println("REFB " + mref.getRef());
            Toast.makeText(mContext, mref.getRef().toString(), Toast.LENGTH_LONG).show();

        }
    }

    //region SET de firebase
    public static class setData {


        public static void StringFB(String Ruta, final String Valor, final Context mContext) {
            setLogCat(TAG_FUEGO, "fbSetStringChild", "d", LogActivo);

            if (mFirebaseUser != null) {

                // User Name
                String userNAME = mFirebaseUser.getDisplayName();
                // User ID
                String userUID = mFirebaseUser.getUid();
                // Email-ID
                String userEMAIL = mFirebaseUser.getEmail();
                // User-Profile (if available)
                Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

                //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().setValue(Valor);

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    }
                });


            }


        }

        public static void IntFB(String Ruta, final int Valor, final Context mContext) {
            setLogCat(TAG_FUEGO, "fbSetIntChild", "d", LogActivo);


            if (mFirebaseUser != null) {

                // User Name
                String userNAME = mFirebaseUser.getDisplayName();
                // User ID
                String userUID = mFirebaseUser.getUid();
                // Email-ID
                String userEMAIL = mFirebaseUser.getEmail();
                // User-Profile (if available)
                Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

                //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().setValue(Valor);

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    }
                });


            }
        }

        public static void BooleanFB(String Ruta, final Boolean Valor, final Context mContext) {
            setLogCat(TAG_FUEGO, "fbSetBooleanChild", "d", LogActivo);


            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser != null) {

                // User Name
                String userNAME = mFirebaseUser.getDisplayName();
                // User ID
                String userUID = mFirebaseUser.getUid();
                // Email-ID
                String userEMAIL = mFirebaseUser.getEmail();
                // User-Profile (if available)
                Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

                //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().setValue(Valor);
                        setLogCat("fuego.SetData.BooleanFB", "Se envió '" + Valor.toString() + "'" + " a: " + Ruta, "d", LogActivo);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        setLogCat("fuego.SetData.BooleanFB", "ERROR NO envió '" + Valor.toString() + "'" + " a: " + Ruta + " :: RAZON: " + databaseError.getMessage(), "e", LogActivo);
                    }
                });
            }
        }
    }

    //endregion

    //region SET de firebase

    public static class getData {


        public static Boolean BooleanFB(String Ruta, final Context mContext) {

            Boolean valorBoolean = null;

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser != null) {

                // User Name
                String userNAME = mFirebaseUser.getDisplayName();
                // User ID
                String userUID = mFirebaseUser.getUid();
                // Email-ID
                String userEMAIL = mFirebaseUser.getEmail();
                // User-Profile (if available)
                Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

                //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean valorBoolean = dataSnapshot.getValue(Boolean.class);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    }
                });
            }


            return valorBoolean;
        }

        public static String StringFB(String Ruta, final Context mContext) {
            setLogCat(TAG_FUEGO, "fbGetStringChild", "d", LogActivo);


            final String valorString = "vacio";

            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser != null) {

                // User Name
                String userNAME = mFirebaseUser.getDisplayName();
                // User ID
                String userUID = mFirebaseUser.getUid();
                // Email-ID
                String userEMAIL = mFirebaseUser.getEmail();
                // User-Profile (if available)
                Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

                //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String valorString = dataSnapshot.getValue(String.class);
                        System.out.println("valorString " + valorString);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    }
                });
            }


            return valorString;

        }

        public static int IntFB(String Ruta, final Context mContext) {


            final int valorInt = 0;


            FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            if (mFirebaseUser != null) {

                // User Name
                String userNAME = mFirebaseUser.getDisplayName();
                // User ID
                String userUID = mFirebaseUser.getUid();
                // Email-ID
                String userEMAIL = mFirebaseUser.getEmail();
                // User-Profile (if available)
                Uri userPHOTOURL = mFirebaseUser.getPhotoUrl();

                //  Toast.makeText(mContext, mFirebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();


                DatabaseReference mref = FirebaseDatabase.getInstance().getReference(Ruta);
                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int valorInt = Integer.parseInt(dataSnapshot.getValue().toString());
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Toast.makeText(mContext, "ha cambiado", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return valorInt;
        }

    }


    //endregion


}
