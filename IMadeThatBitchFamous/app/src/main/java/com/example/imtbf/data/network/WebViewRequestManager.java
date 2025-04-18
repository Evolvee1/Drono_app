package com.example.imtbf.data.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.imtbf.data.models.DeviceProfile;
import com.example.imtbf.data.models.SimulationSession;
import com.example.imtbf.utils.Constants;
import com.example.imtbf.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages WebView-based requests with Instagram referrer spoofing.
 * This class handles making requests that appear to come from Instagram.
 */
public class WebViewRequestManager {

    private static final String TAG = "WebViewRequestManager";
    private static final int MAX_REDIRECTS = 10;
    private static final int PAGE_LOAD_TIMEOUT_MS = 30000; // 30 seconds

    private final Context context;
    private final NetworkStateMonitor networkStateMonitor;
    private WebView webView;
    private final Handler mainHandler;
    private final Random random = new Random();

    // Add flag to control whether to create a new WebView for each request
    private boolean useNewWebViewPerRequest = false;
    // Counter for tracking WebView creations
    private int webViewCreationCounter = 0;
    // Store the current instance ID for debugging
    private String currentWebViewId = "";

    /**
     * Constructor that initializes the WebView request manager.
     * @param context Application context
     * @param networkStateMonitor Network state monitor
     */
    public WebViewRequestManager(Context context, NetworkStateMonitor networkStateMonitor) {
        this.context = context;
        this.networkStateMonitor = networkStateMonitor;
        this.mainHandler = new Handler(Looper.getMainLooper());

        // Log initialization for debugging
        String logMessage = "WebViewRequestManager initialized";
        Logger.i(TAG, logMessage);
    }

    /**
     * Set whether to use a new WebView for each request.
     * @param useNewWebViewPerRequest True to create a new WebView for each request, false to reuse
     */
    public void setUseNewWebViewPerRequest(boolean useNewWebViewPerRequest) {
        String logMessage = "New WebView per request mode changed: " +
                (this.useNewWebViewPerRequest ? "Enabled" : "Disabled") + " -> " +
                (useNewWebViewPerRequest ? "Enabled" : "Disabled");

        this.useNewWebViewPerRequest = useNewWebViewPerRequest;

        // Log to app logs
        Logger.i(TAG, logMessage);

        // If we're switching to reuse mode and we have an existing WebView, clean it up
        if (!useNewWebViewPerRequest && webView != null) {
            Logger.i(TAG, "Cleaning up existing WebView for reuse mode");
            cleanupWebViewState();
        } else if (useNewWebViewPerRequest && webView != null) {
            // If switching to new WebView mode, destroy any existing WebView
            Logger.i(TAG, "Destroying existing WebView for per-request mode");
            destroyWebViewCompletely();
        }
    }

    /**
     * Completely destroys the current WebView instance
     */
    private void destroyWebViewCompletely() {
        if (webView != null) {
            mainHandler.post(() -> {
                try {
                    Logger.i(TAG, "Starting complete destruction of WebView ID: " + currentWebViewId);

                    // Remove from parent if attached
                    ViewParent parent = webView.getParent();
                    if (parent instanceof ViewGroup) {
                        ((ViewGroup) parent).removeView(webView);
                        Logger.d(TAG, "WebView removed from parent view");
                    }

                    // Stop any loading and clear all state
                    webView.stopLoading();
                    webView.clearHistory();
                    webView.clearCache(true);
                    webView.clearFormData();
                    webView.clearSslPreferences();
                    webView.clearMatches();

                    // Clear all types of storage
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeAllCookies(null);
                    cookieManager.flush();
                    WebStorage.getInstance().deleteAllData();

                    // Try to clear as much as possible
                    try {
                        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword();
                        WebViewDatabase.getInstance(context).clearFormData();
                    } catch (Exception e) {
                        Logger.e(TAG, "Error clearing WebView database: " + e.getMessage());
                    }

                    // Load blank page before destroying
                    webView.loadUrl("about:blank");

                    // Destroy the WebView
                    webView.destroy();
                    webView = null;

                    // Force garbage collection hint
                    System.gc();

                    Logger.i(TAG, "WebView ID: " + currentWebViewId + " completely destroyed");
                    currentWebViewId = "";

                } catch (Exception e) {
                    Logger.e(TAG, "Error destroying WebView: " + e.getMessage());
                }
            });
        }
    }

