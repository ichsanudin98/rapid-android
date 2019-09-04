package com.ichsanudinstore.loka.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.activity.CreateUpdateAccountActivity;
import com.ichsanudinstore.loka.adapter.KeeperAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.Profile;
import com.ichsanudinstore.loka.api.endpoint.profile.readprofile.ReadProfileResponse;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.ProfileModel;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.IntentUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;
import com.ichsanudinstore.loka.util.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeeperFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, KeeperAdapter.KeeperAdapterListener {
    @BindView(R.id.parent_view)
    CoordinatorLayout layoutParent;
    @BindView(R.id.content_view)
    SwipeRefreshLayout layoutContent;
    @BindView(R.id.empty)
    AppCompatTextView txvEmpty;
    @BindView(R.id.list)
    RecyclerView rcvList;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private Unbinder unbinder;

    private Realm realm;

    private Call<ReadProfileResponse> getProfileResponse;
    private Call<BaseResponse> sendDeleteProfileResponse;

    private RealmResults<ProfileModel> profileModels;

    public KeeperFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keeper, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.getKeeper(false);
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
    public void onRefresh() {
        this.getKeeper(true);
    }

    @Override
    public void onItemTriggeredClick(byte type, int position, ProfileModel data) {
        if (type != 3) {
            Intent intent = IntentUtil.generalGoTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), CreateUpdateAccountActivity.class, null, false);
            intent.putExtra("type", type);
            intent.putExtra("id", data.getId());
            IntentUtil.goTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), intent);
        } else {
            sendDeleteProfile(data);
        }
    }

    private void initializeUI() {
        this.layoutContent.setOnRefreshListener(this);
        this.rcvList.setItemAnimator(new DefaultItemAnimator());
        this.rcvList.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));
        this.rcvList.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getContext()), RecyclerView.VERTICAL, false));
    }

    private void hint() {
        this.txvEmpty.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.empty));
    }

    private void initializeData() {
        this.profileModels = realm.where(ProfileModel.class)
                .notEqualTo("id", Long.parseLong(SharedPreferencesUtil.get(Constant.SharedPreferenceKey.USER_ID, String.class)))
                .findAll();
    }

    private void showData() {
        initializeData();
        if (profileModels.size() > 0) {
            rcvList.setVisibility(View.VISIBLE);
            txvEmpty.setVisibility(View.GONE);
            this.rcvList.setAdapter(new KeeperAdapter(this.profileModels, true, KeeperFragment.this, KeeperFragment.this));
        } else {
            rcvList.setVisibility(View.GONE);
            txvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getKeeper(Boolean isRefresh) {
        if (getProfileResponse == null) {
            if (isRefresh) {
                if (layoutContent != null)
                    layoutContent.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
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
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.COMPANY_ID, String.class),
                    null,
                    (byte) 1);
            getProfileResponse.enqueue(new Callback<ReadProfileResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReadProfileResponse> call, @NotNull Response<ReadProfileResponse> response) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getProfileResponse = null;
                    if (response.isSuccessful()) {
                        ReadProfileResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(KeeperFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(KeeperFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReadProfileResponse> call, @NotNull Throwable t) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getProfileResponse = null;
                    t.printStackTrace();
                    Toast.makeText(KeeperFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    profileEntity.setPhone(data.getPhone());
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

    private void updateData(ProfileModel data) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                data.deleteFromRealm();
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

    private void sendDeleteProfile(ProfileModel data) {
        if (sendDeleteProfileResponse == null) {
            RestUtil.resetTimestamp();
            sendDeleteProfileResponse = RestApi.sendDeleteProfile(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    String.valueOf(data.getId())
            );
            sendDeleteProfileResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendDeleteProfileResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(getActivity(),
                                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.profile) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success4),
                                        Toast.LENGTH_SHORT).show();
                                updateData(data);
                                Objects.requireNonNull(rcvList.getAdapter()).notifyDataSetChanged();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(KeeperFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(KeeperFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendDeleteProfileResponse = null;
                    t.printStackTrace();
                    Toast.makeText(KeeperFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
