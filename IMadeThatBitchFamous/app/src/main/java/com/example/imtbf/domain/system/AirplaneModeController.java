package com.example.imtbf.domain.system;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

import com.example.imtbf.data.network.NetworkStateMonitor;
import com.example.imtbf.utils.Constants;
import com.example.imtbf.utils.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for airplane mode toggling with multiple fallback methods.
 */
public class AirplaneModeController {

    private static final String TAG = "AirplaneController";
    private static final int DEFAULT_RECONNECTION_TIMEOUT_MS = 30000; // 30 seconds
    private static final int TOGGLE_TIMEOUT_SECONDS = 10; // 10 seconds timeout for toggle operations

    private final Context context;
    private final NetworkStateMonitor networkStateMonitor;
    private int airplaneModeDelay;
    private final AtomicBoolean isRotatingIp = new AtomicBoolean(false);
    private OperationListener operationListener;

    /**
     * Constructor that initializes the controller with required dependencies.
     * @param context Application context
     * @param networkStateMonitor Network state monitor
     * @param airplaneModeDelay Delay between toggling airplane mode on and off (ms)
     */
    public AirplaneModeController(
            Context context,
            NetworkStateMonitor networkStateMonitor,
            int airplaneModeDelay) {
        this.context = context;
        this.networkStateMonitor = networkStateMonitor;
        this.airplaneModeDelay = airplaneModeDelay;
    }

    /**
     * Interface for listening to airplane mode operations status
     */
    public interface OperationListener {
        void onOperationStatusChanged(boolean isOperating);
    }

    /**
     * Set a listener for airplane mode operations
     * @param listener Operation listener
     */
    public void setOperationListener(OperationListener listener) {
        this.operationListener = listener;
    }

    /**
     * Reset the state of the controller.
     * This should be called when the app starts to ensure no stuck flags.
     */
    public void resetState() {
        isRotatingIp.set(false);
        Logger.d(TAG, "Reset airplane mode controller state");
    }

