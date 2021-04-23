//|---------------------------------------------------------------------------|
//|    FILE NAME: MainActivity.java                                           |
//|                                                                           |
//|    AUTHOR   :  Alex Gennero & Mitchell Murphy                             |
//|                                                                           |
//|    PURPOSE  :  When opening the app it called the onCreate method         |
//|                Main activity also controls the buttons used on the app    |
//|                                                                           |
//|---------------------------------------------------------------------------|


package com.example.guitarfinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.guitarfinal.data.AppDatabase;
import com.example.guitarfinal.data.Preset;
import com.example.guitarfinal.data.PresetDao;
import com.example.guitarfinal.ui.presetEdit.EditFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//import android.widget.AdapterView.OnClickListener;


public class MainActivity extends AppCompatActivity {

    //Define Variables needed
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button preset1Select,preset2Select,preset3Select,preset4Select,preset5Select,preset6Select;
    Preset selectedPreset;
    EditFragment editFrag;
    AppDatabase db;
    ImageView image_view;
    LayoutInflater imageViewF;
    View imageView;
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
    Bitmap bitmap;
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * Author: Alex Gennero
     * Purpose: This function returns the Preset that is selected when opening Dialog to edit a Preset.
     * @return selectedPreset
     */
    public Preset getSelectedPreset() {
        return selectedPreset;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public MainActivity() {
    }

    public static final class layout{
        public static final int myImage = 1000360;
    }


    /** Author: Alex Gennero
     *  Purpose: When the application launches the OnCreate() function runs that shows the Home Page and the Bottom Navigation Menu.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageViewF = getLayoutInflater();
        imageView = imageViewF.inflate(R.layout.fragment_home, null);
        image_view = (ImageView) imageView.findViewById(R.id.myImage);
//        setContentView(R.layout.fragment_home);
//        View view = getLayoutInflater().inflate(R.layout.fragment_home,null);
//        image_view = findViewById(R.id.myImage);
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

        //Database Creator
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "GuitarLooperDatabase")
                .allowMainThreadQueries()
                .build();

        presetDao = db.presetDao();
        presets = presetDao.getAll();

        //If the preset is empty, it will call getOrCreateNewPreset which will insert the preset from the Room Database.
        preset1 = getOrCreateNewPreset(presets.size() > 0 ? presets.get(0) : null);
        preset2 = getOrCreateNewPreset(presets.size() > 1 ? presets.get(1) : null);
        preset3 = getOrCreateNewPreset(presets.size() > 2 ? presets.get(2) : null);
        preset4 = getOrCreateNewPreset(presets.size() > 3 ? presets.get(3) : null);
        preset5 = getOrCreateNewPreset(presets.size() > 4 ? presets.get(4) : null);
        preset6 = getOrCreateNewPreset(presets.size() > 5 ? presets.get(5) : null);

        //sets image whenever app restarts.
        if(preset1.picture != null) {
            bitmap = BitmapFactory.decodeByteArray(preset1.picture, 0, (preset1.picture).length);
            image_view.setImageBitmap(bitmap);
        }
    }

    /** Author: Alex Gennero
     *  Purpose: In order to change the names of the buttons on the fragment_dashboard.xml when updated in the Room Database
     *           you need to get all of the presets and return them in DashboardFragment.java
     * @return presets
     */
    public List<Preset> returnList(){
        presets = presetDao.getAll();
        return presets;
    }

    public Preset getImage(){
        return preset1;
    }

    /** Author: Alex Gennero
     *  Purpose: Saves the preset to the Room Database.
     * @param preset
     */
    public void savePreset(Preset preset){
        PresetDao presetDao = db.presetDao();
        presetDao.update(preset);
    }

    /** Author: Alex Gennero
     *  Purpose: Creates the edit button on the top right of the screen.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editmenu, menu);
        return true;
    }

    /** Author: Alex Gennero
     *  Purpose: When pressing the menu that is created in onCreateOptionsMenu() it will call selectEditPreset()
     * @param item
     * @return
     */
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

    /** Author: Alex Gennero
     *  Purpose: Creates a pop up menu that allows the user to select which preset they would like to edit.
     */
    public void selectEditPreset(){
        dialogBuilder = new AlertDialog.Builder(this); //Creates the popup dialog
        View editPresetView = getLayoutInflater().inflate(R.layout.popup, null); //Creates the view for the popup

        //Identify which buttons are on the popup menu.
        preset1Select = (Button) editPresetView.findViewById(R.id.button9);
        preset2Select = (Button) editPresetView.findViewById(R.id.button10);
        preset3Select = (Button) editPresetView.findViewById(R.id.button11);
        preset4Select = (Button) editPresetView.findViewById(R.id.button12);
        preset5Select = (Button) editPresetView.findViewById(R.id.button13);
        preset6Select = (Button) editPresetView.findViewById(R.id.button14);

        //Changes the name of the buttons to the name defined in Room Database
        preset1Select.setText(preset1.presetName);
        preset2Select.setText(preset2.presetName);
        preset3Select.setText(preset3.presetName);
        preset4Select.setText(preset4.presetName);
        preset5Select.setText(preset5.presetName);
        preset6Select.setText(preset6.presetName);

        //Shows the popup on screen
        dialogBuilder.setView(editPresetView);
        dialog = dialogBuilder.create();
        dialog.show();

        //When a button is pressed it sets the selectedPreset to the corresponding preset.
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

    /** Author: Mitchell Murphy
     *  Purpose:
     * @param v
     */
    public void btnClick(View v){
        image_view = findViewById(R.id.myImage);
        btn = findViewById(R.id.cb);
        openImageForm();
    }

    /** Author: Mitchell Murphy
     *  Purpose: Opens the devices photos folder so the user can select an image for their home page background
     */
    private void openImageForm() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CODE);
    }

