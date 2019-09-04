package com.ichsanudinstore.loka.bottomsheet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Ichsanudin_Chairin
 * @since Sunday 8/25/2019 9:03 AM
 */
public class BaseUrlBottomSheet extends BottomSheetDialogFragment {
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

    private BaseUrlBottomSheetCallback callback;

    public BaseUrlBottomSheet() {
        super();
    }

    public static BaseUrlBottomSheet newInstance(
            BaseUrlBottomSheetCallback callback
    ) {
        BaseUrlBottomSheet baseUrlBottomSheet = new BaseUrlBottomSheet();
        baseUrlBottomSheet.setCallback(callback);
        return baseUrlBottomSheet;
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
    }

    @OnClick(R.id.send)
    void send() {
        if (!TextUtils.isEmpty(edtName.getText())) {
            callback.onUpdateBaseUrl(edtName.getText().toString());
            dismiss();
        }
    }

    @SuppressLint("SetTextI18n")
    private void hint() {
        this.txvTitle.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.base_url));

        this.tilName.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.base_url));

        this.btnSend.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.save));

        this.edtName.setText(SharedPreferencesUtil.get(Constant.SharedPreferenceKey.BASE_URL, String.class, Constant.URL.BASE_DEVELOPMENT));
    }

    public BaseUrlBottomSheetCallback getCallback() {
        return callback;
    }

    public void setCallback(BaseUrlBottomSheetCallback callback) {
        this.callback = callback;
    }

    public interface BaseUrlBottomSheetCallback {
        void onUpdateBaseUrl(String baseUrl);
    }

}
