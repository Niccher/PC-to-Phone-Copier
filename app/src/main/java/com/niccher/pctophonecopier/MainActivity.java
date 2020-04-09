package com.niccher.pctophonecopier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Button lg,camvw;
    AlertDialog.Builder builder;
    EditText bas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camvw= (Button) findViewById(R.id.btn_camvw);
        bas=findViewById(R.id.pasted);

        final Activity activity = this;

        camvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scanning for text to copy");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

                CameraManager camma= (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String camid=null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        //camid=camma.getCameraIdList(0);
                        camid=camma.getCameraIdList()[0];
                        //camma.setTorchMode(camid,true);
                    }catch (CameraAccessException ex){
                        Toast.makeText(activity, "Error--> "+ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                final String pars=String.valueOf(result.getContents());
                    builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("QR Contents...");
                    builder.setMessage(result.getContents());
                    builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            try {
                                String selectedText = String.valueOf(result.getContents());
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Kopied", selectedText);
                                if (clipboard == null || clip == null) return;
                                clipboard.setPrimaryClip(clip);

                                //bas.setText(selectedText);

                            } catch (Exception ex){
                            }

                        }
                    });

                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                    android.app.AlertDialog alertdialog = builder.create();
                    alertdialog.show();

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
