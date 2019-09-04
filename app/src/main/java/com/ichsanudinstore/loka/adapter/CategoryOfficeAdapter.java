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
import com.ichsanudinstore.loka.model.entity.CategoryOfficeModel;
import com.ichsanudinstore.loka.util.LocaleUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Ichsanudin_Chairin
 * @since Sunday 8/25/2019 9:15 AM
 */
public class CategoryOfficeAdapter extends RealmRecyclerViewAdapter<CategoryOfficeModel, RecyclerView.ViewHolder> {
    private Fragment fragment;
    private CategoryOfficeAdapterListener listener;

    public CategoryOfficeAdapter(
            @Nullable OrderedRealmCollection<CategoryOfficeModel> data,
            boolean autoUpdate,
            Fragment fragment,
            CategoryOfficeAdapterListener listener
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
        layoutView = inflater.inflate(R.layout.adapter_category_office, parent, false);
        viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryOfficeModel data = this.getItem(position);
        if (data != null) {
            ((ViewHolder) holder).txvName.setText(data.getName());

            ((ViewHolder) holder).btnDetail.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.detail));
            ((ViewHolder) holder).btnEdit.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.edit));
            ((ViewHolder) holder).btnDelete.setText(LocaleUtil.getString(Objects.requireNonNull(fragment.getContext()), R.string.delete));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parent_view)
        MaterialCardView layoutParent;
        @BindView(R.id.name)
        AppCompatTextView txvName;
        @BindView(R.id.edit)
        MaterialButton btnEdit;
        @BindView(R.id.detail)
        MaterialButton btnDetail;
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

    public interface CategoryOfficeAdapterListener {
        void onItemTriggeredClick(byte type, int position, CategoryOfficeModel data);
    }
}
