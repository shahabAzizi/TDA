package com.tda.tda.model.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tda.tda.R;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Devices;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.myViewHolder> {

    public AsyncListDiffer<DeviceDetails> differ = new AsyncListDiffer<DeviceDetails>(this,new DeviceDiffer());
    public DeviceDetails SelectedDevice=new DeviceDetails();
    public ItemClickListener itemClickListener;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_device_record, parent, false);
        myViewHolder viewHolder = new myViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final DeviceDetails myListData = differ.getCurrentList().get(position);
        holder.txtName.setText(myListData.name);
        holder.txtIp.setText(myListData.type);
        if(myListData.name.equals(SelectedDevice.name) && myListData.id==SelectedDevice.id){
            holder.imgTick.setVisibility(View.VISIBLE);
        }else{
            holder.imgTick.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            if(itemClickListener!=null)itemClickListener.onItemClickListener(myListData);
        });
    }


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }


    class DeviceDiffer extends DiffUtil.ItemCallback<DeviceDetails> {


        @Override
        public boolean areItemsTheSame(@NonNull DeviceDetails oldItem, @NonNull DeviceDetails newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DeviceDetails oldItem, @NonNull DeviceDetails newItem) {
            return oldItem.id==newItem.id;
        }
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName,txtIp,txtType;
        public ImageView imgTick;
        public myViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(R.id.device_record_layout_name);
            this.txtIp = (TextView) itemView.findViewById(R.id.device_record_layout_ip);
            this.txtType = (TextView) itemView.findViewById(R.id.device_record_layout_type);
            this.imgTick = (ImageView) itemView.findViewById(R.id.device_record_layout_tick);
        }

    }

    public interface ItemClickListener{
        public void onItemClickListener(DeviceDetails device);
    }
}
