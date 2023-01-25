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
import com.tda.tda.model.dataclass.Devices;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.myViewHolder> {

    public AsyncListDiffer<Devices> differ = new AsyncListDiffer<Devices>(this,new DeviceDiffer());
    public Devices SelectedDevice=new Devices();
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
        final Devices myListData = differ.getCurrentList().get(position);
        holder.txtName.setText(myListData.getName());
        holder.txtIp.setText(myListData.getIp());
        if(myListData.equals(SelectedDevice)){
            holder.imgTick.setVisibility(View.VISIBLE);
        }else{
            holder.imgTick.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null)itemClickListener.onItemClickListener(myListData);
            }
        });
    }


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }


    class DeviceDiffer extends DiffUtil.ItemCallback<Devices> {


        @Override
        public boolean areItemsTheSame(@NonNull Devices oldItem, @NonNull Devices newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Devices oldItem, @NonNull Devices newItem) {
            return oldItem.getName().equals(newItem.getName());
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
        public void onItemClickListener(Devices device);
    }
}
