package com.ichsanudinstore.loka.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import java.util.ArrayList;

public class SpinnerMaterialButton extends MaterialButton {
    private ArrayList<SpinnerAdapter.Item> items;
    private SpinnerAdapter.Item selectedItem;
    private OnSelectedChangeListener onSelectedChangeListener;

    {
        this.items = new ArrayList<>();
        this.setIconResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
        this.setIconGravity(ICON_GRAVITY_START);
        this.determine();
    }

    public SpinnerMaterialButton(Context context) {
        super(context);
    }

    public SpinnerMaterialButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerMaterialButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArrayList<SpinnerAdapter.Item> getItems() {
        return this.items;
    }

    public SpinnerAdapter.Item getSelectedItem() {
        return this.selectedItem;
    }

    public void setSelectedItem(SpinnerAdapter.Item selectedItem) {
        this.selectedItem = selectedItem;
        this.determine();

        if (this.onSelectedChangeListener != null) {
            this.onSelectedChangeListener.onSelectedChange(this, this.selectedItem);
        }
    }

    public void clear() {
        this.items.clear();
        this.clearSelectionItem();
    }

    public void clearSelectionItem() {
        this.selectedItem = null;
        this.determine();
    }

    private void determine() {
        if (this.selectedItem == null) {
            this.setText(R.string.select_one);
        } else {
            if (!StringUtil.isEmpty(this.selectedItem.getSelectedDescription())) {
                this.setText(this.selectedItem.getSelectedDescription());
            } else {
                this.setText(this.selectedItem.getDescription());
            }
        }
    }

    public void showSpinner(AppCompatActivity appCompatActivity, String title) {
        BottomSheetUtil.spinner(appCompatActivity, title, this.getItems(), new SpinnerBottomSheet.SpinnerBottomSheetCallback() {
            @Override
            public void dismiss() {
            }

            @Override
            public void select(SpinnerAdapter.Item selectedItem) {
                SpinnerMaterialButton.this.setSelectedItem(selectedItem);
            }
        });
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    public interface OnSelectedChangeListener {
        void onSelectedChange(SpinnerMaterialButton spinnerMaterialButton, SpinnerAdapter.Item selectedItem);
    }
}