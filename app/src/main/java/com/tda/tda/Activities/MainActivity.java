package com.tda.tda.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andreseko.SweetAlert.SweetAlertDialog;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sirvar.bluetoothkit.BluetoothKit;
import com.tda.tda.R;

import java.io.DataInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity  {

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(newBase);
//    }
//
//    LinearLayout btnNew,btnChangeAdmin,btnChangePass,btnReset,btnTurnOff,btnList,btnDelete;
//    RelativeLayout btnTurnOffShadow;
//    CardView btnTurnOffFlash;
//    BluetoothKit BTKit;
//    TextView txt;
//    boolean HasPermission=false;//To Check Have BLUETOOTH_CONNECTION Permission
//    boolean isConnected=false;//To Check Connected to a Device or not
//    boolean IsOnOrder=false;//To Check is Sending Command to device or not
//    boolean isShowingListOfDevice=false;//Is Device Dialog Show or not
//    boolean isSendingCommandRun=false;//To Check App is Sending Command
//
//    int MaxTriesToDiscover=0;//Counter Max Tries to Discover Devices
//    int TryToSendCommand=0;//Counter Max Tries to Send Command
//    int triesOn=0;//Counter Number of Tries to Turn Bluetooth ON
//    int TriesToConnect=0;
//
//    public final int BT_ADMIN_CONFIRM=3;//Command Needs to Accept Admin FingerPrint
//    public final int BT_ADMIN_ACCEPT=2;//Command Admin Accepted
//    public final int BT_ADMIN_ERROR=-2;//Command Admin wrong FingerPrint
//    public final int BT_CMD_SUCCESS=1;//Command Run Success
//    public final int BT_CMD_FAIL=-1;//Command Run Failed
//
//    protected SweetAlertDialog LoadingDialog;
//
//    protected String CommandString="";//Running Command String
//    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private BluetoothDevice myDevice=null;//Device that We Connected to
//    private BluetoothService service ;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        LoadingDialog=new SweetAlertDialog(MainActivity.this);
//        LoadingDialog.setTitle("درخال بررسی...");
//        txt = (TextView) findViewById(R.id.main_device_name_txt);
//        btnNew = (LinearLayout) findViewById(R.id.main_btn_new);
//        btnReset = (LinearLayout) findViewById(R.id.main_btn_reset);
//        btnDelete = (LinearLayout) findViewById(R.id.main_btn_delete_user);
//        btnChangeAdmin = (LinearLayout) findViewById(R.id.main_btn_change_admin);
//        btnChangePass = (LinearLayout) findViewById(R.id.main_btn_change_password);
//        btnList = (LinearLayout) findViewById(R.id.main_btn_list_user);
//        btnTurnOff = (LinearLayout) findViewById(R.id.main_turnoff);
//
//        btnTurnOffShadow=(RelativeLayout)findViewById(R.id.main_turnoff_shadow);
//        btnTurnOffFlash=(CardView)findViewById(R.id.main_turnoff_flash);
//
//        btnTurnOff.setOnClickListener(btnTurnOnListener);
//
//        BTKit = new BluetoothKit();
//
//        btnNew.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendCommand("New");
//            }
//        });
//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendCommand("Reset");
//            }
//        });
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendCommand("Delete");
//            }
//        });
//        btnChangeAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendCommand("ChangeAdmin");
//            }
//        });
//        btnChangePass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SendCommand("ChangePass");
//            }
//        });
//
////        BluetoothConfiguration config = new BluetoothConfiguration();
////        config.context = getApplicationContext();
////        config.bluetoothServiceClass = BluetoothClassicService.class;
////
////        config.bufferSize = 1024;
////        config.callListenersInMainThread = true;
////        BluetoothService.init(config);
////        service= BluetoothService.getDefaultInstance();
//    }
//
//    final View.OnClickListener btnTurnOnListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if(!HasPermission){
//                CheckPermission();
//                return;
//            }
//            if (BTKit.isEnabled()) {
//                if(isConnected && !IsOnOrder){
//                    TurnOffBluetooth();
//                }else {
//                    StartScan(false);
//                }
//            } else {
//                TurnOnBluetooth();
//            }
//        }
//    };
//
//    public void CheckPermission(){
//        Dexter.withContext(this)
//                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION
//                        )
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
//                        if(multiplePermissionsReport.areAllPermissionsGranted()){
//                            HasPermission=true;
//                            btnTurnOff.performClick();
//                        }else{
//                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                            txt.setText("دسترسی لازم را ندارید");
//                            HasPermission=false;
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
//                    }
//                }).check();
//    }
//
//    protected void TurnOnBluetooth(){
//        Handler handler=new Handler();
//        handler.postDelayed(CheckBluetoothON,1500);
//    }
//
//    protected void TurnOffBluetooth(){
//        try{
//            txt.setText("بلوتوث خاموش شد");
//            UIShow(false);
//            isConnected=false;
//            IsOnOrder=false;
//            BTKit.setEnabled(false);
//        }catch (Exception e){}
//    }
//
//    protected Runnable CheckBluetoothON=new Runnable() {
//        @Override
//        public void run() {
//            try{
//                if(!BTKit.isEnabled() && triesOn<3){
//                    txt.setText("درحال روشن سازی بلوتوث...");
//                    BTKit.setEnabled(true);
//                    triesOn++;
//                    TurnOnBluetooth();
//                }else if(BTKit.isEnabled()){
//                    txt.setText("بلوتوث روشن شد");
//                }else if(!BTKit.isEnabled() && triesOn>=3){
//                    txt.setText("خطا در روشن کردن بلوتوث");
//                }
//            }catch (Exception e){
//            }
//        }
//    };
//
//    protected Runnable CheckBTDiscoveredFinished=new Runnable() {
//        @Override
//        public void run() {
//            if(MaxTriesToDiscover>100){
//                StartScan(true);
//                return;
//            }
//            if(!BTKit.getBluetoothAdapter().isDiscovering()){
//                MaxTriesToDiscover++;
//                txt.setText("درحال دریافت دستگاهها...");
//                Handler handler=new Handler();
//                handler.postDelayed(CheckBTDiscoveredFinished,20);
//            }else{
//                StartScan(true);
//            }
//        }
//    };
//
//    protected void StartScan(boolean CallAfterDiscovering){
//        if(CallAfterDiscovering){
//            //After Discovering We Show List of Devices to Connecting
//            LoadingDialog.hide();
//            ShowListOfDevices();
//
//        }else {
//            //We Start Discovering BT Devices
//            if (!BTKit.getBluetoothAdapter().isDiscovering()) {
//                MaxTriesToDiscover=0;
//                txt.setText("شروع جستجو دستگاه ها");
//                BTKit.getBluetoothAdapter().startDiscovery();
//                LoadingDialog.changeAlertType(5);
//                LoadingDialog.setCanceledOnTouchOutside(false);
//                LoadingDialog.show();
//                Handler handler = new Handler();
//                handler.postDelayed(CheckBTDiscoveredFinished, 20);
//            }
//
//        }
//    }
//
//
//    protected void ShowListOfDevices(){
//        if(isShowingListOfDevice)return;
//        isShowingListOfDevice=true;
//        try {
//            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//            builder.setTitle("دستگاه را انتخاب کنید");
//
//            List<BluetoothDevice> Devices = new ArrayList<>();
//
//            //Paired Devices
//            Set<BluetoothDevice> setPair = BTKit.getPairedDevices();
//            if(setPair.size()>0) {
//                for (BluetoothDevice device : setPair) {
//                    Log.i("BTKPair", device.getName());
//                    boolean isInArray = false;
//                    for (int i = 0; i < Devices.size(); i++) {
//                        if (Devices.get(i).getAddress() == device.getAddress()) {
//                            isInArray = true;
//                        }
//                    }
//                    if (!isInArray) {
//                        Devices.add(device);
//                    }
//                }
//            }
//            Log.i("BTK", "Number Pair:" + setPair.size());
//            // add a list
//            if(Devices.size()>0) {
//                String[] finalNames=new String[Devices.size()];
//                int counter=0;
//                for(BluetoothDevice device : Devices){
//                    finalNames[counter]=device.getName();
//                    counter++;
//                }
//                builder.setItems(finalNames, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        isShowingListOfDevice = false;
//                        LoadingDialog.show();
//                        ConnectToDevice(Devices.get(which));
//                    }
//                });
//
//                AlertDialog dialog = builder.create();
//                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        isShowingListOfDevice = false;
//                    }
//                });
//                dialog.show();
//            }
//        }catch (Exception e){
//            Log.e("BTK-FATAL",e.toString());
//        }
//    }
//
//    protected void ConnectToDevice(BluetoothDevice device){
//        if(device!=null){
//            txt.setText("در حال اتصال به دستگاه...");
//            TriesToConnect=0;
//            myDevice=device;
//            Handler handler=new Handler();
//            handler.postDelayed(ConnectionRunnabel,0);
//        }else{
//            txt.setText("امکان اتصال مقدور نیست");
//        }
//    }
//
//
//    private Runnable ConnectionRunnabel=new Runnable() {
//        @Override
//        public void run() {
//            try{
//                if(TriesToConnect>2) {
//
//                    if (isConnected) {
//                        Log.e("BTK", "Connecting To Device Success");
//                        txt.setText(myDevice.getName());
//                        LoadingDialog.dismiss();
//                        //Show Button Green
//                        UIShow(true);
//                    } else {
//                        txt.setText("اتصال به دستگاه مقدور نمی باشد");
//                        LoadingDialog.dismiss();
//                        Log.e("BTK", "Connecting To Device Failed");
//                        //Show Button Red
//                        UIShow(false);
//                    }
//                }else {
//
//                    TriesToConnect++;
//                    if (myDevice == null) return;
//                    isConnected=BTKit.connect(myDevice,MY_UUID);
//                    isConnected=BTKit.getBluetoothSocket().getSocket().isConnected();
//                    Log.i("BTConnect",String.valueOf(isConnected));
//                    if(!isConnected) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(ConnectionRunnabel, 5);
//                    }else{
//                        txt.setText(myDevice.getName());
//                        Log.i("BTK", "Connecting To Device Success");
//                        LoadingDialog.dismiss();
//                        UIShow(true);
//                    }
//                }
//            }catch (Exception e){
//                LoadingDialog.dismiss();
//                Log.e("BTK-FATAL",e.toString());
//            }
//        }
//    };
//
//    protected void UIShow(boolean isOk){
//        if(isOk){
//            btnTurnOffShadow.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.style_turnoff_green_color));
//            btnTurnOffFlash.setCardBackgroundColor(Color.parseColor("#5D9213"));
//        }else{
//            btnTurnOffShadow.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.style_turnoff_red_color));
//            btnTurnOffFlash.setCardBackgroundColor(Color.parseColor("#BF0A0A"));
//        }
//    }
//
//    protected void SendCommand(String command){
//        try{
//            if(isSendingCommandRun){
//                isSendingCommandRun=false;
//                LoadingDialog.hide();
//                return;
//            }
//            TryToSendCommand=0;
//            isSendingCommandRun=true;
//            switch (command){
//                case "New":
//                    CommandString="AddNew";
//                    break;
//                case "Delete":
//                    CommandString="Delete";
//                    break;
//                case "Reset":
//                    CommandString="Reset";
//                    break;
//                case "ChangeAdmin":
//                    CommandString="ChangeAdmin";
//                    break;
//
//            }
//            //Start Sending Command
//            if(CommandString!="") {
//                LoadingDialog.show();
//                isSendingCommandRun = true;
//                TryToSendCommand = 0;
//                ExcuteCommand();
//
//            }
//        }catch (Exception e){
//            Log.e("FFFF",e.toString());
//        }
//    }
//
//    private void ExcuteCommand(){
//        try{
//            TryToSendCommand++;
//            if(TryToSendCommand>20 || !isSendingCommandRun){
//                Log.e("BTKK","Try:"+TryToSendCommand);
//                isSendingCommandRun=false;
//                LoadingDialog.hide();
//                return;
//            }
////            Toast.makeText(this.getApplicationContext(),"Execute",Toast.LENGTH_SHORT).show();
//            //Clear InputStream Before Sending
//            byte[] inputData = new byte[1024];
//            int Read=0;
//
//            DataInputStream mmInStream = new DataInputStream(BTKit.getBluetoothSocket().getInputStream());
////            DataOutputStream mmOutStream = new DataOutputStream(BTKit.getBluetoothSocket().getOutputStream());
//
//
//            if(BTKit.getBluetoothSocket().getSocket().getInputStream().available()>0) {
//                mmInStream.read(inputData,0,BTKit.getBluetoothSocket().getSocket().getInputStream().available());
//                Log.i("BT-IN3",new BigInteger(inputData).toString());
//                BTKit.getBluetoothSocket().getSocket().getInputStream().read(inputData, 0, BTKit.getBluetoothSocket().getSocket().getInputStream().available());
//                Log.i("BT-IN1",new BigInteger(inputData).toString());
//            }
//
//            //Send Data
//            String HelloText="*"+CommandString+"#";
//            byte[] bytes = HelloText.getBytes("UTF-8");
//            BTKit.write(bytes,0,bytes.length);
//
//            //Check For Received or not
//            if(BTKit.getBluetoothSocket().getSocket().getInputStream().available()>0){
//                mmInStream.read(inputData,0,BTKit.getBluetoothSocket().getSocket().getInputStream().available());
//                Log.i("BT-IN3",new BigInteger(inputData).toString());
//
////                BTKit.getBluetoothSocket().getSocket().getInputStream().read(inputData,0,BTKit.getBluetoothSocket().getSocket().getInputStream().available());
//                Log.e("BTKK","ACA"+BTKit.getBluetoothSocket().getSocket().getInputStream().available());
//                isSendingCommandRun=false;
//                TryToSendCommand=0;
//                CommandString="";
//
//                WhereIs();
//                LoadingDialog.hide();
//            }else{
//                Handler handler=new Handler();
//                handler.postDelayed(CallMore,10);
//            }
//        }catch (Exception e){
//            LoadingDialog.hide();
//            Log.e("BTK_send",e.toString());
//        }
//    }
//
//    Runnable CallMore=new Runnable() {
//        @Override
//        public void run() {
//            ExcuteCommand();
//        }
//    };
//
//    Runnable GetMore=new Runnable() {
//        @Override
//        public void run() {
//            WhereIs();
//        }
//    };
//    protected void WhereIs(){
//        Log.e("WH","Y");
//        try{
//            byte[] inputData = new byte[1024];
//            if(BTKit.getBluetoothSocket().getSocket().getInputStream().available()>0) {
//                BTKit.getBluetoothSocket().getSocket().getInputStream().read(inputData, 0, BTKit.getBluetoothSocket().getSocket().getInputStream().available());
//                    Log.i("BT-IN",inputData.toString());
////                switch (CommandString){
////                    case "AddNew":
//                        if(new BigInteger(inputData).intValue()==BT_ADMIN_CONFIRM)txt.setText("اثر انگشت مدیر لازم است");
//                        if(new BigInteger(inputData).intValue()==BT_ADMIN_ERROR)txt.setText("اثر انگشت مدیر رد شد");
//                        if(new BigInteger(inputData).intValue()==BT_ADMIN_ACCEPT)txt.setText("اثر انگشت جدید را وارد کنید");
//                        if(new BigInteger(inputData).intValue()==BT_CMD_SUCCESS){
//                            txt.setText("عملیات انجام شد");
//                            return;
//                        }
//                        if(new BigInteger(inputData).intValue()==BT_CMD_FAIL){
//                            txt.setText("عملیات رد شد");
//                            return;
//                        }
////                        break;
////                    case "Delete":
////
////                        break;
////                    case "Reset":
////
////                        break;
////                    case "ChangeAdmin":
////
////                        break;
////                }
//            }
//            Handler handler=new Handler();
//            handler.postDelayed(GetMore,1);
//        }catch (Exception e){}
//    }
//
//    protected void ShowHelp(int level){
//
//    }
}
