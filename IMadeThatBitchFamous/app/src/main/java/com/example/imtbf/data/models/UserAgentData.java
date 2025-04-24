package com.example.imtbf.data.models;

import com.example.imtbf.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Contains comprehensive data about user agents.
 * This class is used to manage and retrieve user agents based on various criteria.
 */
public class UserAgentData {

    private static final Random random = new Random();

    // Maps to store user agents by different criteria
    private static final Map<String, List<String>> deviceTypeUserAgents = new HashMap<>();
    private static final Map<String, List<String>> platformUserAgents = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> deviceTierUserAgents = new HashMap<>();
    private static final Map<String, List<String>> browserUserAgents = new HashMap<>();
    private static final Map<String, Map<String, Float>> demographicDistributions = new HashMap<>();

    // Lists of user agents by category
    private static final List<String> androidUserAgents = new ArrayList<>();
    private static final List<String> iosUserAgents = new ArrayList<>();
    private static final List<String> windowsUserAgents = new ArrayList<>();
    private static final List<String> macUserAgents = new ArrayList<>();
    private static final List<String> instagramAppUserAgents = new ArrayList<>();

    // Initialize with some sample user agents - this would be expanded in a real implementation
    static {
        // Initialize Android user agents
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; SM-S918B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; ONEPLUS A6013) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 12; moto g(60)) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; M2101K6G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36");

        // Initialize iOS user agents
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 17_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone14,7; U; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_6_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 15_7_8 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.7 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPad; CPU OS 17_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1");

        // Initialize Windows user agents
        windowsUserAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
        windowsUserAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/112.0");
        windowsUserAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.58");
        windowsUserAgents.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 OPR/98.0.0.0");
        windowsUserAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");

        // Initialize Mac user agents
        macUserAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Safari/605.1.15");
        macUserAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");
        macUserAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 Edg/112.0.1722.58");
        macUserAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/112.0");
        macUserAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36 OPR/98.0.0.0");

        // 2015-2016 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 5.0; SM-G900F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.127 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 6.0.1; SM-G920F Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 6.0; HTC One M9 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 5.0.2; LG-D855 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.89 Mobile Safari/537.36");

// 2016-2017 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 7.0; SAMSUNG SM-G930F Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/6.2 Chrome/56.0.2924.87 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 7.0; HUAWEI VNS-L31 Build/HUAWEIVNS-L31) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.83 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 7.1.1; Pixel Build/NMF26O) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.132 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 6.0.1; SM-A510F Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 7.0; SM-G935F Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.116 Mobile Safari/537.36");

// 2017-2018 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 8.0.0; SM-G950F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.137 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 8.0.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.109 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 8.1.0; Nokia 7 plus Build/OPR1.170623.026) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.126 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 8.0.0; ONEPLUS A5000 Build/OPR1.170623.032) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 8.0.0; MI 6 Build/OPR1.170623.027) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.91 Mobile Safari/537.36");

// 2018-2019 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 9; SM-G965F Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 9; Pixel 3 Build/PQ1A.190105.004) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.90 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 9; ONEPLUS A6003 Build/PKQ1.180716.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 9; SM-N960F Build/PPR1.180610.011) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.101 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 9; Honor 10 Build/PKQ1.180634.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Mobile Safari/537.36");

// 2019-2020 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 10; SM-G975F Build/QP1A.190711.020) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 10; Pixel 4 Build/QD1A.190821.014.C2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.93 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 10; Mi 9T Pro Build/QKQ1.190825.002) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 10; ONEPLUS A6013 Build/QKQ1.190716.003) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 10; VOG-L29 Build/HUAWEIVOG-L29) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Mobile Safari/537.36");

// 2020-2021 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 11; SM-G981B Build/RP1A.200720.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 11; Pixel 5 Build/RD1A.201105.003) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.101 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 11; ONEPLUS A7T Build/RKQ1.201022.002) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.181 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 11; Mi 10 Pro Build/RKQ1.200826.002) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.105 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 11; M2103K19G Build/RKQ1.200826.002) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Mobile Safari/537.36");

// 2021-2022 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 12; SM-G991B Build/SP1A.210812.016) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 12; Pixel 6 Build/SD1A.210817.036) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.98 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 12; ONEPLUS LE2123 Build/SKQ1.210216.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.101 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 12; M2102J20SG Build/SKQ1.210908.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.73 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 12; Nokia X20 Build/SKQ1.210821.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Mobile Safari/537.36");

