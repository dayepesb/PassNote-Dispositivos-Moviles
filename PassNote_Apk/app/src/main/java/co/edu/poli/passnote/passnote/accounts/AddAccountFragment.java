package co.edu.poli.passnote.passnote.accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import co.edu.poli.passnote.passnote.MainNavigationActivity;
import co.edu.poli.passnote.passnote.R;

import static co.edu.poli.passnote.passnote.utils.EncryptionUtils.encrypt;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showGeneralError;
import static co.edu.poli.passnote.passnote.utils.NotificationUtils.showNotification;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class AddAccountFragment extends Fragment {

    private View fragmentInflatedView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference accountsCollRef = db.collection("accounts");
    private CollectionReference usersCollRef = db.collection("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.fragmentInflatedView = inflater.inflate(R.layout.fragment_add_account, container, false);

            Button saveButton = this.fragmentInflatedView.findViewById(R.id.addAccountSaveBtn);
            saveButton.setOnClickListener(new AddAccountButtonListener());

            Button cancelButton = this.fragmentInflatedView.findViewById(R.id.addAccountCancelBtn);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToHome();
                }
            });
            return this.fragmentInflatedView;
        } catch (Exception e) {
            showGeneralError(e);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class AddAccountButtonListener implements View.OnClickListener {
        private String websiteName;
        private String URL;
        private String username;
        private String password;

        @Override
        public void onClick(View view) {
            readParameters();

            if (validateParameters()) {

                findCurrentUserId(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String userId = "";
                            for (DocumentSnapshot user : task.getResult()) {
                                userId = user.getId();
                                break;
                            }
                            AccountItem newAccount = new AccountItem();
                            newAccount.setImageEntryName("passnotelogowhite");
                            newAccount.setName(AddAccountButtonListener.this.websiteName);
                            newAccount.setURL(AddAccountButtonListener.this.URL);
                            newAccount.setUsername(AddAccountButtonListener.this.username);
                            newAccount.setPassword(encrypt(AddAccountButtonListener.this.password));
                            newAccount.setUserId(userId);

                            AddAccountFragment.this.accountsCollRef.document().set(newAccount)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showNotification(R.string.addAccountDataSavedOK);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showGeneralError();
                                }
                            });


                            goToHome();
                        }
                    }
                });
            }
        }

        private void readParameters() {
            TextView accountNameTextView = getActivity().findViewById(R.id.addAccountName);
            TextView accountUsernameTextView = getActivity().findViewById(R.id.addAccountUsername);
            TextView accountURLTextView = getActivity().findViewById(R.id.addAccountURL);
            TextView accountPasswordTextView = getActivity().findViewById(R.id.addAccountPassword);

            this.websiteName = accountNameTextView.getText().toString();
            this.URL = accountURLTextView.getText().toString();
            this.username = accountUsernameTextView.getText().toString();
            this.password = accountPasswordTextView.getText().toString();
        }

        private boolean validateParameters() {
            if (isBlank(this.websiteName)) {
                showNotification(R.string.addAccountSiteNameRequired);
                return false;
            }
            if (isBlank(this.URL)) {
                showNotification(R.string.addAccountURLRequired);
                return false;
            }
            if (isBlank(this.username)) {
                showNotification(R.string.addAccountUsernameRequired);
                return false;
            }
            if (isBlank(this.password)) {
                showNotification(R.string.addAccountPasswordRequired);
                return false;
            }

            return true;
        }
    }

    private void goToHome() {
        MainNavigationActivity activity = (MainNavigationActivity) getActivity();
        activity.showFragment(AccountsFragment.class);
    }

    private void findCurrentUserId(OnCompleteListener<QuerySnapshot> callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String currentUserEmail = user.getEmail();
            usersCollRef
                    .whereEqualTo("email", currentUserEmail)
                    .get()
                    .addOnCompleteListener(callback);
        }
    }
}
