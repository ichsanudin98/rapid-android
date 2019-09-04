package com.ichsanudinstore.loka.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.adapter.SpinnerAdapter;
import com.ichsanudinstore.loka.util.KeyboardUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SpinnerBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.parent_view)
    LinearLayoutCompat linearLayout;
    @BindView(R.id.title_view)
    LinearLayoutCompat layoutTitle;
    @BindView(R.id.title)
    AppCompatTextView txvTitle;
    @BindView(R.id.search_view)
    LinearLayoutCompat layoutSearch;
    @BindView(R.id.search)
    SearchView searchView;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    LinearLayoutCompat layoutEmpty;
    @BindView(R.id.empty)
    AppCompatTextView txvEmpty;

    private AppCompatActivity appCompatActivity;
    private Unbinder unbinder;
    public SpinnerBottomSheetCallback spinnerBottomSheetCallback;
    private CountDownTimer countDownTimer;
    private String searchTerm;
    private ArrayList<SpinnerAdapter.Item> allItems;

    {
        this.allItems = new ArrayList<>();
    }

    public static SpinnerBottomSheet newInstance(
            CharSequence title,
            ArrayList<SpinnerAdapter.Item> items
    ) {
        SpinnerBottomSheet spinnerBottomSheet = new SpinnerBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putCharSequence("TITLE", title);
        bundle.putSerializable("ITEMS", items);

        spinnerBottomSheet.setArguments(bundle);

        return spinnerBottomSheet;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.appCompatActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_spinner, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.init();
        this.extract();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.spinnerBottomSheetCallback == null) {
            this.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (this.spinnerBottomSheetCallback != null) {
            this.spinnerBottomSheetCallback.dismiss();
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        if (this.spinnerBottomSheetCallback != null) {
            this.spinnerBottomSheetCallback.dismiss();
        }
    }

    @OnClick(R.id.btnSearch)
    public void search() {
        layoutSearch.setVisibility(View.VISIBLE);
        layoutTitle.setVisibility(View.GONE);
    }

    @OnClick(R.id.back)
    public void back() {
        layoutTitle.setVisibility(View.VISIBLE);
        layoutSearch.setVisibility(View.GONE);
    }

    public void callback(SpinnerBottomSheetCallback spinnerBottomSheetCallback) {
        this.spinnerBottomSheetCallback = spinnerBottomSheetCallback;
    }

    @SuppressWarnings("unchecked")
    private void extract() {
        if (this.getArguments() != null) {
            CharSequence title = this.getArguments().getCharSequence("TITLE");

            if (title != null) {
                this.txvTitle.setText(title);

                this.txvTitle.setVisibility(View.VISIBLE);
            }

            ArrayList<SpinnerAdapter.Item> items = (ArrayList<SpinnerAdapter.Item>) this.getArguments().getSerializable("ITEMS");

            if (items != null) {
                this.allItems.addAll(items);
                this.reload();
            }
        }
    }

    private void init() {
        new KeyboardUtil(this.appCompatActivity, this.linearLayout);

        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.appCompatActivity, RecyclerView.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.appCompatActivity, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new SpinnerAdapter(this));

        this.searchView.setQueryHint(LocaleUtil.getString(Objects.requireNonNull(getContext()), R.string.search));

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (SpinnerBottomSheet.this.countDownTimer != null) {
                    SpinnerBottomSheet.this.countDownTimer.cancel();
                }

                SpinnerBottomSheet.this.countDownTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        SpinnerBottomSheet.this.searchTerm = newText;
                        SpinnerBottomSheet.this.reload();
                    }
                };

                SpinnerBottomSheet.this.countDownTimer.start();

                return false;
            }
        });
    }

    private void reload() {
        SpinnerAdapter spinnerAdapter = (SpinnerAdapter) this.recyclerView.getAdapter();

        if (spinnerAdapter != null) {
            spinnerAdapter.getItems().clear();

            for (SpinnerAdapter.Item item : this.allItems) {
                spinnerAdapter.getItems().add(item);
            }

            if (spinnerAdapter.getItems().isEmpty()) {
                this.empty();
            } else {
                this.notEmpty();
            }

            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void empty() {
        this.recyclerView.setVisibility(View.GONE);
        this.layoutEmpty.setVisibility(View.VISIBLE);
    }

    private void notEmpty() {
        this.recyclerView.setVisibility(View.VISIBLE);
        this.layoutEmpty.setVisibility(View.GONE);
    }

    public interface SpinnerBottomSheetCallback {
        default void dismiss() {
        }

        void select(SpinnerAdapter.Item selectedItem);
    }
}