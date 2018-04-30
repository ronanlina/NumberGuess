package com.example.connivingdog.numberguess;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PrefixListAdapter extends BaseAdapter {

    private DatabaseReference mDatabaseReference;
    private Activity mActivity;
    private ArrayList<DataSnapshot> mSnapshotList;
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public PrefixListAdapter(DatabaseReference databaseReference, Activity activity) {
        mDatabaseReference = databaseReference;
        mActivity = activity;
        mDatabaseReference.child("simnumbers").addChildEventListener(mListener);
        mSnapshotList = new ArrayList<>();
    }

    static class ViewHolder{
        TextView sim;
        TextView prefix;
        Button remove;
    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public SimPrefix getItem(int i) {
        DataSnapshot snapshot = mSnapshotList.get(i);

        return snapshot.getValue(SimPrefix.class);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.existing_prefix_list,viewGroup,false);

            final ViewHolder holder = new ViewHolder();

            holder.prefix = (TextView) view.findViewById(R.id.prefix);
            holder.sim = (TextView) view.findViewById(R.id.sim);
            holder.remove = (Button) view.findViewById(R.id.remove);

            view.setTag(holder);
        }

        final SimPrefix sp = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        String sim = sp.getSimCard().toString() + ": ";
        String prefix = sp.getSimPref() + " ";

        holder.prefix.setText(prefix);
        holder.sim.setText(sim);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(i);
            }
        });

        return view;
    }

    public void cleanup(){
        mDatabaseReference.removeEventListener(mListener);
    }

    private void remove(int i){
        mSnapshotList.remove(i);
        mDatabaseReference.child("");
    }
}