// 2022-2023 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; SM-S901B Build/TP1A.220624.014) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; Pixel 7 Build/TD1A.220804.031) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; 21081111RG Build/TP1A.220624.014) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; CPH2409 Build/TP1A.220905.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 13; XQ-BE72 Build/61.2.F.0.208) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36");

// 2023-2024 Android Devices
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 14; SM-S918B Build/UP1A.231005.007) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro Build/UPB0.230908.017) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 14; 23049RAD8C Build/UP1A.231005.007) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 14; CPH2527 Build/UP1A.231005.007) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 14; Xperia 1 V Build/UP1A.231005.007) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36");

// 2024-2025 Android Devices (Projected)
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 15; SM-S928B Build/VOA1A.240430.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 15; Pixel 9 Pro Build/VP1A.240624.002) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 15; 24045RPACG Build/VOA1A.240430.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 15; ONEPLUS HD2201 Build/VOA1A.240430.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36");
        androidUserAgents.add("Mozilla/5.0 (Linux; Android 15; Nothing A451 Build/VOA1A.240430.001) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Mobile Safari/537.36");

        // 2015-2016 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_2 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13F69 Safari/601.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G36 Safari/601.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A403 Safari/602.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_1_1 like Mac OS X) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0 Mobile/14B100 Safari/602.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2 like Mac OS X) AppleWebKit/602.3.12 (KHTML, like Gecko) Version/10.0 Mobile/14C92 Safari/602.1");

// 2016-2017 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/603.2.4 (KHTML, like Gecko) Version/10.0 Mobile/14F89 Safari/602.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_3 like Mac OS X) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.0 Mobile/14G60 Safari/602.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_1 like Mac OS X) AppleWebKit/604.3.5 (KHTML, like Gecko) Version/11.0 Mobile/15B93 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_2 like Mac OS X) AppleWebKit/604.4.7 (KHTML, like Gecko) Version/11.0 Mobile/15C114 Safari/604.1");

// 2017-2018 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 11_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.0 Mobile/15F79 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1 Mobile/15E148 Safari/604.1");

// 2018-2019 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");

// 2019-2020 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.4 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 13_7 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Mobile/15E148 Safari/604.1");

// 2020-2021 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 14_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 14_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 14_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 14_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.1 Mobile/15E148 Safari/604.1");

// 2021-2022 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 15_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 15_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 15_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.3 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 15_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.4 Mobile/15E148 Safari/604.1");

// 2022-2023 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.3 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 16_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.4 Mobile/15E148 Safari/604.1");

// 2023-2024 iOS Devices
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 17_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 17_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 17_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 17_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.4 Mobile/15E148 Safari/604.1");

// 2024-2025 iOS Devices (Projected)
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 18_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.0 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 18_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.1 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 18_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.2 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 18_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.3 Mobile/15E148 Safari/604.1");
        iosUserAgents.add("Mozilla/5.0 (iPhone; CPU iPhone OS 18_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.4 Mobile/15E148 Safari/604.1");

        // 2015-2017 Instagram App Agents (Android)
        instagramAppUserAgents.add("Instagram 10.14.0 Android (24/7.0; 480dpi; 1080x1920; HUAWEI; VNS-L31; HWVNS-H; hi6250; sk_SK; 162478517)");
        instagramAppUserAgents.add("Instagram 25.0.0.26.136 Android (23/6.0.1; 480dpi; 1080x1920; Samsung; SM-G935F; hero2lte; samsungexynos8890; sk_SK; 138226743)");
        instagramAppUserAgents.add("Instagram 42.0.0.19.95 Android (25/7.1.1; 480dpi; 1080x1920; Google; Pixel; pixel; qcom; sk_SK; 150804623)");
        instagramAppUserAgents.add("Instagram 55.0.0.12.79 Android (24/7.0; 420dpi; 1080x1920; Xiaomi; Mi 5s; capricorn; qcom; sk_SK; 118229529)");
        instagramAppUserAgents.add("Instagram 64.0.0.14.96 Android (24/7.0; 320dpi; 720x1280; HUAWEI; CAN-L01; HWCAN; hi6250; sk_SK; 125112142)");

