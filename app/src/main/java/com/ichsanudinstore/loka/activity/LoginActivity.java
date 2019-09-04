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
import com.ichsanudinstore.loka.api.RestManager;
import com.ichsanudinstore.loka.api.endpoint.profile.login.LoginResponse;
import com.ichsanudinstore.loka.bottomsheet.BaseUrlBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.IntentUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import org.jetbrains.annotations.NotNull;

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

public class LoginActivity extends AppCompatActivity
        implements BaseUrlBottomSheet.BaseUrlBottomSheetCallback {
    @BindView(R.id.parent_view)
    LinearLayoutCompat layoutParent;
    @BindView(R.id.mail_input)
    TextInputLayout tilMail;
    @BindView(R.id.mail)
    TextInputEditText edtMail;
    @BindView(R.id.password_input)
    TextInputLayout tilPassword;
    @BindView(R.id.password)
    TextInputEditText edtPassword;
    @BindView(R.id.send)
    MaterialButton btnSend;
    @BindView(R.id.register)
    MaterialButton btnRegister;
    @BindView(R.id.base_url)
    MaterialButton btnUrl;
    @BindView(R.id.forget_password)
    MaterialButton btnForgetPassword;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    Call<LoginResponse> sendLoginResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.initializeBaseUrl(null);
        this.hint();
        this.checkSession();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sendLoginResponse != null) {
            sendLoginResponse.cancel();
            sendLoginResponse = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (sendLoginResponse != null) {
            sendLoginResponse.cancel();
            sendLoginResponse = null;
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.send)
    protected void send() {
        if (StringUtil.isEmpty(Objects.requireNonNull(edtMail.getText()).toString())) {
            BottomSheetUtil.message(
                    LoginActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (!StringUtil.isEmailValid(Objects.requireNonNull(edtMail.getText()).toString())) {
            BottomSheetUtil.message(
                    LoginActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.message_invalid_email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    false
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPassword.getText()).toString())) {
            BottomSheetUtil.message(
                    LoginActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.password),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        sendLogin();
    }

    @OnClick(R.id.register)
    protected void register() {
        IntentUtil.goTo(LoginActivity.this, IntentUtil.generalGoTo(LoginActivity.this, RegisterActivity.class, null, false));
    }

    @OnClick(R.id.forget_password)
    protected void forgetPassword() {
        IntentUtil.goTo(LoginActivity.this, IntentUtil.generalGoTo(LoginActivity.this, ForgetPasswordActivity.class, null, false));
    }

    @OnClick(R.id.base_url)
    protected void baseUrl() {
        BottomSheetUtil.baseUrl(LoginActivity.this, LoginActivity.this);
    }


    @Override
    public void onUpdateBaseUrl(String baseUrl) {
        this.initializeBaseUrl(baseUrl);
        RestManager.GET_RETROFIT((byte) 0);
    }

    private void sendLogin() {
        if (sendLoginResponse == null) {
            progressBar.setVisibility(View.VISIBLE);
            RestUtil.resetTimestamp();
            sendLoginResponse = RestApi.sendLogin(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            Objects.requireNonNull(edtMail.getText()).toString() +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    Objects.requireNonNull(edtMail.getText()).toString(),
                    HashUtil.MD5(
                            Objects.requireNonNull(edtMail.getText()).toString() +
                                    Objects.requireNonNull(edtPassword.getText()).toString())
            );
            sendLoginResponse.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendLoginResponse = null;
                    if (response.isSuccessful()) {
                        LoginResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(LoginActivity.this,
                                        LocaleUtil.getString(getApplicationContext(), R.string.login) + " " +
                                                LocaleUtil.getString(getApplicationContext(), R.string.message_success1),
                                        Toast.LENGTH_SHORT).show();
                                SharedPreferencesUtil.getInstance().begin();
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.SESSION_ID, bodyResponse.getSession_id());
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.USER_ID, bodyResponse.getUser_id());
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.NAME, bodyResponse.getUser_name());
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.EMAIL, edtMail.getText().toString());
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.COMPANY_ID, bodyResponse.getCompany_id());
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.ROLE_NAME, bodyResponse.getRole_name());
                                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.ROLE_ID, bodyResponse.getRole_id());
                                SharedPreferencesUtil.getInstance().commit();
                                SharedPreferencesUtil.getInstance().close();
                                IntentUtil.goTo(LoginActivity.this, IntentUtil.generalGoTo(LoginActivity.this, DashboardActivity.class, null, true));
                            } else {
                                Toast.makeText(LoginActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendLoginResponse = null;
                    t.printStackTrace();
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeBaseUrl(String baseUrl) {
        if (baseUrl == null) {
            if (SharedPreferencesUtil.get(Constant.SharedPreferenceKey.BASE_URL, String.class, Constant.URL.BASE_DEVELOPMENT).equals(Constant.URL.BASE_DEVELOPMENT)) {
                SharedPreferencesUtil.getInstance().begin();
                SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.BASE_URL, Constant.URL.BASE_DEVELOPMENT);
                SharedPreferencesUtil.getInstance().commit();
            }
        } else {
            SharedPreferencesUtil.getInstance().begin();
            SharedPreferencesUtil.getInstance().put(Constant.SharedPreferenceKey.BASE_URL, baseUrl);
            SharedPreferencesUtil.getInstance().commit();
        }
    }

    private void hint() {
        this.tilMail.setHint(LocaleUtil.getString(getApplicationContext(), R.string.email));
        this.tilPassword.setHint(LocaleUtil.getString(getApplicationContext(), R.string.password));

        this.btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.login_send));
        this.btnRegister.setText(LocaleUtil.getString(getApplicationContext(), R.string.register_send));
        this.btnUrl.setText(LocaleUtil.getString(getApplicationContext(), R.string.base_url));
        this.btnForgetPassword.setText(LocaleUtil.getString(getApplicationContext(), R.string.forget_password));
    }

    private void checkSession() {
        if (!SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class, "").equals("")) {
            IntentUtil.goTo(LoginActivity.this, IntentUtil.generalGoTo(LoginActivity.this, DashboardActivity.class, null, true));

        }
    }
}