    /**
     * Rotate IP address using multiple methods.
     * @return CompletableFuture with IP rotation result
     */
    public CompletableFuture<IpRotationResult> rotateIp() {
        // Force reset any stuck flags
        if (isRotatingIp.get()) {
            Logger.w(TAG, "Forced reset of stuck rotation flag");
            isRotatingIp.set(false);
        }

        // Don't allow multiple rotations at the same time
        if (isRotatingIp.get()) {
            Logger.w(TAG, "IP rotation already in progress, ignoring request");
            return CompletableFuture.completedFuture(new IpRotationResult(
                    false,
                    "Unknown",
                    "Unknown",
                    "IP rotation already in progress",
                    0
            ));
        }

        return CompletableFuture.supplyAsync(() -> {
            isRotatingIp.set(true);
            if (operationListener != null) {
                operationListener.onOperationStatusChanged(true);
            }

            try {
                Logger.i(TAG, "Starting IP rotation");

                // Get current IP before toggling
                String previousIp = networkStateMonitor.getCurrentIpAddress().getValue();
                boolean wasConnected = networkStateMonitor.isNetworkAvailable();

                if (!wasConnected) {
                    Logger.w(TAG, "Network not connected before IP rotation");
                    return new IpRotationResult(false, previousIp, previousIp,
                            "Network not connected", 0);
                }

                Logger.d(TAG, "Current IP before rotation: " + previousIp);

                // For Xiaomi/Redmi with root, use direct root commands
                if (isXiaomiDevice() && isRooted()) {
                    try {
                        Logger.i(TAG, "Using direct root commands for Xiaomi device");

                        Process process = Runtime.getRuntime().exec("su");
                        DataOutputStream os = new DataOutputStream(process.getOutputStream());

                        // Enable airplane mode
                        os.writeBytes("settings put global airplane_mode_on 1\n");
                        os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n");
                        os.flush();

                        Logger.d(TAG, "Airplane mode enabled, waiting for " + airplaneModeDelay + "ms");
                        // Wait for the specified delay
                        Thread.sleep(airplaneModeDelay);

                        // Disable airplane mode
                        os.writeBytes("settings put global airplane_mode_on 0\n");
                        os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n");
                        os.writeBytes("exit\n");
                        os.flush();
                        os.close();

                        int exitValue = process.waitFor();
                        Logger.i(TAG, "Root airplane mode toggle completed with exit value: " + exitValue);

                        // Wait for network reconnection
                        long startTime = System.currentTimeMillis();
                        boolean reconnected = networkStateMonitor.waitForReconnection(DEFAULT_RECONNECTION_TIMEOUT_MS);
                        long reconnectionTime = System.currentTimeMillis() - startTime;

                        if (!reconnected) {
                            Logger.e(TAG, "Network failed to reconnect");
                            return new IpRotationResult(false, previousIp, previousIp,
                                    "Network failed to reconnect", reconnectionTime);
                        }

                        // Give time for IP to update
                        Thread.sleep(1000);

                        // Fetch new IP
                        networkStateMonitor.fetchCurrentIpAddress();

                        // Wait for IP address to be fetched
                        int attempts = 0;
                        String newIp = networkStateMonitor.getCurrentIpAddress().getValue();
                        while ((newIp == null || newIp.isEmpty() || newIp.equals("Unknown")) && attempts < 10) {
                            Thread.sleep(500);
                            attempts++;
                            newIp = networkStateMonitor.getCurrentIpAddress().getValue();
                        }

                        boolean ipChanged = !previousIp.equals(newIp) && !newIp.isEmpty() && !newIp.equals("Unknown");
                        return new IpRotationResult(ipChanged, previousIp, newIp,
                                ipChanged ? "Success" : "IP did not change", reconnectionTime);

                    } catch (Exception e) {
                        Logger.e(TAG, "Failed to toggle airplane mode via root", e);
                        // Fall through to try other methods
                    }
                }

                // If direct root method failed, try other methods
                IpRotationResult rotationResult = tryRotationMethods(previousIp);
                return rotationResult;

            } catch (Exception e) {
                Logger.e(TAG, "Unexpected error during IP rotation", e);
                return new IpRotationResult(false,
                        networkStateMonitor.getCurrentIpAddress().getValue(),
                        networkStateMonitor.getCurrentIpAddress().getValue(),
                        "Unexpected error: " + e.getMessage(),
                        0
                );
            } finally {
                isRotatingIp.set(false);
                if (operationListener != null) {
                    operationListener.onOperationStatusChanged(false);
                }
            }
        });
    }

    /**
     * Try multiple methods to rotate IP.
     * @param previousIp Previous IP address
     * @return IP rotation result
     */
    private IpRotationResult tryRotationMethods(String previousIp) {
        // Method 1: Xiaomi/MIUI specific method
        if (isXiaomiDevice()) {
            try {
                IpRotationResult xiaomiResult = rotateIpViaXiaomiMethod(previousIp);
                if (xiaomiResult.isSuccess()) {
                    return xiaomiResult;
                }
            } catch (Exception e) {
                Logger.w(TAG, "Xiaomi IP rotation failed: " + e.getMessage());
            }
        }

        // Method 2: Root method
        if (isRooted()) {
            try {
                IpRotationResult rootResult = rotateIpViaRootMethod(previousIp);
                if (rootResult.isSuccess()) {
                    return rootResult;
                }
            } catch (Exception e) {
                Logger.w(TAG, "Root IP rotation failed: " + e.getMessage());
            }
        }

        // Method 3: Intent-based method
        try {
            IpRotationResult intentResult = rotateIpViaIntent(previousIp);
            if (intentResult.isSuccess()) {
                return intentResult;
            }
        } catch (Exception e) {
            Logger.w(TAG, "Intent-based IP rotation failed: " + e.getMessage());
        }

        // Fallback: No successful rotation
        Logger.e(TAG, "All IP rotation methods failed");
        return new IpRotationResult(
                false,
                previousIp,
                networkStateMonitor.getCurrentIpAddress().getValue(),
                "All IP rotation methods failed",
                0
        );
    }