// 2017-2019 Instagram App Agents (Android)
        instagramAppUserAgents.add("Instagram 76.0.0.15.395 Android (26/8.0.0; 480dpi; 1080x2220; HUAWEI; ANE-LX1; HWANE; hi6250; sk_SK; 138226743)");
        instagramAppUserAgents.add("Instagram 91.0.0.18.118 Android (28/9.0; 420dpi; 1080x2094; Xiaomi; MI 8; dipper; qcom; sk_SK; 154400278)");
        instagramAppUserAgents.add("Instagram 103.1.0.15.119 Android (28/9.0; 440dpi; 1080x2246; Xiaomi; POCO F1; beryllium; qcom; sk_SK; 164094540)");
        instagramAppUserAgents.add("Instagram 118.0.0.28.122 Android (28/9.0; 440dpi; 1080x2340; samsung; SM-A705FN; a70q; qcom; sk_SK; 180322800)");
        instagramAppUserAgents.add("Instagram 128.0.0.19.128 Android (28/9.0; 420dpi; 1080x2160; Nokia; Nokia 7.1; PL2; qcom; sk_SK; 195435559)");

// 2019-2021 Instagram App Agents (Android)
        instagramAppUserAgents.add("Instagram 146.0.0.27.125 Android (29/10.0; 420dpi; 1080x2220; OnePlus; ONEPLUS A6013; OnePlus6T; qcom; sk_SK; 223928353)");
        instagramAppUserAgents.add("Instagram 159.0.0.40.122 Android (29/10.0; 440dpi; 1080x2312; Xiaomi; Redmi Note 8 Pro; begonia; mt6785; sk_SK; 246559739)");
        instagramAppUserAgents.add("Instagram 169.3.0.30.135 Android (29/10.0; 440dpi; 1080x2134; samsung; SM-A515F; a51; exynos9611; sk_SK; 262886843)");
        instagramAppUserAgents.add("Instagram 177.0.0.30.119 Android (30/11.0; 480dpi; 1080x2400; Samsung; SM-A515F; a51; exynos9611; sk_SK; 276028020)");
        instagramAppUserAgents.add("Instagram 187.0.0.32.120 Android (30/11.0; 420dpi; 1080x2400; Xiaomi; M2007J20CG; gauguinpro; mt6885; sk_SK; 289692181)");

// 2021-2022 Instagram App Agents (Android)
        instagramAppUserAgents.add("Instagram 198.0.0.32.120 Android (30/11.0; 440dpi; 1080x2400; Nokia; Nokia 5.4; DoctorStrange; qcom; sk_SK; 307053212)");
        instagramAppUserAgents.add("Instagram 212.0.0.38.119 Android (30/11.0; 420dpi; 1080x2400; Xiaomi; M2101K6G; sweet; qcom; sk_SK; 329518503)");
        instagramAppUserAgents.add("Instagram 226.0.0.22.113 Android (31/12.0; 440dpi; 1080x2400; Samsung; SM-A525F; a52q; qcom; sk_SK; 356336146)");
        instagramAppUserAgents.add("Instagram 239.0.0.21.108 Android (31/12.0; 440dpi; 1080x2400; OPPO; CPH2211; OP4F39L1; qcom; sk_SK; 377299313)");
        instagramAppUserAgents.add("Instagram 248.0.0.15.109 Android (31/12.0; 420dpi; 1080x2400; Motorola; moto g52; rhode; qcom; sk_SK; 391374857)");

// 2022-2023 Instagram App Agents (Android)
        instagramAppUserAgents.add("Instagram 262.0.0.24.101 Android (32/12.0; 420dpi; 1080x2400; Nothing; A063; Spacewar; qcom; sk_SK; 415339925)");
        instagramAppUserAgents.add("Instagram 272.0.0.20.105 Android (32/12.0; 440dpi; 1080x2400; Samsung; SM-A536B; a53x; exynos1280; sk_SK; 432439037)");
        instagramAppUserAgents.add("Instagram 280.0.0.16.114 Android (33/13.0; 440dpi; 1080x2400; Google; Pixel 7a; lynx; qcom; sk_SK; 448818488)");
        instagramAppUserAgents.add("Instagram 288.0.0.22.111 Android (33/13.0; 420dpi; 1080x2400; Xiaomi; Redmi Note 12; tapas; qcom; sk_SK; 455613460)");
        instagramAppUserAgents.add("Instagram 299.0.0.33.109 Android (33/13.0; 440dpi; 1080x2400; Motorola; motorola edge 40; hiphi; mt6879; sk_SK; 472304341)");

