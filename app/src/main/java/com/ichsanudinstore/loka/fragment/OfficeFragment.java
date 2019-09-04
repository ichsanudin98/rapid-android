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
import com.ichsanudinstore.loka.activity.CreateUpdateOfficeActivity;
import com.ichsanudinstore.loka.adapter.OfficeAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.office.Office;
import com.ichsanudinstore.loka.api.endpoint.office.readoffice.ReadOfficeResponse;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.OfficeModel;
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
public class OfficeFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        OfficeAdapter.OfficeAdapterListener {
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

    private Call<ReadOfficeResponse> getOfficeResponse;
    private Call<BaseResponse> sendDeleteOfficeResponse;

    private RealmResults<OfficeModel> officeModels;

    public OfficeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_office, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.getOffice(false);
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
        if (getOfficeResponse != null) {
            getOfficeResponse.cancel();
            getOfficeResponse = null;
        }
    }

    @Override
    public void onRefresh() {
        this.getOffice(true);
    }

    @Override
    public void onItemTriggeredClick(byte type, int position, OfficeModel data) {
        if (type != 3) {
            Intent intent = IntentUtil.generalGoTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), CreateUpdateOfficeActivity.class, null, false);
            intent.putExtra("type", type);
            intent.putExtra("id", data.getId());
            IntentUtil.goTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), intent);
        } else {
            sendDeleteOfficeResponse(data);
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
        this.officeModels = realm.where(OfficeModel.class).findAll();
    }

    private void showData() {
        initializeData();
        if (officeModels.size() > 0) {
            rcvList.setVisibility(View.VISIBLE);
            txvEmpty.setVisibility(View.GONE);
            this.rcvList.setAdapter(new OfficeAdapter(this.officeModels, true, OfficeFragment.this, OfficeFragment.this));
        } else {
            rcvList.setVisibility(View.GONE);
            txvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getOffice(Boolean isRefresh) {
        if (getOfficeResponse == null) {
            if (isRefresh) {
                if (layoutContent != null)
                    layoutContent.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
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
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getOfficeResponse = null;
                    if (response.isSuccessful()) {
                        ReadOfficeResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(OfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(OfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReadOfficeResponse> call, @NotNull Throwable t) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getOfficeResponse = null;
                    t.printStackTrace();
                    Toast.makeText(OfficeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateData(OfficeModel data, Long id) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                if (id != null) {
                    // TODO Update data
                } else {
                    data.deleteFromRealm();
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void sendDeleteOfficeResponse(OfficeModel data) {
        if (sendDeleteOfficeResponse == null) {
            RestUtil.resetTimestamp();
            sendDeleteOfficeResponse = RestApi.sendDeleteCategory(
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
            sendDeleteOfficeResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendDeleteOfficeResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(getActivity(),
                                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.office) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success4),
                                        Toast.LENGTH_SHORT).show();
                                updateData(data, null);
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(OfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(OfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendDeleteOfficeResponse = null;
                    t.printStackTrace();
                    Toast.makeText(OfficeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
