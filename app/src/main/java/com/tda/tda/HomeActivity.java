package com.tda.tda;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

//import com.tda.tda.Activities.databinding.ActivityHomeBinding;
import com.tda.tda.MyBaseActivity;
import com.tda.tda.R;
import com.tda.tda.databinding.ActivityHomeBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends MyBaseActivity {



//    private AppBarConfiguration mAppBarConfiguration;
//    private ActivityHomeBinding binding;
//    private Toolbar toolbar;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}