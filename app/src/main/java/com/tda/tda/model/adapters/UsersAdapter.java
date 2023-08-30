package com.tda.tda.model.adapters;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tda.tda.HomeActivity;
import com.tda.tda.R;
import com.tda.tda.helpers.DB.Models.DeviceDetails;
import com.tda.tda.model.dataclass.Devices;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.myViewHolder> {
    public AsyncListDiffer<DeviceDetails> differ = new AsyncListDiffer<DeviceDetails>(this,new DeviceDiffer());
    private MenuClickListener listener;
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_user_record, parent, false);
        myViewHolder viewHolder = new myViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final DeviceDetails myListData = differ.getCurrentList().get(position);
        holder.txtName.setText(myListData.name);
        holder.txtIp.setText(myListData.type);
        holder.btnMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.getMenuInflater().inflate(R.menu.user, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                if(listener!=null)listener.onMenuItemClicked(item.getItemId(),differ.getCurrentList().get(position));
                return false;
            });
        });
    }


    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public void setListener(MenuClickListener listener) {
        this.listener = listener;
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

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName,txtIp;
        public ImageButton btnMenu;
        public myViewHolder(View itemView) {
            super(itemView);
            this.txtName = (TextView) itemView.findViewById(R.id.user_record_layout_name);
            this.txtIp = (TextView) itemView.findViewById(R.id.user_record_layout_ip);
            this.btnMenu = (ImageButton) itemView.findViewById(R.id.user_record_layout_menu);
        }

    }

    public interface MenuClickListener{
        public void onMenuItemClicked(int item,DeviceDetails object);
    }


}
