package com.mxn672.foodrating.recyclerView;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.Establishment;
import com.mxn672.foodrating.data.EstablishmentDatabase;
import com.mxn672.foodrating.fragments.EstablishmentFragment;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

    public ArrayList<Establishment> mDataset;
    public ArrayList<Establishment> copy_mDataset;
    public ArrayList<Establishment> mFavourited;
    public FragmentManager fragmentManager;
    private EstablishmentDatabase db;
    private Boolean fav;

    public MyAdapter(ArrayList<Establishment> dataSet, FragmentManager fragmentManager,
                     EstablishmentDatabase db) {
        this.mDataset = dataSet;
        this.fragmentManager = fragmentManager;
        this.db = db;
        this.mFavourited = (ArrayList<Establishment>) db.establishmentDao().getAll();
        this.copy_mDataset = new ArrayList<>(this.mDataset);
        fav = false;
    }

    public MyAdapter(ArrayList<Establishment> dataSet, FragmentManager fragmentManager,
                     EstablishmentDatabase db, boolean isFav) {
        this.mDataset = dataSet;
        this.fragmentManager = fragmentManager;
        this.db = db;
        this.mFavourited = (ArrayList<Establishment>) db.establishmentDao().getAll();
        this.copy_mDataset = new ArrayList<>(this.mDataset);
        fav = true;

    }

    private Filter establishmentsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Establishment> filteredEstablishments = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredEstablishments.addAll(mDataset);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Establishment e : mDataset){
                    if(e.businessName.toLowerCase().contains(filterPattern)){
                        filteredEstablishments.add(e);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredEstablishments;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataset.clear();
            mDataset.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // Setup of listeners
        holder.favourite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if((Integer) holder.favourite.getTag() == 1){
                    holder.favourite.setImageResource(R.drawable.favourite_border);
                    holder.favourite.setTag(0);
                    mDataset.get(position).favoured = false;
                    db.establishmentDao().deleteEstablishment(mDataset.get(position));
                    mFavourited.remove(mDataset.get(position));
                    if(fav){
                        mDataset.remove(position);
                        notifyDataSetChanged();
                    }
                }else{
                    holder.favourite.setImageResource(R.drawable.favourite_filled);
                    holder.favourite.setTag(1);
                    mDataset.get(position).favoured = true;
                    mFavourited.add(mDataset.get(position));
                    db.establishmentDao().insertEstablishment(mDataset.get(position));
                }
            }
        });

        holder.rowButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               showEstablishmentFragment(mDataset.get(position));
            }
        });

        // Pupulating the layout based on data
        String eName = new String();
        Integer eRating = -1;
        String eAddress = new String();
        String eDistance = new String();
        String eID = new String();

        try{
            eID = mDataset.get(position).estb_id;
            eName = mDataset.get(position).businessName;

            eAddress = mDataset.get(position).getAddress_l1();
            eDistance = mDataset.get(position).distance;

            eRating = Integer.parseInt(mDataset.get(position).rating);
        }catch (NumberFormatException e){
            eRating = -1;
        }catch(NullPointerException e){
            eName = "";
        }

        holder.favourite.setTag(0);
        for(Establishment estb : mFavourited){
            if(estb.estb_id.equals(mDataset.get(position).estb_id)){
                holder.favourite.setImageResource(R.drawable.favourite_filled);
                holder.favourite.setTag(1);
                break;
            }
        }

        if(eName.length() >= 35){
            holder.txtHeader.setText(eName.substring(0,35) + "...");
        }else{
            holder.txtHeader.setText(eName);
        }

        if(0 <= eRating &&  eRating <= 5 ){
            holder.ratingBar.setRating(eRating);
        }else{
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.ratingError.setVisibility(View.VISIBLE);
            holder.ratingError.setText("No rating available");
        }

        if(eDistance.isEmpty()){
            holder.txtFooter.setVisibility(View.INVISIBLE);
        }else{
            holder.txtFooter.setText(eDistance + " mi. away");

        }
    }

    public void showEstablishmentFragment(Establishment data){
        EstablishmentFragment establishmentFragment = new EstablishmentFragment(data, db);
        establishmentFragment.show(this.fragmentManager, "Establishment");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