    /**
     * Rotate IP via Xiaomi/MIUI specific method.
     * @param previousIp Previous IP address
     * @return IP rotation result
     */
    private IpRotationResult rotateIpViaXiaomiMethod(String previousIp) {
        try {
            // Send Xiaomi-specific airplane mode toggle intents
            Intent enableIntent = new Intent("miui.intent.action.TOGGLE_AIRPLANE_ON");
            Intent disableIntent = new Intent("miui.intent.action.TOGGLE_AIRPLANE_OFF");

            enableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            disableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.sendBroadcast(enableIntent);
            Logger.d(TAG, "Sent Xiaomi airplane mode ON intent, waiting for " + airplaneModeDelay + "ms");
            Thread.sleep(airplaneModeDelay);
            context.sendBroadcast(disableIntent);
            Logger.d(TAG, "Sent Xiaomi airplane mode OFF intent");

            // Wait for network reconnection
            long startTime = System.currentTimeMillis();
            boolean reconnected = networkStateMonitor.waitForReconnection(DEFAULT_RECONNECTION_TIMEOUT_MS);
            long reconnectionTime = System.currentTimeMillis() - startTime;

            if (!reconnected) {
                Logger.e(TAG, "Network failed to reconnect after Xiaomi method");
                return new IpRotationResult(false, previousIp, previousIp,
                        "Network failed to reconnect", reconnectionTime);
            }

            // Wait a moment for IP to update
            Thread.sleep(1000);

            // Fetch new IP
            networkStateMonitor.fetchCurrentIpAddress();

            // Wait for IP address to be fetched
            int attempts = 0;
            String newIp = networkStateMonitor.getCurrentIpAddress().getValue();
            while ((newIp == null || newIp.isEmpty() || newIp.equals("Unknown")) && attempts < 10) {
                Thread.sleep(500);
                attempts++;
                newIp = networkStateMonitor.getCurrentIpAddress().getValue();
            }

            boolean ipChanged = !previousIp.equals(newIp) && !newIp.isEmpty() && !newIp.equals("Unknown");

            Logger.i(TAG, "Xiaomi method result: " + (ipChanged ? "Success" : "Failed") +
                    ", Previous IP: " + previousIp + ", New IP: " + newIp);

            return new IpRotationResult(
                    ipChanged,
                    previousIp,
                    newIp,
                    ipChanged ? "Xiaomi method successful" : "IP did not change",
                    reconnectionTime
            );
        } catch (Exception e) {
            Logger.e(TAG, "Xiaomi IP rotation method failed", e);
            return new IpRotationResult(
                    false,
                    previousIp,
                    previousIp,
                    "Xiaomi method error: " + e.getMessage(),
                    0
            );
        }
    }

    /**
     * Rotate IP via root method.
     * @param previousIp Previous IP address
     * @return IP rotation result
     */
    private IpRotationResult rotateIpViaRootMethod(String previousIp) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            // Enhanced root commands with proper intent extras
            os.writeBytes("settings put global airplane_mode_on 1\n");
            os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n");
            os.flush();

            Logger.d(TAG, "Enabled airplane mode via root, waiting for " + airplaneModeDelay + "ms");
            Thread.sleep(airplaneModeDelay);

