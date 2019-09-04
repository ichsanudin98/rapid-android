package com.ichsanudinstore.loka.fragment;


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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SeatAdapter;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.seat.Seat;
import com.ichsanudinstore.loka.api.endpoint.seat.readseat.ReadSeatResponse;
import com.ichsanudinstore.loka.bottomsheet.RentBottomSheet;
import com.ichsanudinstore.loka.bottomsheet.SpinnerBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.SeatModel;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
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
public class RentFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        SeatAdapter.SeatAdapterListener, SpinnerBottomSheet.SpinnerBottomSheetCallback,
        RentBottomSheet.RentBottomSheetCallback {
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

    private Call<ReadSeatResponse> getSeatResponse;
    private Call<BaseResponse> sendCreatUpdateResponse;

    private RealmResults<SeatModel> seatModels;

    private ArrayList<SpinnerAdapter.Item> optionItems;

    private SeatModel valueEntity = null;

    public RentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.getSeat(false);

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
        if (getSeatResponse != null) {
            getSeatResponse.cancel();
            getSeatResponse = null;
        }
    }

    @Override
    public void onRefresh() {
        this.getSeat(true);
    }

    @Override
    public void onItemClick(int position, SeatModel data) {
        this.valueEntity = data;
        BottomSheetUtil.spinner((AppCompatActivity) this.getActivity(), LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.seat), optionItems, RentFragment.this);
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        if (!valueEntity.getStatus()) {
            if (Byte.parseByte(selectedItem.getIdentifier().toString()) == (byte) 0) {
                Toast.makeText(
                        getContext(),
                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.seat) + " " +
                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.rent_assigned),
                        Toast.LENGTH_SHORT).show();
            } else {
                BottomSheetUtil.rentSeat((AppCompatActivity) Objects.requireNonNull(getActivity()), this, Byte.parseByte(selectedItem.getIdentifier().toString()), valueEntity);
            }
        } else {
            if (Byte.parseByte(selectedItem.getIdentifier().toString()) == (byte) 0) {
                BottomSheetUtil.rentSeat((AppCompatActivity) Objects.requireNonNull(getActivity()), this, Byte.parseByte(selectedItem.getIdentifier().toString()), valueEntity);
            } else {
                Toast.makeText(
                        getContext(),
                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.seat) + " " +
                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.rent_unassigned),
                        Toast.LENGTH_SHORT).show();
            }
        }

        this.valueEntity = null;
    }

    @Override
    public void onUpdateSeat(byte type, SeatModel data, String note, String password) {
        this.sendCreateUpdateSeat(data, note, password);
    }

    private void initializeUI() {
        this.layoutContent.setOnRefreshListener(this);
        this.rcvList.setItemAnimator(new DefaultItemAnimator());
        this.rcvList.setLayoutManager(new GridLayoutManager(Objects.requireNonNull(getContext()), 2, RecyclerView.VERTICAL, false));

        List<String> option = new ArrayList<>();
        option.add(LocaleUtil.getString(getContext(), R.string.assign) + " " + LocaleUtil.getString(getContext(), R.string.item));
        option.add(LocaleUtil.getString(getContext(), R.string.unassign) + " " + LocaleUtil.getString(getContext(), R.string.item));
        optionItems = EntityUtil.setRentOption(getContext(), option);
    }

    private void hint() {
        this.txvEmpty.setText(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.empty));
    }

    private void initializeData() {
        this.seatModels = realm.where(SeatModel.class).findAll();
    }

    private void showData() {
        initializeData();
        if (seatModels.size() > 0) {
            rcvList.setVisibility(View.VISIBLE);
            txvEmpty.setVisibility(View.GONE);
            this.rcvList.setAdapter(new SeatAdapter(this.seatModels, true, RentFragment.this, RentFragment.this));
        } else {
            rcvList.setVisibility(View.GONE);
            txvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getSeat(Boolean isRefresh) {
        if (getSeatResponse == null) {
            if (isRefresh) {
                if (layoutContent != null)
                    layoutContent.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            RestUtil.resetTimestamp();
            getSeatResponse = RestApi.getSeat(
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
            getSeatResponse.enqueue(new Callback<ReadSeatResponse>() {
                @Override
                public void onResponse(@NotNull Call<ReadSeatResponse> call, @NotNull Response<ReadSeatResponse> response) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getSeatResponse = null;
                    if (response.isSuccessful()) {
                        ReadSeatResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(RentFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(RentFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
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
                    getSeatResponse = null;
                    t.printStackTrace();
                    Toast.makeText(RentFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendCreateUpdateSeat(SeatModel data, String note, String keyphrase) {
        if (sendCreatUpdateResponse == null) {
            progressBar.setVisibility(View.VISIBLE);
            RestUtil.resetTimestamp();
            sendCreatUpdateResponse = RestApi.sendCreateUpdateRent(
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
                    data.getName(),
                    String.valueOf(data.getOffice_id()),
                    !data.getStatus(),
                    note,
                    keyphrase
            );
            sendCreatUpdateResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendCreatUpdateResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                updateData(data, note, keyphrase);
                                Objects.requireNonNull(rcvList.getAdapter()).notifyDataSetChanged();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(RentFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(RentFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendCreatUpdateResponse = null;
                    t.printStackTrace();
                    Toast.makeText(RentFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    SeatModel seatEntity = checkingData(realm, data.getSeat_id());
                    if (seatEntity == null)
                        seatEntity = realm.createObject(SeatModel.class, data.getSeat_id());
                    seatEntity.setName(data.getSeat_name());
                    seatEntity.setStatus(data.getStatus());
                    seatEntity.setOffice_id(Long.valueOf(data.getOffice_id()));
                    if (data.getNote() != null)
                        seatEntity.setNote(data.getNote());
                    if (data.getKeyphrase() != null)
                        seatEntity.setKeyphrase(data.getKeyphrase());
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData(SeatModel data, String note, String keyphrase) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                data.setStatus(!data.getStatus());
                data.setNote(note);
                data.setKeyphrase(keyphrase);
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SeatModel checkingData(Realm realm, String user_id) {
        return realm.where(SeatModel.class)
                .equalTo("id", Long.parseLong(user_id))
                .findFirst();
    }
}
