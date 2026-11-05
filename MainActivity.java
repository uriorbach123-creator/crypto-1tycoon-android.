package com.example.cryptotycoon;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends Activity {
    // Google's official test rewarded-ad unit ID. Replace with your own before publishing.
    private static final String REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

    private WebView webView;
    private RewardedAd rewardedAd;
    private boolean isLoadingAd = false;
    private boolean adShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");

        webView.loadUrl("file:///android_asset/index.html");

        MobileAds.initialize(this, initializationStatus -> runOnUiThread(this::loadRewardedAd));
    }

    private void loadRewardedAd() {
        if (isLoadingAd || rewardedAd != null || isFinishing()) return;
        isLoadingAd = true;

        RewardedAd.load(this, REWARDED_AD_UNIT_ID, new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        rewardedAd = ad;
                        isLoadingAd = false;
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError error) {
                        rewardedAd = null;
                        isLoadingAd = false;
                        // Retry only when the player asks again, avoiding a tight retry loop.
                    }
                });
    }

    private void notifyPage(String function, String message) {
        if (webView == null) return;
        String safe = message == null ? "" : message
                .replace("\\", "\\\\").replace("'", "\\'")
                .replace("\n", " ").replace("\r", " ");
        runOnUiThread(() -> webView.evaluateJavascript(
                "if(window." + function + ") window." + function + '("' + safe + '");", null));
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void showRewardedAd() {
            runOnUiThread(() -> {
                if (adShowing) return;

                if (rewardedAd == null) {
                    notifyPage("onRewardedAdUnavailable", "הסרטון עדיין נטען או אינו זמין. נסה שוב בעוד רגע.");
                    loadRewardedAd();
                    return;
                }

                RewardedAd adToShow = rewardedAd;
                rewardedAd = null;
                adShowing = true;

                adToShow.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        adShowing = false;
                        loadRewardedAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError error) {
                        adShowing = false;
                        notifyPage("onRewardedAdUnavailable", "לא ניתן להציג את הסרטון כרגע. נסה שוב.");
                        loadRewardedAd();
                    }
                });

                adToShow.show(MainActivity.this, rewardItem -> {
                    // Grant the in-game bonus only after Google's earned-reward callback.
                    notifyPage("onRewardedAdCompleted", "");
                });
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.removeJavascriptInterface("AndroidBridge");
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
