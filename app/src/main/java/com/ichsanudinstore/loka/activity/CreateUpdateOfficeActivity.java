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
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.CategoryOffice;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.readcategory.ReadCategoryResponse;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.CategoryOfficeModel;
import com.ichsanudinstore.loka.model.entity.OfficeModel;
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

public class CreateUpdateOfficeActivity extends AppCompatActivity
        implements SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.parent_view)
    CoordinatorLayout layoutParent;
    @BindView(R.id.appbar_view)
    AppBarLayout layoutAppbar;
    @BindView(R.id.toolbar_view)
    Toolbar layoutToolbar;
    @BindView(R.id.content_view)
    NestedScrollView layoutContent;
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
    @BindView(R.id.business_category)
    SpinnerMaterialButton btnCategory;
    @BindView(R.id.send)
    MaterialButton btnSend;

    private Realm realm;

    private byte valueType = 0;
    private Long valueID;

    Call<BaseResponse> createUpdateOfficeResponse;
    Call<ReadCategoryResponse> getCategoryResponse;

    ArrayList<SpinnerAdapter.Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_office);
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
        if (StringUtil.isEmpty(Objects.requireNonNull(edtBusinessName.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateOfficeActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.business_name),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtBusinessAddress.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateOfficeActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.business_address),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (StringUtil.isEmpty(Objects.requireNonNull(edtBusinessPhone.getText()).toString())) {
            BottomSheetUtil.message(
                    CreateUpdateOfficeActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.business_phone),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        if (btnCategory.getSelectedItem() == null) {
            BottomSheetUtil.message(
                    CreateUpdateOfficeActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.category),
                    LocaleUtil.getString(getApplicationContext(), R.string.got_it),
                    true
            );
            return;
        }

        this.sendCreateUpdateProfile();
    }

    @OnClick(R.id.business_category)
    protected void setCategory() {
        BottomSheetUtil.spinner(CreateUpdateOfficeActivity.this, LocaleUtil.getString(getApplicationContext(), R.string.category), items, this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        this.btnCategory.setSelectedItem(selectedItem);
    }

    private void sendCreateUpdateProfile() {
        if (createUpdateOfficeResponse == null) {
            RestUtil.resetTimestamp();
            createUpdateOfficeResponse = RestApi.sendCreateUpdateOffice(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    valueType == 0 ? null : String.valueOf(valueID),
                    Objects.requireNonNull(edtBusinessName.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtBusinessAddress.getText()).toString(),
                    Objects.requireNonNull(edtBusinessPhone.getText()).toString(),
                    null,
                    null,
                    null,
                    String.valueOf(btnCategory.getSelectedItem().getIdentifier()),
                    valueType
            );

            createUpdateOfficeResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    createUpdateOfficeResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                if (valueType == (byte) 0)
                                    Toast.makeText(CreateUpdateOfficeActivity.this,
                                            LocaleUtil.getString(getApplicationContext(), R.string.office) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success2),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(CreateUpdateOfficeActivity.this,
                                            LocaleUtil.getString(getApplicationContext(), R.string.office) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(getApplicationContext(), R.string.message_success3),
                                            Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(CreateUpdateOfficeActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(CreateUpdateOfficeActivity.this, bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    createUpdateOfficeResponse = null;
                    t.printStackTrace();
                    Toast.makeText(CreateUpdateOfficeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void hint() {
        tilBusinessName.setHint(LocaleUtil.getString(getApplicationContext(), R.string.business_name));
        tilBusinessAddress.setHint(LocaleUtil.getString(getApplicationContext(), R.string.business_address));
        tilBusinessPhone.setHint(LocaleUtil.getString(getApplicationContext(), R.string.business_phone));

        btnSend.setText(LocaleUtil.getString(getApplicationContext(), R.string.send));

        if (valueType == 0)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.create) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.office)
            );
        else if (valueType == 1)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.edit) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.office)
            );
        else if (valueType == 2)
            Objects.requireNonNull(this.getSupportActionBar()).setTitle(
                    LocaleUtil.getString(getApplicationContext(), R.string.delete) + " " +
                            LocaleUtil.getString(getApplicationContext(), R.string.office)
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
            this.edtBusinessPhone.setFocusable(false);
            this.edtBusinessAddress.setFocusable(false);
            this.edtBusinessName.setFocusable(false);
            this.btnCategory.setOnClickListener(null);

            this.btnSend.setVisibility(View.GONE);
        }
    }

    private void initializeData() {
        OfficeModel officeEntity = null;
        if (valueID != -1) {
            officeEntity = realm.where(OfficeModel.class)
                    .equalTo("id", valueID)
                    .findFirst();

            if (officeEntity != null) {
                edtBusinessName.setText(officeEntity.getName());
                edtBusinessAddress.setText(officeEntity.getAddress());
                edtBusinessPhone.setText(officeEntity.getPhone());
            }
        }

        RealmResults<CategoryOfficeModel> categoryOfficeEntities = realm.where(CategoryOfficeModel.class)
                .findAll();

        if (categoryOfficeEntities.size() == 0) {
            this.getCategory();
        } else {
            items = new ArrayList<>();

            for (CategoryOfficeModel categoryOfficeEntity : categoryOfficeEntities) {
                SpinnerAdapter.Item item = new SpinnerAdapter.Item();
                item.setIdentifier(categoryOfficeEntity.getId());
                item.setDescription(categoryOfficeEntity.getName());
                if (officeEntity != null) {
                    if (officeEntity.getCategory_id().equals(categoryOfficeEntity.getId())) {
                        btnCategory.setSelectedItem(item);
                    }
                }
                items.add(item);
            }
        }
    }

    private void getCategory() {
        if (getCategoryResponse == null) {
            RestUtil.resetTimestamp();
            getCategoryResponse = RestApi.getCategory(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.COMPANY_ID, String.class)
            );
            getCategoryResponse.enqueue(new Callback<ReadCategoryResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReadCategoryResponse> call, @NotNull Response<ReadCategoryResponse> response) {
                    getCategoryResponse = null;
                    if (response.isSuccessful()) {
                        ReadCategoryResponse bodyResponse = response.body();
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
                public void onFailure(@NotNull Call<ReadCategoryResponse> call, @NotNull Throwable t) {
                    getCategoryResponse = null;
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void insertData(List<CategoryOffice> collections) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                EntityUtil.deleteWithClass(CategoryOfficeModel.class, realm);
                for (CategoryOffice data : collections) {
                    CategoryOfficeModel categoryOfficeEntity = checkingData(realm, data.getCategory_id());
                    if (categoryOfficeEntity == null)
                        categoryOfficeEntity = realm.createObject(CategoryOfficeModel.class, data.getCategory_id());
                    categoryOfficeEntity.setName(data.getCategory_name());
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CategoryOfficeModel checkingData(Realm realm, String category_id) {
        return realm.where(CategoryOfficeModel.class)
                .equalTo("id", Long.parseLong(category_id))
                .findFirst();
    }
}
