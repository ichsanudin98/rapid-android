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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.CategoryOfficeAdapter;
import com.ichsanudinstore.loka.api.RestApi;
import com.ichsanudinstore.loka.api.endpoint.BaseResponse;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.CategoryOffice;
import com.ichsanudinstore.loka.api.endpoint.categoryoffice.readcategory.ReadCategoryResponse;
import com.ichsanudinstore.loka.bottomsheet.CategoryOfficeBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.model.entity.CategoryOfficeModel;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.HashUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.RestUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;

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
public class CategoryOfficeFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        CategoryOfficeBottomSheet.CategoryOfficeBottomSheetCallback,
        CategoryOfficeAdapter.CategoryOfficeAdapterListener {
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

    private Call<ReadCategoryResponse> getCategoryResponse;
    private Call<BaseResponse> sendCreateUpdateCategoryResponse;
    private Call<BaseResponse> sendDeleteCategoryResponse;

    private RealmResults<CategoryOfficeModel> categoryOfficeModels;

    public CategoryOfficeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_office, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        this.initializeUI();
        this.hint();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        this.getCategory(false);
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
        if (getCategoryResponse != null) {
            getCategoryResponse.cancel();
            getCategoryResponse = null;
        }
    }

    @Override
    public void onRefresh() {
        this.getCategory(true);
    }

    @Override
    public void onItemTriggeredClick(byte type, int position, CategoryOfficeModel data) {
        if (type != 3)
            BottomSheetUtil.categoryOffice((AppCompatActivity) Objects.requireNonNull(getActivity()), this, type, data);
        else
            sendDeleteCategory(data);
    }

    @Override
    public void onUpdateCategoryOffice(CategoryOfficeModel data, String name) {
        sendCreateUpdateCategory(data, name);
    }

    public void addCategoryOffice() {
        BottomSheetUtil.categoryOffice((AppCompatActivity) Objects.requireNonNull(getActivity()), this, (byte) 0, null);
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
        this.categoryOfficeModels = realm.where(CategoryOfficeModel.class).findAll();
    }

    private void showData() {
        initializeData();
        if (categoryOfficeModels.size() > 0) {
            rcvList.setVisibility(View.VISIBLE);
            txvEmpty.setVisibility(View.GONE);
            this.rcvList.setAdapter(new CategoryOfficeAdapter(this.categoryOfficeModels, true, CategoryOfficeFragment.this, CategoryOfficeFragment.this));
        } else {
            rcvList.setVisibility(View.GONE);
            txvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void getCategory(Boolean isRefresh) {
        if (getCategoryResponse == null) {
            if (isRefresh) {
                if (layoutContent != null)
                    layoutContent.setRefreshing(true);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
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
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getCategoryResponse = null;
                    if (response.isSuccessful()) {
                        ReadCategoryResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                insertData(bodyResponse.getList());
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(CategoryOfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(CategoryOfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ReadCategoryResponse> call, @NotNull Throwable t) {
                    if (isRefresh) {
                        if (layoutContent != null)
                            layoutContent.setRefreshing(false);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    getCategoryResponse = null;
                    t.printStackTrace();
                    Toast.makeText(CategoryOfficeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateData(CategoryOfficeModel data, String name) {
        try {
            if (realm != null) {
                realm.beginTransaction();
                if (name != null) {
                    data.setName(name);
                } else {
                    data.deleteFromRealm();
                }
                realm.commitTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void sendCreateUpdateCategory(CategoryOfficeModel data, String name) {
        if (sendCreateUpdateCategoryResponse == null) {
            byte type = data != null ? (byte) 1 : (byte) 0;
            RestUtil.resetTimestamp();
            sendCreateUpdateCategoryResponse = RestApi.sendCreateUpdateCategory(
                    RestUtil.generateTimestamp(),
                    RestUtil.generateSecurityCode(
                            HashUtil.SHA256(
                                    Constant.Application.SALT +
                                            SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class) +
                                            RestUtil.generateTimestamp()
                            )
                    ),
                    SharedPreferencesUtil.get(Constant.SharedPreferenceKey.SESSION_ID, String.class),
                    data != null ? String.valueOf(data.getId()) : null,
                    name,
                    type
            );
            sendCreateUpdateCategoryResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    sendCreateUpdateCategoryResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                if (type == 1)
                                    updateData(data, name);
                                else
                                    getCategory(true);

                                if (type == (byte) 0)
                                    Toast.makeText(getActivity(),
                                            LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.category) + " " +
                                                    LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success2),
                                            Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getActivity(),
                                            LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.category) + " " +
                                                    LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                    LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success3),
                                            Toast.LENGTH_SHORT).show();

                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(CategoryOfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(CategoryOfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    sendCreateUpdateCategoryResponse = null;
                    t.printStackTrace();
                    Toast.makeText(CategoryOfficeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendDeleteCategory(CategoryOfficeModel data) {
        if (sendDeleteCategoryResponse == null) {
            progressBar.setVisibility(View.VISIBLE);
            RestUtil.resetTimestamp();
            sendDeleteCategoryResponse = RestApi.sendDeleteCategory(
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
            sendDeleteCategoryResponse.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendDeleteCategoryResponse = null;
                    if (response.isSuccessful()) {
                        BaseResponse bodyResponse = response.body();
                        if (bodyResponse != null) {
                            if (bodyResponse.getResponseCode() == 0) {
                                Toast.makeText(getActivity(),
                                        LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.category) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success1) + " " +
                                                LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.message_success4),
                                        Toast.LENGTH_SHORT).show();
                                updateData(data, null);
                                showData();
                            } else if (bodyResponse.getResponseCode() == 1) {
                                Toast.makeText(CategoryOfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            } else if (bodyResponse.getResponseCode() == 2) {
                                Toast.makeText(CategoryOfficeFragment.this.getActivity(), bodyResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    sendDeleteCategoryResponse = null;
                    t.printStackTrace();
                    Toast.makeText(CategoryOfficeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