// 2023-2024 Instagram App Agents (Android)
        instagramAppUserAgents.add("Instagram 309.0.0.18.106 Android (33/13.0; 440dpi; 1080x2400; Xiaomi; 23030RAC7Y; ruby; mt6877t; sk_SK; 487445903)");
        instagramAppUserAgents.add("Instagram 322.0.0.40.117 Android (33/13.0; 420dpi; 1080x2400; Samsung; SM-A346B; a34x; samsungexynos1280; sk_SK; 512987341)");
        instagramAppUserAgents.add("Instagram 334.0.0.41.102 Android (34/14.0; 440dpi; 1080x2400; OnePlus; CPH2507; OP5958L1; qcom; sk_SK; 531654322)");
        instagramAppUserAgents.add("Instagram 343.1.0.23.105 Android (34/14.0; 480dpi; 1080x2400; Google; Pixel 8; shiba; qcom; sk_SK; 545432987)");
        instagramAppUserAgents.add("Instagram 355.0.0.25.114 Android (34/14.0; 440dpi; 1080x2400; Nothing; A065; Pong; qcom; sk_SK; 557891234)");

// 2024-2025 Instagram App Agents (Android) - Projected
        instagramAppUserAgents.add("Instagram 368.0.0.22.109 Android (34/14.0; 440dpi; 1080x2400; Samsung; SM-A556B; a55x; samsungexynos1480; sk_SK; 571239856)");
        instagramAppUserAgents.add("Instagram 373.0.0.26.104 Android (35/15.0; 440dpi; 1080x2400; Xiaomi; 24046RPACG; zircon; qcom; sk_SK; 578912345)");
        instagramAppUserAgents.add("Instagram 384.0.0.18.112 Android (35/15.0; 480dpi; 1200x2640; Google; Pixel 9; husky; qcom; sk_SK; 592341786)");
        instagramAppUserAgents.add("Instagram 391.0.0.24.101 Android (35/15.0; 440dpi; 1080x2400; Nothing; A142; Pacman; qcom; sk_SK; 603214587)");
        instagramAppUserAgents.add("Instagram 396.0.0.31.108 Android (35/15.0; 440dpi; 1080x2400; OnePlus; HD2401; OP7F39L1; qcom; sk_SK; 612345789)");

// 2015-2017 Instagram App Agents (iOS)
        instagramAppUserAgents.add("Instagram 10.26.0 iOS (10_2; iPhone8,1; sk_SK; sk-SK; scale=2.00; 750x1334; 65254535)");
        instagramAppUserAgents.add("Instagram 24.0.0.12.201 iOS (10_3_3; iPhone9,3; sk_SK; sk-SK; scale=2.00; 750x1334; 138179672)");
        instagramAppUserAgents.add("Instagram 39.0.0.14.93 iOS (11_1; iPhone10,1; sk_SK; sk-SK; scale=2.00; 750x1334; 151048500)");
        instagramAppUserAgents.add("Instagram 55.0.0.12.71 iOS (11_2_5; iPhone8,2; sk_SK; sk-SK; scale=2.61; 1080x1920; 123136533)");
        instagramAppUserAgents.add("Instagram 66.0.0.11.101 iOS (11_4; iPhone7,2; sk_SK; sk-SK; scale=2.00; 750x1334; 129251246)");

// 2017-2019 Instagram App Agents (iOS)
        instagramAppUserAgents.add("Instagram 78.0.0.8.113 iOS (12_0; iPhone10,3; sk_SK; sk-SK; scale=3.00; 1125x2436; 140542248)");
        instagramAppUserAgents.add("Instagram 95.0.0.21.124 iOS (12_1; iPhone11,2; sk_SK; sk-SK; scale=3.00; 1125x2436; 157747317)");
        instagramAppUserAgents.add("Instagram 104.0.0.21.118 iOS (12_3_1; iPhone9,1; sk_SK; sk-SK; scale=2.00; 750x1334; 165661507)");
        instagramAppUserAgents.add("Instagram 118.0.0.25.121 iOS (12_4; iPhone10,6; sk_SK; sk-SK; scale=3.00; 1125x2436; 181380493)");
        instagramAppUserAgents.add("Instagram 128.0.0.26.128 iOS (13_1_3; iPhone11,8; sk_SK; sk-SK; scale=2.00; 828x1792; 195061483)");

