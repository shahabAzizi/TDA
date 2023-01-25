package com.tda.tda.Activities;

public class DeviceActivity  {
//
//    DB db;
//    private BluetoothHelper BTHelper;
//    private String deviceName="";
//    private String bluetoothName="";
//    private Switch btnBluetooth;
//    private TextView bluetoothTextView;
//    private RecyclerView deviceList;
//    private BluetoothAdapter adapter;
//    private RelativeLayout empty;
//    private SpinKitView loadingList;
//    private Button btnConnection;
//    private boolean activityExitOrDestroy=false;
//
//    private int device_id;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device);
//
//        //Get Device Id from Intent.
//        Bundle bundle=this.getIntent().getExtras();
//        if(bundle!=null){
//            device_id=bundle.getInt("device_id");
//        }else{
//            this.finish();
//        }
//
//        BTHelper=new BluetoothHelper(this);
//        db=DB.getInstance(this);
//
//        btnBluetooth=(Switch)findViewById(R.id.device_bluetooth_btn);
//        bluetoothTextView=(TextView)findViewById(R.id.device_bluetooth_status);
//        deviceList=(RecyclerView)findViewById(R.id.device_device_list);
//        empty=(RelativeLayout)findViewById(R.id.device_empty);
//        loadingList=(SpinKitView) findViewById(R.id.device_spin_kit);
//        Button btnAdd=(Button) findViewById(R.id.device_btn_add);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        btnConnection=(Button) findViewById(R.id.device_btn_connection);
//        btnConnection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(BTHelper.isBluetoothOn()){
//                    new Thread(getDeviceToConnect).start();
//                }else{
//                    btnBluetooth.callOnClick();
//                }
//            }
//        });
//
//        ImageButton btnBack=(ImageButton)findViewById(R.id.device_toolbar_drawer_btn);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DeviceActivity.this.finish();
//            }
//        });
//
//        btnBluetooth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(btnBluetooth.isChecked()){
//                    BTHelper.bluetoothOn();
//                    //Check Every 0.5 second for BLUETOOTH Status After Click
//                    new Handler().postDelayed(checkBluetoothStatus,200);
//                }else{
//                    BTHelper.bluetoothOff();
//                    bluetoothTextView.setText("خاموش");
//                }
//            }
//        });
//
//
//        loadingList.setVisibility(View.GONE);
//        adapter = new BluetoothAdapter();
//        deviceList.setLayoutManager(new LinearLayoutManager(this));
//        deviceList.setAdapter(adapter);
//    }
//
//
//    Runnable AddDeviceRun=new Runnable() {
//        @Override
//        public void run() {
//            Device device=new Device();
//            device.device_name="";
//            device.device_bluetooth=adapter.SelectedDevice.getName();
//            device.password="";
//            device.device_ip=adapter.SelectedDevice.getIp();
//
//            try {
//                long ans=db.tbDevice().insert(device);
//                if(ans>0){
//                    Snackbar snackbar = Snackbar
//                            .make(DeviceActivity.this,(View)findViewById(R.id.adddevice_container),"ثبت انجام شد", Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                }
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private BluetoothHelper.ConnectionListener connectionListener=new BluetoothHelper.ConnectionListener() {
//        @Override
//        public void onConnect(Boolean Connected) {
//            if(Connected){
//                btnConnection.setText(R.string.connection_success);
//            }else{
//                btnConnection.setText(R.string.connection_fail);
//            }
//        }
//    };
//
//    Runnable getDeviceToConnect=new Runnable() {
//        @Override
//        public void run() {
//            try {
//                Device device=db.tbDevice().get(device_id);
//                BTHelper.connectToDevice(device,connectionListener);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    Runnable checkBluetoothStatus=new Runnable() {
//        @Override
//        public void run() {
//            Log.i("BT","ADD_DEVICE_BT_RUN");
//            if(BTHelper.isBluetoothOn()){
//                btnBluetooth.setChecked(true);
//                bluetoothTextView.setText("روشن");
//                ShowBluetoothLists();
//            }else{
//                if(btnBluetooth.isChecked()){
//                    //Check For Bluetooth Status After 0.5 Second
//                    if(!activityExitOrDestroy)new Handler().postDelayed(checkBluetoothStatus,500);
//                }
//            }
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.activityExitOrDestroy=false;
//        if(BTHelper.isBluetoothOn()){
//            btnBluetooth.setChecked(true);
//            bluetoothTextView.setText("روشن");
//            ShowBluetoothLists();
//        }else{
//            btnBluetooth.setChecked(false);
//            bluetoothTextView.setText("خاموش");
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        this.activityExitOrDestroy=true;
//    }
//
//    private void ShowBluetoothLists(){
//        try{
//            loadingList.setVisibility(View.VISIBLE);
//            BTHelper.startScan(new BluetoothHelper.DiscoveryFinishedListener() {
//                @Override
//                public void onDiscover(List<BluetoothDevice> deviceList) {
//                    loadingList.setVisibility(View.GONE);
//                    if(deviceList.size()>0) {
//                        empty.setVisibility(View.INVISIBLE);
//                        adapter.differ.submitList(deviceList);
//                    }else{
//                        empty.setVisibility(View.VISIBLE);
//                    }
////                    Toast.makeText(AdddeviceActivity.this, deviceList.size()+"", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }catch (Exception e){}
//    }
}