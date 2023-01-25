package com.tda.tda.Activities;


public class AdddeviceActivity  {


//    private ActivityAdddeviceBinding binding;
//    private HomeActivityViewModel homeActivityViewModel;
//    DB db;
//    private BluetoothHelper BTHelper;
//    private BluetoothAdapter adapter;
//    private boolean activityExitOrDestroy=false;
//    private ShowMessage showMessage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        binding=ActivityAdddeviceBinding.;
//        setContentView(binding.getRoot());
//
//        showMessage=new ShowMessage();
//

//        homeActivityViewModel = ViewModelProvider(this).get(LoginViewModel::class);
//
//        homeActivityViewModel.getName().observe(this,binding.adddeviceName::setText);
//        homeActivityViewModel.getPassword().observe(this,binding.adddevicePassword::setText);
//
//        binding.adddeviceBtnAdd.setOnClickListener(view -> {
//            if(homeActivityViewModel.isInputsValid()){
//
//            }else{
//
//            }
//        });

//        binding.adddeviceBtnAdd.setOnClickListener(view -> {
//            binding.adddeviceName.setError(null);
//            if(binding.adddeviceName.getText().toString().length()>1 &&
//                binding.adddevicePassword.getText().toString().length()>1) {
//                new Thread(addDeviceRun).start();
//            }else{
//                if(binding.adddeviceName.getText().toString().length()<1)binding.adddeviceName.setError("لطفا نام دستگاه را وارد کنید");
//                if(binding.adddevicePassword.getText().toString().length()<1)binding.adddevicePassword.setError("لطفا رمز عبور دستگاه را وارد کنید");
//            }
//        });
//
//        binding.adddeviceToolbarDrawerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AdddeviceActivity.this.finish();
//            }
//        });
//
//        binding.adddeviceBluetoothBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(binding.adddeviceBluetoothBtn.isChecked()){
//                    BTHelper.bluetoothOn();
//                    //Check Every 0.5 second for BLUETOOTH Status After Click
//                    new Handler().postDelayed(checkBluetoothStatusRunnable,200);
//                }else{
//                    BTHelper.bluetoothOff();
//                    binding.adddeviceBluetoothStatus.setText("خاموش");
//                }
//            }
//        });
//
//
//        binding.adddeviceSpinKit.setVisibility(View.GONE);
//        adapter = new BluetoothAdapter();
//        binding.adddeviceDeviceList.setLayoutManager(new LinearLayoutManager(this));
//        binding.adddeviceDeviceList.setAdapter(adapter);

//    }
//
//    private void initialView(){
////        BTHelper=new BluetoothHelper(AdddeviceActivity.this);
////        db=DB.getInstance(this);
//
//    }
//
//
//
//    Runnable addDeviceRun=new Runnable() {
//        @Override
//        public void run() {
//            //if No Bluetooth Device Selected
//            if(adapter.SelectedDevice.getIp()=="" || adapter.SelectedDevice.getIp()==null){
//                showMessage.showMessageAlert("لطفا یک بلوتوث را انتخاب کنید",ShowMessage.ALERT_ERROR);
//                return;
//            }
//            Device device=new Device();
//            device.device_name=binding.adddeviceName.getText().toString();
//            device.device_bluetooth=adapter.SelectedDevice.getName();
//            device.password=binding.adddevicePassword.getText().toString();
//            device.fp_password="";
//            device.device_ip=adapter.SelectedDevice.getIp();
//
//            try {
//                //if We Saved a Device with same name or same address show error
//                Device deviceSearch=db.tbDevice().getByNameOrAddress(binding.adddeviceName.getText().toString(),adapter.SelectedDevice.getIp());
//                if(deviceSearch==null) {
//                    long ans = db.tbDevice().insert(device);
//                    if (ans > 0) {
//                        showMessage.showMessageAlert("دستگاه ثبت گردید",ShowMessage.ALERT_SUCCESS);
//                    }
//                }else{
//                    showMessage.showMessageAlert("یک دستگاه با نام یا آدرس وارد شده موجود است",ShowMessage.ALERT_ERROR);
//                }
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    Runnable checkBluetoothStatusRunnable=new Runnable() {
//        @Override
//        public void run() {
//            Log.i("BT","ADD_DEVICE_BT_RUN");
//            checkBluetoothAvailable();
//        }
//    };
//
//
//    private void checkBluetoothAvailable(){
//        if(BTHelper.isBluetoothOn()){
//            binding.adddeviceBluetoothBtn.setChecked(true);
//            binding.adddeviceBluetoothStatus.setText("روشن");
//            ShowBluetoothLists();
//        }else{
//            binding.adddeviceBluetoothStatus.setText("خاموش");
//            if(!activityExitOrDestroy)new Handler().postDelayed(checkBluetoothStatusRunnable,500);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.activityExitOrDestroy=false;
//        this.checkBluetoothAvailable();
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
//            binding.adddeviceSpinKit.setVisibility(View.VISIBLE);
//            BTHelper.startScan(deviceList -> {
//                binding.adddeviceSpinKit.setVisibility(View.GONE);
//                if(deviceList.size()>0) {
//                    binding.adddeviceEmpty.setVisibility(View.INVISIBLE);
//                    adapter.differ.submitList(deviceList);
//                }else{
//                    binding.adddeviceEmpty.setVisibility(View.VISIBLE);
//                }
////                    Toast.makeText(AdddeviceActivity.this, deviceList.size()+"", Toast.LENGTH_SHORT).show();
//            });
//        }catch (Exception e){}
//    }

}