package com.ichsanudinstore.loka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.model.entity.SeatModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Ichsanudin_Chairin
 * @since Friday 8/16/2019 4:14 PM
 */
public class SeatAdapter extends RealmRecyclerViewAdapter<SeatModel, RecyclerView.ViewHolder> {
    private Fragment fragment;
    private SeatAdapterListener listener;

    public SeatAdapter(
            @Nullable OrderedRealmCollection<SeatModel> data,
            boolean autoUpdate,
            Fragment fragment,
            SeatAdapterListener listener
    ) {
        super(data, autoUpdate);
        this.fragment = fragment;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(parent, LayoutInflater.from(parent.getContext()), viewType);
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View layoutView;
        layoutView = inflater.inflate(R.layout.adapter_seat, parent, false);
        viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SeatModel data = this.getItem(position);
        if (data != null) {
            if (data.getStatus())
                ((ViewHolder) holder).layoutContent.setBackgroundColor(ContextCompat.getColor(
                        Objects.requireNonNull(fragment.getContext()), R.color.colorPrimaryDark
                ));
            else
                ((ViewHolder) holder).layoutContent.setBackgroundColor(ContextCompat.getColor(
                        Objects.requireNonNull(fragment.getContext()), R.color.colorAccent
                ));

            ((ViewHolder) holder).txvName.setText(data.getName());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent_view)
        MaterialCardView layoutParent;
        @BindView(R.id.content_view)
        LinearLayoutCompat layoutContent;
        @BindView(R.id.name)
        AppCompatTextView txvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.parent_view)
        void onItemClick() {
            listener.onItemClick(getAdapterPosition(), Objects.requireNonNull(getItem(getAdapterPosition())));
        }
    }


    public interface SeatAdapterListener {
        void onItemClick(int position, SeatModel data);
    }
}
