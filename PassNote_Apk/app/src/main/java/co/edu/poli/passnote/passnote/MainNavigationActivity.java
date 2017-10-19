package co.edu.poli.passnote.passnote;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import co.edu.poli.passnote.passnote.accounts.AccountsFragment;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;

public class MainNavigationActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_navigation);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mDrawer = findViewById(R.id.mainNavigationDrawerLayout);
            drawerToggle = setupDrawerToggle();
            mDrawer.addDrawerListener(drawerToggle);
            nvDrawer = findViewById(R.id.mainNavigationDrawer);
            setupDrawerContent(nvDrawer);
            selectAccounts();
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            int id = item.getItemId();

            switch (id) {
                case R.id.action_settings:
                    return true;
                case R.id.action_search:
                    handleMenuSearch();
                    return true;
            }
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
        View headerLayout = navigationView.getHeaderView(0);
        TextView drawerHeaderName = headerLayout.findViewById(R.id.drawerHeaderName);
        String currentUserFullName = FirebaseAuth.getInstance().getCurrentUser()
                .getDisplayName();
        drawerHeaderName.setText(currentUserFullName);
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = AccountsFragment.class;
                break;
            default:
                fragmentClass = AccountsFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainNavigationContent, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    private void selectAccounts() {
        Fragment fragment = null;
        Class fragmentClass = AccountsFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainNavigationContent, fragment).commit();
        setTitle(R.string.drawer_accounts);
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,
                R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        try {
            super.onPostCreate(savedInstanceState);
            drawerToggle.syncState();
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            drawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            mSearchAction = menu.findItem(R.id.action_search);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void handleMenuSearch() {
        ActionBar action = getSupportActionBar();
        if (isSearchOpened) {
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
            mSearchAction.setIcon(ContextCompat.getDrawable(MainNavigationActivity.this,
                    R.drawable.action_search));
            isSearchOpened = false;
        } else {
            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search_bar);
            action.setDisplayShowTitleEnabled(false);
            edtSeach = action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });
            edtSeach.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
            mSearchAction.setIcon(ContextCompat.getDrawable(MainNavigationActivity.this
                    , R.drawable.close));
            isSearchOpened = true;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (isSearchOpened) {
                handleMenuSearch();
                return;
            }
            super.onBackPressed();
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
    }

    private void doSearch() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.main, menu);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(this, e);
        }
        return true;
    }
}