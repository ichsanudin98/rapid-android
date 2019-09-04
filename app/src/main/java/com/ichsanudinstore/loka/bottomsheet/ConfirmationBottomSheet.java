package com.ichsanudinstore.loka.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.ichsanudinstore.loka.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmationBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    @BindView(R.id.title)
    public AppCompatTextView txvTitle;
    @BindView(R.id.message)
    public AppCompatTextView txvMessage;
    @BindView(R.id.back)
    public MaterialButton btnNegative;
    @BindView(R.id.send)
    public MaterialButton btnPositive;

    private ConfirmationBottomSheetCallback confirmationBottomSheetCallback;

    public static ConfirmationBottomSheet newInstance(
            CharSequence title,
            CharSequence message,
            CharSequence negative,
            CharSequence positive
    ) {
        ConfirmationBottomSheet confirmationBottomSheet = new ConfirmationBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putCharSequence("TITLE", title);
        bundle.putCharSequence("MESSAGE", message);
        bundle.putCharSequence("NEGATIVE", negative);
        bundle.putCharSequence("POSITIVE", positive);

        confirmationBottomSheet.setArguments(bundle);

        return confirmationBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_confirmation, container, false);

        ButterKnife.bind(this, view);

        this.extract();
        this.init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.confirmationBottomSheetCallback == null) {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == this.btnNegative) {
            this.dismiss();
            this.confirmationBottomSheetCallback.negative();
        } else if (view == this.btnPositive) {
            this.dismiss();
            this.confirmationBottomSheetCallback.positive();
        }
    }

    public void callback(ConfirmationBottomSheetCallback confirmationBottomSheetCallback) {
        this.confirmationBottomSheetCallback = confirmationBottomSheetCallback;
    }

    private void extract() {
        if (this.getArguments() != null) {
            CharSequence title = this.getArguments().getCharSequence("TITLE");
            CharSequence message = this.getArguments().getCharSequence("MESSAGE");
            CharSequence negative = this.getArguments().getCharSequence("NEGATIVE");
            CharSequence positive = this.getArguments().getCharSequence("POSITIVE");

            if (title != null) {
                this.txvTitle.setText(title);
            }

            if (message != null) {
                this.txvMessage.setText(message);

                this.txvMessage.setVisibility(View.VISIBLE);
            }

            if (negative != null) {
                this.btnNegative.setText(negative);
            }

            if (positive != null) {
                this.btnPositive.setText(positive);
            }
        }
    }

    private void init() {
        this.btnNegative.setOnClickListener(this);
        this.btnPositive.setOnClickListener(this);
    }

    public interface ConfirmationBottomSheetCallback {
        void negative();

        void positive();
    }
}