// 2019-2021 Instagram App Agents (iOS)
        instagramAppUserAgents.add("Instagram 146.0.0.19.124 iOS (13_3; iPhone12,1; sk_SK; sk-SK; scale=2.00; 828x1792; 223714655)");
        instagramAppUserAgents.add("Instagram 157.0.0.28.120 iOS (13_5_1; iPhone11,6; sk_SK; sk-SK; scale=3.00; 1242x2688; 244316169)");
        instagramAppUserAgents.add("Instagram 168.0.0.40.113 iOS (14_0_1; iPhone12,1; sk_SK; sk-SK; scale=2.00; 828x1792; 259051498)");
        instagramAppUserAgents.add("Instagram 175.0.0.27.120 iOS (14_2; iPhone13,2; sk_SK; sk-SK; scale=3.00; 1170x2532; 275214946)");
        instagramAppUserAgents.add("Instagram 186.0.0.34.124 iOS (14_4; iPhone11,8; sk_SK; sk-SK; scale=2.00; 828x1792; 288518330)");

// 2021-2022 Instagram App Agents (iOS)
        instagramAppUserAgents.add("Instagram 196.0.0.31.119 iOS (14_6; iPhone12,8; sk_SK; sk-SK; scale=2.00; 750x1334; 303825791)");
        instagramAppUserAgents.add("Instagram 210.0.0.23.118 iOS (15_0_2; iPhone13,2; sk_SK; sk-SK; scale=3.00; 1170x2532; 325879513)");
        instagramAppUserAgents.add("Instagram 223.0.0.11.103 iOS (15_2_1; iPhone14,2; sk_SK; sk-SK; scale=3.00; 1170x2532; 349721290)");
        instagramAppUserAgents.add("Instagram 237.0.0.8.117 iOS (15_4; iPhone13,3; sk_SK; sk-SK; scale=3.00; 1170x2532; 371489549)");
        instagramAppUserAgents.add("Instagram 248.1.0.15.111 iOS (15_5; iPhone13,1; sk_SK; sk-SK; scale=2.88; 1080x2340; 394125876)");

// 2022-2023 Instagram App Agents (iOS)
        instagramAppUserAgents.add("Instagram 262.0.0.18.114 iOS (15_6_1; iPhone13,4; sk_SK; sk-SK; scale=3.00; 1284x2778; 415567891)");
        instagramAppUserAgents.add("Instagram 271.1.0.16.107 iOS (16_0; iPhone14,7; sk_SK; sk-SK; scale=3.00; 1170x2532; 429487651)");
        instagramAppUserAgents.add("Instagram 278.0.0.20.115 iOS (16_1_1; iPhone14,3; sk_SK; sk-SK; scale=3.00; 1170x2532; 441234567)");
        instagramAppUserAgents.add("Instagram 289.1.0.23.109 iOS (16_3; iPhone14,4; sk_SK; sk-SK; scale=3.00; 1125x2436; 457654321)");
        instagramAppUserAgents.add("Instagram 301.0.0.27.105 iOS (16_4_1; iPhone14,8; sk_SK; sk-SK; scale=3.00; 1179x2556; 479012345)");

// 2023-2024 Instagram App Agents (iOS)
        instagramAppUserAgents.add("Instagram 311.0.0.29.118 iOS (16_5; iPhone14,5; sk_SK; sk-SK; scale=3.00; 1170x2532; 491234567)");
        instagramAppUserAgents.add("Instagram 323.0.0.16.110 iOS (16_6; iPhone14,7; sk_SK; sk-SK; scale=3.00; 1170x2532; 513579246)");
        instagramAppUserAgents.add("Instagram 336.1.0.21.101 iOS (17_0; iPhone15,2; sk_SK; sk-SK; scale=3.00; 1179x2556; 533214789)");
        instagramAppUserAgents.add("Instagram 346.0.0.23.119 iOS (17_1_1; iPhone15,4; sk_SK; sk-SK; scale=3.00; 1179x2556; 548765431)");
        instagramAppUserAgents.add("Instagram 356.0.0.18.114 iOS (17_3; iPhone15,5; sk_SK; sk-SK; scale=3.00; 1290x2796; 561234789)");

