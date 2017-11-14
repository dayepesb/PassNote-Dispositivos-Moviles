package co.edu.poli.passnote.passnote;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import co.edu.poli.passnote.passnote.accounts.AccountsFragment;
import co.edu.poli.passnote.passnote.notes.NotesFragment;
import co.edu.poli.passnote.passnote.reminders.ReminderFragment;

import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;

public class MainNavigationActivity extends AppCompatActivity implements ToggleHamburguerBackIcons, FragmentManager.OnBackStackChangedListener {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavigationDrawer;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;

    private int lastMenuSelection;

    private boolean isSearchable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_navigation);

            assignMemberVariables();
            setSupportActionBar(mToolbar);
            mActionBarDrawerToggle = createActionBarDrawerToogle();
            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            setupDrawerContent(mNavigationDrawer);
            getSupportFragmentManager().addOnBackStackChangedListener(this);
            disableAutoFill();

            if (savedInstanceState == null) {
                selectAccounts();
            }
        } catch (Exception e) {
            showGeneralError(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutoFill() {
        if (Build.VERSION.SDK_INT >= 26) {
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    private void assignMemberVariables() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.mainNavigationDrawerLayout);
        mNavigationDrawer = findViewById(R.id.mainNavigationDrawer);
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
        lastMenuSelection = menuItem.getItemId();
        switch (lastMenuSelection) {
            case R.id.nav_first_fragment:
                fragmentClass = AccountsFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = NotesFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = ReminderFragment.class;
                break;
            default:
                fragmentClass = AccountsFragment.class;
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        showFragment(fragmentClass);

        showHamburgerIcon();
    }

    public void showFragment(Class fragmentClass) {
        showFragment(fragmentClass, null);
    }

    public void showFragment(Class fragmentClass, Bundle arguments) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            if (arguments != null) {
                fragment.setArguments(arguments);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainNavigationContent, fragment)
                .addToBackStack(fragment.getClass().getName()).commit();
        mDrawerLayout.closeDrawers();
    }

    private void selectAccounts() {
        selectMenu(AccountsFragment.class, R.string.drawer_accounts);
    }

    private <T extends Fragment> void selectMenu(Class<T> fragmentClass, int titleId) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainNavigationContent, fragment).commit();
        setTitle(titleId);
        mDrawerLayout.closeDrawers();
    }

    private ActionBarDrawerToggle createActionBarDrawerToogle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        try {
            super.onPostCreate(savedInstanceState);
            mActionBarDrawerToggle.syncState();
        } catch (Exception e) {
            showGeneralError(e);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            mActionBarDrawerToggle.onConfigurationChanged(newConfig);
        } catch (Exception e) {
            showGeneralError(e);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            mSearchAction = menu.findItem(R.id.action_search);
        } catch (Exception e) {
            showGeneralError(e);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void handleMenuSearch() {
//        ActionBar action = getSupportActionBar();
//        if (isSearchOpened) {
//            action.setDisplayShowCustomEnabled(false);
//            action.setDisplayShowTitleEnabled(true);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
//            mSearchAction.setIcon(ContextCompat.getDrawable(MainNavigationActivity.this,
//                    R.drawable.action_search));
//            isSearchOpened = false;
//        } else {
//            action.setDisplayShowCustomEnabled(true);
//            action.setCustomView(R.layout.search_bar);
//            action.setDisplayShowTitleEnabled(false);
//            edtSeach = action.getCustomView().findViewById(R.id.edtSearch); //the text editor
//            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                        doSearch();
//                        return true;
//                    }
//                    return false;
//                }
//            });
//            edtSeach.requestFocus();
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
//            mSearchAction.setIcon(ContextCompat.getDrawable(MainNavigationActivity.this
//                    , R.drawable.ic_close_black_24dp));
//            isSearchOpened = true;
//        }
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
            showGeneralError(e);
        }
    }

    private void doSearch() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            if (!this.isSearchable) {
                searchItem.setVisible(false);
            } else {
                searchItem.setVisible(true);
            }
            SearchView searchView = (SearchView) searchItem.getActionView();

            // configure the search info and add any event listener

        } catch (Exception e) {
            showGeneralError(e);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            int id = item.getItemId();

            switch (id) {
                case android.R.id.home:
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack();
                    }
                    return true;
                case R.id.action_settings:
                    return true;
                case R.id.action_search:
                    //handleMenuSearch();
                    return true;
            }
        } catch (Exception e) {
            showGeneralError(e);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showHamburgerIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public void showBackIcon() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager
                    .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            String fragmentName = backStackEntry.getName();

            if (AccountsFragment.class.getName().equals(fragmentName) ||
                    NotesFragment.class.getName().equals(fragmentName) ||
                    ReminderFragment.class.getName().equals(fragmentName)) {
                this.isSearchable = true;
                showHamburgerIcon();
            } else {
                this.isSearchable = false;
                showBackIcon();
            }
        } else {
            showHamburgerIcon();
        }
        invalidateOptionsMenu();
    }
}