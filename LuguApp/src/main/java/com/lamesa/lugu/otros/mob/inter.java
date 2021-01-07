package com.lamesa.lugu.otros.mob;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.amplitude.api.Amplitude;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.lamesa.lugu.App.mFirebaseAnalytics;
import static com.lamesa.lugu.App.mixpanel;
import static com.lamesa.lugu.otros.statics.constantes.mixAdClic;

public class inter {
	// Remove the below line after defining your own ad unit ID.

	private static InterstitialAd mInterstitialAd;

	private static InterstitialAd newInterstitialAd(Context mContext) {
		InterstitialAd interstitialAd = new InterstitialAd(mContext);
		interstitialAd.setAdUnitId("ca-app-pub-1553194436365145/8454320885");
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {

			}

			@Override
			public void onAdFailedToLoad(int errorCode) {

			}

			@Override
			public void onAdClosed() {
				// Proceed to the next level.
				loadInterstitial(mContext);
			}
		});
		return interstitialAd;
	}

	public static void showInterstitial(Context mContext) {
		// Show the ad if it"s ready. Otherwise toast and reload the ad.
		if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
			//region MIX mixAdClic para estadisticas
			JSONObject props = new JSONObject();
			try {
				props.put("TipoAd", "Interstitial");
				//para FB
				Bundle params = new Bundle();
				params.putString("TipoAd", "Interstitial");

				mFirebaseAnalytics.logEvent(mixAdClic, params);
				mixpanel.track(mixAdClic, props);
				Amplitude.getInstance().logEvent(mixAdClic, props);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			//endregion
		} else {
			Toast.makeText(mContext, "Ad did not load", Toast.LENGTH_SHORT).show();
			loadInterstitial(mContext);
		}
	}


	public static void loadInterstitial(Context mContext) {
		// Show the next level and reload the ad to prepare for the level after.
		mInterstitialAd = newInterstitialAd(mContext);
		// Disable the next level button and load the ad.
		AdRequest adRequest = new AdRequest.Builder().build();
		mInterstitialAd.loadAd(adRequest);
	}

	public static void CargarInterAleatorio(Context mContext, int numProbabilidad){
		Random random = new Random();
		int numRandom = random.nextInt(numProbabilidad);
		if(numProbabilidad==numRandom){
			showInterstitial(mContext);
		}
	}

}