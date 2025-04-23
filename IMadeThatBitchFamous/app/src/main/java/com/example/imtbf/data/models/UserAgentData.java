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

        // Google Devices
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (34/14; 480dpi; 1080x2400; Google/google; Pixel 7; panther; arm64-v8a; sk_SK; 462519124)");
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (34/14; 480dpi; 1080x2400; Google/google; Pixel 8; shiba; arm64-v8a; sk_SK; 463921753)");
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (33/13; 480dpi; 1080x2280; Google/google; Pixel 6; oriole; arm64-v8a; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (33/13; 440dpi; 1080x2280; Google/google; Pixel 5; redfin; arm64-v8a; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (33/13; 440dpi; 1080x2280; Google/google; Pixel 4a; sunfish; arm64-v8a; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (32/12; 440dpi; 1080x2280; Google/google; Pixel 4; flame; arm64-v8a; sk_SK; 467123489)");

        // Samsung Devices (Very popular in Slovakia)
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 Android (34/14; 560dpi; 1440x3200; samsung; SM-S928B; CS3; exynos2400; sk_SK; 469283533)");
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 Android (34/14; 480dpi; 1080x2400; samsung; SM-S918B; CS2; arm64-v8a; sk_SK; 469283533)");
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (34/14; 480dpi; 1080x2400; samsung; SM-S908B; CS1; arm64-v8a; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (33/13; 450dpi; 1080x2400; samsung; SM-A546B; a54; arm64-v8a; sk_SK; 467123489)");
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (33/13; 420dpi; 1080x2400; samsung; SM-A536B; a53; arm64-v8a; sk_SK; 463921753)");
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (33/13; 450dpi; 1080x2400; samsung; SM-A346B; a34; arm64-v8a; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (33/13; 420dpi; 1080x2400; samsung; SM-A336B; a33; arm64-v8a; sk_SK; 462519124)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (33/13; 420dpi; 1080x2400; samsung; SM-A236B; a23; arm64-v8a; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (32/12; 420dpi; 1080x2400; samsung; SM-A136B; a13; arm64-v8a; sk_SK; 463921753)");
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (31/12; 320dpi; 720x1600; samsung; SM-A125F; a12; mt6765; sk_SK; 462519124)");
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (31/12; 320dpi; 720x1600; samsung; SM-A037G; a03s; arm64-v8a; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (33/13; 450dpi; 1080x2400; samsung; SM-F936B; q4; arm64-v8a; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (33/13; 450dpi; 1080x2400; samsung; SM-F711B; flip3; arm64-v8a; sk_SK; 467123489)");
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 Android (32/12; 450dpi; 1080x2640; samsung; SM-F700F; fold; arm64-v8a; sk_SK; 469283533)");
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (33/13; 480dpi; 1080x2640; samsung; SM-S901B; s22; arm64-v8a; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (33/13; 480dpi; 1080x2400; samsung; SM-S906B; s22p; arm64-v8a; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (33/13; 560dpi; 1440x3200; samsung; SM-S908B; s22u; arm64-v8a; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (31/12; 420dpi; 1080x2400; samsung; SM-A525F; a52; arm64-v8a; sk_SK; 463921753)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (30/11; 420dpi; 1080x2400; samsung; SM-A515F; a51; arm64-v8a; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 Android (29/10; 420dpi; 1080x2400; samsung; SM-A505F; a50; arm64-v8a; sk_SK; 458733883)");

        // Xiaomi/Redmi Devices (Popular in Slovakia)
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 Android (33/13; 440dpi; 1080x2400; Xiaomi; 23077RABAG; sea; mt6877; sk_SK; 469283533)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (33/13; 440dpi; 1080x2400; Xiaomi; 23050RN02G; sky; qcom; sk_SK; 467123489)");
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (33/13; 440dpi; 1080x2400; Xiaomi; 2307CRN29G; garnet; mt6877; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (32/12; 440dpi; 1080x2400; Xiaomi/Redmi; Redmi Note 12; tapas; qcom; sk_SK; 462519124)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (32/12; 440dpi; 1080x2400; Xiaomi/Redmi; Redmi Note 11; spes; qcom; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (31/12; 440dpi; 1080x2400; Xiaomi/Redmi; Redmi Note 10 Pro; sweet; qcom; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 291.1.0.34.111 Android (31/12; 440dpi; 1080x2400; Xiaomi/Redmi; Redmi Note 10; mojito; qcom; sk_SK; 457189239)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (30/11; 440dpi; 1080x2400; Xiaomi/Redmi; Redmi Note 9 Pro; joyeuse; qcom; sk_SK; 467123489)");
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (29/10; 440dpi; 1080x2400; Xiaomi/Redmi; Redmi Note 8 Pro; begonia; mt6785; sk_SK; 463921753)");
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (30/11; 280dpi; 720x1600; Xiaomi/Redmi; Redmi 9A; dandelion; mt6762; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (30/11; 280dpi; 720x1560; Xiaomi/POCO; POCO M3; angelica; qcom; sk_SK; 462519124)");
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (33/13; 440dpi; 1080x2400; Xiaomi/POCO; POCO F5; marble; qcom; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (32/12; 440dpi; 1080x2400; Xiaomi/POCO; POCO X5 Pro; redwood; qcom; sk_SK; 467123489)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (31/12; 440dpi; 1080x2400; Xiaomi/POCO; POCO X4 GT; xaga; mt6895; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 Android (33/13; 440dpi; 1080x2400; Xiaomi; Mi 11; venus; qcom; sk_SK; 469283533)");

        // Other popular brands in Slovakia
        // Huawei
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 Android (29/10; 420dpi; 1080x2340; HUAWEI; P30 Lite; HWMAR; kirin710; sk_SK; 458733883)");
        instagramAppUserAgents.add("Instagram 290.0.0.16.111 Android (29/10; 480dpi; 1080x2340; HUAWEI; P30; HWELE; kirin980; sk_SK; 456213364)");
        instagramAppUserAgents.add("Instagram 291.1.0.34.111 Android (29/10; 480dpi; 1080x2340; HUAWEI; P40 Lite; HWJNY; kirin810; sk_SK; 457189239)");

        // OnePlus
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (33/13; 450dpi; 1080x2400; OnePlus; Nord 3; CPH2491; qcom; sk_SK; 462519124)");
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (33/13; 450dpi; 1080x2400; OnePlus; 11; OP5933; qcom; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 291.1.0.34.111 Android (30/11; 440dpi; 1080x2400; OnePlus; Nord CE 2; OP535BL1; mt6877; sk_SK; 457189239)");

        // Motorola
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (33/13; 480dpi; 1080x2400; motorola; moto g73 5G; hawao; qcom; sk_SK; 463921753)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (33/13; 480dpi; 1080x2400; motorola; moto g71 5G; corfur; qcom; sk_SK; 461020179)");
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (32/12; 450dpi; 1080x2400; motorola; moto g52; rhode; qcom; sk_SK; 467123489)");

        // Nokia
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 Android (31/12; 420dpi; 1080x2400; HMD Global; Nokia X20; NKX20; qcom; sk_SK; 459881122)");
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 Android (30/11; 420dpi; 1080x2340; HMD Global; Nokia 5.4; DoctorStrange; qcom; sk_SK; 458733883)");

        // Realme
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 Android (33/13; 440dpi; 1080x2400; realme; RMX3630; RE58C2L1; qcom; sk_SK; 465283622)");
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 Android (33/13; 440dpi; 1080x2400; realme; RMX3561; RE547F; mt6877; sk_SK; 462519124)");

        // Vivo
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 Android (33/13; 440dpi; 1080x2400; vivo; V2230; PD2230F_EX; qcom; sk_SK; 469283533)");
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 Android (33/13; 440dpi; 1080x2400; vivo; V2045; PD2045; qcom; sk_SK; 463921753)");

        // Oppo
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 Android (33/13; 440dpi; 1080x2400; OPPO; CPH2471; OP4EC3L1; qcom; sk_SK; 467123489)");
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 Android (32/12; 440dpi; 1080x2400; OPPO; CPH2363; OP4F93L1; mt6893; sk_SK; 461020179)");

        // iOS Instagram app user agents
        // iPhone 15 Series
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 iOS (17_6_1; iPhone16,1; sk_SK; sk-SK; scale=3.00; 1179x2556; 469283533)"); // iPhone 15
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 iOS (17_6_1; iPhone16,2; sk_SK; sk-SK; scale=3.00; 1179x2556; 467123489)"); // iPhone 15 Plus
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 iOS (17_6_1; iPhone16,4; sk_SK; sk-SK; scale=3.00; 1290x2796; 465283622)"); // iPhone 15 Pro
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 iOS (17_6_1; iPhone16,5; sk_SK; sk-SK; scale=3.00; 1290x2796; 463921753)"); // iPhone 15 Pro Max

        // iPhone 14 Series
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 iOS (17_6_1; iPhone14,7; sk_SK; sk-SK; scale=3.00; 1170x2532; 462519124)"); // iPhone 14
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 iOS (17_6_1; iPhone14,8; sk_SK; sk-SK; scale=3.00; 1170x2532; 461020179)"); // iPhone 14 Plus
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 iOS (17_6_1; iPhone15,2; sk_SK; sk-SK; scale=3.00; 1170x2532; 459881122)"); // iPhone 14 Pro
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 iOS (17_6_1; iPhone15,3; sk_SK; sk-SK; scale=3.00; 1290x2796; 458733883)"); // iPhone 14 Pro Max

        // iPhone 13 Series
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 iOS (17_6_1; iPhone14,5; sk_SK; sk-SK; scale=3.00; 1170x2532; 469283533)"); // iPhone 13
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 iOS (17_6_1; iPhone14,4; sk_SK; sk-SK; scale=3.00; 1080x2340; 467123489)"); // iPhone 13 mini
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 iOS (17_6_1; iPhone14,2; sk_SK; sk-SK; scale=3.00; 1170x2532; 465283622)"); // iPhone 13 Pro
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 iOS (17_6_1; iPhone14,3; sk_SK; sk-SK; scale=3.00; 1284x2778; 463921753)"); // iPhone 13 Pro Max

        // iPhone 12 Series
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 iOS (17_6_1; iPhone13,2; sk_SK; sk-SK; scale=3.00; 1170x2532; 462519124)"); // iPhone 12
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 iOS (17_6_1; iPhone13,1; sk_SK; sk-SK; scale=3.00; 1080x2340; 461020179)"); // iPhone 12 mini
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 iOS (17_6_1; iPhone13,3; sk_SK; sk-SK; scale=3.00; 1170x2532; 459881122)"); // iPhone 12 Pro
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 iOS (17_6_1; iPhone13,4; sk_SK; sk-SK; scale=3.00; 1284x2778; 458733883)"); // iPhone 12 Pro Max

        // iPhone 11 Series
        instagramAppUserAgents.add("Instagram 291.0.0.31.111 iOS (17_6_1; iPhone12,1; sk_SK; sk-SK; scale=2.00; 828x1792; 457142244)"); // iPhone 11
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 iOS (17_6_1; iPhone12,3; sk_SK; sk-SK; scale=3.00; 1125x2436; 469283533)"); // iPhone 11 Pro
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 iOS (17_6_1; iPhone12,5; sk_SK; sk-SK; scale=3.00; 1242x2688; 467123489)"); // iPhone 11 Pro Max

        // iPhone SE/XR/XS Series
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 iOS (17_6_1; iPhone12,8; sk_SK; sk-SK; scale=2.00; 750x1334; 465283622)"); // iPhone SE 2020
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 iOS (17_6_1; iPhone14,6; sk_SK; sk-SK; scale=2.00; 750x1334; 463921753)"); // iPhone SE 2022
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 iOS (17_6_1; iPhone11,8; sk_SK; sk-SK; scale=2.00; 828x1792; 462519124)"); // iPhone XR
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 iOS (17_6_1; iPhone11,2; sk_SK; sk-SK; scale=3.00; 1125x2436; 461020179)"); // iPhone XS
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 iOS (17_6_1; iPhone11,6; sk_SK; sk-SK; scale=3.00; 1242x2688; 459881122)"); // iPhone XS Max

        // Older iPhones with latest iOS
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 iOS (17_6_1; iPhone10,1; sk_SK; sk-SK; scale=2.00; 750x1334; 458733883)"); // iPhone 8
        instagramAppUserAgents.add("Instagram 290.0.0.16.111 iOS (17_6_1; iPhone10,4; sk_SK; sk-SK; scale=2.00; 750x1334; 456213364)"); // iPhone 8
        instagramAppUserAgents.add("Instagram 291.1.0.34.111 iOS (17_6_1; iPhone10,2; sk_SK; sk-SK; scale=3.00; 1080x1920; 457189239)"); // iPhone 8 Plus
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 iOS (17_6_1; iPhone10,5; sk_SK; sk-SK; scale=3.00; 1080x1920; 469283533)"); // iPhone 8 Plus

        // iPads
        instagramAppUserAgents.add("Instagram 298.0.0.23.118 iOS (17_6_1; iPad13,1; sk_SK; sk-SK; scale=2.00; 1640x2360; 467123489)"); // iPad Air 4
        instagramAppUserAgents.add("Instagram 297.0.0.29.101 iOS (17_6_1; iPad13,2; sk_SK; sk-SK; scale=2.00; 1640x2360; 465283622)"); // iPad Air 4 Cellular
        instagramAppUserAgents.add("Instagram 296.0.0.31.111 iOS (17_6_1; iPad13,4; sk_SK; sk-SK; scale=2.00; 1640x2360; 463921753)"); // iPad Pro 11 2021
        instagramAppUserAgents.add("Instagram 295.0.0.14.109 iOS (17_6_1; iPad13,8; sk_SK; sk-SK; scale=2.00; 2048x2732; 462519124)"); // iPad Pro 12.9 2021
        instagramAppUserAgents.add("Instagram 294.0.0.26.117 iOS (17_6_1; iPad13,16; sk_SK; sk-SK; scale=2.00; 1640x2360; 461020179)"); // iPad Air 5
        instagramAppUserAgents.add("Instagram 293.0.0.27.111 iOS (17_6_1; iPad13,17; sk_SK; sk-SK; scale=2.00; 1640x2360; 459881122)"); // iPad Air 5 Cellular
        instagramAppUserAgents.add("Instagram 292.0.0.29.122 iOS (17_6_1; iPad14,3; sk_SK; sk-SK; scale=2.00; 1640x2360; 458733883)"); // iPad Pro 11 2022
        instagramAppUserAgents.add("Instagram 290.0.0.16.111 iOS (17_6_1; iPad14,5; sk_SK; sk-SK; scale=2.00; 2048x2732; 456213364)"); // iPad Pro 12.9 2022
        instagramAppUserAgents.add("Instagram 291.1.0.34.111 iOS (17_6_1; iPad12,1; sk_SK; sk-SK; scale=2.00; 1620x2160; 457189239)"); // iPad 9
        instagramAppUserAgents.add("Instagram 299.0.0.35.117 iOS (17_6_1; iPad12,2; sk_SK; sk-SK; scale=2.00; 1620x2160; 469283533)"); // iPad 9 Cellular

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
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).add(androidUserAgents.get(0)); // Pixel 7
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_FLAGSHIP).add(androidUserAgents.get(1)); // Samsung S22
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).add(androidUserAgents.get(2)); // OnePlus
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_MID_RANGE).add(androidUserAgents.get(3)); // Moto G
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_ANDROID).get(DeviceProfile.TIER_BUDGET).add(androidUserAgents.get(4)); // Xiaomi

        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_FLAGSHIP).add(iosUserAgents.get(0)); // iOS 17
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_FLAGSHIP).add(iosUserAgents.get(1)); // iPhone 13
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_MID_RANGE).add(iosUserAgents.get(2)); // iOS 16
        deviceTierUserAgents.get(DeviceProfile.PLATFORM_IOS).get(DeviceProfile.TIER_BUDGET).add(iosUserAgents.get(3)); // iOS 15

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
        deviceDistributionSlovakia.put(DeviceProfile.TYPE_MOBILE, 0.99f);
        deviceDistributionSlovakia.put(DeviceProfile.TYPE_DESKTOP, 0.00f);
        deviceDistributionSlovakia.put(DeviceProfile.TYPE_TABLET, 0.01f);

        Map<String, Float> platformDistributionSlovakia = new HashMap<>();
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_ANDROID, 0.78f);
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_IOS, 0.18f);
        platformDistributionSlovakia.put(DeviceProfile.PLATFORM_WINDOWS, 0.01f);
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
            if (random.nextFloat() < 1.0f) { // 95% chance for mobile to use Instagram app
                String baseUserAgent = getInstagramAppUserAgentByPlatform(platform);
                userAgent = randomizeInstagramSessionId(baseUserAgent);
                isInstagramApp = true;
            } else {
                userAgent = getRandomUserAgentByTier(platform, deviceTier);
            }
        } else {
            // Desktop devices always use browser user agents
            userAgent = getRandomUserAgentByTier(platform, deviceTier);
        }

//        if (deviceType.equals(DeviceProfile.TYPE_MOBILE) || deviceType.equals(DeviceProfile.TYPE_TABLET)) {
//            // Always use Instagram app user agent (100% probability)
//            String baseUserAgent = getInstagramAppUserAgentByPlatform(platform);
//            userAgent = randomizeInstagramSessionId(baseUserAgent);
//            isInstagramApp = true;
//        } else {
//            // For desktop devices, we could still use Instagram web browser user agents
//            userAgent = getRandomUserAgentByTier(platform, deviceTier);
//        }

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

    /**
     * Generates a random Instagram user agent with randomized session ID.
     */
    public static String randomizeInstagramSessionId(String baseUserAgent) {
        // The session ID is the numerical part at the end of the user agent
        int lastSemicolonIndex = baseUserAgent.lastIndexOf(';');
        if (lastSemicolonIndex == -1 || lastSemicolonIndex >= baseUserAgent.length() - 2) {
            // Invalid format or no session ID, return as is
            return baseUserAgent;
        }

        // Extract everything before the session ID
        String prefix = baseUserAgent.substring(0, lastSemicolonIndex + 1) + " ";

        // Generate a random 9-digit session ID (typical format)
        Random random = new Random();
        int sessionId = 450000000 + random.nextInt(20000000); // Range from ~450M to ~470M for current versions

        return prefix + sessionId + ")";
    }

    /**
     * Get a random Instagram app user agent with randomized session ID.
     */
    public static String getRandomInstagramUserAgentWithRandomSessionId() {
        // Get a random base user agent
        String baseUserAgent = instagramAppUserAgents.get(random.nextInt(instagramAppUserAgents.size()));

        // Randomize its session ID
        return randomizeInstagramSessionId(baseUserAgent);
    }

    /**
     * Get an Instagram app user agent for a specific platform.
     */
    public static String getInstagramAppUserAgentByPlatform(String platform) {
        List<String> filteredAgents = new ArrayList<>();

        // Filter user agents by platform
        for (String userAgent : instagramAppUserAgents) {
            if (platform.equals(DeviceProfile.PLATFORM_ANDROID) && userAgent.contains("Android")) {
                filteredAgents.add(userAgent);
            } else if ((platform.equals(DeviceProfile.PLATFORM_IOS) || platform.equals(DeviceProfile.PLATFORM_MAC))
                    && userAgent.contains("iOS")) {
                filteredAgents.add(userAgent);
            }
        }

        // If no matching agents, return any random one
        if (filteredAgents.isEmpty()) {
            return instagramAppUserAgents.get(random.nextInt(instagramAppUserAgents.size()));
        }

        // Return a random agent from the filtered list
        return filteredAgents.get(random.nextInt(filteredAgents.size()));
    }

}