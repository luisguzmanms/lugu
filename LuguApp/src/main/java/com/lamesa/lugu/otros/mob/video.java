package com.lamesa.lugu.otros.mob;

import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.otros.statics.constantes.mixAdOpened;

import android.content.Context;
import android.os.Bundle;

import com.amplitude.api.Amplitude;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class video {
    // Remove the below line after defining your own ad unit ID.

    public static RewardedAd rewardedAd;


    public static RewardedAd createAndLoadRewardedAd(Context mContext) {
        rewardedAd = new RewardedAd(mContext,
                "ca-app-pub-1553194436365145/2272055918");
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                //	Toast.makeText(mContext, "onRewardedAdLoaded", Toast.LENGTH_SHORT).show();
                //region MIX mixAdClic para estadisticas
                JSONObject props = new JSONObject();
                try {
                    props.put("TipoAd", "Rewarded");
                    //para FB
                    Bundle params = new Bundle();
                    params.putString("TipoAd", "Rewarded");

                    mFirebaseAnalytics.logEvent(mixAdOpened, params);
                    mixpanel.track(mixAdOpened, props);
                    Amplitude.getInstance().logEvent(mixAdOpened, props);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                //	Toast.makeText(mContext, " adError . "+adError.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        return rewardedAd;

    }


    public static void onRewardedAdClosed(Context mContext) {
        rewardedAd = createAndLoadRewardedAd(mContext);
    }


}