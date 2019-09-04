package com.ichsanudinstore.loka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.model.entity.RentModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Ichsanudin_Chairin
 * @since Saturday 8/31/2019 1:50 AM
 */
public class HistoryRentAdapter extends RealmRecyclerViewAdapter<RentModel, RecyclerView.ViewHolder> {
    private Fragment fragment;

    public HistoryRentAdapter(
            @Nullable OrderedRealmCollection<RentModel> data,
            boolean autoUpdate,
            Fragment fragment) {
        super(data, autoUpdate);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(parent, LayoutInflater.from(parent.getContext()), viewType);
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View layoutView;
        layoutView = inflater.inflate(R.layout.adapter_history_rent, parent, false);
        viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RentModel data = this.getItem(position);
        if (data != null) {
            ((ViewHolder) holder).txvName.setText(data.getSeat_name());
            ((ViewHolder) holder).txvNote.setText(data.getNote());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent_view)
        MaterialCardView layoutParent;
        @BindView(R.id.name)
        AppCompatTextView txvName;
        @BindView(R.id.date)
        Chip chpDate;
        @BindView(R.id.note)
        AppCompatTextView txvNote;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
