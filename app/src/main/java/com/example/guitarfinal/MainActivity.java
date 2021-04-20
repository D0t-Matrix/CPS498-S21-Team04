package com.example.guitarfinal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
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
    Button listen, send, listDevices;
    ListView listView;
    TextView msg_box, status;
    EditText writeMsg;

    SendReceive sendReceive;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    private static final String APP_NAME = "BTConnect";
    private static final UUID MY_UUID = UUID.fromString("8ce255c0-233a-11e0-ac64-0803450c9a66");


    //______________________________________________________________________________
    private void findViewByIdes(){
        listen = (Button) findViewById(R.id.listen);
        send = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.DL);
        msg_box = (TextView) findViewById(R.id.SentInformation);
        status = (TextView) findViewById(R.id.Status);
        writeMsg = (EditText) findViewById(R.id.EnterText);
        listDevices = (Button) findViewById(R.id.listDevice);
        bluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
    }
    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionsCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionsCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionsCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }
        }
    }



    public void onShowDevicesClicked(View v){
        status = (TextView) findViewById(R.id.Status);
        if(!bluetoothAdapter.isEnabled()){
            checkBTPermissions();
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }
        ServerClass serverClass = new ServerClass();
        serverClass.start();
      
    }


    public void onListDevicesClicked(View v){
        listView =  findViewById(R.id.DL);
        checkBTPermissions();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionsCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionsCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionsCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            }
        }

        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();
        bluetoothAdapter  = BluetoothAdapter.getDefaultAdapter();
        String[] strings = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
         int index = 0;

        if(bt.size() > 0){
            for(BluetoothDevice device: bt){
                btArray[index] = device;
                strings[index] = device.getName();
                index++;
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings);
            listView.setAdapter(arrayAdapter);

        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass = new ClientClass(btArray[i]);
                clientClass.start();

                status.setText("Connecting");
            }
        });


    }

    public void onSendClicked(View v){
        writeMsg = (EditText) findViewById(R.id.EnterText);
        msg_box = (TextView) findViewById(R.id.SentInformation);
        String string  = String.valueOf(writeMsg.getText());
        sendReceive.write(string.getBytes());
    }

    public void onPresetClicked(View v){
            switch (v.getId()) {
                case R.id.button1:
                    String string = String.valueOf(preset1.channel1 + " " + preset1.channel2 + " " + preset1.channel3 + " " + preset1.channel4 + " " + preset1.channel5 + " " + preset1.channel6 + " " + preset1.channel7 + " " + preset1.channel8);
                    System.out.println(preset1.channel1 + " " + preset1.channel2 + " " + preset1.channel3 + " " + preset1.channel4 + " " + preset1.channel5 + " " + preset1.channel6 + " " + preset1.channel7 + " " + preset1.channel8);
                    sendReceive.write(string.getBytes());
                    break;
                case R.id.button2:
                    String string2 = String.valueOf(preset2.channel1 + " " + preset2.channel2 + " " + preset2.channel3 + " " + preset2.channel4 + " " + preset2.channel5 + " " + preset2.channel6 + " " + preset2.channel7 + " " + preset2.channel8);
                    System.out.println(preset2.channel1 + " " + preset2.channel2 + " " + preset2.channel3 + " " + preset2.channel4 + " " + preset2.channel5 + " " + preset2.channel6 + " " + preset2.channel7 + " " + preset2.channel8);
                    sendReceive.write(string2.getBytes());
                    break;
                case R.id.button3:
                    String string3 = String.valueOf(preset3.channel1 + " " + preset3.channel2 + " " + preset3.channel3 + " " + preset3.channel4 + " " + preset3.channel5 + " " + preset3.channel6 + " " + preset3.channel7 + " " + preset3.channel8);
                    System.out.println(preset1.channel3 + " " + preset1.channel3 + " " + preset3.channel3 + " " + preset3.channel4 + " " + preset3.channel5 + " " + preset3.channel6 + " " + preset3.channel7 + " " + preset3.channel8);
                    sendReceive.write(string3.getBytes());
                    break;
                case R.id.button4:
                    String string4 = String.valueOf(preset4.channel1 + " " + preset4.channel2 + " " + preset4.channel3 + " " + preset4.channel4 + " " + preset4.channel5 + " " + preset4.channel6 + " " + preset4.channel7 + " " + preset4.channel8);
                    System.out.println(preset4.channel1 + " " + preset4.channel2 + " " + preset4.channel3 + " " + preset4.channel4 + " " + preset4.channel5 + " " + preset4.channel6 + " " + preset4.channel7 + " " + preset4.channel8);
                    sendReceive.write(string4.getBytes());
                    break;
                case R.id.button5:
                    String string5 = String.valueOf(preset5.channel1 + " " + preset5.channel2 + " " + preset5.channel3 + " " + preset5.channel4 + " " + preset5.channel5 + " " + preset5.channel6 + " " + preset5.channel7 + " " + preset5.channel8);
                    System.out.println(preset5.channel1 + " " + preset5.channel2 + " " + preset5.channel3 + " " + preset5.channel4 + " " + preset5.channel5 + " " + preset5.channel6 + " " + preset5.channel7 + " " + preset5.channel8);
                    sendReceive.write(string5.getBytes());
                    break;

                case R.id.button6:
                    String string6 = String.valueOf(preset6.channel1 + " " + preset6.channel2 + " " + preset6.channel3 + " " + preset6.channel4 + " " + preset6.channel5 + " " + preset6.channel6 + " " + preset6.channel7 + " " + preset6.channel8);
                    System.out.println(preset6.channel1 + " " + preset6.channel2 + " " + preset6.channel3 + " " + preset6.channel4 + " " + preset6.channel5 + " " + preset6.channel6 + " " + preset6.channel7 + " " + preset6.channel8);
                    sendReceive.write(string6.getBytes());
                    break;

            }

    }


    Handler handler = new Handler(new Handler.Callback(){
        @Override
       public boolean handleMessage(Message msg){
            switch (msg.what){
                case STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff,0,msg.arg1);
                    msg_box.setText(tempMsg);
                    break;
            }
            return true;
       }
    });

    private class ServerClass extends Thread{
        private BluetoothServerSocket serverSocket;

        public ServerClass(){
            try {
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void run(){
            BluetoothSocket socket = null;
            while(socket == null){
                try {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);

                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);
                }
                if(socket != null){
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);

                    sendReceive = new SendReceive(socket);
                    sendReceive.start();

                    break;
                }
            }
        }
    }

    private class ClientClass extends Thread{
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1){

            device = device1;

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        public void run(){

            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

            }

        }

    }

    private class SendReceive extends Thread{
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive(BluetoothSocket socket){
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut  = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;

        }
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        public void write(byte[] bytes){
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
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

        //____________________BLUETOOTH____________________________
            findViewByIdes();

        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();


        //__________________________________________________________
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

}