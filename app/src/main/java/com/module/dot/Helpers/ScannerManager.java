package com.module.dot.Helpers;

import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.module.dot.Activities.CaptureAct;
import com.module.dot.Activities.Items.Items;

public class ScannerManager {
    private ActivityResultLauncher<ScanOptions> scannerLauncher;

    public ScannerManager(Fragment fragment) {
        // Initialize the scannerLauncher using the provided fragment
        scannerLauncher = fragment.registerForActivityResult(new ScanContract(), result -> {
            String scanItem = result.getContents();

            // Handle the result using the fragment's context
            // (e.g., access fragment.getActivity() for the activity context)

            // ...

        });
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
