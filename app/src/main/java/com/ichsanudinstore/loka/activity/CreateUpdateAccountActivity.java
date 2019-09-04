package com.ichsanudinstore.loka.activity;

import android.os.Bundle;
import android.view.View;
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
import com.ichsanudinstore.loka.api.endpoint.office.Office;
import com.ichsanudinstore.loka.api.endpoint.office.readoffice.ReadOfficeResponse;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.OfficeModel;
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
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUpdateAccountActivity extends AppCompatActivity
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
    @BindView(R.id.gender)
    SpinnerMaterialButton btnGender;
    @BindView(R.id.office)
    SpinnerMaterialButton btnOffice;
    @BindView(R.id.send)
    MaterialButton btnSend;

    private Realm realm;

    private byte valueType = 0;
    private Long valueID;
    private Boolean isGender = false;

    private Call<BaseResponse> sendCreateUpdateAccount;
    private Call<ReadOfficeResponse> getOfficeResponse;

    ArrayList<SpinnerAdapter.Item> genderItems, officeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_account);
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
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.email),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPassword.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.password),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtName.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.name),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtAddress.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.address),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtPhone.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.phone),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnGender.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.gender),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnOffice.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    CreateUpdateAccountActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.office),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        this.sendCreateUpdateProfile();
    }

    @OnClick(R.id.gender)
    protected void setGender() {
        isGender = true;
        BottomSheetUtil.spinner(CreateUpdateAccountActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.gender), genderItems, this);
    }

    @OnClick(R.id.office)
    protected void setOffice() {
        isGender = false;
        BottomSheetUtil.spinner(CreateUpdateAccountActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.office), officeItems, this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        if (isGender) {
            this.btnGender.setSelectedItem(selectedItem);
        } else {
            this.btnOffice.setSelectedItem(selectedItem);
        }
    }

    private void hint() {
        tilAddress.setHint(LocaleUtil.getString(getApplicationContext(), R.string.address));
        tilMail.setHint(LocaleUtil.getString(getApplicationContext(), R.string.email));
        tilName.setHint(LocaleUtil.getString(getApplicationContext(), R.string.name));
        tilPassword.setHint(LocaleUtil.getString(getApplicationContext(), R.string.password));
        tilPhone.setHint(LocaleUtil.getString(getApplicationContext(), R.string.phone));

        btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.save));

        if (valueType == 0)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.create) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.profile)
            );
        else if (valueType == 1)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.edit) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.profile)
            );
        else if (valueType == 2)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.detail) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.profile)
            );
    }

    private void initializeUI() {
        this.realm = Realm.getDefaultInstance();

        this.valueType = getIntent().getByteExtra("type", (byte) 0);
        this.valueID = getIntent().getLongExtra("id", -1L);

        this.setSupportActionBar(layoutToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.layoutToolbar.setNavigationOnClickListener(view -> onBackPressed());

        if (valueType == 2) {
            this.edtPhone.setFocusable(false);
            this.edtAddress.setFocusable(false);
            this.edtName.setFocusable(false);
            this.edtMail.setFocusable(false);
            this.edtPassword.setFocusable(false);
            this.tilPassword.setVisibility(View.GONE);

            this.btnOffice.setOnClickListener(null);
            this.btnGender.setOnClickListener(null);
            this.btnSend.setVisibility(View.GONE);
        }
    }

    private void initializeData() {
        genderItems = new ArrayList<>();
        genderItems = EntityUtil.setGender(getApplicationContext());

        ProfileModel profileEntity = null;
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
//                            edtGender.setText(item.getDescription());
                            btnGender.setSelectedItem(item);
                        }
                    }
                }
            }
        }

        RealmResults<OfficeModel> officeEntities = realm.where(OfficeModel.class)
                .findAll();

        if (officeEntities.size() == 0) {
            this.getOffice();
        } else {
            officeItems = new ArrayList<>();

            for (OfficeModel officeEntity : officeEntities) {
                SpinnerAdapter.Item item = new SpinnerAdapter.Item();
                item.setIdentifier(officeEntity.getId());
                item.setDescription(officeEntity.getName());
                if (profileEntity != null) {
                    if (profileEntity.getOffice_id().equals(officeEntity.getId())) {
                        btnOffice.setSelectedItem(item);
                    }
                }
                officeItems.add(item);
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
                    String.valueOf(valueType == 0 ? null : valueID),
                    Objects.requireNonNull(edtMail.getText()).toString(),
                    HashUtil.MD5(Objects.requireNonNull(edtMail.getText()).toString() +
                            Objects.requireNonNull(edtPassword.getText()).toString()),
                    Objects.requireNonNull(edtName.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtAddress.getText()).toString(),
                    Byte.parseByte(btnGender.getSelectedItem().getIdentifier().toString()),
                    Objects.requireNonNull(edtPhone.getText()).toString(),
                    btnOffice.getSelectedItem().getIdentifier().toString(),
                    null,
                    null,
                    null,
                    valueType
            );

            sendCreateUpdateAccount.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendCreateUpdateAccount = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                if (valueType == (byte) 0)
                                    Toast.makeText(CreateUpdateAccountActivity.this,
                                            LocaleUtil.getString(getApplicationContext(), R.string.profile) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success2),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(CreateUpdateAccountActivity.this,
                                            LocaleUtil.getString(getApplicationContext(), R.string.profile) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success3),
                                            Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(CreateUpdateAccountActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(CreateUpdateAccountActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendCreateUpdateAccount = null;
                    t.printStackTrace();
                    Toast.makeText(CreateUpdateAccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getOffice() {
        if (getOfficeResponse == null) {
            RestUtil.resetTimestamp();
            getOfficeResponse = RestApi.getOffice(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class)
            );
            getOfficeResponse.enqueue(new Callback<ReadOfficeResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReadOfficeResponse> call, @NotNull Response<ReadOfficeResponse> response) {
                    getOfficeResponse = null;
                    if (response.isSuccessful()) {
                        ReadOfficeResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                initializeData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(getApplicationContext(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(getApplicationContext(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReadOfficeResponse> call, @NotNull Throwable t) {
                    getOfficeResponse = null;
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void insertData(List<Office> collections) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                EntityUtil.deleteWithClass(OfficeModel.class, realm);
                for (Office data : collections) {
                    OfficeModel officeEntity = checkingData(realm, data.getOffice_id());
                    if (officeEntity == null)
                        officeEntity = realm.createObject(OfficeModel.class, data.getOffice_id());
                    officeEntity.setName(data.getOffice_name());
                    if (!StringUtil.isEmpty(data.getOffice_image()))
                        officeEntity.setImage(data.getOffice_image());
                    officeEntity.setAddress(data.getOffice_address());
                    officeEntity.setPhone(data.getOffice_phone());
                    if (!StringUtil.isEmpty(data.getOffice_latitude()))
                        officeEntity.setLatitude(data.getOffice_latitude());
                    if (!StringUtil.isEmpty(data.getOffice_longitude()))
                        officeEntity.setLongitude(data.getOffice_longitude());
                    if (!StringUtil.isEmpty(data.getTotal_seat()))
                        officeEntity.setTotal_seat(data.getTotal_seat());
                    officeEntity.setCategory_id(Long.parseLong(data.getCategory_id()));
                    officeEntity.setCategory_name(data.getCategory_name());
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OfficeModel checkingData(Realm realm, String office_id) {
        return realm.where(OfficeModel.class)
                .equalTo("id", Long.parseLong(office_id))
                .findFirst();
    }
}
