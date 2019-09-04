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
import com.ichsanudinstore.loka.model.entity.SeatModel;
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

public class CreateUpdateSeatActivity extends AppCompatActivity
        implements SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.parent_view)
    CoordinatorLayout layoutParent;
    @BindView(R.id.appbar_view)
    AppBarLayout layoutAppbar;
    @BindView(R.id.toolbar_view)
    Toolbar layoutToolbar;
    @BindView(R.id.content_view)
    NestedScrollView layoutContent;
    @BindView(R.id.name_input)
    TextInputLayout tilName;
    @BindView(R.id.name)
    TextInputEditText edtName;
    @BindView(R.id.office)
    SpinnerMaterialButton btnOffice;
    @BindView(R.id.status)
    SpinnerMaterialButton btnStatus;
    @BindView(R.id.send)
    MaterialButton btnSend;

    private Realm realm;

    private byte valueType = 0;
    private Long valueID;
    private Boolean isOffice = false;

    private Call<BaseResponse> sendCreateUpdateSeat;
    private Call<ReadOfficeResponse> getOfficeResponse;

    ArrayList<SpinnerAdapter.Item> statusItems, officeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_seat);
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
        if (StringUtil.isEmpty(Objects.requireNonNull(edtName.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateSeatActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.name),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnOffice.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    CreateUpdateSeatActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.office),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnStatus.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    CreateUpdateSeatActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.status),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        this.sendCreateUpdateSeat();
    }

    @OnClick(R.id.office)
    protected void setGender() {
        isOffice = true;
        BottomSheetUtil.spinner(CreateUpdateSeatActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.office), officeItems, this);
    }

    @OnClick(R.id.status)
    protected void setOffice() {
        isOffice = false;
        BottomSheetUtil.spinner(CreateUpdateSeatActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.status), statusItems, this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        if (isOffice) {
            this.btnOffice.setSelectedItem(selectedItem);
        } else {
            this.btnStatus.setSelectedItem(selectedItem);
        }
    }

    private void hint() {
        tilName.setHint(LocaleUtil.getString(getApplicationContext(), R.string.name));

        btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.save));

        if (valueType == 0)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.create) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.seat)
            );
        else if (valueType == 1)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.edit) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.seat)
            );
        else if (valueType == 2)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.detail) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.seat)
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
            this.edtName.setFocusable(false);
            this.btnStatus.setOnClickListener(null);
            this.btnOffice.setOnClickListener(null);
            this.btnSend.setVisibility(View.GONE);
        }
    }

    private void initializeData() {
        officeItems = new ArrayList<>();
        statusItems = EntityUtil.setStatus(getApplicationContext());

        SeatModel seatEntity = null;
        if (valueID != -1) {
            seatEntity = realm.where(SeatModel.class)
                    .equalTo("id", valueID)
                    .findFirst();

            if (seatEntity != null) {
                edtName.setText(seatEntity.getName());
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
                if (seatEntity != null) {
                    if (seatEntity.getOffice_id().equals(officeEntity.getId())) {
                        btnOffice.setSelectedItem(item);
                    }

                    if (seatEntity.getStatus())
                        btnStatus.setSelectedItem(statusItems.get(0));
                    else
                        btnStatus.setSelectedItem(statusItems.get(1));
                }
                officeItems.add(item);
            }
        }
    }

    private void sendCreateUpdateSeat() {
        if (sendCreateUpdateSeat == null) {
            RestUtil.resetTimestamp();
            sendCreateUpdateSeat = RestApi.sendCreateUpdateSeat(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    String.valueOf(valueType == 0 ? null : valueID),
                    Objects.requireNonNull(edtName.getText()).toString(),
                    btnOffice.getSelectedItem().getIdentifier().toString(),
                    Integer.parseInt(btnStatus.getSelectedItem().getIdentifier().toString()) == 0,
                    valueType
            );

            sendCreateUpdateSeat.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendCreateUpdateSeat = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                if (valueType == (byte) 0)
                                    Toast.makeText(CreateUpdateSeatActivity.this,
                                            LocaleUtil.getString(getApplicationContext(), R.string.seat) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success2),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(CreateUpdateSeatActivity.this,
                                            LocaleUtil.getString(getApplicationContext(), R.string.seat) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success3),
                                            Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(CreateUpdateSeatActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(CreateUpdateSeatActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(CreateUpdateSeatActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
