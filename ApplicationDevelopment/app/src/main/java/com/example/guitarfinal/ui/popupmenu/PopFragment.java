package com.example.guitarfinal.ui.popupmenu;

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

public class PopFragment extends Fragment {

    List<Preset> p;

    private PopViewModel popViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        popViewModel =
                new ViewModelProvider(this).get(PopViewModel.class);
        View root = inflater.inflate(R.layout.popup, container, false);
        final TextView textView = root.findViewById(R.id.text_home);


        Button b1 = (Button)root.findViewById(R.id.button9);
        Button b2 = (Button)root.findViewById(R.id.button10);
        Button b3 = (Button)root.findViewById(R.id.button11);
        Button b4 = (Button)root.findViewById(R.id.button12);
        Button b5 = (Button)root.findViewById(R.id.button13);
        Button b6 = (Button)root.findViewById(R.id.button14);
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