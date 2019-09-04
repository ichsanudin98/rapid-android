package com.ichsanudinstore.loka.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.activity.ChangeProfileActivity;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.Profile;
import com.ichsanudinstore.loka.api.endpoint.profile.readprofile.ReadProfileResponse;
import com.ichsanudinstore.loka.bottomsheet.ChangePasswordBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.ProfileModel;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.IntentUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment
        implements ChangePasswordBottomSheet.ChangePasswordBottomSheetCallback {
    @BindView(R.id.parent_view)
    NestedScrollView layoutParent;
    @BindView(R.id.content_view)
    LinearLayoutCompat layoutContent;
    @BindView(R.id.profile_picture)
    CircleImageView civPicture;
    @BindView(R.id.name)
    TextInputEditText edtName;
    @BindView(R.id.name_input)
    TextInputLayout tilName;
    @BindView(R.id.mail)
    TextInputEditText edtMail;
    @BindView(R.id.mail_input)
    TextInputLayout tilMail;
    @BindView(R.id.address)
    TextInputEditText edtAddress;
    @BindView(R.id.address_input)
    TextInputLayout tilAddress;
    @BindView(R.id.phone)
    TextInputEditText edtPhone;
    @BindView(R.id.phone_input)
    TextInputLayout tilPhone;
    @BindView(R.id.gender)
    TextInputEditText edtGender;
    @BindView(R.id.gender_input)
    TextInputLayout tilGender;


    private Unbinder unbinder;

    private Realm realm;

    private Menu menuOption;

    private Call<ReadProfileResponse> getProfileResponse;
    private Call<BaseResponse> sendCreateUpdateAccount;

    private RealmResults<ProfileModel> profileModels;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.initializeData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.unbinder != null)
            this.unbinder.unbind();
        if (getProfileResponse != null) {
            getProfileResponse.cancel();
            getProfileResponse = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_profile_option_menu, menu);
        this.menuOption = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            Intent intent = IntentUtil.generalGoTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), ChangeProfileActivity.class, null, false);
            intent.putExtra("id", Objects.requireNonNull(profileModels.get(0)).getId());
            IntentUtil.goTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), intent);
            return true;
        }

        if (id == R.id.password) {
            BottomSheetUtil.changePassword((AppCompatActivity) Objects.requireNonNull(this.getActivity()), this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChangePassword(String oldPassword, String newPassword) {
        this.sendUpdatePassword(oldPassword, newPassword);
    }

    private void initializeUI() {
        this.edtGender.setVisibility(View.VISIBLE);
        this.edtPhone.setFocusable(false);
        this.edtAddress.setFocusable(false);
        this.edtMail.setFocusable(false);
        this.edtName.setFocusable(false);
        this.edtGender.setFocusable(false);

        if (this.menuOption != null) {
            this.menuOption.findItem(R.id.back).setVisible(false);
            this.menuOption.findItem(R.id.send).setVisible(false);
        }
    }

    private void hint() {
        this.tilName.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.name));
        this.tilMail.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.email));
        this.tilAddress.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.address));
        this.tilPhone.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.phone));
        this.tilGender.setHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.gender));
    }

    private void initializeData() {
        this.profileModels = realm.where(ProfileModel.class)
                .equalTo("id", Long.parseLong(SharedPreferencesUtil.get(Constant.SharedPreferenceKey.USER_ID, String.class)))
                .findAll();

        if (profileModels.size() > 0) {
            if (Objects.requireNonNull(profileModels.get(0)).getImage() == null)
                civPicture.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_user_primary_24dp));
            edtName.setText(Objects.requireNonNull(profileModels.get(0)).getName());
            if (Objects.requireNonNull(profileModels.get(0)).getGender() != null) {
                ArrayList<SpinnerAdapter.Item> genderItems = EntityUtil.setGender(getContext());
                for (SpinnerAdapter.Item gender : genderItems) {
                    if (Objects.requireNonNull(profileModels.get(0)).getGender() == Byte.parseByte(String.valueOf(gender.getIdentifier())))
                        edtGender.setText(gender.getDescription());
                }
            }
            if (Objects.requireNonNull(profileModels.get(0)).getEmail() != null)
                edtMail.setText(Objects.requireNonNull(profileModels.get(0)).getEmail());
            if (Objects.requireNonNull(profileModels.get(0)).getAddress() != null)
                edtAddress.setText(Objects.requireNonNull(profileModels.get(0)).getAddress());
            if (Objects.requireNonNull(profileModels.get(0)).getPhone() != null)
                edtPhone.setText(Objects.requireNonNull(profileModels.get(0)).getPhone());
            if (Objects.requireNonNull(profileModels.get(0)).getOffice_id() != null) {
                // TODO office
            }
        } else {
            this.getProfile();
        }
    }

    private void getProfile() {
        if (getProfileResponse == null) {
            RestUtil.resetTimestamp();
            getProfileResponse = RestApi.getProfile(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    null,
                    null,
                    (byte) 0);
            getProfileResponse.enqueue(new Callback<ReadProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReadProfileResponse> call, @NotNull Response<ReadProfileResponse> response) {
                    getProfileResponse = null;
                    if (response.isSuccessful()) {
                        ReadProfileResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                initializeData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(ProfileFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(ProfileFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReadProfileResponse> call, @NotNull Throwable t) {
                    getProfileResponse = null;
                    t.printStackTrace();
                    Toast.makeText(ProfileFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void insertData(List<Profile> collections) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                EntityUtil.deleteWithClass(ProfileModel.class, realm);
                for (Profile data : collections) {
                    ProfileModel profileEntity = checkingData(realm, data.getUser_id());
                    if (profileEntity == null)
                        profileEntity = realm.createObject(ProfileModel.class, data.getUser_id());
                    profileEntity.setEmail(data.getEmail());
                    profileEntity.setName(data.getName());
                    profileEntity.setAddress(data.getAddress());
                    profileEntity.setPhone(data.getAddress());
                    profileEntity.setGender(data.getGender());
                    if (!StringUtil.isEmpty(data.getOffice_id()))
                        profileEntity.setOffice_id(Long.parseLong(data.getOffice_id()));
                    profileEntity.setF_activated(data.getActivated());
                    if (!StringUtil.isEmpty(data.getCompany_id()))
                        profileEntity.setCompany_id(Long.parseLong(data.getCompany_id()));
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ProfileModel checkingData(Realm realm, String user_id) {
        return realm.where(ProfileModel.class)
                .equalTo("id", Long.parseLong(user_id))
                .findFirst();
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
                    null,
                    null,
                    Objects.requireNonNull(edtMail.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtName.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtAddress.getText()).toString(),
                    null,
                    Objects.requireNonNull(edtPhone.getText()).toString(),
                    null,
                    null,
                    null,
                    null,
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
                                if (menuOption != null) {
                                    menuOption.findItem(R.id.edit).setVisible(true);
                                    menuOption.findItem(R.id.back).setVisible(false);
                                    menuOption.findItem(R.id.send).setVisible(false);
                                }

                                edtGender.setVisibility(View.VISIBLE);
                                edtName.requestFocus();
                                edtPhone.setFocusable(false);
                                edtGender.setFocusable(false);
                                edtAddress.setFocusable(false);
                                edtMail.setFocusable(false);
                                edtName.setFocusable(false);
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(ProfileFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(ProfileFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendCreateUpdateAccount = null;
                    t.printStackTrace();
                    Toast.makeText(ProfileFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUpdatePassword(String oldPassword, String newPassword) {
        if (sendCreateUpdateAccount == null) {
            RestUtil.resetTimestamp();
            sendCreateUpdateAccount = RestApi.sendUpdatePassword(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    Objects.requireNonNull(profileModels.get(0)).getPhone(),
                    HashUtil.MD5(
                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.EMAIL, String.class) +
                                    oldPassword
                    ),
                    HashUtil.MD5(
                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.EMAIL, String.class) +
                                    newPassword
                    )
            );

            sendCreateUpdateAccount.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendCreateUpdateAccount = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(Objects.requireNonNull(getContext()),
                                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.password) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success3),
                                        Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(ProfileFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(ProfileFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendCreateUpdateAccount = null;
                    t.printStackTrace();
                    Toast.makeText(ProfileFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
