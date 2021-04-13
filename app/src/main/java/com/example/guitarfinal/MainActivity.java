package com.example.guitarfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.guitarfinal.data.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button preset1Select,preset2Select,preset3Select,preset4Select,preset5Select,preset6Select;


    AppDatabase db;


    ImageView image_view;
    Button btn;
    Uri imageUri;
    public static final int IMAGE_CODE = 1;


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
                AppDatabase.class, "GuitarLooperDatabase").build();
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
        final View editPresetView = getLayoutInflater().inflate(R.layout.popup, null);
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

            image_view.setImageURI(imageUri);

        }

    }

}