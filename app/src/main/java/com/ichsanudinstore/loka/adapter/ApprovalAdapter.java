package com.ichsanudinstore.loka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
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
 * @since Wednesday 8/21/2019 4:09 PM
 */
public class ApprovalAdapter extends RealmRecyclerViewAdapter<ProfileModel, RecyclerView.ViewHolder> {
    private Fragment fragment;
    private ApprovalAdapterListener listener;

    public ApprovalAdapter(
            @Nullable OrderedRealmCollection<ProfileModel> data,
            boolean autoUpdate,
            Fragment fragment,
            ApprovalAdapterListener listener
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
        layoutView = inflater.inflate(R.layout.adapter_approval, parent, false);
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
//            if (data.getImage() != null)
//                ((ViewHolder) holder).imgUser.setImageBitmap();
            ((ViewHolder) holder).txvName.setText(data.getName());
            ((ViewHolder) holder).txvAddress.setText(data.getAddress());

            if (!data.getF_activated())
                ((ViewHolder) holder).btnActivation.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.activated));
            else
                ((ViewHolder) holder).btnActivation.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.deactivated));
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
        @BindView(R.id.activation)
        MaterialButton btnActivation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.activation)
        void onActivationClick() {
            listener.onItemActivationClick(getAdapterPosition(), Objects.requireNonNull(getItem(getAdapterPosition())));
        }

        @OnClick(R.id.parent_view)
        void onItemClick() {
            listener.onItemClick(getAdapterPosition(), Objects.requireNonNull(getItem(getAdapterPosition())));
        }
    }

    public interface ApprovalAdapterListener {
        void onItemClick(int position, ProfileModel data);

        void onItemActivationClick(int position, ProfileModel data);
    }
}
