package com.ichsanudinstore.loka.bottomsheet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.model.entity.CategoryOfficeModel;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Ichsanudin_Chairin
 * @since Sunday 8/25/2019 9:03 AM
 */
public class CategoryOfficeBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.parent_view)
    MaterialCardView layoutParent;
    @BindView(R.id.content_view)
    LinearLayoutCompat layoutContent;
    @BindView(R.id.title)
    AppCompatTextView txvTitle;
    @BindView(R.id.name_input)
    TextInputLayout tilName;
    @BindView(R.id.name)
    TextInputEditText edtName;
    @BindView(R.id.send)
    MaterialButton btnSend;

    private byte type;

    private CategoryOfficeModel data;

    private CategoryOfficeBottomSheetCallback callback;

    public CategoryOfficeBottomSheet() {
        super();
    }

    public static CategoryOfficeBottomSheet newInstance(
            CategoryOfficeBottomSheetCallback callback,
            byte type,
            CategoryOfficeModel data
    ) {
        CategoryOfficeBottomSheet categoryOfficeBottomSheet = new CategoryOfficeBottomSheet();
        categoryOfficeBottomSheet.setCallback(callback);
        categoryOfficeBottomSheet.setType(type);
        categoryOfficeBottomSheet.setData(data);
        return categoryOfficeBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_category_office, container, false);
        ButterKnife.bind(this, view);
        this.hint();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initializeUI();
    }

    @OnClick(R.id.send)
    void send() {
        if (StringUtil.isEmpty(Objects.requireNonNull(edtName.getText()).toString())) {
            tilName.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.name) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        if (type == 0 || type == 1)
            callback.onUpdateCategoryOffice(data, edtName.getText().toString());
        dismiss();
    }

    private void initializeUI() {
        if (data != null)
            this.edtName.setText(data.getName());

        if (type == 2) {
            this.edtName.setFocusable(false);
            this.btnSend.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.close));
        }

        this.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tilName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void hint() {
        this.tilName.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.category_name));

        this.btnSend.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.save));

        if (type == 0) {
            this.txvTitle.setText(
                    LocaleUtil.getString(getContext(), R.string.create) + " " +
                            LocaleUtil.getString(getContext(), R.string.category)
            );
        } else if (type == 1) {
            this.txvTitle.setText(
                    LocaleUtil.getString(getContext(), R.string.edit) + " " +
                            LocaleUtil.getString(getContext(), R.string.category)
            );
        } else if (type == 2) {
            this.txvTitle.setText(
                    LocaleUtil.getString(getContext(), R.string.detail) + " " +
                            LocaleUtil.getString(getContext(), R.string.category)
            );
        }
    }

    public CategoryOfficeBottomSheetCallback getCallback() {
        return callback;
    }

    public void setCallback(CategoryOfficeBottomSheetCallback callback) {
        this.callback = callback;
    }

    public interface CategoryOfficeBottomSheetCallback {
        void onUpdateCategoryOffice(CategoryOfficeModel data, String name);
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public CategoryOfficeModel getData() {
        return data;
    }

    public void setData(CategoryOfficeModel data) {
        this.data = data;
    }
}
