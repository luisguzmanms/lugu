package com.lamesa.lugu.otros;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.amplitude.api.Amplitude;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.otros.statics.constantes.mixAdOpened;


public class AdMesa {

    public static InterstitialAd mInterstitial; // Interstital
    private static AdView mAdView; // banner

    public static void createLoadInterstitial(final Context context, String ad_unit_id,View view)

    {

        mInterstitial = new InterstitialAd(context);
        mInterstitial.setAdUnitId(ad_unit_id);
        mInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // TODO Auto-generated method stub

                super.onAdFailedToLoad(errorCode);
            }

            @Override
            public void onAdOpened() {
                // TODO Auto-generated method stub

                //region MIX mixEpisodioClic para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("TipoAd", "Interstitial");
                    //para FB
                    Bundle params = new Bundle();
                    params.putString("TipoAd", "Interstitial");


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
            public void onAdClosed() {
                // TODO Auto-generated method stub
                super.onAdClosed();

            }

            @Override
            public void onAdLeftApplication() {
                // TODO Auto-generated method stub
                // Called when an ad leaves the app (for example, to go to the
                // browser).

                super.onAdLeftApplication();
            }

        });


    }







    public static void createLoadBanner(final Context context, View view) {
        // mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice("830648A2D5D5AF09D0FAED08D38E2353").//ca-app-pub-3940256099942544/6300978111
                build();
        mAdView.loadAd(adRequest);

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

}