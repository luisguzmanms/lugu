package com.lamesa.lugu.otros.mob;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class video {
	// Remove the below line after defining your own ad unit ID.

	public static RewardedAd rewardedAd;


	public static RewardedAd createAndLoadRewardedAd(Context mContext) {
		rewardedAd = new RewardedAd(mContext,
				"ca-app-pub-3940256099942544/5224354917");
		RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
			@Override
			public void onRewardedAdLoaded() {
				// Ad successfully loaded.
			}

			@Override
			public void onRewardedAdFailedToLoad(LoadAdError adError) {
				// Ad failed to load.
			}
		};

		rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

		return rewardedAd;

	}


	public static void onRewardedAdClosed(Context mContext) {
		rewardedAd = createAndLoadRewardedAd(mContext);
	}


}