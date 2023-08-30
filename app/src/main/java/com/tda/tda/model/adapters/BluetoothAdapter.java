package com.tda.tda.model.adapters;

import android.annotation.SuppressLint;
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
import com.tda.tda.model.dataclass.BluetoothDevice;

public class BluetoothAdapter extends RecyclerView.Adapter<BluetoothAdapter.myViewHolder> {

    public AsyncListDiffer<BluetoothDevice> differ = new AsyncListDiffer<BluetoothDevice>(this, new BluetoothDiffer());
    public BluetoothDevice SelectedDevice = new BluetoothDevice();
    private int selectedPosition = -1;

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_bluetooth_record, parent, false);
        myViewHolder viewHolder = new myViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final BluetoothDevice myListData = differ.getCurrentList().get(position);
        if(myListData.getName()!=null) {
            holder.txtName.setText(myListData.getName().length() > 20 ? myListData.getName().substring(0, 19) : myListData.getName());
        }else{
            holder.txtName.setText("بدون نام");
        }
        holder.txtIp.setText(myListData.getIp());
        if (myListData.isSelected()) {
            holder.imgTick.setVisibility(View.VISIBLE);
        } else {
            holder.imgTick.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            for(int i = 0;i < differ.getCurrentList().size(); i++){
                differ.getCurrentList().get(i).setSelected(false);
                if(SelectedDevice.getIp()!=null){
                    if(SelectedDevice.getIp().equals(differ.getCurrentList().get(i).getIp())){
                        notifyItemChanged(i);
                    }
                }
            }
            differ.getCurrentList().get(position).setSelected(true);
            differ.submitList(differ.getCurrentList());
            notifyItemChanged(position);
            SelectedDevice=differ.getCurrentList().get(position);
        });
    }


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }


    class BluetoothDiffer extends DiffUtil.ItemCallback<BluetoothDevice> {

        @Override
        public boolean areItemsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
            return oldItem.getIp().equals(newItem.getIp());
        }

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtIp, txtType;
        public ImageView imgTick;

        public myViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(R.id.bluetooth_record_layout_name);
            this.txtIp = (TextView) itemView.findViewById(R.id.bluetooth_record_layout_ip);
            this.txtType = (TextView) itemView.findViewById(R.id.bluetooth_record_layout_type);
            this.imgTick = (ImageView) itemView.findViewById(R.id.bluetooth_record_layout_tick);
        }

    }
}