    /**
     * Check if new WebView per request mode is enabled.
     * @return True if enabled, false otherwise
     */
    public boolean isUseNewWebViewPerRequest() {
        return useNewWebViewPerRequest;
    }

    /**
     * Make a request to the specified URL with Instagram referrer.
     * @param url Target URL
     * @param deviceProfile Device profile for user agent
     * @param session Current simulation session
     * @param callback Callback for request result
     */
    public void makeRequest(
            String url,
            DeviceProfile deviceProfile,
            SimulationSession session,
            RequestCallback callback) {

        if (!networkStateMonitor.isNetworkAvailable()) {
            String errorMessage = "Network not available";
            Logger.e(TAG, errorMessage);
            if (callback != null) {
                callback.onError(errorMessage);
            }
            return;
        }

        // Check if URL is valid
        if (url == null || url.isEmpty() || !(url.startsWith("http://") || url.startsWith("https://"))) {
            String errorMessage = "Invalid URL: " + url;
            Logger.e(TAG, errorMessage);
            if (callback != null) {
                callback.onError(errorMessage);
            }
            return;
        }

        String currentIp = networkStateMonitor.getCurrentIpAddress().getValue();
        Logger.i(TAG, "Making WebView request to " + url + " with IP " + currentIp);
        Logger.i(TAG, "Using " + (deviceProfile.isInstagramApp() ? "Instagram app" : "browser") +
                " profile on " + deviceProfile.getPlatform() +
                " device type: " + deviceProfile.getDeviceType() +
                ", New WebView per request: " + useNewWebViewPerRequest);

        final long startTime = System.currentTimeMillis();
        final AtomicBoolean requestComplete = new AtomicBoolean(false);
        final CountDownLatch requestLatch = new CountDownLatch(1);

        // WebView must be created and used on the main thread
        mainHandler.post(() -> {
            try {
                // Create or reuse WebView based on settings
                if (useNewWebViewPerRequest || webView == null) {
                    // Clean up existing WebView if there is one
                    if (webView != null) {
                        Logger.i(TAG, "Destroying existing WebView before creating a new one");
                        destroyWebViewCompletely();
                        webView = null;
                    }

                    // Create a new WebView
                    webViewCreationCounter++;
                    currentWebViewId = "WebView-" + UUID.randomUUID().toString().substring(0, 8);
                    Logger.i(TAG, "Creating new WebView #" + webViewCreationCounter + ", ID: " + currentWebViewId);

                    webView = new WebView(context);
                    webView.setLayoutParams(new LinearLayout.LayoutParams(1, 1)); // 1x1 pixel size (invisible)
                    webView.setTag(currentWebViewId); // Tag the WebView for tracking
                } else {
                    // Reuse existing WebView but clear state
                    Logger.i(TAG, "Reusing existing WebView ID: " + currentWebViewId + " with state clearing");
                    cleanupWebViewState();
                }

                // Configure WebView with specialized settings for uniqueness
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setUserAgentString(deviceProfile.getUserAgent());
                webSettings.setDomStorageEnabled(true);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                webSettings.setLoadsImagesAutomatically(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
                webSettings.setSupportMultipleWindows(false);

                // Add unique identifier to the User-Agent if using new WebView per request
                // This helps make each request appear as a different visitor
                if (useNewWebViewPerRequest) {
                    String baseUserAgent = deviceProfile.getUserAgent();
                    String uniqueId = UUID.randomUUID().toString().substring(0, 8);
                    String uniqueUserAgent = baseUserAgent + " Visitor/" + uniqueId;
                    webSettings.setUserAgentString(uniqueUserAgent);

                    Logger.i(TAG, "Set unique User-Agent: [" + uniqueUserAgent + "]");
                }

                // Set up cookie handling
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.setAcceptThirdPartyCookies(webView, true);

                // For per-request mode, make sure cookies are reset
                if (useNewWebViewPerRequest) {
                    cookieManager.removeAllCookies(null);
                    cookieManager.flush();
                    Logger.d(TAG, "Cookies cleared for new WebView instance");
                }

                // Set up WebView client to handle page loads
                webView.setWebViewClient(new RobustWebViewClient(
                        url,
                        currentIp,
                        session,
                        callback,
                        requestComplete,
                        requestLatch,
                        webView,
                        currentWebViewId));

                // Add Instagram headers and other realistic headers
                Map<String, String> headers = getRealisticHeaders(deviceProfile, useNewWebViewPerRequest);
                StringBuilder headerDebug = new StringBuilder("Request headers:\n");
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    headerDebug.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
                }
                Logger.d(TAG, headerDebug.toString());

                // Load the URL with headers
                Logger.i(TAG, "Loading URL in WebView ID: " + currentWebViewId);
                webView.loadUrl(url, headers);

                // Set a timeout for the request
                mainHandler.postDelayed(() -> {
                    if (!requestComplete.getAndSet(true)) {
                        String timeoutMsg = "WebView request timed out after " + PAGE_LOAD_TIMEOUT_MS + "ms";
                        Logger.w(TAG, timeoutMsg);

                        if (session != null) {
                            session.addRequestResult(
                                    new SimulationSession.RequestResult(
                                            timeoutMsg, currentIp));
                        }

                        if (callback != null) {
                            callback.onError(timeoutMsg);
                        }

                        // Clean up and release latch
                        if (useNewWebViewPerRequest) {
                            Logger.i(TAG, "Destroying WebView ID: " + currentWebViewId + " after timeout");
                            destroyWebViewCompletely();
                        } else {
                            cleanupWebView();
                        }
                        requestLatch.countDown();
                    }
                }, PAGE_LOAD_TIMEOUT_MS);

            } catch (Exception e) {
                String exceptionMsg = "Error creating WebView: " + e.getMessage();
                Logger.e(TAG, exceptionMsg, e);

                if (!requestComplete.getAndSet(true)) {
                    if (session != null) {
                        session.addRequestResult(
                                new SimulationSession.RequestResult(
                                        exceptionMsg, currentIp));
                    }

                    if (callback != null) {
                        callback.onError(exceptionMsg);
                    }

                    requestLatch.countDown();
                }
            }
        });

        // Wait for the request to complete with a timeout
        try {
            requestLatch.await(PAGE_LOAD_TIMEOUT_MS + 5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Logger.w(TAG, "WebView request interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get realistic browser headers for WebView request, with option to add unique visitor info
     */
    private Map<String, String> getRealisticHeaders(DeviceProfile deviceProfile, boolean addUniqueVisitor) {
        Map<String, String> headers = new HashMap<>();

        // Instagram referrer
        headers.put("Referer", Constants.INSTAGRAM_REFERER);

        // User Agent (may be modified for uniqueness)
        String userAgent = deviceProfile.getUserAgent();
        if (addUniqueVisitor) {
            // Add a unique visitor ID to appear as a different visitor
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);
            userAgent = userAgent + " UniqueVisitor/" + uniqueId;
            Logger.d(TAG, "Added unique visitor ID to User-Agent: " + uniqueId);
        }
        headers.put("User-Agent", userAgent);

        // Standard browser headers
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Connection", "keep-alive");
        headers.put("Upgrade-Insecure-Requests", "1");

        // Security and privacy headers that real browsers send
        headers.put("DNT", "1"); // Do Not Track

        // Add cache control headers for better uniqueness
        if (addUniqueVisitor) {
            headers.put("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.put("Pragma", "no-cache");
            headers.put("Expires", "0");
        }

        return headers;
    }

    /**
     * Cleans up WebView state without destroying the WebView.
     * This is used when reusing a WebView instance.
     */
    private void cleanupWebViewState() {
        if (webView != null) {
            mainHandler.post(() -> {
                try {
                    Logger.i(TAG, "Clearing WebView state for ID: " + currentWebViewId);

                    webView.stopLoading();
                    webView.loadUrl("about:blank");
                    webView.clearHistory();
                    webView.clearCache(true);
                    webView.clearFormData();
                    webView.clearSslPreferences();

                    // Clear cookies
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeAllCookies(null);
                    cookieManager.flush();

                    // Clear storage
                    WebStorage.getInstance().deleteAllData();

                    // Clear navigation history
                    webView.clearHistory();

                    // Try to clear form data and passwords
                    try {
                        WebViewDatabase.getInstance(context).clearHttpAuthUsernamePassword();
                        WebViewDatabase.getInstance(context).clearFormData();
                    } catch (Exception e) {
                        // Just log and continue
                        Logger.w(TAG, "Error clearing WebView database: " + e.getMessage());
                    }

                    Logger.i(TAG, "WebView state cleared successfully for ID: " + currentWebViewId);
                } catch (Exception e) {
                    Logger.e(TAG, "Error during WebView state cleanup: " + e.getMessage());
                }
            });
        }
    }

    /**
     * A robust WebViewClient that handles redirects and page loading events.
     */
    private class RobustWebViewClient extends WebViewClient {
        private int redirectCount = 0;
        private long startTime = 0;
        private final String initialUrl;
        private final String currentIp;
        private final SimulationSession session;
        private final RequestCallback callback;
        private final AtomicBoolean requestComplete;
        private final CountDownLatch requestLatch;
        private final WebView webView;
        private final String webViewId;

        public RobustWebViewClient(
                String url,
                String currentIp,
                SimulationSession session,
                RequestCallback callback,
                AtomicBoolean requestComplete,
                CountDownLatch requestLatch,
                WebView webView,
                String webViewId) {
            this.initialUrl = url;
            this.currentIp = currentIp;
            this.session = session;
            this.callback = callback;
            this.requestComplete = requestComplete;
            this.requestLatch = requestLatch;
            this.webView = webView;
            this.webViewId = webViewId;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startTime = System.currentTimeMillis();

            if (!url.equals(initialUrl)) {
                redirectCount++;
                Logger.i(TAG, "-----------********** WebView ID: " + webViewId + " - Redirect #" + redirectCount + ": " + initialUrl + " -> " + url + "**********-------------");
            }

            Logger.i(TAG, "WebView ID: " + webViewId + " - Page load started: " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            long loadTime = System.currentTimeMillis() - startTime;
            Logger.i(TAG, "WebView ID: " + webViewId + " - Page load finished: " + url + " in " + loadTime + "ms");
            Logger.i(TAG, "WebView ID: " + webViewId + " - Total redirects: " + redirectCount);

            // Give a short delay to ensure JavaScript has run
            mainHandler.postDelayed(() -> {
                if (!requestComplete.getAndSet(true)) {
                    // Now simulate behavior before completing
                    Logger.i(TAG, "WebView ID: " + webViewId + " - Starting user behavior simulation");

                    simulateUserBehavior(webView, () -> {
                        if (session != null) {
                            session.addRequestResult(
                                    new SimulationSession.RequestResult(
                                            200, loadTime, currentIp));
                        }

                        if (callback != null) {
                            callback.onSuccess(200, loadTime);
                        }

                        // Now clean up and release latch
                        Logger.i(TAG, "WebView ID: " + webViewId + " - Request completed successfully, cleaning up");

                        if (useNewWebViewPerRequest) {
                            destroyWebViewCompletely();
                        } else {
                            cleanupWebView();
                        }
                        requestLatch.countDown();
                    });
                }
            }, 500);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            String errorMsg = "WebView ID: " + webViewId + " - Error: " + description;
            Logger.e(TAG, errorMsg);

            handleError(errorMsg);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (request.isForMainFrame()) {
                String errorMsg = "WebView ID: " + webViewId + " - Error: " + error.getDescription();
                Logger.e(TAG, errorMsg);

                handleError(errorMsg);
            }
        }

        private void handleError(String errorMsg) {
            if (!requestComplete.getAndSet(true)) {
                if (session != null) {
                    session.addRequestResult(
                            new SimulationSession.RequestResult(
                                    errorMsg, currentIp));
                }

                if (callback != null) {
                    callback.onError(errorMsg);
                }

                // Clean up and release latch
                Logger.i(TAG, "WebView ID: " + webViewId + " - Request failed, cleaning up");

                if (useNewWebViewPerRequest) {
                    destroyWebViewCompletely();
                } else {
                    cleanupWebView();
                }
                requestLatch.countDown();
            }
        }
    }

    /**
     * Simulate user behavior on loaded page
     */
    private void simulateUserBehavior(WebView webView, Runnable onComplete) {
        // Randomly determine how long to stay on page (5-10 seconds)
        int stayTimeMs = 5000 + random.nextInt(5000);
        Logger.i(TAG, "-----------********** WebView ID: " + currentWebViewId + " - Simulating user behavior for " + stayTimeMs + "ms");

        // Schedule some scroll events
        scheduleScrollEvents(webView, stayTimeMs);

        // Complete after the stay time
        mainHandler.postDelayed(onComplete, stayTimeMs);
    }

    /**
     * Schedule random scroll events
     */
    private void scheduleScrollEvents(WebView webView, int totalTimeMs) {
        int scrollCount = 2 + random.nextInt(4); // 2-5 scrolls
        Logger.d(TAG, "WebView ID: " + currentWebViewId + " - Scheduling " + scrollCount + " scroll events");

        for (int i = 1; i <= scrollCount; i++) {
            int delay = (totalTimeMs * i) / (scrollCount + 1); // Distribute throughout the time
            int scrollAmount = 100 + random.nextInt(300); // 100-400px

            final int finalScrollAmount = scrollAmount;
            final int eventNumber = i;
            mainHandler.postDelayed(() -> {
                try {
                    String js = "window.scrollBy(0, " + finalScrollAmount + ");";
                    webView.evaluateJavascript(js, null);
                    Logger.d(TAG, "WebView ID: " + currentWebViewId + " - Scroll event #" + eventNumber + ": " + finalScrollAmount + "px");
                } catch (Exception e) {
                    Logger.e(TAG, "Error in scroll event: " + e.getMessage());
                }
            }, delay);
        }
    }

    /**
     * Clean up the WebView to prevent memory leaks.
     */
    private void cleanupWebView() {
        mainHandler.post(() -> {
            if (webView != null) {
                try {
                    Logger.i(TAG, "Cleaning up WebView ID: " + currentWebViewId);

                    webView.stopLoading();
                    webView.loadUrl("about:blank");
                    webView.clearHistory();
                    webView.clearCache(true);
                    webView.clearFormData();

                    // If using new WebView per request, fully destroy the WebView
                    if (useNewWebViewPerRequest) {
                        // Remove from parent view if attached
                        ViewParent parent = webView.getParent();
                        if (parent instanceof ViewGroup) {
                            ((ViewGroup) parent).removeView(webView);
                            Logger.d(TAG, "WebView removed from parent view");
                        }

                        webView.destroy();
                        webView = null;
                        System.gc(); // Hint to garbage collector

                        Logger.i(TAG, "-----------********** WebView ID: " + currentWebViewId + " completely destroyed **************-----------");
                        currentWebViewId = "";
                    } else {
                        // Just clean the WebView but keep it for reuse
                        webView.destroy();
                        webView = null;
                        Logger.i(TAG, "WebView instance cleaned for reuse");
                    }
                } catch (Exception e) {
                    Logger.e(TAG, "Error during WebView cleanup: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Callback interface for WebView requests.
     */
    public interface RequestCallback {
        void onSuccess(int statusCode, long responseTimeMs);
        void onError(String error);
    }
}