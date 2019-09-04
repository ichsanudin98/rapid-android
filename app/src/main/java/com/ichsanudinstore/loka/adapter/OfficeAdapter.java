package com.ichsanudinstore.loka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.model.entity.OfficeModel;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Ichsanudin_Chairin
 * @since Thursday 8/15/2019 4:58 PM
 */
public class OfficeAdapter extends RealmRecyclerViewAdapter<OfficeModel, RecyclerView.ViewHolder> {
    private Fragment fragment;
    private OfficeAdapterListener listener;

    public OfficeAdapter(
            @Nullable OrderedRealmCollection<OfficeModel> data,
            boolean autoUpdate,
            Fragment fragment,
            OfficeAdapterListener listener
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
        layoutView = inflater.inflate(R.layout.adapter_office, parent, false);
        viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OfficeModel data = this.getItem(position);
        if (data != null) {
            ((ViewHolder) holder).txvAddress.setText(data.getAddress());
            ((ViewHolder) holder).txvName.setText(data.getName());
            ((ViewHolder) holder).chpSeat.setText(
                    StringUtil.isEmpty(data.getTotal_seat()) ?
                            "0 " + LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.seat) :
                            data.getTotal_seat() + " " + LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.seat)
            );
            if (data.getImage() == null)
                ((ViewHolder) holder).imgOffice.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(fragment.getContext()), R.drawable.ic_office_black_24dp));

            ((ViewHolder) holder).btnDetail.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.detail));
            ((ViewHolder) holder).btnEdit.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.edit));
            ((ViewHolder) holder).btnDelete.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.delete));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent_view)
        MaterialCardView layoutParent;
        @BindView(R.id.adapter_office_address)
        AppCompatTextView txvAddress;
        @BindView(R.id.content_view)
        LinearLayoutCompat layoutContent;
        @BindView(R.id.adapter_office_description_view)
        FrameLayout layoutDescription;
        @BindView(R.id.adapter_office_image)
        AppCompatImageView imgOffice;
        @BindView(R.id.adapter_office_name)
        AppCompatTextView txvName;
        @BindView(R.id.adapter_office_seat)
        Chip chpSeat;
        @BindView(R.id.detail)
        MaterialButton btnDetail;
        @BindView(R.id.edit)
        MaterialButton btnEdit;
        @BindView(R.id.delete)
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.edit)
        void onEditClick() {
            listener.onItemTriggeredClick((byte) 1, getAdapterPosition(), Objects.requireNonNull(getItem(getAdapterPosition())));
        }

        @OnClick(R.id.detail)
        void onDetailClick() {
            listener.onItemTriggeredClick((byte) 2, getAdapterPosition(), Objects.requireNonNull(getItem(getAdapterPosition())));
        }

        @OnClick(R.id.delete)
        void onDeleteClick() {
            listener.onItemTriggeredClick((byte) 3, getAdapterPosition(), Objects.requireNonNull(getItem(getAdapterPosition())));
        }

    }

    public interface OfficeAdapterListener {
        void onItemTriggeredClick(byte type, int position, OfficeModel data);
    }
}