    /** Author: Alex Gennero & Mitchell Murphy
     *  Purpose: Receives image selected and sets to HomeBackground. Also, saves the Picture to the Room Database as a Byte[]
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_CODE && resultCode==RESULT_OK &&
                data != null && data.getData() != null){
            imageUri = data.getData();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                Bitmap bm = getBitmapFromUri(imageUri);
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                preset1.picture = baos.toByteArray();
                bm.recycle();
                bitmap = BitmapFactory.decodeByteArray(preset1.picture, 0, (preset1.picture).length);
                image_view.setImageBitmap(bitmap);
                savePreset(preset1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Author: Alex Gennero
     *  Purpose: Transfers Uri into a Bitmap.
     * @param u
     * @return
     * @throws IOException
     */
    private Bitmap getBitmapFromUri(Uri u) throws IOException {
        ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(u, "r");
        FileDescriptor fd = pfd.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fd);
        pfd.close();
        return image;
    }

    /** Author: Alex Gennero
     *  Purpose: When assigning preset1, ..., & preset6 to the data in our Room Database. This function checks if it is already filled. If it isn't it creates a new Preset and inserts the information.
     * @param preset
     * @return
     */
    Preset getOrCreateNewPreset(Preset preset) {
        if (preset == null) {
            preset = new Preset();
            PresetDao presetDao = db.presetDao();
            presetDao.insertAll(preset);
        }
        return preset;
    }

    /** Author: Mitchell Murphy
     *  Purpose: This function checks to see what version of Android the users device is running and then requests permission if they are running Android lollipop or newer.
     */
    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionsCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionsCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionsCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }

        }
    }

    /** Author: Mitchell Murphy
     *  Purpose: (onPreset1Clicked - onPreset6Clicked)
     *          1. Create a new thread in order to handle the data being sent so it does not overload the main thread.
     *          2. Check the Bluetooth permissions on the device and make sure that the user can use Bluetooth on their device
     *          3. Establish a connection to the ESP-32
     *          4. Output the data that is stored in the preset
     *          5. Close the socket
     * @param v
     */
    public void onPreset1Clicked(View v){
        new Thread(() -> {
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            bluetoothAdapter = manager.getAdapter();
            checkBTPermissions();
            BluetoothDevice esp32 = bluetoothAdapter.getRemoteDevice("84:CC:A8:5C:F4:8E");
            System.out.println(esp32.getName());
            BluetoothSocket btSocket = null;

            int counter = 0;
            do {
                try {
                    btSocket = esp32.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Service Record: " + btSocket);
                    btSocket.connect();
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(!btSocket.isConnected() && counter < 3);


            try {
                OutputStream outputStream = btSocket.getOutputStream();

                boolean[] ar = {preset1.channel1, preset1.channel2, preset1.channel3, preset1.channel4, preset1.channel5, preset1.channel6, preset1.channel7, preset1.channel8};
                String binaryStr = "";
                for (boolean bit : ar)
                    binaryStr += bit ? "1" : "0";
                int dec = Integer.parseInt(binaryStr, 2);
                String hexStr = Integer.toString(dec, 16);

                System.out.println("Hex of boolean values: " + hexStr); //For demo purposes

                outputStream.write(hexStr.getBytes());



            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                btSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    public void onPreset2Clicked(View v){
        new Thread(() -> {

            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            bluetoothAdapter = manager.getAdapter();
            BluetoothDevice esp32 = bluetoothAdapter.getRemoteDevice("84:CC:A8:5C:F4:8E");
            System.out.println(esp32.getName());
            BluetoothSocket btSocket = null;
            int counter = 0;
            do {
                try {
                    btSocket = esp32.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Service Record: " + btSocket);

                    btSocket.connect();
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(!btSocket.isConnected() && counter < 3);

            try{
                OutputStream outputStream = btSocket.getOutputStream();
                boolean[] ar = {preset2.channel1, preset2.channel2, preset2.channel3, preset2.channel4, preset2.channel5, preset2.channel6, preset2.channel7, preset2.channel8};
                String binaryStr = "";
                for (boolean bit : ar)
                    binaryStr += bit ? "1" : "0";
                int dec = Integer.parseInt(binaryStr, 2);
                String hexStr = Integer.toString(dec, 16);

                System.out.println("Hex of boolean values: " + hexStr); //For demo purposes

                outputStream.write(hexStr.getBytes());



            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                btSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    public void onPreset3Clicked(View v){
        new Thread(() -> {
            checkBTPermissions();
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            bluetoothAdapter = manager.getAdapter();
            BluetoothDevice esp32 = bluetoothAdapter.getRemoteDevice("84:CC:A8:5C:F4:8E");
            System.out.println(esp32.getName());

            BluetoothSocket btSocket = null;

            int counter = 0;
            do {
                try {
                    btSocket = esp32.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Service Record: " + btSocket);

                    btSocket.connect();
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(!btSocket.isConnected() && counter < 3);

            try{
                OutputStream outputStream = btSocket.getOutputStream();
                boolean[] ar = {preset3.channel1, preset3.channel2, preset3.channel3, preset3.channel4, preset3.channel5, preset3.channel6, preset3.channel7, preset3.channel8};
                String binaryStr = "";
                for (boolean bit : ar)
                    binaryStr += bit ? "1" : "0";
                int dec = Integer.parseInt(binaryStr, 2);
                String hexStr = Integer.toString(dec, 16);

                System.out.println("Hex of boolean values: " + hexStr); //For demo purposes

                outputStream.write(hexStr.getBytes());


            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                btSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();

    }

    /**
     *
     * @param v
     */
    public void onPreset4Clicked(View v){
        new Thread(() -> {
            checkBTPermissions();
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            bluetoothAdapter = manager.getAdapter();
            BluetoothDevice esp32 = bluetoothAdapter.getRemoteDevice("84:CC:A8:5C:F4:8E");
            System.out.println(esp32.getName());

            BluetoothSocket btSocket = null;

            int counter = 0;
            do {
                try {
                    btSocket = esp32.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Service Record: " + btSocket);

                    btSocket.connect();
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(!btSocket.isConnected() && counter < 3);

            try{
                OutputStream outputStream = btSocket.getOutputStream();
                boolean[] ar = {preset4.channel1, preset4.channel2, preset4.channel3, preset4.channel4, preset4.channel5, preset4.channel6, preset4.channel7, preset4.channel8};
                String binaryStr = "";
                for (boolean bit : ar)
                    binaryStr += bit ? "1" : "0";
                int dec = Integer.parseInt(binaryStr, 2);
                String hexStr = Integer.toString(dec, 16);

                System.out.println("Hex of boolean values: " + hexStr); //For demo purposes

                outputStream.write(hexStr.getBytes());


            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                btSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }).start();
    }

    public void onPreset5Clicked(View v){

        new Thread(() -> {
            checkBTPermissions();
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            bluetoothAdapter = manager.getAdapter();
            BluetoothDevice esp32 = bluetoothAdapter.getRemoteDevice("84:CC:A8:5C:F4:8E");
            System.out.println(esp32.getName());

            BluetoothSocket btSocket = null;

            int counter = 0;
            do {
                try {
                    btSocket = esp32.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Service Record: " + btSocket);

                    btSocket.connect();
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected()); //For demo purposes
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(!btSocket.isConnected() && counter < 3);

            try{
                OutputStream outputStream = btSocket.getOutputStream();
                boolean[] ar = {preset5.channel1, preset5.channel2, preset5.channel3, preset5.channel4, preset5.channel5, preset5.channel6, preset5.channel7, preset5.channel8};
                String binaryStr = "";
                for (boolean bit : ar)
                    binaryStr += bit ? "1" : "0";
                int dec = Integer.parseInt(binaryStr, 2);
                String hexStr = Integer.toString(dec, 16);

                System.out.println("Hex of boolean values: " + hexStr); //For demo purposes

                outputStream.write(hexStr.getBytes());


            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                btSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }).start();

    }

    public void onPreset6Clicked(View v) {

        new Thread(() -> {
            checkBTPermissions();
            BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
            bluetoothAdapter = manager.getAdapter();
            BluetoothDevice esp32 = bluetoothAdapter.getRemoteDevice("84:CC:A8:5C:F4:8E");
            System.out.println(esp32.getName());

            BluetoothSocket btSocket = null;

            int counter = 0;
            do {
                try {
                    btSocket = esp32.createRfcommSocketToServiceRecord(MY_UUID);
                    System.out.println("Service Record: " + btSocket);

                    btSocket.connect();
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (!btSocket.isConnected() && counter < 3);

            try {
                OutputStream outputStream = btSocket.getOutputStream();
                boolean[] ar = {preset6.channel1, preset6.channel2, preset6.channel3, preset6.channel4, preset6.channel5, preset6.channel6, preset6.channel7, preset6.channel8};
                String binaryStr = "";
                for (boolean bit : ar)
                    binaryStr += bit ? "1" : "0";
                int dec = Integer.parseInt(binaryStr, 2);
                String hexStr = Integer.toString(dec, 16);

                System.out.println("Hex of boolean values: " + hexStr); //For demo purposes

                outputStream.write(hexStr.getBytes());



            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

}