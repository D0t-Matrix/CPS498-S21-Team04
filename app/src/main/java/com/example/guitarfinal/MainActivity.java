package com.example.guitarfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.guitarfinal.data.AppDatabase;
import com.example.guitarfinal.data.Preset;
import com.example.guitarfinal.data.PresetDao;
import com.example.guitarfinal.ui.presetEdit.EditFragment;
import com.example.guitarfinal.ui.presetEdit.EditViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button preset1Select,preset2Select,preset3Select,preset4Select,preset5Select,preset6Select;

    //Button saveButton1 = (Button) findViewById(R.id.saveButton);
    Preset selectedPreset;

    public Preset getSelectedPreset() {
        return selectedPreset;
    }




    EditFragment editFrag;

    AppDatabase db;

    ImageView image_view;
    Button btn;
    Uri imageUri;
    public static final int IMAGE_CODE = 1;

    Preset preset1 = new Preset();
    Preset preset2 = new Preset();
    Preset preset3 = new Preset();
    Preset preset4 = new Preset();
    Preset preset5 = new Preset();
    Preset preset6 = new Preset();

    List<Preset> presets;
    PresetDao presetDao;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "GuitarLooperDatabase")
                .allowMainThreadQueries()
                .build();

        presetDao = db.presetDao();
        presets = presetDao.getAll();
        preset1 = getOrCreateNewPreset(presets.size() > 0 ? presets.get(0) : null);
        preset2 = getOrCreateNewPreset(presets.size() > 1 ? presets.get(1) : null);
        preset3 = getOrCreateNewPreset(presets.size() > 2 ? presets.get(2) : null);
        preset4 = getOrCreateNewPreset(presets.size() > 3 ? presets.get(3) : null);
        preset5 = getOrCreateNewPreset(presets.size() > 4 ? presets.get(4) : null);
        preset6 = getOrCreateNewPreset(presets.size() > 5 ? presets.get(5) : null);
    }

    public List<Preset> returnList(){
        presets = presetDao.getAll();
        return presets;
    }

    public void savePreset(Preset preset){
        PresetDao presetDao = db.presetDao();
        presetDao.update(preset);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.editMenu:
                selectEditPreset();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void selectEditPreset(){
        dialogBuilder = new AlertDialog.Builder(this);
        View editPresetView = getLayoutInflater().inflate(R.layout.popup, null);
        preset1Select = (Button) editPresetView.findViewById(R.id.button9);
        preset2Select = (Button) editPresetView.findViewById(R.id.button10);
        preset3Select = (Button) editPresetView.findViewById(R.id.button11);
        preset4Select = (Button) editPresetView.findViewById(R.id.button12);
        preset5Select = (Button) editPresetView.findViewById(R.id.button13);
        preset6Select = (Button) editPresetView.findViewById(R.id.button14);

        dialogBuilder.setView(editPresetView);
        dialog = dialogBuilder.create();
        dialog.show();


        View.OnClickListener clicker = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.button9:
                        selectedPreset = preset1;
                        break;
                    case R.id.button10:
                        selectedPreset = preset2;
                        break;
                    case R.id.button11:
                        selectedPreset = preset3;
                        break;
                    case R.id.button12:
                        selectedPreset = preset4;
                        break;
                    case R.id.button13:
                        selectedPreset = preset5;
                        break;
                    case R.id.button14:
                        selectedPreset = preset6;
                        break;
                    default:
                        break;

                }
                Navigation.findNavController((Activity)v.getContext(), R.id.nav_host_fragment).navigate(R.id.navigation_edit);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
                dialog.dismiss();


            }
        };


        preset1Select.setOnClickListener(clicker);
        preset2Select.setOnClickListener(clicker);
        preset3Select.setOnClickListener(clicker);
        preset4Select.setOnClickListener(clicker);
        preset5Select.setOnClickListener(clicker);
        preset6Select.setOnClickListener(clicker);



    }

    //methods allow you to open and select image for the home background image
    public void btnClick(View v){
        image_view = findViewById(R.id.myImage);
        btn = findViewById(R.id.cb);

        openImageForm();

    }

    private void openImageForm() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_CODE && resultCode==RESULT_OK &&
                data != null && data.getData() != null){

            imageUri = data.getData();
            //preset1.picture = imageUri.toString();
            image_view.setImageURI(imageUri);

        }

    }

    Preset getOrCreateNewPreset(Preset preset) {
        if (preset == null) {
            preset = new Preset();
            PresetDao presetDao = db.presetDao();
            presetDao.insertAll(preset);
        }

        return preset;
    }

}