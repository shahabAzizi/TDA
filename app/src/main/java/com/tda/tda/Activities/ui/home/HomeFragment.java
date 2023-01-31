package com.tda.tda.Activities.ui.home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.tda.tda.Activities.AdddeviceActivity;
import com.tda.tda.Activities.DeviceActivity;
import com.tda.tda.helpers.DB.DB;
import com.tda.tda.helpers.DB.Models.Device;
import com.tda.tda.R;
import com.tda.tda.model.adapters.DeviceAdapter;
import com.tda.tda.model.dataclass.Devices;
import com.tda.tda.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

//import com.tda.tda.Activities.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    DB db;
    private DeviceAdapter adapter;
    private RecyclerView list;
    private RelativeLayout empty;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider((ViewModelStoreOwner) this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        list=(RecyclerView) root.findViewById(R.id.home_list);
        empty=(RelativeLayout) root.findViewById(R.id.home_empty);
        Button btnNewDevice=(Button) root.findViewById(R.id.home_btn_add);
        btnNewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeFragment.this.getActivity(), AdddeviceActivity.class));
            }
        });
//        db=DB.getInstance(this.getContext());
        adapter=new DeviceAdapter();
        adapter.itemClickListener=new DeviceAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(Devices device) {
                Intent intent=new Intent(HomeFragment.this.getActivity(), DeviceActivity.class);
                intent.putExtra("device_id",device.getId());
                startActivity(intent);
            }
        };
//        list.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list.setAdapter(adapter);

        new Thread(GetDevices).start();
        return root;
    }

    Runnable GetDevices=new Runnable() {
        @Override
        public void run() {
            try {
                List<Device> ans=db.tbDevice().getAll();
                List<Devices> newList=new ArrayList();
                for (int i=0;i<ans.size();i++){
                    Devices item=new Devices();
                    item.setName(ans.get(i).device_name);
                    item.setIp(ans.get(i).device_ip);
                    item.setId(ans.get(i).id);
                    newList.add(item);
                }
                adapter.differ.submitList(newList);
                if(newList.size()>0){
                    empty.setVisibility(View.GONE);
                }else{
                    empty.setVisibility(View.VISIBLE);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(GetDevices).start();
    }
}