package com.ichsanudinstore.loka.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    @BindView(R.id.parent_view)
    LinearLayoutCompat layoutParent;
    @BindView(R.id.mail_input)
    TextInputLayout tilMail;
    @BindView(R.id.mail)
    TextInputEditText edtMail;
    @BindView(R.id.phone_input)
    TextInputLayout tilPhone;
    @BindView(R.id.phone)
    TextInputEditText edtPhone;
    @BindView(R.id.confirmation_password_input)
    TextInputLayout tilConfirmationPassword;
    @BindView(R.id.confirmation_password)
    TextInputEditText edtConfirmationPassword;
    @BindView(R.id.send)
    MaterialButton btnSend;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    Call<BaseResponse> sendForgetPasswordResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        this.hint();
    }

    @Override
    public void onBackPressed() {
        if (sendForgetPasswordResponse != null) {
            sendForgetPasswordResponse.cancel();
            sendForgetPasswordResponse = null;
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.send)
    protected void send() {
        if (StringUtil.isEmpty(Objects.requireNonNull(edtMail.getText()).toString())) {
            BottomSheetUtil.message(
                    ForgetPasswordActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (!StringUtil.isEmailValid(Objects.requireNonNull(edtMail.getText()).toString())) {
            BottomSheetUtil.message(
                    ForgetPasswordActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.message_invalid_email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    false
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPhone.getText()).toString())) {
            BottomSheetUtil.message(
                    ForgetPasswordActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.phone),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtConfirmationPassword.getText()).toString())) {
            BottomSheetUtil.message(
                    ForgetPasswordActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.confirmation_password),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        sendForgetPassword();
    }

    private void sendForgetPassword() {
        if (sendForgetPasswordResponse == null) {
            progressBar.setVisibility(View.VISIBLE);
            RestUtil.resetTimestamp();
            sendForgetPasswordResponse = RestApi.sendForgetPassword(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            Objects.requireNonNull(edtMail.getText()).toString() +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    Objects.requireNonNull(edtMail.getText()).toString(),
                    Objects.requireNonNull(edtPhone.getText()).toString(),
                    HashUtil.MD5(
                            Objects.requireNonNull(edtMail.getText()).toString() +
                                    Objects.requireNonNull(edtConfirmationPassword.getText()).toString())
            );
            sendForgetPasswordResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendForgetPasswordResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(ForgetPasswordActivity.this,
                                        LocaleUtil.getString(getApplicationContext(), R.string.forget_password) + " " +
                                                LocaleUtil.getString(getApplicationContext(), R.string.message_success1),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendForgetPasswordResponse = null;
                    t.printStackTrace();
                    Toast.makeText(ForgetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void hint() {
        this.tilMail.setHint(LocaleUtil.getString(getApplicationContext(), R.string.email));
        this.tilPhone.setHint(LocaleUtil.getString(getApplicationContext(), R.string.phone));
        this.tilConfirmationPassword.setHint(LocaleUtil.getString(getApplicationContext(), R.string.confirmation_password));

        this.btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.login_send));
    }
}
