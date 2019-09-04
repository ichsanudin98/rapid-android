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
import com.ichsanudinstore.loka.model.entity.SeatModel;
import com.ichsanudinstore.loka.util.HashUtil;
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
public class RentBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.parent_view)
    MaterialCardView layoutParent;
    @BindView(R.id.content_view)
    LinearLayoutCompat layoutContent;
    @BindView(R.id.title)
    AppCompatTextView txvTitle;
    @BindView(R.id.note_input)
    TextInputLayout tilNote;
    @BindView(R.id.note)
    TextInputEditText edtNote;
    @BindView(R.id.password_input)
    TextInputLayout tilPassword;
    @BindView(R.id.password)
    TextInputEditText edtPassword;
    @BindView(R.id.send)
    MaterialButton btnSend;

    private byte type;

    private SeatModel data;

    private RentBottomSheetCallback callback;

    public RentBottomSheet() {
        super();
    }

    public static RentBottomSheet newInstance(
            RentBottomSheetCallback callback,
            byte type,
            SeatModel data
    ) {
        RentBottomSheet categoryOfficeBottomSheet = new RentBottomSheet();
        categoryOfficeBottomSheet.setCallback(callback);
        categoryOfficeBottomSheet.setType(type);
        categoryOfficeBottomSheet.setData(data);
        return categoryOfficeBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_rent, container, false);
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
        if (StringUtil.isEmpty(Objects.requireNonNull(edtNote.getText()).toString())) {
            tilNote.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.note) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPassword.getText()).toString())) {
            tilPassword.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.password) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        callback.onUpdateSeat(
                getType(),
                getData(),
                edtNote.getText().toString(),
                HashUtil.MD5(
                        edtPassword.getText().toString()
                )
        );

        dismiss();
    }

    private void initializeUI() {
        if (data != null)
            this.edtNote.setText(data.getNote());

        this.edtNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tilNote.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tilPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void hint() {
        this.tilNote.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.note));
        this.tilPassword.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.password));

        this.btnSend.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.save));

        this.txvTitle.setText(
                LocaleUtil.getString(getContext(), R.string.edit) + " " +
                        LocaleUtil.getString(getContext(), R.string.rent)
        );
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public SeatModel getData() {
        return data;
    }

    public void setData(SeatModel data) {
        this.data = data;
    }

    public RentBottomSheetCallback getCallback() {
        return callback;
    }

    public void setCallback(RentBottomSheetCallback callback) {
        this.callback = callback;
    }

    public interface RentBottomSheetCallback {
        void onUpdateSeat(byte type, SeatModel data, String note, String password);
    }
}
