//|---------------------------------------------------------------------------|
//|    CLASS    : EditFragment.java by Alex Gennero                           |
//|                                                                           |
//|    PURPOSE  : Creates the view for Edit Page                              |
//|                                                                           |
//|---------------------------------------------------------------------------|


package com.example.guitarfinal.ui.presetEdit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.guitarfinal.MainActivity;
import com.example.guitarfinal.R;
import com.example.guitarfinal.data.Preset;
import com.google.android.material.textfield.TextInputEditText;

public class EditFragment extends Fragment {

    private EditViewModel editViewModel;
    Preset preset;
    Button saveButton2;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editViewModel =
                new ViewModelProvider(this).get(EditViewModel.class);
        view = inflater.inflate(R.layout.fragment_edit, container, false);

        //gets the preset that was selected when on popup.
        preset = ((MainActivity)getActivity()).getSelectedPreset();

        //Setting switches, buttons, & text box by finding them on the fragment_edit.xml
        Switch switch1 = (Switch)view.findViewById(R.id.switch1);
        Switch switch2 = (Switch)view.findViewById(R.id.switch2);
        Switch switch3 = (Switch)view.findViewById(R.id.switch3);
        Switch switch4 = (Switch)view.findViewById(R.id.switch4);
        Switch switch5 = (Switch)view.findViewById(R.id.switch5);
        Switch switch6 = (Switch)view.findViewById(R.id.switch6);
        Switch switch7 = (Switch)view.findViewById(R.id.switch7);
        Switch switch8 = (Switch)view.findViewById(R.id.switch8);
        TextInputEditText name = (TextInputEditText)view.findViewById(R.id.editedText);
        saveButton2 = (Button) view.findViewById(R.id.saveButton);

        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When save button is pressed it sets the presets values.
                preset.channel1 = switch1.isChecked();
                preset.channel2 = switch2.isChecked();
                preset.channel3 = switch3.isChecked();
                preset.channel4 = switch4.isChecked();
                preset.channel5 = switch5.isChecked();
                preset.channel6 = switch6.isChecked();
                preset.channel7 = switch7.isChecked();
                preset.channel8 = switch8.isChecked();
                preset.presetName = name.getEditableText().toString();
                ((MainActivity) getActivity()).savePreset(preset); //Calls savePreset method inside of MainActivity.java
                Toast.makeText(getActivity(),(preset.presetName + " has been saved."),Toast.LENGTH_SHORT).show();
                moveToNewActivity();
            }
        });

        //Setting if the switches are turned on depending if preset channel values are True/False.
        switch1.setChecked(preset.channel1);
        switch2.setChecked(preset.channel2);
        switch3.setChecked(preset.channel3);
        switch4.setChecked(preset.channel4);
        switch5.setChecked(preset.channel5);
        switch6.setChecked(preset.channel6);
        switch7.setChecked(preset.channel7);
        switch8.setChecked(preset.channel8);
        name.setText(preset.presetName);

        return view;
    }

    /** Author: Alex Gennero
     *  Purpose: Sends user back to main page once saved.
     */
    private void moveToNewActivity () {
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        ((MainActivity) getActivity()).overridePendingTransition(0, 0);

    }
}