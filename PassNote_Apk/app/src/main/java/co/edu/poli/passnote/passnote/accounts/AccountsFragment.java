package co.edu.poli.passnote.passnote.accounts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.edu.poli.passnote.passnote.R;
import co.edu.poli.passnote.passnote.utils.NotificationUtils;

import static co.edu.poli.passnote.passnote.Application.getAppContext;
import static co.edu.poli.passnote.passnote.utils.ImageUtils.getImageIdByName;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;

public class AccountsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AccountItemAdapter.ViewHolder> adapter;
    private List<AccountItem> accountItemList;

    private FirebaseFirestore db;
    private CollectionReference accountsCollection;
    private CollectionReference usersCollection;

    private View fragmentInflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            fragmentInflatedView = inflater.inflate(R.layout.fragment_accounts, container, false);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(e);
        }
        return fragmentInflatedView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            showProgressBar();

            db = FirebaseFirestore.getInstance();
            accountsCollection = db.collection("accounts");
            usersCollection = db.collection("users");

            recyclerView = fragmentInflatedView.findViewById(R.id.accountsRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getAppContext()));
            recyclerView.setHasFixedSize(false);

            loadAccounts();
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
                                accountItemList = new ArrayList<>();
                                for (DocumentSnapshot account : task.getResult()) {
                                    String imageEntryName = account.getString("imageEntryName");
                                    int imageResourceId = getImageIdByName(getAppContext(), imageEntryName);
                                    String text = account.getString("name");
                                    accountItemList.add(new AccountItem(imageResourceId, text));
                                }
                                adapter = new AccountItemAdapter(accountItemList, getAppContext());
                                recyclerView.setAdapter(adapter);
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
        fragmentInflatedView.findViewById(R.id.accountsLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        fragmentInflatedView.findViewById(R.id.accountsLoadingPanel).setVisibility(View.GONE);
    }
}