package com.example.guitarfinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guitarfinal.data.AppDatabase;
import com.example.guitarfinal.data.Preset;
import com.example.guitarfinal.data.PresetDao;
import com.example.guitarfinal.ui.presetEdit.EditFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import android.widget.AdapterView.OnClickListener;


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



    //___________________________BLUETOOTH__________________________________________

    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH = 1;




    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

//    public void onEnableClicked(View v){
//        if(!bluetoothAdapter.isEnabled()){
//            checkBTPermissions();
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
//        }
//
//
//    }





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
        preset1.picture = null;

    }




    public List<Preset> returnList(){
        presets = presetDao.getAll();
        return presets;
    }

    public Preset getImage(){
        return preset1;
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
        preset1Select.setText(preset1.presetName);
        preset2Select.setText(preset2.presetName);
        preset3Select.setText(preset3.presetName);
        preset4Select.setText(preset4.presetName);
        preset5Select.setText(preset5.presetName);
        preset6Select.setText(preset6.presetName);

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

    ///<summary> Mitchell Murphy
    ///
    ///</summary>
    public void btnClick(View v){
        image_view = findViewById(R.id.myImage);
        btn = findViewById(R.id.cb);

        openImageForm();

    }
    ///<summary> Mitchell Murphy
    /// Opens the devices photos folder so the user can select an image for their home page background
    ///</summary>
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

            preset1.picture = imageUri.toString();
            image_view.setImageURI(Uri.parse(preset1.picture));
            savePreset(preset1);

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


    ///<summary> Mitchell Murphy
    /// This function checks to see what version of Android the users device is running and then requests permission if they are running Android lollipop or newer.
    ///</summary>
    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionsCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionsCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionsCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }

        }
    }

    ///<summary>Mitchell Murphy
    /// (onPreset1Clicked - onPreset6Clicked)
    /// 1st) Create a new thread in order to handle the data being sent so it does not overload the main thread.
    /// 2nd) Check the Bluetooth permissions on the device and make sure that the user can use Bluetooth on their device
    /// 3rd) Establish a connection to the ESP-32
    /// 4th) Output the data that is stored in the preset
    /// 5th) Close the socket
    ///</summary>
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
                String string = String.valueOf(preset1.channel1 + " " + preset1.channel2 + " " + preset1.channel3 + " " + preset1.channel4 + " " + preset1.channel5 + " " + preset1.channel6 + " " + preset1.channel7 + " " + preset1.channel8);

                System.out.println("Channel Values in String: " + string);
                StringBuffer sb = new StringBuffer();

                //Converts string to string of 1s and 0s
                char ch[] = string.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == 't') {
                         String ints = ("1 ");
                         sb.append(ints);
                    } else if(ch[i] == 'f') {
                        String ints = ("0 ");
                        sb.append(ints);
                    }
                }
                String result = sb.toString();
                System.out.println("String Array of Integers: " + result);

                //Converts string to integer array.
                String[] splited = result.split(" ");
                int[] numbers = new int[splited.length];
                for(int x = 0; x < splited.length; x++){
                    numbers[x] = Integer.parseInt(splited[x]);

                }
                System.out.println("Integer Array: " + Arrays.toString(numbers));

                //Converts integer array to hex.
                StringBuffer toHex = new StringBuffer();
                for(int y = 0; y < numbers.length; y++){
                    String hexString = Integer.toHexString(numbers[y]);
                    toHex.append(hexString);
                }

                String hexResult = toHex.toString();
                System.out.println(hexResult);

                outputStream.write(hexResult.getBytes());



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
                String string = String.valueOf(preset2.channel1 + " "+ preset2.channel2 + " "+ preset2.channel3 + " "+ preset2.channel4 + " " + preset2.channel5 + " "+ preset2.channel6 + " " +preset2.channel7 + " "+ preset2.channel8 );

                System.out.println("Channel Values in String: " + string);
                StringBuffer sb = new StringBuffer();

                //Converts string to string of 1s and 0s
                char ch[] = string.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == 't') {
                        String ints = ("1 ");
                        sb.append(ints);
                    } else if(ch[i] == 'f') {
                        String ints = ("0 ");
                        sb.append(ints);
                    }
                }
                String result = sb.toString();
                System.out.println("String Array of Integers: " + result);

                //Converts string to integer array.
                String[] splited = result.split(" ");
                int[] numbers = new int[splited.length];
                for(int x = 0; x < splited.length; x++){
                    numbers[x] = Integer.parseInt(splited[x]);

                }
                System.out.println("Integer Array: " + Arrays.toString(numbers));

                //Converts integer array to hex.
                StringBuffer toHex = new StringBuffer();
                for(int y = 0; y < numbers.length; y++){
                    String hexString = Integer.toHexString(numbers[y]);
                    toHex.append(hexString);
                }

                String hexResult = toHex.toString();
                System.out.println(hexResult);

                outputStream.write(hexResult.getBytes());



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
                String string = String.valueOf(preset3.channel3 + " "+ preset3.channel2 + " "+ preset3.channel3 + " "+ preset3.channel4 + " " + preset3.channel5 + " "+ preset3.channel6 + " " +preset3.channel7 + " "+ preset3.channel8 );

                System.out.println("Channel Values in String: " + string);
                StringBuffer sb = new StringBuffer();

                //Converts string to string of 1s and 0s
                char ch[] = string.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == 't') {
                        String ints = ("1 ");
                        sb.append(ints);
                    } else if(ch[i] == 'f') {
                        String ints = ("0 ");
                        sb.append(ints);
                    }
                }
                String result = sb.toString();
                System.out.println("String Array of Integers: " + result);

                //Converts string to integer array.
                String[] splited = result.split(" ");
                int[] numbers = new int[splited.length];
                for(int x = 0; x < splited.length; x++){
                    numbers[x] = Integer.parseInt(splited[x]);

                }
                System.out.println("Integer Array: " + Arrays.toString(numbers));

                //Converts integer array to hex.
                StringBuffer toHex = new StringBuffer();
                for(int y = 0; y < numbers.length; y++){
                    String hexString = Integer.toHexString(numbers[y]);
                    toHex.append(hexString);
                }

                String hexResult = toHex.toString();
                System.out.println(hexResult);

                outputStream.write(hexResult.getBytes());





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
                String string = String.valueOf(preset4.channel3 + " "+ preset4.channel2 + " "+ preset4.channel3 + " "+ preset4.channel4 + " " + preset4.channel5 + " "+ preset4.channel6 + " " +preset4.channel7 + " "+ preset4.channel8 );

                System.out.println("Channel Values in String: " + string);
                StringBuffer sb = new StringBuffer();

                //Converts string to string of 1s and 0s
                char ch[] = string.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == 't') {
                        String ints = ("1 ");
                        sb.append(ints);
                    } else if(ch[i] == 'f') {
                        String ints = ("0 ");
                        sb.append(ints);
                    }
                }
                String result = sb.toString();
                System.out.println("String Array of Integers: " + result);

                //Converts string to integer array.
                String[] splited = result.split(" ");
                int[] numbers = new int[splited.length];
                for(int x = 0; x < splited.length; x++){
                    numbers[x] = Integer.parseInt(splited[x]);

                }
                System.out.println("Integer Array: " + Arrays.toString(numbers));

                //Converts integer array to hex.
                StringBuffer toHex = new StringBuffer();
                for(int y = 0; y < numbers.length; y++){
                    String hexString = Integer.toHexString(numbers[y]);
                    toHex.append(hexString);
                }

                String hexResult = toHex.toString();
                System.out.println(hexResult);

                outputStream.write(hexResult.getBytes());



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
                    System.out.println("Socket is connected (true/false): " + btSocket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(!btSocket.isConnected() && counter < 3);

            try{
                OutputStream outputStream = btSocket.getOutputStream();
                String string = String.valueOf(preset5.channel3 + " "+ preset5.channel2 + " "+ preset5.channel3 + " "+ preset5.channel4 + " " + preset5.channel5 + " "+ preset5.channel6 + " " +preset5.channel7 + " "+ preset5.channel8 );
                System.out.println("Channel Values in String: " + string);
                StringBuffer sb = new StringBuffer();

                //Converts string to string of 1s and 0s
                char ch[] = string.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == 't') {
                        String ints = ("1 ");
                        sb.append(ints);
                    } else if(ch[i] == 'f') {
                        String ints = ("0 ");
                        sb.append(ints);
                    }
                }
                String result = sb.toString();
                System.out.println("String Array of Integers: " + result);

                //Converts string to integer array.
                String[] splited = result.split(" ");
                int[] numbers = new int[splited.length];
                for(int x = 0; x < splited.length; x++){
                    numbers[x] = Integer.parseInt(splited[x]);

                }
                System.out.println("Integer Array: " + Arrays.toString(numbers));

                //Converts integer array to hex.
                StringBuffer toHex = new StringBuffer();
                for(int y = 0; y < numbers.length; y++){
                    String hexString = Integer.toHexString(numbers[y]);
                    toHex.append(hexString);
                }

                String hexResult = toHex.toString();
                System.out.println(hexResult);

                outputStream.write(hexResult.getBytes());



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
                String string = String.valueOf(preset6.channel3 + " " + preset6.channel2 + " " + preset6.channel3 + " " + preset6.channel4 + " " + preset6.channel5 + " " + preset6.channel6 + " " + preset6.channel7 + " " + preset6.channel8);

                System.out.println("Channel Values in String: " + string);
                StringBuffer sb = new StringBuffer();

                //Converts string to string of 1s and 0s
                char ch[] = string.toCharArray();
                for (int i = 0; i < ch.length; i++) {
                    if (ch[i] == 't') {
                        String ints = ("1 ");
                        sb.append(ints);
                    } else if(ch[i] == 'f') {
                        String ints = ("0 ");
                        sb.append(ints);
                    }
                }
                String result = sb.toString();
                System.out.println("String Array of Integers: " + result);

                //Converts string to integer array.
                String[] splited = result.split(" ");
                int[] numbers = new int[splited.length];
                for(int x = 0; x < splited.length; x++){
                    numbers[x] = Integer.parseInt(splited[x]);

                }
                System.out.println("Integer Array: " + Arrays.toString(numbers));

                //Converts integer array to hex.
                StringBuffer toHex = new StringBuffer();
                for(int y = 0; y < numbers.length; y++){
                    String hexString = Integer.toHexString(numbers[y]);
                    toHex.append(hexString);
                }

                String hexResult = toHex.toString();
                System.out.println(hexResult);

                outputStream.write(hexResult.getBytes());



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