package com.example.chezelisma;

import static com.example.chezelisma.LocalFormat.getCurrencyFormat;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;



public class ReceiptFragment extends Fragment {

    final static int REQUEST_CODE = 1232;
    ArrayList<Orders> orders = new ArrayList<>();
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


        // When user clicks on email button, send email to user with receipt
        emailButton.setOnClickListener(v -> Toast.makeText(getContext(), "Email Button Works", Toast.LENGTH_SHORT).show());

        // When user clicks on print button, print receipt
        printButton.setOnClickListener(v -> {
                createPDF();
                Toast.makeText(getContext(), "Print", Toast.LENGTH_SHORT).show();
            }

        );

        // When user clicks on share button, share receipt
        shareButton.setOnClickListener(v -> Toast.makeText(getContext(), "Share Button Works", Toast.LENGTH_SHORT).show());

        }


    private void createPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String orderNumberString = Utils.formatOrderNumber(orders.get(0).getOrderNumber());

            // Create a ContentValues object to store the file's metadata
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Downloads.DISPLAY_NAME, "Receipt_" + orderNumberString + ".pdf");

            // Get the content resolver and insert the file into the Downloads directory
            ContentResolver contentResolver = getContext().getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

            try {
                // Open an output stream using the document's URI
                OutputStream outputStream = contentResolver.openOutputStream(uri);

                if (outputStream != null) {
                    // Define the page width and height in points (1/72 inch)
                    int pageWidth = 1280; // 8.5 inches (8.5 * 72 points)
                    int pageHeight = 1920; // 11 inches (11 * 72 points)

                    // Create the PDF document
                    PdfDocument document = new PdfDocument();
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);
                    Canvas canvas = page.getCanvas();

                    // Inflate the XML layout into a view
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_receipt, null);

                    LinearLayout receiptLayout = view.findViewById(R.id.receiptFooter);

                    // Update the view elements with data
                    RecyclerView receiptItems = view.findViewById(R.id.receiptItems);
                    TextView date = view.findViewById(R.id.receiptDate);
                    TextView time = view.findViewById(R.id.receiptTime);
                    TextView total = view.findViewById(R.id.receiptTotal);
                    TextView orderNumber = view.findViewById(R.id.receiptOrderNumber);

                    receiptItems.setLayoutManager(new LinearLayoutManager(getContext()));

                    if (!orders.isEmpty()) {
                        date.setText(orders.get(0).getOrderDate());
                        time.setText(orders.get(0).getOrderTime());
                        total.setText(getCurrencyFormat(orders.get(0).getOrderTotalAmount()));
                        orderNumber.setText(orderNumberString);

                        ArrayList<Items> selectedItems = orders.get(0).getSelectedItem();

                        // Create the adapter and set it to the RecyclerView
                        BottomSheetAdapter receiptAdapter = new BottomSheetAdapter(selectedItems, getContext());
                        receiptItems.setAdapter(receiptAdapter);
                        receiptLayout.setVisibility(View.GONE);
                    }


                    // Measure and draw the updated view onto the PDF canvas
                    view.measure(View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY));
                    view.layout(0, 0, pageWidth, pageHeight);
                    view.draw(canvas);

                    document.finishPage(page);

                    // Write the PDF content to the output stream
                    document.writeTo(outputStream);

                    // Close the output stream and the PDF document
                    outputStream.close();
                    document.close();

                    Log.i("PDF", "PDF Created Successfully!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Handle versions prior to Android 10 if needed
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false);
    }

}