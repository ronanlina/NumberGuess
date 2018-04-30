package com.example.connivingdog.numberguess;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SimPrefixAdapter extends RecyclerView.Adapter<SimPrefixAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<SimPrefix> mSimPrefixes;

    public SimPrefixAdapter(Context context, ArrayList<SimPrefix> simPrefixes) {
        mContext = context;
        mSimPrefixes = simPrefixes;
    }

    @NonNull
    @Override
    public SimPrefixAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.existing_prefix_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimPrefixAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mSimPrefixes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView sim;
        TextView prefix;

        public ViewHolder(View itemView){
            super(itemView);

            sim = itemView.findViewById(R.id.sim);
            prefix = itemView.findViewById(R.id.prefix);
        }
    }
}
