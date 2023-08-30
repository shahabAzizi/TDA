package com.tda.tda;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

//import com.tda.tda.Activities.databinding.ActivityHomeBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tda.tda.MyBaseActivity;
import com.tda.tda.R;
import com.tda.tda.databinding.ActivityHomeBinding;
import com.tda.tda.model.dialogs.ShowMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.qualifiers.ApplicationContext;

@AndroidEntryPoint
public class HomeActivity extends MyBaseActivity {

    private HomeActivityViewModelMy homeActivityViewModelMy;

    //    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    public ShowMessage showMessage;
    public int toolbarMenu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        showMessage = new ShowMessage(this);
        homeActivityViewModelMy = new ViewModelProvider(this).get(HomeActivityViewModelMy.class);
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setOpenableLayout(drawer)
//                .build();



    }
//        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        binding = ActivityHomeBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        toolbar=(Toolbar)findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        DrawerLayout drawer = binding.drawerLayout;
//        NavigationView navigationView = binding.navView;
//        ImageButton drawerBtn=(ImageButton)findViewById(R.id.toolbar_drawer_btn);
//        drawerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!drawer.isOpen()){
//                    drawer.open();
//                }
//            }
//        });
//        ImageButton overflowBtn=(ImageButton)findViewById(R.id.toolbar_overflow_btn);
//        overflowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toolbar.showOverflowMenu();
//            }
//        });
//
//
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
////        mAppBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
////                .setOpenableLayout(drawer)
////                .build();
//
////        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
////            @Override
////            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//////                if(item.getItemId()==R.id.nav_slideshow){
////                    Toast.makeText(HomeActivity.this, "Yes", Toast.LENGTH_SHORT).show();
//////                }
////                return false;
////            }
////        });
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
////
////        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
////        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
////            @Override
////            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
////                boolean isTopLevelDestination = mAppBarConfiguration.getTopLevelDestinations().contains(navDestination.getId());
////                binding.appBarHome.toolbar.setNavigationIcon(
////                    isTopLevelDestination?R.drawable.ic_menu_bar:0
////                );
////            }
////        });
////        NavigationUI.setupWithNavController(navigationView, navController);
//
//    }
//

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        if (toolbarMenu != 0) {
            menu.clear();
            getMenuInflater().inflate(toolbarMenu, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    public void checkPermission(Context context) {
        Collection<String> perms=new ArrayList<>();
//        perms.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        perms.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        startPermission(context);
//        perms.add(Manifest.permission.BLUETOOTH);
//        perms.add(Manifest.permission.BLUETOOTH_ADMIN);





    }

    public void accessFineLocationPermission(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        startPermission2(context);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showMessage.showMessageAlert("لطفا لوکیشن دستگاه را روشن کنید", ShowMessage.ALERT_ERROR);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError dexterError) {
                Toast.makeText(context,dexterError.toString(),Toast.LENGTH_LONG).show();
            }
        }).check();
    }

    public void startPermission2(Context context){
        Dexter.withContext(context)
                .withPermissions(Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.S){
//                                startPermission3(context);
                            }
                        } else {
                            showMessage.showMessageAlert("بدون اجازه دسترسی به بلوتوث امکان استفاده از برنامه وجود ندارد", ShowMessage.ALERT_ERROR);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        Toast.makeText(context, "LEELE", Toast.LENGTH_LONG).show();
                    }
                }).check();
    }

    public void checkBluetoothConnectPermission(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.BLUETOOTH_CONNECT)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        startPermission4(context);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showMessage.showMessageAlert("دسترسی اتصال از طریق بلوتوث را ندارید", ShowMessage.ALERT_ERROR);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    public void checkBluetoothScanPermission(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.BLUETOOTH_SCAN)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
//                            startPermission5(context);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        HomeActivity.this.showMessage.showMessageAlert("دسترسی لازم برای اسکن بلوتوث داده نشده است.",ShowMessage.ALERT_ERROR);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    public void checkLocationPermission(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        showMessage.showMessageAlert("عدم دسترسی لوکیشن باعث اختلال در کارکرد برنامه می شود",ShowMessage.ALERT_ERROR);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void networkStatePermission(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        isVPNTurnOn();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
                        Log.e("btk",dexterError.toString());
                    }
                }).check();
    }

    public void internetPermission(Context context){
        Dexter.withContext(context)
                .withPermission(Manifest.permission.INTERNET)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        isVPNTurnOn();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
                        Log.e("btk",dexterError.toString());
                    }
                }).check();
    }

    public boolean isVPNTurnOn(){
        String requiredPermission = android.Manifest.permission.ACCESS_NETWORK_STATE;
        int checkVal = HomeActivity.this.checkCallingOrSelfPermission(requiredPermission);
        if(checkVal != PackageManager.PERMISSION_GRANTED){
            Log.i("BTK","NETWORK PERMMMMM!!!!!!!!");
            networkStatePermission(HomeActivity.this);
            return false;
        }
        try{
            ConnectivityManager connectivityManager= (ConnectivityManager) HomeActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
            boolean vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
            return vpnInUse;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}