// 2024-2025 Instagram App Agents (iOS) - Projected
        instagramAppUserAgents.add("Instagram 369.0.0.14.103 iOS (17_4_1; iPhone16,1; sk_SK; sk-SK; scale=3.00; 1179x2556; 573891234)");
        instagramAppUserAgents.add("Instagram 378.0.0.21.118 iOS (17_5; iPhone16,2; sk_SK; sk-SK; scale=3.00; 1290x2796; 585432198)");
        instagramAppUserAgents.add("Instagram 388.0.0.19.107 iOS (18_0; iPhone17,1; sk_SK; sk-SK; scale=3.00; 1179x2556; 597654321)");
        instagramAppUserAgents.add("Instagram 392.0.0.26.114 iOS (18_1; iPhone17,2; sk_SK; sk-SK; scale=3.00; 1290x2796; 608765432)");
        instagramAppUserAgents.add("Instagram 399.0.0.11.105 iOS (18_2; iPhone17,4; sk_SK; sk-SK; scale=3.00; 1290x2796; 617654321)");

        // Map by device type
        deviceTypeUserAgents.put(DeviceProfile.TYPE_MOBILE, new ArrayList<>());
        deviceTypeUserAgents.get(DeviceProfile.TYPE_MOBILE).addAll(androidUserAgents.subList(0, 4));
        deviceTypeUserAgents.get(DeviceProfile.TYPE_MOBILE).addAll(iosUserAgents.subList(0, 4));

        deviceTypeUserAgents.put(DeviceProfile.TYPE_TABLET, new ArrayList<>());
        deviceTypeUserAgents.get(DeviceProfile.TYPE_TABLET).add(iosUserAgents.get(4)); // iPad

        deviceTypeUserAgents.put(DeviceProfile.TYPE_DESKTOP, new ArrayList<>());
        deviceTypeUserAgents.get(DeviceProfile.TYPE_DESKTOP).addAll(windowsUserAgents);
        deviceTypeUserAgents.get(DeviceProfile.TYPE_DESKTOP).addAll(macUserAgents);

        // Map by platform
        platformUserAgents.put(DeviceProfile.PLATFORM_ANDROID, androidUserAgents);
        platformUserAgents.put(DeviceProfile.PLATFORM_IOS, iosUserAgents);
        platformUserAgents.put(DeviceProfile.PLATFORM_WINDOWS, windowsUserAgents);
        platformUserAgents.put(DeviceProfile.PLATFORM_MAC, macUserAgents);

        // Initialize device tier maps for each platform
        for (String platform : new String[]{
                DeviceProfile.PLATFORM_ANDROID,
                DeviceProfile.PLATFORM_IOS,
                DeviceProfile.PLATFORM_WINDOWS,
                DeviceProfile.PLATFORM_MAC
        }) {
            deviceTierUserAgents.put(platform, new HashMap<>());
            deviceTierUserAgents.get(platform).put(DeviceProfile.TIER_BUDGET, new ArrayList<>());
            deviceTierUserAgents.get(platform).put(DeviceProfile.TIER_MID_RANGE, new ArrayList<>());
            deviceTierUserAgents.get(platform).put(DeviceProfile.TIER_FLAGSHIP, new ArrayList<>());
        }

        // Add some sample device tier mappings (in a real implementation, this would be more comprehensive)
        // Update device type mappings based on expanded lists
        deviceTypeUserAgents.get(DeviceProfile.TYPE_MOBILE).clear();
        deviceTypeUserAgents.get(DeviceProfile.TYPE_MOBILE).addAll(androidUserAgents.subList(0, 65)); // Android phones
        deviceTypeUserAgents.get(DeviceProfile.TYPE_MOBILE).addAll(iosUserAgents.subList(0, 65)); // iPhones

// Update tablet mapping
        deviceTypeUserAgents.get(DeviceProfile.TYPE_TABLET).clear();
        deviceTypeUserAgents.get(DeviceProfile.TYPE_TABLET).add(iosUserAgents.get(4)); // Original iPad
        deviceTypeUserAgents.get(DeviceProfile.TYPE_TABLET).addAll(iosUserAgents.subList(65, 70)); // Additional iPads

// Update device tier mappings for Android
// Budget devices (older/lower spec phones)
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_BUDGET).clear();
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_BUDGET).add(androidUserAgents.get(4)); // Original Xiaomi
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_BUDGET).addAll(androidUserAgents.subList(5, 15));
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_BUDGET).addAll(androidUserAgents.subList(30, 40));

