package com.example.guitarfinal.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.guitarfinal.MainActivity;
import com.example.guitarfinal.R;
import com.example.guitarfinal.data.Preset;

import java.util.List;

public class DashboardFragment extends Fragment {

    List<Preset> p;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });



        Button b1 = (Button)root.findViewById(R.id.button1);
        Button b2 = (Button)root.findViewById(R.id.button2);
        Button b3 = (Button)root.findViewById(R.id.button3);
        Button b4 = (Button)root.findViewById(R.id.button4);
        Button b5 = (Button)root.findViewById(R.id.button5);
        Button b6 = (Button)root.findViewById(R.id.button6);

        p = ((MainActivity)getActivity()).returnList();
        b1.setText(p.get(0).presetName);
        b2.setText(p.get(1).presetName);
        b3.setText(p.get(2).presetName);
        b4.setText(p.get(3).presetName);
        b5.setText(p.get(4).presetName);
        b6.setText(p.get(5).presetName);

        return root;
    }
}