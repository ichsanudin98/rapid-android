package com.ichsanudinstore.loka.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.ProfileModel;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;
import com.ichsanudinstore.loka.util.StringUtil;
import com.ichsanudinstore.loka.view.SpinnerMaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfileActivity extends AppCompatActivity
        implements SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.parent_view)
    CoordinatorLayout layoutParent;
    @BindView(R.id.appbar_view)
    AppBarLayout layoutAppbar;
    @BindView(R.id.toolbar_view)
    Toolbar layoutToolbar;
    @BindView(R.id.content_view)
    NestedScrollView layoutContent;
    @BindView(R.id.mail_input)
    TextInputLayout tilMail;
    @BindView(R.id.mail)
    TextInputEditText edtMail;
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
    @BindView(R.id.gender)
    SpinnerMaterialButton btnGender;
    @BindView(R.id.send)
    MaterialButton btnSend;

    private Realm realm;

    private Long valueID;
    private ProfileModel profileEntity;

    private Call<BaseResponse> sendCreateUpdateAccount;

    ArrayList<SpinnerAdapter.Item> genderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        ButterKnife.bind(this);
        this.initializeUI();
        this.hint();
        this.initializeData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
    }

    @OnClick(R.id.send)
    protected void send() {
        if (StringUtil.isEmpty(Objects.requireNonNull(edtMail.getText()).toString())) {
            BottomSheetUtil.message(
                    ChangeProfileActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtName.getText()).toString())) {
            BottomSheetUtil.message(
                    ChangeProfileActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.name),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtAddress.getText()).toString())) {
            BottomSheetUtil.message(
                    ChangeProfileActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.address),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPhone.getText()).toString())) {
            BottomSheetUtil.message(
                    ChangeProfileActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.phone),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnGender.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    ChangeProfileActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.gender),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        this.sendCreateUpdateProfile();
    }

    @OnClick(R.id.gender)
    protected void setGender() {
        BottomSheetUtil.spinner(ChangeProfileActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.gender), genderItems, this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        this.btnGender.setSelectedItem(selectedItem);
    }

    private void hint() {
        tilAddress.setHint(LocaleUtil.getString(getApplicationContext(), R.string.address));
        tilMail.setHint(LocaleUtil.getString(getApplicationContext(), R.string.email));
        tilName.setHint(LocaleUtil.getString(getApplicationContext(), R.string.name));
        tilPhone.setHint(LocaleUtil.getString(getApplicationContext(), R.string.phone));

        btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.save));

        Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                LocaleUtil.getString(getApplicationContext(), R.string.edit) + " " +
                        LocaleUtil.getString(getApplicationContext(), R.string.profile)
        );
    }

    private void initializeUI() {
        this.realm = Realm.getDefaultInstance();

        this.valueID = getIntent().getLongExtra("id", -1L);

        this.setSupportActionBar(layoutToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.layoutToolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initializeData() {
        genderItems = new ArrayList<>();
        genderItems = EntityUtil.setGender(getApplicationContext());

        profileEntity = null;
        if (valueID != -1) {
            profileEntity = realm.where(ProfileModel.class)
                    .equalTo("id", valueID)
                    .findFirst();

            if (profileEntity != null) {
                edtMail.setText(profileEntity.getEmail());
                edtName.setText(profileEntity.getName());
                edtAddress.setText(profileEntity.getAddress());
                edtPhone.setText(profileEntity.getPhone());

                if (profileEntity.getGender() != null) {
                    for (SpinnerAdapter.Item item : genderItems) {
                        if (profileEntity.getGender() == Byte.parseByte(String.valueOf(item.getIdentifier()))) {
                            btnGender.setSelectedItem(item);
                        }
                    }
                }
            }
        }
    }

    private void sendCreateUpdateProfile() {
        if (sendCreateUpdateAccount == null) {
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
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    String.valueOf(valueID),
                    Objects.requireNonNull(edtMail.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtName.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtAddress.getText()).toString(),
                    Byte.parseByte(btnGender.getSelectedItem().getIdentifier().toString()),
                    Objects.requireNonNull(edtPhone.getText()).toString(),
                    String.valueOf(profileEntity.getOffice_id()),
                    profileEntity.getCompany_name(),
                    profileEntity.getCompany_address(),
                    profileEntity.getCompany_phone(),
                    (byte) 1
            );

            sendCreateUpdateAccount.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendCreateUpdateAccount = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(ChangeProfileActivity.this,
                                        LocaleUtil.getString(getApplicationContext(), R.string.profile) + " " +
                                                LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                LocaleUtil.getString(getApplicationContext(), R.string.message_success3),
                                        Toast.LENGTH_SHORT).show();
                                updateData();
                                finish();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(ChangeProfileActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(ChangeProfileActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendCreateUpdateAccount = null;
                    t.printStackTrace();
                    Toast.makeText(ChangeProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateData() {
        try {
            if (realm != null) {
                realm.beginTransaction();
                profileEntity.setName(Objects.requireNonNull(this.edtName.getText()).toString());
                profileEntity.setAddress(Objects.requireNonNull(this.edtName.getText()).toString());
                profileEntity.setGender(Byte.parseByte(Objects.requireNonNull(this.btnGender.getSelectedItem().getIdentifier().toString())));
                profileEntity.setPhone(Objects.requireNonNull(this.edtName.getText()).toString());
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
