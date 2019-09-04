package com.ichsanudinstore.loka.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.IntentUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.StringUtil;
import com.ichsanudinstore.loka.view.SpinnerMaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Ichsanudin_Chairin
 * @since Wednesday 8/14/2019 6:43 PM
 */

public class RegisterActivity extends AppCompatActivity
        implements SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.parent_view)
    NestedScrollView layoutParent;
    @BindView(R.id.content_view)
    LinearLayoutCompat layoutContent;
    @BindView(R.id.mail_input)
    TextInputLayout tilMail;
    @BindView(R.id.mail)
    TextInputEditText edtMail;
    @BindView(R.id.password_input)
    TextInputLayout tilPassword;
    @BindView(R.id.password)
    TextInputEditText edtPassword;
    @BindView(R.id.name_input)
    TextInputLayout tilName;
    @BindView(R.id.name)
    TextInputEditText edtName;
    @BindView(R.id.address_input)
    TextInputLayout tilAddress;
    @BindView(R.id.address)
    TextInputEditText edtAddress;
    @BindView(R.id.phone_input)
    TextInputLayout tilPhone;
    @BindView(R.id.phone)
    TextInputEditText edtPhone;

    @BindView(R.id.business_name_input)
    TextInputLayout tilBusinessName;
    @BindView(R.id.business_name)
    TextInputEditText edtBusinessName;
    @BindView(R.id.business_address_input)
    TextInputLayout tilBusinessAddress;
    @BindView(R.id.business_address)
    TextInputEditText edtBusinessAddress;
    @BindView(R.id.business_phone_input)
    TextInputLayout tilBusinessPhone;
    @BindView(R.id.business_phone)
    TextInputEditText edtBusinessPhone;

    @BindView(R.id.gender)
    SpinnerMaterialButton btnGender;
    @BindView(R.id.send)
    MaterialButton btnSend;
    @BindView(R.id.login)
    MaterialButton btnLogin;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    private ArrayList<SpinnerAdapter.Item> genderItems;

    Call<BaseResponse> sendCreateUpdateAccount;

    {
        this.genderItems = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        this.initializeUI();
        this.hint();
    }

    @Override
    public void onBackPressed() {
        if (sendCreateUpdateAccount != null) {
            sendCreateUpdateAccount.cancel();
            sendCreateUpdateAccount = null;
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.send)
    protected void send() {
        if (StringUtil.isEmpty(Objects.requireNonNull(edtMail.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPassword.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.password),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtName.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.name),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtAddress.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.address),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPhone.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.phone),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnGender.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.gender),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtBusinessName.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.business_name),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtBusinessAddress.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.business_address),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtBusinessPhone.getText()).toString())) {
            BottomSheetUtil.message(
                    RegisterActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.business_phone),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        this.sendCreateUpdateProfile();
    }

    @OnClick(R.id.login)
    protected void login() {
        IntentUtil.goTo(RegisterActivity.this, IntentUtil.generalGoTo(RegisterActivity.this, LoginActivity.class, null, true));
    }

    @OnClick(R.id.gender)
    protected void gender() {
        BottomSheetUtil.spinner(RegisterActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.gender), genderItems, this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        this.btnGender.setSelectedItem(selectedItem);
    }

    private void sendCreateUpdateProfile() {
        if (sendCreateUpdateAccount == null) {
            progressBar.setVisibility(View.VISIBLE);
            RestUtil.resetTimestamp();
            sendCreateUpdateAccount = RestApi.sendCreateUpdateProfile(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            Objects.requireNonNull(edtMail.getText()).toString() +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    null,
                    null,
                    Objects.requireNonNull(edtMail.getText()).toString(),
                    HashUtil.MD5(Objects.requireNonNull(edtMail.getText()).toString() +
                            Objects.requireNonNull(edtPassword.getText()).toString()),
                    Objects.requireNonNull(edtName.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtAddress.getText()).toString(),
                    Byte.parseByte(btnGender.getSelectedItem().getIdentifier().toString()),
                    Objects.requireNonNull(edtPhone.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtBusinessName.getText()).toString(),
                    Objects.requireNonNull(edtBusinessAddress.getText()).toString(),
                    Objects.requireNonNull(edtBusinessPhone.getText()).toString(),
                    (byte) 0
            );

            sendCreateUpdateAccount.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendCreateUpdateAccount = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(RegisterActivity.this,
                                        LocaleUtil.getString(getApplicationContext(), R.string.register) + " " +
                                                LocaleUtil.getString(getApplicationContext(), R.string.message_success1),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendCreateUpdateAccount = null;
                    t.printStackTrace();
                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeUI() {
        genderItems = EntityUtil.setGender(getApplicationContext());
    }

    private void hint() {
        tilMail.setHint(LocaleUtil.getString(getApplicationContext(), R.string.email));
        tilPassword.setHint(LocaleUtil.getString(getApplicationContext(), R.string.password));
        tilName.setHint(LocaleUtil.getString(getApplicationContext(), R.string.name));
        tilAddress.setHint(LocaleUtil.getString(getApplicationContext(), R.string.address));
        tilPhone.setHint(LocaleUtil.getString(getApplicationContext(), R.string.phone));

        tilBusinessName.setHint(LocaleUtil.getString(getApplicationContext(), R.string.business_name));
        tilBusinessAddress.setHint(LocaleUtil.getString(getApplicationContext(), R.string.business_address));
        tilBusinessPhone.setHint(LocaleUtil.getString(getApplicationContext(), R.string.business_phone));

        btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.register_send));
        btnLogin.setText(LocaleUtil.getString(getApplicationContext(), R.string.login_send));
    }
}
