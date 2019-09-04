package com.ichsanudinstore.loka.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.ApprovalAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.profile.Profile;
import com.ichsanudinstore.loka.api.endpoint.profile.readprofile.ReadProfileResponse;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.ProfileModel;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
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
public class ApprovalFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ApprovalAdapter.ApprovalAdapterListener {
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
    private Call<BaseResponse> sendUpdateProfileActivation;

    private RealmResults<ProfileModel> profileModels;

    public ApprovalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.getApproval(false);
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
        this.getApproval(true);
    }

    @Override
    public void onItemClick(int position, ProfileModel data) {

    }

    @Override
    public void onItemActivationClick(int position, ProfileModel data) {
        this.sendUpdateProfileActivation(data);
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
            this.rcvList.setAdapter(new ApprovalAdapter(this.profileModels, true, ApprovalFragment.this, ApprovalFragment.this));
        } else {
            rcvList.setVisibility(View.GONE);
            txvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getApproval(Boolean isRefresh) {
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
                    null,
                    null,
                    (byte) 2);
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
                                Toast.makeText(ApprovalFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(ApprovalFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ApprovalFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateData(ProfileModel data) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                data.setF_activated(!data.getF_activated());
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

    private void sendUpdateProfileActivation(ProfileModel data) {
        if (sendUpdateProfileActivation == null) {
            RestUtil.resetTimestamp();
            sendUpdateProfileActivation = RestApi.sendActivationProfile(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    String.valueOf(data.getId()),
                    !data.getF_activated()
            );
            sendUpdateProfileActivation.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendUpdateProfileActivation = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(getActivity(),
                                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.profile) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success3),
                                        Toast.LENGTH_SHORT).show();
                                updateData(data);
                                Objects.requireNonNull(rcvList.getAdapter()).notifyDataSetChanged();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(ApprovalFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(ApprovalFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendUpdateProfileActivation = null;
                    t.printStackTrace();
                    Toast.makeText(ApprovalFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
