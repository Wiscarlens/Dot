package com.module.dot.view.utils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.module.dot.view.CaptureAct;

public class ScannerManager {
    private final ActivityResultLauncher<ScanOptions> scannerLauncher;
    private String scanItem = null;

    public ScannerManager(Fragment fragment) {
        // Initialize the scannerLauncher using the provided fragment
        scannerLauncher = fragment.registerForActivityResult(
                new ScanContract(), result -> scanItem = result.getContents()
        );
    }

    public String getScanItem() {
        return scanItem;
    }

    public void startBarcodeScanning() {
        ScanOptions options = new ScanOptions();

        options.setPrompt("Press Volume Up to Turn Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        scannerLauncher.launch(options);
    }
}
