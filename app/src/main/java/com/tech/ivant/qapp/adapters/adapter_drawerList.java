package com.tech.ivant.qapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tech.ivant.qapp.R;
import com.tech.ivant.qapp.entities.drawerList_model;

import java.util.Collections;
import java.util.List;

/**
 * Created by james on 7/31/15.
 */
public class adapter_drawerList extends RecyclerView.Adapter<adapter_drawerList.MyViewHolder> {

    private LayoutInflater inflater;
    List<drawerList_model> data = Collections.emptyList();
    private Context context;


    public adapter_drawerList(Context context, List<drawerList_model> data) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_drawerlist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        drawerList_model current=data.get(position);
        holder.title.setText(current.title);
        holder.image.setImageResource(current.imageID);
        holder.color.setBackgroundColor(Color.parseColor(current.colorID));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView color;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.drawerList_title);
            image= (ImageView) itemView.findViewById(R.id.drawerList_image);
            color = (TextView) itemView.findViewById(R.id.drawerList_color);
        }


    }
}
