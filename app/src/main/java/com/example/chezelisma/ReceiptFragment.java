package com.example.chezelisma;

import static com.example.chezelisma.LocalFormat.getCurrencyFormat;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



public class ReceiptFragment extends Fragment {
    final static int REQUEST_CODE = 1232;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView logo = view.findViewById(R.id.receiptLogo);
        TextView companyName = view.findViewById(R.id.receiptCompanyName);
        TextView companyAddress = view.findViewById(R.id.receiptCompanyStreetName);
        TextView companyCity = view.findViewById(R.id.receiptCompanyCityStZipcode);
        TextView date = view.findViewById(R.id.receiptDate);
        TextView time = view.findViewById(R.id.receiptTime);

        RecyclerView receiptItems = view.findViewById(R.id.receiptItems);

        TextView subtotal = view.findViewById(R.id.receiptSubtotal);
        TextView tax = view.findViewById(R.id.receiptTax);
        TextView total = view.findViewById(R.id.receiptTotal);

        ImageView barcode = view.findViewById(R.id.receiptBarcode);
        TextView orderNumber = view.findViewById(R.id.receiptOrderNumber);

        TextView emailButton = view.findViewById(R.id.receiptEmailButton);
        TextView printButton = view.findViewById(R.id.receiptPrintButton);
        TextView shareButton = view.findViewById(R.id.receiptShareButton);

        receiptItems.setLayoutManager(new LinearLayoutManager(getContext()));

        MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());

        ArrayList<Orders> orders = new ArrayList<>();

        // Receiving Order  Number from HomeFragment
        getParentFragmentManager().setFragmentResultListener(
                "orderNumberData",
                this,
                (requestKey, result) -> {

                    MyDatabaseHelper.getOrdersDetails(
                            myDB,
                            orders,
                            getResources(),
                            result.getLong("orderNumber") // Order Number
                    );

                    // Update UI elements with order data here
                    if (!orders.isEmpty()) {
                        date.setText(orders.get(0).getOrderDate());
                        time.setText(orders.get(0).getOrderTime());
                        // TODO: Make total a string and format it to local currency in order class
                        total.setText(getCurrencyFormat(orders.get(0).getOrderTotalAmount()));
                        orderNumber.setText(Utils.formatOrderNumber(orders.get(0).getOrderNumber()));

                        ArrayList<Items> selectedItems = orders.get(0).getSelectedItem();

                        // Create the adapter and set it to the RecyclerView
                        BottomSheetAdapter receiptAdapter = new BottomSheetAdapter(selectedItems, getContext());
                        receiptItems.setAdapter(receiptAdapter);
                    }
                }
        );

        askPermission();


        // When user clicks on email button, send email to user with receipt
        emailButton.setOnClickListener(v -> Toast.makeText(getContext(), "Email Button Works", Toast.LENGTH_SHORT).show());

        // When user clicks on print button, print receipt
        printButton.setOnClickListener(v -> {
                createPDF();
                Toast.makeText(getContext(), "Print Button Works", Toast.LENGTH_SHORT).show();
            }

        );

        // When user clicks on share button, share receipt
        shareButton.setOnClickListener(v -> Toast.makeText(getContext(), "Share Button Works", Toast.LENGTH_SHORT).show());

    }

    private void createPDF() {
        if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            
            View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_receipt, null);
            DisplayMetrics displayMetrics = new DisplayMetrics();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getActivity().getDisplay().getRealMetrics(displayMetrics);
            } else {
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            }

            view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

            PdfDocument document = new PdfDocument();

            int viewWidth = view.getMeasuredWidth();
            int viewHeight = view.getMeasuredHeight();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            view.draw(canvas);

            document.finishPage(page);


            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "exampleXML.pdf";
            File file = new File(downloadsDir, fileName);

            try {
                FileOutputStream fos = new FileOutputStream(file);
                document.writeTo(fos);
                document.close();
                fos.close();
                Toast.makeText(getContext(), "Converted to PDF Successfully!!!", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                Log.d("myLog", "File not found exception" + e.getMessage());
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        }, REQUEST_CODE);
    }

}