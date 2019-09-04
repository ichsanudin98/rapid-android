package com.ichsanudinstore.loka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.model.entity.ProfileModel;
import com.ichsanudinstore.loka.util.LocaleUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Ichsanudin_Chairin
 * @since Monday 8/26/2019 12:43 AM
 */
public class KeeperAdapter extends RealmRecyclerViewAdapter<ProfileModel, RecyclerView.ViewHolder> {
    private Fragment fragment;
    private KeeperAdapterListener listener;

    public KeeperAdapter(
            @Nullable OrderedRealmCollection<ProfileModel> data,
            boolean autoUpdate,
            Fragment fragment,
            KeeperAdapterListener listener
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
        layoutView = inflater.inflate(R.layout.adapter_keeper, parent, false);
        viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProfileModel data = this.getItem(position);
        if (data != null) {
            if (data.getImage() == null)
                ((ViewHolder) holder).imgUser.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(fragment.getContext()), R.drawable.ic_keeper_black_24dp));
            ((ViewHolder) holder).txvName.setText(data.getName());
            ((ViewHolder) holder).txvAddress.setText(data.getAddress());

            ((ViewHolder) holder).btnDetail.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.detail));
            ((ViewHolder) holder).btnEdit.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.edit));
            ((ViewHolder) holder).btnDelete.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.delete));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent_view)
        MaterialCardView layoutParent;
        @BindView(R.id.image)
        CircleImageView imgUser;
        @BindView(R.id.name)
        AppCompatTextView txvName;
        @BindView(R.id.address)
        AppCompatTextView txvAddress;
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

    public interface KeeperAdapterListener {
        void onItemTriggeredClick(byte type, int position, ProfileModel data);
    }
}
