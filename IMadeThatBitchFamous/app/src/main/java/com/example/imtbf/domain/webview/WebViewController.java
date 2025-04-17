package com.example.imtbf.domain.webview;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.imtbf.data.models.DeviceProfile;
import com.example.imtbf.utils.Logger;

/**
 * Controls WebView configuration and provides central access to WebView functionality.
 */
public class WebViewController {
    private static final String TAG = "WebViewController";
    private final Context context;

    public WebViewController(Context context) {
        this.context = context;
    }

    /**
     * Configure a WebView based on a device profile.
     * @param webView WebView to configure
     * @param deviceProfile Device profile to use for configuration
     */
    public void configureWebView(WebView webView, DeviceProfile deviceProfile) {
        if (webView == null) {
            Logger.e(TAG, "Cannot configure null WebView");
            return;
        }

        WebSettings settings = webView.getSettings();

        // Basic settings
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        // Performance settings
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // AppCache is deprecated, remove this line:
        // settings.setAppCacheEnabled(true);

        // Content display settings
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLoadsImagesAutomatically(true);

        // User agent
        if (deviceProfile != null && deviceProfile.getUserAgent() != null) {
            settings.setUserAgentString(deviceProfile.getUserAgent());
            Logger.d(TAG, "Set user agent: " + deviceProfile.getUserAgent());
        }

        // Set up cookie handling
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        Logger.i(TAG, "WebView configured for device: " +
                (deviceProfile != null ? deviceProfile.getDeviceType() : "unknown"));
    }

    /**
     * Clear all WebView data and cookies.
     * @param webView WebView to clear
     */
    public void clearWebViewData(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearFormData();

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
            cookieManager.flush();

            Logger.d(TAG, "WebView data cleared");
        }
    }
}