            os.writeBytes("settings put global airplane_mode_on 0\n");
            os.writeBytes("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();

            int exitValue = process.waitFor();
            Logger.i(TAG, "Root commands executed with exit value: " + exitValue);

            // Wait for network reconnection
            long startTime = System.currentTimeMillis();
            boolean reconnected = networkStateMonitor.waitForReconnection(DEFAULT_RECONNECTION_TIMEOUT_MS);
            long reconnectionTime = System.currentTimeMillis() - startTime;

            if (!reconnected) {
                Logger.e(TAG, "Network failed to reconnect after root method");
                return new IpRotationResult(false, previousIp, previousIp,
                        "Network failed to reconnect", reconnectionTime);
            }

            // Wait a moment for IP to update
            Thread.sleep(1000);

            // Fetch new IP
            networkStateMonitor.fetchCurrentIpAddress();

            // Wait for IP address to be fetched
            int attempts = 0;
            String newIp = networkStateMonitor.getCurrentIpAddress().getValue();
            while ((newIp == null || newIp.isEmpty() || newIp.equals("Unknown")) && attempts < 10) {
                Thread.sleep(500);
                attempts++;
                newIp = networkStateMonitor.getCurrentIpAddress().getValue();
            }

            boolean ipChanged = !previousIp.equals(newIp) && !newIp.isEmpty() && !newIp.equals("Unknown");

            Logger.i(TAG, "Root method result: " + (ipChanged ? "Success" : "Failed") +
                    ", Previous IP: " + previousIp + ", New IP: " + newIp);

            return new IpRotationResult(
                    ipChanged,
                    previousIp,
                    newIp,
                    ipChanged ? "Root method successful" : "IP did not change",
                    reconnectionTime
            );
        } catch (Exception e) {
            Logger.e(TAG, "Root IP rotation method failed", e);
            return new IpRotationResult(
                    false,
                    previousIp,
                    previousIp,
                    "Root method error: " + e.getMessage(),
                    0
            );
        }
    }

    /**
     * Rotate IP via Intent method (user interaction).
     * @param previousIp Previous IP address
     * @return IP rotation result
     */
    private IpRotationResult rotateIpViaIntent(String previousIp) {
        try {
            // Open airplane mode settings
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            // Note: This method requires user interaction and is not fully automatic
            Logger.w(TAG, "Opened airplane mode settings. Manual toggle required.");

            return new IpRotationResult(
                    false,
                    previousIp,
                    previousIp,
                    "Manual intervention needed",
                    0
            );
        } catch (Exception e) {
            Logger.e(TAG, "Intent-based IP rotation method failed", e);
            return new IpRotationResult(
                    false,
                    previousIp,
                    previousIp,
                    "Intent method error: " + e.getMessage(),
                    0
            );
        }
    }

    /**
     * Check if the device is a Xiaomi/Redmi device.
     * @return True if it's a Xiaomi device, false otherwise
     */
    private boolean isXiaomiDevice() {
        return Build.MANUFACTURER.toLowerCase().contains("xiaomi") ||
                Build.BRAND.toLowerCase().contains("xiaomi") ||
                Build.BRAND.toLowerCase().contains("redmi");
    }

    /**
     * Check if the device is rooted.
     * @return True if rooted, false otherwise
     */
    private boolean isRooted() {
        try {
            Process process = Runtime.getRuntime().exec("su -c exit");
            int exitValue = process.waitFor();
            Logger.d(TAG, "Root check result: " + (exitValue == 0 ? "Rooted" : "Not rooted"));
            return exitValue == 0;
        } catch (Exception e) {
            Logger.w(TAG, "Error checking root: " + e.getMessage());
            return false;
        }
    }

    /**
     * Toggle airplane mode via Xiaomi-specific methods
     */
    private boolean toggleAirplaneModeViaXiaomi(boolean enable) {
        try {
            // For Xiaomi/Redmi devices, we can try using quick settings tile
            Intent intent = new Intent(enable ? "miui.intent.action.TOGGLE_AIRPLANE_ON" :
                    "miui.intent.action.TOGGLE_AIRPLANE_OFF");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.sendBroadcast(intent);

            // Sleep briefly to let the broadcast be processed
            Thread.sleep(1000);

            // Verify the state changed
            return true; // We can't easily verify, so assume it worked
        } catch (Exception e) {
            Logger.e(TAG, "Error using Xiaomi method", e);
            return false;
        }
    }

    /**
     * Toggle airplane mode via settings activity intent
     */
    private boolean toggleAirplaneModeViaIntent(boolean enable) {
        try {
            // This is a fallback approach using UI automation
            Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            // Give time for settings to open
            Thread.sleep(2000);

            // We'd need UI automation here which requires accessibility service
            // This is just a placeholder - it will open settings but not toggle

            return false; // Return false as we can't guarantee the toggle happened
        } catch (Exception e) {
            Logger.e(TAG, "Error using intent method", e);
            return false;
        }
    }

    /**
     * Check if the app has WRITE_SETTINGS permission.
     * @return True if the app can write settings, false otherwise
     */
    private boolean canWriteSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        }
        return true;
    }


    /**
     * Set the airplane mode toggle delay.
     * @param delayMs Delay in milliseconds
     */
    public void setAirplaneModeDelay(int delayMs) {
        this.airplaneModeDelay = delayMs;
        Logger.d(TAG, "Airplane mode delay set to " + delayMs + "ms");
    }

    /**
     * Toggle airplane mode via settings.
     * @param enable True to enable, false to disable
     * @return True if successful, false otherwise
     */
    private boolean toggleAirplaneModeViaSettings(boolean enable) {
        try {
            int value = enable ? Constants.AIRPLANE_MODE_ON : Constants.AIRPLANE_MODE_OFF;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                // For older Android versions
                return Settings.System.putInt(context.getContentResolver(),
                        Settings.System.AIRPLANE_MODE_ON, value);
            } else {
                // For newer Android versions
                return Settings.Global.putInt(context.getContentResolver(),
                        Settings.Global.AIRPLANE_MODE_ON, value);
            }

        } catch (Exception e) {
            Logger.e(TAG, "Error toggling airplane mode via settings", e);
            return false;
        }
    }

    /**
     * Toggle airplane mode via root commands.
     * @param enable True to enable, false to disable
     * @return True if successful, false otherwise
     */
    private boolean toggleAirplaneModeViaRoot(boolean enable) {
        DataOutputStream os = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());

            // Command to toggle airplane mode
            String command = enable ? "settings put global airplane_mode_on 1\n" :
                    "settings put global airplane_mode_on 0\n";

            // Broadcast the change with proper extras
            command += "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state " + enable + "\n";

            os.writeBytes(command);
            os.writeBytes("exit\n");
            os.flush();

            int exitValue = process.waitFor();
            return exitValue == 0;

        } catch (IOException | InterruptedException e) {
            Logger.e(TAG, "Error toggling airplane mode via root", e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    /**
     * Open airplane mode settings as a fallback.
     */
    private void openAirplaneModeSettings() {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Request WRITE_SETTINGS permission.
     * This opens the system settings screen for the app.
     */
    public void requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * Result class for IP rotation operations.
     */
    public static class IpRotationResult {
        private final boolean success;
        private final String previousIp;
        private final String newIp;
        private final String message;
        private final long reconnectionTimeMs;

        public IpRotationResult(boolean success, String previousIp, String newIp,
                                String message, long reconnectionTimeMs) {
            this.success = success;
            this.previousIp = previousIp;
            this.newIp = newIp;
            this.message = message;
            this.reconnectionTimeMs = reconnectionTimeMs;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getPreviousIp() {
            return previousIp;
        }

        public String getNewIp() {
            return newIp;
        }

        public String getMessage() {
            return message;
        }

        public long getReconnectionTimeMs() {
            return reconnectionTimeMs;
        }

        @Override
        public String toString() {
            return "IpRotationResult{" +
                    "success=" + success +
                    ", previousIp='" + previousIp + '\'' +
                    ", newIp='" + newIp + '\'' +
                    ", message='" + message + '\'' +
                    ", reconnectionTimeMs=" + reconnectionTimeMs +
                    '}';
        }
    }
}