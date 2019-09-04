package com.ichsanudinstore.loka.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ichsanudinstore.loka.R;
import com.ichsanudinstore.loka.bottomsheet.ConfirmationBottomSheet;
import com.ichsanudinstore.loka.config.Constant;
import com.ichsanudinstore.loka.fragment.ApprovalFragment;
import com.ichsanudinstore.loka.fragment.CategoryOfficeFragment;
import com.ichsanudinstore.loka.fragment.HistoryRentFragment;
import com.ichsanudinstore.loka.fragment.KeeperFragment;
import com.ichsanudinstore.loka.fragment.OfficeFragment;
import com.ichsanudinstore.loka.fragment.ProfileFragment;
import com.ichsanudinstore.loka.fragment.RentFragment;
import com.ichsanudinstore.loka.fragment.SeatFragment;
import com.ichsanudinstore.loka.util.BottomSheetUtil;
import com.ichsanudinstore.loka.util.EntityUtil;
import com.ichsanudinstore.loka.util.FragmentUtil;
import com.ichsanudinstore.loka.util.IntentUtil;
import com.ichsanudinstore.loka.util.LocaleUtil;
import com.ichsanudinstore.loka.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConfirmationBottomSheet.ConfirmationBottomSheetCallback {
    @BindView(R.id.parent_view)
    DrawerLayout layoutParent;
    @BindView(R.id.appbar_view)
    BottomAppBar layoutAppbar;
    @BindView(R.id.nav_view)
    NavigationView layoutNav;
    @BindView(R.id.content_view)
    FrameLayout layoutContent;
    @BindView(R.id.add)
    FloatingActionButton fabAdd;

    public static Fragment current;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        this.initializeUI();
        this.hint();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.layoutParent.isDrawerOpen(GravityCompat.START)) {
            this.layoutParent.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(false);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        this.clearSelection();
        int itemId = menuItem.getItemId();
        menuItem.setChecked(true);
        this.layoutParent.closeDrawer(GravityCompat.START);

        if (itemId == R.id.dashboard_menu_approval) {
            current = new ApprovalFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.GONE);
            return true;
        }

        if (itemId == R.id.dashboard_menu_category_office) {
            current = new CategoryOfficeFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.VISIBLE);
            return true;
        }

        if (itemId == R.id.dashboard_menu_office) {
            current = new OfficeFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.VISIBLE);
            return true;
        }

        if (itemId == R.id.dashboard_menu_keeper) {
            current = new KeeperFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.VISIBLE);
            return true;
        }

        if (itemId == R.id.dashboard_menu_history_rent) {
            current = new HistoryRentFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.GONE);
            return true;
        }


        if (itemId == R.id.dashboard_menu_rent) {
            current = new RentFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.GONE);
            return true;
        }

        if (itemId == R.id.dashboard_menu_seat) {
            current = new SeatFragment();
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, current)) {
                FragmentUtil.replaceFragment(DashboardActivity.this, current, R.id.content_view, true);
            }
            fabAdd.setVisibility(View.VISIBLE);
            return true;
        }

        if (itemId == R.id.dashboard_menu_clear_data || itemId == R.id.dashboard_menu_logout) {
            BottomSheetUtil.confirmation(
                    DashboardActivity.this,
                    LocaleUtil.getString(getApplicationContext(), R.string.warning),
                    LocaleUtil.getString(getApplicationContext(), R.string.message_logout),
                    LocaleUtil.getString(getApplicationContext(), R.string.cancel),
                    LocaleUtil.getString(getApplicationContext(), R.string.yes),
                    DashboardActivity.this
            );
            return true;
        }

        return false;
    }

    @OnClick(R.id.add)
    void onAddItem() {
        if (current instanceof CategoryOfficeFragment) {
            ((CategoryOfficeFragment) current).addCategoryOffice();
        } else if (current instanceof OfficeFragment) {
            Intent intent = IntentUtil.generalGoTo(DashboardActivity.this, CreateUpdateOfficeActivity.class, null, false);
            intent.putExtra("type", (byte) 0);
            IntentUtil.goTo(DashboardActivity.this, intent);
        } else if (current instanceof KeeperFragment) {
            Intent intent = IntentUtil.generalGoTo(DashboardActivity.this, CreateUpdateAccountActivity.class, null, false);
            intent.putExtra("type", (byte) 0);
            IntentUtil.goTo(DashboardActivity.this, intent);
        } else if (current instanceof SeatFragment) {
            Intent intent = IntentUtil.generalGoTo(DashboardActivity.this, CreateUpdateSeatActivity.class, null, false);
            intent.putExtra("type", (byte) 0);
            IntentUtil.goTo(DashboardActivity.this, intent);
        }
    }

    @Override
    public void negative() {

    }

    @Override
    public void positive() {
        EntityUtil.deleteAll(realm);
        IntentUtil.goTo(DashboardActivity.this, IntentUtil.generalGoTo(DashboardActivity.this, LoginActivity.class, null, true));
    }

    @SuppressLint("RestrictedApi")
    private void initializeUI() {
        this.realm = Realm.getDefaultInstance();

        this.setSupportActionBar(layoutAppbar);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.layoutParent, this.layoutAppbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        this.layoutParent.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        this.layoutNav.setNavigationItemSelectedListener(this);

        NavigationHeaderView navigationHeaderView = new NavigationHeaderView(this.layoutNav.getHeaderView(0));

        navigationHeaderView.btnProfile.setText(LocaleUtil.getString(getApplicationContext(), R.string.profile));
        navigationHeaderView.txvName.setText(SharedPreferencesUtil.get(Constant.SharedPreferenceKey.NAME, String.class));
        navigationHeaderView.txvEmail.setText(SharedPreferencesUtil.get(Constant.SharedPreferenceKey.EMAIL, String.class));

        if (SharedPreferencesUtil.get(Constant.SharedPreferenceKey.ROLE_NAME, String.class) != null) {
            if (SharedPreferencesUtil.get(Constant.SharedPreferenceKey.ROLE_NAME, String.class).equals(Constant.Privileges.admin.toString())) {
                this.layoutNav.getMenu().findItem(R.id.admin).setVisible(true);
                this.layoutNav.getMenu().findItem(R.id.owner).setVisible(false);
                current = new ApprovalFragment();
                this.layoutNav.getMenu().findItem(R.id.dashboard_menu_rent).setVisible(false);
                this.layoutNav.getMenu().findItem(R.id.dashboard_menu_approval).setChecked(true);
                FragmentUtil.addFragment(this, current, R.id.content_view, true);

                fabAdd.setVisibility(View.GONE);
            }

            if (SharedPreferencesUtil.get(Constant.SharedPreferenceKey.ROLE_NAME, String.class).equals(Constant.Privileges.owner.toString())) {
                this.layoutNav.getMenu().findItem(R.id.admin).setVisible(false);
                this.layoutNav.getMenu().findItem(R.id.owner).setVisible(true);
                current = new KeeperFragment();
                this.layoutNav.getMenu().findItem(R.id.dashboard_menu_keeper).setChecked(true);
                FragmentUtil.addFragment(this, current, R.id.content_view, true);

                fabAdd.setVisibility(View.VISIBLE);
            }

            if (SharedPreferencesUtil.get(Constant.SharedPreferenceKey.ROLE_NAME, String.class).equals(Constant.Privileges.keeper.toString())) {
                this.layoutNav.getMenu().findItem(R.id.admin).setVisible(false);
                this.layoutNav.getMenu().findItem(R.id.owner).setVisible(false);
                current = new RentFragment();
                this.layoutNav.getMenu().findItem(R.id.dashboard_menu_seat).setChecked(true);
                FragmentUtil.addFragment(this, current, R.id.content_view, true);
            }
        }
    }

    private void clearSelection() {
        for (int i = 0; i < this.layoutNav.getMenu().size(); i++) {
            this.layoutNav.getMenu().findItem(this.layoutNav.getMenu().getItem(i).getItemId()).setChecked(false);
        }
    }

    private void hint() {
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_approval).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.dashboard_approval));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_category_office).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.dashboard_category_office));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_office).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.dashboard_office));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_keeper).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.dashboard_keeper));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_rent).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.dashboard_rent));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_seat).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.dashboard_seat));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_clear_data).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.clear_data));
        this.layoutNav.getMenu().findItem(R.id.dashboard_menu_logout).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.logout));

        this.layoutNav.getMenu().findItem(R.id.admin).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.admin));
        this.layoutNav.getMenu().findItem(R.id.owner).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.owner));
        this.layoutNav.getMenu().findItem(R.id.general).setTitle(LocaleUtil.getString(getApplicationContext(), R.string.general));
    }

    class NavigationHeaderView {
        @BindView(R.id.name)
        public AppCompatTextView txvName;
        @BindView(R.id.mail)
        public AppCompatTextView txvEmail;
        @BindView(R.id.profile)
        public MaterialButton btnProfile;

        NavigationHeaderView(View view) {
            ButterKnife.bind(this, view);
        }

        @SuppressLint("RestrictedApi")
        @OnClick(R.id.profile)
        public void profile() {
            if (!FragmentUtil.isSameFragment(DashboardActivity.this, new ProfileFragment())) {
                FragmentUtil.replaceFragment(DashboardActivity.this, new ProfileFragment(), R.id.content_view, true);
                layoutParent.closeDrawer(GravityCompat.START);

                fabAdd.setVisibility(View.GONE);
            }
        }
    }
}
