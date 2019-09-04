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
public class ChangePasswordBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.parent_view)
    MaterialCardView layoutParent;
    @BindView(R.id.content_view)
    LinearLayoutCompat layoutContent;
    @BindView(R.id.title)
    AppCompatTextView txvTitle;
    @BindView(R.id.old_password_input)
    TextInputLayout tilOldPassword;
    @BindView(R.id.old_password)
    TextInputEditText edtOldPassword;
    @BindView(R.id.password_input)
    TextInputLayout tilNewPassword;
    @BindView(R.id.password)
    TextInputEditText edtNewPassword;
    @BindView(R.id.confirmation_password_input)
    TextInputLayout tilConfirmPassword;
    @BindView(R.id.confirmation_password)
    TextInputEditText edtConfirmPassword;

    @BindView(R.id.send)
    MaterialButton btnSend;

    private ChangePasswordBottomSheetCallback callback;

    public ChangePasswordBottomSheet() {
        super();
    }

    public static ChangePasswordBottomSheet newInstance(
            ChangePasswordBottomSheetCallback callback
    ) {
        ChangePasswordBottomSheet changePasswordBottomSheet = new ChangePasswordBottomSheet();
        changePasswordBottomSheet.setCallback(callback);
        return changePasswordBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_change_password, container, false);
        ButterKnife.bind(this, view);
        this.hint();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.send)
    void send() {
        if (StringUtil.isEmpty(Objects.requireNonNull(edtOldPassword.getText()).toString())) {
            tilOldPassword.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.old_password) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtNewPassword.getText()).toString())) {
            tilNewPassword.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.password) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtConfirmPassword.getText()).toString())) {
            tilConfirmPassword.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.confirmation_password) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        if (!Objects.requireNonNull(edtNewPassword.getText()).toString().equals(Objects.requireNonNull(edtConfirmPassword.getText()).toString())) {
            tilConfirmPassword.setError(LocaleUtil.getString(Objects.requireNonNull(getContext()),
                    R.string.confirmation_password) + " " +
                    LocaleUtil.getString(getContext(), R.string.required));
            return;
        }

        this.callback.onChangePassword(
                HashUtil.MD5(Objects.requireNonNull(edtOldPassword.getText()).toString() +
                        Objects.requireNonNull(edtOldPassword.getText()).toString()),
                HashUtil.MD5(Objects.requireNonNull(edtConfirmPassword.getText()).toString() +
                        Objects.requireNonNull(edtConfirmPassword.getText()).toString())
        );
    }

    private void initializeUI() {
        this.edtOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tilOldPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.edtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tilNewPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                    tilConfirmPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void hint() {
        this.txvTitle.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.edit) + " " +
                LocaleUtil.getString(getContext(), R.string.password).toLowerCase());

        this.tilOldPassword.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.old_password));
        this.tilNewPassword.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.password));
        this.tilConfirmPassword.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.confirmation_password));

        this.btnSend.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.save));
    }

    public ChangePasswordBottomSheetCallback getCallback() {
        return callback;
    }

    public void setCallback(ChangePasswordBottomSheetCallback callback) {
        this.callback = callback;
    }

    public interface ChangePasswordBottomSheetCallback {
        void onChangePassword(String oldPassword, String newPassword);
    }

}
