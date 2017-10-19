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

import static co.edu.poli.passnote.passnote.utils.ImageUtils.getImageIdByName;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;

public class AccountsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AccountItemAdapter.ViewHolder> adapter;
    private List<AccountItem> accountItemList;

    private FirebaseFirestore db;
    private CollectionReference accountsCollection;
    private CollectionReference usersCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_accounts, container, false);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(getContext(), e);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        try {
            showProgressBar();
            db = FirebaseFirestore.getInstance();
            accountsCollection = db.collection("accounts");
            usersCollection = db.collection("users");

            recyclerView = getActivity().findViewById(R.id.accountsRecyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(false);

            loadAccounts();
            super.onActivityCreated(savedInstanceState);
        } catch (Exception e) {
            NotificationUtils.showGeneralError(getContext(), e);
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
                                    int imageResourceId = getImageIdByName(getContext(), imageEntryName);
                                    String text = account.getString("name");
                                    accountItemList.add(new AccountItem(imageResourceId, text));
                                }
                                adapter = new AccountItemAdapter(accountItemList, getContext());
                                recyclerView.setAdapter(adapter);
                                hideProgressBar();
                            } else {
                                hideProgressBar();
                                showGeneralError(getContext());
                            }
                        }
                    });

                } else {
                    hideProgressBar();
                    showGeneralError(getContext());
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
        getActivity().findViewById(R.id.accountsLoadingPanel).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        getActivity().findViewById(R.id.accountsLoadingPanel).setVisibility(View.GONE);
    }
}