// Mid-range devices
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).clear();
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).add(androidUserAgents.get(2)); // Original OnePlus
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).add(androidUserAgents.get(3)); // Original Moto G
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).addAll(androidUserAgents.subList(15, 25));
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).addAll(androidUserAgents.subList(40, 50));

// Flagship devices
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).clear();
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).add(androidUserAgents.get(0)); // Original Pixel 7
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).add(androidUserAgents.get(1)); // Original Samsung S22
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).addAll(androidUserAgents.subList(25, 30));
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).addAll(androidUserAgents.subList(50, 70));

// Update device tier mappings for iOS
// Budget devices (older iPhones)
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_BUDGET).clear();
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_BUDGET).add(iosUserAgents.get(3)); // Original iOS 15
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_BUDGET).addAll(iosUserAgents.subList(5, 25));

// Mid-range devices
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_MID_RANGE).clear();
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_MID_RANGE).add(iosUserAgents.get(2)); // Original iOS 16
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_MID_RANGE).addAll(iosUserAgents.subList(25, 45));

// Flagship devices (newest iPhones)
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_FLAGSHIP).clear();
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_FLAGSHIP).add(iosUserAgents.get(0)); // Original iOS 17.2
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_FLAGSHIP).add(iosUserAgents.get(1)); // Original iPhone 17.0
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_FLAGSHIP).addAll(iosUserAgents.subList(45, 70));

        // Slovak-specific demographic distributions
        Map<String, Float> ageDistributionSlovakia = new HashMap<>();
        ageDistributionSlovakia.put("13-17", 0.12f);
        ageDistributionSlovakia.put("18-24", 0.24f);
        ageDistributionSlovakia.put("25-34", 0.31f);
        ageDistributionSlovakia.put("35-44", 0.18f);
        ageDistributionSlovakia.put("45-54", 0.09f);
        ageDistributionSlovakia.put("55-64", 0.04f);
        ageDistributionSlovakia.put("65+", 0.02f);

        Map<String, Float> deviceDistributionSlovakia = new HashMap<>();
        deviceDistributionSlovakia.put(DeviceProfile.TYPE_MOBILE, 0.90f);
        deviceDistributionSlovakia.put(DeviceProfile.TYPE_DESKTOP, 0.05f);
        deviceDistributionSlovakia.put(DeviceProfile.TYPE_TABLET, 0.05f);

        Map<String, Float> platformDistributionSlovakia = new HashMap<>();
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_ANDROID, 0.72f);
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_IOS, 0.15f);
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_WINDOWS, 0.10f);
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_MAC, 0.03f);

        demographicDistributions.put("age_slovakia", ageDistributionSlovakia);
        demographicDistributions.put("device_slovakia", deviceDistributionSlovakia);
        demographicDistributions.put("platform_slovakia", platformDistributionSlovakia);
    }

    /**
     * Get a random user agent string.
     * @return Random user agent string
     */
    public static String getRandomUserAgent() {
        List<String> allUserAgents = new ArrayList<>();
        allUserAgents.addAll(androidUserAgents);
        allUserAgents.addAll(iosUserAgents);
        allUserAgents.addAll(windowsUserAgents);
        allUserAgents.addAll(macUserAgents);

        return allUserAgents.get(random.nextInt(allUserAgents.size()));
    }

    /**
     * Get a random user agent for a specific device type.
     * @param deviceType Device type (mobile, tablet, desktop)
     * @return Random user agent string
     */
    public static String getRandomUserAgentByDeviceType(String deviceType) {
        List<String> agents = deviceTypeUserAgents.get(deviceType);
        if (agents == null || agents.isEmpty()) {
            return getRandomUserAgent();
        }
        return agents.get(random.nextInt(agents.size()));
    }

    /**
     * Get a random user agent for a specific platform.
     * @param platform Platform (android, ios, windows, mac)
     * @return Random user agent string
     */
    public static String getRandomUserAgentByPlatform(String platform) {
        List<String> agents = platformUserAgents.get(platform);
        if (agents == null || agents.isEmpty()) {
            return getRandomUserAgent();
        }
        return agents.get(random.nextInt(agents.size()));
    }

    /**
     * Get a random user agent for a specific platform and device tier.
     * @param platform Platform (android, ios, windows, mac)
     * @param tier Device tier (budget, mid-range, flagship)
     * @return Random user agent string
     */
    public static String getRandomUserAgentByTier(String platform, String tier) {
        Map<String, List<String>> platformTiers = deviceTierUserAgents.get(platform);
        if (platformTiers == null) {
            return getRandomUserAgentByPlatform(platform);
        }

        List<String> agents = platformTiers.get(tier);
        if (agents == null || agents.isEmpty()) {
            return getRandomUserAgentByPlatform(platform);
        }

        return agents.get(random.nextInt(agents.size()));
    }

    /**
     * Get a random Instagram app user agent.
     * @return Random Instagram app user agent string
     */
    public static String getRandomInstagramAppUserAgent() {
        return instagramAppUserAgents.get(random.nextInt(instagramAppUserAgents.size()));
    }

    /**
     * Get a device profile based on Slovak demographic data.
     * @return Device profile with Slovak demographic characteristics
     */
    public static DeviceProfile getSlovakDemographicProfile() {
        // Choose age group based on Slovak demographics
        String ageGroup = chooseBased(demographicDistributions.get("age_slovakia"));

        // Choose device type based on age group and Slovak demographics
        String deviceType = chooseBased(demographicDistributions.get("device_slovakia"));

        // Choose platform based on device type and Slovak demographics
        String platform = chooseBased(demographicDistributions.get("platform_slovakia"));

        // Choose device tier based on age group
        String deviceTier;
        if (ageGroup.equals("13-17") || ageGroup.equals("18-24")) {
            // Younger users more likely to have budget or mid-range devices
            deviceTier = random.nextFloat() < 0.7f ?
                    (random.nextFloat() < 0.6f ? DeviceProfile.TIER_BUDGET : DeviceProfile.TIER_MID_RANGE) :
                    DeviceProfile.TIER_FLAGSHIP;
        } else if (ageGroup.equals("25-34") || ageGroup.equals("35-44")) {
            // Middle-aged users more likely to have mid-range or flagship devices
            deviceTier = random.nextFloat() < 0.8f ?
                    (random.nextFloat() < 0.5f ? DeviceProfile.TIER_MID_RANGE : DeviceProfile.TIER_FLAGSHIP) :
                    DeviceProfile.TIER_BUDGET;
        } else {
            // Older users have more varied device tiers
            deviceTier = random.nextFloat() < 0.4f ? DeviceProfile.TIER_BUDGET :
                    (random.nextFloat() < 0.5f ? DeviceProfile.TIER_MID_RANGE : DeviceProfile.TIER_FLAGSHIP);
        }

        // Get appropriate user agent
        String userAgent;
        boolean isInstagramApp = false;

        if (deviceType.equals(DeviceProfile.TYPE_MOBILE) || deviceType.equals(DeviceProfile.TYPE_TABLET)) {
            // UPDATED: Increased chance to use Instagram app user agent from 70% to 95%
            if (random.nextFloat() < 0.97f) { // 95% chance for mobile to use Instagram app
                userAgent = getRandomInstagramAppUserAgent();
                isInstagramApp = true;
            } else {
                userAgent = getRandomUserAgentByTier(platform, deviceTier);
            }
        } else {
            // Desktop devices always use browser user agents
            userAgent = getRandomUserAgentByTier(platform, deviceTier);
        }

        // Create and return the device profile
        DeviceProfile profile = new DeviceProfile.Builder()
                .deviceType(deviceType)
                .platform(platform)
                .deviceTier(deviceTier)
                .userAgent(userAgent)
                .region("slovakia")
                .isInstagramApp(isInstagramApp)
                .build();

        Logger.d("UserAgentData", "Created profile: " + deviceType + ", " + platform +
                ", Instagram app: " + isInstagramApp);

        return profile;
    }

    /**
     * Choose a random item based on a probability distribution.
     * @param distribution Map of items to their probabilities
     * @param <T> Type of the items
     * @return Randomly chosen item
     */
    private static <T> T chooseBased(Map<T, Float> distribution) {
        float value = random.nextFloat();
        float cumulative = 0.0f;

        for (Map.Entry<T, Float> entry : distribution.entrySet()) {
            cumulative += entry.getValue();
            if (value < cumulative) {
                return entry.getKey();
            }
        }

        // If we somehow get here, return the first item
        return distribution.keySet().iterator().next();
    }
}

