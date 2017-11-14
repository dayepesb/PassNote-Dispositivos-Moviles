package co.edu.poli.passnote.passnote.accounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.passnote.passnote.MainNavigationActivity;
import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;

import static co.edu.poli.passnote.passnote.Application.getAppContext;
import static co.edu.poli.passnote.passnote.utils.ImageUtils.getImageIdByName;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AccountsFragment extends Fragment {
    public static final String KEY_SELECTED_ACCOUNT_ID = "UPDATE";

    private RecyclerView mRecyclerView;
    private AccountItemAdapter mAdapter;
    private List<AccountItem> mAccountItems;
    private View mFragmentInflatedView;
    private int mSelectedAccountPosition;

    private FirebaseFirestore db;
    private CollectionReference accountsCollection;
    private CollectionReference usersCollection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            mFragmentInflatedView = inflater.inflate(R.layout.fragment_accounts, container, false);
            FloatingActionButton addAccountBtn = mFragmentInflatedView.findViewById(R.id.accountsFab);
            addAccountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFragment(SaveAccountFragment.class);
                }
            });
        } catch (Exception e) {
            NotificationUtils.showGeneralError(e);
        }

        return mFragmentInflatedView;
    }

    private void showFragment(Class fragment) {
        showFragment(fragment, null);
    }

    private void showFragment(Class fragment, Bundle bundle) {
        MainNavigationActivity parentActivity =
                (MainNavigationActivity) AccountsFragment.this.getActivity();
        parentActivity.showFragment(fragment, bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            showProgressBar();

            db = FirebaseFirestore.getInstance();
            accountsCollection = db.collection("accounts");
            usersCollection = db.collection("users");

            mRecyclerView = mFragmentInflatedView.findViewById(R.id.accountsRecyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getAppContext()));
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.addOnItemTouchListener(new AccountItemClickListener(getActivity(), mRecyclerView, new AccountItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mSelectedAccountPosition = position;
                    mRecyclerView.showContextMenuForChild(view);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    // TODO add context menu with copy username and copy password
                }
            }));
            registerForContextMenu(mRecyclerView);
            accountsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    loadAccounts();
                }
            });
        } catch (Exception e) {
            NotificationUtils.showGeneralError(e);
        }
    }

    private void loadAccounts() {
        findCurrentUserId(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String userId = "";
                    for (DocumentSnapshot user : task.getResult()) {
                        userId = user.getId();
                        break;
                    }
                    findAccounts(userId, new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                mAccountItems = new ArrayList<>();
                                for (DocumentSnapshot account : task.getResult()) {
                                    AccountItem accountItem = account.toObject(AccountItem.class);
                                    accountItem.setId(account.getId());
                                    int imageResourceId = 0;
                                    if (isNotBlank(accountItem.getImageEntryName())) {
                                        String imageEntryName = accountItem.getImageEntryName();
                                        imageResourceId = getImageIdByName(getAppContext(), imageEntryName);
                                        accountItem.setLocalImageId(imageResourceId);
                                    }
                                    mAccountItems.add(accountItem);
                                }
                                mAdapter = new AccountItemAdapter(mAccountItems, getActivity());
                                mRecyclerView.setAdapter(mAdapter);
                                hideProgressBar();
                            } else {
                                hideProgressBar();
                                showGeneralError();
                            }
                        }
                    });

                } else {
                    hideProgressBar();
                    showGeneralError();
                }
            }
        });
    }

    private void findCurrentUserId(OnCompleteListener<QuerySnapshot> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserEmail = user.getEmail();
            usersCollection
                    .whereEqualTo("email", currentUserEmail)
                    .get()
                    .addOnCompleteListener(callback);
        }
    }

    private void findAccounts(String userId, OnCompleteListener<QuerySnapshot> callback) {
        accountsCollection
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(callback);
    }

    private void showProgressBar() {
        mFragmentInflatedView.findViewById(R.id.accountsLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mFragmentInflatedView.findViewById(R.id.accountsLoadingPanel).setVisibility(View.GONE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = mAdapter.getPosition();
        } catch (Exception e) {
            showGeneralError();
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.accountsContextMenuEdit:
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SELECTED_ACCOUNT_ID,
                        AccountsFragment.this.mAccountItems.get(mSelectedAccountPosition).getId());
                showFragment(SaveAccountFragment.class, bundle);
                return true;
            case R.id.accountsContextMenuCopyUsername:
                return super.onContextItemSelected(item);
            case R.id.accountsContextMenuCopyPassword:
                return super.onContextItemSelected(item);
            case R.id.accountsContextMenuCopyURL:
                return super.onContextItemSelected(item);
            case R.id.accountsContextMenuDelete:
                return super.onContextItemSelected(item);
            default:
                return super.onContextItemSelected(item);
        }
    }
}