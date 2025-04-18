package com.example.imtbf.presentation.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.CompoundButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.core.widget.NestedScrollView;
import android.content.Context;
import java.util.UUID; // Also add this for UUID

import androidx.appcompat.app.AppCompatActivity;

import com.example.imtbf.InstagramTrafficSimulatorApp;
import com.example.imtbf.R;
import com.example.imtbf.data.local.PreferencesManager;
import com.example.imtbf.data.models.DeviceProfile;
import com.example.imtbf.data.models.UserAgentData;
import com.example.imtbf.data.network.HttpRequestManager;
import com.example.imtbf.data.network.NetworkStateMonitor;
import com.example.imtbf.data.network.WebViewRequestManager;
import com.example.imtbf.databinding.ActivityMainBinding;
import com.example.imtbf.domain.simulation.BehaviorEngine;
import com.example.imtbf.domain.simulation.SessionManager;
import com.example.imtbf.data.network.SessionClearingManager;
import com.example.imtbf.domain.simulation.TimingDistributor;
import com.example.imtbf.domain.system.AirplaneModeController;
import com.example.imtbf.domain.webview.WebViewController;
import com.example.imtbf.utils.Constants;
import com.example.imtbf.utils.Logger;

/**
 * Main activity for the Instagram Traffic Simulator app.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Constants for new preferences
    private static final String PREF_DELAY_MIN = "delay_min";
    private static final String PREF_DELAY_MAX = "delay_max";
    private static final int DEFAULT_DELAY_MIN = 1; // 1 second
    private static final int DEFAULT_DELAY_MAX = 5; // 5 seconds

    private WebView webView;
    private WebViewController webViewController;
    private boolean useWebViewMode = false;
    private boolean isConfigExpanded = true;

    private ActivityMainBinding binding;
    private PreferencesManager preferencesManager;
    private NetworkStateMonitor networkStateMonitor;
    private WebViewRequestManager webViewRequestManager;
    private HttpRequestManager httpRequestManager;
    private AirplaneModeController airplaneModeController;

    private SessionClearingManager sessionClearingManager;
    private BehaviorEngine behaviorEngine;
    private TimingDistributor timingDistributor;
    private SessionManager sessionManager;
    private long simulationStartTime = 0;

    // Fields for time tracking
    private long startTimeMs = 0;
    private Handler timeUpdateHandler = new Handler();
    private Runnable timeUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateElapsedTime();
            timeUpdateHandler.postDelayed(this, 1000); // Update every second
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize components
        initializeComponents();

        // Set up UI
        setupUI();

        // Set up listeners
        setupListeners();

        // Observe network state
        observeNetworkState();

        // Load saved settings
        loadSettings();

        // Ensure clean state for airplane mode controller
        if (airplaneModeController != null) {
            airplaneModeController.resetState();
        }

        if (webView != null) {
            webViewController.configureWebViewForIncognito(webView);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register network state monitor
        networkStateMonitor.register();

        // Fetch current IP
        networkStateMonitor.fetchCurrentIpAddress();

        // Update UI based on session state
        updateUIBasedOnSessionState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save settings
        saveSettings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister network state monitor
        networkStateMonitor.unregister();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // TODO: Open settings activity
            Toast.makeText(this, "Settings (Coming Soon)", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_clear_logs) {
            clearLogs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * Initialize all components needed for the app.
     */
    private void initializeComponents() {
        // Get preferences manager
        preferencesManager = ((InstagramTrafficSimulatorApp) getApplication()).getPreferencesManager();

        // Initialize network state monitor
        networkStateMonitor = new NetworkStateMonitor(this);

        // Initialize WebView request manager
        webViewRequestManager = new WebViewRequestManager(this, networkStateMonitor);

        // Initialize WebView controller
        webViewController = new WebViewController(this);

        // Initialize HTTP request manager
        httpRequestManager = new HttpRequestManager(this, networkStateMonitor);

        // Initialize timing distributor
        timingDistributor = new TimingDistributor(
                preferencesManager.getMinInterval(),
                preferencesManager.getMaxInterval(),
                Constants.DEFAULT_READING_TIME_MEAN_MS,
                Constants.DEFAULT_READING_TIME_STDDEV_MS,
                Constants.SCROLL_PROBABILITY
        );

        // Initialize behavior engine
        behaviorEngine = new BehaviorEngine(timingDistributor);

        // Initialize airplane mode controller
        airplaneModeController = new AirplaneModeController(
                this,
                networkStateMonitor,
                preferencesManager.getAirplaneModeDelay()
        );
        // Initialize session clearing manager
        sessionClearingManager = new SessionClearingManager(this);

        // Initialize session manager
        sessionManager = new SessionManager(
                this,
                networkStateMonitor,
                httpRequestManager,
                webViewRequestManager,
                airplaneModeController,
                behaviorEngine,
                timingDistributor
        );
    }

    /**
     * Test the WebView functionality
     */
    private void testWebView() {
        String testUrl = "https://detiyavanny.com/";
        DeviceProfile testProfile = new DeviceProfile.Builder()
                .deviceType(DeviceProfile.TYPE_MOBILE)
                .platform(DeviceProfile.PLATFORM_ANDROID)
                .deviceTier(DeviceProfile.TIER_MID_RANGE)
                .userAgent(UserAgentData.getRandomUserAgent())
                .region("slovakia")
                .build();

        webViewRequestManager.makeRequest(testUrl, testProfile, null, new WebViewRequestManager.RequestCallback() {
            @Override
            public void onSuccess(int statusCode, long responseTimeMs) {
                addLog("WebView test successful - Loaded in " + responseTimeMs + "ms");
            }

            @Override
            public void onError(String error) {
                addLog("WebView test failed: " + error);
            }
        });
    }



    /**
     * Set up WebView controls
     */
    private void setupWebViewControls() {
        // Use SwitchMaterial instead of Switch
        SwitchMaterial switchUseWebView = findViewById(R.id.switchUseWebView);
        Button btnHideWebView = findViewById(R.id.btnHideWebView);
        View cardWebView = findViewById(R.id.cardWebView);

        if (switchUseWebView == null || btnHideWebView == null) {
            Logger.e(TAG, "WebView controls not found in layout");
            return;
        }

        // Set initial state
        useWebViewMode = preferencesManager.getUseWebViewMode();
        switchUseWebView.setChecked(useWebViewMode);

        if (cardWebView != null) {
            cardWebView.setVisibility(useWebViewMode ? View.VISIBLE : View.GONE);
        }

        // Set up listener for the switch
        switchUseWebView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            useWebViewMode = isChecked;

            // Show WebView card if in WebView mode
            if (cardWebView != null) {
                cardWebView.setVisibility(useWebViewMode ? View.VISIBLE : View.GONE);
            }

            // Update preference
            preferencesManager.setUseWebViewMode(useWebViewMode);

            // Log the change
            addLog("Switched to " + (useWebViewMode ? "WebView" : "HTTP") + " mode");
        });

        // Set up listener for the hide button
        btnHideWebView.setOnClickListener(v -> {
            if (cardWebView != null) {
                cardWebView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Set up the UI components.
     */
    private void setupUI() {
        // Set up logs text view
        binding.tvLogs.setMovementMethod(new ScrollingMovementMethod());

        // Set up initial status
        binding.tvStatusLabel.setText("Status: Ready");
        binding.tvProgress.setText("Progress: 0/0");
        binding.tvCurrentIp.setText("Current IP: Checking...");

        // Disable stop button initially
        binding.btnStop.setEnabled(false);

        // Initialize WebView
        webView = findViewById(R.id.webView);
        if (webView != null) {
            webViewController.configureWebView(webView, null); // Initial configuration
        }

        SwitchMaterial switchAggressiveSessionClearing = findViewById(R.id.switchAggressiveSessionClearing);
        if (switchAggressiveSessionClearing != null) {
            // Set initial state from preferences
            switchAggressiveSessionClearing.setChecked(
                    preferencesManager.isAggressiveSessionClearingEnabled()
            );

            // Set listener for switch
            switchAggressiveSessionClearing.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Save preference
                preferencesManager.setAggressiveSessionClearingEnabled(isChecked);

                // Log the change
                addLog("Aggressive Session Clearing: " + (isChecked ? "Enabled" : "Disabled"));
            });
        }

        // Set up WebView controls
        setupWebViewControls();


        // Set up configuration toggle
        ImageButton btnToggleConfig = findViewById(R.id.btnToggleConfig);
        if (btnToggleConfig != null) {
            btnToggleConfig.setOnClickListener(v -> toggleConfigVisibility());
        }

        // Initialize config state
        isConfigExpanded = preferencesManager.getBoolean("config_expanded", true);
        View settingsSection = findViewById(R.id.settingsSection);
        if (settingsSection != null) {
            settingsSection.setVisibility(isConfigExpanded ? View.VISIBLE : View.GONE);
        }
        if (btnToggleConfig != null) {
            btnToggleConfig.setImageResource(isConfigExpanded ?
                    android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }
    }

    /**
     * Toggle the visibility of the configuration settings section
     */
    private void toggleConfigVisibility() {
        isConfigExpanded = !isConfigExpanded;
        View settingsSection = findViewById(R.id.settingsSection);
        ImageButton btnToggleConfig = findViewById(R.id.btnToggleConfig);

        if (settingsSection != null) {
            settingsSection.setVisibility(isConfigExpanded ? View.VISIBLE : View.GONE);
        }

        if (btnToggleConfig != null) {
            btnToggleConfig.setImageResource(isConfigExpanded ?
                    android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        }

        // Store preference
        preferencesManager.setBoolean("config_expanded", isConfigExpanded);
    }



    private WebView prepareIncognitoWebView(Context context, DeviceProfile deviceProfile) {
        try {
            // Create a fresh WebView for each request
            WebView freshWebView = new WebView(context);

            // Configure WebView with incognito settings and device profile
            webViewController.configureWebView(freshWebView, deviceProfile);

            // Additional optional configurations for incognito mode
            WebSettings settings = freshWebView.getSettings();

            // Ensure no tracking
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            // Clear any potential existing data
            freshWebView.clearCache(true);
            freshWebView.clearHistory();
            freshWebView.clearFormData();

            // Add a unique identifier for the session
            freshWebView.setTag(UUID.randomUUID().toString());

            // Optional: Log the preparation
            Logger.d(TAG, "Prepared incognito WebView with unique ID: " + freshWebView.getTag());

            return freshWebView;
        } catch (Exception e) {
            // Fallback to standard WebView if something goes wrong
            Logger.e(TAG, "Error preparing incognito WebView", e);
            return new WebView(context);
        }
    }



    /**
     * Set up button click listeners.
     */
    private void setupListeners() {
        // Start button
        binding.btnStart.setOnClickListener(v -> {
            if (validateInputs()) {
                startSimulation();
            }
        });

        // Stop button
        binding.btnStop.setOnClickListener(v -> {
            stopSimulation();
        });
    }

    /**
     * Observe network state changes.
     */
    private void observeNetworkState() {
        // Observe connection state
        networkStateMonitor.getIsConnected().observe(this, isConnected -> {
            String status = isConnected ? "Connected" : "Disconnected";
            addLog("Network: " + status);
        });

        // Observe IP address
        networkStateMonitor.getCurrentIpAddress().observe(this, ipAddress -> {
            if (ipAddress != null && !ipAddress.isEmpty()) {
                binding.tvCurrentIp.setText("Current IP: " + ipAddress);
                addLog("IP Address: " + ipAddress);
            } else {
                binding.tvCurrentIp.setText("Current IP: Unknown");
            }
        });

        // Observe airplane mode
        networkStateMonitor.getIsAirplaneModeOn().observe(this, isOn -> {
            addLog("Airplane Mode: " + (isOn ? "On" : "Off"));
        });
    }

    /**
     * Load saved settings from preferences.
     */
    private void loadSettings() {
        binding.etTargetUrl.setText(preferencesManager.getTargetUrl());
        binding.etMinInterval.setText(String.valueOf(preferencesManager.getMinInterval()));
        binding.etMaxInterval.setText(String.valueOf(preferencesManager.getMaxInterval()));
        binding.etIterations.setText(String.valueOf(preferencesManager.getIterations()));

        // Load custom delay settings
        binding.etDelayMin.setText(String.valueOf(
                preferencesManager.getInt(PREF_DELAY_MIN, DEFAULT_DELAY_MIN)));
        binding.etDelayMax.setText(String.valueOf(
                preferencesManager.getInt(PREF_DELAY_MAX, DEFAULT_DELAY_MAX)));

        binding.etDelayMin.setText(String.valueOf(
                preferencesManager.getInt(PREF_DELAY_MIN, DEFAULT_DELAY_MIN)));
        binding.etDelayMax.setText(String.valueOf(
                preferencesManager.getInt(PREF_DELAY_MAX, DEFAULT_DELAY_MAX)));

        // Add this line to load airplane mode delay
        binding.etAirplaneModeDelay.setText(String.valueOf(
                preferencesManager.getAirplaneModeDelay()));

        // Load WebView mode setting
        useWebViewMode = preferencesManager.getUseWebViewMode();
        SwitchMaterial switchUseWebView = findViewById(R.id.switchUseWebView);
        if (switchUseWebView != null) {
            switchUseWebView.setChecked(useWebViewMode);
        }
        // Load configuration expansion state
        isConfigExpanded = preferencesManager.getBoolean("config_expanded", true);

        SwitchMaterial switchAggressiveSessionClearing =
                findViewById(R.id.switchAggressiveSessionClearing);
        if (switchAggressiveSessionClearing != null) {
            switchAggressiveSessionClearing.setChecked(
                    preferencesManager.isAggressiveSessionClearingEnabled()
            );
        }
    }

    /**
     * Save settings to preferences.
     */
    private void saveSettings() {
        try {
            String targetUrl = binding.etTargetUrl.getText().toString().trim();
            if (!targetUrl.isEmpty()) {
                preferencesManager.setTargetUrl(targetUrl);
            }

            int minInterval = Integer.parseInt(binding.etMinInterval.getText().toString());
            preferencesManager.setMinInterval(minInterval);

            int maxInterval = Integer.parseInt(binding.etMaxInterval.getText().toString());
            preferencesManager.setMaxInterval(maxInterval);

            int iterations = Integer.parseInt(binding.etIterations.getText().toString());
            preferencesManager.setIterations(iterations);

            // Save custom delay settings
            int delayMin = Integer.parseInt(binding.etDelayMin.getText().toString());
            int delayMax = Integer.parseInt(binding.etDelayMax.getText().toString());
            preferencesManager.setInt(PREF_DELAY_MIN, delayMin);
            preferencesManager.setInt(PREF_DELAY_MAX, delayMax);

            // Add this block to save airplane mode delay
            int airplaneModeDelay = Integer.parseInt(binding.etAirplaneModeDelay.getText().toString());
            preferencesManager.setAirplaneModeDelay(airplaneModeDelay);

            // Save WebView mode setting
            preferencesManager.setUseWebViewMode(useWebViewMode);

            preferencesManager.setBoolean("config_expanded", isConfigExpanded);

        } catch (NumberFormatException e) {
            Logger.e(TAG, "Error parsing numbers", e);
        }
    }

    /**
     * Validate user inputs.
     * @return True if inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        String targetUrl = binding.etTargetUrl.getText().toString().trim();
        if (targetUrl.isEmpty() || !(targetUrl.startsWith("http://") || targetUrl.startsWith("https://"))) {
            Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            int minInterval = Integer.parseInt(binding.etMinInterval.getText().toString());
            int maxInterval = Integer.parseInt(binding.etMaxInterval.getText().toString());
            int iterations = Integer.parseInt(binding.etIterations.getText().toString());
            int delayMin = Integer.parseInt(binding.etDelayMin.getText().toString());
            int delayMax = Integer.parseInt(binding.etDelayMax.getText().toString());
            // Add this line to parse airplane mode delay
            int airplaneModeDelay = Integer.parseInt(binding.etAirplaneModeDelay.getText().toString());

            if (minInterval <= 0 || maxInterval <= 0 || iterations <= 0 || delayMin <= 0 || delayMax <= 0 || airplaneModeDelay <= 0) {
                Toast.makeText(this, "Values must be greater than 0", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Add specific validation for airplane mode delay
            if (airplaneModeDelay < 1000) {
                Toast.makeText(this, "Airplane mode delay should be at least 1000ms", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (minInterval > maxInterval) {
                Toast.makeText(this, "Min interval cannot be greater than max interval", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (delayMin > delayMax) {
                Toast.makeText(this, "Min delay cannot be greater than max delay", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Start the simulation.
     */
    private void startSimulation() {
        // Get input values
        String targetUrl = binding.etTargetUrl.getText().toString().trim();
        int iterations = Integer.parseInt(binding.etIterations.getText().toString());
        boolean useRandomDeviceProfile = binding.switchRandomDevices.isChecked();
        boolean rotateIp = binding.switchRotateIp.isChecked();

        // Get timing values
        int minInterval = Integer.parseInt(binding.etMinInterval.getText().toString());
        int maxInterval = Integer.parseInt(binding.etMaxInterval.getText().toString());
        int delayMin = Integer.parseInt(binding.etDelayMin.getText().toString());
        int delayMax = Integer.parseInt(binding.etDelayMax.getText().toString());
        //Airplane-mode settings
        int airplaneModeDelay = Integer.parseInt(binding.etAirplaneModeDelay.getText().toString());
        airplaneModeController.setAirplaneModeDelay(airplaneModeDelay);

        // Update timing distributor settings for legacy code
        timingDistributor.setMinIntervalSeconds(minInterval);
        timingDistributor.setMaxIntervalSeconds(maxInterval);

        // Update UI
        binding.btnStart.setEnabled(false);
        binding.btnStop.setEnabled(true);
        binding.tvStatusLabel.setText("Status: Running");
        binding.tvProgress.setText("Progress: 0/" + iterations);

        // Start tracking elapsed time
        startTimeMs = System.currentTimeMillis();
        updateElapsedTime();
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
        timeUpdateHandler.postDelayed(timeUpdateRunnable, 1000);

        // Clear logs
        clearLogs();

        // Add start log
        addLog("Starting simulation: " + targetUrl + ", " +
                iterations + " iterations, " +
                "Random Devices: " + useRandomDeviceProfile + ", " +
                "Rotate IP: " + rotateIp + ", " +
                "Delays: " + delayMin + "-" + delayMax + "s, " +
                "Airplane Mode Delay: " + airplaneModeDelay + "ms, " +
                "Mode: " + (useWebViewMode ? "WebView" : "HTTP"));
        addLog("Target URL: " + targetUrl);

        // Set up airplane mode listener
        airplaneModeController.setOperationListener(this::showAirplaneModeOperation);

        // Reset airplane mode controller state
        airplaneModeController.resetState();

        // Set up progress observer
        sessionManager.setProgressListener((current, total) -> {
            runOnUiThread(() -> {
                binding.tvProgress.setText("Progress: " + current + "/" + total);

                // Update estimated time remaining
                if (current > 0) {
                    long elapsedMs = System.currentTimeMillis() - startTimeMs;
                    long avgTimePerIteration = elapsedMs / current;
                    long remainingMs = avgTimePerIteration * (total - current);

                    String remainingTime = formatTime(remainingMs);
                    binding.tvTimeRemaining.setText("Estimated time remaining: " + remainingTime);
                }
            });
        });

        // Check if aggressive session clearing is enabled
        boolean isAggressiveClearing =
                preferencesManager.isAggressiveSessionClearingEnabled();

        // Prepare WebView based on session clearing setting
        WebView simulationWebView = isAggressiveClearing
                ? sessionClearingManager.createCleanWebView()
                : new WebView(this);

        // Clear session data if aggressive clearing is on
        if (isAggressiveClearing) {
            sessionClearingManager.clearSessionData(simulationWebView);
        }

        // Start session with custom delay settings
        sessionManager.startSession(
                        targetUrl,
                        iterations,
                        useRandomDeviceProfile,
                        rotateIp,
                        delayMin,
                        delayMax,
                        useWebViewMode) // Pass the WebView mode flag
                .thenRun(() -> {
                    // Update UI when finished
                    runOnUiThread(() -> {
                        binding.btnStart.setEnabled(true);
                        binding.btnStop.setEnabled(false);
                        binding.tvStatusLabel.setText("Status: Completed");
                        addLog("Simulation completed");
                        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
                    });
                })
                .exceptionally(throwable -> {
                    // Handle errors
                    runOnUiThread(() -> {
                        binding.btnStart.setEnabled(true);
                        binding.btnStop.setEnabled(false);
                        binding.tvStatusLabel.setText("Status: Error");
                        addLog("Error: " + throwable.getMessage());
                        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);
                    });
                    return null;
                });

        // Save settings
        preferencesManager.setSimulationRunning(true);
        saveSettings();
    }

    /**
     * Stop the simulation.
     */
    private void stopSimulation() {
        sessionManager.stopSession();

        // Update UI
        binding.btnStart.setEnabled(true);
        binding.btnStop.setEnabled(false);
        binding.tvStatusLabel.setText("Status: Stopped");
        addLog("Simulation stopped");
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable);

        // Save settings
        preferencesManager.setSimulationRunning(false);

        // Reset controller state to ensure clean state for next run
        airplaneModeController.resetState();
    }

    /**
     * Update UI based on session state.
     */
    private void updateUIBasedOnSessionState() {
        if (preferencesManager.isSimulationRunning() && !sessionManager.isRunning()) {
            // Simulation was running but stopped (app restart)
            binding.tvStatusLabel.setText("Status: Stopped (App Restarted)");
            binding.btnStart.setEnabled(true);
            binding.btnStop.setEnabled(false);
            preferencesManager.setSimulationRunning(false);
            addLog("Detected interrupted simulation - ready to restart");

            // Reset controller state to ensure clean state
            airplaneModeController.resetState();
        } else if (sessionManager.isRunning()) {
            // Simulation is running
            binding.btnStart.setEnabled(false);
            binding.btnStop.setEnabled(true);
            binding.tvStatusLabel.setText("Status: Running");
        } else {
            // No simulation running
            binding.btnStart.setEnabled(true);
            binding.btnStop.setEnabled(false);
            binding.tvStatusLabel.setText("Status: Ready");
        }
    }

    /**
     * Update elapsed time in UI.
     */
    private void updateElapsedTime() {
        if (startTimeMs > 0) {
            long elapsedMs = System.currentTimeMillis() - startTimeMs;
            binding.tvTimeElapsed.setText("Time elapsed: " + formatTime(elapsedMs));
        }
    }

    /**
     * Add a log message to the log view.
     * @param message Log message
     */
    private void addLog(String message) {
        String timestamp = Logger.formatTime(System.currentTimeMillis());
        String logMessage = timestamp + " | " + message + "\n";

        runOnUiThread(() -> {
            binding.tvLogs.append(logMessage);

            // Find the logs NestedScrollView by ID
            NestedScrollView logsScrollView = findViewById(R.id.logsScrollView);
            if (logsScrollView != null) {
                // Post to ensure it happens after layout is complete
                logsScrollView.post(() -> logsScrollView.fullScroll(View.FOCUS_DOWN));
            }
        });
    }

    /**
     * Format time in milliseconds to human-readable string.
     * @param timeMs Time in milliseconds
     * @return Formatted time string (HH:MM:SS)
     */
    private String formatTime(long timeMs) {
        long seconds = (timeMs / 1000) % 60;
        long minutes = (timeMs / (1000 * 60)) % 60;
        long hours = (timeMs / (1000 * 60 * 60));

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Method to show airplane mode operation status.
     */
    private void showAirplaneModeOperation(boolean isOperating) {
        runOnUiThread(() -> {
            if (isOperating) {
                binding.tvStatusLabel.setText("Status: Rotating IP...");
                addLog("IP rotation in progress");
                // Could add a progress indicator here if desired
            } else {
                binding.tvStatusLabel.setText("Status: Running");
                addLog("IP rotation completed");
            }
        });
    }



    /**
     * Clear all logs.
     */
    private void clearLogs() {
        binding.tvLogs.setText("");
    }
}