package com.ichsanudinstore.loka.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.ViewHolder> {
    private List<Item> items;
    private SpinnerBottomSheet spinnerBottomSheet;

    {
        this.items = new ArrayList<>();
    }

    public SpinnerAdapter(SpinnerBottomSheet spinnerBottomSheet) {
        this.spinnerBottomSheet = spinnerBottomSheet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_spinner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = this.items.get(position);

        holder.txvName.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public List<Item> getItems() {
        return this.items;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.name)
        public TextView txvName;

        public ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            this.init();
        }

        @Override
        public void onClick(View v) {
            Item item = SpinnerAdapter.this.items.get(this.getAdapterPosition());

            if (item != null) {
                if (v == this.itemView) {
                    SpinnerAdapter.this.spinnerBottomSheet.spinnerBottomSheetCallback.select(item);
                    SpinnerAdapter.this.spinnerBottomSheet.dismiss();
                }
            }
        }

        private void init() {
            this.itemView.setOnClickListener(this);
        }
    }

    public static class Item implements Serializable {
        private Object identifier;
        private String description;
        private String selectedDescription;

        public Object getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(Object identifier) {
            this.identifier = identifier;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSelectedDescription() {
            return this.selectedDescription;
        }

        public void setSelectedDescription(String selectedDescription) {
            this.selectedDescription = selectedDescription;
        }
    }
}