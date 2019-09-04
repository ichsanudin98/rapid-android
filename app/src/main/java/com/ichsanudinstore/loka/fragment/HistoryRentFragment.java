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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.activity.CreateUpdateSeatActivity;
import com.ichsanudinstore.loka.adapter.HistoryRentAdapter;
import com.ichsanudinstore.loka.adapter.SeatAdapter;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.seat.Seat;
import com.ichsanudinstore.loka.api.endpoint.seat.readseat.ReadSeatResponse;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.RentModel;
import com.ichsanudinstore.loka.model.entity.SeatModel;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.IntentUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
public class HistoryRentFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        SeatAdapter.SeatAdapterListener, SpinnerBottomSheet.SpinnerBottomSheetCallback {
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

    private Call<ReadSeatResponse> getHistoryResponse;

    private RealmResults<RentModel> rentModels;

    private ArrayList<SpinnerAdapter.Item> optionItems;

    private boolean isOffice = false;
    private Long valueID = -1L;

    public HistoryRentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.getHistoryRent(false);
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
        if (getHistoryResponse != null) {
            getHistoryResponse.cancel();
            getHistoryResponse = null;
        }
    }

    @Override
    public void onRefresh() {
        this.getHistoryRent(true);
    }

    @Override
    public void onItemClick(int position, SeatModel data) {
        this.valueID = data.getId();
        isOffice = false;
        BottomSheetUtil.spinner((AppCompatActivity) this.getActivity(), LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.seat), optionItems, HistoryRentFragment.this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        if (isOffice) {
            // TODO Filter by office
        } else {
            Intent intent = IntentUtil.generalGoTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), CreateUpdateSeatActivity.class, null, false);
            intent.putExtra("type", (byte) selectedItem.getIdentifier());
            intent.putExtra("id", this.valueID);
            IntentUtil.goTo((AppCompatActivity) Objects.requireNonNull(this.getActivity()), intent);
        }

        this.valueID = -1L;
    }

    private void initializeUI() {
        this.layoutContent.setOnRefreshListener(this);
        this.rcvList.setItemAnimator(new DefaultItemAnimator());
        this.rcvList.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getContext()), RecyclerView.VERTICAL, false));

        List<String> option = new ArrayList<>();
        option.add(LocaleUtil.getString(getContext(), R.string.edit) + " " + LocaleUtil.getString(getContext(), R.string.seat));
        option.add(LocaleUtil.getString(getContext(), R.string.detail) + " " + LocaleUtil.getString(getContext(), R.string.seat));
        option.add(LocaleUtil.getString(getContext(), R.string.delete) + " " + LocaleUtil.getString(getContext(), R.string.seat));
        optionItems = EntityUtil.setOption(getContext(), option);
    }

    private void hint() {
        this.txvEmpty.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.empty));
    }

    private void initializeData() {
        this.rentModels = realm.where(RentModel.class).findAll();
    }

    private void showData() {
        initializeData();
        if (rentModels.size() > 0) {
            rcvList.setVisibility(View.VISIBLE);
            txvEmpty.setVisibility(View.GONE);
            this.rcvList.setAdapter(new HistoryRentAdapter(this.rentModels, true, HistoryRentFragment.this));
        } else {
            rcvList.setVisibility(View.GONE);
            txvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getHistoryRent(Boolean isRefresh) {
        if (getHistoryResponse == null) {
            if (isRefresh) {
                if (layoutContent != null)
                    layoutContent.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            RestUtil.resetTimestamp();
            getHistoryResponse = RestApi.getRent(
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
            getHistoryResponse.enqueue(new Callback<ReadSeatResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReadSeatResponse> call, @NotNull Response<ReadSeatResponse> response) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getHistoryResponse = null;
                    if (response.isSuccessful()) {
                        ReadSeatResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(HistoryRentFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(HistoryRentFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReadSeatResponse> call, @NotNull Throwable t) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getHistoryResponse = null;
                    t.printStackTrace();
                    Toast.makeText(HistoryRentFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void insertData(List<Seat> collections) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                EntityUtil.deleteWithClass(SeatModel.class, realm);
                for (Seat data : collections) {
                    RentModel rentEntity = checkingData(realm, data.getRent_id());
                    if (rentEntity == null)
                        rentEntity = realm.createObject(RentModel.class, data.getRent_id());
                    rentEntity.setSeat_name(data.getSeat_name());
//                    rentEntity.setStatus(data.getStatus());
                    rentEntity.setOffice_id(Long.valueOf(data.getOffice_id()));
                    rentEntity.setCreate_date(data.getCreate_date());
                    if (data.getNote() != null)
                        rentEntity.setNote(data.getNote());
                    if (data.getKeyphrase() != null)
                        rentEntity.setSecurity(data.getKeyphrase());
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData(SeatModel data) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                data.setStatus(!data.getStatus());
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RentModel checkingData(Realm realm, String rent_id) {
        return realm.where(RentModel.class)
                .equalTo("id", Long.parseLong(rent_id))
                .findFirst();
    }
}
