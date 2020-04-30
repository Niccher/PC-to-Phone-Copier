package com.niccher.pctophonecopier.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.niccher.pctophonecopier.R;

public class Fragment_Home extends Fragment {

    AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_home,container,false);

        Button cam = view.findViewById(R.id.btn_camvw);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan text to copy");
                integrator.setOrientationLocked(false);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                //integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

                CameraManager camma= (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
                String camid=null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        //camid=camma.getCameraIdList(0);
                        camid=camma.getCameraIdList()[0];
                        //camma.setTorchMode(camid,true);
                    }catch (CameraAccessException ex){
                        Toast.makeText(getActivity(), "Error--> "+ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getActivity(), "Hallo", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(getActivity(), "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getActivity(), result.getContents(),Toast.LENGTH_LONG).show();
                final String pars=String.valueOf(result.getContents());
                builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("QR Contents...");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        try {
                            String selectedText = String.valueOf(result.getContents());
                            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Kopied", selectedText);
                            if (clipboard == null || clip == null) return;
                            clipboard.setPrimaryClip(clip);

                            Toast.makeText(getContext(), "Copied >\n"+selectedText, Toast.LENGTH_SHORT).show();

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
