package com.niccher.pctophonecopier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.niccher.pctophonecopier.fragments.Fragment_History;
import com.niccher.pctophonecopier.fragments.Fragment_Home;
import com.niccher.pctophonecopier.fragments.Fragment_Image;
import com.niccher.pctophonecopier.fragments.Fragment_QR;

public class Dope extends AppCompatActivity {

    FrameLayout frameLayout;
    BubbleNavigationConstraintView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dope);
        frameLayout = findViewById(R.id.frame);

        bubbleNavigationLinearView = findViewById(R.id.bub_navigation_view_linear);

    }

    @Override
    protected void onStart() {
        super.onStart();

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                Fragment selectedFragm;

                switch (position){
                    case 0:
                        selectedFragm = new Fragment_Home();
                        break;
                    case 1:
                        selectedFragm = new Fragment_QR();
                        break;
                    case 2:
                        selectedFragm = new Fragment_Image();
                        break;
                    case 3:
                        selectedFragm = new Fragment_History();
                        break;
                    default:
                        selectedFragm = new Fragment_Home();
                }

                goToSelectedFragment(selectedFragm);


            }
        });

        goToSelectedFragment(new Fragment_Home());
    }

    private void goToSelectedFragment(Fragment selectedFragm) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, selectedFragm);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }
}
